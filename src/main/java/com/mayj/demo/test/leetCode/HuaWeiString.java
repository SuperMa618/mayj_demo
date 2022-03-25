package com.mayj.demo.test.leetCode;

import java.util.*;

/**
 * @ClassName HuaWeiString
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/20 14:44
 **/
public class HuaWeiString {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        String str = in.nextLine();
        char[] chars = str.toCharArray();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            String nextStr = next(chars, str.length() - 1, false);
            if (Objects.equals(null, nextStr)) {
                break;
            }
            list.add(nextStr);
            chars = nextStr.toCharArray();
        }
        if (list.isEmpty()) {
            System.out.println("NO");
        }
        for (String s :
                list) {
            //if () {
            //    System.out.println(s);
            //    break;
            //}
        }
    }

    public static String next(char[] array, int num, Boolean def) {
        if (num > array.length - 1 || num < 0) {
            return null;
        }
        if (num == 0 && (array[0] == 'Z' || array[0] == 'z')) {
            return null;
        }
        if (array[num] < 'A' || (array[num] > 'Z' && array[num] < 'a') || array[num] > 'z') {
            return null;
        }
        if (array[num] == 'Z' || array[num] == 'z') {
            return next(array, num - 1, true);
        }
        char[] rs = Arrays.copyOf(array, array.length);
        rs[num] = (char) (array[num] + 1);
        if (def) {
            if (rs[num + 1] == 'Z') {
                rs[num + 1] = 'A';
            } else {
                rs[num + 1] = 'a';
            } 
        }
        return String.valueOf(rs);
    }

}
