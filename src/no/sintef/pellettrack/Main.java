package no.sintef.pellettrack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by alver on 09.01.15.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("TEst");

        FileWriter fw = new FileWriter(new File("pellets.csv"));

        int storeCount = 1000;
        double dt = 1, t0 = 0;
        int nTimes = 200;
        int nPell = 500;

        double[][] initPos = new double[nPell][];
        for (int i=0; i<initPos.length; i++) {
            initPos[i] = getRandomPos();
        }

        Environment env = new Environment();
        Pellets pellets = new Pellets(env, initPos);


        for (int i=0; i<nTimes; i++) {
            double t = i*dt + t0;
            pellets.stepPellets(dt);

            pellets.writePositions(fw, storeCount);

            if (i % 10 == 0) {
                for (int j=0; j<50; j++)
                    pellets.addPellet(getRandomPos());
            }

        }

        fw.close();
    }

    public static double[] getRandomPos() {
        double[] initPos = new double[3];
        initPos[0] = Math.random();
        initPos[1] = Math.random();
        initPos[2] = 0;
        return initPos;
    }
}
