package com.sprout.dlyy.student.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.dlyy.student.entity.Question;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionDao extends BaseRepository<Question, Long> {
}
