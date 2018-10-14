package com.bootdo.common.exception;

/**
 * <p><p/>
 *
 * @ProjectName: 异常类型
 * @ClassName: ExceptionTypeEnum
 * @Description:
 * @Author: sky.liu
 * @Date: 2018/6/19 12:07
 * @Version 1.0
 * @ChangeLog
 */
public enum ExceptionTypeEnum {

    BD("BDException"), APP("APPException");

    private String name;

    ExceptionTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
