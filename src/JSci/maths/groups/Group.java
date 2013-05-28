package JSci.maths.groups;

/**
* This interface defines a group.
* @planetmath Group
* @version 1.0
* @author Mark Hale
*/
public interface Group extends Monoid {
        /**
        * Returns true if one member is the inverse of the other.
        * @param a a group member
        * @param b a group member
        */
        boolean isInverse(Member a, Member b);

        /**
        * This interface defines a member of a group.
        */
        interface Member extends Monoid.Member {
                /**
                * Returns the inverse member.
                */
                Member inverse();
        }
}

