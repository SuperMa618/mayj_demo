package com.mayj.demo.test.leetCode;

/**
 * @ClassName cuttingRope
 * @Description 剑指 Offer 14- I. 剪绳子
 * @Author Mayj
 * @Date 2022/3/19 22:26
 **/
public class cuttingRope {
    /*
    数论 优解 3的次方是最大的
    public int cuttingRope(int n) {
    if (n<=3) return n-1;
    int div = n/3;
    int rem = n % 3;
    long result = 1;
    for (int i = 0; i < div; i++) {
      result *= i<div-1 ? 3 : (rem == 2 ? 3*rem : (3+rem));
      if (result >= 1000000007) {
        result = result%1000000007;
      }
    }
    return (int)result;
  }
     */

    /**
     * 动态规划
     * @param n
     * @return
     */
    public int cuttingRope(int n) {
        // f(0)=1,f(1)=1,f(2)=1*1,f(3)=2
        if (n <= 3) {
            return Math.max(n - 1, 1);
        }
        // dp思路：砍成两段的最大乘积是 f(n) = f(x)*f(n-x)
        // f(2)虽然=1，在计算的时候的等于2，比如4=2*2=4,不能是1*1
        // f(3)虽然=2，在计算的时候的等于3，比如6=3*3=9,不能是2*2
        // 也就是说在dp计算的时候,f(x)的最小值只能是x,不能小于x
        // 在x>=4的时候计算出来都是大于x,所以不需要特殊定义
        int[] f = new int[n + 1];
        f[1] = 1;
        f[2] = 2;
        f[3] = 3;
        int ans = 1;
        for (int i = 4; i <= n; i++) {
            for (int j = 1; j <= i / 2; j++) {
                ans = Math.max(ans, f[j] * f[i - j]);
            }
            f[i] = ans;
            ans = 0;
        }
        return f[n];
    }
}
