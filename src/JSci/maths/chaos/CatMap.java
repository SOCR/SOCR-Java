package JSci.maths.chaos;

import JSci.maths.*;

/**
* The CatMap class provides an object that encapsulates the cat map.
* x<sub>n+1</sub> = (x<sub>n</sub> + y<sub>n</sub>) mod 1
* y<sub>n+1</sub> = (x<sub>n</sub> + 2y<sub>n</sub>) mod 1
* (Arnol'd).
* @version 1.0
* @author Mark Hale
*/
public final class CatMap extends Object implements MappingND {
        /**
        * Constructs a cat map.
        */
        public CatMap() {}
        /**
        * Performs the mapping.
        * @param x a 2-D double array
        * @return a 2-D double array
        */
        public double[] map(double x[]) {
                double ans[]=new double[2];
                ans[0]=(x[0]+x[1])%1;
                ans[1]=(x[0]+2*x[1])%1;
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

