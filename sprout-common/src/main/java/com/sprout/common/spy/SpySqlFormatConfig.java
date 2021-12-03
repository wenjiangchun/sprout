package com.sprout.common.spy;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import com.sprout.common.util.SproutDateUtils;
import com.sprout.common.util.SproutStringUtils;

import java.util.Date;

public class SpySqlFormatConfig implements MessageFormattingStrategy {
    /**
     * 过滤掉定时任务的 SQL
     */
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return SproutStringUtils.isNotBlank(sql) ? SproutDateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")
                + " | use " + elapsed + " ms | SQL ：" + SproutStringUtils.LF + sql.replaceAll("[\\s]+", SproutStringUtils.SPACE) + ";" : "";
    }
}
