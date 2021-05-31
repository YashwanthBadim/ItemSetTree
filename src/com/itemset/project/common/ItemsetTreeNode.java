package com.itemset.project.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class ItemsetTreeNode implements Serializable{


	public int[] itemset;
	public int[] query;
	public int support;
	public String strquery="";
	public int rootfreq=0;
	public Collection<ItemsetTreeNode> childs = new HashSet<ItemsetTreeNode>();
	private ArrayList<ItemList> itemList=null;
	public ItemsetTreeNode(int[] itemset, int support,int[] query,ArrayList<ItemList> list){
		this.itemset = itemset;
		this.support = support;
		this.query=query;
		for(int q:query)
		{
			strquery+=q+" ";
		}
		this.itemList=list;

	}


	public void setQuery(int[] query){

		for(int q:query)
		{
			strquery+=q+" ";
		}
		

	}

	public String toString(StringBuffer buffer, String space){
		buffer.append(space);
		String query="";

		if(itemset == null){
			buffer.append("{}");


		}else{
			buffer.append("[");
			for(Integer item : itemset){
				buffer.append(item);
				buffer.append(" ");
				query+=item+" ";
			}
			buffer.append("]");
		}
		buffer.append("   freq=");
		buffer.append(support);
		buffer.append("\n");
		for(ItemsetTreeNode node : childs){
			node.toString(buffer, space + "  ");
		}

		/*if(queryPrepare.contains(strquery))
		{
			ItemList il=new ItemList();
			ArrayList<String> ar=new ArrayList<String>();
			ar.add(queryPrepare);
			il.setList(ar);
			il.setSupport(support);
			remove(queryPrepare);
			itemList.add(il);
		}*/
		query="";
		strquery="";
		return buffer.toString();
	}

	public String toString(){
		return toString(new StringBuffer(), "  ");
	}

	/*public void remove(String qString)
	{
		for(int i=0;i<itemList.size();i++)
		{
			String s=itemList.get(i).getList().toString().replace("[", "").replace("]", "").trim();
			if(s.startsWith(qString))
			{
				itemList.remove(i);	
			}
		}
	}
*/

}
