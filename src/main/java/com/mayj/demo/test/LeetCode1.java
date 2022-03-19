package com.mayj.demo.test;

import com.mayj.demo.test.node.ListNode;
import com.mayj.demo.test.node.TreeNode;
import io.swagger.models.auth.In;

import java.util.*;

/**
 * @ClassName LeetCode1
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/19 15:18
 **/
public class LeetCode1 {



    HashMap<Integer, Integer> map = new HashMap<>();//标记中序遍历
    int[] preorder;//保留的先序遍历，方便递归时依据索引查看先序遍历的值

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        this.preorder = preorder;
        //将中序遍历的值及索引放在map中，方便递归时获取左子树与右子树的数量及其根的索引
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        //三个索引分别为
        //当前根的的索引
        //递归树的左边界，即数组左边界
        //递归树的右边界，即数组右边界
        return recur(0,0,inorder.length-1);
    }

    TreeNode recur(int pre_root, int in_left, int in_right){
        if(in_left > in_right) return null;// 相等的话就是自己
        TreeNode root = new TreeNode(preorder[pre_root]);//获取root节点
        int idx = map.get(preorder[pre_root]);//获取在中序遍历中根节点所在索引，以方便获取左子树的数量
        //左子树的根的索引为先序中的根节点+1
        //递归左子树的左边界为原来的中序in_left
        //递归左子树的右边界为中序中的根节点索引-1
        root.left = recur(pre_root+1, in_left, idx-1);
        //右子树的根的索引为先序中的 当前根位置 + 左子树的数量 + 1
        //递归右子树的左边界为中序中当前根节点+1
        //递归右子树的右边界为中序中原来右子树的边界
        root.right = recur(pre_root + (idx - in_left) + 1, idx+1, in_right);
        return root;

    }


    public static int[] reversePrint(ListNode head) {
        List<Integer> list = new ArrayList<>();

        Stack stack = new Stack<>();
        reverse(head, stack);
        while (!stack.isEmpty()) {
            list.add((Integer) stack.pop());
        }
        int[] a=list.stream().mapToInt(Integer::valueOf).toArray();
        return a;
    }

    public static Stack reverse(ListNode head,Stack stack) {
        if (null == head) return stack;
        stack.push(head.value);
        reverse(head.next, stack);
        return stack;
    }

    /**
     * 最长回文子串
     * @param s
     * @return
     */
    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 1){
            return "";
        }

        // 初始化最大回文子串的起点和终点
        int start = 0;
        int end   = 0;

        // 遍历每个位置，当做中心位
        for (int i = 0; i < s.length(); i++) {
            // 分别拿到奇数偶数的回文子串长度
            int len_odd = expandCenter(s,i,i);
            int len_even = expandCenter(s,i,i + 1);
            // 对比最大的长度
            int len = Math.max(len_odd,len_even);
            // 计算对应最大回文子串的起点和终点
            if (len > end - start){
                start = i - (len - 1)/2;
                end = i + len/2;
            }
        }
        // 注意：这里的end+1是因为 java自带的左闭右开的原因
        return s.substring(start,end + 1);
    }

    /**
     *
     * @param s             输入的字符串
     * @param left          起始的左边界
     * @param right         起始的右边界
     * @return              回文串的长度
     */
    private static int expandCenter(String s,int left,int right){
        // left = right 的时候，此时回文中心是一个字符，回文串的长度是奇数
        // right = left + 1 的时候，此时回文中心是一个空隙，回文串的长度是偶数
        // 跳出循环的时候恰好满足 s.charAt(left) ！= s.charAt(right)
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)){
            left--;
            right++;
        }
        // 回文串的长度是right-left+1-2 = right - left - 1
        return right - left - 1;
    }
    public static void main(String[] args) {

        ListNode a = new ListNode(1);
        ListNode b = new ListNode(2);
        ListNode c = new ListNode(3);
        ListNode d = new ListNode(4);
        ListNode e = new ListNode(5);
        ListNode f = new ListNode(6);
        a.next = b;
        b.next = c;
        c.next = d;
        d.next = e;
        e.next = f;
        System.out.println(Arrays.toString(reversePrint(a)));
        //System.out.println(longestPalindrome("asdffggf"));
    }

}
