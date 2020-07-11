package A003_GeneticAlgorithmExample;

import java.util.Random;

public class DNA {
    // The genetic sequence
    char[] genes;

    float fitness;
    Random r = new Random();

    // Constructor (makes a random DNA)
    DNA(int num) {
        genes = new char[num];
        for (int i = 0; i < genes.length; i++) {
            genes[i] = (char) (r.nextInt(128-32)+32); // Pick from range of chars
        }
    }

    // Converts character array to a String
    String getPhrase() {
        return new String(genes);
    }

    // Fitness function (returns floating point % of "correct" characters)
    void fitness (String target) {
        int score = 0;
        for (int i = 0; i < genes.length; i++) {
            if (genes[i] == target.charAt(i)) {
                score++;
            }
        }
        fitness = (float) Math.pow(2,score);
    }

    // Crossover
    DNA crossover(DNA partner) {
        // A new child
        DNA child = new DNA(genes.length);

        int midpoint = (int)r.nextInt(genes.length); // Pick a midpoint

        // Half from one, half from the other
        for (int i = 0; i < genes.length; i++) {
            if (i > midpoint) child.genes[i] = genes[i];
            else              child.genes[i] = partner.genes[i];
        }
        return child;
    }

    // Based on a mutation probability, picks a new random character
    void mutate(float mutationRate) {
        for (int i = 0; i < genes.length; i++) {
            if ((r.nextInt(1000)/(float)1000) < mutationRate) {
                genes[i] = (char) (r.nextInt(128-32)+32);
            }
        }
    }
}
