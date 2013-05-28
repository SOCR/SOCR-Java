package JSci.maths.fields;

import JSci.maths.groups.AbelianGroup;

/**
* This interface defines a ring.
* @planetmath Ring
* @version 1.0
* @author Mark Hale
*/
public interface Ring extends AbelianGroup {
        /**
        * Returns the unit element.
        */
        Member one();
        /**
        * Returns true if the member is the unit element.
        */
        boolean isOne(Member r);

        /**
        * This interface defines a member of a ring.
        */
        interface Member extends AbelianGroup.Member {
                /**
                * The multiplication law.
                * @param r a ring member
                */
                Member multiply(Member r);
        }
}

