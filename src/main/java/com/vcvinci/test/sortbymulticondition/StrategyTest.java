package com.vcvinci.test.sortbymulticondition;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author vinci
 * @Title: StrategyTest
 * @ProjectName vc-test
 * @Description: TODO
 * @date 2018/9/5下午1:35
 */
public class StrategyTest {
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
        Collections.sort(curPartitions, new Comparator<IdcPartition>() {
            @Override
            public int compare(IdcPartition o1, IdcPartition o2) {
                int cr = 0;
                // idc 不为空
                if (StringUtils.isNotBlank(o1.getIdc()) && StringUtils.isNotBlank(o2.getIdc())) {
                    int a = o1.getIdc().compareTo(o2.getIdc());
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
                    throw new RuntimeException("sort partitions of topic: idc info is null or empty string");
                }
            }
        });

        /*List<String> curConsumers = new ArrayList<>();
        curConsumers.add("consuemr-a-A");
        curConsumers.add("consuemr-b-A");
        curConsumers.add("consuemr-c-A");
        curConsumers.add("consuemr-a-B");
        curConsumers.add("consuemr-b-B");
        curConsumers.add("consuemr-c-B");*/

        /*List<String> curConsumers = new ArrayList<>();
        curConsumers.add("consuemr-a-A");
        curConsumers.add("consuemr-b-A");
        curConsumers.add("consuemr-c-A");
        curConsumers.add("consuemr-a-B");
        curConsumers.add("consuemr-b-B");*/

        /*List<String> curConsumers = new ArrayList<>();
        curConsumers.add("consuemr-a-A");
        curConsumers.add("consuemr-b-A");
        curConsumers.add("consuemr-c-A");
        curConsumers.add("consuemr-d-A");
        curConsumers.add("consuemr-e-A");
        curConsumers.add("consuemr-f-A");
        curConsumers.add("consuemr-g-A");
        curConsumers.add("consuemr-h-A");
        curConsumers.add("consuemr-a-B");
        curConsumers.add("consuemr-b-B");
        curConsumers.add("consuemr-c-B");*/

        List<String> curConsumers = new ArrayList<>();
        curConsumers.add("consuemr-a-A");
        curConsumers.add("consuemr-b-B");

        Map<String, List<String>> idc2Consumers = idc2Consumers(curConsumers);



//        String idc = IDC_A;
        String idc = IDC_B;




        // 当前机房中心的消费者
        List<String> currentConsumers = idc2Consumers.get(idc);
        List<IdcPartition> newParts = allocatedPartitionsNew(curConsumers,curPartitions,0.3d,idc2Consumers,idc,  currentConsumers);
        newParts.forEach(System.out::println);
    }

    public static Map<String, List<String>>  idc2Consumers(List<String> curConsumers) {
        Map<String, List<String>> idc2Consumers = new TreeMap<String, List<String>>();
        for (String consumerIdStr : curConsumers) {
            String idc = idcOfConsumerId(consumerIdStr);
            if (idc2Consumers.containsKey(idc)) {
                idc2Consumers.get(idc).add(consumerIdStr);
            } else {
                List<String> list = new ArrayList<String>();
                list.add(consumerIdStr);
                idc2Consumers.put(idc, list);
            }
        }
        return idc2Consumers;
    }

    public static String idcOfConsumerId (String consuemrId) {
        // todo Vinci
        String[] strs = consuemrId.split("-");
        if (strs.length == 3) {
            return strs[2];
        } else {
            throw new RuntimeException("split length of consumerId is wrong!");
        }
    }



    public static List<IdcPartition> allocatedPartitionsNew(List<String> curConsumers, List<IdcPartition> curPartitions, double ratio, Map<String, List<String>> idc2Consumers, String idc, List<String> currentConsumers) {
        int allPartitionNum = curPartitions.size();
        int allConsumerNum = curConsumers.size();
        Double ratioValue = ratio;
        // 当前机房中心消费者数量
        int currentConsumersNum = CollectionUtils.isEmpty(currentConsumers) ? 0 : currentConsumers.size();
        // 分区容忍数
        int tolerateNum = (int)Math.floor(allPartitionNum * ratioValue) ;
        // 第一个机房应该分配分区数量；  第二个机房分配的分区数量 =  总分区数量 - 第一个机房分配分区数量；
        int firstIdcAllocatePartitionNum = 0;
        // 当前机房中心应该分配的分区数量
        int allocatePartitionNum;
        int start = 0;
        int end = 0;

        if (MapUtils.isEmpty(idc2Consumers)) {
            return Collections.emptyList();
        }
        if (idc2Consumers.size() == 1) {
            if (CollectionUtils.isEmpty(idc2Consumers.get(idc))) {
                return Collections.emptyList();
            } else {
                return curPartitions;
            }
        }
        if (idc2Consumers.size() == 2) {
            // i == 0 表示第一个机房中心  ； i == 1 表示第二个机房中心
            int index = 0;
            for (Map.Entry<String, List<String>> idcEntry : idc2Consumers.entrySet()) {
                if (index > 1) {
                    throw new RuntimeException("机房中心数量不应大于两个！！！");
                }
                String currentIdc = idcEntry.getKey();
                List<String> currentConsumerIds = idcEntry.getValue();
                if (index == 0) {
                    firstIdcAllocatePartitionNum = ((int) Math.floor(allPartitionNum * currentConsumerIds.size() / allConsumerNum) / tolerateNum) * tolerateNum + currentConsumerIds.size();
                    // consumer过多会导致 越界
                    firstIdcAllocatePartitionNum = firstIdcAllocatePartitionNum > allPartitionNum ? allPartitionNum : firstIdcAllocatePartitionNum;
                    if (idc.equals(currentIdc)) {
                        allocatePartitionNum = firstIdcAllocatePartitionNum;
                        start = 0;
                        end = allocatePartitionNum;
                    }
                }

                if (index == 1) {
                    if (idc.equals(currentIdc)) {
                        allocatePartitionNum = firstIdcAllocatePartitionNum;
                        start = allocatePartitionNum;
                        end = allPartitionNum;
                    }
                }
                index ++;
            }
        }
        return curPartitions.subList(start, end);
    }
}
