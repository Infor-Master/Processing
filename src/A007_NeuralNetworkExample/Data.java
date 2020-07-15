package A007_NeuralNetworkExample;

import org.jetbrains.annotations.NotNull;

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

    public static boolean dataCompare(@NotNull List<Double> target, @NotNull List<Double> outputs){
        double maxI = target.get(0);
        int labelI = 0;
        double maxO = outputs.get(0);
        int labelO = 0;
        for(int i=0; i<target.size(); i++){
            if (target.get(i)>maxI){
                maxI=target.get(i);
                labelI=i;
            }
        }
        for(int i=0; i<outputs.size(); i++){
            if (outputs.get(i)>maxO){
                maxO=outputs.get(i);
                labelO=i;
            }
        }
        return (labelI == labelO);
    }
}
