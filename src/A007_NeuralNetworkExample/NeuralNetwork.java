package A007_NeuralNetworkExample;

import org.ejml.simple.SimpleMatrix;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@XmlRootElement
public class NeuralNetwork {

    private int input_nodes;
    private int hidden_nodes;
    private int output_nodes;
    private double learning_rate;
    private SimpleMatrix wih;
    private SimpleMatrix who;
    private String description = "";

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

    public NeuralNetwork(){}

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

    @XmlElement
    public void setInput_nodes(int input_nodes) {
        this.input_nodes = input_nodes;
    }

    @XmlElement
    public void setHidden_nodes(int hidden_nodes) {
        this.hidden_nodes = hidden_nodes;
    }

    @XmlElement
    public void setOutput_nodes(int output_nodes) {
        this.output_nodes = output_nodes;
    }

    @XmlElement
    public void setLearning_rate(double learning_rate) {
        this.learning_rate = learning_rate;
    }

    @XmlElement
    public void setWih(double[][] dwih) {
        wih = new SimpleMatrix(dwih);
    }

    @XmlElement
    public void setWho(double[][] dwho) {
        who = new SimpleMatrix(dwho);
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public int getInput_nodes() {
        return input_nodes;
    }

    public int getHidden_nodes() {
        return hidden_nodes;
    }

    public int getOutput_nodes() {
        return output_nodes;
    }

    public double getLearning_rate() {
        return learning_rate;
    }

    public double[][] getWih() {
        double[][] data = new double[wih.numRows()][wih.numCols()];
        for (int i=0; i<wih.numRows(); i++){
            for (int j=0; j<wih.numCols(); j++){
                data[i][j]=wih.get(i,j);
            }
        }
        return data;
    }

    public double[][] getWho() {
        double[][] data = new double[who.numRows()][who.numCols()];
        for (int i=0; i<who.numRows(); i++){
            for (int j=0; j<who.numCols(); j++){
                data[i][j]=who.get(i,j);
            }
        }
        return data;
    }

    public SimpleMatrix getWihMatrix(){
        return wih;
    }

    public SimpleMatrix getWhoMatrix(){
        return who;
    }

    public String getDescription() {
        return description;
    }

    public boolean readXML(String filepath){
        try {
            File xmlFile = new File(filepath);
            JAXBContext jaxbContext = JAXBContext.newInstance(NeuralNetwork.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            NeuralNetwork temp = (NeuralNetwork) jaxbUnmarshaller.unmarshal(xmlFile);
            this.setInput_nodes(temp.getInput_nodes());
            this.setHidden_nodes(temp.getHidden_nodes());
            this.setOutput_nodes(temp.getOutput_nodes());
            this.setDescription(temp.getDescription());
            this.setLearning_rate(temp.getLearning_rate());
            this.wih = temp.wih;
            this.who = temp.who;
            return true;
        } catch (JAXBException ignored) {
            return false;
        }
    }

    public String saveXML(String folderpath){
        try {
            String dateTime = java.time.LocalDateTime.now().toString().replace(":", "-");
            String filename = folderpath+"/NN_"+dateTime+".xml";
            File file = new File(filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(NeuralNetwork.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            NeuralNetwork aux = new NeuralNetwork();
            aux.setInput_nodes(this.getInput_nodes());
            aux.setHidden_nodes(this.getHidden_nodes());
            aux.setOutput_nodes(this.getOutput_nodes());
            aux.setDescription(this.getDescription());
            aux.setLearning_rate(this.getLearning_rate());
            aux.wih = this.wih;
            aux.who = this.who;
            jaxbMarshaller.marshal(aux, file);
            return  filename;
        } catch (JAXBException ignored) {
            return null;
        }
    }
}
