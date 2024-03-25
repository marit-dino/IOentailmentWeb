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
import restApplication.exceptions.ValidationException;
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
    public static boolean solveProblem(ProblemInput problemInput) throws ValidationException{
        EntailmentProblem p = null;
        p = getInput(problemInput);
        return p.entails();
    }


    /**
     * Takes the input from the user and returns the corresponding entailment problem.
     *
     * @param p input that is transformed into an instance of the entailment problem
     * @return instance of the entailment problem with the deriving pairs, goal pair and the I/O Logic
     */
    private static EntailmentProblem getInput(ProblemInput p) throws ValidationException {
        List<String[]> errors = new ArrayList<>();


        DagNode goalPair = null;
        try {
            goalPair = getGoalPairFromInput(p);
        } catch (GoalPairParseException e) {
            errors.add(new String[]{e.getClass().getSimpleName(), e.getMessage()});
        }

        List<DagNode> derivingPairs = null;
        try {
            derivingPairs = getDerivingPairsFromInput(p);
        } catch (DerivingPairsParseException e) {
            errors.add(new String[]{e.getClass().getSimpleName(), e.getMessage()});
        }

        if(p.getType() == null) {
            errors.add(new String[]{IllegalLogicException.class.getSimpleName(), "Field is null."});
        }

        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        switch (p.getType().strip()) {
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
            default -> {
                errors.add(new String[]{IllegalLogicException.class.getSimpleName(), "Unexpected Logic encountered"});
                throw new ValidationException(errors);
            }
        }
    }

    private static DagNode getGoalPairFromInput(ProblemInput p) throws GoalPairParseException{
        if(p.getGoalPair() == null) {
            throw new GoalPairParseException("Field is null");
        }
        InputStream stream = new ByteArrayInputStream(p.getGoalPair().getBytes(StandardCharsets.UTF_8));
        try {
            return getGoalPair(new PairParser(stream));
        }
        catch (ParseException | TokenMgrError e) {
            throw new GoalPairParseException(e.getMessage());
        }
    }

    private static List<DagNode> getDerivingPairsFromInput(ProblemInput p) throws DerivingPairsParseException {
        if(p.getDerivingPairs() == null) {
            throw new DerivingPairsParseException("Field is null.");
        }
        InputStream stream = new ByteArrayInputStream(p.getDerivingPairs().getBytes(StandardCharsets.UTF_8));
        List<DagNode> derivingPairs;
        try {
            return getDerivingPairs(new PairParser(stream));
        }
        catch (ParseException | TokenMgrError e){
            throw new DerivingPairsParseException(e.getMessage());
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