package com.mayj.demo.test.leetCode;

/**
 * @ClassName RobotArea
 * @Description 剑指 Offer 13. 机器人的运动范围
 * @Author Mayj
 * @Date 2022/3/19 21:16
 **/
public class RobotArea {

    public int movingCount(int m, int n, int k) {
        boolean[][] mn = new boolean[m][n];
        return dfs(0,0,m,n,mn,k);
    }

    private int dfs(int i, int j, int m, int n, boolean[][] mn, int k) {

        if (i > m - 1 || j > n - 1 || mn[i][j] ||
                (i / 10 + i % 10 + j / 10 + j % 10) > k) {
            return 0;
        }
        mn[i][j] = true;
        return 1 + dfs(i + 1, j, m, n, mn, k) + dfs(i, j + 1, m, n, mn, k);
    }

    public static void main(String[] args) {
        RobotArea robotArea = new RobotArea();
        System.out.println(robotArea.movingCount(12, 12, 12));
    }
}
