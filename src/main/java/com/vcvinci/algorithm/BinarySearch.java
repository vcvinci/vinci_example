package com.vcvinci.algorithm;

import java.util.Arrays;

/**
 * @author vinci
 * @Title: BinarySearch
 * @ProjectName vinci_example
 * @Description: TODO
 * @date 2019/8/2910:40 上午
 */
public class BinarySearch {
    /**
     * 输入: nums = [-1,0,3,5,9,12], target = 9
     * 输出: 4
     * 解释: 9 出现在 nums 中并且下标为 4
     *
     * 输入: nums = [-1,0,3,5,9,12], target = 2
     * 输出: -1
     * 解释: 2 不存在 nums 中因此返回 -1
     */
    public static void main(String[] args) {
        int[] nums =new int[]{-1,0,3,5,9,12};
        Arrays.stream(nums).forEach(System.out::println);



    }

    public static int search(int[] nums, int target) {
        if (nums.length == 0) {
            return -1;
        }
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            }
        }
        return -1;
    }
}
