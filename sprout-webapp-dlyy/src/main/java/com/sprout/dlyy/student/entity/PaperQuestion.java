package com.sprout.dlyy.student.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "y_paper_question")
public class PaperQuestion extends AbstractBaseEntity<Long> {

    private Long paperId;

    private Long questionId;

    private Integer num;

    private String answer;

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "PaperQuestion{" +
                "id=" + id +
                ", paperId=" + paperId +
                ", questionId=" + questionId +
                ", num=" + num +
                ", answer='" + answer + '\'' +
                '}';
    }
}
