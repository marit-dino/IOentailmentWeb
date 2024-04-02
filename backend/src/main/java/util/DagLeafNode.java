package util;

import java.util.Objects;

public class DagLeafNode extends DagNode{
    private String var;

    public DagLeafNode(String var) {
        super();
        this.setType(Type.VAR);
        this.var = var;
    }

    /**
     * Gets the name of the variable.
     *
     * @return variable name
     */
    public String getVar() {
        return var;
    }

    /**
     * Sets the name of the variable.
     *
     * @param var new name
     */
    public void setVar(String var) {
        this.var = var;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DagLeafNode that = (DagLeafNode) o;
        return Objects.equals(var, that.var);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), var);
    }

    @Override
    public String formatNode(){
        return getType() + ": " + this.var;
    }

    /**
     * Returns a representation of the variable in a given world.
     *
     * @param i world
     * @return name of variable in world i
     */
    public String getVarInWorld(int i) {
        return this.var + "[w" + i + "]";
    }
}
