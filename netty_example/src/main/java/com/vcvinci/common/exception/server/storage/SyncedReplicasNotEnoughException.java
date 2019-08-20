package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

/**
 * @author yankai.zhang
 * @since 2019/2/2 下午5:52
 */
public class SyncedReplicasNotEnoughException extends DoveProcessibleException {
    public SyncedReplicasNotEnoughException(int srSize, int minSrSize) {
        super("sr size " + srSize + " is less than min sr size " + minSrSize);
    }
}
