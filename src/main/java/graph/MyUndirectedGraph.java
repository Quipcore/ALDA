package graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyUndirectedGraph<T> implements UndirectedGraph<T>{

    private final Map<T, Set<Edge<T>>> adjacencies = new HashMap<>();

    private int edgeAmount = 0;

    @Override
    public int getNumberOfNodes() {
        return adjacencies.size();
    }

    @Override
    public int getNumberOfEdges() {
        return edgeAmount;
    }

    /**
     *
     * @return a stream of distinct non-Null Edges
     */
    public Stream<Edge<T>> getFlatMap(MyUndirectedGraph<T> myUndirectedGraph){
        return myUndirectedGraph.adjacencies.values()
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
        edgeAmount++;

        return true;
    }


    @Override
    public boolean connect(T nodeOne, T nodeTwo, int cost) {
        if(cost <= 0 || !adjacencies.containsKey(nodeOne) || !adjacencies.containsKey(nodeTwo)){
            return false;
        }

        Edge<T> edge = getEdgeBetween(nodeOne,nodeTwo);
        if(edge == null){
            edge = new Edge<>(nodeOne,nodeTwo,cost);
            adjacencies.computeIfAbsent(nodeOne, k -> new HashSet<>());
            adjacencies.get(nodeOne).add(edge);

            adjacencies.computeIfAbsent(nodeTwo, k -> new HashSet<>());
            adjacencies.get(nodeTwo).add(edge);
        }else{
            edge.setCost(cost);
        }
        return true;
    }

    private Edge<T> getEdgeBetween(T nodeOne, T nodeTwo){
        if(!adjacencies.containsKey(nodeOne) || !adjacencies.containsKey(nodeTwo)){
            return null;
        }

        Set<Edge<T>> edges = adjacencies.get(nodeOne);
        if(edges == null){
            return null;
        }

        return edges.stream()
                .filter(edge -> edge.isBetween(nodeOne,nodeTwo))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean isConnected(T nodeOne, T nodeTwo) {
        return getEdgeBetween(nodeOne,nodeTwo) != null;
    }

    @Override
    public int getCost(T nodeOne, T nodeTwo) {
        Edge<T> edge = getEdgeBetween(nodeOne,nodeTwo);
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
    //Bredden först:
    //Explored i BFS är onödig, skulle kunna använda nycklarna från parentNode.
    //
    //BFS bör inte använda en prioritetskö, det är Dijkstra's som kräver en sådan.
    @Override
    public List<T> breadthFirstSearch(T start, T end) {
        if(start.equals(end)){
            return List.of(start);
        }

        Map<T,T> parentNode = new HashMap<>();
        Queue<T> queue = new LinkedList<>();
        queue.add(start);

        while(!queue.isEmpty()){
            T vertex = queue.poll();

            if(vertex.equals(end)){
                return generatePath(parentNode, start, vertex);
            }

            for(Edge<T> edge : adjacencies.get(vertex)){
                T vertexNeighbour = edge.getNeighbour(vertex);
                if(!parentNode.containsKey(vertexNeighbour)){
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

    //TODO: Comments
    //Minimalt uppspännande träd:
    //En prioritetskö skulle kunna använts för att hitta den billigaste kanten i minimumSpanningTree.
    @Override
    public UndirectedGraph<T> minimumSpanningTree() {
        MyUndirectedGraph<T> minimumTree = new MyUndirectedGraph<>();
        Set<T> nodes = new HashSet<>(adjacencies.keySet());

        T startNode = nodes.iterator().next();
        nodes.remove(startNode);
        minimumTree.add(startNode);


        PriorityQueue<Edge<T>> edgeQueue = new PriorityQueue<>(Comparator.comparingInt(Edge::getCost));
        edgeQueue.addAll(adjacencies.get(startNode));

        while (!nodes.isEmpty()) {
            Edge<T> lowestEdgeAway = edgeQueue.poll();

            if (lowestEdgeAway == null) {
                throw new IllegalStateException("Could not find lowest edge away from tree, graph might be incomplete");
            }

            T treeNode = minimumTree.contains(lowestEdgeAway.getNodeOne()) ? lowestEdgeAway.getNodeOne() : lowestEdgeAway.getNodeTwo();
            T awayNode = lowestEdgeAway.getNeighbour(treeNode);

            if (nodes.contains(awayNode)) {
                minimumTree.add(awayNode);
                minimumTree.connect(treeNode, awayNode, lowestEdgeAway.getCost());
                nodes.remove(awayNode);

                edgeQueue.addAll(adjacencies.get(awayNode));
            }
        }

        return minimumTree;
    }

//    //Prims algorithm
//    @Override
//    public UndirectedGraph<T> minimumSpanningTree() {
//
//        MyUndirectedGraph<T> minimumTree = new MyUndirectedGraph<>();
//        Set<T> nodes = new HashSet<>(adjacencies.keySet());
//
//        //Add first node arbitrarily
//        T startNode = nodes.iterator().next();
//        nodes.remove(startNode);
//        minimumTree.add(startNode);
//
//        while(!nodes.isEmpty()){
//            Edge<T> lowestEdgeAway = getLowestEdgeAway(minimumTree);
//
//            if(lowestEdgeAway == null){
//                throw new IllegalStateException("Could not find lowest edge away from tree, graph might be incomplete");
//            }
//
//            T treeNode = minimumTree.contains(lowestEdgeAway.getNodeOne()) ? lowestEdgeAway.getNodeOne() : lowestEdgeAway.getNodeTwo();
//            T awayNode = lowestEdgeAway.getNeighbour(treeNode);
//
//            minimumTree.add(awayNode);
//            minimumTree.connect(treeNode,awayNode,lowestEdgeAway.getCost());
//            nodes.remove(awayNode);
//        }
//
//        return minimumTree;
//    }
//
//
//    //TODO: FIX
//    private Edge<T> getLowestEdgeAway(MyUndirectedGraph<T> minimumTree){
//        return getFlatMap(this)
//                .filter(minimumTree::containsPartOfEdge)
//                .min(Comparator.comparingInt(Edge::getCost))
//                .orElse(null);
//    }

    // Can be replaced by XOR operation
    private boolean containsPartOfEdge(Edge<T> edge) {
        return adjacencies.containsKey(edge.getNodeOne()) && !adjacencies.containsKey(edge.getNodeTwo())
                || adjacencies.containsKey(edge.getNodeTwo()) && !adjacencies.containsKey(edge.getNodeOne());
    }


    private boolean contains(T node){
        return adjacencies.containsKey(node);
    }
}