package graph;

import graph.project.bacon.BaconGraph;
import graph.project.bacon.BaconNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BaconProjectTest {

    private static final String PATH = "moviedata.txt";
    private static final Path PATH_TO_DATA;

    static {
        try {
            PATH_TO_DATA = Path.of(BaconGraph.class.getResource(PATH).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

//    private static final BaconNode kevinBacon = new BaconNode("<a>Bacon, Kevin (I)");

    public List<BaconNode> getPath(BaconNode node) throws IOException, ParseException {
        BaconGraph bacon = new BaconGraph(PATH_TO_DATA);
        return getPath(bacon, node);
    }

    public List<BaconNode> getPath(BaconGraph bacon, BaconNode node){
        return bacon.getBaconNumber(node);
    }

    @Test
    public void pathToAlanAldaIsValid() throws IOException, ParseException {
        BaconGraph baconGraph = new BaconGraph(PATH_TO_DATA);
        BaconNode alanAlda = new BaconNode("<a>Alda, Alan");
        List<BaconNode> alanAldaPath = getPath(baconGraph,alanAlda);
        assertTrue(baconGraph.isValidPath(alanAldaPath));
    }

    @Test
    public void pathToAlanAldaDoesntTimeout(){
        BaconNode alanAlda = new BaconNode("<a>Alda, Alan");
        assertTimeout(Duration.ofMinutes(2),() -> getPath(alanAlda));
    }

    @Test
    public void pathToRevengeOfTheSithDoesntTimeout(){
        BaconNode node = new BaconNode("<t>Star Wars: Episode III - Revenge of the Sith (2005)");
        assertTimeout(Duration.ofMinutes(2),() -> getPath(node));
    }

    @Test
    public void pathToIcelanderDoesntTimeout(){
        BaconNode node = new BaconNode("<a>Þórðardóttir, María");
        assertTimeout(Duration.ofMinutes(2),() -> getPath(node));
    }

    @Test
    public void sizeOfListToKevinBaconIsOne() throws IOException, ParseException {
        BaconNode node = new BaconNode("<a>Bacon, Kevin (I)");
        List<BaconNode> path = getPath(node);
        assertEquals(path.size(), 1);
    }
}
