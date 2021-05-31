package com.itemset.project.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ItemsetTree extends AbstractItemsetTree implements Serializable{

	/**
	 * Default constructor
	 */
	public ItemsetTree() {
		super();
	}
	int[] query=null;
	ArrayList<ItemList> list=null;

	public void setQuery(int[] q) {
		this.query=q;
	}


	public void setArrayList(ArrayList<ItemList> list) {
		this.list=list;
	}

	public void buildTree(String input)
			throws IOException {
		startTimestamp = System.currentTimeMillis();
		root = new ItemsetTreeNode(null, 0,query,list);
		root.setQuery(query);

		// Scan the database to read the transactions
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line;
		// for each line (transaction) until the end of file
		while (((line = reader.readLine()) != null)) {

			if (line.isEmpty() == true ||
					line.charAt(0) == '#' || line.charAt(0) == '%'
					|| line.charAt(0) == '@') {
				continue;
			}
			String[] lineSplited = line.split(" ");
			// create a structure for storing the transaction
			int[] itemset = new int[lineSplited.length];
			// for each item in the transaction
			for (int i=0; i< lineSplited.length; i++) {
				// convert the item to integer and add it to the structure
				itemset[i] = Integer.parseInt(lineSplited[i]);
			}

			construct(null, root, itemset);
		}

		reader.close();
		//	MemoryLogger.getInstance().checkMemory();

		endTimestamp = System.currentTimeMillis();
	}

	/**
	 * Add a transaction to the itemset tree.
	 * @param transaction the transaction to be added (array of ints)
	 */
	public void addTransaction(int[] transaction){
		// call the "construct" algorithm to add it
		construct(null, root, transaction);
	}


	/**
	 * Given the root of a sub-tree, add an itemset at the proper position in that tree
	 * @param r  the root of the sub-tree
	 * @param s  the itemset to be inserted
	 */
	public void construct(ItemsetTreeNode parentOfR, ItemsetTreeNode r, int[] s) {
		// get the itemset in the root node
		int[] sr = r.itemset;
		if(same(s, sr)){
			r.support++;
			return;
		}

		// if the node to be inserted is an ancestor of the itemset of the root node
		if(ancestorOf(s, sr)){

			ItemsetTreeNode newNode = new ItemsetTreeNode(s, r.support+1,query,list);
			newNode.childs.add(r);
			parentOfR.childs.remove(r);
			parentOfR.childs.add(newNode);
			return; 
		}


		int[] l = getLargestCommonAncestor(s, sr);
		if(l != null){ // if there is one largest common ancestor

			ItemsetTreeNode newNode = new ItemsetTreeNode(l, r.support +1,query,list);
			newNode.childs.add(r);
			parentOfR.childs.remove(r);
			parentOfR.childs.add(newNode);
			ItemsetTreeNode newNode2 = new ItemsetTreeNode(s, 1,query,list);
			newNode.childs.add(newNode2);
			return;
		}


		int indexLastItemOfR = (sr == null)? 0 : sr.length;
		r.support++;
		for(ItemsetTreeNode ci : r.childs){

			if(same(s, ci.itemset)){ 
				ci.support++;
				return;
			}


			if(ancestorOf(s, ci.itemset)){ 
				ItemsetTreeNode newNode = new ItemsetTreeNode(s, ci.support+ 1,query,list);
				newNode.childs.add(ci);

				r.childs.remove(ci);
				r.childs.add(newNode);

				return;
			}

			// if the child ci is an ancestor of s
			if(ancestorOf(ci.itemset, s)){ // case 4
				// then make a recursive call to construct to handle this case.
				construct(r, ci, s);
				return;
			}

			// case 5
			// if ci and s have a common ancestor that is larger than r:
			if(ci.itemset[indexLastItemOfR] == s[indexLastItemOfR]){
				// find the largest common ancestor
				int[] ancestor = getLargestCommonAncestor(s, ci.itemset);
				ItemsetTreeNode newNode = new ItemsetTreeNode(ancestor, ci.support+ 1,query,list);
				r.childs.add(newNode);
				newNode.childs.add(ci);
				//				ci.parent = newNode;
				r.childs.remove(ci);
				ItemsetTreeNode newNode2 = new ItemsetTreeNode(s, 1,query,list);
				newNode.childs.add(newNode2);
				// end
				return;
			}

		}

		// Otherwise, case 1:
		// A new node is created for s with a support of 1 and is added
		// below the node r.
		ItemsetTreeNode newNode = new ItemsetTreeNode(s, 1,query,list);
		//		newNode.parent = r;
		r.childs.add(newNode);

	}



	public void printStatistics() {

		System.out.println("========== ITEMSET TREE CONSTRUCTION - STATS ============");
		System.out.println(" Tree construction time ~: " + (endTimestamp - startTimestamp)
				+ " ms");
		//System.out.println(" Max memory:" + MemoryLogger.getInstance().getMaxMemory());
		nodeCount = 0;
		totalItemCountInNodes = 0;
		totalsupport=root.support;
		recursiveStats(root);
		System.out.println(" Node count: " + nodeCount);
		System.out.println(" Sum of items in all node: " + totalItemCountInNodes + " avg per node :" + totalItemCountInNodes / ((double)nodeCount));
		System.out.println("=====================================");
	}


	public void printQueryTree(ArrayList<ItemList> totallist) {
		recursiveStatchecker(root,totallist);
	}


	private void recursiveStats(ItemsetTreeNode root) {
		if(root != null && root.itemset!=null){
			nodeCount++;
			totalItemCountInNodes += root.itemset.length;
		}
		for(ItemsetTreeNode node : root.childs){
			recursiveStats(node);
		}
	}


	private void recursiveStatchecker(ItemsetTreeNode root,ArrayList<ItemList> totallist) {

		for(ItemsetTreeNode node : root.childs){

			if(isRootNode(node))
			{
				ItemList il=new ItemList();
				ArrayList<String> ar=new ArrayList<String>();
				int[] items=node.itemset;
				String quer="";
				for(int i=0;i<items.length;i++)
				{
					quer+=items[i]+" ";
				}
				ar.add(quer);
				il.setList(ar);
				il.setSupport(node.support);
				totallist.add(il);

			}
			else
			{
				recursiveStatchecker(node,totallist);
			}

		}
	}


	public boolean isRootNode(ItemsetTreeNode node)
	{

		int[] items=node.itemset;
		String quer="";
		String queryLen="";

		for(int i=0;i<items.length;i++)
		{
			quer+=items[i]+" ";
		}

		for(int i=0;i<query.length;i++)
		{
			queryLen+=query[i]+" ";
		}

		if(quer.contains(queryLen))
		{

			return checkItems(items);
		}
		else
			return false;
	}

	public boolean checkItems(int[] items)
	{
		int count=0;
		for(int i=0;i<query.length;i++)
		{
			for(int j=0;j<items.length;j++)
			{
				if(query[i]==items[j])
				{
					count++;
				}
			}
		}
		if(count==query.length)
			return true;
		else
			return false;
	}

	/**
	 * Print the tree to System.out.
	 */
	public void printTree() {
		System.out.println(root.toString(new StringBuffer(),""));
	}

	/**
	 * Return a string representation of the tree.
	 */
	public String toString() {
		return root.toString(new StringBuffer(), "");
	}

	/**
	 * Get the support of a given itemset s.
	 * @param s the itemset
	 * @return the support as an integer.
	 */
	public int getSupportOfItemset(int[] s) {
		return count(s, root);  // call the method count.
	}

	/**
	 * This method calculate the support of an itemset by using a subtree
	 * defined by its root.
	 * 
	 * Note: this is implemented based on the algorithm "count" of Table 2 in the paper by Kubat et al.
	// Note that there was a few problem in the algorithm in the paper.
	// I had to change > by < in :  ci.itemset[ci.itemset.length -1] < s[s.length -1]){ 
	// also the count was not correct so i had to change the way it counted the support a little bit
	// by using += instead of return.
	 * 
	 * @param s  the itemset
	 * @param root  the root of the subtree
	 * @return  the support as an integer
	 */
	private int count(int[] s, ItemsetTreeNode root) {
		// the variable count will be used to count the support
		int count =0;
		// for each child of the root
		for(ItemsetTreeNode ci : root.childs){
			// if the first item of the itemset that we are looking for
			// is smaller than the first item of the child, we need to look
			// further in that tree.
			if(ci.itemset[0]  <= s[0]){
				// if s is included in ci, add the support of ci to the current count.
				if(includedIn(s, ci.itemset)){
					count += ci.support;
				}else if(ci.itemset[ci.itemset.length -1] < s[s.length -1]){  
					// otherwise, if the last item of ci is smaller than
					// the last item of s, then  make a recursive call to explore
					// the subtree where ci is the root
					count += count(s, ci);
				}
			}
		}
		// return the total count
		return count;
	}


	/**
	 * Check if an itemset is contained in another
	 * @param itemset1 the first itemset
	 * @param itemset2 the second itemset
	 * @return true if yes, otherwise false
	 */
	private boolean includedIn(int[] itemset1, int[] itemset2) {
		int count = 0; // the current position of itemset1 that we want to find in itemset2

		// for each item in itemset2
		for(int i=0; i< itemset2.length; i++){
			// if we found the item
			if(itemset2[i] == itemset1[count]){
				// we will look for the next item of itemset1
				count++;
				// if we have found all items already, return true
				if(count == itemset1.length){
					return true;
				}
			}
		}
		// it is not included, so return false!
		return false;
	}



}
