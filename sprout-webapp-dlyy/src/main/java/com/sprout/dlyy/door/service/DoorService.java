package com.sprout.dlyy.door.service;


import com.sprout.common.util.SproutDateUtils;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.dlyy.door.config.DoorConfig;
import com.sprout.dlyy.door.wrapper.Record;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import com.mysql.jdbc.Driver;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public final class DoorService {
    private static JdbcTemplate jdbcTemplate;
    static {
        DoorConfig config = SpringContextUtils.getBean(DoorConfig.class);

        DataSource simpleDataSource = null;
        try {
            simpleDataSource = new SimpleDriverDataSource(new Driver(),config.getUrl(), config.getUserName(), config.getPassword());
            jdbcTemplate = new JdbcTemplate(simpleDataSource);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public static List<Record> getRecordList(Date startDate, Date endDate) {
        //JdbcTemplate jdbcTemplate = SpringContextUtils.getBean(JdbcTemplate.class);
        String sql = "select r.ID, r.Date,r.OnDuty1, r.OffDuty1, r.OnDuty2, r.OffDuty2, r.WorkLong, r.WorkingDays, p.Name from attendancedayrecord r left join personnel p on r.PersonnelID = p.ID";
        if(Objects.isNull(startDate)) {
            //获取本月第一天数据
            startDate = SproutDateUtils.getFirstDayOfMonth();
        }
        if (Objects.isNull(endDate)) {
            endDate = new Date();
        }
        sql += " where r.Date>=? and r.Date<=? and p.ParentID=2 and p.Name not in ('王相协', '王强', '张浩') order by r.Date ASC, p.Name ASC";
        Object[] params = new Object[] { startDate, endDate };
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Record record = new Record();
            record.setId(rs.getLong("ID"));
            record.setRecordDate(SproutDateUtils.format(rs.getDate("Date"), "yyyy-MM-dd"));
            record.setOnDutyAm(rs.getString("OnDuty1"));
            record.setOffDutyAm(rs.getString("OffDuty1"));
            record.setOnDutyPm(rs.getString("OnDuty2"));
            record.setOffDutyPm(rs.getString("OffDuty2"));
            record.setPersonName(rs.getString("Name"));
            record.setWorkDays(rs.getFloat("WorkingDays"));
            record.setWorkLong(rs.getFloat("WorkLong"));
            return record;
        });
    }

    /**
     * 获取全部人员名称
     * @return 人员名称列表
     */
    public static List<String> getPersonNameList() {
        //JdbcTemplate jdbcTemplate = SpringContextUtils.getBean(JdbcTemplate.class);
        String sql = "select p.Name from personnel p where p.ParentID=2 and p.Name not in ('王相协', '王强', '张浩') order by p.Name ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("Name"));
    }
}
