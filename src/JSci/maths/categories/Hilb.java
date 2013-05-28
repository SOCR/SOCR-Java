package JSci.maths.categories;

import JSci.maths.*;
import JSci.maths.algebras.*;
import JSci.maths.fields.*;
import JSci.maths.groups.AbelianGroup;

/**
* The Hilb class encapsulates the category <b>Hilb</b>.
* @version 1.0
* @author Mark Hale
*/
public class Hilb extends Object implements Category {
        /**
        * Constructs a <b>Hilb</b> category.
        */
        public Hilb() {}
        /**
        * Returns the identity morphism for an object.
        * @param a a HilbertSpace.
        */
        public Category.Morphism identity(Object a) {
                return new LinearMap(ComplexDiagonalMatrix.identity(((HilbertSpace)a).dimension()));
        }
        /**
        * Returns the cardinality of an object.
        * @param a a HilbertSpace.
        */
        public Object cardinality(Object a) {
                return new MathInteger(((HilbertSpace)a).dimension());
        }
        /**
        * Returns a hom-set.
        * @param a a HilbertSpace.
        * @param b a HilbertSpace.
        * @return a HilbertSpace.
        */
        public Category.HomSet hom(Object a,Object b) {
                return new OperatorSpace((HilbertSpace)a,(HilbertSpace)b);
        }
        public class OperatorSpace extends HilbertSpace implements Category.HomSet {
                private final int rows,cols;
                public OperatorSpace(HilbertSpace a,HilbertSpace b) {
                        super(a.dimension()*b.dimension());
                        rows=b.dimension();
                        cols=a.dimension();
                }
                /**
                * Returns an element of this hom-set.
                */
                public VectorSpace.Member getVector(Complex array[][]) {
                        return new LinearMap(array);
                }
        }
        public class LinearMap implements BanachSpace.Member, Category.Morphism {
                private ComplexMatrix matrix;
                public LinearMap(Complex array[][]) {
                        matrix=new ComplexMatrix(array);
                }
                public LinearMap(ComplexMatrix m) {
                        matrix=m;
                }
                public Object domain() {
                        return new HilbertSpace(matrix.columns());
                }
                public Object codomain() {
                        return new HilbertSpace(matrix.rows());
                }
                public Object map(Object v) {
                        return matrix.multiply((ComplexVector)v);
                }
                public Category.Morphism compose(Category.Morphism m) {
                        if(m instanceof LinearMap) {
                                LinearMap lm=(LinearMap)m;
                                if(matrix.columns()==lm.matrix.rows())
                                        return new LinearMap(lm.matrix.multiply(matrix));
                                else
                                        throw new UndefinedCompositionException();
                        } else
                                throw new IllegalArgumentException("Morphism is not a LinearMap.");
                }
                public double norm() {
                        return matrix.frobeniusNorm();
                }
                public int dimension() {
                        return matrix.rows()*matrix.columns();
                }
                public AbelianGroup.Member add(final AbelianGroup.Member m) {
                        if(m instanceof LinearMap)
                                return new LinearMap(matrix.add(((LinearMap)m).matrix));
                        else
                                throw new IllegalArgumentException("Member class not recognised by this method.");
                }
                public AbelianGroup.Member negate() {
                        return new LinearMap((ComplexMatrix)matrix.negate());
                }
                public AbelianGroup.Member subtract(final AbelianGroup.Member m) {
                        if(m instanceof LinearMap)
                                return new LinearMap(matrix.subtract(((LinearMap)m).matrix));
                        else
                                throw new IllegalArgumentException("Member class not recognised by this method.");
                }
                public Module.Member scalarMultiply(Ring.Member z) {
                        if(z instanceof Complex)
                                return new LinearMap(matrix.scalarMultiply((Complex)z));
                        else
                                throw new IllegalArgumentException("Member class not recognised by this method.");
                }
                public VectorSpace.Member scalarDivide(Field.Member z) {
                        if(z instanceof Complex)
                                return new LinearMap(matrix.scalarMultiply((Complex)z));
                        else
                                throw new IllegalArgumentException("Member class not recognised by this method.");
                }
        }
}

