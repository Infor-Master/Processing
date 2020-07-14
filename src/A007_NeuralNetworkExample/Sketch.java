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
        /*input_nodes = 3;
        hidden_nodes = 20;
        output_nodes = 3;*/
        learning_rate = (float) 0.01;
        n = new NeuralNetwork(input_nodes, hidden_nodes, output_nodes, learning_rate);
        dataSet = dataPrepare_mnist_train("mnist_train_100.csv");

        //n.query(dataSet.get(0).getInputs());
    }

    @Override
    public void draw(){
        if (!mousePressed){
            background(255);

            // Training NN
            //dataSet = dataPrepare_max3(1000);
            dataSet = dataPrepare_mnist_train("mnist_train_100.csv");
            dataTrainNN(dataSet, n);

            // Preparing Query for NN
            /*List<Double> inputs = new ArrayList<>();
            for(int i=0; i<input_nodes; i++){
                inputs.add(r.nextDouble());
            }*/
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
            text("%  "+ratio, 100, 225);

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

    public List<Data> dataPrepare_max3(int size){
        List<Data> dataSet = new ArrayList<>();
        for (int i=0; i<size; i++){
            List<Double> inputs = new ArrayList<>();
            List<Double> targets = new ArrayList<>();
            double max = 0;
            int correct_label=0;
            for(int j=0; j<input_nodes; j++){
                inputs.add(r.nextDouble());
                if (j==0){
                    max=inputs.get(0);
                }else{
                    if (inputs.get(j)>max){
                        max=inputs.get(j);
                        correct_label = j;
                    }
                }
            }
            for(int j=0; j<output_nodes; j++){
                if (j==correct_label){
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


    /*public void setupGraphData(){
        System.out.println(dataMatrix.toString());
        dataMatrix.set(0,0,1);
        dataMatrix.set(0,1,2);
        dataMatrix.set(1,0,9);
        dataMatrix.set(2,1,12);
        System.out.println(dataMatrix.toString());
        System.out.println(dataMatrix.get(0,1));
        System.out.println(dataMatrix.get(1,0));
    }

    public void drawGraphData(){
        int side = 50;
        for (int i=0; i<lines; i++){
            for(int j=0; j<columns; j++){
                float value = map((float) dataMatrix.get(i, j), 0, 20, 0, 16777215);
                int R = (int)value/(256*256);
                int G = (int)(value%(256*256))/(256);
                int B = (int)(value%(256*256))%(256);
                fill(R,G,B);
                square(j*side, i*side, side);
                fill(255);
                square((j+5)*side, i*side, side);
            }
        }
        for (int i=0; i<lines; i++){
            for(int j=0; j<columns; j++){
                textSize(12);
                fill(0);
                text((float) dataMatrix.get(i,j), (j+5)*side, (float) ((i+0.5)*side));
            }
        }
    }*/

    public static void main(String[] args){
        String[] processingArgs = {"A007_NeuralNetworkExample.Template"};
        Sketch currentSketch = new Sketch();
        PApplet.runSketch(processingArgs, currentSketch);
    }
}