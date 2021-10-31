package com.sprout.work.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.work.entity.Holiday;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HolidayDao extends BaseRepository<Holiday, Long> {

    @Query("from Holiday h where h.workDay>=:startDay and h.workDay<=:endDay order by h.workDay asc")
    List<Holiday> getHolidayList(String startDay, String endDay);
}
