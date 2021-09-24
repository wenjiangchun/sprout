package com.sprout.work.service;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.core.service.AbstractBaseService;
import com.sprout.data.ds.provider.SproutDataSource;
import com.sprout.work.dao.HolidayDao;
import com.sprout.work.dao.HolidayItemDao;
import com.sprout.work.entity.Holiday;
import com.sprout.work.entity.HolidayItem;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class HolidayService extends AbstractBaseService<Holiday, Long> {

	private HolidayDao holidayDao;

	private HolidayItemDao holidayItemDao;

	public HolidayService(HolidayDao holidayDao, HolidayItemDao holidayItemDao) {
		super(holidayDao);
		this.holidayDao = holidayDao;
		this.holidayItemDao = holidayItemDao;
	}

	public void saveHolidayItem(HolidayItem holidayItem) {
		List<HolidayItem> itemList = this.holidayItemDao.findByProperty("name", holidayItem.getName());
		if (!itemList.isEmpty()) {
			holidayItem.setId(itemList.get(0).getId());
		}
		this.holidayItemDao.save(holidayItem);
	}

	public List<HolidayItem> getHolidayItemList() {
		return this.holidayItemDao.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	public void saveHoliday(String itemName, Date workDay) throws Exception {
		HolidayItem holidayItem = this.holidayItemDao.findOneByProperty("name", itemName);
		if (Objects.isNull(holidayItem)) {
			throw new Exception("未找到对应节日类型");
		}
		Holiday holiday = new Holiday();
		holiday.setHolidayItem(holidayItem);
		holiday.setWorkDay(workDay);
		holiday.setYear(SproutDateUtils.getYear(workDay));
		holiday.setMonth(SproutDateUtils.getMonth(workDay));
		holiday.setDay(SproutDateUtils.getDay(workDay));
		this.holidayDao.save(holiday);
		logger.debug("保存节假日信息成功，【{}】", holiday);
	}
}
