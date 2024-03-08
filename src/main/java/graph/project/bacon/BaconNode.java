package graph.project.bacon;

import java.nio.file.Path;
import java.util.Objects;


/**
 * A node class that represent an actor/actress, movie or tv-show
 */
public class BaconNode {

    private static final int TAG_LENGTH = "<?>".length();
    private final boolean isMovie;
    private final String name;

    /**
     * Constructs a node with a specific name and type as defined in {@link graph.project.bacon.BaconGraph#BaconGraph(Path)}
     * @param name the name and type of this node
     */
    public BaconNode(String name){
        if(name.isEmpty()){
            this.name = name;
            this.isMovie = false;
            return;
        }

        this.isMovie = name.startsWith("<t>");
        this.name = name.substring(TAG_LENGTH); //Skip over tag
    }

    /**
     * Two objects are considered equal they both represent an actor, movie, or tv-show and share the same name
     * @param object to be compared
     * @return true if the argument is considered equal
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BaconNode node = (BaconNode) object;
        if (isMovie != node.isMovie) return false;
        return Objects.equals(name, node.name);
    }

    /**
     * Returns the hash value of this node.
     * @return the nodes hashcode
     */
    @Override
    public int hashCode() {
        int result = (isMovie ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    /**
     * A string representation of this node including type and name
     * @return The {@link String} representation of this node
     */
    @Override
    public String toString() {
        char tag = isMovie ? 't' : 'a';

        return "<"+tag+">"+name;
    }
}
