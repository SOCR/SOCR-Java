package JSci.maths.polynomials;

/** The famous Chebychev basis for interpolating polynomials
 * with minimal variation
 * @author b.dietrich
 */
public class ChebychevBasis extends RealLagrangeBasis
    implements PolynomialBasis {
    /** Creates a new instance of ChebychevBase for a given degree
     * @param dim Dimension (= degree)
     */
    public ChebychevBasis( int dim ) {
        super();
        if ( dim <= 0 ) {
            throw new IllegalArgumentException();
        }
        super._dim  = dim;
        _samplingsX = new double[_dim];

        int n       = _dim - 1;
        for ( int k = 0; k < _dim; k++ ) {
            _samplingsX[k] = Math.cos( ( (double) ( 2 * k + 1 ) * Math.PI ) / (double) ( 2 * n + 2 ) );
        }
        buildBasis();
    }
}

