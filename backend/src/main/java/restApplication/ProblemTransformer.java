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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class ProblemTransformer {
    private static final Logger logger = LogManager.getLogger();


    /**
     * Takes the input from the user and returns the corresponding entailment problem.
     *
     * @param p input that is transformed into an instance of the entailment problem
     * @throws ValidationException if parsing fails or a field is null
     * @return instance of the entailment problem with the deriving pairs, goal pair and the I/O Logic
     */
    public static EntailmentProblem getInput(ProblemInput p) throws ValidationException {
        logger.trace("getInput({})", p);
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
            logger.warn("Logic is null {}", p.getType());
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
                logger.warn("Unexpected logic {} encountered", p.getType());
                errors.add(new String[]{IllegalLogicException.class.getSimpleName(), "Unexpected Logic encountered"});
                throw new ValidationException(errors);
            }
        }
    }

    /**
     * Returns the root of the goal pair from the input of the user.
     *
     * @param p ProblemInpuut
     * @return root node
     * @throws GoalPairParseException if parsing fails.
     */
    private static DagNode getGoalPairFromInput(ProblemInput p) throws GoalPairParseException{
        logger.trace("getGoalPairFromInput({})", p);

        if(p.getGoalPair() == null) {
            logger.warn("Goal pair is null");
            throw new GoalPairParseException("Field is null");
        }
        InputStream stream = new ByteArrayInputStream(p.getGoalPair().getBytes(StandardCharsets.UTF_8));
        try {
            return getGoalPair(new PairParser(stream));
        }
        catch (ParseException | TokenMgrError e) {
            logger.info("Parsing goal pair failed for {}", p.getGoalPair());
            throw new GoalPairParseException(e.getMessage());
        }
    }

    /**
     * Returns the list of deriving pairs from the user input.
     *
     * @param p ProblemInput
     * @return List of pairs
     * @throws DerivingPairsParseException if parsing fails
     */
    private static List<DagNode> getDerivingPairsFromInput(ProblemInput p) throws DerivingPairsParseException {
        logger.trace("getDerivingPairsFromInput({})", p);

        if(p.getDerivingPairs() == null) {
            logger.warn("Deriving pairs are null");
            throw new DerivingPairsParseException("Field is null.");
        }
        InputStream stream = new ByteArrayInputStream(p.getDerivingPairs().getBytes(StandardCharsets.UTF_8));
        List<DagNode> derivingPairs;
        try {
            return getDerivingPairs(new PairParser(stream));
        }
        catch (ParseException | TokenMgrError e){
            logger.info("Parsing deriving pairs failed for {}", p.getDerivingPairs());
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
        logger.trace("getGoalPair({})", parser);

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
        logger.trace("getDerivingPairs({})", parser);

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