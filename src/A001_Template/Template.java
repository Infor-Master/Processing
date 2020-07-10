package A001_Template;

import processing.core.*;

public class Template extends PApplet {

    // TODO: Customize screen size and so on here
    @Override
    public void settings(){
        size(500, 500);
    }

    // TODO: Your custom drawing and setup on applet start belongs here
    @Override
    public void setup(){
        frameRate(60);
        noStroke();
    }

    // TODO: Do your drawing for each frame here
    @Override
    public void draw(){
        ellipse(mouseX, mouseY, 50, 50);
    }

    @Override
    public void mousePressed(){
        //clear();
        //background(64);
        background(64,64,64,255);
    }

    public static void main(String[] args){
        String[] processingArgs = {"A001_Template.Template"};
        Template currentSketch = new Template();
        PApplet.runSketch(processingArgs, currentSketch);
    }
}
