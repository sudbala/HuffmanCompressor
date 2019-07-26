
/**
 * This class is a driver class that instantiates a Huffman Object which will pass in the file to compress/decompress 
 * 
 * @author Sudharsan Balasubramani
 *
 */
public class HuffmanDriver {

	public static void main(String[] args) throws Exception {
		//Constructs Huffman class, with the filename as a parameter
		Huffman huffman = new Huffman("WarAndPeace.txt");
		
		
		/*
		 * Calls all methods in Huffman to compress and decompress 
		 */
		huffman.createFrequencyTable();
		huffman.treeToPQ();
		huffman.treeCreation();
		huffman.codeRetrieval();
		huffman.compress();
		huffman.decompress();

	}

}
