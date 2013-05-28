package JSci.physics.quantum;

import JSci.maths.*;
import JSci.maths.groups.AbelianGroup;
import JSci.maths.algebras.Module;
import JSci.maths.fields.Ring;

/**
* The BraVector class provides an object for encapsulating Dirac bra vectors.
* @version 1.5
* @author Mark Hale
*/
public final class BraVector extends MathVector {
        private ComplexVector representation;

        /**
        * Constructs a bra vector given a vector representation.
        * @param rep a vector representation
        */
        public BraVector(ComplexVector rep) {
                super(rep.dimension());
                representation=rep;
        }
        /**
        * Compares two bra vectors for equality.
        * @param a a bra vector
        */
        public boolean equals(Object a) {
                return representation.equals(((BraVector)a).representation);
        }
        /**
        * Returns a comma delimited string representing the value of this bra vector.
        */
        public String toString() {
                return representation.toString();
        }
        /**
        * Returns a hashcode for this bra vector.
        */
        public int hashCode() {
                return representation.hashCode();
        }
        /**
        * Map this bra vector to a ket vector.
        */
        public KetVector toKetVector() {
                return new KetVector(representation.conjugate());
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
                if(v instanceof BraVector)
                        return add((BraVector)v);
                else
                        throw new IllegalArgumentException("Vector class not recognised by this method.");
        }
        /**
        * Returns the addition of this vector and another.
        * @param v a bra vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public BraVector add(BraVector v) {
                return new BraVector(representation.add(v.representation));
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this vector by another.
        */
        public AbelianGroup.Member subtract(AbelianGroup.Member v) {
                if(v instanceof BraVector)
                        return subtract((BraVector)v);
                else
                        throw new IllegalArgumentException("Vector class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this vector by another.
        * @param v a bra vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public BraVector subtract(BraVector v) {
                return new BraVector(representation.subtract(v.representation));
        }

// MULTIPLICATION

        /**
        * Returns the multiplication of this bra vector by a scalar.
        */
        public Module.Member scalarMultiply(Ring.Member x) {
                return representation.scalarMultiply(x);
        }
        /**
        * Returns the multiplication of this bra vector and a ket vector.
        * @param ket a ket vector
        * @exception VectorDimensionException If the vectors have different dimensions.
        */
        public Complex multiply(KetVector ket) {
                final int braDim=dimension();
                if(braDim==ket.dimension()) {
                        ComplexVector ketRep=ket.getRepresentation();
                        Complex answer=representation.getComponent(0).multiply(ketRep.getComponent(0));
                        for(int i=1;i<braDim;i++)
                                answer=answer.add(representation.getComponent(i).multiply(ketRep.getComponent(i)));
                        return answer;
                } else
                        throw new VectorDimensionException("Vectors have different dimensions.");
        }
        /**
        * Returns the multiplication of this bra vector and an operator.
        * @param op an operator
        * @exception DimensionException If the operator and vector have different dimensions.
        */
        public BraVector multiply(Operator op) {
                final int braDim=dimension();
                if(braDim==op.dimension()) {
                        ComplexSquareMatrix opRep=op.getRepresentation();
                        Complex tmp,array[]=new Complex[braDim];
                        for(int j,i=0;i<braDim;i++) {
                                tmp=representation.getComponent(0).multiply(opRep.getElement(0,i));
                                for(j=1;j<braDim;j++)
                                        tmp=tmp.add(representation.getComponent(j).multiply(opRep.getElement(j,i)));
                                array[i]=tmp;
                        }
                        return new BraVector(new ComplexVector(array));
                } else
                        throw new DimensionException("Operator and vector have different dimensions.");
        }
}

