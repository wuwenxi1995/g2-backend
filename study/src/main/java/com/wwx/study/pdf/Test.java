package com.wwx.study.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author wuwenxi 2022-03-31
 */
public class Test {
    private static List<CsgMonthBillExportDTO.CsgMonthBillOrderPlatformData> buildData() {
        List<CsgMonthBillExportDTO.CsgMonthBillOrderPlatformData> list = new ArrayList<>();
        CsgMonthBillExportDTO.CsgMonthBillOrderPlatformData orderPlatformData = new CsgMonthBillExportDTO.CsgMonthBillOrderPlatformData();
        orderPlatformData.setPlatformOrderCode("47405153487262100593");
        orderPlatformData.setCustomerName("xiehaiming");
        orderPlatformData.setMobileNumber("28892033286");
        orderPlatformData.setCompanyName("");
        orderPlatformData.setUnitName("子工会1 - 部门1");
        orderPlatformData.setActivityCode("CSG2020082600003");
        orderPlatformData.setActivityName("2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动2020国庆节福利活动");
        orderPlatformData.setPlatformCreatedTime("2020-09-01 16:37:56");
        orderPlatformData.setReceiveDate("2020-11-19 16:40:39");
        orderPlatformData.setPlatformProductCode("CSGP100219");
        orderPlatformData.setSkuCode("CSGK100249");
        orderPlatformData.setTitle("安啧啧 尼西鸡 800g/只");
        orderPlatformData.setBaseUomCodeMeaning("只");
        orderPlatformData.setPurchasePrice(BigDecimal.ZERO);
        orderPlatformData.setUnitPrice(BigDecimal.valueOf(0.01));
        orderPlatformData.setQuantity(1L);
        orderPlatformData.setTotalAmount(BigDecimal.ZERO);
        orderPlatformData.setSalesAmount(BigDecimal.valueOf(0.01));
        orderPlatformData.setIsRefund("否");
        orderPlatformData.setRefundPurchasePrice(BigDecimal.ZERO);
        orderPlatformData.setRefundQuantity(0L);
        orderPlatformData.setRefundTotalAmount(BigDecimal.ZERO);
        orderPlatformData.setBillPeriodFrom("2020-11-18 00:00:00");
        orderPlatformData.setBillPeriodTo("2020-11-18 00:00:00");
        orderPlatformData.setRefundLastDate("2020-11-18 00:00:00");
        orderPlatformData.setActualTotalAmount(BigDecimal.ZERO);
        orderPlatformData.setRedRefundAmount(BigDecimal.ZERO);
        orderPlatformData.setActualPaidTotalAmount(BigDecimal.ZERO);
        list.add(orderPlatformData);
        return list;
    }

    private static List<CsgMonthBillExportDTO.CsgMonthBillRefundData> buildRefundData() {
        List<CsgMonthBillExportDTO.CsgMonthBillRefundData> list = new ArrayList<>();
        CsgMonthBillExportDTO.CsgMonthBillRefundData data = new CsgMonthBillExportDTO.CsgMonthBillRefundData();
        data.setPlatformCode("47405153487262100593");
        data.setRefundTypeCodeMeaning("仅退款");
        data.setPlatformOrderCode("47405153487262100593");
        data.setRefundPlatformCreatedDate("2020-11-18 00:00:00");
        data.setRefundPlatformProductCode("CSGP100219");
        data.setRefundPlatformSkuCode("CSGK100249");
        data.setRefundTitle("安啧啧 尼西鸡 800g/只");
        data.setRefundBaseUomCodeMeaning("只");
        data.setPurchasePrice(BigDecimal.ZERO);
        data.setRefundQuantity(1L);
        data.setRefundTotalAmount(BigDecimal.ZERO);
        data.setRefundDate("2020-11-18 00:00:00");
        data.setBillPeriodFrom("2020-11-18 00:00:00");
        data.setBillPeriodTo("2020-11-18 00:00:00");
        data.setRefundLastDate("2020-11-18 00:00:00");
        list.add(data);
        return list;
    }


