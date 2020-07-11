package A006_ControlP5;

import controlP5.*;
import processing.core.PApplet;
import processing.core.PFont;

public class Sketch extends PApplet {

    ControlP5 controlP5;

    @Override
    public void settings(){
        size(500, 500);
    }

    @Override
    public void setup(){
        smooth();
        controlP5 = new ControlP5(this);

        // change the default font to Verdana
        PFont p = createFont("Verdana",10);
        ControlFont font = new ControlFont(p);

        // change the original colors
        controlP5.setColorForeground(0xffaa0000);
        controlP5.setColorBackground(0xff660000);
        controlP5.setFont(font);
        controlP5.setColorActive(0xffff0000);

        // add the elments ( see example 1 for the parameters )
        controlP5.addBang("bang1")
                .setPosition(10,10)
                .setSize(20,20);
        controlP5.addButton("button1")
                .setValue(1)
                .setPosition(70,10)
                .setSize(60,20);
        controlP5.addToggle("toggle1")
                .setValue(false)
                .setPosition(170,10)
                .setSize(20,20);
        controlP5.addSlider("slider1")
                .setRange(0,255)
                .setValue(128)
                .setPosition(10,80)
                .setSize(10,100)
                .setColorValue(0xffff88ff)
                .setColorLabel(0xffdddddd);

        Slider s = controlP5.addSlider("slider2")
                .setRange(0,255)
                .setValue(128)
                .setPosition(70,80)
                .setSize(100,10);
        // change sliderMode of the Slider object. The default is Slider.FIX
        s.setSliderMode(Slider.FLEXIBLE);

        controlP5.addKnob("knob1")
                .setRange(0,360)
                .setValue(0)
                .setPosition(70,120)
                .setSize(50,50);

        Numberbox n = controlP5.addNumberbox("numberbox1")
                .setValue(50)
                .setPosition(170,120)
                .setSize(60,14);
        // change Multiplier of the Numberbox ( default is 1 )
        n.setMultiplier(30);
    }

    @Override
    public void draw(){
        background(0); // background black
    }

    @Override
    public void mousePressed(){

    }

    @Override
    public void mouseDragged(){

    }

    @Override
    public void mouseReleased(){

    }

    void controlEvent(ControlEvent theEvent) {


        if(theEvent.isController()) {

            print("control event from : "+theEvent.getController().getName());
            println(", value : "+theEvent.getController().getValue());

            // clicking on bang1 sets toggle1 value to 1 (true)
            if(theEvent.getController().getName()=="bang1") {
                controlP5.getController("toggle1").setValue(1);
            }

            // clicking on button1 sets toggle1 value to 0 (false)
            if(theEvent.getController().getName()=="button1") {
                controlP5.getController("toggle1").setValue(0);
            }

            // dragging slider1 changes the value of slider2
            if(theEvent.getController().getName()=="slider1") {
                float value = theEvent.getController().getValue();
                // prevent warnings at start
                if(controlP5.getController("slider2") != null) {
                    controlP5.getController("slider2").setValue(value);
                }
            }


            // changing the value of numberbox1 turns knob1
            if(theEvent.getController().getName()=="numberbox1") {
                controlP5.getController("knob1").setValue(theEvent.getController().getValue());
            }


        }

    }

    public static void main(String[] args){
        String[] processingArgs = {"A006_ControlP5.Sketch"};
        Sketch currentSketch = new Sketch();
        PApplet.runSketch(processingArgs, currentSketch);
    }

}
