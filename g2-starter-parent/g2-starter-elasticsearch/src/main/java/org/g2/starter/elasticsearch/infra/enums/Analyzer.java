package org.g2.starter.elasticsearch.infra.enums;

/**
 * @author wuwenxi 2021-06-09
 */
public enum Analyzer {

    /**
     * 分词类型
     */
    DEFAULT(""),
    /**
     * 简单分词
     */
    IK_SMART("ik_smart"),
    /**
     * 最大分词
     */
    IK_MAX_WORD("ik_max_word");

    private String analyzer;

    Analyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public String getAnalyzer() {
        return analyzer;
    }
}
