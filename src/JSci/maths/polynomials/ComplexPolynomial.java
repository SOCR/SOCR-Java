package JSci.maths.polynomials;

import JSci.maths.Complex;
import JSci.maths.MathDouble;
import JSci.maths.fields.*;
import JSci.maths.fields.Field;
import JSci.maths.groups.*;


/**
 * A Polynomial over the complex field.
 * For a description of the methods
 * @see JSci.maths.polynomials.RealPolynomial
 *
 * @author  b.dietrich
 */
public class ComplexPolynomial implements Polynomial {
    private Complex[] _c;

    /** Creates a new instance of ComplexPolynomial */
    public ComplexPolynomial( Complex[] coeff ) {
        if ( coeff == null ) {
            throw new NullPointerException();
        }
        if ( coeff.length == 0 ) {
            _c = new Complex[] { Complex.ZERO };
        } else {
            _c = coeff;
        }
    }

    /**
     * Creates a new ComplexPolynomial object.
     *
     * @param f
     */
    public ComplexPolynomial( Field.Member[] f ) {
        _c = ComplexPolynomialRing.toComplex( f );
    }

    /**
     *
     * @param n
     */
    public Field.Member getCoefficient( int n ) {
        return getCoefficientAsComplex( n );
    }

    /**
     *
     * @param n
     */
    public Complex getCoefficientAsComplex( int n ) {
        if ( n >= _c.length ) {
            return Complex.ZERO;
        } else {
            return _c[n];
        }
    }

    /**
     */
    public Field.Member[] getCoefficients() {
        return getCoefficientsAsComplexes();
    }

    /**
     * Return the coefficients as an array of complex numbers.
     */
    public Complex[] getCoefficientsAsComplexes() {
        return _c;
    }

    /**
     *
     * @return the degree
     */
    public int degree() {
        return _c.length;
    }

    /**
     *
     * @return true if null
     */
    public boolean isNull() {
        boolean res = true;
        for ( int k = 0; res && ( k < degree() ); k++ ) {
            if ( getCoefficientAsComplex( k ).norm() > ( POLYEPS * 2.0 ) ) {
                res = false;
            }
        }

        return res;
    }

    /**
     *
     * @return true if this is equal to one.
     */
    public boolean isOne() {
        boolean res = getCoefficientAsComplex( 0 ).subtract( Complex.ONE ).norm() <= ( POLYEPS * 2.0 );

        for ( int k = 1; res && ( k < degree() ); k++ ) {
            if ( getCoefficientAsComplex( k ).norm() > ( 2.0 * POLYEPS ) ) {
                res = false;
            }
        }

        return res;
    }