    public static void main(String[] args) throws Exception {
//        Document document = new Document();
//        PdfWriter writer = null;
//        FileOutputStream fos = null;
//        ByteArrayOutputStream boss = null;
//        File file = new File("订单明细.pdf");
//        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
//            writer = PdfWriter.getInstance(document, bos);
//            document.open();
//            order(document);
//            int pageNumber = writer.getPageNumber();
//            document.newPage();
//            document.setPageCount(1);
//            refund(document);
//            int refundNumber = writer.getPageNumber();
//            document.close();
//            byte[] fileBytes = bos.toByteArray();
//            List<PdfReader> readers = new ArrayList<>();
//            readers.add(new PdfReader(fileBytes));
//            boss = createPdfFooterPageNumber(readers, new Integer[]{pageNumber, refundNumber});
//            if (file.exists()) {
//                file.delete();
//            }
//            file.createNewFile();
//            fos = new FileOutputStream(file);
//            boss.writeTo(fos);
//        } finally {
//            if (writer != null) {
//                writer.close();
//            }
//            if (fos != null) {
//                fos.close();
//            }
//            if (boss != null) {
//                boss.close();
//            }
//        }
        try {
                    Document document = new Document();
        PdfWriter writer = null;
        FileOutputStream fos = null;
        ByteArrayOutputStream boss = null;
        File file = new File("订单明细.pdf");
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                writer = PdfWriter.getInstance(document, bos);
                // 创建PdfWriter对象
                document.open();
                // 订单明细
                buildOrder(document);
                int pageOrderNumber = writer.getPageNumber();
                // 订单行单
                document.newPage();
                document.setPageCount(1);
                buildOrderEntry(document);
                int pageOrderEntryNumber = writer.getPageNumber();
                document.close();
                // 文件上传
                byte[] fileBytes = bos.toByteArray();
                List<PdfReader> readers = new ArrayList<>();
                readers.add(new PdfReader(fileBytes));
                boss = createPdfFooterPageNumber(readers, new Integer[]{pageOrderNumber, pageOrderEntryNumber});
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            boss.writeTo(fos);
            }
        } catch (Exception e) {
            System.out.println(e);
//            LOG.error("劳保对账单生成pdf发生错误, 集采订单:{} ,错误信息：{}", exportDTO.getLaborGroupOrderCode(), Throwables.getStackTraceAsString(e));
        }
    }

    private static void buildOrderEntry(Document document) throws Exception {
        String orderEntryColumn = "商品名称\n" +
                "销售单价\n" +
                "规格参数\n" +
                "尺码\n" +
                "颜色\n" +
                "数量\n" +
                "劳保豆支付金额\n" +
                "现金支付金额";
        String s = "活动编码, 商品类别, 商家名称, " + orderEntryColumn.replace("\n", ", ");
        StringBuilder sb = new StringBuilder();
        for (Field field : CsgMonthBillExportDTO.PdfOrderEntryDTO.class.getDeclaredFields()) {
            sb.append(field.getName()).append(",");
        }
        String propertyName = sb.toString().substring(0, sb.lastIndexOf(","));
        document.add(buildPdfPTable(billExcelDataList(), s, "南网个人商城(劳保商城)采购对账单 - 商品明细",
                "蝴蝶是非得失发的范德萨范德萨范德萨范德萨", propertyName, new float[]{50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50}));
    }

    private static List<CsgMonthBillExportDTO.PdfOrderEntryDTO> billExcelDataList() {
        List<CsgMonthBillExportDTO.PdfOrderEntryDTO> data = new ArrayList<>();
        CsgMonthBillExportDTO.PdfOrderEntryDTO entryDTO = new CsgMonthBillExportDTO.PdfOrderEntryDTO();
        entryDTO.setActivityCode("111111");
        entryDTO.setCategoryName("测试");
        entryDTO.setSupplierName("供应商111111");
        entryDTO.setTitle("323fudssbfdsbfhd");
        entryDTO.setUnitPrice(BigDecimal.valueOf(12));
        entryDTO.setDescription("111111");
        entryDTO.setSize("170/65");
        entryDTO.setColour("红色");
        entryDTO.setQuantity(1L);
        entryDTO.setCardLineAmount(BigDecimal.valueOf(1));
        entryDTO.setCashPaidAmount(BigDecimal.ZERO);
        data.add(entryDTO);
        return data;
    }

    private static void buildOrder(Document document) throws Exception{
        String orderColumn = "公司名称\n" +
                "部门\n" +
                "姓名\n" +
                "商品名称\n" +
                "劳保豆支付金额\n" +
                "个人现金支付金额\n" +
                "签收情况";
        String s = " 活动编码, 商品类别, 商家名称, " + orderColumn.replace("\n", ", ");
        StringBuilder sb = new StringBuilder();
        for (Field field : CsgMonthBillExportDTO.PdfOrderDTO.class.getDeclaredFields()) {
            sb.append(field.getName()).append(",");
        }
        String propertyName = sb.toString().substring(0, sb.lastIndexOf(","));
        document.add(buildPdfPTable(billPdfDataList(), s, "南网个人商城(劳保商城)采购对账单 - 人员消费明细",
                "蝴蝶是非得失发的范德萨范德萨范德萨", propertyName, new float[]{50, 50, 50, 80, 50, 50, 50, 50, 50, 50}));
    }

    private static List<CsgMonthBillExportDTO.PdfOrderDTO> billPdfDataList() {
        List<CsgMonthBillExportDTO.PdfOrderDTO> orderDTOS = new ArrayList<>();
        CsgMonthBillExportDTO.PdfOrderDTO pdfOrderDTO = new CsgMonthBillExportDTO.PdfOrderDTO();
        pdfOrderDTO.setActivityCode("111111");
        pdfOrderDTO.setCategoryName("测试");
        pdfOrderDTO.setSupplierName("供应商111111");
        pdfOrderDTO.setCompanyName("测试组织架构/北京公司/北京公司子公司");
        pdfOrderDTO.setUnitName("资金\n" +
                "部");
        pdfOrderDTO.setName("AAAAA");
        pdfOrderDTO.setTitle("123178312djfsjfbjdsfjkdskfdsfs");
        pdfOrderDTO.setActivityTotalAmount(BigDecimal.valueOf(20));
        pdfOrderDTO.setCashAmount(BigDecimal.ZERO);
        pdfOrderDTO.setSignStatus("sign");
        orderDTOS.add(pdfOrderDTO);
        return orderDTOS;
    }

    private static void order(Document document) throws Exception {
        String s = "订单编码,下单人姓名,下单人手机号,下单人公司,下单人部门,活动编码,活动名称,下单时间,确认收货时间,平台SPU,平台SKU,商品名称,计价单位,采购单价,销售单价," +
                "数量,当月采购总金额,销售总价,是否发生退款,退款采购单价,退款数量,当月退款总金额,对账周期从,对账周期至,退款统计截止时间,实际交易总金额,红冲退款金额,实际付款总金额";
        float[] floats = new float[]{24, 15, 15, 20, 20, 24, 60, 20, 20, 18, 18, 30, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 20, 20, 20, 15, 15, 15};
        StringBuilder sb = new StringBuilder();
        for (Field field : CsgMonthBillExportDTO.CsgMonthBillOrderPlatformData.class.getDeclaredFields()) {
            sb.append(field.getName()).append(",");
        }
        String propertyName = sb.toString().substring(0, sb.lastIndexOf(","));
        document.add(buildPdfPTable(buildData(), s, "南网个人商城(赫兹乐购)采购对账单-订单明细", propertyName, floats));
    }

    private static void refund(Document document) throws Exception {
        String s = "退款编码,退款类型,关联订单编码,退款申请时间,平台SPU,平台SKU,商品名称,计价单位,退款采购单价,退款数量,退款采购总金额,退款完成时间,对账周期从,对账周期至,退款统计截止时间";
        float[] floats = new float[]{30, 20, 30, 30, 20, 20, 60, 20, 20, 20, 20, 30, 30, 30, 30};
        StringBuilder sb = new StringBuilder();
        for (Field field : CsgMonthBillExportDTO.CsgMonthBillRefundData.class.getDeclaredFields()) {
            sb.append(field.getName()).append(",");
        }
        String propertyName = sb.toString().substring(0, sb.lastIndexOf(","));
        document.add(buildPdfPTable(buildRefundData(), s, "南网个人商城(赫兹乐购)采购对账单-退款明细", propertyName, floats));
    }

    private static ByteArrayOutputStream createPdfFooterPageNumber(List<PdfReader> readers, Integer[] pageNumbers) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Document document = null;
        PdfSmartCopy smartCopy = null;
        PdfReader reader = null;
        try {
            document = new Document();
            smartCopy = new PdfSmartCopy(document, os);
            // 打开文档
            document.open();
            int currentPage = 1, totalPage = pageNumbers[0];
            Iterator<PdfReader> iterator = readers.iterator();
            while (iterator.hasNext()) {
                reader = iterator.next();
                PdfImportedPage page;
                PdfCopy.PageStamp stamp;
                int pages = reader.getNumberOfPages();
                for (int index = 1; index <= pages; index++) {
                    document.newPage();
                    page = smartCopy.getImportedPage(reader, index);
                    stamp = smartCopy.createPageStamp(page);
                    if (currentPage > totalPage) {
                        currentPage = 1;
                        totalPage = pageNumbers[1];
                    }
                    float x = (document.right() + document.left()) / 2;
                    float y = 20f;
                    ColumnText.showTextAligned(stamp.getUnderContent(), Element.ALIGN_CENTER,
                            new Phrase(createFont(String.format("第%d页/共%d页", currentPage, totalPage))), x, y, 0f);
                    stamp.alterContents();
                    smartCopy.addPage(page);
                    currentPage++;
                }
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
            if (smartCopy != null) {
                smartCopy.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
        return os;
    }

    private static Paragraph createFont(String content) {
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Paragraph paragraph = new Paragraph(content, new Font(bf, 9f));
        // 字体居中
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    private static <T> PdfPTable buildPdfPTable(List<T> dataList, String columnTitle, String topTitle, String propertyName, float[] columnWidthArrays) {
        List<String> columnTitleList = Arrays.asList(columnTitle.split(","));
        List<String> propertyNameList = Arrays.asList(propertyName.split(","));
        try {
            PdfPTable table = new PdfPTable(columnTitleList.size());
            //// 设置表格宽度比例为%100
            table.setWidthPercentage(100);
            // 设置表格的宽度
            // table.setTotalWidth(columnWidthArrays);
            // 也可以每列分别设置宽度
            table.setTotalWidth(columnWidthArrays);
            // 锁住宽度
            table.setLockedWidth(true);
            // 设置表格上面空白宽度
            table.setSpacingBefore(0f);
            // 设置表格下面空白宽度
            table.setSpacingAfter(0f);
            // 设置表格默认为无边框
            table.getDefaultCell().setBorder(0);
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font fontTop = new Font(bfChinese, 8, Font.BOLD);
            Font fontTitle = new Font(bfChinese, 3, Font.BOLD);
            //Font fontInfo = new Font(bfChinese, 8,Font.NORMAL);
            //设置头标题
            PdfPCell cell0 = new PdfPCell(new Paragraph(topTitle, fontTop));
            cell0.setBorderColor(BaseColor.WHITE);
            cell0.setColspan(columnTitleList.size());
            cell0.setPaddingLeft(10);
            cell0.setFixedHeight(20);
            cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell0);

            PdfPCell cell1 = new PdfPCell(new Paragraph("活动第三方对方速度范德萨范德萨", fontTop));
//            cell1.setBorderColor(BaseColor.WHITE);
            cell1.setColspan(columnTitleList.size());
            cell1.setPaddingLeft(10);
            cell1.setFixedHeight(20);
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell1);

            //设置列表标题
            for (int i = 0; i < columnTitleList.size(); i++) {
                PdfPCell cellTemp = new PdfPCell(new Paragraph(columnTitleList.get(i), fontTitle));
                cellTemp.setFixedHeight(14);
                cellTemp.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellTemp.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cellTemp);
            }
            //填充内容-通过反射
            if (dataList != null) {
                int serialNumber = 0;
                boolean firstRow = true;
                for (T item : dataList) {
                    //首先判定该行数据是否需要加粗数据
                    Font fontInfo = new Font(bfChinese, 2, Font.NORMAL);
                    try {
                        Field boldFlagField = item.getClass().getDeclaredField("isBoldFlag");
                        boldFlagField.setAccessible(true);
                        String boldFlagValue = String.valueOf(boldFlagField.get(item));
                        if (boldFlagValue != null && "1".equalsIgnoreCase(boldFlagValue)) {
                            fontInfo = new Font(bfChinese, 2, Font.BOLD);
                        }
                    } catch (Exception e) {
                        // LOG.error("java反射为没有找到属性isBoldFlag", e);
                    }
                    for (int i = 0; i < propertyNameList.size(); i++) {
                        if (!StringUtils.contains(propertyNameList.get(i), "serialNumber")) {
                            Field field = item.getClass().getDeclaredField(propertyNameList.get(i));
                            field.setAccessible(true);
                            Object fieldValue = field.get(item);
                            table.addCell(buildPdfPCellInfo(fieldValue == null ? "" : String.valueOf(fieldValue), fontInfo));
                        } else {
                            // 序号设置 跳过第一行汇总数据
                            if (!firstRow) {
                                table.addCell(buildPdfPCellInfo(String.valueOf(serialNumber), fontInfo));
                            } else {
                                table.addCell(buildPdfPCellInfo(null, fontInfo));
                            }
                        }
                    }
                    firstRow = false;
                    serialNumber++;
                }
            }
            return table;
        } catch (Exception e) {
            System.out.println(String.format("对账生成pdf报错:%s", e));
        }
        return null;
    }

    private static PdfPCell buildPdfPCellInfo(String cellValue, Font fontInfo) {
        PdfPCell cellInfo = new PdfPCell(new Paragraph(cellValue, fontInfo));
        cellInfo.disableBorderSide(1);
        cellInfo.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellInfo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cellInfo;
    }

    private static  <T> PdfPTable buildPdfPTable(
            List<T> dataList
            , String columnTitle
            , String topTitle
            , String activityName
            , String propertyName
            , float[] columnWidthArrays) {
        List<String> columnTitleList = Arrays.asList(columnTitle.split(","));
        List<String> propertyNameList = Arrays.asList(propertyName.split(","));
        try {
            PdfPTable table = new PdfPTable(columnTitleList.size());
            //// 设置表格宽度比例为%100
            table.setWidthPercentage(100);
            // 也可以每列分别设置宽度
//            table.setTotalWidth(columnWidthArrays);
            // 也可以每列分别设置宽度
            table.setTotalWidth(columnWidthArrays);
            // 锁住宽度
            table.setLockedWidth(true);
            // 设置表格上面空白宽度
            table.setSpacingBefore(0f);
            // 设置表格下面空白宽度
            table.setSpacingAfter(0f);
            // 设置表格默认为无边框
            table.getDefaultCell().setBorder(0);
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font fontTop = new Font(bfChinese, 14, Font.BOLD);
            Font fontTitle = new Font(bfChinese, 8, Font.BOLD);
            //Font fontInfo = new Font(bfChinese, 8,Font.NORMAL);
            //设置头标题
            PdfPCell cell0 = new PdfPCell(new Paragraph(topTitle, fontTop));
            cell0.setBorderColor(BaseColor.WHITE);
            cell0.setColspan(columnTitleList.size());
            cell0.setPaddingLeft(10);
            cell0.setFixedHeight(25);
            cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell0);
            if (StringUtils.isNotBlank(activityName)) {
                //额外：添加活动名称
                PdfPCell cellActivityName = new PdfPCell(new Paragraph(activityName, fontTitle));
//                cellActivityName.setBorderColor(BaseColor.LIGHT_GRAY);
                cellActivityName.setColspan(columnTitleList.size());
                cellActivityName.setPaddingLeft(10);
                cellActivityName.setFixedHeight(18);
                cellActivityName.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellActivityName.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellActivityName.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cellActivityName);
            }

            //设置列表标题
            for (int i = 0; i < columnTitleList.size(); i++) {
                PdfPCell cellTemp = new PdfPCell(new Paragraph(columnTitleList.get(i), fontTitle));
                cellTemp.setFixedHeight(16);
                cellTemp.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellTemp.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cellTemp);
            }
            //填充内容-通过反射
            int serialNumber = 0;
            boolean firstRow = true;
            for (T item : dataList) {
                //首先判定该行数据是否需要加粗数据
                Font fontInfo = new Font(bfChinese, 8, Font.NORMAL);
                try {
                    Field boldFlagField = item.getClass().getDeclaredField("isBoldFlag");
                    boldFlagField.setAccessible(true);
                    String boldFlagValue = String.valueOf(boldFlagField.get(item));
                    if (boldFlagValue != null && "1".equalsIgnoreCase(boldFlagValue)) {
                        fontInfo = new Font(bfChinese, 8, Font.BOLD);
                    }
                } catch (Exception e) {
//                    LOG.error("java反射为没有找到属性isBoldFlag", e);
                }
                for (int i = 0; i < propertyNameList.size(); i++) {
                    if (!StringUtils.contains(propertyNameList.get(i), "serialNumber")) {
                        Field field = item.getClass().getDeclaredField(propertyNameList.get(i));
                        field.setAccessible(true);
                        Object fieldValue = field.get(item);
                        table.addCell(buildPdfPCellInfo(fieldValue == null ? "" : String.valueOf(fieldValue), fontInfo));
                    } else {
                        // 序号设置 跳过第一行汇总数据
                        if (!firstRow) {
                            table.addCell(buildPdfPCellInfo(String.valueOf(serialNumber), fontInfo));
                        } else {
                            table.addCell(buildPdfPCellInfo(null, fontInfo));
                        }
                    }
                }
                firstRow = false;
                serialNumber++;
            }
            return table;
        } catch (Exception e) {
//            LOG.error("对账生成pdf报错：", e);
        }
        return null;
    }
}
