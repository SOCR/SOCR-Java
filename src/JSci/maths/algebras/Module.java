package JSci.maths.algebras;

import JSci.maths.groups.AbelianGroup;
import JSci.maths.fields.Ring;

/**
* This interface defines a module.
* @planetmath Module
* @version 1.0
* @author Mark Hale
*/
public interface Module extends AbelianGroup {
        /**
        * This interface defines a member of a module.
        */
        interface Member extends AbelianGroup.Member {
                /**
                * The scalar multiplication law.
                * @param r a ring member
                */
                Member scalarMultiply(Ring.Member r);
        }
}

