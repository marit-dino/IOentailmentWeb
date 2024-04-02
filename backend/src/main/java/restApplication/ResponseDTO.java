package restApplication;

import encoding.IOLogics.CounterModel;
import encoding.IOLogics.CounterModelClassical;
import encoding.IOLogics.CounterModelWorlds;

public class ResponseDTO {
    public boolean entails;
    public CounterModelClassical counterModelC;
    public CounterModelWorlds counterModelW;

    public ResponseDTO() {
    }

    public boolean isEntails() {
        return entails;
    }

    public void setEntails(boolean entails) {
        this.entails = entails;
    }

    public CounterModelClassical getCounterModelC() {
        return counterModelC;
    }

    public void setCounterModelC(CounterModelClassical counterModelC) {
        this.counterModelC = counterModelC;
    }

    public CounterModelWorlds getCounterModelW() {
        return counterModelW;
    }

    public void setCounterModelW(CounterModelWorlds counterModelW) {
        this.counterModelW = counterModelW;
    }

    public void setCounterModel(CounterModel m){
        if(m instanceof CounterModelClassical){
            setCounterModelC((CounterModelClassical) m);
        }
        else if(m instanceof CounterModelWorlds){
            setCounterModelW((CounterModelWorlds) m);
        }
    }
}