    /** The group composition law.
     * @param g a group member
     *
     */
    public AbelianGroup.Member add( AbelianGroup.Member g ) {
        AbelianGroup.Member result = null;
        if ( g instanceof ComplexPolynomial ) {
            ComplexPolynomial p = (ComplexPolynomial) g;
            int maxgrade        = PolynomialMath.maxDegree( this, p );
            Complex[] c         = new Complex[maxgrade];
            for ( int k = 0; k < maxgrade; k++ ) {
                c[k] = getCoefficientAsComplex( k ).add( p.getCoefficientAsComplex( k ) );
            }
            result = new ComplexPolynomial( c );
        } else {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    /**
     * Returns the division of this polynomial by a scalar.
     * @param a
     *
     */
    public Polynomial divide( Field.Member f ) {
        if ( f instanceof Complex ) {
            return divide( (Complex) f );
        } else if ( f instanceof MathDouble ) {
            return divide( ( (MathDouble) f ).value() );
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the division of this polynomial by a scalar.
     * @param a
     */
    public ComplexPolynomial divide( Complex a ) {
        Complex[] c = new Complex[_c.length];
        for ( int k = 0; k < c.length; k++ ) {
            c[k] = _c[k].divide( a );
        }

        return new ComplexPolynomial( c );
    }

    /**
     * Returns the division of this polynomial by a scalar.
     * @param a
     */
    public ComplexPolynomial divide( double a ) {
        Complex[] c = new Complex[_c.length];
        for ( int k = 0; k < c.length; k++ ) {
            c[k] = _c[k].divide( a );
        }

        return new ComplexPolynomial( c );
    }

    /**
     *
     * @param o
     */
    public boolean equals( Object o ) {
        boolean result = false;
        if ( o == this ) {
            result = true;
        } else if ( o instanceof ComplexPolynomial ) {
            ComplexPolynomial p = (ComplexPolynomial) o;
            result = true;
            for ( int k = 0; result && ( k < degree() ); k++ ) {
                if ( p.getCoefficientAsComplex( k ).subtract( getCoefficientAsComplex( k ) ).norm() > ( 2 * POLYEPS ) ) {
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     *
     */
    public int hashCode() {
        int res = 0;
        for ( int k = 0; k < degree(); k++ ) {
            res += (int) ( getCoefficientAsComplex( k ).norm() * 10.0 );
        }

        return res;
    }

    /**
     * Returns the multiplication of this polynomial by a scalar.
     * @param f
     */
    public Polynomial multiply( Field.Member f ) {
        if ( f instanceof MathDouble ) {
            double a = ( (MathDouble) f ).value();

            return multiply( a );
        } else if ( f instanceof Complex ) {
            return multiply( (Complex) f );
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the multiplication of this polynomial by a scalar.
     * @param a
     */
    public ComplexPolynomial multiply( double a ) {
        Complex[] c = new Complex[_c.length];
        for ( int k = 0; k < c.length; k++ ) {
            c[k] = _c[k].multiply( a );
        }

        return new ComplexPolynomial( c );
    }

    /**
     * Returns the multiplication of this polynomial by a scalar.
     * @param a
     */
    public ComplexPolynomial multiply( Complex a ) {
        Complex[] c = new Complex[_c.length];
        for ( int k = 0; k < c.length; k++ ) {
            c[k] = _c[k].multiply( a );
        }

        return new ComplexPolynomial( c );
    }

    /** The multiplication law.
     * @param r a ring member
     *
     */
    public Ring.Member multiply( Ring.Member r ) {
        if ( r instanceof ComplexPolynomial ) {
            ComplexPolynomial p = (ComplexPolynomial) r;
            int maxgrade        = PolynomialMath.maxDegree( this, p );
            int mingrade        = PolynomialMath.minDegree( this, p );
            int destgrade       = ( maxgrade + mingrade ) - 1;
            Complex[] n         = new Complex[destgrade];
            for ( int k = 0; k < destgrade; k++ ) {
                n[k] = Complex.ZERO;
            }
            for ( int k = 0; k < degree(); k++ ) {
                Complex tis = getCoefficientAsComplex( k );
                for ( int j = 0; j < p.degree(); j++ ) {
                    Complex tat = p.getCoefficientAsComplex( j );
                    n[k + j] = n[k + j].add( tis.multiply( tat ) );
                }
            }

            return new ComplexPolynomial( n );
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /** Returns the inverse member.
     *
     */
    public AbelianGroup.Member negate() {
        Complex[] c = new Complex[_c.length];
        for ( int k = 0; k < c.length; k++ ) {
            c[k] = (Complex) _c[k].negate();
        }

        return new ComplexPolynomial( c );
    }

    /** The group composition law with inverse.
     * @param g a group member
     *
     */
    public AbelianGroup.Member subtract( AbelianGroup.Member g ) {
        AbelianGroup.Member result = null;
        if ( g instanceof ComplexPolynomial ) {
            ComplexPolynomial p = (ComplexPolynomial) g;
            int maxgrade        = PolynomialMath.maxDegree( this, p );
            Complex[] c         = new Complex[maxgrade];
            for ( int k = 0; k < maxgrade; k++ ) {
                c[k] = getCoefficientAsComplex( k ).subtract( p.getCoefficientAsComplex( k ) );
            }
            result = new ComplexPolynomial( c );
        } else {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    /**
     *
     */
    public String toString() {
        StringBuffer sb = new StringBuffer( "P(z) = " );

        for ( int k = degree(); k > 1; k-- ) {
            sb.append( _c[k - 1] ).append( "z^" ).append( k - 1 ).append( " + " );
        }
        sb.append( _c[0] );

        return sb.toString();
    }
}

