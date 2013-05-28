package JSci.maths;

/**
* This exception occurs when there is a problem involving a vector's dimension.
* For example, accessing a component outside a vector's dimension or performing
* an operation with vectors that have different dimensions.
* @version 1.1
* @author Mark Hale
*/
public final class VectorDimensionException extends DimensionException {
        /**
        * Constructs a VectorDimensionException with no detail message.
        */
        public VectorDimensionException() {}
        /**
        * Constructs a VectorDimensionException with the specified detail message.
        */
        public VectorDimensionException(String s) {
                super(s);
        }
}

