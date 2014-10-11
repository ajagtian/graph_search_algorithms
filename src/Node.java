public class Node implements Comparable<Node>{
	
	private int id;
	private String name;
	private Node parent;
	private int pathCost;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public int getPathCost() {
		return pathCost;
	}
	public void setPathCost(int pathCost) {
		this.pathCost = pathCost;
	}
	
	//@Override
	public int compareTo(Node o) {
		if(this.name.compareTo(o.getName()) < 0)
			return -1;
		else if(this.name.compareTo(o.getName()) > 0)
			return 1;
		else 
			return 0;			
	}
	
	//@Override
	public String toString() {
		return "Node [id=" + id + ", name=" + name + ", parent=" + parent
				+ ", pathCost=" + pathCost + "]";
	}
}

