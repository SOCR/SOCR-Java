package JSci.maths.algebras;

/**
* This interface defines a C<sup>*</sup>-algebra.
* @planetmath CAlgebra
* @version 1.0
* @author Mark Hale
*/
public interface CStarAlgebra extends Algebra {
        /**
        * This interface defines a member of a C<sup>*</sup>-algebra.
        */
        interface Member extends Algebra.Member, BanachSpace.Member {
                /**
                * The involution operation.
                */
                Member involution();
        }
}

