package com.itemset.project.parallel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import com.itemset.project.common.ItemList;

public class ParallelProcessInitiator {

	private long startTime=0l;
	private int[] query=null;
	
	private double subsupport;
	private double entiresupport;
	
	private HashMap<Integer,ArrayList<ArrayList<Integer>>> itemNodeMapper;
	public ParallelProcessInitiator(HashMap<Integer,ArrayList<ArrayList<Integer>>> itemNodeMapper,long starttime)
	{
		this.itemNodeMapper=itemNodeMapper;		
		this.startTime=starttime;
	}
	
	
	public void setQuery(int[] query)
	{
		this.query=query;
	}
	
	public void setsubsupport(double subsupport)
	{
		this.subsupport=subsupport;
	}
	
	public void setentiresupport(double entiresupport)
	{
		this.entiresupport=entiresupport;
	}
	
	private ArrayList<ItemList> list=null;

	public void setArrayList(ArrayList<ItemList> list) {
		this.list=list;
	}

	public void startProcesser()
	{
		long startTime=System.currentTimeMillis();
		ItemSetParallelProcesser itp=null;
		Iterator<Integer> iterator= itemNodeMapper.keySet().iterator();
		Integer mapperKey=null;
		EntireSupporter entsupporter=new EntireSupporter();
		int mapCount=itemNodeMapper.size();
		int counter=0;
		ProcessEnd process=new ProcessEnd();
		EntireTreeRules etr=new EntireTreeRules();
		while(iterator.hasNext())
		{
			counter++;
			mapperKey=iterator.next();
			itp=new ItemSetParallelProcesser(itemNodeMapper.get(mapperKey),startTime);
			if(mapCount==counter)
			{
				itp.displayResult(true);
			}
			process.add(itp);	
			itp.setQuery(query);
			itp.setSupporter(entsupporter);
			itp.setentiresupport(entiresupport); 
			itp.setsubsupport(subsupport);
			itp.setEntireTreeRules(etr);
			itp.setArrayList(list);
		}
		while(process.adderList.size()>0)
		{
			process.remove();
		}
		etr.setentireSupport(entiresupport);
		etr.printResult(list);
	}



}
