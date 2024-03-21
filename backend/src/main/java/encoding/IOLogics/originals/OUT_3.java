package encoding.IOLogics.originals;

import encoding.IOLogics.causal.OUT_3_C;
import util.DagNode;

import java.util.List;

public class OUT_3 extends OUT {
    public OUT_3(DagNode goalPair, List<DagNode> derivingPairs) {
        super(new OUT_3_C(goalPair, derivingPairs));
    }
}
