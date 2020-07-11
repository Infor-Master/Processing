package A004_GASmartRockets;

import processing.core.PApplet;
import processing.core.PVector;

public class Sketch extends PApplet{

    Rocket rocket;
    Population population;
    PVector target;
    static float maxforce = (float) 0.2;

    int age;
    float stat;

    int generation = 0;

    float barrierx;
    float barriery;
    float barrierw;
    float barrierh;

    @Override
    public void settings(){
        size(1024, 768);
    }

    @Override
    public void setup(){
        frameRate(200);
        population = new Population(this);
        rocket = new Rocket(this);
        target = new PVector(width / (float)2, 50);
        age = 0;

        stat = 0;

        barrierw = width / (float)8;
        barrierh = 10;
        barrierx = (width - barrierw) / 2;
        barriery = (height - barrierh) / 2;
    }

    @Override
    public void draw(){
        background(5);

        // draw the target
        stroke(255);
        fill(128);
        ellipse(target.x, target.y, 20, 20);
        fill(100);
        noStroke();
        strokeWeight(2);
        ellipse(target.x+2, target.y-2, 10, 10);

        // draw the barrier
        fill(255, 0, 0);
        stroke(128);
        rectMode(CORNER);
        rect(barrierx, barriery, barrierw, barrierh);
        strokeWeight(1);
        stroke(55, 0, 0);
        line(barrierx, barriery-10, target.x-10, target.y+10);
        line(barrierx + barrierw, barriery-10, target.x+10, target.y+10);

        population.run();

        age++;
        if (age >= DNA.lifespan) {
            stat = population.evaluate();
            population.selection();
            age = 0;
            generation++;
        }

        textSize(18);
        noStroke();
        fill(255, 128, 0);
        text("Generation: " + generation, 20, 20);
        text("Age: " + age, 20, 40);
        if (stat != 0) {
            text("Stat: " + stat, 20, 60);
        }
    }

    void show(Rocket rocket) {
        pushMatrix();
        noStroke();
        if (rocket.hitTarget) {
            fill(50, 205, 50);
        } else if (rocket.crashed) {
            fill(128, 128, 128);
        } else {
            fill(255, 150);
        }

        translate(rocket.pos.x, rocket.pos.y);
        rotate(rocket.vel.heading());

        // draw rocket body
        rectMode(CENTER);
        rect(0, 0, 25, 5);

        // draw nose cone
        fill(165, 42, 42);
        ellipse(12, 0, 10, 5);

        if (!rocket.hitTarget && !rocket.crashed) {
            // draw thrust flame
            fill(255, 140 + random(0, 115), random(0, 128));
            beginShape();
            vertex(-14, -3);
            vertex(-35, 0);
            vertex(-14, 3);
            endShape();
        }

        popMatrix();
    }

    public static void main(String[] args){
        String[] processingArgs = {"A004_GASmartRockets.Sketch"};
        Sketch currentSketch = new Sketch();
        PApplet.runSketch(processingArgs, currentSketch);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public PVector getTarget() {
        return target;
    }

    public float getBarrier(char type){
        if (type == 'x') return barrierx;
        else if (type == 'y') return barriery;
        else if (type == 'w') return barrierw;
        else return barrierh;
    }

    public int getAge() {
        return age;
    }
}
