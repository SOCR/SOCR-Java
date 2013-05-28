package JSci.maths.fields;

/**
* This interface defines a field.
* @planetmath Field
* @version 1.0
* @author Mark Hale
*/
public interface Field extends Ring {
        /**
        * Returns true if one member is the inverse of the other.
        */
        boolean isInverse(Member a, Member b);

        /**
        * This interface defines a member of a field.
        */
        interface Member extends Ring.Member {
                /**
                * Returns the inverse member.
                */
                Member inverse();
                /**
                * The multiplication law with inverse.
                * @param f a field member
                */
                Member divide(Member f);
        }
}

