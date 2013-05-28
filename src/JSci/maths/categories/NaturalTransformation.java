package JSci.maths.categories;

/**
* This interface defines a natural transformation.
* @version 1.0
* @author Mark Hale
*/
public interface NaturalTransformation {
        /**
        * Maps one functor to another.
        */
        Functor map(Functor f);
        /**
        * Returns the vertical composition of this transformation with another.
        */
        NaturalTransformation composeVert(NaturalTransformation n);
        /**
        * Returns the horizontal composition of this transformation with another.
        */
        NaturalTransformation composeHorz(NaturalTransformation n);
}

