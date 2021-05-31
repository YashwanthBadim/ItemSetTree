package com.itemset.project.parallel;

import java.util.List;
import java.util.Vector;

public class ProcessEnd {

	public  List<ItemSetParallelProcesser> adderList=new Vector<ItemSetParallelProcesser>();
	
	public void ProcessEnd()
	{
		
	}
	
	public synchronized void add(ItemSetParallelProcesser adder)
	{
		adderList.add(adder);
	}
	
	public synchronized void remove()
	{
		
		for(int i=0;i<adderList.size();i++)
		{
			if(adderList.get(i).isComplete())
			{
				adderList.remove(i);
			}
			else
			{
				
			}
		}		
	}

}
