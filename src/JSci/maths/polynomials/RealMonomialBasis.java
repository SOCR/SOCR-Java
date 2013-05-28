package JSci.maths.polynomials;

import JSci.maths.fields.Field;

/**
 *
 * @author  b.dietrich
 */
public class RealMonomialBasis implements PolynomialBasis {
    private RealPolynomial[] _basis;
    private int _dim;

    /** Creates a new instance of RealMonomialBasis */
    public RealMonomialBasis( int dim ) {
        _dim  = dim;
        _basis = new RealPolynomial[dim];
    }

    /**
     *
     * @param k
     *
     * @return a basis vector
     */
    public Polynomial getBasisVector( int k ) {
        if ( k >= _dim ) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            if ( _basis[k] == null ) {
                synchronized ( this ) {
                    if ( _basis[k] == null ) {
                        double[] db = new double[_dim];
                        db[k]    = 1.;
                        _basis[k] = new RealPolynomial( db );
                    }
                }
            }

            return _basis[k];
        }
    }

    /**
     *
     * @return the dimension of this basis
     */
    public int dimension() {
        return _dim;
    }

    /**
     *
     */
    public Field.Member[] getSamplingPoints() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param coeff
     *
     */
    public Polynomial superposition(Field.Member[] coeff) {
        return superposition( RealPolynomialRing.toDouble( coeff ) );
    }

    /**
     *
     * @param d
     */
    public RealPolynomial superposition( double[] d ) {
        if ( d == null ) {
            throw new NullPointerException();
        }
        if ( d.length != _dim ) {
            throw new IllegalArgumentException( "Dimension of basis is " + _dim + ". Got "
                                                + d.length + " coefficients" );
        }

        return new RealPolynomial( d );
    }
}
