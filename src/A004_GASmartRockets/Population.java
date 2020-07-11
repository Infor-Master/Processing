package A004_GASmartRockets;

import java.util.ArrayList;
import java.util.List;

public class Population {
    Rocket[] rockets;
    int popsize;
    List<Rocket> matingpool;
    Sketch sketch;

    Population(Sketch sketch) {
        this.sketch=sketch;
        popsize = 100;
        rockets = new Rocket[popsize];
        matingpool = new ArrayList<Rocket>();

        for (int i = 0; i < popsize; i++) {
            rockets[i]  = new Rocket(sketch);
        }
    }

    float evaluate() {
        float avgfit = 0;
        float maxfit = 0;
        for (Rocket rocket : rockets) {
            rocket.calcFitness();
            if (rocket.fitness > maxfit) {
                maxfit = rocket.fitness;
            }
            avgfit += rocket.fitness;
        }
        avgfit /= rockets.length;

        for (Rocket rocket : rockets) {
            rocket.fitness /= maxfit;
        }

        matingpool = new ArrayList<Rocket>();

        for (Rocket rocket : rockets) {
            float n = rocket.fitness * 100;
            for (int j = 0; j < n; j++) {
                matingpool.add(rocket);
            }
        }

        return avgfit;
    }

    Rocket random(List<Rocket> list) {
        int r = (int)(Math.random() * (list.size()));
        return list.get(r);
    }

    void selection() {
        Rocket[] newRockets = new Rocket[popsize];
        for (int i = 0; i < rockets.length; i++) {
            DNA parentA = random(matingpool).dna;
            DNA parentB = random(matingpool).dna;
            DNA child = parentA.crossover(parentB);
            child.mutation();
            newRockets[i] = new Rocket(sketch, child);
        }

        rockets = newRockets;
    }

    void run() {
        for (Rocket rocket : rockets) {
            rocket.update();
            sketch.show(rocket);
        }
    }
}
