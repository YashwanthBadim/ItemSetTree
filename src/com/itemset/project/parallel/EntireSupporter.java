package com.itemset.project.parallel;

public class EntireSupporter {
	
	public int support=0;
	
	public void setEntireSupport(int support)
	{
		this.support+=support;
	}
	
	
	public int getSupport()
	{
		return support;
	}
	
	public void supportincremental()
	{
		this.support++;
	}

}
