package encoding.IOLogics;

import java.util.HashMap;
import java.util.Map;

public class CounterModelClassical implements CounterModel {
    private Map<String, Boolean> vals;

    public CounterModelClassical() {
        this.vals = new HashMap<>();
    }

    public void add(String var, boolean val){
        vals.put(var, val);
    }

    public Map<String, Boolean> getVals() {
        return vals;
    }

    public void setVals(Map<String, Boolean> vals) {
        this.vals = vals;
    }
}
