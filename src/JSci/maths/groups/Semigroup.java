package JSci.maths.groups;

/**
* This interface defines a semigroup.
* @planetmath Semigroup
* @version 1.0
* @author Mark Hale
*/
public interface Semigroup {

        /**
        * This interface defines a member of a semigroup.
        */
        interface Member extends JSci.maths.Member {
                /**
                * The semigroup composition law.
                * @param g a semigroup member
                */
                Member compose(Member g);
        }
}

