package com.sprout.work.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.work.dao.DairySendLogDao;
import com.sprout.work.entity.DairySendLog;
import org.springframework.stereotype.Service;

@Service
public class DairySendLogService extends AbstractBaseService<DairySendLog, Long> {

	private DairySendLogDao dairySendLogDao;

	public DairySendLogService(DairySendLogDao dairySendLogDao) {
		super(dairySendLogDao);
		this.dairySendLogDao = dairySendLogDao;
	}
}
