package com.sprout.dlyy.student.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.dlyy.student.QuestionType;
import com.sprout.dlyy.student.dao.QuestionDao;
import com.sprout.dlyy.student.entity.Question;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionService extends AbstractBaseService<Question, Long> {

    private QuestionDao questionDao;

    public QuestionService(QuestionDao questionDao) {
        super(questionDao);
        this.questionDao = questionDao;
    }

    public void generateMathQuestion(int count, QuestionType questionType) {
        Random r = new Random();
        switch (questionType) {
            case ADD:
                for (int i = 0; i < count; i++) {
                    int firstNum = r.nextInt(1000);
                    if (firstNum < 10) {
                        firstNum += 10;
                    }
                    int secondNum = r.nextInt(1000);
                    if (secondNum < 10) {
                        secondNum += 10;
                    }
                    Question question = new Question();
                    question.setContent(firstNum + " + " + secondNum + " = ");
                    question.setAnswer(String.valueOf(firstNum + secondNum));
                    question.setSelectNum(0L);
                    question.setType(QuestionType.ADD.getType());
                    try {
                        this.save(question);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
                break;
            case MINUS:
                for (int i = 0; i < count; i++) {
                    int firstNum = r.nextInt(1000);
                    if (firstNum < 100) {
                        firstNum += 100;
                    }
                    int secondNum = r.nextInt(1000);
                    if (secondNum < 10) {
                        secondNum += 10;
                    }
                    boolean b = true;
                    while(b) {
                        if (secondNum >= firstNum) {
                            secondNum = r.nextInt(1000);
                            if (secondNum < 10) {
                                secondNum += 10;
                            }
                        } else {
                            b = false;
                        }

                    }
                    Question question = new Question();
                    question.setContent(firstNum + " - " + secondNum + " = ");
                    question.setAnswer(String.valueOf(firstNum - secondNum));
                    question.setSelectNum(0L);
                    question.setType(QuestionType.MINUS.getType());
                    try {
                        this.save(question);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
        }
    }
}


