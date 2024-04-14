package jmu.lsk.common.exception;


import jmu.lsk.model.common.enums.AppHttpCodeEnum;

public class CustomException extends RuntimeException {

    private AppHttpCodeEnum appHttpCodeEnum;

    public CustomException(AppHttpCodeEnum appHttpCodeEnum){
        this.appHttpCodeEnum = appHttpCodeEnum;
    }

    public CustomException(AppHttpCodeEnum appHttpCodeEnum, String msg){
        this.appHttpCodeEnum = appHttpCodeEnum;
    }

    public AppHttpCodeEnum getAppHttpCodeEnum() {
        return appHttpCodeEnum;
    }
}
