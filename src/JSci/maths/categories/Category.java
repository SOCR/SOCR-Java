package JSci.maths.categories;

/**
* This interface defines a category.
* @planetmath Category
* @version 1.0
* @author Mark Hale
*/
public interface Category {
        /**
        * Returns the identity morphism for an object.
        */
        Morphism identity(Object a);
        /**
        * Returns the cardinality of an object.
        * In general, this may not be an Integer.
        */
        Object cardinality(Object a);
        /**
        * Returns a hom-set.
        */
        HomSet hom(Object a, Object b);

        /**
        * This interface defines a morphism in a category.
        */
        interface Morphism {
                /**
                * Returns the domain.
                */
                Object domain();
                /**
                * Returns the codomain.
                */
                Object codomain();
                /**
                * Maps an object from the domain to the codomain.
                */
                Object map(Object o);
                /**
                * Returns the composition of this morphism with another.
                */
                Morphism compose(Morphism m) throws UndefinedCompositionException;
        }

        /**
        * This interface defines a hom-set.
        */
        interface HomSet {}
}

