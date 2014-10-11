import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

// Class Graph..

public class Graph {
	
	private static Logger LOG = Logger.getLogger("Graph");
	
	private int nodeCount;
	private List<Node> nodes;
	private int [][] weightMatrix;
	
	final Comparator<Node> UCS_EDGE_COMPARATOR = new Comparator<Node>() {
		
		//@Override
		public int compare(Node o1, Node o2) {
			if(o1.getPathCost() < o2.getPathCost())
				return -1;
			else if(o1.getPathCost() > o2.getPathCost()) 
				return 1;
			else{
				if(o1.getName().compareTo(o2.getName()) < 0)
					return -1;
				else if(o1.getName().compareTo(o2.getName()) > 0)
						return 1;
				else
					return 0;
			}
		}
	};
	
	
	public void setNodeCount(int nodeCount){
		this.nodeCount = nodeCount;
		weightMatrix = new int[nodeCount][nodeCount];
	}
	
	public int getNodeCount() {
		return this.nodeCount;
	}
	
	public int setEdgeWeight(int id1, int id2, int weight) {
		if(id1 >= nodeCount || id2 >= nodeCount) {
			return -1;
		}
		else{
			weightMatrix[id1][id2] = weight;
			return weight;
		}
	}
	
	public int setEdge(int id1, int id2) {
		if(id1 >= nodeCount || id2 >= nodeCount) {
			return -1;
		}
		else{
			weightMatrix[id1][id2] = 1;
			return 1;
		}
	}
	
	public void setNodes(Collection<Node> nodes){
		this.nodes = new ArrayList<Node>(nodes);
	}
		
	private Node getNodeFromId(int id){ 
		Node node = null;
		for (Node n : nodes) {
			if(n.getId() == id){
				node = n;
				break;
			}
 		}
		return node;
	}
	
	public int hasEdge(int id1, int id2) {
		if(id1 >= nodeCount || id2 >= nodeCount) {
			return -1;
		}
		else{
			return weightMatrix[id1][id2];
		}
	}
	
