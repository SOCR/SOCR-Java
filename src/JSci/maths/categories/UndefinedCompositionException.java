package JSci.maths.categories;

/**
* This exception occurs when trying to compose two morphisms
* whose composition is undefined.
* @version 1.0
* @author Mark Hale
*/
public final class UndefinedCompositionException extends RuntimeException {
        /**
        * Constructs a UndefinedCompositionException with no detail message.
        */
        public UndefinedCompositionException() {}
        /**
        * Constructs a UndefinedCompositionException with the specified detail message.
        */
        public UndefinedCompositionException(String s) {
                super(s);
        }
}

