package com.sprout.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.system.dao.GroupDao;
import com.sprout.system.entity.Group;
import com.sprout.system.entity.User;
import com.sprout.system.utils.Status;
import com.sprout.web.tree.TreeNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织机构业务操作类
 *
 */
@Service
@Transactional
public class GroupService extends AbstractBaseService<Group, Long> {

    private GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		super(groupDao);
		this.groupDao = groupDao;
	}

    @Transactional(readOnly = true)
    public List<Group> getTopGroups() {
        return groupDao.getTopGroups();
    }

    /**
     * 获取系统中状态为启用的机构和该机构下状态为启用的用户树节点信息
     *
     * @return List 机构和树节点信息列表
     */
	@Transactional(readOnly = true)
    public List<TreeNode> getTreeNode() {
        List<Group> groups = this.findByProperty("status", Status.ENABLE);
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Group g : groups) {
			getGroupTreeNode(g, treeNodes, false);
		}
        return treeNodes;
    }

	@Transactional(readOnly = true)
    public List<TreeNode> getTreeNode(Long groupId) {
        Group g = this.findById(groupId);
        List<TreeNode> treeNodes = new ArrayList<>();
		getGroupTreeNode(g, treeNodes, true);
		return treeNodes;
    }

	private void getGroupTreeNode(Group g, List<TreeNode> treeNodes, boolean open) {
		TreeNode treeNode = new TreeNode();
		treeNode.setId(g.getId());
		treeNode.setName(g.getFullName());
		treeNode.setParentId(g.getPid());
		treeNode.setOpen(open);
		treeNode.setNodeType("group");
		Set<User> users = g.getUsers();
		for (User u : users) {
			if (u.getStatus() == Status.ENABLE) {
				TreeNode treeNode1 = new TreeNode();
				treeNode1.setId(u.getId());
				treeNode1.setName(u.getName());
				treeNode1.setParentId(g.getId());
				treeNodes.add(treeNode1);
				treeNode1.setNodeType("user");
			}
		}
		treeNodes.add(treeNode);
	}

	/**
	 * 根据顶级机构代码查询机构信息
	 * @param rootCode 顶级机构代码
	 * @return 机构信息
	 */
	public Group getRootGroup(String rootCode) {
		return groupDao.getRootGroup(rootCode);
	}

	/**
	 * 根据顶级机构代码查询该机构下所有启用的机构信息
	 * @param rootCode 顶级机构代码
	 * @return 返回包含顶级机构的所有机构列表
	 */
	public List<Group> getEnabledGroups(String rootCode) {
		List<Group> groupList = new ArrayList<>();
		Group root = this.getRootGroup(rootCode);
		if (root == null || root.getStatus() != Status.ENABLE) {
			return groupList;
		}
		//查询节点下面所有子节点
		getChildsGroup(root, groupList);
		return groupList;
	}

	/**
	 * 遍历循环机构下所有启用机构信息
	 * @param parent 所在机构节点
	 * @param groupList 返回包含机构本身以及子机构信息的机构列表
	 */
	private void getChildsGroup(Group parent, List<Group> groupList) {
		if (parent.getStatus() == Status.ENABLE) {
			groupList.add(parent);
			for (Group child : parent.getChilds()) {
				getChildsGroup(child, groupList);
			}
		}
	}
}
