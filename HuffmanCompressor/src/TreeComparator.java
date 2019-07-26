import java.util.Comparator;

/**
 * TreeComparator provides a compare method to use for PriorityQueues that will be storing Binary Trees of CharFrequencies
 * 
 * @author Sudharsan Balasubramani
 *
 */
public class TreeComparator implements Comparator<BinaryTree<CharFrequency>> {

	@Override
	
	/**
	 * Compares by frequency, returns -1 if smaller, 0 if equal, and 1 if larger.
	 * 
	 * @param t1	CharFrequency to compare
	 * @param t2	CharFrequency 2 to compare
	 * @return	return -1,0,1
	 */
	public int compare(BinaryTree<CharFrequency> t1, BinaryTree<CharFrequency> t2) {
		if(t1.getData().getFreq() < t2.getData().getFreq())
			return -1;
		else if(t1.getData().getFreq() > t2.getData().getFreq())
			return 1;
		return 0;
	}

}
