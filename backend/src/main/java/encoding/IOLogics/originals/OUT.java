package encoding.IOLogics.originals;

import com.microsoft.z3.*;
import encoding.EntailmentProblem;
import encoding.IOLogics.causal.OUT_C;
import util.DagLeafNode;
import util.DagNode;

import java.util.List;

/**
 * Centralizes the common ground between the different original I/O Logics.
 */
public abstract class OUT implements EntailmentProblem {
    private OUT_C causalCounterpart;

    public OUT(OUT_C causalCounterpart) {
        this.causalCounterpart = causalCounterpart;
    }

    public boolean entails() {
        try (Context ctx = new Context();) {
            Solver s = ctx.mkSolver();
            BoolExpr causalFormula = causalCounterpart.completeFormula(ctx);
            BoolExpr conj = conjunction(ctx);
            BoolExpr completeFormula = ctx.mkOr(causalFormula, conj);
            s.add(new BoolExpr[]{completeFormula});
            Status res = s.check();
            if(res.equals(Status.UNSATISFIABLE)) return true;
        }
        return false;
    }

    /**
     * Returns the conjunction of the negated output of the goal pair and the outputs from the deriving pairs.
     *
     * @param ctx context
     * @return conjunction
     */
    private BoolExpr conjunction(Context ctx){
        BoolExpr tmp1 = ctx.mkNot(formula(ctx, causalCounterpart.getGoalPair().getChild(1)));
        List<DagNode> pairs = causalCounterpart.getDerivingPairs();
        BoolExpr[] outputs = new BoolExpr[pairs.size()];
        for (int i = 0; i < pairs.size(); i++) {
            outputs[i] = formula(ctx, pairs.get(i).getChild(1));
        }
        BoolExpr tmp2 = ctx.mkAnd(outputs);
        return ctx.mkAnd(tmp1, tmp2);
    }

    /**
     * Returns the formula represented by the dag node.
     *
     * @param ctx context
     * @param root node of the formula
     * @return formula of node
     */
    private BoolExpr formula(Context ctx, DagNode root){
        switch (root.getType()) {
            case EQ -> {
                return ctx.mkIff(formula(ctx, root.getChild(0)), formula(ctx, root.getChild(1)));
            }
            case CON -> {
                return ctx.mkAnd(formula(ctx, root.getChild(0)), formula(ctx, root.getChild(1)));
            }
            case DIS -> {
                return ctx.mkOr(formula(ctx, root.getChild(0)), formula(ctx, root.getChild(1)));
            }
            case IMP -> {
                return ctx.mkImplies(formula(ctx, root.getChild(0)), formula(ctx, root.getChild(1)));
            }
            case NEG -> {
                return ctx.mkNot(formula(ctx, root.getChild(0)));
            }
            case VAR -> {
                return ctx.mkBoolConst(((DagLeafNode) root).getVar());
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
}
