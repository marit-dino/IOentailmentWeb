package encoding.IOLogics.causal;

import com.microsoft.z3.*;
import encoding.EntailmentProblem;
import util.DagLeafNode;
import util.DagNode;

import java.util.List;

/**
 * Centralizes the common ground between the different causal I/O Logics.
 */
public abstract class OUT_C implements EntailmentProblem {
    private DagNode goalPair;
    private List<DagNode> derivingPairs;
    private int worlds;

    public int getWorlds() {
        return worlds;
    }

    public OUT_C(DagNode goalPair, List<DagNode> derivingPairs, int worlds) {
        this.goalPair = goalPair;
        this.derivingPairs = derivingPairs;
        this.worlds = worlds;
    }


    public boolean entails() {
        try (Context ctx = new Context()) {
            Solver s = ctx.mkSolver();
            BoolExpr formula = completeFormula(ctx);
            s.add(new BoolExpr[]{formula});
            Status res = s.check();
            if(res.equals(Status.UNSATISFIABLE)) return true;
        }
        return false;
    }

    /**
     * Returns a classical propositional formula which is unsatisfiable iff the deriving pairs entail the goal pair.
     *
     * @param ctx context
     * @return classical propositional formula
     */
    public BoolExpr completeFormula(Context ctx){
            BoolExpr tmp1 = ctx.mkNot(pairFormula(goalPair, ctx));
            BoolExpr[] pairs = new BoolExpr[derivingPairs.size()];
            for (int i = 0; i < derivingPairs.size(); i++) {
                pairs[i] = pairFormula(derivingPairs.get(i), ctx);
            }
            BoolExpr tmp2 = ctx.mkAnd(pairs);
            return ctx.mkAnd(tmp1, tmp2);
    }

    /**
     * Encodes the given pair into a classical propositional formula.
     *
     * @param pair of input and output
     * @param ctx context
     * @return classical propositional formula of the pair
     */
    private BoolExpr pairFormula(DagNode pair, Context ctx) {
        return ctx.mkImplies(antecedent(pair.getChild(0), ctx), consequent(pair.getChild(1), ctx));
    }

    /**
     * Returns the antecedent for the pair formula given the pair's input.
     *
     * @param input of a pair
     * @param ctx context
     * @return classical propositional formula
     */
    private BoolExpr antecedent(DagNode input, Context ctx) {
        BoolExpr[] copies = new BoolExpr[getWorlds()];
        for (int l = 1; l <= copies.length; l++) {
            copies[l-1] = formulaInWorld(ctx, input, l);
        }
        return ctx.mkAnd(copies);
    }

    /**
     * Returns the consequent for the pair formula given the pair's output.
     *
     * @param output of a pair
     * @param ctx context
     * @return classical propositional formula
     */
    protected abstract BoolExpr consequent(DagNode output, Context ctx);

    /**
     * Returns the formula represented by a dag node in the given world l, where all instances of variables are replaced
     * by a labeled version according to the world.
     *
     * @param ctx context
     * @param root node of the formula
     * @param l world
     * @return classical propositional formula
     */
    protected BoolExpr formulaInWorld(Context ctx, DagNode root, int l){
        switch (root.getType()) {
            case EQ -> {
                return ctx.mkIff(formulaInWorld(ctx, root.getChild(0), l), formulaInWorld(ctx, root.getChild(1), l));
            }
            case CON -> {
                return ctx.mkAnd(formulaInWorld(ctx, root.getChild(0), l), formulaInWorld(ctx, root.getChild(1), l));
            }
            case DIS -> {
                return ctx.mkOr(formulaInWorld(ctx, root.getChild(0), l), formulaInWorld(ctx, root.getChild(1), l));
            }
            case IMP -> {
                return ctx.mkImplies(formulaInWorld(ctx, root.getChild(0), l), formulaInWorld(ctx, root.getChild(1), l));
            }
            case NEG -> {
                return ctx.mkNot(formulaInWorld(ctx, root.getChild(0), l));
            }
            case VAR -> {
                return ctx.mkBoolConst(((DagLeafNode) root).getVarInWorld(l));
            }
            case TRUE -> {
                return ctx.mkTrue();
            }
            case FALSE -> {
                return ctx.mkFalse();
            }
            default -> throw new RuntimeException("Error while encoding formula in propositional logic, encountered unexpected node type " + root.getType());
        }
    }

    /**
     * Gets the goal pair.
     *
     * @return goal pair
     */
    public DagNode getGoalPair() {
        return goalPair;
    }

    /**
     * Gets the deriving pairs
     *
     * @return deriving pairs
     */
    public List<DagNode> getDerivingPairs() {
        return derivingPairs;
    }
}
