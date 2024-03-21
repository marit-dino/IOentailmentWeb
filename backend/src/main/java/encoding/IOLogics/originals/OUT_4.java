package encoding.IOLogics.originals;

import encoding.IOLogics.causal.OUT_4_C;
import util.DagNode;

import java.util.List;

public class OUT_4 extends OUT {
    public OUT_4(DagNode goalPair, List<DagNode> derivingPairs) {
        super(new OUT_4_C(goalPair, derivingPairs));
    }
}
