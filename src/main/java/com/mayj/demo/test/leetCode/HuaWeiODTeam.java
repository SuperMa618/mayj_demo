package com.mayj.demo.test.leetCode;

import java.util.*;

/**
 * @ClassName HuaWeiODTeam
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/20 13:56
 **/
public class HuaWeiODTeam {

    public static void main(String[] args) {
        Map<Integer, Set<Integer>> teamMap=new HashMap<>();
        Scanner in = new Scanner(System.in);
        String memAndMsg = in.nextLine();
        String[] stringss = memAndMsg.split(" ");
        int mem = Integer.parseInt(stringss[0]);
        int msg = Integer.parseInt(stringss[1]);
            System.out.println(mem+msg);
        //int mem = in.nextInt();
        //int msg = in.nextInt();
        for (int i = 0; i <= msg; i++) {
            String s = in.nextLine();
            String[] strings = s.split(" ");
            int a, b, c;
            for (String ss :
                    strings) {
                System.out.println(ss);
            }
            a=Integer.parseInt(strings[0]);
            b=Integer.parseInt(strings[1]);
            c=Integer.parseInt(strings[2]);
            if (a > mem || b > mem) {
                System.out.println("da pian zi");
                continue;
            }
            if (c != 0 && c != 1) {
                System.out.println("da pian zi");
                continue;
            }
            if (c == 0) {
                Set<Integer> team = new HashSet<>();
                team.addAll(teamMap.getOrDefault(a, new HashSet<>()));
                team.addAll(teamMap.getOrDefault(b, new HashSet<>()));
                team.add(a);
                team.add(b);
                for (Integer t :
                        team) {
                    teamMap.put(t, team);
                }
            } else {
                System.out.println(teamMap.get(a).contains(b) ? "we are a team" : "we are not a team");
            }
        }
    }
}
//1 2 1
//1 5 0
//2 3 1
//2 5 1
//1 3 2
