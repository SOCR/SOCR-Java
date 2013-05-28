package JSci.maths.polynomials;

import JSci.maths.MathDouble;
import JSci.maths.fields.*;
import JSci.maths.fields.Field;
import JSci.maths.groups.*;


/** A Polynomial as a <code>Ring.Member</code> over a <i>real</i> <code>Field</code>
 * @author b.dietrich
 */
public class RealPolynomial implements Polynomial {
    private double[] _c;

    /** Creates a new instance of RealPolynomial */
    public RealPolynomial( double[] coeff ) {
        if ( coeff == null ) {
            throw new NullPointerException();
        }
        if ( coeff.length == 0 ) {
            _c = new double[] { 0. };
        } else {
            _c = coeff;
        }
    }

    /**
     * Creates a new RealPolynomial object.
     *
     * @param f
     */
    public RealPolynomial( Field.Member[] f ) {
        if ( f == null ) {
            throw new NullPointerException();
        }
        if ( f.length == 0 ) {
            _c = new double[] { 0. };
        } else {
            _c = new double[f.length];
            for ( int k = 0; k < _c.length; k++ ) {
                if ( f[k] instanceof MathDouble ) {
                    _c[k] = ( (MathDouble) f[k] ).value();
                } else {
                    throw new IllegalArgumentException( "Different fields. Argument was " + f[k] );
                }
            }
        }
    }

    /** Get the coefficient of degree k, i.e. <I>a_k</I> if
     * <I>P(x)</I> := sum_{k=0}^n <I>a_k x^k</I>
     * @param k degree
     * @return coefficient as described above
     */
    public Field.Member getCoefficient( int n ) {
        return new MathDouble( getCoefficientAsDouble( n ) );
    }

    /** Get the coefficient of degree k, i.e. <I>a_k</I> if
     * <I>P(x)</I> := sum_{k=0}^n <I>a_k x^k</I> as a real number
     * @param k degree
     * @return coefficient as described above
     */
    public double getCoefficientAsDouble( int n ) {
        if ( n >= _c.length ) {
            return 0.;
        } else {
            return _c[n];
        }
    }

    /** Get the coefficients as an array
      * @return the coefficients as an array
      */
    public Field.Member[] getCoefficients() {
        return RealPolynomialRing.toMathDouble( getCoefficientsAsDoubles() );
    }

    /** Get the coefficients as an array of doubles
     * @return the coefficients as an array
     */
    public double[] getCoefficientsAsDoubles() {
        return _c;
    }

    /** The degree
     * @return the degree
     */
    public int degree() {
        return _c.length;
    }

    /**                                               
     * Does this polynomial represent a "NULL". This does not coincide with
     * grade==0. All Coefficients are tested for |a_k| < POLYEPS
     * @return true if all coefficients <  POLYEPS 
     */
    public boolean isNull() {
        boolean res = true;
        for ( int k = 0; res && ( k < degree() ); k++ ) {
            if ( Math.abs( getCoefficientAsDouble( k ) ) > POLYEPS ) {
                res = false;
            }
        }

        return res;
    }

    /**
     * Does this polynomial represent a "ONE". This does not necessarily
     * mean grade==1. It is tested, whether |a_0 -1. | <=POLYEPS and the remaining
     * coefficients are |a_k| < POLYEPS
     * @return true if so
     */
    public boolean isOne() {
        boolean res = Math.abs( getCoefficientAsDouble( 0 ) - 1.0 ) <= POLYEPS;

        for ( int k = 1; res && ( k < degree() ); k++ ) {
            if ( Math.abs( getCoefficientAsDouble( k ) ) > POLYEPS ) {
                res = false;
            }
        }

        return res;
    }

