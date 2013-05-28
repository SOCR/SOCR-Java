package JSci.maths.categories;

import JSci.maths.*;

/**
* The FinSet class encapsulates the category <b>FinSet</b>.
* @version 1.0
* @author Mark Hale
*/
public class FinSet extends Object implements Category {
        /**
        * Constructs a <b>FinSet</b> category.
        */
        public FinSet() {}
        /**
        * Returns the identity morphism for an object.
        */
        public Category.Morphism identity(Object a) {
                return new IdentityFunction((MathSet)a);
        }
        /**
        * Returns the cardinality of an object.
        */
        public Object cardinality(Object a) {
                return new MathInteger(((MathSet)a).cardinality());
        }
        /**
        * Returns a hom-set.
        */
        public Category.HomSet hom(Object a,Object b) {
                return new FunctionSet((MathSet)a,(MathSet)b);
        }
        public class FunctionSet implements MathSet, Category.HomSet {
                private final MathSet from,to;
                private final int size;
                public FunctionSet(MathSet a,MathSet b) {
                        from=a;
                        to=b;
                        size=ExtraMath.pow(b.cardinality(),a.cardinality());
                }
                /**
                * Returns an element of this hom-set.
                */
                public Function getElement(Object in[],Object out[]) {
                        return new Function(from,to,in,out);
                }
                public int cardinality() {
                        return size;
                }
                public MathSet union(MathSet set) {
                        return set.union(this);
                }
                public MathSet intersect(MathSet set) {
                        return set.intersect(this);
                }
        }
        public class Function implements Category.Morphism {
                private final MathSet from,to;
                private final Object in[],out[];
                public Function(MathSet a,MathSet b,Object inObjs[],Object outObjs[]) {
                        from=a;
                        to=b;
                        in=inObjs;
                        out=outObjs;
                }
                public Object domain() {
                        return from;
                }
                public Object codomain() {
                        return to;
                }
                public Object map(Object o) {
                        for(int i=0;i<in.length;i++) {
                                if(o.equals(in[i]))
                                        return out[i];
                        }
                        return null;
                }
                public Category.Morphism compose(Category.Morphism m) {
                        if(m instanceof Function) {
                                Function f=(Function)m;
                                if(to.equals(f.from)) {
                                        Object outObjs[]=new Object[in.length];
                                        for(int i=0;i<outObjs.length;i++)
                                                outObjs[i]=f.map(out[i]);
                                        return new Function(from,f.to,in,outObjs);
                                } else
                                        throw new UndefinedCompositionException();
                        } else
                                throw new IllegalArgumentException("Morphism is not a Function.");
                }
        }
        private class IdentityFunction extends Function {
                public IdentityFunction(MathSet s) {
                        super(s,s,null,null);
                }
                public Object map(Object o) {
                        return o;
                }
                public Category.Morphism compose(Category.Morphism m) {
                        if(m instanceof Function) {
                                return m;
                        } else
                                throw new IllegalArgumentException("Morphism is not a Function.");
                }
        }
}

