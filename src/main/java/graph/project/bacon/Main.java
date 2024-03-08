package graph.project.bacon;

import graph.UndirectedGraph;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    private static BaconNode kevinBacon = new BaconNode("<a>Bacon, Kevin (I)");
    private static BaconNode alanAlda = new BaconNode("<a>Alda, Alan");
    private static BaconNode rots = new BaconNode("<t>Star Wars: Episode III - Revenge of the Sith (2005)");

    public static void main(String[] args) throws IOException, URISyntaxException {

        long timer = System.currentTimeMillis();
        String path = "moviedata.txt";
        List<String> nodes = Files.lines(getPathOf(path)).toList();
        long finished = System.currentTimeMillis();
        long diff = finished - timer;
        System.out.println("Collected lines: " + diff + "ms");

        timer = System.currentTimeMillis();
        UndirectedGraph<BaconNode> graph = generateGraph(nodes);
        finished = System.currentTimeMillis();
        diff = finished - timer;
        System.out.println("Created graph: " + diff + "ms");
        System.out.println("Nodes in graph = " + graph.getNumberOfNodes());
        System.out.println("Edges in graph = " + graph.getNumberOfEdges());

        timer = System.currentTimeMillis();
        List<BaconNode> graphBFSPath = graph.breadthFirstSearch(kevinBacon, kevinBacon);
        finished = System.currentTimeMillis();
        diff = finished - timer;
        System.out.println("Found path using BFS in graph: " + diff + "ms");
        System.out.println("Graph path length = " + (graphBFSPath.size()-1));
        System.out.println(graphBFSPath);

        timer = System.currentTimeMillis();
        List<BaconNode> graphDFSPath = graph.depthFirstSearch(kevinBacon, kevinBacon);
        finished = System.currentTimeMillis();
        diff = finished - timer;
        System.out.println("Found path using DFS in graph: " + diff + "ms");
        System.out.println("Graph path length = " + (graphDFSPath.size()-1));
        System.out.println(graphDFSPath);

    }

    private static UndirectedGraph<BaconNode> generateGraph(List<String> nodes) {
        UndirectedGraph<BaconNode> graph = new BaconGraph<>();
        BaconNode actor = new BaconNode("");
        for (String line : nodes) {
            if (line.startsWith("<a>")) {
                actor = new BaconNode(line);
                graph.add(actor);
            } else {
                BaconNode node = new BaconNode(line);
                if (!graph.contains(node)) {
                    graph.add(node);
                }
                graph.connect(actor, node, 1);
            }
        }
        return graph;
    }

    private static Path getPathOf(String path) throws URISyntaxException {
        URI uri = Main.class.getResource(path).toURI();
        return Path.of(uri);
    }
}
