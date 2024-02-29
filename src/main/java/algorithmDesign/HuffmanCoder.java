package algorithmDesign;

public class HuffmanCoder {

	public EncodedMessage encode(String msg) {
		HuffmanTree huffmanTree = new HuffmanTree(msg);

		StringBuilder stringBuilder = new StringBuilder();
		for(Character c : msg.toCharArray()){
			stringBuilder.append(huffmanTree.encodeChar(c));
		}

		return new EncodedMessage(huffmanTree, stringBuilder.toString());
	}

	public String decode(EncodedMessage msg) {
		return msg.getHeader().decodeMessage(msg.getBitString());
	}
}
