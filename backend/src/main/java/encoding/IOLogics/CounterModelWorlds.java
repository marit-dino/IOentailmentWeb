package encoding.IOLogics;

import java.util.HashMap;
import java.util.Map;

public class CounterModelWorlds implements CounterModel {
    private Map<String, Map<Integer, Boolean>> in;
    private Map<String, Map<Integer, Boolean>> out;

    public CounterModelWorlds() {
        this.in = new HashMap<>();
        this.out = new HashMap<>();
    }

    public void addToIn(String var, int world, boolean value){
        if(!in.containsKey(var)){
            in.put(var, new HashMap<>());
        }
        in.get(var).put(world, value);
    }
    public void addToOut(String var, int world, boolean value){
        if(!out.containsKey(var)){
            out.put(var, new HashMap<>());
        }
        out.get(var).put(world, value);
    }

    public Map<String, Map<Integer, Boolean>> getIn() {
        return in;
    }

    public void setIn(Map<String, Map<Integer, Boolean>> in) {
        this.in = in;
    }

    public Map<String, Map<Integer, Boolean>> getOut() {
        return out;
    }

    public void setOut(Map<String, Map<Integer, Boolean>> out) {
        this.out = out;
    }
}
