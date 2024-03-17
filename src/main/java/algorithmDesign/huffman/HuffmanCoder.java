package algorithmDesign.huffman;

public class HuffmanCoder {

	public EncodedMessage<?,?> encode(String msg) {
		HuffmanTree huffmanTree = new HuffmanTree(msg);

		StringBuilder stringBuilder = new StringBuilder();
		for(char c : msg.toCharArray()){
			stringBuilder.append(huffmanTree.encodeChar(c));
		}

		return new EncodedMessage<>(huffmanTree, stringBuilder.toString());
	}

	public String decode(EncodedMessage<?,?> msg) {
		return ((HuffmanTree)msg.header).decodeMessage((String) msg.message);
	}
}
