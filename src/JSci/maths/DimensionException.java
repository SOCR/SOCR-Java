package JSci.maths;

/**
* This exception occurs when there is a problem involving an object's dimensions.
* @version 1.1
* @author Mark Hale
*/
public class DimensionException extends IllegalArgumentException {
        /**
        * Constructs a DimensionException with no detail message.
        */
        public DimensionException() {}
        /**
        * Constructs a DimensionException with the specified detail message.
        */
        public DimensionException(String s) {
                super(s);
        }
}

