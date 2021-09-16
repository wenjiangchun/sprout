package com.sprout.dlyy.student.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.dlyy.student.dao.PaperQuestionDao;
import com.sprout.dlyy.student.entity.PaperQuestion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色对象Service
 *
 * @author sofar
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PaperQuestionService extends AbstractBaseService<PaperQuestion, Long> {

    private PaperQuestionDao paperQuestionDao;

    public PaperQuestionService(PaperQuestionDao paperQuestionDao) {
        super(paperQuestionDao);
        this.paperQuestionDao = paperQuestionDao;
    }

}


