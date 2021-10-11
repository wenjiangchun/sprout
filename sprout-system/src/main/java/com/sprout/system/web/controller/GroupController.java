package com.sprout.system.web.controller;

import java.util.*;

import com.sprout.system.entity.Dict;
import com.sprout.system.entity.Group;
import com.sprout.system.service.DictService;
import com.sprout.system.service.GroupService;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.tree.TreeNode;
import com.sprout.web.util.RestResult;
import com.sprout.web.util.WebMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 组织机构Controller
 *
 * @author wenjiangchun
 */
@Controller
@RequestMapping(value = "/system/group")
public class GroupController extends BaseCrudController<Group, Long> {

    private GroupService groupService;

    @Autowired
    private DictService dictService;

    public GroupController(GroupService groupService) {
        super("system", "group", "机构信息", groupService);
        this.groupService = groupService;
    }

    @Override
    protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
        if (queryVariables.get("parent.id") != null) {
            Group g = new Group();
            g.setId(Long.valueOf(queryVariables.get("parent.id").toString()));
            queryVariables.put("parent.id", Long.valueOf(queryVariables.get("parent.id").toString()));
        } else {
            queryVariables.put("parent_isNull", null); //默认查询顶级字典列表
        }
        if (queryVariables.get("groupType.id") != null) {
            String value = (String) queryVariables.get("groupType.id");
            queryVariables.put("groupType.id", Long.valueOf(value));
        }
    }

    @Override
    protected void setModel(Model model, HttpServletRequest request) {
        List<Dict> groupTypeList = this.dictService.findChildsByRootCode(Group.GROUP_TYPE);
        model.addAttribute("groupTypeList", groupTypeList);
        String parentId = request.getParameter("parentId");
        if (parentId != null) {
            model.addAttribute("parent", groupService.findById(Long.parseLong(parentId)));
        }
    }

    @RequestMapping(value = "getTopGroups")
    @ResponseBody
    public List<Group> getTopGroups() {
        List<Group> groups = groupService.findAll();
        Set<Group> newGroup = new HashSet<>();
        Group root = new Group();
        root.setFullName("组织机构树");
        for (Group g : groups) {
            g.setUsers(null);
            g.setChilds(null);
            g.setGroupType(null);
            if (g.getPid() == null) {
                g.setParent(root);
            }
            newGroup.add(g);
        }
        newGroup.add(root);
        return new ArrayList<>(newGroup);
    }

    @Override
    @GetMapping(value = "add")
    public String add(Model model, HttpServletRequest request) {
        String parentId = request.getParameter("parentId");
        if (parentId != null) {
            Group parent = this.groupService.findById(Long.parseLong(parentId));
            model.addAttribute("parent", parent);
            model.addAttribute("parentId", parentId);
        }
        List<Dict> groupTypeList = this.dictService.findChildsByRootCode(Group.GROUP_TYPE);
        model.addAttribute("groupTypeList", groupTypeList);
        return "system/group/add";
    }

    @Override
    @PostMapping(value = "save")
    @ResponseBody
    public RestResult save(Group group, HttpServletRequest request) {
        try {
            Date date = new Date();
			/*group.setUpdateTime(date);
            if (group.isNew()) {
            	group.setCreateTime(date);
			}*/
            this.groupService.save(group);
            logger.debug("机构添加/更新成功, group={}", group);
            return RestResult.createSuccessResult("保存成功");
        } catch (Exception e) {
            logger.error("机构添加/更新失败", e);
            return RestResult.createErrorResult("保存失败【"+e.getMessage() + "】");
        }
    }

    @PostMapping(value = "update")
    @ResponseBody
    public WebMessage update(Group group) {
        try {
            this.groupService.save(group);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }

    @PostMapping(value = "getTreeNode")
    @ResponseBody
    public List<TreeNode> getTreeNode() {
        return this.groupService.getTreeNode();
    }

}
