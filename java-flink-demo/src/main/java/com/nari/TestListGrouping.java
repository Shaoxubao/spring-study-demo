package com.nari;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestListGrouping {

    private static Map<Long, List<Long>> c_mp_rela = new ConcurrentHashMap<>() ;

    static {
        c_mp_rela.put(1L, new ArrayList<>());
        c_mp_rela.put(2L, new ArrayList<>());
        c_mp_rela.put(3L, new ArrayList<>());
        c_mp_rela.put(4L, new ArrayList<>());
        c_mp_rela.put(5L, new ArrayList<>());
    }

    private static Map<Integer, List<Long>> c_mp_grouping = new ConcurrentHashMap<>() ;
    public static void main(String[] args) {

        // 分组
        List<Long> keys = new ArrayList<>(c_mp_rela.keySet());
        int groupSize = (int) Math.ceil((double) keys.size() / 3);
        for (int i = 0; i < 3; i++) {
            int start = i * groupSize;
            int end = Math.min(start + groupSize, keys.size());
            if (start < end) {
                List<Long> group = keys.subList(start, end);
                c_mp_grouping.put(i, group);
            }
        }

        for (Map.Entry<Integer, List<Long>> entry : c_mp_grouping.entrySet()) {
            System.out.println("Group " + entry.getKey() + ": " + entry.getValue());
        }
    }
}
