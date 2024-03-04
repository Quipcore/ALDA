package algorithmDesign;
public class HuffmanNode implements Comparable<HuffmanNode>{
    private final char data; // data == null if data == 0 == '\0' == NULL_CHAR
    private final int weight;
    private HuffmanNode leftChildNode;
    private HuffmanNode rightChildNode;


    public HuffmanNode(int weight, char data){
        this.weight = weight;
        this.data = data;
    }

    //If the priority queue is reverse sorted flip the sign
    @Override
    public int compareTo(HuffmanNode o) {
        return weight - o.weight;
    }

    public void connect(HuffmanNode left, HuffmanNode right){
        this.leftChildNode = left;
        this.rightChildNode = right;
    }

    public int getWeight(){
        return weight;
    }

    public char getData(){
        return data;
    }


    /**
     *
     * @param character to be encoded into a bit-string
     * @return the encoding for the character or NULL if the character is not in the tree
     */
    public String encodeCharacter(char character) {
        return findPathToCharacter(character, "");
    }

    private String findPathToCharacter(char character, String path){
        if(character == data){
            return path;
        }

        String leftPath = null;
        if(leftChildNode != null){
            leftPath = leftChildNode.findPathToCharacter(character, path + "0");
        }

        String rightPath = null;
        if(rightChildNode != null){
            rightPath = rightChildNode.findPathToCharacter(character, path +"1");
        }

        return leftPath == null ? rightPath
                : rightPath == null ? leftPath : null;
    }

    public HuffmanNode getRightChild() {
        return rightChildNode;
    }

    public HuffmanNode getLeftChild(){
        return leftChildNode;
    }
}