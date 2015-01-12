package no.sintef.pellettrack;

import ucar.nc2.NetcdfFileWriteable;
import ucar.nc2.NetcdfFileWriter;

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

        NetcdfFileWriter nc = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, "densities.nc", null);
        double[][][] density = new double[15][15][15];
        double resolution = 1;
        int storeCount = 10000;
        double dt = 1, t0 = 0;
        int nTimes = 200;
        int nPell = 1000;

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
                for (int j=0; j<nPell; j++)
                    pellets.addPellet(getRandomPos());
                System.out.println(pellets.getPelletCount());
            }
            if (i % 50 == 0) {
                pellets.getDensityMatrix(density, resolution);
                
            }
        }

        fw.close();
    }

    public static double[] getRandomPos() {
        double[] initPos = new double[3];
        initPos[0] = Math.random()-0.5;
        initPos[1] = Math.random()-0.5;
        initPos[2] = 0;
        return initPos;
    }
}
