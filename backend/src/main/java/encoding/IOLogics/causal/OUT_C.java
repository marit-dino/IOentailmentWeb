package encoding.IOLogics.causal;

import com.microsoft.z3.*;
import encoding.EntailmentProblem;
import encoding.IOLogics.CounterModel;
import encoding.IOLogics.CounterModelWorlds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.DagLeafNode;
import util.DagNode;

import java.util.*;

/**
 * Centralizes the common ground between the different causal I/O Logics.
 */
public abstract class OUT_C implements EntailmentProblem {
    private static final Logger logger = LogManager.getLogger();

    private DagNode goalPair;
    private List<DagNode> derivingPairs;
    private int worlds;
    private List<BoolExpr> exprs;
    private Model counterModel;
    private Context ctx;

    public int getWorlds() {
        return worlds;
    }

    public OUT_C(DagNode goalPair, List<DagNode> derivingPairs, int worlds) {
        this.exprs = new ArrayList<>();
        this.goalPair = goalPair;
        this.derivingPairs = derivingPairs;
        this.worlds = worlds;
    }


    public boolean entails() {
        logger.trace("entails()");

        ctx = getContext();
        Solver s = ctx.mkSolver();
        BoolExpr formula = completeFormula(ctx);
        s.add(new BoolExpr[]{formula});
        Status res = s.check();
        if (res.equals(Status.UNSATISFIABLE)) {
            ctx.close();
            return true;
        }
        this.counterModel = s.getModel();
        return false;
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

    public CounterModel getCounterModel(){
        logger.trace("getCounterModel()");

        if(counterModel == null) return null;
        CounterModelWorlds model = new CounterModelWorlds();

        for (BoolExpr expr : this.exprs) {
            String varInWorld = expr.toString();
            String world = getWorldOfVar(varInWorld);
            String var = getOriginalVarName(varInWorld);

            boolean value = this.counterModel.eval(expr, false).isTrue();
            addToModel(model, var, world, value);
        }
        ctx.close();
        return model;
    }

    private void addToModel(CounterModelWorlds model, String var, String world, boolean value) {
        if(world.equals("w0")) model.addToOut(var, world, value);
        else {
            model.addToIn(var, world, value);
        }
    }

    private String getOriginalVarName(String str){
        return str.substring(1, str.indexOf("["));
    }

    private String getWorldOfVar(String str){
        return str.substring(str.indexOf("[") + 1, str.indexOf("]"));
    }


    /**
     * Returns a classical propositional formula which is unsatisfiable iff the deriving pairs entail the goal pair.
     *
     * @param ctx context
     * @return classical propositional formula
     */
    public BoolExpr completeFormula(Context ctx){
        logger.trace("completeFormula({})", ctx);

        BoolExpr tmp1 = ctx.mkNot(pairFormula(goalPair, ctx));
        BoolExpr[] pairs = new BoolExpr[derivingPairs.size()];
        for (int i = 0; i < derivingPairs.size(); i++) {
            pairs[i] = pairFormula(derivingPairs.get(i), ctx);
        }
        BoolExpr tmp2 = ctx.mkAnd(pairs);
        BoolExpr complete =  ctx.mkAnd(tmp1, tmp2);
        logger.debug("complete Formula: {}", complete);
        return complete;
    }

    /**
     * Encodes the given pair into a classical propositional formula.
     *
     * @param pair of input and output
     * @param ctx context
     * @return classical propositional formula of the pair
     */
    private BoolExpr pairFormula(DagNode pair, Context ctx) {
        logger.trace("pairFormula({}, {})", pair, ctx);
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
        logger.trace("antecedent({}, {})", input, ctx);

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
        logger.trace("formulaInWorld({}, {}, {})", ctx, root, l);

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
                BoolExpr tmp = ctx.mkBoolConst(((DagLeafNode) root).getVarInWorld(l));
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
     * Gets the goal pair.
     *
     * @return goal pair
     */
    public DagNode getGoalPair() {
        logger.trace("getGoalPair()");
        return goalPair;
    }

    /**
     * Gets the deriving pairs
     *
     * @return deriving pairs
     */
    public List<DagNode> getDerivingPairs() {
        logger.trace("getDerivingPairs()");
        return derivingPairs;
    }

    /**
     * Adds expression to expression list
     * @param expr to add
     */
    private void addExpr(BoolExpr expr){
        logger.trace("addExpr({})", expr);
        if(!exprs.contains(expr)) this.exprs.add(expr);
    }


}
