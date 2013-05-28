package JSci.maths;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
* A set containing a finite number of elements.
* This class provides a bridge between <code>java.util.Set</code>
* and <code>JSci.maths.MathSet</code>.
* @version 1.0
* @author Mark Hale
*/
public final class FiniteSet extends Object implements MathSet {
        private final Set elements;
        /**
        * Constructs a finite set.
        * @param set a set of elements
        */
        public FiniteSet(Set set) {
                elements = set;
        }
        /**
        * Compares two sets for equality.
        */
        public boolean equals(Object s) {
                return (s != null) && (s instanceof FiniteSet) && elements.equals(((FiniteSet)s).elements);
        }
        /**
        * Returns a string representing this set.
        */
        public String toString() {
                return elements.toString();
        }
        /**
        * Returns the elements of this set.
        */
        public Set getElements() {
                return Collections.unmodifiableSet(elements);
        }
        /**
        * Returns the cardinality.
        */
        public int cardinality() {
                return elements.size();
        }
        /**
        * Performs the union of this set with another.
        * @param set a set.
        * @return the union of the two sets.
        */
        public MathSet union(MathSet set) {
                Set union = new HashSet(elements);
                union.addAll(((FiniteSet)set).elements);
                return new FiniteSet(union);
        }
        /**
        * Performs the intersection of this set with another.
        * @param set a set.
        * @return the intersection of the two sets.
        */
        public MathSet intersect(MathSet set) {
                Set intersection = new HashSet(elements);
                intersection.retainAll(((FiniteSet)set).elements);
                return new FiniteSet(intersection);
        }
}

