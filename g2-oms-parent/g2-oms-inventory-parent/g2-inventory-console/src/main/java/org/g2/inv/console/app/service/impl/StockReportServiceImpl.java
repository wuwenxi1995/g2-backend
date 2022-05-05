package org.g2.inv.console.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.g2.core.CoreConstants;
import org.g2.core.exception.CommonException;
import org.g2.inv.console.app.service.StockReportService;
import org.g2.inv.console.infra.constant.Constants;
import org.g2.inv.core.api.dto.StockReportDTO;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.entity.StockReport;
import org.g2.inv.core.domain.entity.StockReportEntry;
import org.g2.inv.core.domain.repository.InvTransactionRepository;
import org.g2.inv.core.domain.repository.StockReportEntryRepository;
import org.g2.inv.core.domain.repository.StockReportRepository;
import org.g2.inv.core.infra.constant.InvCoreConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuwenxi 2022-04-24
 */
@Service
public class StockReportServiceImpl implements StockReportService {

    private final StockReportRepository stockReportRepository;
    private final StockReportEntryRepository stockReportEntryRepository;
    private final InvTransactionRepository invTransactionRepository;

    public StockReportServiceImpl(StockReportRepository stockReportRepository, StockReportEntryRepository stockReportEntryRepository,
                                  InvTransactionRepository invTransactionRepository) {
        this.stockReportRepository = stockReportRepository;
        this.stockReportEntryRepository = stockReportEntryRepository;
        this.invTransactionRepository = invTransactionRepository;
    }

    @Override
    public List<StockReport> list(StockReportDTO stockReport) {
        return stockReportRepository.list(stockReport);
    }

    @Override
    public StockReport detail(String reportCode) {
        StockReport stockReport = new StockReport();
        stockReport.setReportCode(reportCode);
        return stockReportRepository.selectOne(stockReport);
    }

    @Override
    public void create(StockReport stockReport) {
        if (StringUtils.isBlank(stockReport.getSupplierCode())) {
            throw new CommonException("request supplierCode nonNull");
        }
        stockReport.setReportCode(generateCode(Constants.RuleCode.STOCK_REPORT_CODE));
        stockReport.setReportStatusCode(Constants.StockReportStatusCode.CREATE);
        stockReport.setReportDate(new Date());
        stockReportRepository.insertSelective(stockReport);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submit(List<String> reportCodes) {
        if (CollectionUtils.isEmpty(reportCodes)) {
            throw new CommonException("request reportCodes nonNull");
        }
        List<StockReport> stockReports = stockReportRepository.selectCreateReportEntry(reportCodes);
        if (CollectionUtils.isNotEmpty(stockReports)) {
            List<StockReportEntry> stockReportEntries = new ArrayList<>();
            for (StockReport stockReport : stockReports) {
                stockReportEntries.addAll(stockReport.getStockReportEntryList());
            }
            String transactionCode = generateCode("");
            List<InvTransaction> invTransactions = new ArrayList<>(stockReportEntries.size());
            for (StockReportEntry stockReportEntry : stockReportEntries) {
                InvTransaction invTransaction = new InvTransaction();
                invTransaction.setTransactionCode(transactionCode);
                invTransaction.setPosCode(stockReportEntry.getPosCode());
                invTransaction.setSkuCode(stockReportEntry.getSkuCode());
                invTransaction.setOnHandInc(stockReportEntry.getQuantity());
                invTransaction.setReservedInc(0L);
                invTransaction.setTransactionType(InvCoreConstants.TransactionTypeCode.INCREMENT);
                invTransaction.setTransactionSource(InvCoreConstants.TransactionSource.OMS);
                invTransaction.setSourceType(InvCoreConstants.TransactionSource.MANUAL);
                invTransaction.setSourceDate(new Date());
                invTransaction.setProcessStatusCode(CoreConstants.ProcessStatus.PENDING);
                invTransactions.add(invTransaction);
            }
            invTransactionRepository.batchInsertSelective(invTransactions);
            for (StockReport stockReport : stockReports) {
                stockReport.setReportStatusCode(Constants.StockReportStatusCode.SUBMIT);
            }
            stockReportRepository.batchUpdateOptional(stockReports, StockReport.FIELD_REPORT_STATUS_CODE);
        }
    }

    private String generateCode(String code) {
        return "";
    }
}
