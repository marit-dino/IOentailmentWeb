package restApplication;

import encoding.IOLogics.CounterModel;

public class ResponseDTO {
    public boolean entails;
    public CounterModel counterModel;

    public ResponseDTO() {
    }

    public boolean isEntails() {
        return entails;
    }

    public void setEntails(boolean entails) {
        this.entails = entails;
    }

    public CounterModel getCounterModel() {
        return counterModel;
    }

    public void setCounterModel(CounterModel counterModel) {
        this.counterModel = counterModel;
    }
}
