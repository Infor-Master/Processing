package A005_GAFunctionFinder;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DNA {
    double fitness;
    Random r = new Random();
    double A;    // ax+b
    double B;

    DNA(){
        double min = (double) -Math.pow(10,10);
        double max = (double) Math.pow(10,10);
        A= min + r.nextDouble() * (max - min);
        B= min + r.nextDouble() * (max - min);
    }

    void fitness(HashMap<String, String> data){
        fitness=0;
        for(Map.Entry<String, String> entry:data.entrySet()){
            double vIn = Double.parseDouble(entry.getKey());
            double vOut = Double.parseDouble(entry.getValue());
            fitness += Math.abs(vOut-(A*vIn + B));
        }
    }

    protected DNA crossover(DNA partner){
        DNA child = new DNA();
        child.A = (A+partner.A)/2;
        child.B = (B+partner.B)/2;
        return child;
    }

    protected void mutate(double mutationRate){
        double min = (double) -Math.pow(10,10);;
        double max = (double) Math.pow(10,10);;
        if (r.nextDouble() < mutationRate) {
            A= min + r.nextDouble() * (max - min);
        }
        if (r.nextDouble() < mutationRate) {
            B= min + r.nextDouble() * (max - min);
        }
    }

    String getFunction(){
        return new String(A+" x + "+B);
    }
}
