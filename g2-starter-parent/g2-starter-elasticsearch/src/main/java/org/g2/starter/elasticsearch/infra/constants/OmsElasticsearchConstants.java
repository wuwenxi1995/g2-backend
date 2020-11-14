package org.g2.starter.elasticsearch.infra.constants;

/**
 * @author wenxi.wu@hand-china.com 2020-09-28
 */
public interface OmsElasticsearchConstants {

    interface IndexType {
        String DEFAULT = "_doc";
    }

    interface FiledType {
        String TEXT = "text";
        String KEYWORD = "keyword";

        String LONG = "long";
        String DOUBLE = "double";
        String FLOAT = "float";
        String INTEGER = "integer";
        String DATE = "date";
        String BOOLEAN = "boolean";

        String OBJECT = "object";
        String NESTED = "nested";
    }

    interface Analyzer {
        String IK_SMART = "ik_smart";
        String IK_MAX_WORD = "ik_max_word";
    }
}
