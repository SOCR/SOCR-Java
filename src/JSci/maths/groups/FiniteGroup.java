package JSci.maths.groups;

/**
* Superclass for finite groups.
* @version 1.0
* @author Mark Hale
*/
public abstract class FiniteGroup implements Group {
        protected final int order;

        /**
        * Constructs a finite group.
        * @param n the order of the group
        */
        public FiniteGroup(int n) {
                order = n;
        }
        /**
        * Returns the elements of this group.
        */
        public abstract Group.Member[] getElements();
        /**
        * Returns the order (the number of group elements) of this group.
        * @planetmath OrderGroup
        */
        public final int order() {
                return order;
        }
}