	public Graph performBFS(int sourceId, int targetId) {
		Graph bfsTree = this;
		
		Node sourceNode = getNodeFromId(sourceId);
		Node targetNode = getNodeFromId(targetId);
		
		if (sourceNode == null || targetNode == null) {
			return null;
		}
		
		ArrayList<Node> searchQueue = new ArrayList<Node>();
		int nodeStatus[] = new int[nodeCount];
		Node dqueuedNode = null;
		
		String traverseLOG = "";
		String shortestPathLOG = "";
		int shortestPathCost;
		
		do {
			if(searchQueue.isEmpty() && nodeStatus[sourceNode.getId()] == 0) {
				sourceNode.setParent(null);
				sourceNode.setPathCost(0);
				searchQueue.add(sourceNode);
				nodeStatus[sourceNode.getId()] = 1;
			}
			if(searchQueue.isEmpty()){
				dqueuedNode = null;
			}
			else{
				dqueuedNode = searchQueue.remove(0);
			}
			if(dqueuedNode != null) {
				nodeStatus[dqueuedNode.getId()] = 2;
				LOG.info(dqueuedNode.getName()+"- SEARCED");
				traverseLOG += dqueuedNode.getName()+"-";
				for (int i = 0; i< nodeCount ; i++){
					if(weightMatrix[dqueuedNode.getId()][i] != 0 && nodeStatus[i] == 0){
						Node node = getNodeFromId(i);
						if(node != null) {
							node.setParent(dqueuedNode);
							node.setPathCost(dqueuedNode.getPathCost() + 1);
							nodeStatus[i] = 1;
							searchQueue.add(node);
						}
					}
					else if(weightMatrix[dqueuedNode.getId()][i] != 0 && nodeStatus[i] != 0){
						bfsTree.setEdgeWeight(dqueuedNode.getId(), i,0);
					}
				}
				Collections.sort(searchQueue,UCS_EDGE_COMPARATOR);
			}	
		}while(dqueuedNode != null && dqueuedNode != targetNode);
		
		List<Node> shortestPath = getShortestPath(sourceNode, targetNode);
		
		if(shortestPath.size() == 1 && sourceNode != targetNode) {
			LOG.info("No Path");
			shortestPathLOG = "NoPathAvailable";
		}
		else {
			while (!shortestPath.isEmpty()) {
				Node n = shortestPath.remove(shortestPath.size()-1);
				LOG.info(n.getName()+"- ON SHORTEST PATH");
				shortestPathLOG += n.getName()+"-";
			}		
		}
		Node temp = targetNode;
		shortestPathCost = 0;
		while(temp != null) {
			if(temp.getParent() != null){
				shortestPathCost += weightMatrix[temp.getParent().getId()][temp.getId()];
			}	
			temp = temp.getParent();
		}
		LOG.info("path cost-"+shortestPathCost);
		writeFile(traverseLOG, shortestPathLOG, shortestPathCost);
		return bfsTree;
	}
	
	
	public Graph performDFS(int sourceId, int targetId) {
		Graph dfsTree = this;
		
		Node sourceNode = getNodeFromId(sourceId);
		Node targetNode = getNodeFromId(targetId);
		
		if (sourceNode == null || targetNode == null) {
			return null;
		}
		
		ArrayList<Node> searchStack = new ArrayList<Node>();
		ArrayList<Node> neighborList = new ArrayList<Node>();
		
		int nodeStatus[] = new int[nodeCount];
		Node poppedNode = null;
		
		String traverseLOG = "";
		String shortestPathLOG = "";
		int shortestPathCost;
		
		do {
			neighborList = new ArrayList<Node>();
			if(searchStack.isEmpty() && nodeStatus[sourceNode.getId()] == 0) {
				sourceNode.setParent(null);
				sourceNode.setPathCost(0);
				searchStack.add(sourceNode);
				nodeStatus[sourceNode.getId()] = 1;
			}
			if(searchStack.isEmpty()){
				poppedNode = null;
			}
			else{
				poppedNode = searchStack.remove(searchStack.size()-1);
			}
			if(poppedNode != null) {
				nodeStatus[poppedNode.getId()] = 2;
				LOG.info(poppedNode.getName()+"- SEARCHED");
				traverseLOG += poppedNode.getName()+"-";
				for (int i = 0; i< nodeCount ; i++){
					if(weightMatrix[poppedNode.getId()][i] != 0 && nodeStatus[i] == 0){
						Node node = getNodeFromId(i);
						if(node != null) {
							node.setParent(poppedNode);
							node.setPathCost(poppedNode.getPathCost() + weightMatrix[poppedNode.getId()][i]);
							nodeStatus[i] = 1;
							neighborList.add(node);
						}
					}
				
					else if(weightMatrix[poppedNode.getId()][i] != 0 && nodeStatus[i] != 0){
						dfsTree.setEdgeWeight(poppedNode.getId(), i,0);
					}
				}
				Collections.sort(neighborList);
				Collections.reverse(neighborList);
				searchStack.addAll(neighborList);
			}	
			
		}while(poppedNode != null && poppedNode != targetNode);
		
		List<Node> shortestPath = getShortestPath(sourceNode, targetNode);
		
		if(shortestPath.size() == 1 && sourceNode != targetNode) {
			LOG.info("No Path");
			shortestPathLOG = "NoPathAvailable";
		}
		else {
			while (!shortestPath.isEmpty()) {
				Node n = shortestPath.remove(shortestPath.size()-1);
				LOG.info(n.getName()+"- ON SHORTEST PATH");
				shortestPathLOG += n.getName()+"-";
			}		
		}
		
		shortestPathCost = targetNode.getPathCost();
		LOG.info("path cost-"+shortestPathCost);
		writeFile(traverseLOG, shortestPathLOG, shortestPathCost);
		
		return dfsTree;
	}
	
