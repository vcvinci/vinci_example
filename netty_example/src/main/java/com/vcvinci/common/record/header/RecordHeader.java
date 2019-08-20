package com.vcvinci.common.record.header;

import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version v1
 * @date 2018年7月17日 上午9:28:08
 * @description TODO
 */
public interface RecordHeader {

    long HAS_NO_STORE_TIME = -1L;
    long HAS_NO_OFFSET = -1L;

    boolean hasStoreTime();

    short getVersion();

    byte getFlag();

    long getStoreTime();

    long getOffset();

    Struct generateStruct(Schema schema);

}
