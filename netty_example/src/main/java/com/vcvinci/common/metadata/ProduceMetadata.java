package com.vcvinci.common.metadata;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 上午10:32:58
 * @description 类说明
 */
public class ProduceMetadata {

    private short requiredAcks;
    private short requiredOffset;
    private int error;
    private long baseOffset;
    private long recordAppendTime;
    private long recordStartOffset;
}
