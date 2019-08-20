package com.vcvinci.test.sortbymulticondition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author vinci
 * @Title: IdcPartitionMain
 * @ProjectName vc-test
 * @Description: TODO
 * @date 2018/9/4上午10:26
 */
public class IdcPartitionInfoMain {
    public static final String IDC_A = "A";
    public static final String IDC_B = "B";
    public static void main(String[] args) {
        List<IdcPartitionInfo> idcPartitionList = Arrays.asList(
            new IdcPartitionInfo(IDC_A, new Partition(4,0, "a")),
            new IdcPartitionInfo(IDC_B, new Partition(4,1, "b")),
            new IdcPartitionInfo(IDC_A, new Partition(4,2, "d")),
            new IdcPartitionInfo(IDC_B, new Partition(4,3, "e")),
            new IdcPartitionInfo(IDC_B, new Partition(4,4, "f")),
            new IdcPartitionInfo(IDC_A, new Partition(4,0, "sdf")),
            new IdcPartitionInfo(IDC_A, new Partition(4,1, "y")),
            new IdcPartitionInfo(IDC_B, new Partition(4,2, "i")),
            new IdcPartitionInfo(IDC_A, new Partition(4,3, "w")),
            new IdcPartitionInfo(IDC_A, new Partition(4,4, "q"))
        );
        // 比较两个条件进行排序 一级条件idc 二级条件partitionStr
        Collections.sort(idcPartitionList);
        idcPartitionList.forEach(IdcPartitionInfo -> {System.out.println(IdcPartitionInfo.getIdc() + ":" + IdcPartitionInfo.getPartition());});
    }
}
