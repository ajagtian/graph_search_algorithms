<i>Search Algorithms on large graphs</i>
=======================

This is Java based library for search and trace algorithms used in large networks, like the social networks and communication networks.

<b>Functionality - For Communication Networks -></b> <pre>Create a minimum spanning tree for the network, trace a machine on network, find the nearest machine, find the cost of communication between 2 machines<br />....</pre>

<b>Functionality - For social networks -></b><pre>find friends, friends of friends, find the degre of closeness, suggest friends<br />...</pre>

To run the code -> make some appropriate I/O of your choice, one such I/O implementation is given in MakeGraph class.

<b>Sample input file</b> - In the sample I/O, graph is fed into the algorithm through a txt file. Sample file is present in code base. Here is the structure of <i>input.txt</i>.

<pre>
	<i>input.txt - for searching a node</i>
	- - - - - - - -
	<br />
	line 1: choide of search algorithm
	line 2: source node
	line 3: target node
	line 4: number of nodes
	line 5: ---
		list nodes...
	line 5 + number of nodes - 1: ----
		graph in the form of adjacency matrix.
	line 5+2*number of nodes - 1. --end
</pre>

<b>To run the code</b>

<pre>
	javac Node.java Graph.java *.java

	java *.java

	<ul><li>*.java your I/O interface class</li><ul>
</pre>	