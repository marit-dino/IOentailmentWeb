package parser;

import org.junit.Test;
import util.DagLeafNode;
import util.DagNode;
import util.DagValueNode;
import util.Type;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class NodeVisitorTest {
    /*
    - tree with identical subtrees -> into correct dag
    - vars transformed into leafnodes, others not
    - (IOlogic classes)
     */

    @Test
    public void createDag_withNoIdenticalSubtrees_createsCorrectDag_simple() throws ParseException {
        String onePair = "(A, B), (D, C)";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetDerivingPairs();

        NodeVisitor visitor = new NodeVisitor();
        node.jjtAccept(visitor, null);

        DagNode root = (DagNode) visitor.visit(node, null);

        assertEquals(Type.ROOT, root.getType());
        assertEquals(2, root.numberOfChildren());

        DagNode pair2 = root.getChild(0);
        DagNode pair1 = root.getChild(1);

        assertEquals(Type.PAIR, pair1.getType());
        assertEquals(Type.PAIR, pair2.getType());

        assertEquals(Type.VAR, pair1.getChild(0).getType());
        assertEquals(Type.VAR, pair1.getChild(1).getType());
        assertTrue(pair1.getChild(0) instanceof DagLeafNode);
        assertTrue(pair1.getChild(1) instanceof DagLeafNode);
        assertEquals("A", ((DagLeafNode) pair1.getChild(0)).getVar());
        assertEquals("B",  ((DagLeafNode) pair1.getChild(1)).getVar());

        assertEquals(Type.VAR, pair2.getChild(0).getType());
        assertEquals(Type.VAR, pair2.getChild(1).getType());
        assertTrue(pair2.getChild(0) instanceof DagLeafNode);
        assertTrue(pair2.getChild(1) instanceof DagLeafNode);
        assertEquals("D", ((DagLeafNode) pair2.getChild(0)).getVar());
        assertEquals("C",  ((DagLeafNode) pair2.getChild(1)).getVar());
    }

    @Test
    public void createDag_withNoIdenticalSubtrees_createsCorrectDag_complex() throws ParseException {
        String onePair = "((A & (B | C)), ((~F -> E) <-> (T & G)))";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetGoalPair();

        NodeVisitor visitor = new NodeVisitor();
        node.jjtAccept(visitor, null);

        DagNode root = (DagNode) visitor.visit(node, null);

        assertEquals(Type.ROOT, root.getType());
        assertEquals(1, root.numberOfChildren());

        DagNode pair = root.getChild(0);
        assertEquals(Type.PAIR, pair.getType());

        //(A & (B | C))
        DagNode in = pair.getChild(0);
        DagNode out = pair.getChild(1);

        assertEquals(Type.CON, in.getType());
        assertEquals(Type.EQ, out.getType());

        //A
        DagNode in1 = in.getChild(1);
        //(B | C)
        DagNode in2 = in.getChild(0);
        assertTrue(in1 instanceof DagLeafNode);
        assertEquals("A", ((DagLeafNode) in1).getVar());
        assertEquals(Type.DIS, in2.getType());

        DagNode in21 = in2.getChild(1);
        DagNode in22 = in2.getChild(0);
        assertTrue(in21 instanceof DagLeafNode);
        assertTrue(in22 instanceof DagLeafNode);
        assertEquals("B", ((DagLeafNode) in21).getVar());
        assertEquals("C", ((DagLeafNode) in22).getVar());

        DagNode out1 = out.getChild(1);
        DagNode out2 = out.getChild(0);
        assertEquals(Type.IMP, out1.getType());
        assertEquals(Type.CON, out2.getType());

        DagNode out11 = out1.getChild(0);
        DagNode out12 = out1.getChild(1);
        assertEquals(Type.NEG, out11.getType());
        assertTrue(out12 instanceof DagLeafNode);
        assertEquals("E", ((DagLeafNode) out12).getVar());

        DagNode out111 = out11.getChild(0);
        assertTrue(out111 instanceof DagValueNode);
        assertEquals(Type.FALSE, out111.getType());

        DagNode out21 = out2.getChild(1);
        DagNode out22 = out2.getChild(0);
        assertTrue(out21 instanceof DagValueNode);
        assertTrue(out22 instanceof DagLeafNode);
        assertEquals(Type.TRUE, out21.getType());
        assertEquals("G", ((DagLeafNode) out22).getVar());
    }

    @Test
    public void createDag_mergesIdenticalSubtrees_simple1() throws ParseException {
        String onePair = "(A, A)";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetDerivingPairs();

        NodeVisitor visitor = new NodeVisitor();
        node.jjtAccept(visitor, null);

        DagNode root = (DagNode) visitor.visit(node, null);

        DagNode pair = root.getChild(0);

        assertEquals(System.identityHashCode(pair.getChild(0)), System.identityHashCode(pair.getChild(1)));
    }

    @Test
    public void createDag_mergesIdenticalSubtrees_simple2() throws ParseException {
        String onePair = "((A & B), (B & A))";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetDerivingPairs();

        NodeVisitor visitor = new NodeVisitor();
        node.jjtAccept(visitor, null);

        DagNode root = (DagNode) visitor.visit(node, null);
        DagNode pair = root.getChild(0);
        DagNode in = pair.getChild(0);
        DagNode out = pair.getChild(1);
        DagNode outA = out.getChild(0);
        DagNode outB = out.getChild(1);
        DagNode inA = in.getChild(0);
        DagNode inB = in.getChild(1);

        assertEquals(System.identityHashCode(in), System.identityHashCode(out));
        assertEquals(System.identityHashCode(inA), System.identityHashCode(outA));
        assertEquals(System.identityHashCode(inB), System.identityHashCode(outB));
    }

    @Test
    public void createDag_mergesIdenticalSubtrees_simple3() throws ParseException {
        String onePair = "(A, B), (A, B)";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetDerivingPairs();

        NodeVisitor visitor = new NodeVisitor();
        node.jjtAccept(visitor, null);

        DagNode root = (DagNode) visitor.visit(node, null);
        DagNode pair1 = root.getChild(0);
        DagNode pair2 = root.getChild(1);

        assertEquals(System.identityHashCode(pair1), System.identityHashCode(pair2));
    }

    @Test
    public void createDag_mergesIdenticalSubtrees_complex1() throws ParseException {
        String onePair = "(((A & (B -> ~D))|(C <-> F)),((A & (C <-> F))&B))";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetGoalPair();

        NodeVisitor visitor = new NodeVisitor();
        node.jjtAccept(visitor, null);

        DagNode root = (DagNode) visitor.visit(node, null);
        DagNode pair = root.getChild(0);
        DagNode in = pair.getChild(0);
        DagNode out = pair.getChild(1);
        //A & (B -> D)
        DagNode inCon = in.getChild(0);
        DagNode inImp = inCon.getChild(0);
        DagNode inB = inImp.getChild(0);
        DagNode inA = inCon.getChild(1);
        //(C <-> F)
        DagNode inEq = in.getChild(1);
        //A & (C <-> F)
        DagNode outCon = out.getChild(0);
        DagNode outA = outCon.getChild(1);
        DagNode outEq = outCon.getChild(0);
        DagNode outB = out.getChild(1);

        assertEquals(System.identityHashCode(inB), System.identityHashCode(outB));
        assertEquals(System.identityHashCode(inA), System.identityHashCode(outA));
        assertEquals(System.identityHashCode(inEq), System.identityHashCode(outEq));
    }

    @Test
    public void createDag_mergesIdenticalSubtrees_complex2() throws ParseException {
        String onePair = "(((A & B) -> ~E), (E | (B & A)))";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetGoalPair();

        NodeVisitor visitor = new NodeVisitor();
        node.jjtAccept(visitor, null);

        DagNode root = (DagNode) visitor.visit(node, null);
        DagNode pair = root.getChild(0);
        //((A & B) -> ~E)
        DagNode in = pair.getChild(0);
        //(E | (B & A))
        DagNode out = pair.getChild(1);
        //(A & B)
        DagNode inCon = in.getChild(0);
        DagNode inA = inCon.getChild(0);
        DagNode inB = inCon.getChild(1);
        // ~E
        DagNode inNeg = in.getChild(1);
        DagNode inE = inNeg.getChild(0);
        //E
        DagNode outE = out.getChild(1);
        //(B & A)
        DagNode outCon = out.getChild(0);
        DagNode outA = outCon.getChild(0);
        DagNode outB = outCon.getChild(1);

        assertEquals(System.identityHashCode(inB), System.identityHashCode(outB));
        assertEquals(System.identityHashCode(inA), System.identityHashCode(outA));
        assertEquals(System.identityHashCode(inE), System.identityHashCode(outE));
        assertEquals(System.identityHashCode(inCon), System.identityHashCode(outCon));
    }

    @Test
    public void createDag_makesLeafNodes_fromVars_simple() throws ParseException {
        String onePair = "(A, B)";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetGoalPair();

        NodeVisitor visitor = new NodeVisitor();
        node.jjtAccept(visitor, null);

        DagNode root = (DagNode) visitor.visit(node, null);

        DagNode pair = root.getChild(0);
        assertFalse(pair instanceof DagLeafNode);
        assertTrue(pair.getChild(0) instanceof DagLeafNode);
        assertTrue(pair.getChild(1) instanceof DagLeafNode);
    }

    @Test
    public void createDag_makesLeafNodes_fromVars_complex() throws ParseException {
        String onePair = "(((A & B) -> ~E), (E | (B & A)))";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetGoalPair();

        NodeVisitor visitor = new NodeVisitor();
        node.jjtAccept(visitor, null);

        DagNode root = (DagNode) visitor.visit(node, null);
        DagNode pair = root.getChild(0);
        //((A & B) -> ~E)
        DagNode in = pair.getChild(0);
        //(E | (B & A))
        DagNode out = pair.getChild(1);
        //(A & B)
        DagNode inCon = in.getChild(0);
        DagNode inA = inCon.getChild(0);
        DagNode inB = inCon.getChild(1);
        // ~E
        DagNode inNeg = in.getChild(1);
        DagNode inE = inNeg.getChild(0);
        //E
        DagNode outE = out.getChild(1);
        //(B & A)
        DagNode outCon = out.getChild(0);
        DagNode outA = outCon.getChild(0);
        DagNode outB = outCon.getChild(1);

        assertTrue(outB instanceof DagLeafNode);
        assertTrue(outA instanceof DagLeafNode);
        assertTrue(inE instanceof DagLeafNode);
    }

}