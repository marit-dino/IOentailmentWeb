package encoding.IOLogics;

import parser.NodeVisitor;
import parser.PairParser;
import parser.ParseException;
import parser.SimpleNode;
import util.DagNode;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PairPreparer {
    public static List<DagNode> prepareDerivingPairs(String str) throws ParseException {
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetDerivingPairs();

        NodeVisitor visitor = new NodeVisitor();
        node.jjtAccept(visitor, null);

        DagNode root = (DagNode) visitor.visit(node, null);
        List<DagNode> pairs = new ArrayList<>();

        for (int i = 0; i < root.numberOfChildren(); i++) {
            pairs.add(root.getChild(i));
        }
        return pairs;
    }

    public static DagNode prepareGoalPair(String str) throws ParseException {
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetGoalPair();

        NodeVisitor visitor = new NodeVisitor();
        node.jjtAccept(visitor, null);

        DagNode root = (DagNode) visitor.visit(node, null);
        return root.getChild(0);
    }
}
