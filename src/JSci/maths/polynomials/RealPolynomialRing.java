package JSci.maths.polynomials;

import JSci.maths.MathDouble;
import JSci.maths.fields.*;
import JSci.maths.groups.*;


/**
 *
 * @author  b.dietrich
 */
public class RealPolynomialRing implements JSci.maths.fields.Ring {
    private static final RealPolynomial ZERO       = new RealPolynomial( new double[] { 0.0 } );
    private static final RealPolynomial ONE        = new RealPolynomial( new double[] { 1.0 } );
    private static RealPolynomialRing _instance;

    /** Creates a new instance of PolynomialRing */
    protected RealPolynomialRing() {
    }

    /**
     * Singleton.
     */
    public static final RealPolynomialRing getInstance() {
        if ( _instance == null ) {
            synchronized ( RealPolynomialRing.class ) {
                if ( _instance == null ) {
                    _instance = new RealPolynomialRing();
                }
            }
        }

        return _instance;
    }

    /** Returns true if one member is the negative of the other.
     * @param a a group member
     * @param b a group member
     *
     */
    public boolean isNegative( AbelianGroup.Member a, AbelianGroup.Member b ) {
        if ( ( a instanceof RealPolynomial ) && ( b instanceof RealPolynomial ) ) {
            RealPolynomial p1 = (RealPolynomial) a;
            RealPolynomial p2 = (RealPolynomial) b;

            return p1.add( p2 ).equals( ZERO );
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /** Returns true if the member is the unit element.
     *
     */
    public boolean isOne( Ring.Member r ) {
        if ( r instanceof RealPolynomial ) {
            return ( (RealPolynomial) r ).isOne();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /** Returns true if the member is the identity element of this group.
     * @param g a group member
     *
     */
    public boolean isZero( AbelianGroup.Member g ) {
        if ( g instanceof RealPolynomial ) {
            return ( (RealPolynomial) g ).isNull();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /** Returns the unit element.
     *
     */
    public Ring.Member one() {
        return ONE;
    }

    /** Returns the identity element.
     *
     */
    public AbelianGroup.Member zero() {
        return ZERO;
    }

    /**
     * internal method for safe typecast
     */
    protected static double[] toDouble( Field.Member[] f ) {
        if ( f == null ) {
            return null;
        }

        int dim = f.length;

        double[] d = new double[dim];
        for ( int k = 0; k < dim; k++ ) {
            if ( f[k] instanceof MathDouble ) {
                d[k] = ( (MathDouble) f[k] ).value();
            } else {
                throw new IllegalArgumentException( "Expected MathDouble. Got (" + k + ") " + f[k] );
            }
        }

        return d;
    }

    /**
     * internal method for safe typecast
     */
    protected static MathDouble[] toMathDouble( double[] d ) {
        if ( d == null ) {
            return null;
        }

        int dim        = d.length;
        MathDouble[] s = new MathDouble[dim];
        for ( int k = 0; k < dim; k++ ) {
            s[k] = new MathDouble( d[k] );
        }

        return s;
    }
}
