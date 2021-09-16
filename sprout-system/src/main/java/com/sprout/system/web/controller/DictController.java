package com.sprout.system.web.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sprout.system.entity.Dict;
import com.sprout.system.service.DictService;
import com.sprout.web.base.BaseCrudController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * 字典对象Controller
 * 
 * @author sofar
 */
@Controller
@RequestMapping(value = "/system/dict")
public class DictController extends BaseCrudController<Dict, Long> {

	private DictService dictService;

	public DictController(DictService dictService) {
		super("system", "dict", "字典信息", dictService);
		this.dictService = dictService;
	}

	@Override
	protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
		if(queryVariables.containsKey("parent.id") && queryVariables.get("parent.id") != null){ //查询parent字典下面的所有子字典列表
			Dict d = new Dict();
			d.setId(Long.valueOf(queryVariables.get("parent.id").toString()));
			queryVariables.put("parent.id", Long.valueOf(queryVariables.get("parent.id").toString()));
		} else {
			queryVariables.put("parent_isNull", null); //默认查询顶级字典列表
		}
		if ( queryVariables.get("isEnabled") != null) {
			String value = (String) queryVariables.get("isEnabled");
			queryVariables.put("isEnabled", Boolean.valueOf(value));
		};
	}

	@Override
	protected void setModel(Model model, HttpServletRequest request) {
		super.setModel(model, request);
		String parentId = request.getParameter("parentId");
		if(StringUtils.isNotBlank(parentId)){
			model.addAttribute("parentId", parentId);
		}
	}

	@PostMapping(value = "getDictionaryTree")
	@ResponseBody
	public List<Dict> getDictionaryTree() {
		List<Dict> dicts =  dictService.findAll();
		Set<Dict> newDict = new HashSet<Dict>();
		Dict root = new Dict();
		root.setName("字典树");
		for (Dict g : dicts) {
			g.setChilds(null);
			if (g.getPid() == null) {
				g.setParent(root);
			}
			newDict.add(g);
		}
		newDict.add(root);
		if(dicts.size() > 0) {
			newDict.add(root);
		}
		return new ArrayList<Dict>(newDict);
	}

	@Override
	@GetMapping(value = "add")
	public String add(Model model, HttpServletRequest request) {
		String parentId = request.getParameter("parentId");
		if (parentId != null) {
			Dict parent = this.dictService.findById(Long.parseLong(parentId));
			model.addAttribute("parent", parent);
			model.addAttribute("num", parent.getChilds().size() + 1);
		} else {
			List<Dict> roots = this.dictService.findByProperty("parent", null);
			model.addAttribute("num", roots.size() + 1);
		}
		return "system/dict/add";
	}
	

	/**
	 * 设置字典启用状态
	 * @param id 字典Id
	 * @param redirectAttributes
	 * @return
	 */
	@GetMapping(value = "updateDictionaryIsEnabled/{id}")
	public String updateDictionaryIsEnabled(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		Dict dict = this.dictService.findById(id);
		this.dictService.updateDictionaryIsEnabled(dict, !dict.getEnabled());
		if (dict.getParent() != null) {
			redirectAttributes.addFlashAttribute("parentId", dict.getParent().getId());
		}
		return "redirect:/system/dict/view";
	}
	
	/**
	 * 判断字典代码是否存在  
	 * @param parentId 字典所在节点Id
	 * @param code 字典代码
	 * @return true/false
	 */
	@PostMapping(value = "isNotExistCode")
	@ResponseBody
	public Boolean isNotExistCode(@RequestParam(required=false)Long parentId, String code) {
		String pa = null;
		String[] codes = code.split(",");
		if (codes.length >= 2) {
			pa = codes[1];
		}
		Boolean isExist = this.dictService.isExistDictionaryCode(parentId, codes[0]);
		return !isExist;
	}
	
	/**
	 * 根据代码名称获取代码字典中所有子字典
	 * @param codeName 字典代码名称
	 * @return List<Dictionary>
	 */
	@GetMapping(value = "getChilds/{codeName}")
	@ResponseBody
	public List<Dict> getChilds(@PathVariable String codeName) {
		return this.dictService.findChildsByRootCode(codeName);
	}
}
