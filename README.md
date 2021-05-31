# ItemSetTree
Introduction to ItemSet:
An Itemset tree is a data structure which aids in users querying for a specific Itemset and its support. 
Let us consider a market basket to analyze Itemsets and Items of an Itemset tree. 
Each item in the supermarket is considered as an item in the Itemset tree and every transaction in the supermarket is considered as an Itemset [2]. 
Let I = {milk, Bread, cheese….etc} be the set of all items. Then, an item set is a subset of I. 
For examples, {milk}, {milk, bread}, {milk, bread, cheese}….etc. can be itemsets. 
Support of an Itemset is the probability that an Itemset X will appear in the database [1]. 
Items within a transaction are mapped to integer values and ordered such that each transaction is in lexical order. 
Using numerical values makes the tree more compact and also numerics follow ordering easily [1].

Project:
Parallelizing the Itemset Tree Data Structure is to reduce the time of searching the support value by passing a query to an Itemset Tree which is broken down into different trees from its root node.
The main goal of this project is on improving the time efficiency of the search algorithm [2] on the Itemset trees by breaking apart a complete tree into distributed trees from the top level of a complete tree.

Data Mining Approach : 
Using the datasets we build the Itemset tree. While building the Itemset tree, we need to keep track of each nodes frequency [1] of an Itemset and store its value in a Hash Table.
When we try to find the support of a given query, we get the frequency of each and every node of the Itemset Tree and then store its values in a Hash table. Then we add all the frequency values of the nodes in the table over total number of transactions.

Next phase of this project is to breakdown the complete tree from its 1st level of the tree by removing the root node. 
To make that happen we have taken the whole database and then grouped the transactions into different tables by considering first numeric value. All transactions are used to construct subtrees using the construct function. This way we have constructed the sub trees by removing the root node from the complete tree. Now we run the same query on the different sub trees formed by splitting the complete tree in parallel. 

Here when we are running the query in different sub trees in parallel to find the support for the query, we use the same method which has been used in the complete tree to find the support. But here we will find the support of the query in different sub trees and use a local minsup value to remove the support values of the nodes which have less than minsup. Values less than minsup will be ignored and then the rest of the values will be added. 
Here we have to wait to find the total support value until we get the support value of each and every sub tree. As we get the support values of the query in every sub tree then we get the total support value by adding all the support values of the sub tree over the total number of transactions in the database. Here we have used the local minsup value in the sub trees and also a global minsup value after getting the support values from the subtrees. 
Then we add all the support values to get the total support.

Conclusion:
We were able to successfully reduce the execution time to find the support value on the parallel trees when compared to the complete tree.