    /** The group composition law. Returns a new polynom with grade = max( this.grade, g.grade)
     * @param g a group member
     *
     */
    public AbelianGroup.Member add( AbelianGroup.Member g ) {
        AbelianGroup.Member result = null;
        if ( g instanceof RealPolynomial ) {
            RealPolynomial p = (RealPolynomial) g;
            int maxgrade     = PolynomialMath.maxDegree( this, p );
            double[] c       = new double[maxgrade];
            for ( int k = 0; k < maxgrade; k++ ) {
                c[k] = getCoefficientAsDouble( k ) + p.getCoefficientAsDouble( k );
            }
            result = new RealPolynomial( c );
        } else {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    /**
     * Differentiate the real polynomial. Only useful iff the polynomial is built over
     * a banach space and an appropriate multiplication law is provided.
     *
     * @return a new polynomial with degree = max(this.degree-1 , 1)
     */
    public RealPolynomial differentiate() {
        if ( degree() == 1 ) {
            return (RealPolynomial) RealPolynomialRing.getInstance().zero();
        } else {
            double[] dn = new double[degree() - 1];
            for ( int k = 1; k < degree(); k++ ) {
                dn[k - 1] = getCoefficientAsDouble( k ) * k;
            }

            return new RealPolynomial( dn );
        }
    }

    /** return a new real Polynomial with coefficients divided by <I>f</I>
     * @param f divisor
     * @return new Polynomial with coefficients /= <I>f</I>
     */
    public Polynomial divide( Field.Member f ) {
        if ( f instanceof MathDouble ) {
            double a = ( (MathDouble) f ).value();

            return divide( a );
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /** return a new real Polynomial with coefficients divided by <I>a</I>
     * @param a divisor
     * @return new Polynomial with coefficients /= <I>a</I>
     */
    public RealPolynomial divide( double a ) {
        double[] c = new double[_c.length];
        for ( int k = 0; k < c.length; k++ ) {
            c[k] = _c[k] / a;
        }

        return new RealPolynomial( c );
    }

    /**
     * Is this-o == Null ?
     * @param o the other polynomial
     *
     * @return true if so
     */
    public boolean equals( Object o ) {
        boolean result = false;
        if ( o == this ) {
            result = true;
        } else if ( o instanceof RealPolynomial ) {
            RealPolynomial p = (RealPolynomial) o;

            return ( (RealPolynomial) this.subtract( p ) ).isNull();
        }

        return result;
    }

    /**
     * Some kind of hashcode... (Since I have an equals)
     * @return a hashcode
     */
    public int hashCode() {
        int res = 0;
        for ( int k = 0; k < degree(); k++ ) {
            res += (int) ( getCoefficientAsDouble( k ) * 10.0 );
        }

        return res;
    }

    /**
     * "inverse" operation for differentiate
     * @return the integrated polynomial
     */
    public RealPolynomial integrate() {
        double[] dn = new double[degree() + 1];
        for ( int k = 0; k < degree(); k++ ) {
            dn[k + 1] = getCoefficientAsDouble( k ) / ( k + 1 );
        }

        return new RealPolynomial( dn );
    }

    /**
     * Returns the multiplication of this polynomial by a scalar
     * @param f
     */
    public Polynomial multiply( Field.Member f ) {
        if ( f instanceof MathDouble ) {
            double a = ( (MathDouble) f ).value();

            return multiply( a );
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the multiplication of this polynomial by a scalar
    * @param a factor
    * @return new Polynomial with coefficients *= <I>a</I>
    */
    public RealPolynomial multiply( double a ) {
        double[] c = new double[_c.length];
        for ( int k = 0; k < c.length; k++ ) {
            c[k] = _c[k] * a;
        }

        return new RealPolynomial( c );
    }

    /**
     * The multiplication law. Multiplies this Polynomial with another
     * @param r a ring member
     * @return a new Polynomial with grade = max( this.grade, r.grade) + min( this.grade, r.grade) -1
     */
    public Ring.Member multiply( Ring.Member r ) {
        if ( r instanceof RealPolynomial ) {
            RealPolynomial p = (RealPolynomial) r;
            int maxgrade     = PolynomialMath.maxDegree( this, p );
            int mingrade     = PolynomialMath.minDegree( this, p );
            int destgrade    = ( maxgrade + mingrade ) - 1;
            double[] n       = new double[destgrade];
            for ( int k = 0; k < degree(); k++ ) {
                double tis = getCoefficientAsDouble( k );
                for ( int j = 0; j < p.degree(); j++ ) {
                    double tat = p.getCoefficientAsDouble( j );
                    n[k + j] += ( tis * tat );
                }
            }

            return new RealPolynomial( n );
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /** Returns the inverse member. (That is mult(-1))
     * @return inverse
     */
    public AbelianGroup.Member negate() {
        double[] c = new double[_c.length];
        for ( int k = 0; k < c.length; k++ ) {
            c[k] = -_c[k];
        }

        return new RealPolynomial( c );
    }

    /** The group composition law with inverse.
     * @param g a group member
     *
     */
    public AbelianGroup.Member subtract( AbelianGroup.Member g ) {
        AbelianGroup.Member result = null;
        if ( g instanceof RealPolynomial ) {
            RealPolynomial p = (RealPolynomial) g;
            int maxgrade     = PolynomialMath.maxDegree( this, p );
            double[] c       = new double[maxgrade];
            for ( int k = 0; k < maxgrade; k++ ) {
                c[k] = getCoefficientAsDouble( k ) - p.getCoefficientAsDouble( k );
            }
            result = new RealPolynomial( c );
        } else {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    /**
     * String representation <I>P(x) = a_k x^k +...</I>
     * @return String
     */
    public String toString() {
        StringBuffer sb = new StringBuffer( "P(x) = " );
        if ( _c[degree() - 1] < 0. ) {
            sb.append( "-" );
        } else {
            sb.append( " " );
        }
        for ( int k = degree(); k > 1; k-- ) {
            sb.append( Math.abs( _c[k - 1] ) ).append( "x^" ).append( k - 1 )
              .append( ( _c[k - 2] >= 0. ) ? " + " : " - " );
        }
        sb.append( _c[0] );

        return sb.toString();
    }
}

