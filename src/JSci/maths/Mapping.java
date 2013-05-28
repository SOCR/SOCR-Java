package JSci.maths;

/**
* This interface defines a map or function.
* It is used to pass user-defined functions to some of
* the other maths classes.
* @see NumericalMath
* @see ComplexMapping
* @see MappingND
* @version 1.1
* @author Mark Hale
*/
public interface Mapping {
        /**
        * A user-defined function.
        */
        double map(double x);
}

