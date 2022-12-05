package com.wwx.study.algorithms.code.图;

/**
 * @author wuwenxi 2022-05-25
 */
public class GraphGenerator {

    /**
     * 满足按照[weight,from,to]的存储的N * 3的二维数组
     */
    public static Graph createGraph(Integer[][] matrix) {
        Graph<Integer> graph = new Graph<>();
        for (Integer[] row : matrix) {
            Integer weight = row[0];
            Integer from = row[1];
            Integer to = row[2];
            graph.nodes.computeIfAbsent(from, Node::new);
            graph.nodes.computeIfAbsent(to, Node::new);
            //
            Node<Integer> fromNode = graph.nodes.get(from);
            Node<Integer> toNode = graph.nodes.get(to);
            if (!fromNode.nexts.contains(toNode)) {
                // 增加from点的出度结点
                fromNode.nexts.add(toNode);
                // 增加出度和入度值
                fromNode.out++;
                toNode.in++;
                // 增加边界
                Edge edge = new Edge<>(weight, fromNode, toNode);
                fromNode.edges.add(edge);
                toNode.edges.add(edge);
                //
                graph.edges.add(edge);
            }
        }
        return graph;
    }
}
