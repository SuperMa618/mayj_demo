package com.mayj.demo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ShowMeBug
 * @Description TODO
 * @Author Mayj
 * @Date 2022/3/16 22:08
 **/
public class ShowMeBug {
    static class Node {
        int id;
        int parentId;
        String name;

        public Node(int id, int parentId, String name) 				{
            this.id = id;
            this.parentId = parentId;
            this.name = name;
        }
        public int getId(){
            return id;
        }
        public int getParentId(){
            return parentId;
        }
        public String getName(){
            return name;
        }
        public void setId(int id){
            this.id = id;
        }
        public void setParentId(int parentId){
            this.parentId = parentId;
        }
        public void setName(String name){
            this.name = name;
        }
    }

    public static void main(String[] args) {
        List<Node> nodeList = Arrays.asList(
                new Node(1, 0, "AA"),
                new Node(2, 1, "BB"),
                new Node(3, 1, "CC"),
                new Node(4, 3, "DD"),
                new Node(5, 3, "EE"),
                new Node(6, 2, "FF"),
                new Node(7, 2, "GG"),
                new Node(8, 4, "HH"),
                new Node(9, 5, "II"),
                new Node(10, 0, "JJ"),
                new Node(11, 10, "KK"),
                new Node(12, 10, "LL"),
                new Node(13, 12, "MM"),
                new Node(14, 13, "NN"),
                new Node(15, 14, "OO")
        );
        print(nodeList);
    }

    public static void print(List<Node> nodeList) {
        if(null == nodeList){
            return;
        }
        //缩进
        List<Node> newNodeList = tree(nodeList,0);
        //遍历
        for(Node n : newNodeList){
            System.out.println(n.getName());
        }
    }

    public static List<Node> tree(List<Node> nodeList,int pid){
        List<Node> newNodeList =new ArrayList<>();
        for(Node n : nodeList){
            if(n.getParentId()==pid){
                n.setName("  "+ n.getName());
                newNodeList.addAll(tree(nodeList,n.getParentId()));
            }

        }
        return newNodeList;
    }
}
