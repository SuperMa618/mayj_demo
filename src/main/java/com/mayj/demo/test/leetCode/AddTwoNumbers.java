package com.mayj.demo.test.leetCode;

import com.mayj.demo.test.node.ListNode;
import com.mayj.demo.test.node.ListNode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @ClassName AddTwoNumbers
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/21 22:47
 **/
public class AddTwoNumbers {

    public static ListNode solution(ListNode l1, ListNode l2) {
        ListNode lastListNode = null;
        Deque<Integer> deque1 = new ArrayDeque<>();
        Deque<Integer> deque2 = new ArrayDeque<>();
        while (l1.next != null) {
            deque1.push((int) l1.value);
            l1 = l1.next;
        }

        while (l2.next != null) {
            deque2.push((int) l2.value);
            l2 = l2.next;
        }

        int rs = 0;
        while (!deque1.isEmpty() || !deque2.isEmpty() || rs != 0) {
            rs += deque1.isEmpty()? 0: deque1.pop();
            rs += deque2.isEmpty()? 0: deque2.pop();
            ListNode node = new ListNode(rs % 10);
            node.next = lastListNode;
            lastListNode = node;
            rs = rs / 10;
        }
        return lastListNode;
    }


    public static void main(String[] args) {

        ListNode A = new ListNode(3);
        ListNode B = new ListNode(4, A);
        ListNode C = new ListNode(2, B);
        ListNode D = new ListNode(7, C);
        ListNode E = new ListNode(4);
        ListNode F = new ListNode(6, E);
        ListNode G = new ListNode(5, F);
        solution(D, G);
        //ListNode H = new ListNode("H");
        //ListNode I = new ListNode("I");
        //ListNode J = new ListNode("J");
        //ListNode K = new ListNode("K");
    }
}
