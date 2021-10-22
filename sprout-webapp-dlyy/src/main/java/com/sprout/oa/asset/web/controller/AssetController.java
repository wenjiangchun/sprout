package com.sprout.oa.asset.web.controller;

import com.sprout.oa.asset.entity.Asset;
import com.sprout.oa.asset.entity.AssetIn;
import com.sprout.oa.asset.service.AssetService;
import com.sprout.system.entity.Dict;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.spi.ResolveResult;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/oa/asset")
public class AssetController extends BaseCrudController<Asset, Long> {

    private AssetService assetService;

    public AssetController(AssetService assetService) {
        super("oa", "asset", "资产", assetService);
        this.assetService = assetService;
    }

    @Override
    protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {

    }

    @Override
    protected void setModel(Model model, HttpServletRequest request) {
        super.setModel(model, request);
    }


    @GetMapping("addAssetIn")
    public String addAssetIn(Model model) {
        List<Dict> brandList = assetService.getAssetBrandList();
        List<Dict> classifyList = assetService.getAssetClassifyList();
        List<Dict> modelList = assetService.getAssetModelList();
        List<Dict> unitList = assetService.getAssetUnitList();
        model.addAttribute("brandList", brandList);
        model.addAttribute("classifyList", classifyList);
        model.addAttribute("modelList", modelList);
        model.addAttribute("unitList", unitList);
        return "oa/asset/addAssetIn";
    }

    @PostMapping("saveAssetIn")
    @ResponseBody
    public RestResult addAssetIn(Model model, AssetIn assetIn) {
        this.assetService.saveAssetIn(assetIn);
        return RestResult.createSuccessResult();
    }
}