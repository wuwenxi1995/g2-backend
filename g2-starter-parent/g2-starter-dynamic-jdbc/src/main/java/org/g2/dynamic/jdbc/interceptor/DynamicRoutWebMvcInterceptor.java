package org.g2.dynamic.jdbc.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.g2.dynamic.jdbc.context.DynamicRoutContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author wuwenxi 2023-02-09
 */
public class DynamicRoutWebMvcInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(DynamicRoutWebMvcInterceptor.class);

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> uriTemplateVariables = (Map) request.getAttribute("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables");
        String organizationIdStr = uriTemplateVariables == null ? null : uriTemplateVariables.get("organizationId");
        if (StringUtils.isBlank(organizationIdStr)) {
            log.error("UriTemplateVariables['organizationId'] is null, skip building TenantContext.");
            return true;
        }
        String method = request.getMethod();
        String uri = request.getRequestURI();
        DynamicRoutContextHolder.set(DynamicRoutContextHolder.build(organizationIdStr, method, uri));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        DynamicRoutContextHolder.clear();
    }
}
