package A003_GeneticAlgorithmExample;

import processing.core.PApplet;
import processing.core.PFont;

public class Sketch extends PApplet{

    PFont f;
    String target;
    int popmax;
    float mutationRate;
    Population population;

    @Override
    public void settings(){
        size(1000, 200);
    }

    @Override
    public void setup(){
        f = createFont("Courier New Bold", 32, true);
        target = "Estuans Interius Ira Vehementi";
        popmax = 1000;
        mutationRate = (float)0.01;

        // Create a populationation with a target phrase, mutation rate, and populationation max
        population = new Population(target, mutationRate, popmax);
    }

    @Override
    public void draw(){
        // Generate mating pool
        population.naturalSelection();
        //Create next generation
        population.generate();
        // Calculate fitness
        population.calcFitness();
        displayInfo();

        // If we found the target phrase, stop
        if (population.finished()) {
            println(millis()/1000.0);
            noLoop();
        }
    }

    void displayInfo() {
        background(255);
        // Display current status of populationation
        String answer = population.getBest();
        textFont(f);
        textAlign(LEFT);
        fill(0);


        textSize(16);
        text("Best phrase:",20,30);
        textSize(32);
        text(answer, 20, 75);

        textSize(12);
        text("total generations: " + population.getGenerations(), 20, 140);
        text("average fitness: " + nf(population.getAverageFitness(), 0, 2), 20, 155);
        text("total populationation: " + popmax, 20, 170);
        text("mutation rate: " + (int)(mutationRate * 100) + "%", 20, 185);

        textSize(10);
        text("All phrases:\n" + population.allPhrases(), 750, 10);
    }

    public static void main(String[] args){
        String[] processingArgs = {"A003_GeneticAlgorithm.Sketch"};
        Sketch currentSketch = new Sketch();
        PApplet.runSketch(processingArgs, currentSketch);
    }
}
