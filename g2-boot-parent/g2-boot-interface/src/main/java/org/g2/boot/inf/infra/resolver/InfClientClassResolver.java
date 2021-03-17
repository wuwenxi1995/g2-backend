package org.g2.boot.inf.infra.resolver;

import org.apache.commons.lang3.StringUtils;
import org.g2.boot.inf.infra.constant.ParamType;
import org.g2.boot.inf.infra.metadata.MethodMetadata;
import org.g2.boot.inf.annotation.Call;
import org.g2.boot.inf.infra.authenticate.adapter.AuthenticatorAdapter;
import org.g2.boot.inf.infra.exception.InfClientException;
import org.g2.boot.inf.infra.metadata.ParamMetadata;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-16
 */
public class InfClientClassResolver implements ApplicationContextAware {

    private final Map<Class, Map<Method, MethodMetadata>> webClientMethodCache;
    private ApplicationContext applicationContext;

    public InfClientClassResolver() {
        webClientMethodCache = new ConcurrentHashMap<>(256);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public MethodMetadata resolveMethod(Class<?> beanClass, Method method) {
        Map<Method, MethodMetadata> methodMetadataMap = webClientMethodCache.computeIfAbsent(beanClass, (k) -> new ConcurrentHashMap<>(16));
        return methodMetadataMap.computeIfAbsent(method, (k) -> {
            MethodMetadata methodMetadata = new MethodMetadata();
            methodMetadata.setSource(method);
            resolveAnnotationCall(methodMetadata, beanClass, method);
            methodMetadata.setResultType(method.getReturnType());

            Map<ParamType, List<ParamMetadata>> paramMetadataMap = new HashMap<>(16);
            Annotation[][] annotations = method.getParameterAnnotations();
            int index = 0;
            for (Annotation[] annotation : annotations) {
                ParamMetadata paramMetadata = resolveParam(index++, annotation);
                paramMetadataMap.computeIfAbsent(paramMetadata.getParamType(), (type) -> new ArrayList<>()).add(paramMetadata);
            }
            methodMetadata.setParamMetadataMap(paramMetadataMap);
            return methodMetadata;
        });
    }

    private ParamMetadata resolveParam(int index, Annotation[] annotations) {
        ParamMetadata paramMetadata = new ParamMetadata();
        paramMetadata.setIndex(index);
        for (Annotation annotation : annotations) {
            if (annotation instanceof RequestParam) {
                RequestParam requestParam = (RequestParam) annotation;
                paramMetadata.setParamType(ParamType.RequestParam);
                paramMetadata.setName(name(requestParam.value(), requestParam.name()));
            } else if (annotation instanceof RequestBody) {
                paramMetadata.setParamType(ParamType.RequestBody);
            } else if (annotation instanceof RequestPart) {
                RequestPart requestPart = (RequestPart) annotation;
                paramMetadata.setParamType(ParamType.RequestPart);
                paramMetadata.setName(name(requestPart.name(), requestPart.value()));
            } else if (annotation instanceof PathVariable) {
                PathVariable pathVariable = (PathVariable) annotation;
                paramMetadata.setParamType(ParamType.PathVariable);
                paramMetadata.setName(name(pathVariable.value(), pathVariable.name()));
            } else {
                paramMetadata.setParamType(ParamType.NONE);
            }
        }
        return paramMetadata;
    }

    private void resolveAnnotationCall(MethodMetadata methodMetadata, Class<?> beanClass, Method method) {
        Call call = AnnotationUtils.findAnnotation(method, Call.class);
        if (call == null) {
            throw new NullPointerException("invoke" + beanClass.getName() + "." + method.getName() + "must appoint @Call");
        }
        methodMetadata.setCode(call.code());
        methodMetadata.setMethod(call.method());
        methodMetadata.setContentType(call.contentType());
        try {
            methodMetadata.setAdapter(new AuthenticatorAdapter(applicationContext.getBean(call.authenticator())));
        } catch (Exception e) {
            throw new InfClientException("Exception when resolve annotation @Call setting Authenticator", e);
        }
    }

    private String name(String value, String name) {
        return StringUtils.isBlank(value) ? name : value;
    }
}
