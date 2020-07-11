package A005_GAFunctionFinder;

import java.util.*;

import static java.lang.Math.min;
import static processing.core.PApplet.map;

public class Population {
    DNA[] population;
    ArrayList<DNA> matingPool;
    protected int generations;
    protected int perfectScore;
    protected boolean finished = false;
    private Random r = new Random();

    Population(int size, double m, HashMap<String, String> data){
        population = new DNA[size];
        generations = 0;
        perfectScore = 0;
        for (int i=0; i<population.length; i++){
            population[i] = new DNA();
        }
        calcFitness(data);
        matingPool = new ArrayList<>();
    }

    protected void calcFitness(HashMap<String, String> data) {
        for (DNA dna : population) {
            dna.fitness(data);
        }
    }

    protected void naturalSelection(){
        matingPool.clear();
        double maxFitness = population[0].fitness;
        for (DNA dna : population){
            if (dna.fitness>maxFitness){
                maxFitness = dna.fitness;
            }
        }
        for (DNA dna : population) {
            double fitness = map((float)dna.fitness, 0, (float)maxFitness, 0, 1);
            int n = (int)(fitness * 100);
            for(int i=n; i<=100; i++){
                matingPool.add(dna);
            }
        }
    }

    protected void generate(double mutationRate, double killRate){
        HashMap<DNA, Double> hmap = new HashMap<>();
        for (DNA dna:population) {
            hmap.put(dna, dna.fitness);
        }
        Map<DNA, Double> map = sortByValue(hmap);
        int i=0;
        for (Map.Entry<DNA, Double> entry: map.entrySet()) {
            population[i]=entry.getKey();
            i++;
            if (i==population.length) break;
        }
        for(i=population.length-(int)(population.length*killRate); i<population.length; i++){
            int pa = r.nextInt(matingPool.size());
            int pb = r.nextInt(matingPool.size());
            DNA partnerA = matingPool.get(pa);
            DNA partnerB = matingPool.get(pb);
            DNA child = partnerA.crossover(partnerB);
            child.mutate(mutationRate);
            population[i] = child;
        }
        generations++;
    }

    protected DNA getBest(){
        DNA recordDNA = population[0];
        double recordFitness = recordDNA.fitness;
        for(DNA dna : population){
            if (dna.fitness<recordFitness){
                recordDNA=dna;
                recordFitness=dna.fitness;
            }
        }
        if (recordFitness == perfectScore) finished=true;
        return recordDNA;
    }

    protected boolean finished() {
        return finished;
    }

    protected int getGenerations() {
        return generations;
    }

    protected double getAverageFitness() {
        double total = 0;
        for (DNA dna : population) {
            total+=dna.fitness;
        }
        return total/population.length;
    }

    public static HashMap<DNA, Double> sortByValue(HashMap<DNA, Double> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<DNA, Double> > list =
                new LinkedList<Map.Entry<DNA, Double> >(hm.entrySet());

        // Sort the list
        list.sort(new Comparator<Map.Entry<DNA, Double>>() {
            public int compare(Map.Entry<DNA, Double> o1,
                               Map.Entry<DNA, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        // put data from sorted list to hashmap
        HashMap<DNA, Double> temp = new LinkedHashMap<DNA, Double>();
        for (Map.Entry<DNA, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    String allFunctions(){
        StringBuilder everything = new StringBuilder();
        int displayLimit = min(population.length,50);

        for (int i = 0; i < displayLimit; i++) {
            everything.append(population[i].getFunction()).append("\n");
        }
        return everything.toString();
    }
}