	public Graph performUCS(int sourceNodeId, int targetNodeId) {
		Graph ucsTree = null;
		ucsTree = this;
		Node sourceNode = getNodeFromId(sourceNodeId);
		Node targetNode = getNodeFromId(targetNodeId);
		if (sourceNode == null || targetNode == null) {
			return null;
		}
		ArrayList<Node> priorityQueue = new ArrayList<Node>();
		int [] nodeStatus = new int[nodeCount];
		Node dequededNode = null;
		
		String traverseLOG = "";
		String shortestPathLOG = "";
		int shortestPathCost;
		
		do {
			if(priorityQueue.isEmpty() && nodeStatus[sourceNode.getId()] == 0) {
				sourceNode.setParent(null);
				sourceNode.setPathCost(0);
				priorityQueue.add(sourceNode);
				nodeStatus[sourceNode.getId()] = 1;
			}
			if(priorityQueue.isEmpty()) {
				dequededNode = null;
			}
			else{
				dequededNode = priorityQueue.remove(0);
				LOG.info(dequededNode.getName()+ "- SEARCHED");
				traverseLOG +=dequededNode.getName()+"-";
			}
			if(dequededNode != null) {
				nodeStatus[dequededNode.getId()] = 2;
				for (int i = 0 ; i< nodeCount ; i++) {
					if(weightMatrix[dequededNode.getId()][i] != 0) {
						if(nodeStatus[i] == 0) {
							Node node = getNodeFromId(i);
							node.setParent(dequededNode);
							node.setPathCost(dequededNode.getPathCost() +  weightMatrix[dequededNode.getId()][i]);
							priorityQueue.add(node);
							Collections.sort(priorityQueue, UCS_EDGE_COMPARATOR);
							nodeStatus[i] = 1;
						}
						else if(nodeStatus[i] == 1) {
							Node node = getNodeFromId(i);
							int pathCost = dequededNode.getPathCost() + weightMatrix[dequededNode.getId()][i];
							if(pathCost < node.getPathCost()) {
								ucsTree.setEdgeWeight(node.getParent().getId(), i, 0);
								priorityQueue.remove(node);
								node.setParent(dequededNode);
								node.setPathCost(pathCost);
								priorityQueue.add(node);
								Collections.sort(priorityQueue, UCS_EDGE_COMPARATOR);
								nodeStatus[i] = 1;
							}	
							else {
								ucsTree.setEdgeWeight(dequededNode.getId(), i, 0);
							}
						}
						else if (nodeStatus[i] == 2) {
							int pathCost = dequededNode.getPathCost() +  weightMatrix[dequededNode.getId()][i];
							Node node = getNodeFromId(i);
							if(pathCost < node.getPathCost()) {
								ucsTree.setEdgeWeight(node.getParent().getId(), i,0);
								priorityQueue.remove(node);
								node.setParent(dequededNode);
								node.setPathCost(pathCost);
								priorityQueue.add(node);
								Collections.sort(priorityQueue, UCS_EDGE_COMPARATOR);
								nodeStatus[i] = 1;
							}
							else{
								ucsTree.setEdgeWeight(dequededNode.getId(), i, 0);
							}
						}
					}
						
				}
 			}
		}while(dequededNode != null && dequededNode!= targetNode);
		
		List<Node> shortestPath = getShortestPath(sourceNode, targetNode);
		
		if(shortestPath.size() == 1 && sourceNode != targetNode) {
			LOG.info("No Path");
			shortestPathLOG = "NoPathAvailable";
		}
		else {
			while (!shortestPath.isEmpty()) {
				Node n = shortestPath.remove(shortestPath.size()-1);
				LOG.info(n.getName()+"- ON SHORTEST PATH");
				shortestPathLOG += n.getName()+"-";
			}		
		}
		shortestPathCost = targetNode.getPathCost();
		LOG.info("path cost-"+shortestPathCost);	
		writeFile(traverseLOG, shortestPathLOG, shortestPathCost);
		
		return ucsTree;
	}
	
	private List<Node> getShortestPath(Node sourceNode, Node targetNode) {
		ArrayList<Node> stack = new ArrayList<Node>();
		do{
			stack.add(targetNode);
			targetNode = targetNode.getParent();
		}while(targetNode != null);
		return stack;
	}

	//@Override
	public String toString() {
		return "Graph [nodeCount=" + nodeCount + ", nodes=" + nodes
				+ ", weightMatrix=" + Arrays.toString(weightMatrix) + "]";
	}
	
	private void writeFile(String traverseLOG, String shortestPathLog, int shortestPathCost) {
		File file = new File("output.txt");
		traverseLOG = traverseLOG.substring(0,traverseLOG.length()-1)+"\n";
		if(shortestPathLog.compareTo("NoPathAvailable") != 0){
			shortestPathLog = shortestPathLog.substring(0,shortestPathLog.length()-1)+"\n";
		}
		final byte [] TRAVERSELOG = traverseLOG.getBytes();
		final byte [] PATH = shortestPathLog.getBytes();
		final byte [] COST = ((Integer.toString(shortestPathCost))).getBytes();
		FileOutputStream outputStream = null;

		try {
			outputStream = new FileOutputStream(file);
			if(shortestPathLog.compareTo("NoPathAvailable") != 0) {
				outputStream.write(TRAVERSELOG);
			}
			outputStream.write(PATH);
			if(shortestPathLog.compareTo("NoPathAvailable") != 0) {
				outputStream.write(COST);
			}
			outputStream.close();
			LOG.info("### output.txt created at- "+file.getAbsolutePath());
		
		} catch (FileNotFoundException e) {
			LOG.info("ERROR: Could not create output.txt "+e.getMessage());
		} catch (IOException e) {
			LOG.info("ERROR: Could not print to output.txt "+e.getMessage());
		}
		finally{
			outputStream = null;
			System.gc();
		}	
	}
}
