
package JSci.maths.wavelet;

/****************************************
* This interface is used to define wavelet filters.
* It is fairly general to accomodate just about any
* filter (except complex ones). Since changing
* an interface is painful, it must be as general
* as possible to start with. Therefore it doesn't
* assume that you are using dyadic wavelets (for
* example) and so, some object will implement
* somewhat redundant method that builds on the
* dyadic grid (for simplicity).
* @author Daniel Lemire
*****************************************/
public interface Filter {
  /************************************
  * lowpass filter
  *************************************/
	double[] lowpass (double[] x);

	/****************************************
	*  Highpass filters are normalized
  * in order to get L2 orthonormality of the
  * resulting wavelets (when it applies).
  * See the class DiscreteHilbertSpace for
  * an implementation of the L2 integration.
	*****************************************/
	double[] highpass (double[] y);
  /************************************
  * lowpass filter
  * @param param a parameter for the filter
  *************************************/
  double[] lowpass (double[] x, double[] param);

	/****************************************
	*  Highpass filters are normalized
  * in order to get L2 orthonormality of the
  * resulting wavelets (when it applies).
  * See the class DiscreteHilbertSpace for
  * an implementation of the L2 integration.
  * @param param a parameter for the filter
	*****************************************/
	double[] highpass (double[] y, double[] param);

	/****************************************
  * This method return the number of "scaling"
  * functions at the previous scale given a
  * number of scaling functions. The answer
  * is always smaller than the provided value
  * (about half since this is a dyadic
  * implementation). This relates to the same idea
  * as the "filter type".
  * However this method is used in the context
  * of signal processing for performance
  * reasons.
	*****************************************/
	int previousDimension(int k);

}
