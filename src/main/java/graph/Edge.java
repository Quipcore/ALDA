package graph;

public class Edge <T> {
    T nodeOne;
    T nodeTwo;
    int cost;
    public Edge(T nodeOne, T nodeTwo, int cost){
        this.nodeOne = nodeOne;
        this.nodeTwo = nodeTwo;
        this.cost = cost;
    }

    public int getCost(){
        return cost;
    }

    public boolean isBetween(T node1, T node2){
        return (nodeOne.equals(node1) && nodeTwo.equals(node2))
            || (nodeOne.equals(node2) && nodeTwo.equals(node1));
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


}
