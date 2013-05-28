package JSci.maths.chaos;

import JSci.maths.*;

/**
* The GingerbreadManMap class provides an object that encapsulates the gingerbread man map.
* x<sub>n+1</sub> = 1 - y<sub>n</sub> + |x<sub>n</sub>|
* y<sub>n+1</sub> = x<sub>n</sub>
* (Devaney).
* @version 1.0
* @author Mark Hale
*/
public final class GingerbreadManMap extends Object implements MappingND {
        /**
        * Chaotic x value.
        */
        public final static double X_CHAOS[]={-0.1,0.0};
        /**
        * Constructs a gingerbread man map.
        */
        public GingerbreadManMap() {}
        /**
        * Performs the mapping.
        * @param x a 2-D double array
        * @return a 2-D double array
        */
        public double[] map(double x[]) {
                double ans[]=new double[2];
                ans[0]=1.0-x[1]+Math.abs(x[0]);
                ans[1]=x[0];
                return ans;
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

