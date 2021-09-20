package com.sprout.work.service;

import com.alibaba.excel.EasyExcel;
import com.sprout.common.util.SproutDateUtils;
import com.sprout.core.service.AbstractBaseService;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.system.entity.User;
import com.sprout.work.dao.DairySendConfigDao;
import com.sprout.work.dao.WorkDairyDao;
import com.sprout.work.entity.DairySendConfig;
import com.sprout.work.entity.WorkDairy;
import com.sprout.work.util.EmailSender;
import com.sprout.work.util.WorkDairyWrapper;
import com.sprout.work.util.WorkDayUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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


	public boolean sendEmail(User worker) throws Exception {
		//查询该用户所有工作日志
		WorkDairy workDairy = new WorkDairy();
		workDairy.setWorker(worker);
		Example<WorkDairy> example = Example.of(workDairy);
		List<WorkDairy> workDairyList = this.workDairyDao.findAll(example, Sort.by(Sort.Direction.ASC, "workDay"));
		//封装excel
		File f = new File("d:\\" + System.currentTimeMillis() + ".xlsx");
		List<WorkDairyWrapper> workDairyWrapperList = new ArrayList<>();
		workDairyList.forEach(w -> {
			WorkDairyWrapper wrapper = new WorkDairyWrapper();
			wrapper.setWorkDay(SproutDateUtils.format(w.getWorkDay(), "yyyy-MM-dd"));
			wrapper.setWeekNum(w.getWeekNum());
			wrapper.setWeekDay(w.getWeekDay());
			wrapper.setContent(w.getContent());
			wrapper.setRemark(w.getRemark());
			workDairyWrapperList.add(wrapper);
		});
		EasyExcel.write(f, WorkDairyWrapper.class).sheet("sheet1").doWrite(workDairyWrapperList);
		//发送邮件
		DairySendConfig config = SpringContextUtils.getBean(DairySendConfigDao.class).findOneByProperty("worker", worker);
		EmailSender.sendMimeMail(config, "工作周报", "", f);
		return f.delete();
	}

	/**
	 * 自动生成人员从打卡日期到目前为止缺的数据模板
	 * @param userId 人员ID
	 * @return 是否创建成功
	 */
	public int generateWorkDairy(Long userId, DairySendConfig dairySendConfig) {
		List<WorkDairy> list = new ArrayList<>();
		User worker = new User();
		worker.setId(userId);
		Date startDay = null;
		Date currentDay = new Date();
		int weekNum = 1;
		//查找配置信息
		if (Objects.nonNull(dairySendConfig)) {
			startDay = dairySendConfig.getDairyStartDay();
			weekNum = dairySendConfig.getWeekStartNum();
		} else {
			//去数据库中查询第一条记录
			WorkDairy firstWorkDairy = this.workDairyDao.findOneByProperty("worker", worker, Sort.by("workDay"));
			if (Objects.nonNull(firstWorkDairy)) {
				startDay = firstWorkDairy.getWorkDay();
			}
		}
		if (Objects.nonNull(startDay)) {
			//查询该人当前所有记录
			//TODO 查询出来所有日期放到一个列表中
			WorkDairy example = new WorkDairy();
			example.setWorker(worker);
			List<WorkDairy> workDairyList = this.workDairyDao.findAll(Example.of(example), Sort.by("workDay"));
			List<String> workDayList = new ArrayList<>();
			workDairyList.forEach(workDairy -> {
				workDayList.add(SproutDateUtils.format(workDairy.getWorkDay(), "yyyy-MM-dd"));
			});
			int diffDays = SproutDateUtils.getDiffBetween(startDay, currentDay);
			for (int i = 0; i < diffDays; i++) {
				Date d = SproutDateUtils.addDays(startDay, i);
				if (!workDayList.contains(SproutDateUtils.format(d, "yyyy-MM-dd"))) {
					//生成数据
					WorkDairy wd = new WorkDairy();
					wd.setWorker(worker);
					wd.setWorkDay(d);
					WorkDayUtils.WeekDay weekDay = WorkDayUtils.getWeekDayByDate(d);
					wd.setWeekDay(weekDay.getWeekDayName());
					wd.setWeekNum(WorkDayUtils.getWeekNum(startDay, d) + weekNum -1);
					if (weekDay.equals(WorkDayUtils.WeekDay.SAT) || weekDay.equals(WorkDayUtils.WeekDay.SUN)) {
						wd.setContent("周末");
					}
					list.add(wd);
				}
			}
		}
		this.workDairyDao.saveAll(list);
		return list.size();
	}
}
