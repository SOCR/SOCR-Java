package JSci.maths;

/**
* This exception occurs when a numerical algorithm exceeds it maximum number of allowable iterations.
* @version 1.0
* @author Mark Hale
*/
public final class MaximumIterationsExceededException extends Exception {
        /**
        * Constructs a MaximumIterationsExceededException with no detail message.
        */
        public MaximumIterationsExceededException() {}
        /**
        * Constructs a MaximumIterationsExceededException with the specified detail message.
        */
        public MaximumIterationsExceededException(String s) {
                super(s);
        }
}

