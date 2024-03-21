package encoding.IOLogics.causal;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import util.DagNode;

import java.util.List;

public abstract class OUT_3_4_C extends OUT_C {
    public OUT_3_4_C(DagNode goalPair, List<DagNode> derivingPairs, int worlds) {
        super(goalPair, derivingPairs, worlds);
    }

    @Override
    protected BoolExpr consequent(DagNode output, Context ctx) {
        BoolExpr[] copies = new BoolExpr[getWorlds() + 1];
        for (int l = 0; l < copies.length; l++) {
            copies[l] = formulaInWorld(ctx, output, l);
        }
        return ctx.mkAnd(copies);
    }
}
