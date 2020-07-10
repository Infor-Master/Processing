package A002_GameOfLife;

public class Cell {
    public int x;
    public int y;
    public boolean alive = false;

    public Cell(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public void create(){
        this.alive=true;
    }

    public void kill(){
        this.alive=false;
    }
}
