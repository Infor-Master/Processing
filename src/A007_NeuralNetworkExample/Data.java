package A007_NeuralNetworkExample;

import java.util.List;

public class Data {
    List<Double> inputs;
    List<Double> target;

    public Data(List<Double> inputs, List<Double> target){
        this.inputs=inputs;
        this.target=target;
    }

    public List<Double> getInputs() {
        return inputs;
    }

    public List<Double> getTarget() {
        return target;
    }
}
