package com.mayj.demo.test.leetCode;

import java.util.Objects;

/**
 * @ClassName BoardSearchWord
 * @Description 剑指 Offer 12. 矩阵中的路径 dfs+回溯
 * @Author Mayj
 * @Date 2022/3/19 19:27
 **/
public class BoardSearchWord {

    public Boolean searchWord(char[][] board, String word) {
        if (Objects.equals(board, null) ||
                Objects.equals(word, null) ||
                board.length < 1) return false;
        int row = board.length;
        int col = board[0].length;

        char[] chars = word.toCharArray();
        boolean[][] visited = new boolean[board.length][board[0].length];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {// 从 (0, 0) 点开始进行 dfs 操作，不断地去找，
                // 如果以 (0, 0) 点没有对应的路径的话，那么就从 (0, 1) 点开始去找
                if (dfs(board, chars, visited, i, j, 0)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean dfs(char[][] board, char[] chars, boolean[][] visited, int i, int j, int start) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length
                || chars[start] != board[i][j] || visited[i][j]) {
            return false;
        }
        if (start == chars.length - 1) {
            return true;
        }
        visited[i][j] = true;
        boolean ans;
        ans = dfs(board, chars, visited, i + 1, j, start + 1)
                || dfs(board, chars, visited, i - 1, j, start + 1)
                || dfs(board, chars, visited, i, j + 1, start + 1)
                || dfs(board, chars, visited, i, j - 1, start + 1);
        visited[i][j] = false;
        return ans;
    }
}
