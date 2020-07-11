package A005_GAFunctionFinder;

import controlP5.ControlFont;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PFont;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Sketch extends PApplet{

    private String filepath;
    private String filename;
    private HashMap<String, String> values;
    private int popSize;
    private Population population;
    private double mutationRate;
    private double killRate;
    private PFont f;
    private PFont p;
    ControlP5 controlP5;

    @Override
    public void settings(){
        size(1200, 800);
    }

    @Override
    public void setup(){
        smooth();
        controlP5 = new ControlP5(this);
        f = createFont("Courier New Bold", 32);
        p = createFont("Courier New Bold", 10);

        ControlFont font = new ControlFont(p);

        // change the original colors
        controlP5.setColorForeground(0xffaa0000);
        controlP5.setColorBackground(0xff660000);
        controlP5.setColorActive(0xffff0000);
        controlP5.setFont(font);

        filename="Celsius-Fahrenheit";
        //filename="Double";
        filepath = "src/A005_GAFunctionFinder/Input/"+filename+".txt";

        popSize = 1000;
        values = new HashMap<>();
        frameRate(60);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line;
            while ((line = reader.readLine()) != null){
                String[] smap = line.split(";");
                values.put(smap[0], smap[1]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        population = new Population(popSize, mutationRate, values);

        controlP5.addSlider("Mutation Rate")
                .setRange(0,100)
                .setValue(1)
                .setPosition(100,400)
                .setSize(10,100)
                .setColorValue(0xffff88ff)
                .setColorLabel(0xffdddddd)
                .setColorCaptionLabel(0)
                .setColorValueLabel(0);

        controlP5.addSlider("Population Kill Rate")
                .setRange(0,100)
                .setValue(50)
                .setPosition(100,600)
                .setSize(10,100)
                .setColorValue(0xffff88ff)
                .setColorLabel(0xffdddddd)
                .setColorCaptionLabel(0)
                .setColorValueLabel(0);
    }

    @Override
    public void draw(){

        mutationRate=controlP5.getController("Mutation Rate").getValue() / 100;
        killRate=controlP5.getController("Population Kill Rate").getValue() / 100;
        // Generate mating pool
        population.naturalSelection();
        //Create next generation
        population.generate(mutationRate, killRate);
        // Calculate fitness
        population.calcFitness(values);
        displayInfo();

        // If we found the target phrase, stop
        if (population.finished()) {
            println(millis()/1000.0);
            noLoop();
        }
    }

    private void displayInfo(){
        background(255);
        String best = population.getBest().getFunction();
        textFont(f);
        textAlign(LEFT);
        fill(0);

        textSize(16);
        text("Best function for ["+filename+"]:",20, 30);
        textSize(32);
        text(best, 20, 75);

        textSize(12);
        text("total generations: " + population.getGenerations(), 20, 140);
        text("best fitness: " + nf((float) population.getBest().fitness, 0, 2), 20, 155);
        text("average fitness: " + nf((float) population.getAverageFitness(), 0, 2), 20, 170);
        text("total populationation: " + popSize, 20, 185);
        text("mutation rate: " + (int)(mutationRate * 100) + "%", 20, 200);

        textSize(10);
        text("All functions:\n" + population.allFunctions(), 800, 10);
    }

    public static void main(String[] args){
        String[] processingArgs = {"A005_GAFuntionFinder.Sketch"};
        Sketch currentSketch = new Sketch();
        PApplet.runSketch(processingArgs, currentSketch);
    }
}
