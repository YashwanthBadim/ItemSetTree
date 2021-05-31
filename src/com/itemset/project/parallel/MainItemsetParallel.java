package com.itemset.project.parallel;

import java.io.IOException;
import java.util.ArrayList;

import com.itemset.project.common.ItemList;

public class MainItemsetParallel {
	
	public static void main(String[] args) throws IOException
	{
		
		long startTimestamp = System.currentTimeMillis();
		ItemSetTreeSeparator ttsp=new ItemSetTreeSeparator();
		String input = "ColumnSet1.txt";  // the database
		double subsupport =0.5;
		double entiresupport=0.0;
		ArrayList<ItemList> list=new ArrayList<ItemList>();
		ttsp.setQuery(new int[]{1,33,174,232});
		ttsp.setArrayList(list);
		ttsp.buildTree(input);
		ttsp.setentiresupport(entiresupport);
		ttsp.setsubsupport(subsupport);
		long endTimestamp = System.currentTimeMillis();
		System.out.println("Total time taken is :"+(endTimestamp-startTimestamp)+"ms");
	}
}
