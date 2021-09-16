package com.sprout.dlyy.monitor.controller;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;

import java.util.List;
import java.util.Objects;

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
            if (msg.contains("缺勤") || msg.contains("迟到") || msg.contains("早退")) {
                WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
                contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
                contentWriteCellStyle.setFillForegroundColor(IndexedColors.RED1.getIndex());
                contentWriteCellStyle.setWrapped(true);
                contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
                contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
                contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
                contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
                contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                WriteFont contentWriteFont = new WriteFont();
                contentWriteCellStyle.setWriteFont(contentWriteFont);
                cell.setCellStyle(StyleUtil.buildContentCellStyle(this.workbook, contentWriteCellStyle));
            }
        }
    }

    public static CustomHorizontalCellStyleStrategy create() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        //headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        WriteFont headWriteFont = new WriteFont();
        //headWriteFont.setFontHeightInPoints((short)20);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景白色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setWrapped(true);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        //contentWriteFont.setFontHeightInPoints((short)20);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        return new CustomHorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }
}
