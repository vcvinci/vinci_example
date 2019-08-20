package com.vcvinci.test.sortbymulticondition;

/**
 * @author vinci
 * @Title: IdcPartiotion
 * @ProjectName vc-test
 * @Description: TODO
 * @date 2018/9/4上午10:25
 */
public class IdcPartition {
    private String partitionStr;

    /**
     * 所属机房
     */
    private String idc;


    public IdcPartition(String partitionStr, String idc) {
        this.partitionStr = partitionStr;
        this.idc = idc;
    }

    public String getPartitionStr() {
        return partitionStr;
    }

    public void setPartitionStr(String partitionStr) {
        this.partitionStr = partitionStr;
    }

    public String getIdc() {
        return idc;
    }

    public void setIdc(String idc) {
        this.idc = idc;
    }

    @Override
    public String toString() {
        return "IdcPartition{" +
                "partitionStr='" + partitionStr + '\'' +
                ", idc='" + idc + '\'' +
                '}';
    }
}
