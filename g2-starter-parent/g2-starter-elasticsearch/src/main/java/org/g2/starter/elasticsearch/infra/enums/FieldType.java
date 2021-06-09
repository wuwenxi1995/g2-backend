package org.g2.starter.elasticsearch.infra.enums;

/**
 * @author wuwenxi 2021-06-09
 */
public enum FieldType {

    /**
     * 文本类型
     */
    TEXT("text"),
    /**
     * 关键词
     */
    KEYWORD("keyword"),
    /**
     * 长整型
     */
    LONG("long"),
    /**
     * 双精度浮点类型
     */
    DOUBLE("double"),
    /**
     * 浮点类型
     */
    FLOAT("float"),
    /**
     * 整型
     */
    INTEGER("integer"),
    /**
     * 时间
     */
    DATE("date"),
    /**
     * 布尔
     */
    BOOLEAN("boolean"),
    /**
     * 对象
     */
    OBJECT("object"),
    NESTED("nested");

    private String type;

    FieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
