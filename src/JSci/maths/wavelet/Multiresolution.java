

package JSci.maths.wavelet;

/************************************
* This class is used to encapsulate
* the various wavelet multiresolution
* (Dau2, Haar, ...). It assumes that
* your are working with the dyadic case
* and with real numbers.
* It is meant to provide only the
* most basic methods in order to be
* as general as possible. Also,
* for performance considerations,
* this class should be as light as
* possible. However, in practice, it
* is useful to put most of the actual
* implementation in a multiresolution
* object instead of spreading it over
* many objects.
* @author Daniel Lemire
************************************/ 
public abstract class Multiresolution extends Object  {
 	/****************************************
  * This method is used to compute
  * how the number of scaling functions
  * changes from on scale to the other.
  * Basically, if you have k scaling
  * function and a Filter of type t, you'll
  * have 2*k+t scaling functions at the
  * next scale (dyadic case).
  * Notice that this method assumes
  * that one is working with the dyadic
  * grid while the method "previousDimension"
  * define in the interface "Filter" doesn't.
	******************************************/
	public abstract int getFilterType ();

	public abstract MultiscaleFunction primaryScaling(int n0, int k);

	public abstract MultiscaleFunction dualScaling(int n0, int k);

	public abstract MultiscaleFunction primaryWavelet(int n0, int k);

	public abstract MultiscaleFunction dualWavelet(int n0, int k);

	/****************************************
  * This method return the number of "scaling"
  * functions at the previous scale given a
  * number of scaling functions. The answer
  * is always smaller than the provided value
  * (about half since this is a dyadic
  * implementation). This relates to the same idea
  * as the "Filter type". It is used by
  * the interface "Filter".
  * If needed it should be
	* overwritten, in particular, for
	* performance considerations.
	**************************************/
	public int previousDimension (int k) {
		return(Cascades.previousDimension(getFilterType(),k));
	}

}
