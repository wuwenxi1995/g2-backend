package org.g2.boot.elasticsearch.infra.uitl;

import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.g2.boot.elasticsearch.domain.entity.Page;

/**
 * @author wenxi.wu@hand-china.com 2020-09-24
 */
public final class ElasticsearchPageUtil {

    public static void page(SearchSourceBuilder searchSourceBuilder, Page page) {
        searchSourceBuilder.from(page.getPage() <= 0 ? 0 : page.getPage() * page.getSize());
        searchSourceBuilder.size(page.getSize() <= 0 ? 20 : page.getSize());
    }
}
