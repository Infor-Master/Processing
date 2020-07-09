package A001_Template;

import processing.core.PApplet;

public class Template extends PApplet {

    public void settings(){
        size(500, 500);
    }

    public void draw(){
        ellipse(mouseX, mouseY, 50, 50);
    }

    public void mousePressed(){
        background(64);
    }

    public static void main(String[] args){
        String[] processingArgs = {"Template"};
        Template currentSketch = new Template();
        PApplet.runSketch(processingArgs, currentSketch);
    }
}
