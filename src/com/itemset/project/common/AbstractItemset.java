package com.itemset.project.common;

import java.text.DecimalFormat;

public abstract class AbstractItemset {

	public AbstractItemset() {
		super();
	}

	public abstract int size();
	
	public abstract String toString();
	
	public void print() {
		System.out.print(toString());
	}
	
	public abstract int getAbsoluteSupport();

	public abstract double getRelativeSupport(int nbObject);
	
	public String getRelativeSupportAsString(int nbObject) {
		// get the relative support
		double frequence = getRelativeSupport(nbObject);
		// convert it to a string with two decimals
		DecimalFormat format = new DecimalFormat();
		format.setMinimumFractionDigits(0); 
		format.setMaximumFractionDigits(5); 
		return format.format(frequence);
	}
	
	
	/**
	 * Check if this itemset contains a given item.
	 * @param item  the item
	 * @return true if the item is contained in this itemset
	 */
	public abstract boolean contains(Integer item);

}