package com.sprout.work.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.work.entity.Holiday;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayDao extends BaseRepository<Holiday, Long> {

}
