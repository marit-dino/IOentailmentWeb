package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class DagLeafNodeTest {

    @Test
    public void equals_withSameValue_true() {
        DagLeafNode node1 = new DagLeafNode("A");
        DagLeafNode node2 = new DagLeafNode("A");

        assertEquals(node1, node2);
    }

    @Test
    public void equals_withDifferentValue_false() {
        DagLeafNode node1 = new DagLeafNode("A");
        DagLeafNode node2 = new DagLeafNode("AA");

        assertNotEquals(node1, node2);
    }

    @Test
    public void varInWorld_includesWorld() {
        DagLeafNode node = new DagLeafNode("A");
        assertEquals(node.getVarInWorld(42), "A[42]");
    }
}