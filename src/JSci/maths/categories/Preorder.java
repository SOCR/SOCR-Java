package JSci.maths.categories;

/**
* The Preorder class encapsulates preorders as categories.
* @version 1.0
* @author Mark Hale
*/
public final class Preorder extends Object implements Category {
        private final int N;
        /**
        * Constructs a preorder category.
        */
        public Preorder(int n) {
                N=n;
        }
        /**
        * Returns the identity morphism for an object.
        * @param a an Integer.
        */
        public Category.Morphism identity(Object a) {
                return new Relation((Integer)a, (Integer)a);
        }
        /**
        * Returns the cardinality of an object.
        * @param a an Integer.
        * @return an Integer.
        */
        public Object cardinality(Object a) {
                return a;
        }
        /**
        * Returns a hom-set.
        */
        public Category.HomSet hom(Object a,Object b) {
                final Integer i=(Integer)a;
                final Integer j=(Integer)b;
                if(i.compareTo(j)<=0)
                        return new RelationSet(i,j);
                else
                        return new RelationSet();
        }
        public Object initial() {
                return new Integer(0);
        }
        public Object terminal() {
                return new Integer(N-1);
        }
        /**
        * Returns the ordinal that this category represents.
        */
        public int ordinal() {
                return N;
        }
        public class RelationSet implements Category.HomSet {
                public final Relation morphism;
                public RelationSet() {
                        morphism=null;
                }
                public RelationSet(Integer a,Integer b) {
                        morphism=new Relation(a,b);
                }
        }
        public class Relation implements Category.Morphism {
                private final Integer from,to;
                public Relation(Integer a,Integer b) {
                        from=a;
                        to=b;
                }
                public Object domain() {
                        return from;
                }
                public Object codomain() {
                        return to;
                }
                public Object map(Object o) {
                        return to;
                }
                public Category.Morphism compose(Category.Morphism m) {
                        if(m instanceof Relation) {
                                Relation r=(Relation)m;
                                if(to.equals(r.from))
                                        return new Relation(from,r.to);
                                else
                                        throw new UndefinedCompositionException();
                        } else
                                throw new IllegalArgumentException("Morphism is not a Relation.");
                }
        }
}

