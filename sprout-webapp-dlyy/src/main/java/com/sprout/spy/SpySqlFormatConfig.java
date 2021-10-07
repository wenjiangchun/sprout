package com.sprout.spy;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import com.sprout.common.util.SproutDateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class SpySqlFormatConfig implements MessageFormattingStrategy {
    /**
     * 过滤掉定时任务的 SQL
     */
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return StringUtils.isNotBlank(sql) ? SproutDateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")
                + " | 耗时 " + elapsed + " ms | SQL 语句：" + StringUtils.LF + sql.replaceAll("[\\s]+", StringUtils.SPACE) + ";" : "";
    }
}
