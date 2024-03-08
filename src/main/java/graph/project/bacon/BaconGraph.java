package graph.project.bacon;

import graph.Edge;
import graph.MyUndirectedGraph;

import java.util.*;

public class BaconGraph<T> extends MyUndirectedGraph<T> {
    @Override
    public List<T> depthFirstSearch(T start, T end) {
        Map<T, T> nodeToParent = new HashMap<>();
        Stack<T> stack = new Stack<>();

        stack.push(start);
        nodeToParent.put(start, null);

        while (!stack.isEmpty()) {
            T current = stack.pop();

            if (current.equals(end)) {
                List<T> path = new LinkedList<>();
                T node = end;
                while (node != null) {
                    path.addFirst(node);
                    node = nodeToParent.get(node);
                }
                return path;
            }

            for (Edge<T> edge : getEdges(current)) {
                T neighbor = edge.getNeighbour(current);
                if (!nodeToParent.containsKey(neighbor)) {
                    stack.push(neighbor);
                    nodeToParent.put(neighbor, current);
                }
            }
        }

        return new LinkedList<>(); // or return null;
    }
}
