/**
 * CharFrequency is an object that holds a character and its frequency that will be used in Maps and Trees
 * 
 * @author Sudharsan Balasubramani
 */

public class CharFrequency implements Comparable<CharFrequency>{
	private Character c;	//Character
	private Integer f;		//Frequency
	
	/**
	 * Constructor for a character and its frequency
	 * 
	 * @param c	character value
	 * @param f	frequency 
	 */
	public CharFrequency(Character c, Integer f) {
		this.c = c;
		this.f = f;
	}
	
	/**
	 * Constructor for internal nodes of trees, as they don't have character values.
	 * 
	 * @param f	frequency
	 */
	public CharFrequency(Integer f) {
		this.f = f;
		this.c = null;
	}
	
	/**
	 * Sets the character value
	 * 
	 * @param c	Character to set
	 */
	public void setChar(Character c) {
		this.c = c;
	}
	
	/**
	 * Returns the character value
	 * 
	 * @return	Character
	 */
	public Character getChar() {
		return this.c;
	}
	
	/**
	 * Sets the Frequency value
	 * 
	 * @param f	Frequency to set
	 */
	public void setFreq(Integer f) {
		this.f = f;
	}
	
	/**
	 * Returns the frequency value
	 * 
	 * @return	Frequency
	 */
	public Integer getFreq() {
		return this.f;
	}
	
	/**
	 * Increments Frequency Value
	 * @throws Exception 
	 */
	public void addFreq() throws Exception {
		if(f != null)
			this.f +=1 ;
		else throw new Exception("Null Frequency");
	}
	
	/**
	 * Decrements Frequency Value
	 * @throws Exception 
	 */
	public void subFreq() throws Exception {
		if(f != null)
			this.f -=1 ;
		else throw new Exception("Null Frequency");
	}
	
	public String toString() {
		if(getChar() == null)
			return getFreq().toString();
		else
			return getChar().toString() +":" + getFreq().toString();
	}

	@Override
	/**
	 * Compares by frequency, returns -1 if smaller, 0 if equal, and 1 if larger.
	 * 
	 * @param o	CharFrequency to compare
	 * @return	return -1,0,1
	 */
	public int compareTo(CharFrequency o) {
		if(f < o.getFreq()) return -1;
		else if (f > o.getFreq()) return 1;
		return 0;
	}
	
	
	
}
