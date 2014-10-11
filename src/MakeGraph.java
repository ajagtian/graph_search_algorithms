import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;


public class MakeGraph {
	
	private static Logger LOG = Logger.getLogger("MakeGraph");
	
	public HashMap<String,Object> getGraphFromFile(String [] args) {
		
		HashMap<String, Object> inputProperties = new HashMap<String, Object>();
		HashMap<String,Integer> nodes = new HashMap<String,Integer>();
		int nodeCount = 0;
		int weightMatrix [][] = null;
		
		String input = getFileInput();
		
		String lines [] = input.split("\n");
			
		
		if(lines.length > 1) {
			try{	
				inputProperties.put("task", Integer.parseInt(lines[0].trim()));
				inputProperties.put("source",lines[1].trim());
				inputProperties.put("target", lines[2].trim());
				inputProperties.put("nodeCount", Integer.parseInt(lines[3].trim()));
				if(inputProperties.get("nodeCount") != null) {
					nodeCount = (Integer)inputProperties.get("nodeCount");
					weightMatrix = new int[nodeCount][nodeCount];
				}
		
				int nodeID = 0;
				for(int i = 0 ; i < nodeCount ; i++) {
					nodes.put(lines[4 + i].trim(),nodeID++);
				}
				inputProperties.put("nodes",nodes);
			
				for ( int id_i = 0 ; id_i< nodeCount ; id_i++) {
					String words [] = lines[4 + nodeCount + id_i].trim().split(" +");
					for (int id_j = 0 ; id_j < nodeCount ; id_j++) {
						weightMatrix[id_i][id_j] = Integer.parseInt(words[id_j]);
					}
				}
				inputProperties.put("weightMatrix", weightMatrix);
			}
			catch(NumberFormatException e) {
				inputProperties = new HashMap<String, Object>();
			}
			catch(ArrayIndexOutOfBoundsException e){
				inputProperties = new HashMap<String, Object>();
			}
			
		}
		return inputProperties;
	}
	
	
	@SuppressWarnings("unchecked")
	public void runSearch(HashMap<String, Object> inputProperties){
		
		ArrayList<Node> nodeList = new ArrayList<Node>();
		
		Graph graph = new Graph();
	
		graph.setNodeCount((Integer)inputProperties.get("nodeCount"));
		
		HashMap<String, Integer> nodes = (HashMap<String, Integer>) inputProperties.get("nodes");
		for(String name : nodes.keySet()){
			Node n = new Node();
			n.setName(name);
			n.setId(nodes.get(name));
			n.setParent(null);
			n.setPathCost(0);
			
			nodeList.add(n);
		}
		graph.setNodes(nodeList);
		
		int [][] weightMatrix = (int[][]) inputProperties.get("weightMatrix");
		for (int i = 0 ; i < graph.getNodeCount(); i++) {
			for (int j = 0 ; j < graph.getNodeCount() ; j++) {
					graph.setEdgeWeight(i, j, weightMatrix[i][j]);
			}
		}
		int sourceId = nodes.get((String)inputProperties.get("source")) != null?nodes.get((String)inputProperties.get("source")):-1;
		int targetId = nodes.get((String)inputProperties.get("target")) != null?nodes.get((String)inputProperties.get("target")):-1;
		
		if((Integer)inputProperties.get("task") == 1) {
			Graph bfsTree = null;
			bfsTree = graph.performBFS(sourceId, targetId);
			if(bfsTree != null) {
				LOG.info(bfsTree.toString());
			}else{
				LOG.info("Source or Target not found.");
			}
		}
		
		if((Integer)inputProperties.get("task") == 2) {
			Graph dfsTree = null;
			dfsTree = graph.performDFS(sourceId, targetId);
			if(dfsTree != null){
				LOG.info(dfsTree.toString());
			}else{
				LOG.info("Source or Target not found.");
			}
		}
		
		if((Integer)inputProperties.get("task") == 3) {
			Graph ucsTree = null;
			ucsTree = graph.performUCS(sourceId, targetId);
			if(ucsTree != null){
				LOG.info(ucsTree.toString());
			}else{
				LOG.info("Source or Target not found.");
			}
		}
	}
	
	public static void main(String [] args) {
		LOG.info("**********************application log start**********************");
		MakeGraph makeGraph =  new MakeGraph();
		HashMap<String,Object> inputProperties = makeGraph.getGraphFromFile(args);
		if(!inputProperties.isEmpty()){
			makeGraph.runSearch(inputProperties);	
		}else{
			LOG.info("ERROR: incorrect/invalid file contents.");
		}
		LOG.info("*******************application log ends*******************");
	}
	
	private String getFileInput () {
		
		String fileDirectory = System.getProperty("user.dir");
		
		File file = new File(fileDirectory+"/input.txt");
		LOG.info("Input file= "+file.getAbsolutePath());
		
		FileInputStream fileStream = null;
		byte [] fileText = null;
		
		if (file.exists()) {
			fileText = new byte[(int)file.length()];
			try {
				fileStream = new FileInputStream(file);
				fileStream.read(fileText);
				fileStream.close();
			} 
			catch(FileNotFoundException e){
				LOG.info("ERROR: File not fount. "+e.getMessage());
			}catch (IOException e) {
				LOG.info("ERROR: problem reading file. "+e.getMessage());
			}
			finally{
				fileStream = null;
				System.gc();
			}
		}
		else{
			LOG.info("ERROR: File 'input.txt' does not exist.");
		}
		return fileText != null?new String(fileText):"";
	}
}