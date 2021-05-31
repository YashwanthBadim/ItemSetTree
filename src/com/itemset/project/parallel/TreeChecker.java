package com.itemset.project.parallel;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeChecker {

	private HashMap<Integer,ArrayList<ArrayList<Integer>>> itemNodeMapper=new HashMap<Integer, ArrayList<ArrayList<Integer>>>(); 
	private ArrayList<Integer> mapList=null;
	public void addTree(int itemNode,int addData,boolean flag)
	{
		if(flag)
		{
			mapList=new ArrayList<Integer>();
		}

		if(itemNodeMapper.get(itemNode)==null)
		{
			ArrayList<ArrayList<Integer>> combineData=new ArrayList<ArrayList<Integer>>();
			mapList.add(addData);
			combineData.add(mapList);
			itemNodeMapper.put(itemNode, combineData);
		}
		else
		{
			if(!flag)
			{
				itemNodeMapper.get(itemNode).get(itemNodeMapper.get(itemNode).size()-1).add(addData);	
			}
			else
			{
				mapList.add(addData);
				itemNodeMapper.get(itemNode).add(mapList);
			}
		}

	}

	public void printMap()
	{
		System.out.println(itemNodeMapper);
	}

	public HashMap<Integer,ArrayList<ArrayList<Integer>>> getMapperObj()
	{
		return itemNodeMapper;	
	}	
}
