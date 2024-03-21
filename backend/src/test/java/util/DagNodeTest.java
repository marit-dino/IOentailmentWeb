package util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DagNodeTest {

    @Test
    public void addChild_ToImplication_NotSorted() {
        DagNode node = new DagNode();
        node.setType(Type.IMP);
        DagNode node1 = new DagNode();
        DagNode node2 = new DagNode();

        node.addChild(node1);
        node.addChild(node2);

        assertEquals(node1, node.getChild(0));
        assertEquals(node2, node.getChild(1));

        node.setChildren(new ArrayList<>());
        node.addChild(node2);
        node.addChild(node1);

        assertEquals(node2, node.getChild(0));
        assertEquals(node1, node.getChild(1));
    }

    @Test
    public void addChild_ToPair_NotSorted() {
        DagNode node = new DagNode();
        node.setType(Type.PAIR);
        DagNode node1 = new DagNode();
        DagNode node2 = new DagNode();

        node.addChild(node1);
        node.addChild(node2);

        assertEquals(node1, node.getChild(0));
        assertEquals(node2, node.getChild(1));

        node.setChildren(new ArrayList<>());
        node.addChild(node2);
        node.addChild(node1);

        assertEquals(node2, node.getChild(0));
        assertEquals(node1, node.getChild(1));
    }

    @Test
    public void addChild_ToConjunction_sorted() {
        DagNode node = new DagNode();
        node.setType(Type.CON);
        DagNode node1 = new DagNode();
        DagNode node2 = new DagNode();
        node2.setType(Type.VAR);
        node1.setType(Type.VAR);

        node.addChild(node1);
        node.addChild(node2);

        assertEquals(node1, node.getChild(0));
        assertEquals(node2, node.getChild(1));

        node.setChildren(new ArrayList<>());
        node.addChild(node2);
        node.addChild(node1);

        assertEquals(node1, node.getChild(0));
        assertEquals(node2, node.getChild(1));
    }

    @Test
    public void addChild_ToDisjunction_sorted() {
        DagNode node = new DagNode();
        node.setType(Type.DIS);
        DagNode node1 = new DagNode();
        DagNode node2 = new DagNode();
        node2.setType(Type.VAR);
        node1.setType(Type.VAR);

        node.addChild(node1);
        node.addChild(node2);

        assertEquals(node1, node.getChild(0));
        assertEquals(node2, node.getChild(1));

        node.setChildren(new ArrayList<>());
        node.addChild(node2);
        node.addChild(node1);

        assertEquals(node1, node.getChild(0));
        assertEquals(node2, node.getChild(1));
    }


    @Test
    public void addChild_ToEquivalence_sorted() {
        DagNode node = new DagNode();
        node.setType(Type.EQ);
        DagNode node1 = new DagNode();
        DagNode node2 = new DagNode();
        node2.setType(Type.VAR);
        node1.setType(Type.VAR);

        node.addChild(node1);
        node.addChild(node2);

        assertEquals(node1, node.getChild(0));
        assertEquals(node2, node.getChild(1));

        node.setChildren(new ArrayList<>());
        node.addChild(node2);
        node.addChild(node1);

        assertEquals(node1, node.getChild(0));
        assertEquals(node2, node.getChild(1));
    }

    @Test
    public void addChild_ToRoot_sorted() {
        DagNode node = new DagNode();
        node.setType(Type.ROOT);
        DagNode node1 = new DagNode();
        DagNode node2 = new DagNode();
        node2.setType(Type.VAR);
        node1.setType(Type.VAR);

        node.addChild(node1);
        node.addChild(node2);

        assertEquals(node1, node.getChild(0));
        assertEquals(node2, node.getChild(1));

        node.setChildren(new ArrayList<>());
        node.addChild(node2);
        node.addChild(node1);

        assertEquals(node1, node.getChild(0));
        assertEquals(node2, node.getChild(1));
    }

    @Test
    public void equals_withNoChildren_sameType_true() {
        DagNode node1 = new DagNode();
        node1.setType(Type.ROOT);
        DagNode node2 = new DagNode();
        node2.setType(Type.ROOT);

        assertEquals(node1, node2);
    }

    @Test
    public void equals_withNoChildren_differentType_false() {
        DagNode node1 = new DagNode();
        node1.setType(Type.ROOT);
        DagNode node2 = new DagNode();
        node2.setType(Type.PAIR);

        assertNotEquals(node1, node2);
    }

    @Test
    public void equals_withSameChildren_sameType_true() {
        DagNode node1 = new DagNode();
        node1.setType(Type.PAIR);
        DagNode node2 = new DagNode();
        node2.setType(Type.PAIR);

        List<DagNode> nodes = new ArrayList<>();
        DagNode child1 = new DagNode();
        child1.setType(Type.IMP);
        DagNode child2 = new DagNode();
        child2.setType(Type.CON);
        DagNode child3 = new DagNode();
        child3.setType(Type.VAR);

        node1.setChildren(nodes);
        node2.setChildren(nodes);

        assertEquals(node1, node2);
    }

    @Test
    public void equals_withDifferentNumberOfGrandChildren_sameType_false() {
        DagNode node1 = new DagNode();
        node1.setType(Type.PAIR);
        DagNode node2 = new DagNode();
        node2.setType(Type.PAIR);

        List<DagNode> nodes = new ArrayList<>();
        DagNode child1 = new DagNode();
        child1.setType(Type.IMP);
        DagNode child2 = new DagNode();
        child2.setType(Type.CON);
        DagNode child3 = new DagNode();
        child3.setType(Type.VAR);
        nodes.add(child1);
        nodes.add(child2);
        nodes.add(child3);

        node1.setChildren(List.copyOf(nodes));

        DagNode child1n = new DagNode();
        child1n.setType(Type.IMP);
        child1n.addChild(child3);
        nodes.remove(child1);
        nodes.add(child1n);
        node2.setChildren(nodes);

        assertNotEquals(node1, node2);
    }

    @Test
    public void equals_withDifferentGrandChildren_sameType_false() {
        DagNode node1 = new DagNode();
        node1.setType(Type.PAIR);
        DagNode node2 = new DagNode();
        node2.setType(Type.PAIR);

        List<DagNode> nodes = new ArrayList<>();
        DagNode child1 = new DagNode();
        child1.setType(Type.IMP);
        DagNode child2 = new DagNode();
        child2.setType(Type.CON);
        DagNode child3 = new DagNode();
        child3.setType(Type.VAR);
        child1.addChild(child2);
        nodes.add(child1);
        nodes.add(child2);
        nodes.add(child3);

        node1.setChildren(List.copyOf(nodes));

        DagNode child1n = new DagNode();
        child1n.setType(Type.IMP);
        child1n.addChild(child3);
        nodes.remove(child1);
        nodes.add(child1n);
        node2.setChildren(nodes);

        assertNotEquals(node1, node2);
    }

    @Test
    public void equals_withSameGrandChildren_sameType_true() {
        DagNode node1 = new DagNode();
        node1.setType(Type.ROOT);
        DagNode node2 = new DagNode();
        node2.setType(Type.ROOT);

        List<DagNode> nodes1 = new ArrayList<>();
        DagNode child1 = new DagNode();
        child1.setType(Type.IMP);
        DagNode child2 = new DagNode();
        child2.setType(Type.CON);
        DagNode child3 = new DagNode();
        child3.setType(Type.VAR);
        child1.addChild(child3);
        nodes1.add(child1);
        nodes1.add(child2);
        nodes1.add(child3);

        node1.setChildren(nodes1);

        List<DagNode> nodes2 = new ArrayList<>();
        DagNode child1n = new DagNode();
        child1n.setType(Type.IMP);
        child1n.addChild(child3);
        nodes2.add(child2);
        nodes2.add(child3);
        nodes2.add(child1n);
        node2.setChildren(nodes2);

        assertEquals(node1, node2);
    }

    @Test
    public void equals_PairsWithSameChildren_differentOrder_false() {
        DagNode node1 = new DagNode();
        node1.setType(Type.PAIR);
        DagNode node2 = new DagNode();
        node2.setType(Type.PAIR);

        DagNode child1 = new DagNode();
        child1.setType(Type.IMP);
        DagNode child2 = new DagNode();
        child2.setType(Type.CON);

        node1.addChild(child2);
        node1.addChild(child1);

        node2.addChild(child1);
        node2.addChild(child2);

        assertNotEquals(node1, node2);
    }

}

