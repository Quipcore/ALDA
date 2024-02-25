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
        return dfs(start,end,new HashSet<>()).reversed();
    }

    private List<T> dfs(T vertex, T end, Set<T> visited) {
        if(vertex.equals(end)){
            return new ArrayList<>(List.of(vertex));
        }

        visited.add(vertex);

        for(Edge<T> edge : adjacencies.get(vertex)){
            T neighbour = edge.getNeighbour(vertex);
            if(!visited.contains(neighbour)){
                List<T> path = dfs(neighbour,end,visited);
                if(path.contains(end)){
                    path.add(vertex);
                    return path;
                }
            }
        }

        return new ArrayList<>();
    }


    //Modified after Wikipedia Pseudocode
    @Override
    public List<T> breadthFirstSearch(T start, T end) {
        if(start.equals(end)){
            return List.of(start);
        }


        Map<T,T> parentNode = new HashMap<>();
        PriorityQueue<T> queue = new PriorityQueue<>();
        Set<T> explored = new HashSet<>();
        explored.add(start);
        queue.add(start);

        while(!queue.isEmpty()){
            T vertex = queue.poll();

            if(vertex.equals(end)){
                return generatePath(parentNode, start, vertex);
            }

            for(Edge<T> edge : adjacencies.get(vertex)){
                T vertexNeighbour = edge.getNeighbour(vertex);
                if(!explored.contains(vertexNeighbour)){
                    explored.add(vertexNeighbour);
                    parentNode.put(vertexNeighbour,vertex);
                    queue.add(vertexNeighbour);
                }

            }
        }
        return new ArrayList<>();
    }

    private List<T> generatePath(Map<T,T> parentNode, T startNode, T endNode) {
        List<T> path = new ArrayList<>();
        path.add(endNode);
        T vertex = parentNode.get(endNode);
        while(!vertex.equals(startNode)){
            path.add(vertex);
            vertex = parentNode.get(vertex);
        }
        path.add(vertex);
        return path.reversed();
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
