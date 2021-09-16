package com.sprout.dlyy.student.service;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.core.service.AbstractBaseService;
import com.sprout.dlyy.student.dao.PaperDao;
import com.sprout.dlyy.student.dao.PaperQuestionDao;
import com.sprout.dlyy.student.dao.QuestionDao;
import com.sprout.dlyy.student.entity.Paper;
import com.sprout.dlyy.student.entity.Question;
import com.sprout.dlyy.student.util.FreemarkerUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * 角色对象Service
 *
 * @author sofar
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PaperService extends AbstractBaseService<Paper, Long> {

    private PaperDao paperDao;

    private QuestionDao questionDao;

    private PaperQuestionDao paperQuestionDao;

    public PaperService(PaperDao paperDao,QuestionDao questionDao, PaperQuestionDao paperQuestionDao) {
        super(paperDao);
        this.paperDao = paperDao;
        this.questionDao = questionDao;
        this.paperQuestionDao = paperQuestionDao;
    }

    public void generatePaper(int count, int questionCount, boolean withAnswer, String destinationPath) {
        List<Map<String, Object>> datas = new ArrayList<>();
        int addStart = 1;
        int minusStart = 5001;
        Date date = null;
        try {
            date = SproutDateUtils.parseDate("2021-07-01","yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int perCount = questionCount / 2;
        for (int i = 0; i < count; i++) {
           Map<String, Object> d = new HashMap<>();
           List<Question> addList = questionDao.findByJql("from Question q where q.id >=" + addStart + " and q.id <=" + (addStart + perCount), null);
           List<Question> minusList = questionDao.findByJql("from Question q where q.id >=" + minusStart + " and q.id <=" + (minusStart + perCount), null);
           addStart += perCount;
           minusStart += perCount;
           d.put("day", SproutDateUtils.format(SproutDateUtils.addDays(date, i), "yyyy-MM-dd"));
            for (int i1 = 0; i1 < addList.size(); i1++) {
                if (withAnswer) {
                    d.put("question" + (i1 + 1), addList.get(i1).getContent() + addList.get(i1).getAnswer());
                } else {
                    d.put("question" + (i1 + 1), addList.get(i1).getContent());
                }
            }
            for (int i1 = 0; i1 < minusList.size(); i1++) {
                if (withAnswer) {
                    d.put("question" + (i1 + addList.size() + 1), minusList.get(i1).getContent() + minusList.get(i1).getAnswer());
                } else {
                    d.put("question" + (i1 + addList.size() + 1), minusList.get(i1).getContent());
                }
            }
            datas.add(d);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("data", datas);
        FreemarkerUtils.createDoc(map, "bb.ftl", destinationPath);
    }
}


