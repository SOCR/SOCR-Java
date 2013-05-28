package JSci.physics.quantum;

import JSci.maths.*;
import JSci.maths.groups.AbelianGroup;
import JSci.maths.algebras.Module;
import JSci.maths.fields.Ring;

/**
* The KetVector class provides an object for encapsulating Dirac ket vectors.
* @version 1.5
* @author Mark Hale
*/
public final class KetVector extends MathVector {
        private ComplexVector representation;

        /**
        * Constructs a ket vector given a vector representation.
        * @param rep a vector representation
        */
        public KetVector(ComplexVector rep) {
                super(rep.dimension());
                representation=rep;
        }
        /**
        * Compares two ket vectors for equality.
        * @param a a ket vector
        */
        public boolean equals(Object a) {
                return representation.equals(((KetVector)a).representation);
        }
        /**
        * Returns a comma delimited string representing the value of this ket vector.
        */
        public String toString() {
                return representation.toString();
        }
        /**
        * Returns a hashcode for this ket vector.
        */
        public int hashCode() {
                return representation.hashCode();
        }
        /**
        * Map this ket vector to a bra vector.
        */
        public BraVector toBraVector() {
                return new BraVector(representation.conjugate());
        }
        /**
        * Returns the representation.
        */
        public ComplexVector getRepresentation() {
                return representation;
        }
        /**
        * Returns the norm.
        */
        public double norm() {
                return representation.norm();
        }

//============
// OPERATIONS
//============

        /**
        * Returns the negative of this vector.
        */
        public AbelianGroup.Member negate() {
                return representation.negate();
        }

// ADDITION

        /**
        * Returns the addition of this vector and another.
        */
        public AbelianGroup.Member add(AbelianGroup.Member v) {
                if(v instanceof KetVector)
                        return add((KetVector)v);
                else
                        throw new IllegalArgumentException("Vector class not recognised by this method.");
        }
        /**
        * Returns the addition of this vector and another.
        * @param v a ket vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public KetVector add(KetVector v) {
                return new KetVector(representation.add(v.representation));
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this vector by another.
        */
        public AbelianGroup.Member subtract(AbelianGroup.Member v) {
                if(v instanceof KetVector)
                        return subtract((KetVector)v);
                else
                        throw new IllegalArgumentException("Vector class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this vector by another.
        * @param v a ket vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public KetVector subtract(KetVector v) {
                return new KetVector(representation.subtract(v.representation));
        }

// MULTIPLICATION

        /**
        * Returns the multiplication of this ket vector by a scalar.
        */
        public Module.Member scalarMultiply(Ring.Member x) {
                return representation.scalarMultiply(x);
        }
        /**
        * Returns the multiplication of this ket vector and a bra vector.
        * @param bra a bra vector
        * @exception VectorDimensionException If the vectors have different dimensions.
        */
        public Operator multiply(BraVector bra) {
                final int ketDim=dimension();
                if(ketDim==bra.dimension()) {
                        ComplexVector braRep=bra.getRepresentation();
                        Complex array[][]=new Complex[ketDim][ketDim];
                        for(int j,i=0;i<ketDim;i++) {
                                array[i][0]=representation.getComponent(i).multiply(braRep.getComponent(0));
                                for(j=1;j<ketDim;j++)
                                        array[i][j]=representation.getComponent(i).multiply(braRep.getComponent(j));
                        }
                        return new Operator(new ComplexSquareMatrix(array));
                } else
                        throw new VectorDimensionException("Vectors have different dimensions.");
        }
}

