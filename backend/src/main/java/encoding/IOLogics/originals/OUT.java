package encoding.IOLogics.originals;

import com.microsoft.z3.*;
import encoding.EntailmentProblem;
import encoding.IOLogics.CounterModelClassical;
import encoding.IOLogics.CounterModel;
import encoding.IOLogics.causal.OUT_C;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.DagLeafNode;
import util.DagNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Centralizes the common ground between the different original I/O Logics.
 */
public abstract class OUT implements EntailmentProblem {
    private static final Logger logger = LogManager.getLogger();

    private OUT_C causalCounterpart;
    private Model counterModel;
    private List<BoolExpr> exprs;
    private Context ctx;



    public OUT(OUT_C causalCounterpart) {
        this.exprs = new ArrayList<>();
        this.causalCounterpart = causalCounterpart;
    }

    public boolean entails() {
        logger.trace("entails()");

        boolean causalEntails = causalCounterpart.entails();

        if(!causalEntails) return false;

        ctx = getContext();
        BoolExpr classicalEnt = conjunction(ctx);

        Solver s = ctx.mkSolver();
        Status res = s.check(classicalEnt);
        if (res.equals(Status.UNSATISFIABLE)) {
            ctx.close();
            return true;
        }
        this.counterModel = s.getModel();
        return false;
    }



    @Override
    public CounterModel getCounterModel() {
        logger.trace("getCounterModel()");
        if(this.counterModel == null) return this.causalCounterpart.getCounterModel();

        CounterModelClassical model = new CounterModelClassical();

        for (BoolExpr expr : this.exprs) {
            String var = expr.toString();
            boolean value = this.counterModel.eval(expr, false).isTrue();
            model.add(var, value);
        }
        ctx.close();
        return model;
    }

    /**
     * Returns the conjunction of the negated output of the goal pair and the outputs from the deriving pairs.
     *
     * @param ctx context
     * @return conjunction
     */
    private BoolExpr conjunction(Context ctx){
        logger.trace("conjunction({})", ctx);

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
        logger.trace("formula({}, {})", ctx, root);

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
                BoolExpr tmp =  ctx.mkBoolConst(((DagLeafNode) root).getVar());
                addExpr(tmp);
                return tmp;
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
     * Adds expression to expression list
     * @param expr to add
     */
    private void addExpr(BoolExpr expr){
        logger.trace("addExpr({})", expr);
        if(!exprs.contains(expr)) this.exprs.add(expr);
    }

    /**
     * Singleton getter for context
     * @return context
     */
    public Context getContext() {
        logger.trace("getContext()");
        if(ctx == null) ctx = new Context();
        return this.ctx;
    }
}
