package restApplication;

import encoding.IOLogics.causal.OUT_1_C;
import encoding.IOLogics.causal.OUT_2_C;
import encoding.IOLogics.causal.OUT_3_C;
import encoding.IOLogics.causal.OUT_4_C;
import encoding.IOLogics.originals.OUT_1;
import encoding.IOLogics.originals.OUT_2;
import encoding.IOLogics.originals.OUT_3;
import encoding.IOLogics.originals.OUT_4;
import encoding.EntailmentProblem;
import parser.ParseException;
import parser.TokenMgrError;
import parser.NodeVisitor;
import restApplication.exceptions.DerivingPairsParseException;
import restApplication.exceptions.GoalPairParseException;
import restApplication.exceptions.IllegalLogicException;
import util.DagNode;
import parser.PairParser;
import parser.SimpleNode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProblemSolver {

    /**
     * Solves the entailment problem for the problem given.
     *
     * @param problemInput to be solved
     * @return whether deriving pairs entail goal pair or not //TODO exceptions
     */
    public static boolean solveProblem(ProblemInput problemInput) throws GoalPairParseException, DerivingPairsParseException, IllegalLogicException {
        if(problemInput.isFieldNull())  throw new IllegalArgumentException("A field is undefined");
        EntailmentProblem p = getInput(problemInput);
        return p.entails();
    }


    /**
     * Takes the input from the user and returns the corresponding entailment problem.
     *
     * @param p input that is transformed into an instance of the entailment problem
     * @return instance of the entailment problem with the deriving pairs, goal pair and the I/O Logic
     */
    private static EntailmentProblem getInput(ProblemInput p) throws GoalPairParseException, DerivingPairsParseException, IllegalLogicException {
        InputStream stream = new ByteArrayInputStream(p.getDerivingPairs().getBytes(StandardCharsets.UTF_8));
        List<DagNode> derivingPairs;
        try {
            derivingPairs = getDerivingPairs(new PairParser(stream));
        }
        catch (ParseException e){
            throw new DerivingPairsParseException(e.getMessage());
        }

        stream = new ByteArrayInputStream(p.getGoalPair().getBytes(StandardCharsets.UTF_8));
        DagNode goalPair;
        try {
            goalPair = getGoalPair(new PairParser(stream));
        }
        catch (ParseException e){
            throw new GoalPairParseException(e.getMessage());
        }

        switch (p.getType()) {
            case "OUT1C" -> {
                return new OUT_1_C(goalPair, derivingPairs);
            }
            case "OUT2C" -> {
                return new OUT_2_C(goalPair, derivingPairs);
            }
            case "OUT3C" -> {
                return new OUT_3_C(goalPair, derivingPairs);
            }
            case "OUT4C" -> {
                return new OUT_4_C(goalPair, derivingPairs);
            }
            case "OUT1" -> {
                return new OUT_1(goalPair, derivingPairs);
            }
            case "OUT2" -> {
                return new OUT_2(goalPair, derivingPairs);
            }
            case "OUT3" -> {
                return new OUT_3(goalPair, derivingPairs);
            }
            case "OUT4" -> {
                return new OUT_4(goalPair, derivingPairs);
            }
            default -> throw new IllegalLogicException("Unexpected I/O Logic encountered.");
        }
    }


    /**
     * Parses the user input for the goal pair according to the specified EBNF grammar of the parser and returns a dag of this input.
     *
     * @param parser for the goal- and deriving pairs
     * @return root of the dag
     * @throws ParseException when parsing fails
     * @throws TokenMgrError when an unexpected token is encountered
     */
    private static DagNode getGoalPair(PairParser parser) throws ParseException, TokenMgrError {
        DagNode root = new DagNode();
        NodeVisitor visitor = new NodeVisitor();
        SimpleNode start = parser.GetGoalPair();
        root = (DagNode) start.jjtAccept(visitor, null);
        return root.getChild(0);
    }

    /**
     * Parses the user input for the deriving pairs according to the specified EBNF grammar of the parser and returns a dag of this input.
     *
     * @param parser for the goal- and deriving pairs
     * @return root of the dag
     * @throws ParseException when parsing fails
     * @throws TokenMgrError when an unexpected token is encountered
     */
    private static List<DagNode> getDerivingPairs(PairParser parser) throws ParseException, TokenMgrError {
        DagNode root = new DagNode();
        NodeVisitor visitor = new NodeVisitor();
        SimpleNode start = parser.GetDerivingPairs();
        root = (DagNode) start.jjtAccept(visitor, null);
        List<DagNode> pairs = new ArrayList<>();

        for (int i = 0; i < root.numberOfChildren(); i++) {
            pairs.add(root.getChild(i));
        }
        return pairs;
    }
}