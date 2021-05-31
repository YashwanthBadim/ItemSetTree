package com.itemset.project.parallel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.itemset.project.common.AbstractItemsetTree;
import com.itemset.project.common.ItemList;
import com.itemset.project.common.ItemsetTreeNode;

public class ItemSetTreeSeparator extends AbstractItemsetTree implements Serializable {

	ArrayList<ItemList> list=null;
	int[] query=null;
	double subsupport;
	double entiresupport;
	
	public void setQuery(int[] q) {
		this.query=q;
	}

	public void setArrayList(ArrayList<ItemList> list) {
		this.list=list;
	}
	
	public void setsubsupport(double subsupport)
	{
		this.subsupport=subsupport;
	}
	
	public void setentiresupport(double entiresupport)
	{
		this.entiresupport=entiresupport;
	}

	public void buildTree(String input)
			throws IOException {

		startTimestamp = System.currentTimeMillis();
		root = new ItemsetTreeNode(null, 0,query,list);
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line;
		TreeChecker tc=new TreeChecker();
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

			boolean flag=true;
			for (int i=0; i< lineSplited.length; i++) {
				// convert the item to integer and add it to the structure
				itemset[i] = Integer.parseInt(lineSplited[i]);
				tc.addTree(itemset[0], itemset[i],flag);
				flag=false;

			}
		}
		reader.close();
		ParallelProcessInitiator pi=new ParallelProcessInitiator(tc.getMapperObj(),startTimestamp);
		pi.setQuery(query);
		pi.setArrayList(list);
		pi.setsubsupport(subsupport);
		pi.setentiresupport(entiresupport);
		pi.startProcesser();
	   endTimestamp = System.currentTimeMillis();
	}

	@Override
	public int getSupportOfItemset(int[] s) {

		return 0;
	}

}
