package com.sprout.oa.leave.util;

public enum DaySection {

    ALL(0),

    AM(1),

    PM(2);

    private int section;

    DaySection(int section) {
        this.section = section;
    }

    public int getSection() {
        return section;
    }
}
