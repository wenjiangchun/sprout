package com.sprout.oa.asset.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.oa.asset.dao.AssetDao;
import com.sprout.oa.asset.dao.AssetInDao;
import com.sprout.oa.asset.entity.Asset;
import com.sprout.oa.asset.entity.AssetIn;
import com.sprout.system.entity.Dict;
import com.sprout.system.service.DictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AssetService extends AbstractBaseService<Asset, Long> {

    private AssetDao assetDao;

    private AssetInDao assetInDao;

    private DictService dictService;

    public AssetService(AssetDao assetDao, AssetInDao assetInDao, DictService dictService) {
        super(assetDao);
        this.assetDao = assetDao;
        this.assetInDao = assetInDao;
        this.dictService = dictService;
    }

    @Transactional(readOnly = true)
    public List<Dict> getAssetBrandList() {
        return dictService.findChildsByRootCode(Asset.DICT_BRAND);
    }

    @Transactional(readOnly = true)
    public List<Dict> getAssetModelList() {
        return dictService.findChildsByRootCode(Asset.DICT_MODEL);
    }

    @Transactional(readOnly = true)
    public List<Dict> getAssetUnitList() {
        return dictService.findChildsByRootCode(Asset.DICT_UNIT);
    }

    @Transactional(readOnly = true)
    public List<Dict> getAssetClassifyList() {
        return dictService.findChildsByRootCode(Asset.DICT_CLASSIFY);
    }

    public void saveAssetIn(AssetIn assetIn) {
        //首先判断是否存在asset 不存在则添加
        Asset asset = assetIn.getAsset();
        if (Objects.isNull(asset.getId())) {
            asset.setCount(assetIn.getCount() + asset.getCount());
            this.assetDao.save(asset);
        }
        //库存相加
        this.assetInDao.save(assetIn);
    }
}
