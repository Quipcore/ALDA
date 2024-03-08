package graph.project.bacon;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final BaconNode ALAN_ALDA = new BaconNode("<a>Alda, Alan");
    private static final BaconNode ROTS = new BaconNode("<t>Star Wars: Episode III - Revenge of the Sith (2005)");
    private static final BaconNode ICELANDER = new BaconNode("<a>Þórðardóttir, María");

    private static final String PATH = "moviedata.txt";
    private static final Path PATH_TO_DATA;

    static {
        try {
            PATH_TO_DATA = Path.of(BaconGraph.class.getResource(PATH).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, ParseException {

        System.out.println("Generating graph...");
        BaconGraph baconGraph = new BaconGraph(PATH_TO_DATA);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of an actor or Tv-Show/Movie: ");
        String item = scanner.nextLine();
        BaconNode baconNode = new BaconNode(item);

        List<BaconNode> path = baconGraph.findPathBetween(BaconGraph.KEVIN_BACON,baconNode);

        System.out.println("\"" + item + "\" bacon number is " + (path.size() - 1) + " with the path:");
        print(path);

    }

    private static <T> void print(List<T> items) {
        for (T item : items) {
            System.out.println(item);
        }
        System.out.println();
    }
}
