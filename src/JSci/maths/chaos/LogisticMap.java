package JSci.maths.chaos;

import JSci.maths.*;

/**
* The LogisticMap class provides an object that encapsulates the logistic map.
* x<sub>n+1</sub> = r x<sub>n</sub> (1-x<sub>n</sub>)
* @version 1.0
* @author Mark Hale
*/
public final class LogisticMap extends Object implements Mapping {
        private final double r;
        /**
        * 2-cycle bifurcation point.
        */
        public final static double R_2CYCLE=3.0;
        /**
        * 4-cycle bifurcation point.
        */
        public final static double R_4CYCLE=1.0+Math.sqrt(6.0);
        /**
        * 8-cycle bifurcation point.
        */
        public final static double R_8CYCLE=3.544090;
        /**
        * 16-cycle bifurcation point.
        */
        public final static double R_16CYCLE=3.564407;
        /**
        * Accumulation point.
        */
        public final static double R_ACCUMULATION=3.569945672;
        /**
        * Constructs a logistic map.
        * @param rval the value of the r parameter
        */
        public LogisticMap(double rval) {
                r=rval;
        }
        /**
        * Performs the mapping.
        * @param x a double
        */
        public double map(double x) {
                return r*x*(1.0-x);
        }
        public double hausdorffDimension() {
                return 0.538;
        }
        /**
        * Iterates the map.
        * @param n the number of iterations
        * @param x the initial value
        */
        public double iterate(int n,double x) {
                for(int i=0;i<n;i++)
                        x=map(x);
                return x;
        }
}

