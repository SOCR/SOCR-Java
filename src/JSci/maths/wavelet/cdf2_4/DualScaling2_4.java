
package JSci.maths.wavelet.cdf2_4;

import JSci.maths.wavelet.*;

/******************************************
* Cohen-Daubechies-Feauveau
* with N=2 and
* Ntilde=4 adapted to the interval
* by Deslauriers-Dubuc-Lemire
* @author Daniel Lemire
*****************************************/
public final class DualScaling2_4 extends MultiscaleFunction  implements Cloneable  {
	int n0;
	int k;
	static CDF2_4 cdf=new CDF2_4();

  /*****************************************
  * Check if another object is equal to this
  * DualScaling2_4 object
  ******************************************/
  public boolean equals(Object a) {
    if((a!=null) && (a instanceof DualScaling2_4))  {
      DualScaling2_4 iv=(DualScaling2_4)a;
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

	public DualScaling2_4 (int N0, int K) {
		setParameters(N0,K);
	}
  /********************************************
  * Return a copy of this object
  *********************************************/
	public Object clone() {
    DualScaling2_4 s = (DualScaling2_4)super.clone();
    s.n0=n0;
    s.k=k;
    return(s);
	}

	public DualScaling2_4 () {
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
		return(Cascades.dimension(n0,jfin));
	}

  /****************************************************
  * Number of scaling functions at scale where this
  * scaling function belongs.
  *****************************************************/
	public int dimension() {
		return(n0);
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
