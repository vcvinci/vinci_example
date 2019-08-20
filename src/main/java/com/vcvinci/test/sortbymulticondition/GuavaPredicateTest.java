package com.vcvinci.test.sortbymulticondition;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vinci
 * @Title: GuavaPredicateTest
 * @ProjectName vc-test
 * @Description: TODO
 * @date 2018/9/19上午10:23
 */
public class GuavaPredicateTest {
    public static final String IDC_A = "A";
    public static final String IDC_B = "B";
    public static void main(String[] args) {
        List<IdcPartition> curPartitions = Arrays.asList(
                new IdcPartition("partition_0_0", IDC_A),
                new IdcPartition("partition_0_1", IDC_B),
                new IdcPartition("partition_0_2", IDC_A),
                new IdcPartition("partition_1_0", IDC_B),
                new IdcPartition("partition_1_1", IDC_B),
                new IdcPartition("partition_1_2", IDC_A),
                new IdcPartition("partition_2_0", IDC_A),
                new IdcPartition("partition_2_1", IDC_B),
                new IdcPartition("partition_2_2", IDC_A),
                new IdcPartition("partition_2_3", IDC_A)
        );

        final List<String> allocatedPartitions = new ArrayList<>();
        allocatedPartitions.add("partition_0_0");
        allocatedPartitions.add("partition_0_1");
        allocatedPartitions.add("partition_0_2");


        List<IdcPartition> partitions = Lists.newArrayList(Collections2.filter(curPartitions, new Predicate<IdcPartition>() {
            @Override
            public boolean apply(IdcPartition input) {
                if (allocatedPartitions.contains(input.getPartitionStr())) {
                    return false;
                } else {
                    return true;
                }
            }
        }));

        partitions.forEach(v -> System.out.println(v.getPartitionStr()));
    }

}
