package com.itemset.project.sequential;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import com.itemset.project.common.ItemList;
import com.itemset.project.common.ItemsetTree;

public class MainItemsetTree {

	public static void main(String [] arg) throws IOException{

		long starttime=System.currentTimeMillis();
		String input = "ColumnSet1.txt";  // the database
		ItemsetTree itemsetTree = new ItemsetTree();
		ArrayList<ItemList> list=new ArrayList<ItemList>();
		ArrayList<ItemList> totallist=new ArrayList<ItemList>();
		itemsetTree.setArrayList(list);
		itemsetTree.setQuery(new int[]{1,33});
		itemsetTree.buildTree(input);
		itemsetTree.printStatistics();
		itemsetTree.printQueryTree(totallist);
		System.out.println("THIS IS THE TREE:");
		itemsetTree.printTree();
		String total="";
		double minSup=0.0;
		double supporter=0.0;
		double totalsupprter=0.0;
		for(ItemList item:totallist)
		{
			supporter=(double)item.getSupport()/itemsetTree.totalsupport;
			if(supporter>minSup)
			{
				System.out.println(item.getList() +"	support/total >>>	:	"+item.getSupport()+"/"+itemsetTree.totalsupport );
				total+=item.getSupport()+"/"+itemsetTree.totalsupport+"+";
				totalsupprter+=(double)item.getSupport()/itemsetTree.totalsupport;
			}
		}
		if(total.length()>0)
			System.out.println("TOTAL ::>>>   "+total.substring(0,total.length()-1));
		
		System.out.println("TOTAL SUPPORT ::>>>   "+totalsupprter);
		long endtime=System.currentTimeMillis();
		System.out.println("TIME :"+(endtime-starttime));

	}

	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainItemsetTree.class.getResource(filename);
		return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}