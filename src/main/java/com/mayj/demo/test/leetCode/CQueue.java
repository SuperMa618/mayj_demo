package com.mayj.demo.test.leetCode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @ClassName CQueue
 * @Description 双栈实现队列
 * @Author Mayj
 * @Date 2022/3/19 18:17
 **/
public class CQueue {
    Deque<Integer> deque1;
    Deque<Integer> deque2;

    public CQueue() {
        deque1 = new ArrayDeque<>();
        deque2 = new ArrayDeque<>();
    }

    public void appendTail(int value) {
        deque1.add(value);
    }

    public int deleteHead() {
        if (deque1.isEmpty() && deque2.isEmpty()) return -1;
        if (!deque2.isEmpty()) return deque2.pop();
        else while (!deque1.isEmpty()) deque2.add(deque1.pop());
        return deque2.pop();
    }

    public static void main(String[] args) {
        CQueue cQueue = new CQueue();
        cQueue.appendTail(1);
        cQueue.appendTail(2);
        cQueue.appendTail(3);
        System.out.println(cQueue.deleteHead());
        cQueue.appendTail(4);
        System.out.println(cQueue.deleteHead());
        cQueue.appendTail(5);
        System.out.println(cQueue.deleteHead());
        System.out.println(cQueue.deleteHead());
        System.out.println(cQueue.deleteHead());
        System.out.println(cQueue.deleteHead());
    }
}
