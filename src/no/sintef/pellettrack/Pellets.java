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

    private double pelletWeight = 0.05; // g
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

    /**
     * Calculate the density matrix for the current pellet distribution.
     * @param density The matrix to fill with density values. The central x-y cell of the matrix
     *                is assumed to be at position. The matrix is not assumed to contain zeroes initially.
     * @param dx The resolution of the density matrix (m)
     */
    public void getDensityMatrix(double[][][] density, double dx) {
        // First fill matrix with zeros:
        for (int i=0; i<density.length; i++)
            for (int j=0; j<density[i].length; j++)
                for (int k=0; k<density[i][j].length; k++)
                    density[i][j][k] = 0;
        // Look up each pellet, and add it to the proper cell:
        double c_x = 0.5*density.length,
                c_y = 0.5*density[0].length,
                c_z = 0.5*density[0][0].length;
        for (double[] pos : pellets) {
            int idx_i = (int)Math.floor(pos[0]/dx + c_x);
            int idx_j = (int)Math.floor(pos[1]/dx + c_y);
            int idx_k = (int)Math.floor(pos[2]/dx);
            if (idx_i >= 0 && idx_j >= 0 && idx_k >= 0 && idx_i < density.length && idx_j < density[0].length &&
                    idx_k < density[0][0].length)
                density[idx_i][idx_j][idx_k] += pelletWeight;
        }
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
