package com.mayj.demo.test.node;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

/**
 * @ClassName BinaryTreeTraverse
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/19 13:18
 **/
public class BinaryTreeTraverse {


    /**
     * 前序遍历（递归方式）
     * @param node
     */
    public static void preOrderByRecursion(TreeNode node) {
        if (node != null) {
            System.out.print(node.value);
            preOrderByRecursion(node.left);
            preOrderByRecursion(node.right);
        }
    }

    /**
     * 前序遍历（非递归方式）
     * 1.先入栈根结点，输出根结点value值，再先后入栈其右结点、左结点；
     * 2.出栈左结点，输出其value值，再入栈该左结点的右结点、左结点；直到遍历完该左结点所在子树。
     * 3.再出栈右结点，输出其value值，再入栈该右结点的右结点、左结点；直到遍历完该右结点所在子树。
     * @param root
     */
    public static void preOrder(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        if (root != null) {
            stack.push(root);
        }

        while (!stack.isEmpty()) {
            TreeNode top = stack.pop();
            System.out.print(top.value);
            if (top.right != null) {
                stack.push(top.right);
            }
            if (top.left != null) {
                stack.push(top.left);
            }
        }
    }

    /**
     * 中序遍历（递归方式）
     * @param node
     */
    public static void inOrderByRecursion(TreeNode node) {
        if (node != null) {
            inOrderByRecursion(node.left);
            System.out.print(node.value);
            inOrderByRecursion(node.right);
        }
    }

    /**
     * 中序遍历（非递归方式）
     * 1.首先从根结点出发一路向左，入栈所有的左结点；
     * 2.出栈一个结点，输出该结点value值，查询该结点是否存在右结点，
     * 若存在则从该右结点出发一路向左入栈该右结点所在子树所有的左结点；
     * 3.若不存在右结点，则出栈下一个结点，输出结点value值，同步骤2操作；
     * 4.直到结点为null，且栈为空。
     * @param root
     */
    public static void inOrder(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            if (!stack.isEmpty()) {
                TreeNode top = stack.pop();
                System.out.print(top.value);
                root = top.right;
            }
        }
    }

    /**
     * 后序遍历（递归方式）
     * @param node
     */
    public static void postOrderByRecursion(TreeNode node) {
        if (node != null) {
            postOrderByRecursion(node.left);
            postOrderByRecursion(node.right);
            System.out.print(node.value);
        }
    }

    /**
     * 后序遍历（非递归）
     * 1.首先定义两个stack，将root结点压入stack1
     * 2.stack1弹出栈顶元素，然后将该元素压入stack2，再将该元素的左结点与右结点压入stack1
     * 3.循环步骤2，直到stack1为空，根据栈的LIFO的特性，这样遍历Stack2就会得到后序遍历的结果
     * @param root
     */
    public static void postOrder(TreeNode root) {
        if (root != null) {
            Stack<TreeNode> stack1 = new Stack<>();
            Stack<TreeNode> stack2 = new Stack<>();
            stack1.push(root);
            while (!stack1.isEmpty()) {
                TreeNode top = stack1.pop();
                stack2.push(top);
                if (top.left != null) {
                    stack1.push(top.left);
                }
                if (top.right != null) {
                    stack1.push(top.right);
                }
            }
            while (!stack2.isEmpty()) {
                System.out.print(stack2.pop().value);
            }
        }
    }

    /**
     * 层次遍历
     * @param root
     */
    public static void layerOrder(TreeNode root) {
        Queue<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root);
        }
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.print(node.value);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }

    public static void main(String[] args) {
        TreeNode A = new TreeNode("A");
        TreeNode B = new TreeNode("B");
        TreeNode C = new TreeNode("C");
        TreeNode D = new TreeNode("D");
        TreeNode E = new TreeNode("E");
        TreeNode F = new TreeNode("F");
        TreeNode G = new TreeNode("G");
        TreeNode H = new TreeNode("H");
        TreeNode I = new TreeNode("I");
        TreeNode J = new TreeNode("J");
        TreeNode K = new TreeNode("K");

        A.left = B;
        A.right = C;
        B.left = D;
        B.right = E;
        C.left = F;
        C.right = G;
        D.left = H;
        D.right = I;
        E.right = J;
        F.right = K;

        // 前序遍历(递归)
        System.out.print("前序遍历：");
        preOrderByRecursion(A);
        System.out.println();

        // 前序遍历(非递归)
        System.out.print("前序遍历：");
        preOrder(A);
        System.out.println();

        // 中序遍历(递归)
        System.out.print("中序遍历：");
        inOrderByRecursion(A);
        System.out.println();

        // 中序遍历(非递归)
        System.out.print("中序遍历：");
        inOrder(A);
        System.out.println();

        // 后序遍历(递归)
        System.out.print("后序遍历：");
        postOrderByRecursion(A);
        System.out.println();

        // 后序遍历(非递归)
        System.out.print("后序遍历：");
        postOrder(A);
        System.out.println();

        // 层次遍历
        System.out.print("层次遍历：");
        layerOrder(A);

    }
}
