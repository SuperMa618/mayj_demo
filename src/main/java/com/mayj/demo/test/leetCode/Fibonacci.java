package com.mayj.demo.test.leetCode;

/**
 * @ClassName Fibonacci
 * @Description 斐波那契数列
 * @Author Mayj
 * @Date 2022/3/19 18:41
 **/
public class Fibonacci {

    /**
     * 大佬解
     * @param n
     * @return
     */
    public int fib(int n) {
        if (n <= 1) return n;
        int first = 0;
        int second = 1;
        int result = 0;
        while (--n > 0) {
            result = first + second;
            if (result >= 1000000007) {
                result -= 1000000007;
            }
            first = second;
            second = result;
        }
        return result;

        //动态规划
        //if(n == 0) return 0;
        //        int[] dp = new int[n + 1];
        //        dp[0] = 0;
        //        dp[1] = 1;
        //        for(int i = 2; i <= n; i++){
        //            dp[i] = dp[i-1] + dp[i-2];
        //            dp[i] %= 1000000007;
        //        }
        //        return dp[n];
    }
    //
    //public int fib(int n) {
    //    if (n==1||n==2) return 1;
    //    if (n==0) return 0;
    //    return fib(n-1)+fib(n-2);
    //}

    public static void main(String[] args) {
        Fibonacci fibonacci = new Fibonacci();
        System.out.println(fibonacci.fib(0));
        System.out.println(fibonacci.fib(1));
        System.out.println(fibonacci.fib(2));
        System.out.println(fibonacci.fib(45));

    }
}
