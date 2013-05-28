package JSci.util;

import JSci.maths.*;

/**
* This is a useful collection of vector related methods.
* @author Mark Hale
*/
public final class VectorToolkit {
        private VectorToolkit() {}

        /**
        * Creates a random generated vector.
        */
        public static DoubleVector randomVector(int size) {
                return new DoubleVector(size).mapComponents(RandomMap.MAP);
        }
        /**
        * Creates a random generated vector.
        */
        public static ComplexVector randomComplexVector(int size) {
                return new ComplexVector(size).mapComponents(RandomMap.MAP);
        }
        /**
        * Converts a vector to an array.
        */
        public static double[] toArray(DoubleVector v) {
                double array[]=new double[v.dimension()];
                array[0]=v.getComponent(0);
                for(int i=1;i<array.length;i++)
                        array[i]=v.getComponent(i);
                return array;
        }
}

