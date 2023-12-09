package com.sample.spring.common.exception;

import com.sample.spring.common.util.ContextUtil;
import com.sample.spring.common.util.I18nUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
@Slf4j
public class CustomErrorAttributes extends DefaultErrorAttributes {
    private final MessageSource messageSource;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, options);
        Object exception = webRequest.getAttribute(ErrorAttributes.ERROR_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        int status = (int) map.get("status");
        String message = (String) map.get("message");
        String path = (String) map.get("path");
        message = StringUtils.isEmpty(message) ? (String) map.get("error") : message;
        String code = String.format("E0%s", status);
        if (exception instanceof BizException) {
            code = ((BizException) exception).getError().getValue();
            message = ((Exception) exception).getMessage();
        } else
            message = I18nUtils.messageResolver(code, message);
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> error = new HashMap<>();
        error.put("code", code);
        error.put("message", message);
        error.put("path", path);
        if ((exception instanceof Exception) && ContextUtil.isProfile("dev", "local", "test", "debug")) {
            error.put("description", ((Exception) exception).getLocalizedMessage());
            error.put("exception", ((Exception) exception).getClass().getSimpleName());
        }
        ContextUtil.getTraceContext().ifPresent(v -> response.put("trace_id", v.getTraceId()));
        response.put("error", error);
        response.put("status", String.valueOf(status));
        response.put("result", I18nUtils.messageResolver("FAILED", "Failed"));
        return response;
    }
}