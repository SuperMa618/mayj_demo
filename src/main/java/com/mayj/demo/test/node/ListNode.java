package com.mayj.demo.test.node;

/**
 * @ClassName ListNode
 * @Description 链表
 * @Author Mayj
 * @Date 2022/3/19 12:45
 **/
//@AllArgsConstructor
public class ListNode {
    public Object value;
    public ListNode next;
    public ListNode() {}
    public ListNode(Object value) {
        this.value = value;
    }
    public ListNode(int value, ListNode next) { this.value = value; this.next = next; }
}
