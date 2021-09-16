package com.sprout.dlyy.student.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "y_question")
public class Question extends AbstractBaseEntity<Long> {

    private String content;

    private String answer;

    private Long selectNum;

    private Integer type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(Long selectNum) {
        this.selectNum = selectNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", answer='" + answer + '\'' +
                ", selectNum=" + selectNum +
                ", type=" + type +
                '}';
    }
}