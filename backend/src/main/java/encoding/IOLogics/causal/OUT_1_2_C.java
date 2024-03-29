package encoding.IOLogics.causal;

import encoding.IOLogics.CounterModelWorlds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.DagNode;
import com.microsoft.z3.*;

import java.util.List;

public abstract class OUT_1_2_C extends OUT_C {
    private static final Logger logger = LogManager.getLogger();

    public OUT_1_2_C(DagNode goalPair, List<DagNode> derivingPairs, int worlds) {
        super(goalPair, derivingPairs, worlds);
    }

    @Override
    protected BoolExpr consequent(DagNode output, Context ctx) {
        logger.trace("consequent({}, {})", output, ctx);
        return formulaInWorld(ctx, output, 0);
    }


}
