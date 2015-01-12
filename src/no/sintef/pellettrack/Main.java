package no.sintef.pellettrack;

import ucar.ma2.ArrayDouble;
import ucar.ma2.DataType;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriteable;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;
import ucar.nc2.stream.NcStreamProto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alver on 09.01.15.
 */
public class Main {

    final static String  FEED_VAR = "feed";

    public static void main(String[] args) throws IOException {
        System.out.println("TEst");

        FileWriter fw = new FileWriter(new File("pellets.csv"));

        double[][][] density = new double[7][7][7];
        double resolution = 2;
        int storeCount = 10000;
        double dt = 1, t0 = 0;
        int nTimes = 200;
        int nPell = 1000;

        NetcdfFileWriter nc = makeNcFile("densities.nc", density);


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

                //writeField(nc, density);
            }
        }
        pellets.getDensityMatrix(density, resolution);
        writeField(nc, density);
        nc.close();
        fw.close();
    }

    public static void writeField(NetcdfFileWriter nc, double[][][] density) throws IOException {

        Variable v = nc.findVariable(FEED_VAR);
        int numTimes = v.getShape(0);
        ArrayDouble A = new ArrayDouble.D4(1, density.length, density[0].length, density[0][0].length);
        Index ima = A.getIndex();
        // First fill matrix with zeros:
        for (int i=0; i<density.length; i++)
            for (int j=0; j<density[i].length; j++)
                for (int k=0; k<density[i][j].length; k++) {
                    A.set(ima.set(0, i, j, k), density[i][j][k]);
                }
        int[] origin = new int[] {numTimes, 0, 0, 0};
        try {
            nc.write(v, origin, A);
        } catch (InvalidRangeException e) {
            e.printStackTrace();
        }
    }

    public static NetcdfFileWriter makeNcFile(String location, double[][][] matrix) throws IOException {
        NetcdfFileWriter nc = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, location, null);
        Dimension td = nc.addUnlimitedDimension("time");

        Dimension xd = nc.addDimension(null, "x", matrix.length);
        Dimension yd = nc.addDimension(null, "y", matrix[0].length);
        Dimension zd = nc.addDimension(null, "z", matrix[0][0].length);
        List<Dimension> dims = new ArrayList<Dimension>();
        dims.add(td);
        dims.add(xd);
        dims.add(yd);
        dims.add(zd);
        Variable t = nc.addVariable(null, FEED_VAR, DataType.DOUBLE, dims);
        nc.create();
        nc.close();
        return NetcdfFileWriter.openExisting(location);
    }

    public static double[] getRandomPos() {
        double[] initPos = new double[3];
        initPos[0] = Math.random()-0.5;
        initPos[1] = Math.random()-0.5;
        initPos[2] = 0;
        return initPos;
    }
}
