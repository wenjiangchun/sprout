package com.sprout.dlyy.student.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.dlyy.student.entity.Paper;
import org.springframework.stereotype.Repository;


@Repository
public interface PaperDao extends BaseRepository<Paper, Long> {
}
