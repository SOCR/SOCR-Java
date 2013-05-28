package JSci.maths;

/**
* This interface defines a complex map or function.
* It is used to pass user-defined functions to some of
* the other maths classes.
* @see NumericalMath
* @see Mapping
* @see MappingND
* @version 1.1
* @author Mark Hale
*/
public interface ComplexMapping {
        /**
        * A user-defined complex function.
        */
        Complex map(Complex z);
        /**
        * A user-defined complex function.
        */
        Complex map(double real,double imag);
}

