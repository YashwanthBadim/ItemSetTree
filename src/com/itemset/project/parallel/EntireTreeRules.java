package com.itemset.project.parallel;

import java.util.ArrayList;

import com.itemset.project.common.ItemList;

public class EntireTreeRules {

	private double entireSupport=0.3;
	private double subSupport=0.5;
	private double sizeR=0;
	private double sizeTree=0;

	public synchronized void setentireSupport(double entireSupport)
	{
		this.entireSupport=entireSupport;
	}

	public synchronized void setsubSupport(double subsupport)
	{
		this.subSupport=subsupport;
	}

	public synchronized void setentireSize(double sizeR)
	{
		this.sizeR=sizeR;
	}

	public synchronized void setSizeTree(double sizeTree)
	{
		this.sizeTree=sizeTree;
	}

	public synchronized void printResult(ArrayList<ItemList> list)
	{
		String total="";
		double minsupporter=0.0;
		double minentsupporter=0.0;
		double tree_subsupport=0.0;
		double tree_entsupport=0.0;
		double totalsupporter=0.0;

		for(ItemList item:list)
		{
			minsupporter=(double)item.getSupport()/sizeTree;
			if(minsupporter>tree_subsupport)
			{
				minentsupporter=(double)item.getSupport()/sizeR;	
				if(minentsupporter>tree_entsupport)
				{
					System.out.println(item.getList() +"	support/total >>>	:	"+item.getSupport()+"/"+(int)sizeR);
					total+=item.getSupport()+"/"+(int)sizeR+"+";
					totalsupporter+=(double)item.getSupport()/(int)sizeR;
				}
			}

		}
		if(total.length()>0)
			System.out.println("TOTAL ::>>>   "+total.substring(0,total.length()-1));
			System.out.println("TOTAL SUPPORT ::>>>   "+totalsupporter);

	}

}
