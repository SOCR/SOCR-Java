package JSci.maths.categories;

import JSci.maths.*;

/**
* The Simplicial class encapsulates the simplicial category.
* @version 1.0
* @author Mark Hale
*/
public final class Simplicial extends Object implements Category {
        public final Bifunctor ADDITION=new Addition();
        /**
        * Constructs a simplicial category.
        */
        public Simplicial() {}
        /**
        * Returns the identity morphism for an object.
        */
        public Category.Morphism identity(Object a) {
                Preorder p=(Preorder)a;
                Integer id[]=new Integer[p.ordinal()];
                for(int i=0;i<id.length;i++)
                        id[i]=new Integer(i);
                return new IncreasingMap(p,id);
        }
        /**
        * Returns the cardinality of an object.
        */
        public Object cardinality(Object a) {
                return new MathInteger(((Preorder)a).ordinal());
        }
        /**
        * Returns a hom-set.
        */
        public Category.HomSet hom(Object a,Object b) {
                return new FunctionSet((Preorder)a,(Preorder)b);
        }
        public Object terminal() {
                return new Preorder(1);
        }
        public class FunctionSet implements Category.HomSet {
                private final Preorder from,to;
                public FunctionSet(Preorder a,Preorder b) {
                        from=a;
                        to=b;
                }
                public int cardinality() {
                        return (int)ExtraMath.binomial(from.ordinal()+to.ordinal()-1,from.ordinal());
                }
        }
        public class IncreasingMap implements Functor {
                protected final Preorder from,to;
                protected final Integer out[];
                public IncreasingMap(Preorder toObj,Integer toImg[]) {
                        from=new Preorder(toImg.length);
                        to=toObj;
                        out=toImg;
                }
                public Object domain() {
                        return from;
                }
                public Object codomain() {
                        return to;
                }
                public Object map(Object o) {
                        return out[((Integer)o).intValue()];
                }
                public Category.Morphism map(Category.Morphism m) {
                        return ((Preorder.RelationSet)to.hom(map(m.domain()),map(m.codomain()))).morphism;
                }
                public Category.Morphism compose(Category.Morphism m) {
                        return compose((Functor)m);
                }
                public Functor compose(Functor f) {
                        if(f instanceof IncreasingMap) {
                                IncreasingMap im=(IncreasingMap)f;
                                if(to.equals(im.from)) {
                                        Integer outImg[]=new Integer[out.length];
                                        for(int i=0;i<outImg.length;i++)
                                                outImg[i]=(Integer)im.map(out[i]);
                                        return new IncreasingMap(im.to,outImg);
                                } else
                                        throw new UndefinedCompositionException();
                        } else
                                throw new IllegalArgumentException("Morphism is not an IncreasingMap.");
                }
        }
        public final class FaceMap extends IncreasingMap {
                private final int skip;
                public FaceMap(Preorder toObj,int i) {
                        super(toObj,new Integer[toObj.ordinal()-1]);
                        skip=i;
                        for(i=0;i<skip;i++)
                                out[i]=new Integer(i);
                        for(;i<out.length;i++)
                                out[i]=new Integer(i+1);
                }
        }
        public final class DegeneracyMap extends IncreasingMap {
                private final int repeat;
                public DegeneracyMap(Preorder toObj,int i) {
                        super(toObj,new Integer[toObj.ordinal()+1]);
                        repeat=i;
                        for(i=0;i<=repeat;i++)
                                out[i]=new Integer(i);
                        for(;i<out.length;i++)
                                out[i]=new Integer(i-1);
                }
        }
        public final class Addition implements Bifunctor {
                public Addition() {}
                public Object map(Object a,Object b) {
                        return new Preorder(((Preorder)a).ordinal()+((Preorder)b).ordinal());
                }
                public Category.Morphism map(Category.Morphism m, Category.Morphism n) {
                        IncreasingMap im=(IncreasingMap)m;
                        IncreasingMap in=(IncreasingMap)n;
                        Preorder to=new Preorder(im.to.ordinal()+in.to.ordinal());
                        Integer toObj[]=new Integer[im.out.length+in.out.length];
                        int i;
                        for(i=0;i<im.out.length;i++)
                                toObj[i]=im.out[i];
                        for(;i<toObj.length;i++)
                                toObj[i]=in.out[i];
                        return new IncreasingMap(to,toObj);
                }
        }
}

