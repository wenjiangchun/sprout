package com.sprout.dlyy.monitor.util;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;

import java.util.List;
import java.util.Objects;

/**
 * 自定义Excel表格样式策略
 */
public class CustomHorizontalCellStyleStrategy extends HorizontalCellStyleStrategy {

    private Workbook workbook;

    public CustomHorizontalCellStyleStrategy(WriteCellStyle headWriteCellStyle, List<WriteCellStyle> contentWriteCellStyleList) {
        super(headWriteCellStyle, contentWriteCellStyleList);
    }

    public CustomHorizontalCellStyleStrategy(WriteCellStyle headWriteCellStyle, WriteCellStyle contentWriteCellStyle) {
        super(headWriteCellStyle, contentWriteCellStyle);
    }

    @Override
    protected void initCellStyle(Workbook workbook) {
        super.initCellStyle(workbook);
        this.workbook = workbook;
    }

    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        super.setContentCellStyle(cell, head, relativeRowIndex);
        String msg = cell.getStringCellValue();
        if (Objects.nonNull(msg)) {
            if (msg.contains(DutyState.ABSENCE.getState()) || msg.contains(DutyState.COME_LATE.getState()) || msg.contains(DutyState.LEAVE_EARLY.getState())) {
                WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
                contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
                contentWriteCellStyle.setFillForegroundColor(IndexedColors.RED1.getIndex());
                setDefaultStyle(contentWriteCellStyle);
                cell.setCellStyle(StyleUtil.buildContentCellStyle(this.workbook, contentWriteCellStyle));
            }
        }
    }

    public static CustomHorizontalCellStyleStrategy create() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        //headWriteFont.setFontHeightInPoints((short)20);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景白色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        setDefaultStyle(contentWriteCellStyle);
        return new CustomHorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    /**
     * 设置默认边框及文本样式
     * @param contentWriteCellStyle 表格内容样式
     */
    private static void setDefaultStyle(WriteCellStyle contentWriteCellStyle) {
        contentWriteCellStyle.setWrapped(true);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        WriteFont contentWriteFont = new WriteFont();
        contentWriteCellStyle.setWriteFont(contentWriteFont);
    }
}
