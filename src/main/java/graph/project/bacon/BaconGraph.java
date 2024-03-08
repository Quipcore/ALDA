package graph.project.bacon;

import graph.Edge;
import graph.MyUndirectedGraph;
import graph.UndirectedGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;

/**
 * Represent an undirected graph of connected actors/actresses, movies and tv-shows.
 */
public class BaconGraph extends MyUndirectedGraph<BaconNode> {
    /**
     * Represent Kevin Bacon
     */
    public static final BaconNode KEVIN_BACON = new BaconNode("<a>Bacon, Kevin (I)");

    /**
     * Reads all lines from a file and from that generates a graph of actors, movies, and tv-shows. Excepted structure
     * of data file is first a line that starts with a &lt;a&gt; tag followed by the name of actor or actress, that line
     * is then followed by any amount of new lines starting with &lt;t&gt; tag and has the name of whatever the
     * actor/actress was featured in. The file should repeat this pattern until end of file
     * @param filePath the path to a file
     * @throws ParseException if a line in the file doesn't start with either a "&lt;a&gt;" or "&lt;t&gt;" tag
     * @throws IOException if an I/O error occurs reading from file. For more information, see {@link java.nio.file.Files#readAllLines(java.nio.file.Path)}
     */
    public BaconGraph(Path filePath) throws ParseException, IOException {
        List<String> nodeNames = Files.readAllLines(filePath);
        generateGraph(nodeNames);
    }

    /**
     * Calculates the inclusive (non-actor nodes) Bacon number for a given node in the graph. This is equivalent to the length of the shortest path
     * between Kevin Bacon and the node because the Bacon number is defined as the minimum number of links between
     * Kevin Bacon and the specified node.
     * For more information, see <a href="https://en.wikipedia.org/wiki/Six_Degrees_of_Kevin_Bacon">Six Degrees of Kevin Bacon</a>.
     *
     * @param node The node for which to calculate the Bacon number.
     * @return The Bacon number of the specified node, including non-actor nodes.
     * @throws NoSuchElementException If the specified node is not part of the graph.
     */
    public int getInclusiveBaconNumber(BaconNode node) throws NoSuchElementException {

        if (!contains(node)) {
            throw new NoSuchElementException("Graph doesn't contain the node" + node);
        }

        return findPathBetween(KEVIN_BACON, node).size() -1;
    }

    /**
     * Calculates the exclusive (non-actor nodes) Bacon number for a given node in the graph. This is equivalent to the length of the shortest path
     * between Kevin Bacon and the node because the Bacon number is defined as the minimum number of links between
     * Kevin Bacon and the specified node.
     * For more information, see <a href="https://en.wikipedia.org/wiki/Six_Degrees_of_Kevin_Bacon">Six Degrees of Kevin Bacon</a>.
     *
     * @param node The node for which to calculate the Bacon number.
     * @return The bacon number of the specified node using exclusively actors/actresses
     * @throws IllegalArgumentException if the node is not representing an actor
     * @throws NoSuchElementException if the node is not a vertex in the graph
     */
    public int getExclusiveBaconNumber(BaconNode node) throws IllegalArgumentException,NoSuchElementException{
        if(!contains(node)){
            throw new NoSuchElementException("Graph doesn't contain the node" + node);
        }

        if(!node.toString().startsWith("<a>")){
            throw new IllegalArgumentException("Node  "+node+" is not an actor");
        }

        List<BaconNode> path = findPathBetween(KEVIN_BACON,node);
        path.removeIf(vertex -> vertex.toString().startsWith("<t>"));
        return path.size() -1;
    }

    /**
     * Preforms a search on the graph to find the shortest path between two nodes.
     * To find the bacon number of one the parameters replace the other parameters with {@link graph.project.bacon.BaconGraph#KEVIN_BACON}
     * @param start the node to be used as start for path search. This node will be put as the first element in the returning path list
     * @param end the node to be used as stop for path search. This node will be put as the last element in the returning path list
     * @return an ordered list with the shortest path between the start and end nodes. Method returns an empty list if there is no path between start and end
     * @throws NoSuchElementException if either one of the parameters is not a vertex of this graph
     */
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

    /**
     * Checks whether there exists a path in graph in that is equivalent to the path given as argument
     * @param nodes a list of nodes representing a path where the first node of the list is the start of the path and last node is the last node in the path
     * @return a boolean value of whether the list is a valid path from first to last element
     * @throws NullPointerException if the list of nodes passed as arguments is null
     * @throws NoSuchElementException if one of the nodes in the list is not a part of this graph
     */
    public boolean isValidPath(List<BaconNode> nodes) throws NullPointerException, NoSuchElementException {

        if (nodes == null) {
            throw new NullPointerException("List of nodes is NULL");
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

    /**
     * Returns a new graph that represent a minimum spanning tree for the graph.
     * @param startNode the node that should be used as the root of the resulting tree
     * @return a graph that represents a minimum spanning tree.
     * @deprecated this method is being deprecated due to taking too much heap space. Consider avoiding this when low JVM heap space available
     */
    @Override
    @Deprecated
    public UndirectedGraph<BaconNode> minimumSpanningTree(BaconNode startNode) {
        return super.minimumSpanningTree(startNode);
    }


    /**
     * Returns a new graph that represent a minimum spanning tree for the graph.
     * @return a graph that represents a minimum spanning tree.
     * @deprecated this method is being deprecated due to taking too much heap space. Consider avoiding this when low JVM heap space available
     */
    @Override
    @Deprecated
    public UndirectedGraph<BaconNode> minimumSpanningTree(){
        return super.minimumSpanningTree();
    }
}
