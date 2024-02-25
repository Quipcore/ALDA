package graph;

import java.util.*;
import java.util.stream.Stream;

public class MyUndirectedGraph<T> implements UndirectedGraph<T>{

    private Map<T, Set<Edge<T>>> adjacencies = new HashMap<>();


    @Override
    public int getNumberOfNodes() {
        return adjacencies.size();
    }

    @Override
    public int getNumberOfEdges() {
        return (int) getFlatMap(this).count();
    }

    /**
     *
     * @return a stream of distinct non-Null Edges
     */
    public Stream<Edge<T>> getFlatMap(MyUndirectedGraph<T> tMyUndirectedGraph){
        return tMyUndirectedGraph.adjacencies.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .distinct();
    }

    @Override
    public boolean add(T newNode) {
        if(contains(newNode)){
            return false;
        }

        adjacencies.put(newNode,null);

        return true;
    }


    @Override
    public boolean connect(T node1, T node2, int cost) {
        if(cost <= 0 || !adjacencies.containsKey(node1) || !adjacencies.containsKey(node2)){
            return false;
        }

        Edge<T> edge = getEdgeBetween(node1,node2);
        if(edge == null){
            edge = new Edge<>(node1,node2,cost);
            adjacencies.computeIfAbsent(node1, k -> new HashSet<>());
            adjacencies.get(node1).add(edge);

            adjacencies.computeIfAbsent(node2, k -> new HashSet<>());
            adjacencies.get(node2).add(edge);
        }else{
            edge.cost = cost;
        }
        return true;
    }

    private Edge<T> getEdgeBetween(T node1, T node2){
        if(!adjacencies.containsKey(node1) || !adjacencies.containsKey(node2)){
            return null;
        }

        Set<Edge<T>> edges = adjacencies.get(node1);
        if(edges == null){
            return null;
        }

        return edges.stream()
                .filter(edge -> edge.isBetween(node1,node2))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean isConnected(T node1, T node2) {
        return getEdgeBetween(node1,node2) != null;
    }

    @Override
    public int getCost(T node1, T node2) {
        Edge<T> edge = getEdgeBetween(node1,node2);
        if(edge == null){
            return -1;
        }
        return edge.getCost();
    }

    @Override
    public List<T> depthFirstSearch(T start, T end) {
        if(start.equals(end)){
            return List.of(start);
        }
        Set<T> visited = new HashSet<>(Set.of(start));

        return dfs(start,end,visited);
    }

    private List<T> dfs(T start, T end, Set<T> visited){

        visited.add(start);

        List<T> path = new ArrayList<>();

        if(start.equals(end)){
            path.add(start);
            return path;
        }

        Set<Edge<T>> edges = adjacencies.get(start);

        if(edges == null){
            return null;
        }

        for(Edge<T> edge : edges){
            T next = edge.getNeighbour(start);
            if(visited.contains(next)){
                continue;
            }

            if(next.equals(end)){
                path.add(end);
                return path;
            }

            List<T> nodes = dfs(next,end,visited);
            if(nodes.getLast().equals(end)){
                return nodes;
            }
        }
        return null;
    }

    @Override
    public List<T> breadthFirstSearch(T start, T end) {

        if(start.equals(end)){
            return List.of(start);
        }

        return null;
    }

    //Prims algorithm
    @Override
    public UndirectedGraph<T> minimumSpanningTree() {

        MyUndirectedGraph<T> minimumTree = new MyUndirectedGraph<>();
        Set<T> nodes = new HashSet<>(adjacencies.keySet());

        //Add first node arbitrarily
        T startNode = nodes.iterator().next();
        nodes.remove(startNode);
        minimumTree.add(startNode);

        while(!nodes.isEmpty()){
            Edge<T> lowestEdgeAway = getLowestEdgeAway(minimumTree);

            if(lowestEdgeAway == null){
                throw new IllegalStateException("Could not find lowest edge away from tree, graph might be incomplete");
            }
            T treeNode = minimumTree.contains(lowestEdgeAway.nodeOne) ? lowestEdgeAway.nodeOne : lowestEdgeAway.nodeTwo;
            T awayNode = lowestEdgeAway.getNeighbour(treeNode);

            minimumTree.add(awayNode);
            minimumTree.connect(treeNode,awayNode,lowestEdgeAway.cost);
            nodes.remove(awayNode);
        }

        return minimumTree;
    }

    //Replaced by method below
//    private Edge<T> getLowestEdgeAway(MyUndirectedGraph<T> minimumTree) {
//        List<Edge<T>> edges = getFlatMap(this).sorted(Comparator.comparingInt(Edge::getCost)).toList();
//        for(Edge<T> edge : edges){
//            if(minimumTree.containsPartOfEdge(edge)){
//                return edge;
//            }
//        }
//        return null;
//    }

    //Can be replaced by XOR operation
//    private boolean containsPartOfEdge(Edge<T> edge) {
//        return adjacencies.containsKey(edge.nodeOne) && !adjacencies.containsKey(edge.nodeTwo)
//                || adjacencies.containsKey(edge.nodeTwo) && !adjacencies.containsKey(edge.nodeOne);
//    }

    //Generated by copilot, after asking for improvement on the methods above
    private Edge<T> getLowestEdgeAway(MyUndirectedGraph<T> minimumTree) {
        return getFlatMap(this)
                .filter(edge -> minimumTree.contains(edge.nodeOne) ^ minimumTree.contains(edge.nodeTwo)) // XOR operation ensures only one node is in the minimumTree
                .min(Comparator.comparingInt(Edge::getCost))
                .orElse(null);
    }

    private boolean contains(T node){
        return adjacencies.containsKey(node);
    }
}
