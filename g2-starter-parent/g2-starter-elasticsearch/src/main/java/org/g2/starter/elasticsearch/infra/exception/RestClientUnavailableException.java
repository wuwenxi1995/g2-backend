package org.g2.starter.elasticsearch.infra.exception;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;

/**
 * @author wuwenxi 2021-05-30
 */
public class RestClientUnavailableException extends ElasticsearchException {

    public RestClientUnavailableException(String message) {
        super(message);
    }

    public RestClientUnavailableException(String message, Throwable t) {
        super(message, t);
    }

    public RestClientUnavailableException(StreamInput in) throws IOException {
        super(in);
    }

    @Override
    public RestStatus status() {
        return RestStatus.GATEWAY_TIMEOUT;
    }
}
