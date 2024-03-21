package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Node for building the dag from the parse tree.
 * A node of the type   Root has >= 1 children,
 *                      Var has 0 children,
 *                      Imp, And, Or, Eq, Pair has 2 children, and
 *                      Neg has 1 child.
 * This is not checked by the program, as this results from the grammar.
 */
public class DagNode implements Comparable<DagNode>{
    private List<DagNode> children;
    private Type type;

    public DagNode(){
        this.children = new ArrayList<>();
    }


    /**
     * Gets the type of the node.
     *
     * @return type of the node
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the node to the given type.
     *
     * @param type new type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Adds a node to the children of this node, and sorts the children if applicable
     *
     * @param child to add
     */
    public void addChild(DagNode child) {
        this.children.add(child);
        if(!(type.equals(Type.IMP) || type.equals(Type.PAIR))) Collections.sort(this.children);
    }

    /**
     * Gets the children of this node.
     *
     * @return the children
     */
    public List<DagNode> getChildren() {
        return children;
    }

    /**
     * Sets the children of the node.
     *
     * @param children of the node
     */
    public void setChildren(List<DagNode> children) {
        this.children = children;
        if(!(type.equals(Type.IMP) || type.equals(Type.PAIR))) Collections.sort(this.children);
    }

    /**
     * Gets the child at index i of this node.
     *
     * @param i index
     * @return child at index i
     */
    public DagNode getChild(int i) {
        return children.get(i);
    }

    /**
     * Gets the number of children.
     *
     * @return number of children
     */
    public int numberOfChildren(){
        return this.children.size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DagNode other = (DagNode) obj;
        if (children == null) {
            if (other.children != null)
                return false;
        } else if (!children.equals(other.children))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    @Override
    public int compareTo(DagNode arg0) {
        if(!this.type.equals(arg0.getType())) return this.type.compareTo(arg0.getType());
        if(this instanceof DagLeafNode) return ((DagLeafNode) arg0).getVar().compareTo(((DagLeafNode) this).getVar());

        if (this.children == null && arg0.getChildren() == null) return 0;
        if (this.children == null) return -1;
        if (arg0.getChildren() == null) return 1;

        int diff = Integer.compare(this.children.size(), arg0.getChildren().size());
        if (diff != 0) return diff;

        for (int i = 0; i < this.children.size(); i++) {
            int elem = this.children.get(i).compareTo(arg0.getChildren().get(i));
            if (elem != 0) {
                return elem;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return printDag(this, 0);
    }

    /**
     * Returns a string representing the dag, with indentations depending on the depth of the nodes.
     *
     * @param dagNode current node
     * @param i depth of current node
     * @return string representation of the dag
     */
    private String printDag(DagNode dagNode, int i) {
        String ret = "";
        for (int j = 0; j < i; j++) {
            ret += " ";
        }
        ret += dagNode.formatNode();
        ret += "\n";
        for (int j = 0; j < dagNode.children.size(); j++) {
            ret += printDag(dagNode.children.get(j), i + 1);
        }
        return ret;
    }

    /**
     * Returns a string representation of the node for printing.
     *
     * @return string of type
     */
    public String formatNode(){
        return "" + this.type;
    }


}
