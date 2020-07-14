package A007_NeuralNetworkExample;

import processing.core.PApplet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Sketch extends PApplet{

    // number of input, hidden and output nodes
    int input_nodes;
    int hidden_nodes;
    int output_nodes;

    // learning rate
    float learning_rate;

    // instance of neural network
    NeuralNetwork n;
    List<Data> dataSet;
    Random r = new Random();

    int score=0;
    int total=0;

    int pos = 0;

    @Override
    public void settings(){
        size(800, 500);
    }

    @Override
    public void setup(){
        frameRate(500);

        input_nodes = 784;
        hidden_nodes = 200;
        output_nodes = 10;
        learning_rate = (float) 0.01;
        n = new NeuralNetwork(input_nodes, hidden_nodes, output_nodes, learning_rate);
        dataSet = dataPrepare_mnist_train("mnist_train_100.csv");
        dataTrainNN(dataSet, n);
        //n.query(dataSet.get(0).getInputs());
    }

    @Override
    public void draw(){
        if (!mousePressed){
            background(255);
            dataTrainNN(dataSet, n);
            // Preparing Query for NN
            List<Double> inputs = dataSet.get(pos).getInputs();
            List<Double> target = dataSet.get(pos).getTarget();
            pos++;
            if (pos==dataSet.size()) pos=0;

            // Pixel drawing Query
            int side = 5;
            int Dsize = (int) Math.ceil(Math.sqrt(input_nodes));
            for (int i=0; i<Dsize; i++){
                for (int j=0; j<Dsize; j++){
                    int pos = (i)+(j*Dsize);
                    if (pos>=input_nodes) break;
                    /*float value = map((float)(double) inputs.get(pos), 0, 1, 0, 16777215);
                    int R = (int)value/(256*256);
                    int G = (int)(value%(256*256))/(256);
                    int B = (int)(value%(256*256))%(256);
                    fill(R,G,B);*/
                    float value = map((float)(double) inputs.get(pos), 0, 1, 0, 255);
                    fill(value);
                    square(i*side, j*side, side);
                }
            }




            List<Double> outputs = n.query(inputs);
            textSize(12);
            fill(0);
            if (dataCompare(target, outputs)){
                text("True", 100, 150);
                score++;
            }else{
                text("False", 100, 150);
            }
            total++;
            text("Score: "+score+"/"+total, 100, 200);
            float ratio = score*100/(float)total;
            int erros = total-score;
            text("Errors:  "+erros, 100, 225);
            text("%  "+ratio, 100, 250);

            //text("inputs:  "+inputs.toString(), 100, 275);
            //text("outputs: "+outputs.toString(), 100, 325);
            int labelO = 0;
            double maxO = outputs.get(0);
            for(int i=0; i<outputs.size(); i++){
                if (outputs.get(i)>maxO){
                    maxO=outputs.get(i);
                    labelO=i;
                }
            }
            int labelT = 0;
            double maxT = target.get(0);
            for(int i=0; i<target.size(); i++){
                if (target.get(i)>maxT){
                    maxT=target.get(i);
                    labelT=i;
                }
            }
            text("i think: "+labelO, 100, 350);
            text("actual: "+labelT, 100, 375);
        }
    }

    public List<Data> dataPrepare_mnist_train(String filename){
        String filepath = "src/A007_NeuralNetworkExample/Data/"+filename;
        List<String> test_data_list = new ArrayList<>();
        try {
            BufferedReader data = new BufferedReader(new FileReader(filepath));
            String line;
            while((line = data.readLine()) != null){
                test_data_list.add(line);
            }
            data.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Data> dataSet = new ArrayList<>();
        for (String record:test_data_list) {
            String[] all_values = record.split(",");
            int correct_label = Integer.parseInt(all_values[0]);
            List<Double> inputs = new ArrayList<>();
            List<Double> targets = new ArrayList<>();
            for(int i=1; i<all_values.length; i++){
                inputs.add((Double.parseDouble(all_values[i])/(255.0*0.99))+0.01);
            }
            for(int i=0; i<output_nodes; i++){
                if (i==correct_label){
                    targets.add(0.99);
                }else{
                    targets.add(0.01);
                }
            }
            Data data = new Data(inputs, targets);
            dataSet.add(data);
        }
        return dataSet;
    }

    public void dataTrainNN(List<Data> dataSet, NeuralNetwork n){
        for (Data data:dataSet) {
            n.train(data.inputs, data.target);
        }
    }

    public boolean dataCompare(List<Double> target, List<Double> outputs){
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

    public static void main(String[] args){
        String[] processingArgs = {"A007_NeuralNetworkExample.Template"};
        Sketch currentSketch = new Sketch();
        PApplet.runSketch(processingArgs, currentSketch);
    }
}