package com.wwx.study.pdf;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author wuwenxi 2022-09-21
 */
public class ExportExcelTest {
    private static final String THIRD = "活动编码: %s;商品类别: %s;商家名称: %s";

    public static void main(String[] args) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("劳保采购单明细");
//        // title
//        CustomExcelHeader title = new CustomExcelHeader();
//        title.setSize(1);
//        title.setCellValues(new String[]{"测试测试"});
//        title.setCellStyle(buildCellStyle(wb));
//        // 活动
//        CustomExcelHeader activity = new CustomExcelHeader();
//        activity.setSize(1);
//        activity.setCellValues(new String[]{"fdjshfdjstje"});
//        activity.setCellStyle(buildActivityNameCellStyle(wb));
//        // 编码
//        CustomExcelHeader third = new CustomExcelHeader();
//        third.setSize(3);
//        third.setCellValues(String.format(THIRD, "code1343423432", "32432fdhjsdfdjdf", "fdahjdbfdsfdfd").split(";"));
//        third.setCellStyle(buildActivityNameCellStyle(wb));
        String s = "退款编码,退款类型,关联订单编码,退款申请时间,平台SPU,平台SKU,商品名称,计价单位,退款采购单价,退款数量,退款采购总金额,退款完成时间,对账周期从,对账周期至,退款统计截止时间";
        List<Integer> sheetColumnWidthList = Arrays.asList(30, 20, 30, 30, 20, 20, 60, 20, 20, 20, 20, 30, 30, 30, 30);
//        buildExcel(wb, sheet, Arrays.asList(title, activity, third), Arrays.asList(s.split(",")), null, sheetColumnWidthList);
        buildExcel(wb,sheet,"测试测测谁","互动的身份地方是非得失",Arrays.asList(s.split(",")),null,sheetColumnWidthList);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = null;
        File file = new File("export.xlsx");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fis = new FileInputStream(file);
            wb.write(bos);
            byte[] fileBytes = bos.toByteArray();
            System.out.println(fileBytes.length);
//            fis.re
            fis.read(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void buildExcel(XSSFWorkbook wb, Sheet sheet, List<CustomExcelHeader> headers,
                                  List<String> sheetColumnList, List<List<String>> sheetDataList,
                                  List<Integer> sheetColumnWidthList) {
        headers.forEach(CustomExcelHeader::validate);
        //设置列宽
        for (int i = 0; i < sheetColumnWidthList.size(); i++) {
            sheet.setColumnWidth(i, sheetColumnWidthList.get(i) * 256);
        }
        int rowMark = -1;
        // 处理excel头部
        for (CustomExcelHeader customExcelHeader : headers) {
            Row row = sheet.createRow(++rowMark);
            // 合并区域大小
            int mergeSize = sheetColumnList.size() / customExcelHeader.size;
            for (int i = 0; i < customExcelHeader.size; i++) {
                int firstCol = i * mergeSize;
                int lastCol = i == customExcelHeader.size - 1 ? sheetColumnList.size() - 1 : ((i + 1) * mergeSize) - 1;
                Cell cell = row.createCell(firstCol);
                cell.setCellValue(customExcelHeader.cellValues[i]);
                if (customExcelHeader.getCellStyle() != null) {
                    cell.setCellStyle(customExcelHeader.getCellStyle());
                }
                sheet.addMergedRegion(new CellRangeAddress(rowMark, rowMark, firstCol, lastCol));
            }
        }
    }

    public static void buildExcel(XSSFWorkbook wb
            , Sheet sheet
            , String title
            , String activityName
            , List<String> sheetColumnList
            , List<List<String>> sheetDataList
            , List<Integer> sheetColumnWidthList) {
        //设置列宽
        for (int i = 0; i < sheetColumnWidthList.size(); i++) {
            sheet.setColumnWidth(i, sheetColumnWidthList.get(i) * 256);
        }
        Row row0Sheet1 = sheet.createRow(0);
        int rowMark = -1;
        //1.设置标题头
        Cell cell0 = row0Sheet1.createCell(++rowMark);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, sheetColumnList.size() - 1));
        cell0.setCellValue(title);
        cell0.setCellStyle(buildCellStyle(wb));
        //额外：第二行添加活动名称
        if (StringUtils.isNotBlank(activityName)) {
            Row rowActivityNameSheet = sheet.createRow(++rowMark);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, sheetColumnList.size() - 1));
            CellStyle cellStyle = buildActivityNameCellStyle(wb);
            for (int i = 0; i < sheetColumnList.size(); i++) {
                Cell cell = rowActivityNameSheet.createCell(i);
                if (i == 0) {
                    cell.setCellValue(activityName);
                }
                cell.setCellStyle(cellStyle);
            }
        }
    }

    public static CellStyle buildCellTitle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        Font ztFont = wb.createFont();
        ztFont.setBold(true);
        style.setFont(ztFont);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        //设置自动换行
        style.setWrapText(true);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    public static CellStyle buildActivityNameCellStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        Font ztFont = wb.createFont();
        ztFont.setBold(true);
        style.setFont(ztFont);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    public static CellStyle buildColumnCellTitle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        //设置自动换行
        style.setWrapText(true);
        return style;
    }

    public static CellStyle buildCellStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        Font ztFont = wb.createFont();
        ztFont.setBold(true);
        ztFont.setFontHeightInPoints((short) 16);
        style.setFont(ztFont);
        return style;
    }

    public static class CustomExcelHeader {
        private String[] cellValues;
        private int size = -1;
        private CellStyle cellStyle;

        void validate() {
            if (cellValues == null) {
                throw new IllegalArgumentException("未传入行内容");
            }
            if (size < 0) {
                throw new IllegalArgumentException("未传入行大小");
            }
            if (cellValues.length != size) {
                throw new IllegalArgumentException("传入行内容与行大小不一致");
            }
        }

        public String[] getCellValues() {
            return cellValues;
        }

        public void setCellValues(String[] cellValues) {
            this.cellValues = cellValues;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public CellStyle getCellStyle() {
            return cellStyle;
        }

        public void setCellStyle(CellStyle cellStyle) {
            this.cellStyle = cellStyle;
        }
    }
}
