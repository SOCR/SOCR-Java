package JSci.maths.algebras;

/**
* This interface defines a Banach space.
* @planetmath BanachSpace
* @version 1.0
* @author Mark Hale
*/
public interface BanachSpace extends VectorSpace {
        /**
        * This interface defines a member of a Banach Space.
        */
        interface Member extends VectorSpace.Member {
                /**
                * Returns the norm.
                */
                double norm();
        }
}

