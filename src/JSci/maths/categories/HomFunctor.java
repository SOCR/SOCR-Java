package JSci.maths.categories;

/**
* The HomFunctor class encapsulates the hom-bifunctor.
* @version 1.0
* @author Mark Hale
*/
public class HomFunctor extends Object implements Bifunctor {
        private Category cat;
        /**
        * Constructs the hom bifunctor for a category.
        */
        public HomFunctor(Category c) {
                cat=c;
        }
        /**
        * Maps two objects to another.
        */
        public Object map(Object a,Object b) {
                return cat.hom(a, b);
        }
        /**
        * Maps two morphisms to another.
        */
        public Category.Morphism map(Category.Morphism m, Category.Morphism n) {
                return new HomMorphism(m, n);
        }
        private class HomMorphism implements Category.Morphism {
                private Category.Morphism in,out;
                public HomMorphism(Category.Morphism m, Category.Morphism n) {
                        in=m;
                        out=n;
                }
                public Object domain() {
                        return cat.hom(in.codomain(), out.domain());
                }
                public Object codomain() {
                        return cat.hom(in.domain(), out.codomain());
                }
                public Object map(Object o) {
                        return in.compose((Category.Morphism)o).compose(out);
                }
                public Category.Morphism compose(Category.Morphism m) {
                        HomMorphism hm=(HomMorphism)m;
                        return new HomMorphism(hm.in.compose(in), out.compose(hm.out));
                }
        }
}

