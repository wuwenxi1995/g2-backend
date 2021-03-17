package org.g2.boot.inf.infra.metadata;

import lombok.Data;
import org.g2.boot.inf.infra.authenticate.adapter.AuthenticatorAdapter;
import org.g2.boot.inf.infra.constant.ParamType;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-17
 */
@Data
public class MethodMetadata {
    private Method source;
    private HttpMethod method;
    private Class<?> resultType;
    private String code;
    private String contentType;
    private AuthenticatorAdapter adapter;
    private Map<ParamType, List<ParamMetadata>> paramMetadataMap;
}
