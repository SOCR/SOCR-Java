package JSci.maths;

/**
* This exception occurs when there is a problem involving a matrix's dimensions.
* For example, accessing an element outside a matrix's dimensions or performing
* an operation with matrices that have incompatible dimensions.
* @version 1.1
* @author Mark Hale
*/
public final class MatrixDimensionException extends DimensionException {
        /**
        * Constructs a MatrixDimensionException with no detail message.
        */
        public MatrixDimensionException() {
        }
        /**
        * Constructs a MatrixDimensionException with the specified detail message.
        */
        public MatrixDimensionException(String s) {
                super(s);
        }
}

