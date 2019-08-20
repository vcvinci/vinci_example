package com.vcvinci.common.exception;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年6月10日 下午1:40:42
 * @description 类说明
 */
public enum ExceptionType {

    API("api"), SERVER("server"), CLIENT("client"), COMMON("common"), UNKNOW("unknow");

    ExceptionType(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return this.type;
    }
}
