package com.sprout.work.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.work.dao.WorkDairyDao;
import com.sprout.work.entity.WorkDairy;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WorkDairyService extends AbstractBaseService<WorkDairy, Long> {

	private WorkDairyDao workDairyDao;

	public WorkDairyService(WorkDairyDao workDairyDao) {
		super(workDairyDao);
		this.workDairyDao = workDairyDao;
	}

	public WorkDairy findWorkDairy(Long userId, Date workDay) {
		return null;
	}
}
