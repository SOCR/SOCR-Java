package JSci.maths.categories;

/**
* This interface defines a bifunctor.
* @version 1.0
* @author Mark Hale
*/
public interface Bifunctor {
        /**
        * Maps a pair of objects from one category to another.
        */
        Object map(Object a,Object b);
        /**
        * Maps a pair of morphisms from one category to another.
        */
        Category.Morphism map(Category.Morphism m, Category.Morphism n);
}

