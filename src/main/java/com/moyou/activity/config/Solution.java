package com.moyou.activity.config;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    public   static  int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                System.out.println(map.get(complement)+"======"+i);
                return new int[] {  map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        return new int[]{};
    }


    public static void main(String[] args) {
        int[] nums = new int[]{1,2,3,5,6,7,8,10,11,15,23,25,27,34,56,78,89};
        twoSum(nums,52);
    }
}
