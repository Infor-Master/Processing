package A004_GASmartRockets;

import processing.core.PVector;

import java.util.Arrays;
import java.util.Random;

public class DNA {
    protected final PVector[] genes;
    protected static int lifespan = 400;
    Random r = new Random();

    DNA() {
        genes = new PVector[lifespan];

        for (int i = 0; i < lifespan; i++) {
            PVector gene = PVector.random2D();
            gene.setMag(Sketch.maxforce);
            genes[i] = gene;
        }
    }

    DNA(PVector[] genes_) {
        genes = Arrays.copyOf(genes_, genes_.length);
    }

    DNA crossover(DNA partner) {
        PVector[] newgenes = new PVector[genes.length];
        int mid = r.nextInt(genes.length*1000)/1000;
        for (int i = 0; i < genes.length; i++) {
            if (i > mid) {
                newgenes[i] = genes[i];
            } else {
                newgenes[i] = partner.genes[i];
            }
        }
        return new DNA(newgenes);
    }

    void mutation() {
        for (int i = 0; i < genes.length; i++) {
            if ((r.nextInt((i*10000)+1)/(float)10000) < 0.01) {
                PVector gene = PVector.random2D();
                gene.setMag(Sketch.maxforce);
                genes[i] = gene;
            }
        }
    }

}
