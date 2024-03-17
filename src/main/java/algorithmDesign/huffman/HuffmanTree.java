package algorithmDesign.huffman;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree {

    private static final char NULL_CHAR = 0;

    private final HuffmanNode root;

    /**
     * Constructs a huffman tree based on the incoming message
     * @param msg The message to serve as seed to tree
     */
    public HuffmanTree(String msg) {
        root = generateStructure(msg);
    }


    private HuffmanNode generateStructure(String msg) {
        PriorityQueue<HuffmanNode> nodes = getHuffmanNodes(msg);

        while (nodes.size() > 1){
            HuffmanNode left = nodes.poll();
            HuffmanNode right = nodes.poll();

            //Intellij analysis tools thinks right.getWeight() will cause nullPointerException, it won't
            int combinedValue = left.getWeight() + right.getWeight();

            HuffmanNode combined = new HuffmanNode(combinedValue,NULL_CHAR);
            combined.connect(left,right);

            nodes.add(combined);
        }

        return nodes.poll();
    }

    private static PriorityQueue<HuffmanNode> getHuffmanNodes(String msg) {
        Map<Character, Integer> charOccurrences = getCharOccurrences(msg);
        PriorityQueue<HuffmanNode> nodes = new PriorityQueue<>(Comparator.comparingInt(HuffmanNode::getWeight));
        for(char ch : charOccurrences.keySet()){
            nodes.add(new HuffmanNode(charOccurrences.get(ch),ch));
        }
        return nodes;
    }

    private static Map<Character, Integer> getCharOccurrences(String msg) {
        Map<Character, Integer> charOccurrences = new HashMap<>();
        for (char ch : msg.toCharArray()){
            charOccurrences.merge(ch, 1, Integer::sum);
        }
        return charOccurrences;
    }


    /**
     * Encodes and generates a bit string from the character based on the tree that was generated during object creation
     * @param character The character to be encoded
     * @return a bit-string generated based on the objects huffman tree
     */
    public String encodeChar(char character) {
        if(root == null){
            return null;
        }
        return root.encodeCharacter(character);
    }

    /**
     * Decodes bit-string into it's original message based on this
     * @param bitString Takes in a bit-string that represents an encoded message
     * @return the original message based on the bit-string and the objects specific tree
     */
    public String decodeMessage(String bitString){

        StringBuilder stringBuilder = new StringBuilder();
        HuffmanNode node = root;
        for(char ch : bitString.toCharArray()){
            if(ch == '1'){
                node = node.getRightChild();
            }

            if(ch == '0'){
                node = node.getLeftChild();
            }

            if(node.getData() != NULL_CHAR){
                stringBuilder.append(node.getData());
                node = root;
            }
        }

        return stringBuilder.toString();
    }
}
