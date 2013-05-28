package JSci.maths.groups;

/**
* The QuaternionGroup class represents the quaternion group.
* @planetmath QuaternionGroup
* @version 1.0
* @author Mark Hale
*/
public final class QuaternionGroup extends FiniteGroup {
        private static QuaternionGroup _instance;

        /**
        * The identity element.
        */
        private final Member ONE;
        private QuaternionGroup() {
                super(8);
                ONE = new Member(1);
        }
        /**
        * Constructs the quaternion group.
        * Singleton.
        */
        public static final QuaternionGroup getInstance() {
                if(_instance == null) {
                        synchronized(QuaternionGroup.class) {
                                if(_instance == null)
                                        _instance = new QuaternionGroup();
                        }
                }
                return _instance;
        }
        public String toString() {
                return "Q";
        }
        /**
        * Returns the elements of this group.
        */
        public Group.Member[] getElements() {
                return new Group.Member[] {new Member(+1), new Member(+2), new Member(+3), new Member(+4),
                                                        new Member(-1), new Member(-2), new Member(-3), new Member(-4)};
        }
        /**
        * Returns the identity element.
        */
        public Monoid.Member identity() {
                return ONE;
        }
        /**
        * Returns true if the element is the identity element of this group.
        * @param a a group element
        */
        public boolean isIdentity(Monoid.Member a) {
                return a.equals(ONE);
        }
        /**
        * Returns true if one element is the inverse of the other.
        * @param a a group element
        * @param b a group element
        */
        public boolean isInverse(Group.Member a, Group.Member b) {
                return (a instanceof Member) && (b instanceof Member) && a.compose(b).equals(ONE);
        }
        /** Multiplication table */
        private static final int multTable[][] = {{1,2,3,4}, {2,-1,4,-3}, {3,-4,-1,2}, {4,3,-2,-1}};
        class Member implements Group.Member {
                /** 1 = '1', 2 = 'i', 3 = 'j', 4 = 'k' */
                private final int unit;

                public Member(int e) {
                        if(e<-4 || e==0 || e>4)
                                throw new IllegalArgumentException();
                        unit = e;
                }
                /**
                * Returns true if this member is equal to another.
                */
                public boolean equals(Object o) {
                        return (o instanceof Member) && (unit == ((Member)o).unit);
                }
                /**
                * The group composition law.
                * @param g a group member
                */
                public Semigroup.Member compose(Semigroup.Member g) {
                        int gunit = ((Member)g).unit;
                        if(unit>0 && gunit>0)
                                return new Member(multTable[unit][gunit]);
                        else if(unit>0 && gunit<0)
                                return new Member(-multTable[unit][-gunit]);
                        else if(unit<0 && gunit>0)
                                return new Member(-multTable[-unit][gunit]);
                        else
                                return new Member(multTable[-unit][-gunit]);
                }
                /**
                * Returns the inverse member.
                */
                public Group.Member inverse() {
                        return new Member(-unit);
                }
        }
}

