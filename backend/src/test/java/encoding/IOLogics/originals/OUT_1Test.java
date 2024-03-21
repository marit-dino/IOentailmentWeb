package encoding.IOLogics.originals;

import encoding.EntailmentProblem;
import encoding.IOLogics.causal.OUT_1_C;
import encoding.IOLogics.causal.OUT_3_C;
import org.junit.Test;
import parser.ParseException;
import util.DagNode;

import java.util.List;

import static encoding.IOLogics.PairPreparer.prepareDerivingPairs;
import static encoding.IOLogics.PairPreparer.prepareGoalPair;
import static org.junit.Assert.*;

public class OUT_1Test {

    @Test
    public void entails_TOP_fromNoDerivingPairs() throws ParseException {
        DagNode goalPair = prepareGoalPair("(T, T)");
        List<DagNode> derivingPairs = prepareDerivingPairs("");
        EntailmentProblem ent = new OUT_1(goalPair, derivingPairs);

        assertTrue(ent.entails());
    }

    @Test
    public void NotEntails_BOT_fromNoDerivingPairs() throws ParseException {
        DagNode goalPair = prepareGoalPair("(F, F)");
        List<DagNode> derivingPairs = prepareDerivingPairs("");
        EntailmentProblem ent = new OUT_1(goalPair, derivingPairs);

        assertFalse(ent.entails());
    }

    @Test
    public void entails_WOT() throws ParseException {
        DagNode goalPair = prepareGoalPair("(A, Y)");
        List<DagNode> derivingPairs = prepareDerivingPairs("(A, (X&(X -> Y))) ");
        EntailmentProblem ent = new OUT_1(goalPair, derivingPairs);

        assertTrue(ent.entails());
    }

    @Test
    public void entails_SI() throws ParseException {
        List<DagNode> derivingPairs = prepareDerivingPairs("(A, X)");
        DagNode goalPair = prepareGoalPair("((A & B), X)");
        EntailmentProblem ent = new OUT_1(goalPair, derivingPairs);

        assertTrue(ent.entails());
    }

    @Test
    public void entails_AND() throws ParseException {
        List<DagNode> derivingPairs = prepareDerivingPairs("(A, X1), (A, X2)");
        DagNode goalPair = prepareGoalPair("(A, (X1 & X2))");
        EntailmentProblem ent = new OUT_1(goalPair, derivingPairs);

        assertTrue(ent.entails());
    }

    @Test
    public void notEntails_OR() throws ParseException {
        List<DagNode> derivingPairs = prepareDerivingPairs("(A1, X), (A2, X)");
        DagNode goalPair = prepareGoalPair("((A1 | A2), X)");
        EntailmentProblem ent = new OUT_1(goalPair, derivingPairs);

        assertFalse(ent.entails());
    }

    @Test
    public void notEntails_CT() throws ParseException {
        List<DagNode> derivingPairs = prepareDerivingPairs("(A, X), ((A & X), Y)");
        DagNode goalPair = prepareGoalPair("(A, Y)");
        EntailmentProblem ent = new OUT_1(goalPair, derivingPairs);

        assertFalse(ent.entails());
    }

    @Test
    public void NotEntails_ghostContraposition() throws ParseException {
        List<DagNode> derivingPairs = prepareDerivingPairs("(~X, ~A), ((A&X), Y)");
        DagNode goalPair = prepareGoalPair("(A, Y)");
        EntailmentProblem ent = new OUT_1(goalPair, derivingPairs);

        assertFalse(ent.entails());
    }

    @Test
    public void NotEntails_example1() throws ParseException {
        List<DagNode> derivingPairs = prepareDerivingPairs("(A, X), ((C <-> ~D), X)");
        DagNode goalPair = prepareGoalPair("(((A & B) | (C <-> ~D)), (Z -> X))");
        //SI, OR, WO
        EntailmentProblem ent = new OUT_1(goalPair, derivingPairs);

        assertFalse(ent.entails());
    }

    @Test
    public void NotEntails_example2() throws ParseException {
        List<DagNode> derivingPairs = prepareDerivingPairs("((A & B), ((X <-> Y) & (T -> X))), (Z, Y), ((B | C), A)");
        DagNode goalPair = prepareGoalPair("((B | Z), Y)");
        //SI, OR, WO, CT
        EntailmentProblem ent = new OUT_1(goalPair, derivingPairs);

        assertFalse(ent.entails());
    }
}