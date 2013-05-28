package JSci.maths.polynomials;

import JSci.maths.fields.Field;


/**
 * The vector space basis for polynomials
 * @author  b.dietrich
 */
public interface PolynomialBasis {
    /**        
     * Get the <I>k</I>th basis vector
     * @param k for the <I>k</I>th basis vector
     */
    public Polynomial getBasisVector( int k );

    /**
     * The dimension of the vector space.
     * @return the dimension
     */
    public int dimension();

    /**
     * If available, get the one- points for the polynomials
     * @return the one-points
     */
    public Field.Member[] getSamplingPoints();

    /**
     * Get a superposition of basis vectors
     * @param coeff coefficients for the superposition
     *
     * @return a superposition
     */
    public Polynomial superposition( Field.Member[] coeff );
}

