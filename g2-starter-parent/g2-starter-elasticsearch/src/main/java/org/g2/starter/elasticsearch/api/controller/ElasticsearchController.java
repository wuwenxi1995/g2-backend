package org.g2.starter.elasticsearch.api.controller;

import org.g2.core.util.Results;
import org.g2.starter.elasticsearch.app.serivce.ElasticsearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
@RestController
@RequestMapping("/v1")
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;

    public ElasticsearchController(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    /**
     * 重建索引
     */
    @PostMapping("/reIndex")
    public ResponseEntity<?> reIndex(String indexName) {
        elasticsearchService.resource(indexName);
        return Results.success();
    }

}


