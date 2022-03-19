package com.mayj.demo.test.node;

/**
 * @ClassName CutNode
 * @Description 剪枝
 * @Author Mayj
 * @Date 2022/3/19 13:53
 **/
public class CutNode {

    public TreeNode cut(TreeNode node) {
        if (null == node) {
            return null;
        }
        node.left = cut(node.left);
        node.right = cut(node.right);
        if (node.value.equals(0) && null == node.left && null == node.right) {
            return null;
        }
        return node;
    }
}
