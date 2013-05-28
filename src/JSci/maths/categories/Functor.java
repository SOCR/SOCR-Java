package JSci.maths.categories;

/**
* This interface defines a functor.
* @planetmath Functor
* @version 1.0
* @author Mark Hale
*/
public interface Functor extends Category.Morphism {
        /**
        * Maps an object from one category to another.
        */
        Object map(Object o);
        /**
        * Maps a morphism from one category to another.
        */
        Category.Morphism map(Category.Morphism m);
        /**
        * Returns the composition of this functor with another.
        */
        Functor compose(Functor f);
}

