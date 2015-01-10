package no.sintef.pellettrack;

/**
 * Created by alver on 09.01.15.
 */
public class Environment {

    double cageDepth = 15;
    double turbKappa = 0.05;

    public double[] getCurrentSpeed(double[] position) {
        return new double[] {0, 0, 0};
    }

    public double[] getTurbulentPerturbation(double[] position) {

        return new double[] {turbKappa*2*(Math.random()-0.5), turbKappa*2*(Math.random()-0.5), turbKappa*2*(Math.random()-0.5)};
    }

    public boolean isInside(double[] position) {
        return (position[2] < cageDepth);
    }
}
