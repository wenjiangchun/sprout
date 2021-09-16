package com.sprout.dlyy.student.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.dlyy.student.entity.PaperQuestion;
import org.springframework.stereotype.Repository;


@Repository
public interface PaperQuestionDao extends BaseRepository<PaperQuestion, Long> {
}
