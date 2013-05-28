package JSci.util;

import JSci.maths.*;

/**
* This is a useful collection of matrix related methods.
* @author Mark Hale
*/
public final class MatrixToolkit {
        private MatrixToolkit() {}

        /**
        * Creates a random generated square matrix.
        */
        public static DoubleSquareMatrix randomSquareMatrix(int size) {
                return (DoubleSquareMatrix)new DoubleSquareMatrix(size).mapElements(RandomMap.MAP);
        }
        /**
        * Creates a random generated tridiagonal matrix.
        */
        public static DoubleTridiagonalMatrix randomTridiagonalMatrix(int size) {
                return (DoubleTridiagonalMatrix)new DoubleTridiagonalMatrix(size).mapElements(RandomMap.MAP);
        }
        /**
        * Creates a random generated diagonal matrix.
        */
        public static DoubleDiagonalMatrix randomDiagonalMatrix(int size) {
                return (DoubleDiagonalMatrix)new DoubleDiagonalMatrix(size).mapElements(RandomMap.MAP);
        }
        /**
        * Creates a random generated square matrix.
        */
        public static ComplexSquareMatrix randomComplexSquareMatrix(int size) {
                return (ComplexSquareMatrix)new ComplexSquareMatrix(size).mapElements(RandomMap.MAP);
        }
        /**
        * Creates a random generated tridiagonal matrix.
        */
        public static ComplexTridiagonalMatrix randomComplexTridiagonalMatrix(int size) {
                return (ComplexTridiagonalMatrix)new ComplexTridiagonalMatrix(size).mapElements(RandomMap.MAP);
        }
        /**
        * Creates a random generated diagonal matrix.
        */
        public static ComplexDiagonalMatrix randomComplexDiagonalMatrix(int size) {
                return (ComplexDiagonalMatrix)new ComplexDiagonalMatrix(size).mapElements(RandomMap.MAP);
        }
        /**
        * Converts a matrix to an array.
        */
        public static double[][] toArray(DoubleMatrix v) {
                double array[][]=new double[v.rows()][v.columns()];
                for(int j,i=0;i<array.length;i++) {
                        for(j=0;j<array[0].length;j++)
                                array[i][j]=v.getElement(i,j);
                }
                return array;
        }
}

