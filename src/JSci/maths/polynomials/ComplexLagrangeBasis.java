package JSci.maths.polynomials;

import JSci.maths.Complex;
import JSci.maths.fields.Field;


/**
 * A Lagrange base for polynomial over a complex field.
 * For more detail
 * @see JSci.maths.polynomials.RealLagrangeBasis
 * @author  b.dietrich
 */
public class ComplexLagrangeBasis implements PolynomialBasis {
    private ComplexPolynomial[] _basis;
    private Complex[] _samplingsX;
    private int _dim;

    /** Creates a new instance of LagrangeBasis */
    public ComplexLagrangeBasis( Field.Member[] samplings ) {
        if ( samplings == null ) {
            throw new NullPointerException();
        }
        _dim        = samplings.length;
        _samplingsX = ComplexPolynomialRing.toComplex( samplings );
        buildBasis();
    }

    /**
     *
     * @param k
     *
     */
    public Polynomial getBasisVector( int k ) {
        return _basis[k];
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
        return _samplingsX;
    }

    /**
     *
     * @param coeff
     *
     */
    public Polynomial superposition( Field.Member[] coeff ) {
        if ( coeff == null ) {
            throw new NullPointerException();
        }
        if ( coeff.length != _dim ) {
            throw new IllegalArgumentException( "Dimensions do not match" );
        }

        Complex[] d = ComplexPolynomialRing.toComplex( coeff );

        return superposition( d );
    }

    /**
     *
     * @param c
     *
     */
    public ComplexPolynomial superposition( Complex[] c ) {
        if ( c == null ) {
            throw new NullPointerException();
        }
        if ( c.length != _dim ) {
            throw new IllegalArgumentException( "Dimension of basis is " + _dim + ". Got "
                                                + c.length + " coefficients" );
        }

        ComplexPolynomial rp = (ComplexPolynomial) ComplexPolynomialRing.getInstance().zero();
        for ( int k = 0; k < _dim; k++ ) {
            ComplexPolynomial b  = (ComplexPolynomial) getBasisVector( k );
            ComplexPolynomial ba = b.multiply( c[k] );
            rp                   = (ComplexPolynomial) rp.add( ba );
        }

        return rp;
    }

    private void buildBasis() {
        _basis = new ComplexPolynomial[_dim];
        for ( int k = 0; k < _dim; k++ ) {
            _basis[k] = (ComplexPolynomial) ComplexPolynomialRing.getInstance().one();

            Complex fac = Complex.ONE;
            for ( int j = 0; j < _dim; j++ ) {
                if ( j == k ) {
                    continue;
                } else {
                    ComplexPolynomial n = new ComplexPolynomial( 
                                                  new Complex[] {
                        (Complex) _samplingsX[j].negate(), Complex.ONE
                    } );
                    _basis[k] = (ComplexPolynomial) _basis[k].multiply( n );

                    Complex a   = _samplingsX[k];
                    Complex b   = _samplingsX[j];
                    Complex dif = a.subtract( b );
                    fac         = fac.multiply( dif );
                }
            }

            _basis[k] = _basis[k].divide( fac );
        }
    }
}
