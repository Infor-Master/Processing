package A002_GameOfLife;

import processing.core.PApplet;

public class Sketch extends PApplet {

    private final int width = 800;
    private final int height = 600;
    private final int step = 10;
    private final int framerate = 5;

    private Cell[][] cells = new Cell[width/step][height/step];
    private boolean paused = false;

    @Override
    public void settings(){
        size(width, height);
    }

    @Override
    public void setup(){
        frameRate(framerate);
        for (int i=0; i<width/step; i++){
            for(int j=0; j<height/step; j++){
                cells[i][j]=new Cell(i*step, j*step);
                fill(50,50,50);
                rect(i*step, j*step, step, step);
            }
        }
    }

    @Override
    public void draw(){
        if (paused) {
            return;
        }

        Cell[][] Tcells = new Cell[width/step][height/step];

        for (int i=0; i<(width/step); i++){
            for (int j=0; j<(height/step); j++){

                Tcells[i][j]=new Cell(i*step, j*step);
                Tcells[i][j].alive=cells[i][j].alive;

                if (cells[i][j].alive){
                    if (checkAround(cells[i][j])>3){
                        Tcells[i][j].kill();
                        fill(50,50,50);
                        rect(i*step, j*step, step, step);
                    }else if (checkAround(cells[i][j])<2){
                        Tcells[i][j].kill();
                        fill(50,50,50);
                        rect(i*step, j*step, step, step);
                    }
                }else{
                    if (checkAround(cells[i][j])==3){
                        Tcells[i][j].create();
                        fill(255,0,0);
                        rect(i*step, j*step, step, step);
                    }
                }
            }
        }
        cells=Tcells;
    }

    private int checkAround(Cell c){
        int count = 0;
        int x = c.x/step;
        int y= c.y/step;
        for (int i=-1; i<=1; i++){
            if (x+i<0 || x+i>=width/step){
                continue;
            }
            for(int j=-1; j<=1; j++){
                if (y+j<0 || y+j>=height/step){
                    continue;
                }
                if (i==0 && j==0){
                    continue;
                }
                if (cells[(x+i)][(y+j)].alive){
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void mousePressed(){
        int x=mouseX;
        x = (x - x%step)/step;
        int y=mouseY;
        y = (y - y%step)/step;
        //System.out.println(x+", "+y);
        Cell c = cells[x][y];
        c.create();
        fill(255,0,0);
        rect(x*step, y*step, step, step);
    }

    @Override
    public void keyPressed(){
        if (key=='p'){
            paused= !paused;
        }
    }

    public static void main(String[] args){
        String[] processingArgs = {"Game Of Life"};
        Sketch currentSketch = new Sketch();
        PApplet.runSketch(processingArgs, currentSketch);
    }
}
