
package JSci.maths.wavelet;

import JSci.maths.*;

/**********************************************
* This class is used to be able to mix the wavelet
* and cosine transforms. It is in fact a normalised
* cosine.
* @author Daniel Lemire
*************************************************/
public final class Cosine extends MultiscaleFunction implements NumericalConstants, Cloneable {
		private int n0; private int freq;private double normalisation;
		double tol=Double.MIN_VALUE;
  /*******************************
  * Return a String representation
  * of the object
  ********************************/
  public String toString() {
    String ans=new String("[n0=");
    ans.concat(Integer.toString(n0));
    ans.concat("][freq=");
    ans.concat(Integer.toString(freq));
    ans.concat("]");
    return(ans);
  }

  /*****************************************
  * Check if another object is equal to this
  * Cosine object
  ******************************************/
  public boolean equals(Object a) {
    if((a!=null) && (a instanceof Cosine))  {
      Cosine iv=(Cosine)a;
      if((this.dimension(0)!=iv.dimension(0))||(this.getFrequency()!=iv.getFrequency())){
        return false;
      } else {
        return true;
      }
    }
    return false;
  }

  public int getFrequency() {
    return(freq);
  }

		public Cosine (int N0,int FREQ) {
			if(N0<0) {
				throw new IllegalArgumentException("The length paramenter "+n0+" must be positive");
			}
			if((FREQ<0)||(FREQ>=N0)) {
				throw new IllegalArgumentException("The frequency parameter "+FREQ+" must be between "+0+" and "+(N0-1));
			}
			n0=N0;freq=FREQ;
			normalisation=Math.sqrt(n0/2d);
			if((2*freq==n0)||(freq==0)) {
				normalisation*=SQRT2;
			}
		}
	/************************************************
  * Return as an array the sampled values
  * of the function
	*************************************************/
		public double[] evaluate() {
			return(ArrayMath.scalarMultiply(1/normalisation,evaluate(n0,freq)));
		}

	private static double[] evaluate(int N0,int FREQ) {
			double[] ans=new double[N0];
			for(int k=0;k<ans.length;k++) {
				ans[k]=Math.cos(TWO_PI*k*FREQ/N0);
			}
			return(ans);
	}
  /*****************************************
  * Tells you how many samples you'll get
  * from this function (will not depend
  * on the parameter)
  ******************************************/
	public int dimension(int jfin) {
		return(n0);
	}
  /*****************************************
  * Tells you how many samples you'll get
  * from this function
  ******************************************/
	public int dimension() {
		return(n0);
	}
  /********************************************
  * Return a copy of this object
  *********************************************/
	public Object clone() {
    Cosine c = (Cosine)super.clone();
    c.n0=n0;
    c.freq=freq;
		return(c);
	}
	/************************************************
  * Return as an array the sampled values
  * of the function
  * @param j number of iterations  (doesn't do anything)
	*************************************************/
	public double[] evaluate (int j1) {
		return(evaluate());
	}
  /******************************************
  * Compute the mass (integral)
  * @param a left boundary of the interval
  * @param b right boundary of the interval
  * @param jfin number of iterations to consider
  *   (precision)
  **********************************************/
	public double mass(double a, double b, int jfin) {
		double somme=0;
		double[] values=evaluate(jfin);
		for(int k=0;k<values.length;k++) {
			somme+=values[k];
		}
        somme=somme/(values.length-1)*Math.abs(b-a);
        return(somme);
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
		return(n0);
	}
}

