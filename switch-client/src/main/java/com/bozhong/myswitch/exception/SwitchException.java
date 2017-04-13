package com.bozhong.myswitch.exception;

/**
 * Created by renyueliang on 17/4/11.
 */
public class SwitchException extends RuntimeException {

    private String errorCode;

    private String errorMessage;

    private String detailErrorMessage;

    public SwitchException( String errorCode,String errorMessage,String detailErrorMessage,Throwable e) {
        super(errorCode,e);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.detailErrorMessage =detailErrorMessage;
    }

    public SwitchException( String errorCode,String errorMessage,String detailErrorMessage) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.detailErrorMessage =detailErrorMessage;
    }

    public SwitchException(String errorCode,String errorMessage,Throwable e) {
        super(errorCode,e);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public SwitchException(String errorCode,String errorMessage) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public SwitchException(String errorCode,Throwable e) {
        super(errorCode,e);
        this.errorCode = errorCode;
    }

    public SwitchException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDetailErrorMessage() {
        return detailErrorMessage;
    }

    public void setDetailErrorMessage(String detailErrorMessage) {
        this.detailErrorMessage = detailErrorMessage;
    }
    
}
