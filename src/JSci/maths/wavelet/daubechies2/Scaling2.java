
package JSci.maths.wavelet.daubechies2;

import JSci.maths.wavelet.*;
/******************************************
* Daubechies wavelets adapted to the
* interval by Meyer. Thanks to Pierre Vial
* for the filters.
* @author Daniel Lemire
*****************************************/
public final class Scaling2 extends MultiscaleFunction implements Cloneable  {
	private int n0;
	private int k;
	private static final Daubechies2 cdf=new Daubechies2();
  
	public Scaling2 (int N0, int K) {
		setParameters(N0,K);
	}
  /*******************************
  * Return a String representation
  * of the object
  ********************************/
  public String toString() {
    String ans=new String("[n0=");
    ans.concat(Integer.toString(n0));
    ans.concat("][k=");
    ans.concat(Integer.toString(k));
    ans.concat("]");
    return(ans);
  }
  /*****************************************
  * Check if another object is equal to this
  * Scaling2 object
  ******************************************/
  public boolean equals(Object a) {
    if((a!=null) && (a instanceof Scaling2))  {
      Scaling2 iv=(Scaling2)a;
      if((this.dimension(0)!=iv.dimension(0))||(this.position()!=iv.position())){
        return false;
      } else {
        return true;
      }
    }
    return false;
  }
	/****************************************
  * This method is used to compute
  * how the number of scaling functions
  * changes from on scale to the other.
  * Basically, if you have k scaling
  * function and a filter of type t, you'll
  * have 2*k+t scaling functions at the
  * next scale (dyadic case).
  * Notice that this method assumes
  * that one is working with the dyadic
  * grid while the method "previousDimension"
  * define in the interface "filter" doesn't.
	******************************************/
	public int getFilterType () {
				return(cdf.filtretype);
	}
	public Scaling2 () {
	}
  /**********************************************
  * Set the parameters for this object
  * @param N0 number of scaling function on the
  *   scale of this object
  * @param K position or number of this object
  * @exception IllegalScalingException if N0 is not
  *   large enough
  ***********************************************/
	public void setParameters(int N0, int K) {
		if(N0<cdf.minlength) {
			throw new IllegalScalingException(N0,cdf.minlength);
		}
		n0=N0;
		k=K;
	}
  /********************************************
  * Return a copy of this object
  *********************************************/
	public Object clone() {
    Scaling2 s=(Scaling2) super.clone();
    s.n0=n0;
    s.k=k;
    return(s);
	}

	/************************************************
  * Return as an array the sampled values
  * of the function
  * @param j number of iterations
	*************************************************/
	public double[] evaluate ( int j1) {
		return(cdf.evalScaling (n0, k, j1));
	}
  /****************************************************
  * Starting with dimension() scaling functions and
  * going jfin scales ahead (iterating jfin times),
  * tells you how many scaling functions you'll have.
  * @param jfin number of iterations
  ******************************************************/
	public int dimension(int jfin) {
		return(Cascades.dimension(n0,jfin,cdf.filtretype));
	}
 /****************************************************
  * Number of scaling functions at scale where this
  * scaling function belongs.
  *****************************************************/
	public int dimension() {
		return(dimension(0));
	}
  /****************************************
  * Tells you what is the number of this
  * scaling function. Scaling functions are
  * numbered from left to right with the
  * one at the left boundary being noted 0.
  *****************************************/
	public int position() {
		return(k);
	}
}
