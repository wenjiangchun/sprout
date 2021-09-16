package com.sprout.dlyy.student;

public enum QuestionType {
    ADD(0), MINUS(1), MIX(2);

    private int type;

    QuestionType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
