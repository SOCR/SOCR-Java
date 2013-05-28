package JSci.maths.polynomials;

import JSci.maths.Complex;
import JSci.maths.MathDouble;
import JSci.maths.fields.Field;
import JSci.maths.fields.Ring;
import JSci.maths.groups.AbelianGroup;


/**
 *
 * @author  b.dietrich
 */
public class ComplexPolynomialRing implements JSci.maths.fields.Ring {
    private static final ComplexPolynomial ZERO       = 
            new ComplexPolynomial( new Complex[] { Complex.ZERO } );
    private static final ComplexPolynomial ONE        = 
            new ComplexPolynomial( new Complex[] { Complex.ONE } );
    private static ComplexPolynomialRing _instance;

    /** Creates a new instance of ComplexPolynomialRing */
    protected ComplexPolynomialRing() {
    }

    /**
     * Singleton.
     */
    public static final ComplexPolynomialRing getInstance() {
        if ( _instance == null ) {
            synchronized ( ComplexPolynomialRing.class ) {
                if ( _instance == null ) {
                    _instance = new ComplexPolynomialRing();
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
        return a.add( b ).equals( ZERO );
    }

    /** Returns true if the member is the unit element.
     *
     */
    public boolean isOne( Ring.Member r ) {
        return r.equals( ONE );
    }

    /** Returns true if the member is the identity element of this group.
     * @param g a group member
     *
     */
    public boolean isZero( AbelianGroup.Member g ) {
        return g.equals( ZERO );
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
     * Internal method for typesafe cast
     * 
     */
    protected static Complex[] toComplex( Field.Member[] f ) {
        Complex[] _c = null;
        if ( f == null ) {
            _c = new Complex[] { Complex.ZERO };
        }
        if ( f.length == 0 ) {
            _c = new Complex[] { Complex.ZERO };
        } else {
            _c = new Complex[f.length];
            for ( int k = 0; k < _c.length; k++ ) {
                if ( f[k] instanceof Complex ) {
                    _c[k] = (Complex) f[k];
                } else if ( f[k] instanceof MathDouble ) {
                    _c[k] = new Complex( ( (MathDouble) f[k] ).value(), 0. );
                } else {
                    throw new IllegalArgumentException( "Different fields. Argument was " + f[k] );
                }
            }
        }

        return _c;
    }
}
