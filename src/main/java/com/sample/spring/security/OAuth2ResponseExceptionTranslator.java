package com.sample.spring.security;

import com.sample.spring.common.exception.FatalSecurityException;
import com.sample.spring.common.util.ContextUtil;
import com.sample.spring.common.util.I18nUtils;
import com.sample.spring.enums.SystemType;
import com.sample.spring.security.dto.OAuth2ExceptionResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class OAuth2ResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    @Override
    @SneakyThrows
    public ResponseEntity<OAuth2Exception> translate(Exception ex) {
        ResponseEntity<OAuth2Exception> responseEntity = super.translate(ex);
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(responseEntity.getHeaders().toSingleValueMap());
        Map<String, Object> response = new HashMap<>();
        int status = responseEntity.getStatusCode().value();
        response.put(SystemType.RESULT.getValue(), I18nUtils.messageResolver(SystemType.FAILED.name(), SystemType.FAILED.getValue()));
        ContextUtil.getTraceContext().ifPresent(v -> response.put(SystemType.TRACE_ID.getValue(), v.getTraceId()));
        response.put(SystemType.STATUS.getValue(), String.valueOf(status));
        String code = String.format("E0%s", status);
        Map<String, Object> error = new HashMap<>();
        error.put(SystemType.CODE.getValue(), code);
        String exception = ex.getClass().getSimpleName();
        Map<String, Object> errorDetail = new HashMap<>(Map.of(SystemType.EXCEPTION.getValue(), exception));
        String message = ex.getLocalizedMessage();
        this.exceptionCheck(ex, error, errorDetail);
        error.put(SystemType.MESSAGE.getValue(), I18nUtils.messageResolver(code, message));
        response.put(SystemType.MESSAGE.getValue(), I18nUtils.messageResolver(code, message));
        response.put(SystemType.ERROR.getValue(), error);
        if (ContextUtil.isProfileTesting()) {
            errorDetail.putAll(error);
            response.put(SystemType.ERROR.getValue(), errorDetail);
        }
        return ResponseEntity.status(HttpStatus.valueOf(status)).headers(headers).body(new OAuth2ExceptionResponse(response));
    }

    private void exceptionCheck(Exception ex, Map<String, Object> error, Map<String, Object> errorDetail) {
        if (ex instanceof FatalSecurityException) {
            errorDetail.put(SystemType.DESCRIPTION.getValue(), ((FatalSecurityException) ex).getDescription());
        } else if (ex instanceof InvalidTokenException) {
            if ("Token has expired".equals(ex.getMessage()) || (StringUtils.isNotEmpty(ex.getMessage()) && ex.getMessage().contains("Access token expired")))
                errorDetail.put(SystemType.DESCRIPTION.getValue(), "Access token has expired");
            else if (StringUtils.isNotEmpty(ex.getMessage()) && ex.getMessage().contains("Client not valid")) {
                String exMsg = ex.getMessage();
                String clientId = exMsg.substring(exMsg.indexOf(":") + 2);
                errorDetail.put(SystemType.DESCRIPTION.getValue(), "Client " + clientId + " not found");
            } else
                errorDetail.put(SystemType.DESCRIPTION.getValue(), "Invalid access token");
        } else if (ex instanceof InvalidGrantException)
            errorDetail.put(SystemType.DESCRIPTION.getValue(), "Unauthorized access");
        else if (ex instanceof BadCredentialsException)
            errorDetail.put(SystemType.DESCRIPTION.getValue(), "Incorrect username or password");
        else if (ex instanceof OAuth2Exception) {
            OAuth2Exception e = (OAuth2Exception) ex;
            errorDetail.put(SystemType.DESCRIPTION.getValue(), e.getMessage());
        } else if (ex instanceof UsernameNotFoundException)
            errorDetail.put(SystemType.DESCRIPTION.getValue(), ex.getMessage());
        else if (ex instanceof AuthenticationException) {
            AuthenticationException e = (AuthenticationException) ex;
            errorDetail.put(SystemType.DESCRIPTION.getValue(), e.getMessage());
        } else if (ex instanceof AccessDeniedException)
            error.put(SystemType.DESCRIPTION.getValue(), ex.getMessage());
    }
}
