package JSci.maths.chaos;

import JSci.maths.*;

/**
* The HenonMap class provides an object that encapsulates the Henon map.
* x<sub>n+1</sub> = 1 - a x<sub>n</sub><sup>2</sup> + y<sub>n</sub>
* y<sub>n+1</sub> = b x<sub>n</sub>
* @version 1.0
* @author Mark Hale
*/
public final class HenonMap extends Object implements MappingND {
        private final double a;
        private final double b;
        /**
        * Chaotic a parameter value.
        */
        public final static double A_CHAOS=1.4;
        /**
        * Chaotic b parameter value.
        */
        public final static double B_CHAOS=0.3;
        /**
        * Constructs a Henon map.
        * @param aval the value of the a parameter
        * @param bval the value of the b parameter
        */
        public HenonMap(double aval,double bval) {
                a=aval;
                b=bval;
        }
        /**
        * Performs the mapping.
        * @param x a 2-D double array
        * @return a 2-D double array
        */
        public double[] map(double x[]) {
                double ans[]=new double[2];
                ans[0]=1.0-a*x[0]*x[0]+x[1];
                ans[1]=b*x[0];
                return ans;
        }
        public double hausdorffDimension() {
                return 1.26;
        }
        /**
        * Iterates the map.
        * @param n the number of iterations
        * @param x the initial values (2-D)
        * @return a 2-D double array
        */
        public double[] iterate(int n,double x[]) {
                double xn[]=map(x);
                for(int i=1;i<n;i++)
                        xn=map(xn);
                return xn;
        }
}

