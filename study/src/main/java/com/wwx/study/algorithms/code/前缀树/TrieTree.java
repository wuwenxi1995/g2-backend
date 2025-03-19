package com.wwx.study.algorithms.code.前缀树;

/**
 * 前缀树：
 * <p>
 * 前缀树是一种用于快速检索的多叉树结构，利用字符串的公共前缀来降低查询时间，核心思想是空间换时间，经常被搜索引擎用于文本词频统计。
 * 优点：最大限度地减少无谓的字符串比较，查询效率高；
 * 缺点：内存消耗较大
 * 特性：
 * （1）不同字符串的相同前缀只保存一份；
 * （2）结点不存放数据，数据存储在树的边上，结点存放字符经过的次数和结束的次数；
 * </p>
 *
 * @author wuwenxi 2022-06-23
 */
public class TrieTree {

    private static class TrieNode {
        private int pass;
        private int end;
        private TrieNode[] nexts;

        TrieNode() {
            this.pass = 0;
            this.end = 0;
            this.nexts = new TrieNode[26];
        }
    }

    public static class Trie {
        private TrieNode head;

        public Trie() {
            this.head = new TrieNode();
        }

        public int searchTrie(String word) {
            if (word == null) {
                return 0;
            }
            TrieNode node = head;
            char[] chars = word.toCharArray();
            int index;
            for (char aChar : chars) {
                index = aChar - 'a';
                if (node.nexts[index] == null) {
                    return 0;
                }
                node = node.nexts[index];
            }
            return node.pass;
        }

        public boolean search(String word) {
            if (word == null) {
                return false;
            }
            TrieNode node = head;
            char[] chars = word.toCharArray();
            int index;
            for (char aChar : chars) {
                index = aChar - 'a';
                if (node.nexts[index] == null) {
                    return false;
                }
                node = node.nexts[index];
            }
            return node.end != 0;
        }

        public void add(String word) {
            if (word == null) {
                return;
            }
            TrieNode node = head;
            node.pass++;
            char[] chars = word.toCharArray();
            int index;
            for (char aChar : chars) {
                // 字符位置
                index = aChar - 'a';
                // 不存在下一级字符,创建字符
                if (node.nexts[index] == null) {
                    node.nexts[index] = new TrieNode();
                }
                // 下一级节点
                node = node.nexts[index];
                node.pass++;
            }
            node.end++;
        }

        public void delete(String word) {
            if (search(word)) {
                TrieNode node = head;
                node.pass--;
                char[] chars = word.toCharArray();
                int index;
                for (char aChar : chars) {
                    index = aChar - 'a';
                    // pass为0,释放空间
                    if (--node.nexts[index].pass == 0) {
                        node.nexts[index] = null;
                        return;
                    }
                    node = node.nexts[index];
                }
                node.end--;
            }
        }
    }
}
