package com.sprout.web.base;

import com.sprout.core.jpa.entity.BaseEntity;
import com.sprout.core.service.BaseService;
import com.sprout.web.datatable.DataTablePage;
import com.sprout.web.datatable.DataTableParams;
import com.sprout.web.util.RestResult;
import com.sprout.web.util.WebMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

/**
 * 系统默认实现增删改查基础controller
 * @param <T> 对应实体对象
 * @param <PK> 实体主键
 */
public class BaseCrudController<T extends BaseEntity<PK>, PK extends Serializable> extends BaseController {

    /**
     * 模块英文名称
     */
    private String moduleName;

    /**
     * 子功能英文名称
     */
    private String name;

    /**
     * 子功能中文说明
     */
    private String cname;

    /**
     * 对应业务类 具体在子类注入
     */
    private BaseService<T, PK> service;

    public BaseCrudController(String moduleName, String name, String cname, BaseService<T, PK> service) {
        this.moduleName = moduleName;
        this.name = name;
        this.cname = cname;
        this.service = service;
    }

    public BaseService<T, PK> getService() {
        return this.service;
    }

    @GetMapping(value = "view")
    public String list(Model model, HttpServletRequest request) {
        setModel(model, request);
        return moduleName + "/" + name + "/" + "list";
    }

    /**
     * 分页查询
     * <pre>
     *     <strong>说明:</strong> 针对每个子类具体分页参数处理重写{@link #setPageQueryVariables(Map, HttpServletRequest)}方法
     * </pre>
     * @param dataTableParams 查询参数
     * @param request
     * @return 查询结果分页对象
     */
    @PostMapping(value = "search")
    @ResponseBody
    public DataTablePage search(DataTableParams dataTableParams, HttpServletRequest request) {
        PageRequest p = dataTableParams.getPageRequest();
        setPageQueryVariables(dataTableParams.getQueryParams(), request);
        Page<T> list = this.service.findPage(p, dataTableParams.getQueryParams());
        return DataTablePage.generateDataTablePage(list, dataTableParams);
    }

    @GetMapping(value = "add")
    public String add(Model model, HttpServletRequest request) {
        setModel(model, request);
        return moduleName + "/" + name + "/" + "add";
    }

    @PostMapping(value = "save")
    @ResponseBody
    public RestResult save(T t, HttpServletRequest request) {
        try {
            this.service.save(t);
            logger.debug("保存/更新{}成功,{}", cname, t);
            return RestResult.createSuccessResult("保存成功");
        } catch (Exception e) {
            logger.error("保存/更新{}失败,{},error={}", cname, t, e);
            return RestResult.createErrorResult("保存失败【"+e.getMessage() + "】");
        }
    }

    @GetMapping(value = "edit/{id}")
    public String edit(@PathVariable PK id, Model model, HttpServletRequest request) {
        T t = this.service.findById(id);
        model.addAttribute(name, t);
        setModel(model, request);
        return moduleName + "/" + name + "/" + "edit";
    }

    @PostMapping(value = "delete/{ids}")
    @ResponseBody
    public RestResult delete(@PathVariable("ids") PK[] ids, HttpServletRequest request) {
        try {
            this.service.deleteIds(ids);
            logger.debug("删除{}成功,ids=[{}]", cname, ids);
            return RestResult.createSuccessResult("用户删除成功");
        } catch (Exception e) {
            logger.debug("删除{}失败,ids=[{}], error={}", cname, ids, e);
            return RestResult.createErrorResult("用户删除失败【"+e.getMessage() + "】");
        }
    }

    /**
     * 分页参数处理
     * <pre>
     *     对一些需要把接受到的参数数据进一步处理的需要，由字类重写该方法
     * </pre>
     * @param queryVariables 分页参数
     * @param request HttpServletRequest
     */
    protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
        //默认不实现 交由子类完成
    }

    /**
     * 页面参数接收赋值
     * @param model Model
     * @param request HttpServletRequest
     */
    protected void setModel(Model model, HttpServletRequest request) {
        model.addAttribute("cname", cname);
        model.addAttribute("name", name);
    }
}
