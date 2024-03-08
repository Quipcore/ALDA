package graph.project.bacon;

import java.util.Objects;

public class BaconNode {

    private static final int TAG_LENGTH = "<?>".length();

    private final boolean isMovie;
    private final String name;


    public BaconNode(String name, boolean isMovie){
        this.name = name;
        this.isMovie = isMovie;
    }

    public BaconNode(String name){
        if(name.isEmpty()){
            this.name = name;
            this.isMovie = false;
            return;
        }

        this.isMovie = name.startsWith("<t>");
        this.name = name.substring(TAG_LENGTH); //Skip over tag
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        BaconNode node = (BaconNode) object;

        if (isMovie != node.isMovie) return false;
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        int result = (isMovie ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        char tag = isMovie ? 't' : 'a';

        return "<"+tag+">"+name;
    }
}
