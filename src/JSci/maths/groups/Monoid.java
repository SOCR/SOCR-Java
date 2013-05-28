package JSci.maths.groups;

/**
* This interface defines a monoid.
* @planetmath Monoid
* @version 1.0
* @author Mark Hale
*/
public interface Monoid {
        /**
        * Returns the identity element.
        */
        Member identity();
        /**
        * Returns true if the member is the identity element of this monoid.
        * @param g a monoid member
        */
        boolean isIdentity(Member g);

        /**
        * This interface defines a member of a monoid.
        */
        interface Member extends Semigroup.Member {}
}

