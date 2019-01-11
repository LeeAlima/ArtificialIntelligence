import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to save a tree structure. Every node saves an attribute
 * name and if exist the childs with the edges name.
 * @author DELL
 */
public class Node {

    private String attr;
    // In the pattern of <EdgeName,SubTree>
    private Map<String, Node> childs;

    /**
     * c'tor
     * @param attributeName - the name of the attribute
     */
    public Node(String attributeName) {
        this.attr = attributeName;
        this.childs = new HashMap<String, Node>();
    }

    /**
     * This function adds a child to the map of childs
     * @param edge - the edge name
     * @param child - the subtree
     */
    public void addchild(String edge, Node child) {
        this.childs.put(edge, child);
    }

    /**
     * This function returns the name of the attribute
     * @return String - the attribute
     */
    public String getAttr() {
        return attr;
    }

    /**
     * This function returns a subtree if exists.
     * @param val - the name of the edge
     * @return the subtree containing the edge
     */
    public Node getChild(String val) {
        // Checks if the maps contain an edge with the val name
        if (childs.containsKey(val)) {
            // return the subtree
            return childs.get(val);
        }
        return null;
    }

    /**
     * This function checks if a this tree is a leaf
     * @return True - if the tree is a leaf, Otherwise returns False.
     */
    public Boolean checkIfLeaf() {
        if (this.childs.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * This function returns an array of the edges name.
     * @return an array of the edges name
     */
    public String[] getKeys() {
        return childs.keySet().toArray(new String[childs.keySet().size()]);
    }

}
