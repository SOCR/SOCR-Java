package JSci.maths.algebras;

import JSci.maths.fields.Ring;

/**
* This interface defines an algebra.
* @version 1.0
* @author Mark Hale
*/
public interface Algebra {
        /**
        * This interface defines a member of an algebra.
        */
        interface Member extends VectorSpace.Member, Ring.Member {}
}

