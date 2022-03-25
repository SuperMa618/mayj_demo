package com.mayj.demo.test.leetCode;

import java.util.*;

/**
 * @ClassName Test1
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/20 12:49
 **/
public class Test1 {

    public static int test(String string, int num) {
        if (Objects.equals(string, null)) {
            return -1;
        }
        Map<String, Integer> map = new HashMap<>();
        int len = string.length();
        char[] chars = string.toCharArray();
        List<Integer> list = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < len; i++) {
            count++;
            if (i == len - 1) {
                //Objects.equals(chars[i], chars[i - 1]) &&
                if (count > map.get(chars[i])) {
                    map.put("" + chars[i], count);
                }
                break;
            }
            if (!Objects.equals(chars[i], chars[i + 1])) {
                if (!map.containsKey(chars[i]) || count > map.get(chars[i])) {
                    map.put("" + chars[i], count);
                }
                count = 0;
            }
        }
        if (num > map.size()) {
            return -1;
        }
        map.forEach((k, v) -> list.add(map.get(k)));
        Collections.sort(list);
        int n = list.size() - (num -1);
        return list.get(n);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        int num = in.nextInt();
        System.out.println(test(s, num));
    }
}
