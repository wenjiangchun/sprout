package com.sprout.work.service;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.core.service.AbstractBaseService;
import com.sprout.work.dao.HolidayDao;
import com.sprout.work.dao.HolidayItemDao;
import com.sprout.work.entity.Holiday;
import com.sprout.work.entity.HolidayItem;
import com.sprout.work.util.WorkDayUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
		getHolidayCache(true);
    }

    public List<HolidayItem> getHolidayItemList() {
        return this.holidayItemDao.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Holiday saveHoliday(String itemName, String workDay) throws Exception {
        HolidayItem holidayItem = this.holidayItemDao.findOneByProperty("name", itemName);
        if (Objects.isNull(holidayItem)) {
            throw new Exception("未找到对应节日类型");
        }
        Holiday holiday = new Holiday();
        holiday.setHolidayItem(holidayItem);
        holiday.setWorkDay(workDay);
        Date d = SproutDateUtils.parseDate(workDay, "yyyy-MM-dd");
        holiday.setYear(SproutDateUtils.getYear(d));
        holiday.setMonth(SproutDateUtils.getMonth(d));
        holiday.setDay(SproutDateUtils.getDay(d));
        this.save(holiday);
        logger.debug("保存节假日信息成功，【{}】", holiday);
        return holiday;
    }

    public HolidayItem getHolidayItemByName(String name) {
        return this.holidayItemDao.findOneByProperty("name", name);
    }

    public int generateHoliday() throws Exception {
        int year = SproutDateUtils.getYear(new Date());
        Date startDay = SproutDateUtils.fromLocalDate(LocalDate.of(year, Month.JANUARY, 1));
        Date endDay = SproutDateUtils.fromLocalDate(LocalDate.of(year, Month.DECEMBER, 31));
        boolean b = true;
        int holidayNum = 0;
        while (b) {
            //判断当日是否为周末
            if (WorkDayUtils.getWeekDayByDate(startDay).equals(WorkDayUtils.WeekDay.SAT) || WorkDayUtils.getWeekDayByDate(startDay).equals(WorkDayUtils.WeekDay.SUN)) {
                //插入数据
                //查询周末配置
                HolidayItem holidayItem = this.getHolidayItemByName("周末");
                //查询是否存在之前数据
                List<Holiday> holidayList = this.findByProperty("workDay", SproutDateUtils.format(startDay, "yyyy-MM-dd"));
                Holiday holiday = new Holiday();
                if (holidayList.isEmpty()) {
                    holiday.setHolidayItem(holidayItem);
                    holiday.setWorkDay(SproutDateUtils.format(startDay, "yyyy-MM-dd"));
                    holiday.setYear(SproutDateUtils.getYear(startDay));
                    holiday.setMonth(SproutDateUtils.getMonth(startDay));
                    holiday.setDay(SproutDateUtils.getDay(startDay));
                    holidayNum++;
                } else {
                    holiday = holidayList.get(0);
                    holiday.setHolidayItem(holidayItem);
                }
                this.save(holiday);
            }
            if (SproutDateUtils.isSameDay(startDay, endDay)) {
                b = false;
            } else {
                startDay = SproutDateUtils.addDays(startDay, 1);
            }
        }
        return holidayNum;
    }

    private static Map<String, Holiday> HOLIDAY_CACHE = new ConcurrentHashMap<>();

    public synchronized Map<String, Holiday> getHolidayCache(boolean forceQuery) {
        if (HOLIDAY_CACHE.isEmpty() || forceQuery) {
            this.findAll().forEach(holiday -> {
                HOLIDAY_CACHE.put(holiday.getWorkDay(), holiday);
            });
        }
        return HOLIDAY_CACHE;
    }

    @Override
    public Holiday save(Holiday holiday) throws Exception {
        super.save(holiday);
        HOLIDAY_CACHE.put(holiday.getWorkDay(), holiday);
        return holiday;
    }

	@Override
	public void delete(Holiday holiday) throws Exception {
		super.delete(holiday);
		HOLIDAY_CACHE.remove(holiday.getWorkDay());
	}

    public void updateHolidayItem(String preName, String name) throws Exception {
        HolidayItem holidayItem = this.holidayItemDao.findOneByProperty("name", preName);
        List<HolidayItem> holidayItemList = this.holidayItemDao.findByProperty("name", name);
        if (!holidayItemList.isEmpty()) {
            throw new Exception("已存在【"+name+"】节假日信息");
        }
        holidayItem.setName(name);
        this.holidayItemDao.save(holidayItem);
        getHolidayCache(true);
    }


    /**
     * 获取两个日期之间的节假日信息
     * @param startDay 开始日期
     * @param endDay 结束日期
     * @return 正序排列节假日信息
     */
    public List<Holiday> getHolidayList(String startDay, String endDay) {
        return this.holidayDao.getHolidayList(startDay, endDay);
    }


    /**
     * 判断日期是否存在于固定节假日列表中
     * @param day 需对比日期
     * @param holidayList 节假日列表
     * @return 存在返回true,否则返回false
     */
    public boolean existHoliday(String day, List<Holiday> holidayList) {
        //String day = SproutDateUtils.format(workDay, "yyyy-MM-dd");
        return holidayList.stream().anyMatch(holiday -> holiday.getWorkDay().equals(day));
    }
}
