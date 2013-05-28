package JSci.maths;

/**
* This interface defines a set.
* @planetmath Set
* @version 1.2
* @author Mark Hale
*/
public interface MathSet {
        /**
        * Returns the cardinality.
        */
        int cardinality();
        /**
        * Performs the union of this set with another.
        * @param set a set.
        * @return the union of the two sets.
        */
        MathSet union(MathSet set);
        /**
        * Performs the intersection of this set with another.
        * @param set a set.
        * @return the intersection of the two sets.
        */
        MathSet intersect(MathSet set);
}

