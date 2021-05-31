package com.itemset.project.common;

public abstract class AbstractItemsetTree {
	
	// root of the itemset tree
	public ItemsetTreeNode root = null;

	// statistics about tree construction
	public int nodeCount=0; // number of nodes in the tree (recalculated by printStatistics() )
	public long totalItemCountInNodes;  // total number of items stored in nodes (recalculated by printStatistics()
	public long totalsupport;       // frequency in null set  
	public long startTimestamp;  // start time of tree construction (buildTree())
	public long endTimestamp;   // end time  of tree construction (buildTree())

	
	protected int[] getLargestCommonAncestor(int[] itemset1, int[] itemset2) {
		// if one of the itemsets is null,
		// return null.
		if(itemset2 == null || itemset1 == null){
			return null;
		}
	
		// find the minimum length of the itemsets
		int minI = itemset1.length < itemset2.length ? itemset1.length : itemset2.length;
		
		int count = 0;  // to count the size of the common ancestor
		
		// for each position in the itemsets from 0 to the maximum length -1
		// Note that we use maxI-1 because we don't want that
		// the maximum ancestor to be equal to itemset1 or itemset2
		for(int i=0; i < minI; i++){   
			// if the two items are different, we stop because
			// of the lexical ordering
			if(itemset1[i] != itemset2[i]){
				break;
			}else{
				// otherwise we inscrease the counter indicating the number of common
				// items in the prefix
				count++;
			}
		}
		// if there is a common ancestor of size >0
		// (we don,t want the empty set!)
		if(count >0 && count < minI){
		
			int[] common = new int[count];
			System.arraycopy(itemset1, 0, common, 0, count);
			return common;
		}
		else{
			// otherwise, return null because the common ancestor is the empty set
			return null;
		}
	}

	/**
	 * Check if a first itemset is the ancestor of the second itemset
	 * @param itemset1  the first itemset
	 * @param itemset2 the second itemset
	 * @return true, if yes, otherwise, false.
	 */
	protected boolean ancestorOf(int[] itemset1, int[] itemset2) {
		// if the second itemset is null (empty set), return false
		if(itemset2 == null){
			return false;
		}
		// if the first itemset is null (empty set), return true
		if(itemset1 == null){
			return true;
		}
		// if the length of itemset 1 is greater than the one of
		// itemset2, it cannot be the ancestor, so return false
		if(itemset1.length >= itemset2.length){
			return false;
		}
		// otherwise, loop on items from itemset1
		// and check if they are the same as itemset 2
		for(int i=0; i< itemset1.length; i++){
			// if one item is different, itemset1 is not the ancestor
			if(itemset1[i] != itemset2[i]){
				return false;
			}
		}
		// otherwise itemset1 is an ancestor of itemset2
		return true;
	}
	

	/**
	 * Method to check if two itemsets are equals
	 * @param itemset1 the first itemset
	 * @param itemset2 the second itemset
	 * @param prefix 
	 * @return true if they are the same or false otherwise
	 */
	protected boolean same(int[] itemset1, int[] itemset2) {
		// if one is null, then returns false
		if(itemset2 == null || itemset1 == null){
			return false;
		}		
	
		if(itemset1.length != itemset2.length){
			return false;
		}
	
		for(int i=0; i< itemset1.length; i++){
			if(itemset1[i] != itemset2[i]){
				// if one is different then they are not the same
				return false;
			}
		}
		// otherwise they are the same
		return true;
	}

	public abstract int getSupportOfItemset(int[] s);

}
