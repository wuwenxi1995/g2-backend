package com.wwx.study.pdf;


import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wuwenxi 2022-03-18
 */
@Data
public class CsgMonthBillExportDTO {
    // 订单

    private String platformOrderCode;
    private String customerName;
    private String mobileNumber;
    private String companyName;
    private String unitName;
    private String activityCode;
    private String activityName;
    private String platformCreatedTime;
    private String receiveDate;
    private String platformProductCode;
    private String skuCode;
    private String title;
    private String baseUomCode;
    private String baseUomCodeMeaning;
    private BigDecimal purchasePrice;
    private BigDecimal unitPrice;
    private Long quantity;
    private BigDecimal totalAmount;
    private BigDecimal salesAmount;
    private String isRefund;
    private Long refundQuantity;
    private BigDecimal refundTotalAmount;
    private String billPeriodFrom;
    private String billPeriodTo;
    private String refundLastDate;
    private BigDecimal actualTotalAmount;
    private BigDecimal redRefundAmount;
    private BigDecimal actualPaidTotalAmount;

    // 退款单

    private String platformCode;
    private String refundTypeCode;
    private String refundTypeCodeMeaning;
    private String refundPlatformCreatedDate;
    private String refundPlatformProductCode;
    private String refundPlatformSkuCode;
    private String refundTitle;
    private String refundDate;
    private String refundBaseUomCode;
    private String refundBaseUomCodeMeaning;

    private String billCode;
    private Integer isRefundFlag;

    @Data
    public static class CsgMonthBillOrderSupplierData {
        private String platformOrderCode;
        private String platformCreatedTime;
        private String receiveDate;
        private String platformProductCode;
        private String skuCode;
        private String title;
        private String baseUomCodeMeaning;
        private BigDecimal purchasePrice;
        private Long quantity;
        private BigDecimal totalAmount;
        private String isRefund;
        private Long refundQuantity;
        private BigDecimal refundTotalAmount;
        private String billPeriodFrom;
        private String billPeriodTo;
        private String refundLastDate;
        private BigDecimal actualTotalAmount;
        private BigDecimal redRefundAmount;
        private BigDecimal actualPaidTotalAmount;
        private String isBoldFlag;
    }

    @Data
    public static class CsgMonthBillOrderPlatformData {
        private String platformOrderCode;
        private String customerName;
        private String mobileNumber;
        private String companyName;
        private String unitName;
        private String activityCode;
        private String activityName;
        private String platformCreatedTime;
        private String receiveDate;
        private String platformProductCode;
        private String skuCode;
        private String title;
        private String baseUomCodeMeaning;
        private BigDecimal purchasePrice;
        private BigDecimal unitPrice;
        private Long quantity;
        private BigDecimal totalAmount;
        private BigDecimal salesAmount;
        private String isRefund;
        private Long refundQuantity;
        private BigDecimal refundPurchasePrice;
        private BigDecimal refundTotalAmount;
        private String billPeriodFrom;
        private String billPeriodTo;
        private String refundLastDate;
        private BigDecimal actualTotalAmount;
        private BigDecimal redRefundAmount;
        private BigDecimal actualPaidTotalAmount;
        private String isBoldFlag;
    }

    @Data
    public static class CsgMonthBillRefundData {
        private String platformCode;
        private String refundTypeCodeMeaning;
        private String platformOrderCode;
        private String refundPlatformCreatedDate;
        private String refundPlatformProductCode;
        private String refundPlatformSkuCode;
        private String refundTitle;
        private String refundBaseUomCodeMeaning;
        private BigDecimal purchasePrice;
        private Long refundQuantity;
        private BigDecimal refundTotalAmount;
        private String refundDate;
        private String billPeriodFrom;
        private String billPeriodTo;
        private String refundLastDate;
        private String isBoldFlag;
    }

    @Data
    public static class CsgMonthBillRedRefundPlatformData {
        private String platformCode;
        private String refundTypeCodeMeaning;
        private String platformOrderCode;
        private String customerName;
        private String mobileNumber;
        private String companyName;
        private String unitName;
        private String activityCode;
        private String activityName;
        private String receiveDate;
        private String refundPlatformCreatedDate;
        private String refundPlatformProductCode;
        private String refundPlatformSkuCode;
        private String refundTitle;
        private String refundBaseUomCodeMeaning;
        private BigDecimal purchasePrice;
        private Long refundQuantity;
        private BigDecimal refundTotalAmount;
        private String refundDate;
        private String billPeriodFrom;
        private String billPeriodTo;
        private String refundLastDate;
        private String isBoldFlag;
    }

    @Data
    public static class CsgMonthBillRedRefundSupplierData {
        private String platformCode;
        private String refundTypeCodeMeaning;
        private String platformOrderCode;
        private String receiveDate;
        private String refundPlatformCreatedDate;
        private String refundPlatformProductCode;
        private String refundPlatformSkuCode;
        private String refundTitle;
        private String refundBaseUomCodeMeaning;
        private BigDecimal purchasePrice;
        private Long refundQuantity;
        private BigDecimal refundTotalAmount;
        private String refundDate;
        private String billPeriodFrom;
        private String billPeriodTo;
        private String refundLastDate;
        private String isBoldFlag;
    }

    @Data
    public static class PdfOrderDTO {
        private String activityCode;
        private String categoryName;
        private String supplierName;
        private String companyName;
        private String unitName;
        private String name;
        private String title;
        private BigDecimal activityTotalAmount;
        private BigDecimal cashAmount;
        private String signStatus;
    }

    @Data
    public static class PdfOrderEntryDTO {
        private String activityCode;
        private String categoryName;
        private String supplierName;
        private String title;
        private BigDecimal unitPrice;
        private String description;
        private String size;
        private String colour;
        private Long quantity;
        private BigDecimal cardLineAmount;
        private BigDecimal cashPaidAmount;
    }

}
