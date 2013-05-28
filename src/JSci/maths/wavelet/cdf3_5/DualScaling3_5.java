
package JSci.maths.wavelet.cdf3_5;

import JSci.maths.wavelet.*;
/******************************************
* Cohen-Daubechies-Feauveau
* with N=3 and
* Ntilde=5 adapted to the interval
* by Deslauriers-Dubuc-Lemire
* @author Daniel Lemire
*****************************************/
public final class DualScaling3_5 extends MultiscaleFunction implements Cloneable {
	private int n0;
	private int k;
  private static CDF3_5 cdf=new CDF3_5();
  /*****************************************
  * Check if another object is equal to this
  * DualScaling3_5 object
  ******************************************/
  public boolean equals(Object a) {
    if((a!=null) && (a instanceof DualScaling3_5) ) {
      DualScaling3_5 iv=(DualScaling3_5)a;
      if((this.dimension(0)!=iv.dimension(0))||(this.position()!=iv.position())){
        return false;
      } else {
        return true;
      }
    }
    return false;
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
	public int getFilterType () {
			return(cdf.filtretype);
	}
	public DualScaling3_5 (int N0, int K) {
		setParameters(N0,K);
	}
  /********************************************
  * Return a copy of this object
  *********************************************/
	public Object clone() {
    DualScaling3_5 s = (DualScaling3_5) super.clone();
    s.n0=n0;
    s.k=k;
    return(s);
	}

	public DualScaling3_5 () {
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
	/************************************************
  * Return as an array the sampled values
  * of the function
  * @param j number of iterations
	*************************************************/
	public double[] evaluate ( int j1) {
		return(cdf.evalScaling (n0,  k, j1));
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
