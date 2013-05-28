package JSci.maths.polynomials;

import JSci.maths.Complex;
import JSci.maths.ComplexSquareMatrix;


/**
 *
 * @author  b.dietrich
 */
public class PolynomialMath {
    /**
     * Get the (complex) frobenius matrix for a given polynomial.
     * The eigenvalues of this frobenius matrix are the null values of the polynomial
     * 
     * @param p the polynomial
     *
     * @return the Frobenius matrix
     */
    public static ComplexSquareMatrix getFrobeniusMatrix( Polynomial p ) {
        ComplexPolynomial cp = normalize( toComplex( p ) );

        int n = cp.degree();
        if ( n < 2 ) {
            throw new IllegalArgumentException( "Cannot get Frobenius matrix for a linear factor" );
        }

        ComplexSquareMatrix csm = new ComplexSquareMatrix( n - 1 );

        // Fill subdiagonal
        for ( int k = 0; k < ( n - 2 ); k++ ) {
            csm.setElement( k + 1, k, 1., 0 );
        }

        // fill lastCol
        for ( int k = 0; k < ( n - 1 ); k++ ) {
            csm.setElement( k, n - 2, cp.getCoefficientAsComplex( k ) );
        }

        return csm;
    }

    /**
     * Get the maximum degree of two polynomials
     * @param p1
     * @param p2
     */
    public static int maxDegree( Polynomial p1, Polynomial p2 ) {
        return Math.max( p1.degree(), p2.degree() );
    }

    /**
     * Get the minimal degree of two polynomials
     * @param p1
     * @param p2
     */
    public static int minDegree( Polynomial p1, Polynomial p2 ) {
        return Math.min( p1.degree(), p2.degree() );
    }

    /**
     * Calculate the null points of a given polynomial by solving the
     * eigenvalue problem for the frobenius matrix
     * This is not yet implemented (depends on a QR- decomposition)
     * @param p the polynomial
     *
     * @return (unordered) list of null points.
     */
    public static Complex[] getNullPoints( Polynomial p ) {
        ComplexSquareMatrix c = getFrobeniusMatrix( p );

        // return solveEigenvalueByQR(c);
        throw new UnsupportedOperationException();
    }

    /**
     * Eval a polynomial by Horner's schema
     * @param coeff
     * @param t
     */
    public static double evalPolynomial( double[] coeff, double t ) {
        int n    = coeff.length - 1;
        double r = coeff[n];
        for ( int i = n - 1; i >= 0; i-- ) {
            r = coeff[i] + ( r * t );
        }

        return r;
    }

    /**
     * Same as above for complex numbers
     * @param coeff
     * @param t
     */
    public static Complex evalPolynomial( Complex[] coeff, Complex t ) {
        int n     = coeff.length - 1;
        Complex r = coeff[n];
        for ( int i = n - 1; i >= 0; i-- ) {
            r = coeff[i].add( r.multiply( t ) );
        }

        return r;
    }

    /**    
     * Get the coefficients of the interpolation polynomial
     * Caveat: this method is brute-force, slow and not very stable. It shouldn't
     * be used for more than appr. 10 points. Remember the strong variations of higher degree
     * polynomials 
     *
     * @param samplingPoints an array[2][n] where array[0] denotes x-values, array[1] y-values
     */
    public static double[] interpolateLagrange( double[][] samplingPoints ) {
        RealLagrangeBasis r = new RealLagrangeBasis( samplingPoints[0] );

        return ( (RealPolynomial) r.superposition( samplingPoints[1] ) ).getCoefficientsAsDoubles();
    }

    /**    
     * Interpolate a value by given sampling points.
     * Caveat: this method is brute-force, slow and not very stable. It shouldn't
     * be used for more than appr. 10 points. Remember the strong variations of higher degree
     * polynomials 
     *
     * @param samplingPoints an array[2][n] where array[0] denotes x-values, array[1] y-values
     */
    public static double interpolateLagrange( double[][] samplingPoints, double t ) {
        return evalPolynomial( interpolateLagrange( samplingPoints ), t );
    }

    /**    
     * Get the coefficients of the interpolation polynomial
     * Caveat: this method is brute-force, slow and not very stable. It shouldn't
     * be used for more than appr. 10 points. Remember the strong variations of higher degree
     * polynomials 
     *
     * @param samplingPoints an array[2][n] where array[0] denotes x-values, array[1] y-values
     */
    public static Complex[] interpolateLagrange( Complex[][] samplingPoints ) {
        ComplexLagrangeBasis r = new ComplexLagrangeBasis( samplingPoints[0] );

        return ( (ComplexPolynomial) r.superposition( samplingPoints[1] ) ).getCoefficientsAsComplexes();
    }

    /**    
     * Interpolate a value by given sampling points.
     * Caveat: this method is brute-force, slow and not very stable. It shouldn't
     * be used for more than appr. 10 points. Remember the strong variations of higher degree
     * polynomials 
     *
     * @param samplingPoints an array[2][n] where array[0] denotes x-values, array[1] y-values
     */
    public static Complex interpolateLagrange( Complex[][] samplingPoints, Complex t ) {
        return evalPolynomial( interpolateLagrange( samplingPoints ), t );
    }

    /**
     * Normalize a given complex polynomial, i.e. divide by the leading coefficient.
     * @param p
     */
    public static ComplexPolynomial normalize( ComplexPolynomial p ) {
        int n       = p.degree();
        Complex c   = p.getCoefficientAsComplex( n - 1 );
        Complex[] m = new Complex[n];
        m[n - 1]    = Complex.ONE;
        for ( int k = 0; k < ( n - 1 ); k++ ) {
            m[k] = p.getCoefficientAsComplex( k ).divide( c );
        }

        return new ComplexPolynomial( m );
    }

    /**
     *
     * Try to cast a Polynomial to a complex polynomial
     */
    public static ComplexPolynomial toComplex( Polynomial p ) {
        if ( p instanceof ComplexPolynomial ) {
            return (ComplexPolynomial) p;
        } else if ( p instanceof RealPolynomial ) {
            double[] d  = ( (RealPolynomial) p ).getCoefficientsAsDoubles();
            Complex[] c = new Complex[d.length];
            for ( int k = 0; k < d.length; k++ ) {
                c[k] = new Complex( d[k], 0 );
            }

            return new ComplexPolynomial( c );
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

