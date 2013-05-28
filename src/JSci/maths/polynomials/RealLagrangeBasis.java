package JSci.maths.polynomials;

import JSci.maths.fields.Field;


/**
 * The Lagrange Basis for real polynomials. For a given set of sampling points {x_1, ..., x_n},
 * the corresponding lagrange polynomials are L_k = \kronecker_kj   \forall j=1..n.
 * The explicit form is
 *       
 * L_k=  \prod_{j=0, j\neq k}^n \frac{t-t_j}{t_k-t_j} 
 *
 * @author  b.dietrich
 */
public class RealLagrangeBasis implements PolynomialBasis {
    protected double[] _samplingsX;
    protected int _dim;
    private RealPolynomial[] _basis;
    private Field.Member[] _samplings;

    /** Creates a new instance of LagrangeBasis for given sampling points*/
    public RealLagrangeBasis( Field.Member[] samplings ) {
        if ( samplings == null ) {
            throw new NullPointerException();
        }
        _dim        = samplings.length;
        _samplings  = samplings;
        _samplingsX = RealPolynomialRing.toDouble( _samplings );
        buildBasis();
    }

    /**
     * Creates a new RealLagrangeBasis object.
     *
     * @param samplings
     */
    public RealLagrangeBasis( double[] samplings ) {
        if ( samplings == null ) {
            throw new NullPointerException();
        }
        _dim        = samplings.length;
        _samplingsX = samplings;
        buildBasis();
    }

    protected RealLagrangeBasis() {
    }

    /**
     * The basis vector as described above
     * @param k
     */
    public Polynomial getBasisVector( int k ) {
        return _basis[k];
    }

    /**
     * The dimension ( # of sampling points)
     * @return the dimension
     *
     */
    public int dimension() {
        return _dim;
    }

    /**
     * The sampling points used in constructor
     * @return the sampling points
     */
    public Field.Member[] getSamplingPoints() {
        if ( _samplings == null ) {
            _samplings = RealPolynomialRing.toMathDouble( _samplingsX );
        }

        return _samplings;
    }

    /**
     * Make a superposition of basis- vectors for a given set of coefficients.
     * Due to the properties of a lagrange base, the result is the interpolating
     * polynomial with values coeff[k] at sampling point k 
     * @param coeff in this case the values of the interpolation problem
     *
     * @return the interpolating polynomial
     *
     */
    public Polynomial superposition(Field.Member[] coeff) {
        if ( coeff == null ) {
            throw new NullPointerException();
        }
        if ( coeff.length != _dim ) {
            throw new IllegalArgumentException( "Dimensions do not match" );
        }

        double[] d = RealPolynomialRing.toDouble( coeff );

        return superposition( d );
    }

    /**
     * Same as above, but type-safe
     */
    public RealPolynomial superposition( double[] c ) {
        if ( c == null ) {
            throw new NullPointerException();
        }
        if ( c.length != _dim ) {
            throw new IllegalArgumentException( "Dimension of basis is " + _dim + ". Got "
                                                + c.length + " coefficients" );
        }

        RealPolynomial rp = (RealPolynomial) RealPolynomialRing.getInstance().zero();
        for ( int k = 0; k < _dim; k++ ) {
            RealPolynomial b  = (RealPolynomial) getBasisVector( k );
            RealPolynomial ba = b.multiply( c[k] );
            rp             = (RealPolynomial) rp.add( ba );
        }

        return rp;
    }

    
    protected void buildBasis() {
        _basis = new RealPolynomial[_dim];
        for ( int k = 0; k < _dim; k++ ) {
            _basis[k] = (RealPolynomial) RealPolynomialRing.getInstance().one();

            double fac = 1.;
            for ( int j = 0; j < _dim; j++ ) {
                if ( j == k ) {
                    continue;
                } else {
                    RealPolynomial n = new RealPolynomial( new double[] { -_samplingsX[j], 1. } );
                    _basis[k] = (RealPolynomial) _basis[k].multiply( n );
                    fac *= ( _samplingsX[k] - _samplingsX[j] );
                }
            }

            _basis[k] = _basis[k].divide( fac );
        }
    }
}
