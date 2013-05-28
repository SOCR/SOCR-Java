package JSci.maths.groups;

/**
* This interface defines an abelian group.
* @version 1.0
* @author Mark Hale
*/
public interface AbelianGroup {
        /**
        * Returns the identity element.
        */
        Member zero();
        /**
        * Returns true if the member is the identity element of this group.
        * @param g a group member
        */
        boolean isZero(Member g);
        /**
        * Returns true if one member is the negative of the other.
        * @param a a group member
        * @param b a group member
        */
        boolean isNegative(Member a, Member b);

        /**
        * This interface defines a member of an abelian group.
        */
        interface Member extends JSci.maths.Member {
                /**
                * The group composition law.
                * @param g a group member
                */
                Member add(Member g);
                /**
                * Returns the inverse member.
                */
                Member negate();
                /**
                * The group composition law with inverse.
                * @param g a group member
                */
                Member subtract(Member g);
        }
}

