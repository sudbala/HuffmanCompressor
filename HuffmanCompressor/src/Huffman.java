import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This class is responsible for generating the frequency table, creating trees,
 * compressing, and decompressing.
 * 
 * @author Sudharsan Balasubramani
 *
 */
public class Huffman {
	/*
	 * Creates the Instance Variables:
	 * Table for frequencies, map for huffman codes of each character
	 * filename string, compressed filename string, decompressed filename,
	 * PriorityQ for all the mini trees and eventual huffman tree,
	 * and the TreeComparator used for the PriorityQ.
	 */
	private Map<Character, Integer> frequency;
	private Map<Character, String> HuffmanCode;
	private String filename;
	private String compressedFilename;
	private String decompressedFilename; 
	private PriorityQueue<BinaryTree<CharFrequency>> pq; 
	private TreeComparator treeComparator;

	/**
	 * Constructor for Huffman
	 * 
	 * @param filename file's pathname
	 */
	public Huffman(String filename) {
		this.filename = filename;
		compressedFilename = "compressed" + filename.substring(0, filename.indexOf(".")) + ".txt";
		decompressedFilename = "decompressed" + filename.substring(0, filename.indexOf(".")) + ".txt";
		frequency = new HashMap<Character, Integer>();

		treeComparator = new TreeComparator();
		pq = new PriorityQueue<BinaryTree<CharFrequency>>(treeComparator);
		HuffmanCode = new HashMap<Character, String>();
	}
	
	/**
	 * Helper method to read files into char arrays for future use.
	 * 
	 * @return char[] full of all chars in the file
	 * @throws Exception
	 */
	public char[] readFileChars() throws Exception {
		BufferedReader reader;
		String file = "";
		String line;
		

		//If a file is never loaded
		if (filename == null)
			throw new Exception("File Not Loaded");
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (Exception e) {
			//If file does not exist
			throw new Exception("File Not Found");					
		}

		//Checks whether reading of file is done. Adds to file string if not.
		while ((line = reader.readLine()) != null) {				
			file += line+"\n";											
		}
		
		//Closes BufferedReader because done
		reader.close();
		
		
		//Converts to char array for easier conversion
		return file.toCharArray();

	}

	/**
	 * Creates the table of frequencies for every character in text;
	 * 
	 * @throws Exception
	 */
	public void createFrequencyTable() throws Exception {
		char[] chars;
		
		chars = readFileChars();

		//Inserts into frequency map, incrementing if there already
		for (char c : chars) {
			Character character = c;
			if (!frequency.containsKey(character)) {
				frequency.put(character, 1);
			} else {
				frequency.replace(character, frequency.get(character) + 1);
			}
		}
	}

	/**
	 * Creates mini trees for each character, then adds to priority queue
	 * 
	 * @throws Exception
	 */
	public void treeToPQ() throws Exception {

		//Checks whether frequency map has been created, or whether the file is empty
		if (frequency.keySet().size() == 0) {
			throw new IOException("Nothing to Compress!");
		}
		
		//For each character, creates a node tree and adds it to the priority queue
		for (Character c : frequency.keySet()) {
			BinaryTree<CharFrequency> freqTree = new BinaryTree<CharFrequency>(new CharFrequency(c, frequency.get(c)));
			pq.add(freqTree);
		}
	}

	/**
	 * Takes priority queue and shortens it until 1 tree is formed with code preset
	 * for each letter
	 */
	public void treeCreation() {
		/*
		 * While pq has more than one tree, take the first two trees, combine them with a node that
		 * has the sum of frequencies and then reinsert back into the priority queue. Repeat until 
		 * one tree is left.
		 */
		while (pq.size() > 1) {
			BinaryTree<CharFrequency> bt1 = pq.poll();
			BinaryTree<CharFrequency> bt2 = pq.poll();

			CharFrequency newFrequency = new CharFrequency(bt1.getData().getFreq() + bt2.getData().getFreq());

			BinaryTree<CharFrequency> combined = new BinaryTree<CharFrequency>(newFrequency, bt1, bt2);

			pq.add(combined);
		}
	}

	/**
	 * retrieves codes for each character using the Huffman Code tree constructed.
	 */
	public void codeRetrieval() {
		//accumulator used to keep track of all codes for each character.
		getCode(HuffmanCode, pq.peek(), "");

		//Makes sure to take care of the case where only one tree is created due to only one
		//Character or one character repeated
		if (HuffmanCode.size() == 1) {
			HuffmanCode.replace(pq.peek().getData().getChar(), "1");
		}
	}

	/**
	 * Helper method to generate code for each character
	 * 
	 * @param code      Map that holds the code for each character
	 * @param freqTree  Frequency tree for characters
	 * @param pathSoFar variable that keeps track of the path of each character
	 *                  during tree traversal
	 */
	private void getCode(Map<Character, String> code, BinaryTree<CharFrequency> freqTree, String pathSoFar) {
		//If a leaf, insert into the code map
		if (freqTree.isLeaf()) {
			code.put(freqTree.getData().getChar(), pathSoFar);
		}
		//If has a left, add 0 to pathSoFar, recurse accordingly
		if (freqTree.hasLeft()) {
			getCode(code, freqTree.getLeft(), pathSoFar + "0");
		}
		//If has a right, add 1 to pathSoFar, recurse accordingly
		if (freqTree.hasRight()) {
			getCode(code, freqTree.getRight(), pathSoFar + "1");
		}
	}

	/**
	 * Compresses the file by reading the file with a BufferedReader and outputs
	 * into a compressed file with bit representations
	 * @throws Exception
	 */
	public void compress() throws Exception{
		//Local BitWriter and char[]
		BufferedBitWriter bitOutput = new BufferedBitWriter(compressedFilename);
		char[] chars;
		
		//reads all chars in file again, this time to compres
		chars = readFileChars();

		//For every char, get the code path
		for (char c : chars) {
			Character character = c;
			String codePath = HuffmanCode.get(character);

			//For every path, goes through and prints false if 0, true if 1.
			for (int i = 0; i < codePath.length(); i++) {
				if (codePath.charAt(i) == '0') {
					bitOutput.writeBit(false);
				} else if (codePath.charAt(i) == '1') {
					bitOutput.writeBit(true);
				}
			}
		}

		//closes writer
		bitOutput.close();
	}

	/**
	 * Decompresses Files that have been compressed.
	 * @throws IOException
	 */
	public void decompress() throws IOException {
		//Local variables bitInput, output writer, the tree we will traverse with and a temporary tree.
		BufferedBitReader bitInput = new BufferedBitReader(compressedFilename);
		BufferedWriter output = new BufferedWriter(new FileWriter(decompressedFilename));
		BinaryTree<CharFrequency> tree = pq.peek();
		BinaryTree<CharFrequency> traverse = tree;
		
		//While the there are bits to be read
		while (bitInput.hasNext()) {
			//If at a leaf off the start (only one), make sure to read the bit so we don't get caught in an infinite loop
			if(traverse.isLeaf()) {
				bitInput.readBit();
			}
			//While not a leaf, read the bit, traverse down the tree with each bit until you get to a leaf
			while (!traverse.isLeaf()) {
				boolean bit = bitInput.readBit();

				if (!bit) {
					traverse = traverse.getLeft();
				} else if (bit) {
					traverse = traverse.getRight();
				}
			}
			//Write the char.
			output.write("" + traverse.getData().getChar());
			traverse = tree;
		}
		//Close the reader and the writer.
		bitInput.close();
		output.close();
	}

}
