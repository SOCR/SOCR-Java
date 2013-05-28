package JSci.maths.groups;

/**
* The CyclicGroup class represents the <i>n</i>th cyclic group.
* Elements are represented by the integers mod <i>n</i>.
* @planetmath CyclicGroup
* @version 1.4
* @author Mark Hale
*/
public final class CyclicGroup extends FiniteGroup implements AbelianGroup {
        /**
        * The zero element.
        */
        private final Member ZERO;
        /**
        * Constructs a cyclic group.
        * @param n the order of the group
        */
        public CyclicGroup(int n) {
                super(n);
                ZERO = new Member(0);
        }
        /**
        * Returns true if this group is isomorphic to another.
        */
        public boolean equals(Object o) {
                return (o instanceof CyclicGroup) && (((CyclicGroup)o).order == order);
        }
        public String toString() {
                return "Z_"+order;
        }
        /**
        * Returns an element from the group.
        * @param i the integer representing the element
        */
        public Member getElement(int i) {
                return new Member(i);
        }
        /**
        * Returns the elements of this group.
        */
        public Group.Member[] getElements() {
                Group.Member elements[] = new Group.Member[order];
                for(int i=0; i<order; i++)
                        elements[i] = getElement(i);
                return elements;
        }
        /**
        * Returns the identity element.
        */
        public AbelianGroup.Member zero() {
                return ZERO;
        }
        /**
        * Returns true if the member is the identity element of this group.
        * @param g a group member
        */
        public boolean isZero(AbelianGroup.Member g) {
                return g.equals(ZERO);
        }
        /**
        * Returns true if one member is the negative (inverse) of the other.
        * @param a a group member
        * @param b a group member
        */
        public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
                return (a instanceof Member) && (b instanceof Member) && a.add(b).equals(ZERO);
        }
        /**
        * Returns the identity element.
        */
        public Monoid.Member identity() {
                return ZERO;
        }
        /**
        * Returns true if the member is the identity element of this group.
        * @param g a group member
        */
        public boolean isIdentity(Monoid.Member g) {
                return g.equals(ZERO);
        }
        /**
        * Returns true if one member is the inverse of the other.
        * @param a a group member
        * @param b a group member
        */
        public boolean isInverse(Group.Member a, Group.Member b) {
                return (a instanceof Member) && (b instanceof Member) && a.compose(b).equals(ZERO);
        }
        class Member implements AbelianGroup.Member, Group.Member {
                /** 0 <= i < order */
                private final int i;
                public Member(int x) {
                        i = (x<0) ? x%order+order : x%order;
                }
                /**
                * Returns true if this member is equal to another.
                */
                public boolean equals(Object o) {
                        return (o instanceof Member) && (i == ((Member)o).i);
                }
                /**
                * The group composition law.
                * @param g a group member
                */
                public AbelianGroup.Member add(AbelianGroup.Member g) {
                        return new Member(i+((Member)g).i);
                }
                /**
                * Returns the inverse member.
                */
                public AbelianGroup.Member negate() {
                        return new Member(-i);
                }
                /**
                * The group composition law with inverse.
                * @param g a group member
                */
                public AbelianGroup.Member subtract(AbelianGroup.Member g) {
                        return new Member(i-((Member)g).i);
                }
                /**
                * The group composition law.
                * @param g a group member
                */
                public Semigroup.Member compose(Semigroup.Member g) {
                        return new Member(i+((Member)g).i);
                }
                /**
                * Returns the inverse member.
                */
                public Group.Member inverse() {
                        return new Member(-i);
                }
        }
}

