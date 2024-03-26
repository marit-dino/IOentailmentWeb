package encoding.IOLogics.causal;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.DagNode;

import java.util.List;

public abstract class OUT_3_4_C extends OUT_C {
    private static final Logger logger = LogManager.getLogger();

    public OUT_3_4_C(DagNode goalPair, List<DagNode> derivingPairs, int worlds) {
        super(goalPair, derivingPairs, worlds);
    }

    @Override
    protected BoolExpr consequent(DagNode output, Context ctx) {
        logger.trace("consequent({}, {})", output, ctx);

        BoolExpr[] copies = new BoolExpr[getWorlds() + 1];
        for (int l = 0; l < copies.length; l++) {
            copies[l] = formulaInWorld(ctx, output, l);
        }
        return ctx.mkAnd(copies);
    }
}
