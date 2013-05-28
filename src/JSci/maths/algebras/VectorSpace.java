package JSci.maths.algebras;

import JSci.maths.fields.Field;

/**
* This interface defines a vector space.
* @planetmath VectorSpace
* @version 1.0
* @author Mark Hale
*/
public interface VectorSpace extends Module {
        /**
        * This interface defines a member of a vector space, i.e. a vector.
        * @planetmath Vector
        */
        interface Member extends Module.Member {
                /**
                * The scalar multiplication law with inverse.
                * @param f a field member
                */
                Member scalarDivide(Field.Member f);
        }
}

