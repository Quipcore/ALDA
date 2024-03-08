package graph.project.bacon;

import graph.Edge;
import graph.MyUndirectedGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;

public class BaconGraph extends MyUndirectedGraph<BaconNode> {

    private static final BaconNode KEVIN_BACON = new BaconNode("<a>Bacon, Kevin (I)");

    public BaconGraph(Path path) throws ParseException, IOException {
        List<String> nodeNames = Files.readAllLines(path);
        generateGraph(nodeNames);
    }

    /**
     * Equivalent to calling findPathBetween(new BaconNode("&lt;a&gt;Bacon, Kevin (I)"), node)
     *
     * @param node
     * @return
     * @throws NoSuchElementException
     */
    public List<BaconNode> getBaconNumber(BaconNode node) throws NoSuchElementException {

        if (!contains(node)) {
            throw new NoSuchElementException("Graph doesn't contain the node" + node);
        }

        return findPathBetween(KEVIN_BACON, node);
    }

    public List<BaconNode> findPathBetween(BaconNode start, BaconNode end) throws NoSuchElementException {

        if (!contains(start)) {
            throw new NoSuchElementException("Graph doesn't contain the node" + start);
        }

        if (!contains(end)) {
            throw new NoSuchElementException("Graph doesn't contain the node" + end);
        }

        return breadthFirstSearch(start, end);
    }

    private void generateGraph(List<String> nodeNames) throws ParseException {
        BaconNode actor = new BaconNode("");
        for (String line : nodeNames) {
            if (line.startsWith("<a>")) {
                actor = new BaconNode(line);
                add(actor);
            } else if (line.startsWith("<t>")) {
                BaconNode node = new BaconNode(line);
                if (!contains(node)) {
                    add(node);
                }
                connect(actor, node, 1);
            } else {
                //Consider moving into node class
                throw new ParseException("Failed to parse element: " + line + ". Failed to determine element type. Tag is either missing or not \"<a>\" or \"<t>\"", 0);
            }
        }
    }

    //Overwritten due the need of using iterative approach due to limited JVM stack size
    @Override
    public List<BaconNode> depthFirstSearch(BaconNode start, BaconNode end) throws NoSuchElementException{

        if(!contains(start) || !contains(end)){
            throw new NoSuchElementException("Missing element in graph");
        }

        Map<BaconNode, BaconNode> nodeToParent = new HashMap<>();
        Stack<BaconNode> stack = new Stack<>();

        stack.push(start);
        nodeToParent.put(start, null);

        while (!stack.isEmpty()) {
            BaconNode current = stack.pop();

            if (current.equals(end)) {
                List<BaconNode> path = new LinkedList<>();
                BaconNode node = end;
                while (node != null) {
                    path.addFirst(node);
                    node = nodeToParent.get(node);
                }
                return path;
            }

            for (Edge<BaconNode> edge : getEdges(current)) {
                BaconNode neighbor = edge.getNeighbour(current);
                if (!nodeToParent.containsKey(neighbor)) {
                    stack.push(neighbor);
                    nodeToParent.put(neighbor, current);
                }
            }
        }

        return new LinkedList<>();
    }

    public boolean isValidPath(List<BaconNode> nodes) throws IllegalArgumentException, NoSuchElementException {

        if (nodes == null) {
            throw new IllegalArgumentException("List of nodes is NULL");
        }

        BaconNode firstNode = nodes.getFirst();
        if (!contains(firstNode)) {
            throw new NoSuchElementException("Graph is missing " + firstNode);
        }

        for (int i = 0; i < nodes.size() - 1; i++) {
            BaconNode currentNode = nodes.get(i);
            BaconNode nextNode = nodes.get(i + 1);
            if (!contains(nextNode)) {
                throw new NoSuchElementException("Graph is missing" + nextNode);
            }

            if (!isNeighbours(currentNode, nextNode)) {
                return false;
            }
        }

        return true;
    }

    private boolean isNeighbours(BaconNode t, BaconNode t1) {
        for (Edge<BaconNode> edge : getEdges(t)) {
            if (edge.getNeighbour(t).equals(t1)) {
                return true;
            }
        }

        return false;
    }

}
