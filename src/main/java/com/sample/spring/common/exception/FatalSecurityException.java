package com.sample.spring.common.exception;

import com.sample.spring.common.consts.ErrorCodeType;
import com.sample.spring.enums.BizErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class FatalSecurityException extends AuthenticationException implements ErrorCodeType {

    private static final long serialVersionUID = -1867514206767286002L;

    protected String value;
    protected BizErrorCode systemCode;

    public FatalSecurityException(BizErrorCode systemCode) {
        super(systemCode == null ? null : systemCode.getDescription());
        this.systemCode = systemCode;
        if (systemCode != null)
            this.value = systemCode.getValue();
    }

    public FatalSecurityException(BizErrorCode systemCode, Throwable cause) {
        super(systemCode == null ? null : systemCode.getDescription(), cause);
        this.systemCode = systemCode;
        if (systemCode != null)
            this.value = systemCode.getValue();
    }

    public FatalSecurityException(String message) {
        super(message);
        this.value = message;
    }

    public FatalSecurityException(String value, String message) {
        super(message);
        this.value = value;
    }

    public FatalSecurityException(String value, Throwable cause) {
        super(value, cause);
        this.value = value;
    }

    public FatalSecurityException(String value, String message, Throwable cause) {
        super(message, cause);
        this.value = value;
    }


    @SuppressWarnings("unchecked")
    public <T extends FatalSecurityException> T setValue(String value) {
        this.value = value;
        return (T) this;
    }

//    @Override
    public BizErrorCode getSystemCode() {
        return systemCode;
    }

//    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
