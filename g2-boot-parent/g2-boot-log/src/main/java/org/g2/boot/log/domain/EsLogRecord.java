package org.g2.boot.log.domain;

import lombok.Data;
import org.g2.starter.elasticsearch.infra.annotation.Document;
import org.g2.starter.elasticsearch.infra.annotation.Field;
import org.g2.starter.elasticsearch.infra.enums.Analyzer;
import org.g2.starter.elasticsearch.infra.enums.FieldType;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author wuwenxi 2021-10-13
 */
@Data
@Document(indexName = "g2_log_record")
public class EsLogRecord {

    @Id
    @Field(type = FieldType.KEYWORD, index = false)
    private String id;
    /**
     * 日志类型
     */
    @Field(type = FieldType.KEYWORD)
    private String logRecordType;
    /**
     * 日志编码
     */
    @Field(type = FieldType.KEYWORD)
    private String logRecordCode;
    /**
     * 日志名称
     */
    @Field(type = FieldType.KEYWORD)
    private String logRecordName;
    /**
     * 接口请求内容
     */
    @Field(type = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD)
    private String requestParam;
    /**
     * 接口响应内容
     */
    @Field(type = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD)
    private String responseContent;
    /**
     * 接口响应时间
     */
    @Field(type = FieldType.DATE)
    private Date responseTime;
    /**
     * 接口响应状态
     */
    @Field(type = FieldType.BOOLEAN)
    private boolean isSuccess;
    /**
     * 日志创建时间
     */
    @Field(type = FieldType.DATE)
    private Date createTime;
    /**
     * 异常栈
     */
    @Field(type = FieldType.TEXT, index = false)
    private String stackTrace;
}
