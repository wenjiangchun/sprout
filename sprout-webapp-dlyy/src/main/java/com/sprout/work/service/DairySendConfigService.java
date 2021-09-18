package com.sprout.work.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.work.dao.DairySendConfigDao;
import com.sprout.work.entity.DairySendConfig;
import org.springframework.stereotype.Service;

@Service
public class DairySendConfigService extends AbstractBaseService<DairySendConfig, Long> {

	private DairySendConfigDao dairySendConfigDao;

	public DairySendConfigService(DairySendConfigDao dairySendConfigDao) {
		super(dairySendConfigDao);
		this.dairySendConfigDao = dairySendConfigDao;
	}
}
