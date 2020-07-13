package A007_NeuralNetworkExample;

import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuralNetwork {

    int input_nodes;
    int hidden_nodes;
    int output_nodes;
    double learning_rate;
    SimpleMatrix wih;
    SimpleMatrix who;

    public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes, float learningRate){
        input_nodes =inputNodes;
        hidden_nodes =hiddenNodes;
        output_nodes =outputNodes;
        learning_rate=learningRate;

        // link weight matrices, Wih and Who
        // weights inside the arrays are w_i_j, where link is from node i to node j in the next layer
        // w11 w21
        // w21 w22 etc...
        wih = new SimpleMatrix(new double[hidden_nodes][input_nodes]);
        who = new SimpleMatrix(new double[output_nodes][hidden_nodes]);
        randomize_matrices();
        //System.out.println(wih.toString());
        //System.out.println(who.toString());
    }

    public void train(List<Double> inputs_list, List<Double> targets_list){
        SimpleMatrix inputs = new SimpleMatrix(array_list(inputs_list)).transpose();
        SimpleMatrix hidden_inputs = wih.mult(inputs);
        SimpleMatrix hidden_outputs = sigmoid_matrice(hidden_inputs);
        SimpleMatrix final_inputs = who.mult(hidden_outputs);
        SimpleMatrix final_outputs = sigmoid_matrice(final_inputs);

        SimpleMatrix targets = new SimpleMatrix(array_list(targets_list)).transpose();
        SimpleMatrix output_errors = targets.minus(final_outputs);
        SimpleMatrix hidden_errors = who.transpose().mult(output_errors);

        // Separated by parts to facilitate debug
        SimpleMatrix temp1who = final_outputs.elementMult(final_outputs.negative().plus(1.0));
        SimpleMatrix temp1wih = hidden_outputs.elementMult(hidden_outputs.negative().plus(1.0));
        SimpleMatrix temp2who = output_errors.elementMult(temp1who);
        SimpleMatrix temp2wih = hidden_errors.elementMult(temp1wih);
        SimpleMatrix temp3who = temp2who.mult(hidden_outputs.transpose());
        SimpleMatrix temp3wih = temp2wih.mult(inputs.transpose().divide(1/learning_rate));
        SimpleMatrix temp4who = temp3who.divide(1/learning_rate);
        SimpleMatrix temp4wih = temp3wih.divide(1/learning_rate);
        who = who.plus(temp4who);
        wih = wih.plus(temp4wih);
    }

    public List<Double> query(List<Double> inputs_list){

        SimpleMatrix inputs = new SimpleMatrix(array_list(inputs_list)).transpose();
        SimpleMatrix hidden_inputs = wih.mult(inputs);
        SimpleMatrix hidden_outputs = sigmoid_matrice(hidden_inputs);
        SimpleMatrix final_inputs = who.mult(hidden_outputs);
        SimpleMatrix final_outputs = sigmoid_matrice(final_inputs);
        return list_matrice(final_outputs);
    }

    private void randomize_matrices(){
        Random r = new Random();

        for (int x  = 0; x < wih.getNumElements(); x++){
            wih.set(x, r.nextGaussian()*Math.pow(hidden_nodes, -0.5));
        }
        for (int x  = 0; x < who.getNumElements(); x++){
            who.set(x, r.nextGaussian()*Math.pow(output_nodes, -0.5));
        }
    }

    private SimpleMatrix sigmoid_matrice(SimpleMatrix m){
        SimpleMatrix res = new SimpleMatrix(new double[m.getNumElements()][1]);
        for (int x  = 0; x < m.getNumElements(); x++){
            res.set(x, sigmoid(m.get(x)));
        }
        return res;
    }

    private List<Double> list_matrice(SimpleMatrix m){
        List<Double> l = new ArrayList<>();
        for (int x  = 0; x < m.getNumElements(); x++){
            l.add(m.get(x));
        }
        return l;
    }

    private double[][] array_list(List<Double> l){
        double[][] a = new double[1][l.size()];
        for (int i=0; i<l.size(); i++) {
            a[0][i]=l.get(i);
        }
        return a;
    }

    private double sigmoid(double x){
        return (1/( 1 + Math.pow(Math.E,(-1*x))));
    }
}
