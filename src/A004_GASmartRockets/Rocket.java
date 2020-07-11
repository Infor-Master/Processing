package A004_GASmartRockets;

import processing.core.PVector;

import static processing.core.PApplet.dist;
import static processing.core.PApplet.map;

public class Rocket{
    final float maxvelocity = 4;
    PVector pos;
    PVector vel;
    PVector acc;
    DNA dna;
    float fitness = 0;
    boolean hitTarget = false;
    boolean crashed = false;
    Sketch sketch;

    Rocket(Sketch sketch) {
        this(sketch, new DNA());
    }

    Rocket(Sketch sketch, DNA dna_) {
        this.sketch=sketch;
        pos = new PVector(sketch.getWidth() / (float)2, sketch.getHeight() - 20);
        vel = new PVector();
        acc = new PVector(0, (float) -0.01);
        dna = dna_;
        fitness = 0;
        hitTarget = false;
        crashed = false;
    }

    void applyForce(PVector force) {
        acc.add(force);
    }

    void calcFitness() {
        float d = distanceToTarget();
        fitness = map(d, 0, sketch.getWidth(), sketch.getWidth(), 0);
        if (hitTarget) {
            fitness *= 10;
        } else if (crashed) {
            fitness /= 10;
        }
    }

    float distanceToTarget() {
        return dist(pos.x, pos.y, sketch.getTarget().x, sketch.getTarget().y);
    }

    void update() {
        float d = distanceToTarget();
        if (d < 10) {
            hitTarget = true;
            pos = sketch.getTarget().copy();
        }

        if (pos.x > sketch.getBarrier('x') && pos.x < (sketch.getBarrier('x') + sketch.getBarrier('w')) && pos.y > sketch.getBarrier('y') && pos.y < (sketch.getBarrier('y') + sketch.getBarrier('h'))) {
            crashed = true;
        }
        if (pos.x > sketch.getWidth() || pos.x < 0 || pos.y > sketch.getHeight() || pos.y < 0) {
            crashed = true;
        }

        applyForce(dna.genes[sketch.getAge()]);

        if (!hitTarget && !crashed) {
            vel.add(acc);
            pos.add(vel);
            acc.mult(0);
            vel.limit(maxvelocity);
        }
    }
}
