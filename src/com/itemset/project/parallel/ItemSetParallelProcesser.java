package com.itemset.project.parallel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.itemset.project.common.AbstractItemsetTree;
import com.itemset.project.common.ItemList;
import com.itemset.project.common.ItemsetTreeNode;

public class ItemSetParallelProcesser extends AbstractItemsetTree implements Serializable,Runnable{

	ArrayList<ArrayList<Integer>> listdata=null;
	private long startTime=0l;
	private boolean display=true;
	private boolean isComplete=false;
	
	private double subsupporter;
	private double entiresupporter;
	
	EntireTreeRules etr=null;
	
	EntireSupporter entireSupport=null;
	
	int[] query=null;
	
	public void setQuery(int[] query)
	{
		this.query=query;
	}
	
	public void setSupporter(EntireSupporter supporter)
	{
		this.entireSupport=supporter;
	}
	
	public void setEntireTreeRules(EntireTreeRules etr)
	{
		this.etr=etr;
	}
	public void setsubsupport(double subsupport)
	{
		this.subsupporter=subsupport;
	}
	
	public void setentiresupport(double entiresupport)
	{
		this.entiresupporter=entiresupport;
	}
	
	public void displayResult(boolean display)
	{
		this.display=display;
	}
	ArrayList<ItemList> list=null;
	
	public void setArrayList(ArrayList<ItemList> list) {
		this.list=list;
	}
	
	public boolean isComplete()
	{
		//isComplete=true;
		return isComplete;
	}
	
	ItemSetParallelProcesser(ArrayList<ArrayList<Integer>> listData,long startTime)
	{
		super();
		try
		{
		
		this.listdata=listData;
		Thread t=new Thread(this);
		t.start();
		
		this.startTime=startTime;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public void run()
	{

		
		root = new ItemsetTreeNode(null, 0,query,list);
		//System.out.println(listdata);
		for(int i=0;i<listdata.size();i++)
		{
			construct(root, root, listToArray(listdata.get(i)));
		}
		entireSupport.setEntireSupport(root.support);
		printTree();
		printQueryTree(list);
		
		etr.setentireSize(entireSupport.getSupport());
		etr.setSizeTree(root.support);
		etr.setsubSupport(subsupporter);
		etr.setentireSupport(entiresupporter);

		isComplete=true;
	}
	
	public int[] listToArray(ArrayList<Integer> list)
	{
		int[] array=new int[list.size()];
		for(int i=0;i<list.size();i++)
		{
			array[i]=list.get(i);
		}
		return array;

	}
	/**
	 * Default constructor
	 */
	public ItemSetParallelProcesser() {
		super();

	}

	public void buildTree(String input)
			throws IOException {
		// record start time
		startTimestamp = System.currentTimeMillis();

		// reset memory usage statistics
		//MemoryLogger.getInstance().reset();

		// create an empty root for the tree
		root = new ItemsetTreeNode(null, 0,query,list);

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
			//printTree();

			construct(null, root, itemset);
		}
		// close the input file
		reader.close();


		//MemoryLogger.getInstance().checkMemory();

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

		// if the itemset in root node is the same as the one to be inserted,
		// we just increase the support, and return.
		if(same(s, sr)){
			r.support++;
			return;
		}

		// if the node to be inserted is an ancestor of the itemset of the root node
		if(ancestorOf(s, sr)){
			// create a new node for the itemset to be inserted with the support of
			// the root node + 1
			ItemsetTreeNode newNode = new ItemsetTreeNode(s, r.support +1,query,list);
			// set the childs and parent pointers.
			newNode.childs.add(r);
			parentOfR.childs.remove(r);
			parentOfR.childs.add(newNode);

			return;  // return
		}

		// Otherwise, calculate the largest common ancestor
		// of the itemset to be inserted and the root of the subtree
		int[] l = getLargestCommonAncestor(s, sr);
		if(l != null){ // if there is one largest common ancestor
			// create a new node with that ancestor and the support of
			// the root +1.
			ItemsetTreeNode newNode = new ItemsetTreeNode(l, r.support +1,query,list);
			// set the node childs and parent pointers
			newNode.childs.add(r);
			parentOfR.childs.remove(r);
			parentOfR.childs.add(newNode);
			ItemsetTreeNode newNode2 = new ItemsetTreeNode(s, 1,query,list);
			// update pointers for the new node
			newNode.childs.add(newNode2);

			return;
		}

		// else  get the length of the root itemset
		int indexLastItemOfR = (sr == null)? 0 : sr.length;
		// increase the support of the root
		r.support++;
		// for each child of the root
		for(ItemsetTreeNode ci : r.childs){

			// if one children of the root is the itemset to be inserted s,
			// then increase its support and stop
			if(same(s, ci.itemset)){ // case 2
				ci.support++;
				return;
			}

			// if   the itemset to be inserted is an ancestor of the child ci
			if(ancestorOf(s, ci.itemset)){ // case 3
				// create a new node between ci and r in the tree
				// and update child /parents pointers
				ItemsetTreeNode newNode = new ItemsetTreeNode(s, ci.support+ 1,query,list);
				newNode.childs.add(ci);
				//				newNode.parent = r;
				r.childs.remove(ci);
				r.childs.add(newNode);
				//				ci.parent = newNode;
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
				// create a new node for the ancestor itemset just found with the support
				// of ci + 1
				ItemsetTreeNode newNode = new ItemsetTreeNode(ancestor, ci.support+ 1,query,list);
				// set r as aprent
				//				newNode.parent = r;
				r.childs.add(newNode);
				// add ci as a childre of the new node
				newNode.childs.add(ci);
				//				ci.parent = newNode;
				r.childs.remove(ci);
				// create another new node for s with a support of 1, which
				// will be the child of the first new node
				ItemsetTreeNode newNode2 = new ItemsetTreeNode(s, 1,query,list);
				//				newNode2.parent = newNode;
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


	/**
	 * Print statistics about the time and maximum memory usage for the construction
	 * of the itemset tree. 
	 */
	public void printStatistics() {

		System.out.println("========== ITEMSET TREE CONSTRUCTION - STATS ============");
		System.out.println(" Tree construction time ~: " + (endTimestamp - startTimestamp)
				+ " ms");
		//System.out.println(" Max memory:" + MemoryLogger.getInstance().getMaxMemory());
		nodeCount = 0;
		totalItemCountInNodes = 0;
		recursiveStats(root);
		System.out.println(" Node count: " + nodeCount);
		System.out.println(" Sum of items in all node: " + totalItemCountInNodes + " avg per node :" + totalItemCountInNodes / ((double)nodeCount));
		System.out.println("=====================================");
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
	/**
	 * Print the tree to System.out.
	 */
	public void printTree() {
		System.out.println(root.toString(new StringBuffer(),""));
	}
	
	public void printQueryTree(ArrayList<ItemList> totallist) {
		recursiveStatchecker(root,totallist);
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

		int count =0;
		for(ItemsetTreeNode ci : root.childs){
			if(ci.itemset[0]  <= s[0]){
				if(includedIn(s, ci.itemset)){
					count += ci.support;
				}else if(ci.itemset[ci.itemset.length -1] < s[s.length -1]){  
					count += count(s, ci);
				}
			}
		}
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
