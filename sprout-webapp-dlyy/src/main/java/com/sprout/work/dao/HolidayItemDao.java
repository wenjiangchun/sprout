package com.sprout.work.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.work.entity.HolidayItem;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayItemDao extends BaseRepository<HolidayItem, Long> {

}
