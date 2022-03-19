package com.mayj.demo.test.node;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @ClassName Depth
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/19 13:39
 **/
public class Depth {

    /**
     * 求最大深度（递归）
     * @param root
     * @return
     */
    public static int maxDepthByRecursion(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = maxDepthByRecursion(root.left);
        int right = maxDepthByRecursion(root.right);
        return Math.max(left, right) + 1;
    }

    /**
     * 求最大深度（非递归）
     * @param root
     * @return
     */
    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 结点不断入队出队的过程，直到左右都无叶子结点
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int level = 0;
        while (!queue.isEmpty()) {
            level ++;
            int levelNum = queue.size();
            for (int i = 0; i < levelNum; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return level;
    }

    /**
     * 求最小深度（递归）
     * @param root
     * @return
     */
    public static int minDepthByRecursion(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // 无叶子结点的情况
        if (root.left == null && root.right == null) {
            return 1;
        }

        // 左子结点为空的情况
        if (root.left == null && root.right != null) {
            return minDepthByRecursion(root.right) + 1;
        }

        // 右子结点为空的情况
        if (root.left != null && root.right == null) {
            return minDepthByRecursion(root.left) + 1;
        }

        int left = minDepthByRecursion(root.left);
        int right = minDepthByRecursion(root.right);

        return Math.min(left, right) + 1;
    }

    /**
     * 求最小深度（非递归）
     * @param root
     * @return
     */
    public static int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int level = 0;
        while (!queue.isEmpty()) {
            level ++;
            int levelNum = queue.size();
            for (int i = 0; i < levelNum; i++) {
                TreeNode node = queue.poll();
                // 当出现第一个无叶子结点的结点时，该结点的深度为最小深度
                if (node.left == null && node.right == null) {
                    return level;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return level;
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

        System.out.println(maxDepthByRecursion(A));
        System.out.println(maxDepth(A));
        System.out.println(minDepthByRecursion(A));
        System.out.println(minDepth(A));
    }
}
