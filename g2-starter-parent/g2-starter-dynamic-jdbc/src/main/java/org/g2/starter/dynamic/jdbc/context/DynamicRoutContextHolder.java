package org.g2.starter.dynamic.jdbc.context;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wuwenxi 2023-02-09
 */
public class DynamicRoutContextHolder {

    private static ThreadLocal<DynamicRoutContext> ROUT_CONTEXT = new InheritableThreadLocal<>();

    private DynamicRoutContextHolder() {
    }

    public static void set(DynamicRoutContext dynamicRoutContext) {
        ROUT_CONTEXT.set(dynamicRoutContext);
    }

    public static DynamicRoutContext get() {
        return ROUT_CONTEXT.get();
    }

    public static void clear() {
        ROUT_CONTEXT.remove();
    }

    public static DynamicRoutContext build(String organizationId, String method, String uri) {
        return DynamicRoutContext.builder()
                .organizationId(organizationId)
                .method(method)
                .uri(uri)
                .build();
    }

    @Builder
    @Getter
    @Setter
    public static class DynamicRoutContext {

        private DynamicRoutContext() {
        }

        private String organizationId;
        private String method;
        private String uri;
    }
}
