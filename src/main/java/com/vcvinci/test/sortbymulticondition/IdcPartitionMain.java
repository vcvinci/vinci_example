package com.vcvinci.test.sortbymulticondition;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author vinci
 * @Title: IdcPartitionMain
 * @ProjectName vc-test
 * @Description: TODO
 * @date 2018/9/4上午10:26
 */
public class IdcPartitionMain {
    public static final String IDC_A = "A";
    public static final String IDC_B = "B";
    public static void main(String[] args) {
        List<IdcPartition> idcPartitionList = Arrays.asList(
            new IdcPartition("a", IDC_A),
            new IdcPartition("c", IDC_B),
            new IdcPartition("d", IDC_A),
            new IdcPartition("z", IDC_B),
            new IdcPartition("a", IDC_B),
            new IdcPartition("t", IDC_A),
            new IdcPartition("e", IDC_A),
            new IdcPartition("g", IDC_B),
            new IdcPartition("l", IDC_A),
            new IdcPartition("j", IDC_A)
        );
        // 比较两个条件进行排序 一级条件idc 二级条件partitionStr
        Collections.sort(idcPartitionList, new Comparator<IdcPartition>() {
            @Override
            public int compare(IdcPartition o1, IdcPartition o2) {
                int cr = 0;
                // idc 不为空
                if (StringUtils.isNotBlank(o1.getIdc()) && StringUtils.isNotBlank(o2.getIdc())) {
                    int a =  o1.getIdc().compareTo(o2.getIdc());
                    if (a != 0) {
                        cr = a > 0 ? 2 : -1;
                    } else {
                        a = o1.getPartitionStr().compareTo(o2.getPartitionStr());
                        if (a != 0) {
                            cr = a > 0 ? 1 : -2;
                        }
                    }
                    return cr;
                } else {
                    //logger.warn("sort partitions of topic : no have idc info so only use [partitionStr] sorted.");
                    throw new RuntimeException("sort partitions of topic: idc info is null or empty string");
                }
            }
        });
        idcPartitionList.forEach(idcPartition -> {System.out.println(idcPartition.getIdc() + ":" + idcPartition.getPartitionStr());});
    }
}
