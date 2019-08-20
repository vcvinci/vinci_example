package com.vcvinci.common.metadata;

import java.util.Objects;

/**
 * @author zhixing.wang E-mail:zhixing.wang@ucarinc.com
 * @version 创建时间：2019/2/19 16:46
 * @description MetadataVersion
 */
public class MetadataVersion {
    public static final int UNKNOW_CONTROLLER_EPOCH = -1;
    public static final int UNKNOW_METADATA_VERSION = -1;
    private int controllerEpoch;
    private int metadataVersion;

    public MetadataVersion() {
        new MetadataVersion(UNKNOW_CONTROLLER_EPOCH, UNKNOW_METADATA_VERSION);
    }

    public MetadataVersion(int controllerEpoch, int metadataVersion) {
        this.controllerEpoch = controllerEpoch;
        this.metadataVersion = metadataVersion;
    }

    public void updateVersion(int controllerEpoch, int metadataVersion) {
        this.controllerEpoch = controllerEpoch;
        this.metadataVersion = metadataVersion;
    }

    public boolean isNewThan(MetadataVersion metadataVersion) {
        if (this.controllerEpoch > metadataVersion.controllerEpoch) {
            return true;
        } else if (this.controllerEpoch == metadataVersion.controllerEpoch && this.metadataVersion > metadataVersion.metadataVersion) {
            return true;
        } else {
            return false;
        }
    }

    public int getControllerEpoch() {
        return controllerEpoch;
    }

    public int getMetadataVersion() {
        return metadataVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(controllerEpoch, metadataVersion);
    }

    @Override
    public String toString() {
        return "MetadataVersion{" +
                "controllerEpoch=" + controllerEpoch +
                ", metadataVersion=" + metadataVersion +
                '}';
    }
}
