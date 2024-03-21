package encoding.IOLogics.causal;

import util.DagNode;
import com.microsoft.z3.*;

import java.util.List;

public abstract class OUT_1_2_C extends OUT_C {
    public OUT_1_2_C(DagNode goalPair, List<DagNode> derivingPairs, int worlds) {
        super(goalPair, derivingPairs, worlds);
    }

    @Override
    protected BoolExpr consequent(DagNode output, Context ctx) {
        return formulaInWorld(ctx, output, 0);
    }
}
