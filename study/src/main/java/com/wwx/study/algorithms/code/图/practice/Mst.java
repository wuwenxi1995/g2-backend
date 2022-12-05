package com.wwx.study.algorithms.code.图.practice;

import com.wwx.study.algorithms.code.图.Edge;
import com.wwx.study.algorithms.code.图.Graph;
import com.wwx.study.algorithms.code.图.Node;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 最小生成树
 *
 * @author wuwenxi 2022-05-31
 */
public class Mst {

    /**
     * kruskal 算法：要求无向图
     * <p>
     * 算法思想：
     * 1. 将所有边按权值大小排序
     * 2. 遍历边集合
     * 3. 使用并查集{@link UnionFind}记录所有符合条件的边对应的点
     * 4. 使用集合记录符合条件的边
     * <p>
     * 核型要点: 并查集
     * </p>
     */
    public static <T> Set<Edge<T>> kruskalMst(Graph<T> graph) {
        UnionFind<T> unionFind = new UnionFind<>();
        unionFind.markSet(graph.getNodes().values());
        // 对边权值排序
        PriorityQueue<Edge<T>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Edge::getWeight));
        priorityQueue.addAll(graph.getEdges());
        // 保存符合条件对边
        Set<Edge<T>> result = new HashSet<>();
        while (!priorityQueue.isEmpty()) {
            Edge<T> edge = priorityQueue.poll();
            // 使用并查集判断
            if (!unionFind.isSameSet(edge.getFrom(), edge.getTo())) {
                unionFind.union(edge.getFrom(), edge.getTo());
                result.add(edge);
            }
        }
        return result;
    }

    /**
     * prim 算法： 要求无向图
     * <p>
     * 算法思想：
     * 1. 随机选择一个点
     * 2. 选择这个点的一条权值最小的边，其他边加入边集合中
     * 3. 选择这条边的to点，判断点集合是否存在该点，将已处理的点加入集合，重复2、3
     * 4. 判断点集合是否已存在全部点
     * 4.1 全部点已存在，结束
     * 4.2 将边集合按照权值大小排序，执行2、3
     * </p>
     */
    public static <T> Set<Edge<T>> primMst(Graph<T> graph) {
        // 优先级队列存储边
        PriorityQueue<Edge<T>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Edge::getWeight));
        // set存储符合条件的点
        Set<Node<T>> container = new HashSet<>();
        // 返回结果
        Set<Edge<T>> result = new HashSet<>();
        // for循环的原因：处理多个图互相独立的情况
        for (Node<T> node : graph.getNodes().values()) {
            // 判断结点是否已经处理
            if (!container.contains(node)) {
                // 记录已处理结点
                container.add(node);
                // 将当前结点所有边加入优先级队列
                for (Edge<T> edge : node.getEdges()) {
                    priorityQueue.offer(edge);
                }
                while (!priorityQueue.isEmpty()) {
                    // 从优先级队列取出边
                    Edge<T> edge = priorityQueue.poll();
                    Node<T> to = edge.getTo();
                    // 判断边的to点是否已处理
                    if (!container.contains(to)) {
                        // 记录新结点
                        container.add(to);
                        // 记录返回结果
                        result.add(edge);
                        // 将新接点所有边加入优先级队列
                        for (Edge<T> toEdge : to.getEdges()) {
                            priorityQueue.offer(toEdge);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * dijkstra 算法：给定一个点，要求返回所有到达其他点的最短距离，不可达点距离为∞；要求不能有权值累加和为负数的路径
     * <p>
     * 1. 从给定点A出发，记录这个点所有边到达点的距离
     * 2. 筛选最短距离的点P，记录最短距离D
     * 3. 从P点继续执行1、2，在记录P到所有边的点距离时并加上最短距离D，并且跳过已经处理过的点A
     * </p>
     */
    public static <T> Map<Node<T>, Integer> dijkstra(Node<T> head) {
        // 存放头节点到每个结点最短距离集合
        Map<Node<T>, Integer> distanceMap = new HashMap<>();
        // 存放已经找到最短距离到结点
        Set<Node<T>> selectionNodes = new HashSet<>();
        distanceMap.put(head, 0);
        // 从集合中筛选距离最短且没有被处理到结点
        Node<T> node = getMinDistanceAndNonSelectionNode(distanceMap, selectionNodes);
        while (node != null) {
            // 源节点和头节点的距离
            Integer originalDistance = distanceMap.get(node);
            // 遍历源节点全部边
            // 将源节点每条边所到节点的权值加上源节点和头节点距离，和原来存储的值比较，较小的值为最短距离
            for (Edge<T> edge : node.getEdges()) {
                if (distanceMap.computeIfPresent(edge.getTo(), (key, value) -> Math.min(value, edge.getWeight() + originalDistance)) == null) {
                    distanceMap.put(edge.getTo(), originalDistance + edge.getWeight());
                }
            }
            // 记录已经处理过的节点
            selectionNodes.add(node);
            // 重新找到一个距离最短的节点
            node = getMinDistanceAndNonSelectionNode(distanceMap, selectionNodes);
        }
        return distanceMap;
    }

    private static <T> Node<T> getMinDistanceAndNonSelectionNode(Map<Node<T>, Integer> distanceMap, Set<Node<T>> selectionSet) {
        Node<T> minNode = null;
        Integer minDistance = Integer.MAX_VALUE;
        for (Map.Entry<Node<T>, Integer> entry : distanceMap.entrySet()) {
            if (!selectionSet.contains(entry.getKey()) && entry.getValue() < minDistance) {
                minNode = entry.getKey();
                minDistance = entry.getValue();
            }
        }
        return minNode;
    }


    /**
     * 并查集
     */
    private static class UnionFind<T> {

        void markSet(Collection<Node<T>> nodes) {
        }

        boolean isSameSet(Node<T> from, Node<T> to) {
            return false;
        }

        void union(Node<T> form, Node<T> to) {

        }
    }
}
