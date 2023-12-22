package com.sample.spring.common.exception;

import com.sample.spring.common.util.ContextUtil;
import com.sample.spring.common.util.I18nUtils;
import com.sample.spring.enums.SystemType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, options);
        Object exception = webRequest.getAttribute(ErrorAttributes.ERROR_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        int status = (int) map.get(SystemType.STATUS.getValue());
        String message = (String) map.get(SystemType.MESSAGE.getValue());
        String path = (String) map.get(SystemType.PATH.getValue());
        message = StringUtils.isEmpty(message) ? (String) map.get(SystemType.ERROR.getValue()) : message;
        String code = String.format("E0%s", status);
        if (exception instanceof BizException) {
            code = ((BizException) exception).getError().getValue();
            message = ((Exception) exception).getMessage();
        } else
            message = I18nUtils.messageResolver(code, message);
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> error = new HashMap<>();
        Map<String, Object> errorDetail = new HashMap<>();
        error.put(SystemType.CODE.getValue(), code);
        error.put(SystemType.MESSAGE.getValue(), message);
        errorDetail.put(SystemType.PATH.getValue(), path);
        errorDetail.put(SystemType.DESCRIPTION.getValue(), message);
        if ((exception instanceof Exception)) {
            errorDetail.put(SystemType.DESCRIPTION.getValue(), ((Exception) exception).getLocalizedMessage());
            errorDetail.put(SystemType.EXCEPTION.getValue(), ((Exception) exception).getClass().getSimpleName());
        } else if (HttpStatus.NOT_FOUND.value() == status)
            errorDetail.put(SystemType.EXCEPTION.getValue(), InvalidEndpointRequestException.class.getSimpleName());
        else if (HttpStatus.UNAUTHORIZED.value() == status)
            errorDetail.put(SystemType.EXCEPTION.getValue(), UnauthorizedClientException.class.getSimpleName());
        if (!errorDetail.get(SystemType.DESCRIPTION.getValue()).toString().endsWith("."))
            errorDetail.put(SystemType.DESCRIPTION.getValue(), errorDetail.get(SystemType.DESCRIPTION.getValue()) + ".");
        response.put(SystemType.ERROR.getValue(), error);
        if (ContextUtil.isProfileTesting()) {
            errorDetail.putAll(error);
            response.put(SystemType.ERROR.getValue(), errorDetail);
        }
        ContextUtil.getTraceContext().ifPresent(v -> response.put(SystemType.TRACE_ID.getValue(), v.getTraceId()));
        response.put(SystemType.STATUS.getValue(), String.valueOf(status));
        response.put(SystemType.RESULT.getValue(), I18nUtils.messageResolver(SystemType.FAILED.name(), SystemType.FAILED.getValue()));
        return response;
    }
}