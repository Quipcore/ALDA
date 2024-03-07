package graph;
import java.util.Objects;

public class Edge <T> {

    private T nodeOne;
    private T nodeTwo;
    private int cost;

    public Edge(T nodeOne, T nodeTwo, int cost){
        this.nodeOne = nodeOne;
        this.nodeTwo = nodeTwo;
        this.cost = cost;
    }

    public int getCost(){
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public T getNodeTwo() {
        return nodeTwo;
    }

    public T getNodeOne() {
        return nodeOne;
    }

    public boolean isBetween(T nodeA, T nodeB){
        return nodeOne.equals(nodeA) && nodeTwo.equals(nodeB)
                || nodeOne.equals(nodeB) && nodeTwo.equals(nodeA);
    }

    /**
     *
     * @param node
     * @return the node on the other side of edge or null if @param{node} is not a part of the edge
     */
    public T getNeighbour(T node){
        if(nodeOne.equals(node)){
            return nodeTwo;
        } else if (nodeTwo.equals(node)) {
            return nodeOne;
        }else{
            return null;
        }
    }

    //Generated via Wizard
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Edge<?> edge = (Edge<?>) object;
        return cost == edge.cost && Objects.equals(nodeOne, edge.nodeOne) && Objects.equals(nodeTwo, edge.nodeTwo);
    }

    //Generated via Wizard
    @Override
    public int hashCode() {
        return Objects.hash(nodeOne, nodeTwo);
    }
}
