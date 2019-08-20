package com.vcvinci.test.sortbymulticondition;

import org.apache.commons.lang3.StringUtils;

/**
 * @author vinci
 * @Title: IdcPartitionInfo
 * @ProjectName vc-test
 * @Description: TODO
 * @date 2018/9/4上午11:25
 */
public class IdcPartitionInfo implements Comparable<IdcPartitionInfo>{

    private Partition partition;

    /**
     * 有可能为null
     */
    private String idc;

    public IdcPartitionInfo(String idc, Partition partition){
        this.idc = idc;
        this.partition = partition;
    }

    public String getIdc(){
        return this.idc;
    }

    public Partition getPartition(){
        return this.partition;
    }

    @Override
    public int compareTo(IdcPartitionInfo o) {

        if(StringUtils.isNotBlank(idc) && !idc.equals(o.idc)){
            return this.idc.compareTo(o.idc);
        }

        return this.partition.compareTo(o.partition);
    }
}
