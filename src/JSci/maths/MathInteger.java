package JSci.maths;

import JSci.GlobalSettings;
import JSci.maths.groups.AbelianGroup;
import JSci.maths.fields.*;

/**
* The MathInteger class encapsulates integer numbers.
* @see JSci.maths.fields.IntegerRing
* @version 1.0
* @author Mark Hale
*/
public final class MathInteger extends Object implements Comparable, Ring.Member {
        private static final long serialVersionUID = 6893485894391864141L;

        private final int x;
        /**
        * Constructs an integer number.
        */
        public MathInteger(final int num) {
                x=num;
        }
        /**
        * Constructs the integer number represented by a string.
        * @param s a string representing an integer number.
        * @exception NumberFormatException if the string does not contain a parsable number.
        */
        public MathInteger(final String s) throws NumberFormatException {
                x=Integer.parseInt(s);
        }
        /**
        * Compares two integer numbers for equality.
        * @param obj an integer number.
        */
        public boolean equals(Object obj) {
                if(obj!=null && (obj instanceof MathInteger))
                        return x==((MathInteger)obj).value();
                else
                        return false;
        }
        /**
        * Compares two integer numbers.
        * @param obj an integer number.
        * @return a negative value if <code>this&lt;obj</code>,
        * zero if <code>this==obj</code>,
        * and a positive value if <code>this&gt;obj</code>.
        */
        public int compareTo(Object obj) throws IllegalArgumentException {
                if(obj!=null && (obj instanceof MathInteger)) {
                        if(x==((MathInteger)obj).value())
                                return 0;
                        else
                                return x-((MathInteger)obj).value();
                } else
                        throw new IllegalArgumentException("Invalid object.");
        }
        /**
        * Returns a string representing the value of this integer number.
        */
        public String toString() {
                return Integer.toString(x);
        }
        /**
        * Returns the integer value.
        */
        public int value() {
                return x;
        }
        /**
        * Returns true if this number is even.
        */
        public boolean isEven() {
                return (x&1)==0;
        }
        /**
        * Returns true if this number is odd.
        */
        public boolean isOdd() {
                return (x&1)==1;
        }
        /**
        * Returns the negative of this number.
        */
        public AbelianGroup.Member negate() {
                return new MathInteger(-x);
        }
        /**
        * Returns the addition of this number and another.
        */
        public AbelianGroup.Member add(final AbelianGroup.Member n) {
                if(n instanceof MathInteger)
                        return add((MathInteger)n);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the addition of this integer number and another.
        */
        public MathInteger add(final MathInteger n) {
                return new MathInteger(x+n.x);
        }
        /**
        * Returns the subtraction of this number and another.
        */
        public AbelianGroup.Member subtract(final AbelianGroup.Member n) {
                if(n instanceof MathInteger)
                        return subtract((MathInteger)n);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this integer number and another.
        * @param n an integer number.
        */
        public MathInteger subtract(final MathInteger n) {
                return new MathInteger(x-n.x);
        }
        /**
        * Returns the multiplication of this number and another.
        */
        public Ring.Member multiply(final Ring.Member n) {
                if(n instanceof MathInteger)
                        return multiply((MathInteger)n);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this integer number and another.
        * @param n an integer number.
        */
        public MathInteger multiply(final MathInteger n) {
                return new MathInteger(x*n.x);
        }
        /**
        * Returns this integer number raised to the power of another.
        * @param n an integer number.
        */
        public MathInteger pow(final MathInteger n) {
                if(n.x==0)
                        return IntegerRing.ONE;
                else {
                        int ans=x;
                        for(int i=1;i<n.x;i++)
                                ans*=x;
                        return new MathInteger(ans);
                }
        }
}

