package no.sintef.pellettrack;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by alver on 09.01.15.
 */
public class Pellets {

    private ArrayList<double[]> pellets;
    private Environment env;

    private double sinkingSpeed = 0.1; // m/s

    public Pellets(Environment env, double[][] positions) {
        this.env = env;
        this.pellets = new ArrayList<double[]>(positions.length);
        for (int i=0; i<positions.length; i++) {
            pellets.add(positions[i]);
        }

    }

    public void addPellet(double[] position) {
        pellets.add(position);
    }

    public void stepPellets(double dt) {

        for (double[] pos : pellets) {
            double[] speed = env.getCurrentSpeed(pos);
            double[] perturbation = env.getTurbulentPerturbation(pos);
            speed[0] = speed[0] + perturbation[0];
            speed[1] = speed[1] + perturbation[1];
            speed[2] = speed[2] + perturbation[2] + sinkingSpeed;
            speed[2] += sinkingSpeed;

            // Update position:
            pos[0] = pos[0] + dt*speed[0];
            pos[1] = pos[1] + dt*speed[1];
            pos[2] = pos[2] + dt*speed[2];

        }

        checkRemoval(); // Sjekk om denne burde kalles bare hvert n-te tidsskritt?
    }

    public void checkRemoval() {
        for (Iterator<double[]> i = pellets.iterator(); i.hasNext();) {
            double[] pos = i.next();
            if (!env.isInside(pos))
                i.remove();
        }
    }

    public double[] getPosition(int pellet) {
        return pellets.get(pellet);
    }

    public int getPelletCount() {
        return pellets.size();
    }

    public void writePositions(Writer out, int colCount) throws IOException {
        int count = 0;
        for (double[] pos : pellets) {
            out.write(String.valueOf(pos[0])+"\t"+String.valueOf(pos[1])+"\t"+String.valueOf(pos[2])+"\t");
            count++;
            if (count == colCount)
                break;
        }
        while (count < colCount) {
            out.write("\t\t\t");
            count++;
        }
        out.write('\n');
    }

}
