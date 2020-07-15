package A007_NeuralNetworkExample;

import processing.core.PApplet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Sketch extends PApplet{

    // instance of neural network
    NeuralNetwork n;
    List<Data> dataSet;

    int score=0;
    int total=0;
    int pos = 0;

    @Override
    public void settings(){
        size(800, 500);
    }

    @Override
    public void setup(){
        frameRate(60);
        // CRIA NN NOVA
        n = new NeuralNetwork(784, 200, 10, (float) 0.01);
        n.setDescription("Recognizes 0~9 digits from 28x28 images");
        // ABRE NN JÁ EXISTENTE
        n = new NeuralNetwork();
        n.readXML("data/A007_NeuralNetworkExample/NeuralNetwork/NN_2020-07-15T16-50-57.856.xml");
        // ABRE SET DE TREINO
        dataSet = dataPrepare_mnist_train("mnist_train_100.csv");
        dataTrainNN(dataSet, n);
        //n.query(dataSet.get(0).getInputs());
    }

    @Override
    public void draw(){

        //gravar NN para ficheiro

        if (mousePressed){
            n.saveXML("data/A007_NeuralNetworkExample/NeuralNetwork/");
        }

        //Treinar a NN - estamos a reutilizar o mesmo SET mas o correto deveria ser ir usando SETS de dados novos. Dificil de arranjar/gerar

        dataTrainNN(dataSet, n);

        //Desenhar aplicação. Irrelevante ao uso de NN

        background(255);
        List<Double> inputs = dataSet.get(pos).getInputs();
        List<Double> target = dataSet.get(pos).getTarget();
        pos++;
        if (pos==dataSet.size()) pos=0;
        int side = 5;
        int Dsize = (int) Math.ceil(Math.sqrt(n.getInput_nodes()));
        for (int i=0; i<Dsize; i++){
            for (int j=0; j<Dsize; j++){
                int pos = (i)+(j*Dsize);
                if (pos>=n.getInput_nodes()){
                    break;
                }
                float value = map((float)(double) inputs.get(pos), 0, 1, 0, 255);
                fill(value);
                square(i*side, j*side, side);
            }
        }
        List<Double> outputs = n.query(inputs);
        textSize(12);
        if (Data.dataCompare(target, outputs)){
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

    public List<Data> dataPrepare_mnist_train(String filename){
        String filepath = "data/A007_NeuralNetworkExample/TrainingData/"+filename;
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
            for(int i=0; i<n.getOutput_nodes(); i++){
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

    public static void main(String[] args){
        String[] processingArgs = {"A007_NeuralNetworkExample.Template"};
        Sketch currentSketch = new Sketch();
        PApplet.runSketch(processingArgs, currentSketch);
    }
}