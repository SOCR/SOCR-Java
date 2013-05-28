

package JSci.maths.wavelet;

/*****************************************
* Abstract encapsulation mostly meant for
* wavelet functions (dyadic case).
* @author Daniel Lemire
*****************************************/
public abstract class MultiscaleFunction implements Cloneable {
	public abstract int dimension(int jfin);

	public abstract int dimension();
  /********************************************
  * Return a copy of the object
  *********************************************/
	public  Object clone() {
    try {
      MultiscaleFunction mf=(MultiscaleFunction) super.clone();
      return(mf);
    } catch (CloneNotSupportedException cnse) {
      throw new InternalError();
    }
  }
  /**********************************************
  * Return a string representing the object
  ***********************************************/
  public abstract String toString();

	/************************************************
  * Return as an array the sampled values
  * of the function
  * @param j number of iterations
	*************************************************/
	public abstract double[] evaluate (int j1) ;

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
		for(int k=1;k<values.length-1;k++) {
			somme+=values[k];
		}
		somme+=values[0]/2;
		somme+=values[values.length-1]/2;
        somme=somme/(values.length-1)*Math.abs(b-a);
        return(somme);
	}

  /******************************************
  * Compute the mass (integral) of the interval 0,1
  * @param jfin number of iterations to consider
  *   (precision)
  **********************************************/
	public double mass(int jfin) {
		return(mass(0,1,jfin));
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
	public abstract int getFilterType ();

  /*****************************************
  * Check if another object is equal to this
  * MultiscaleFunction object
  ******************************************/
  public abstract boolean equals(Object o);
}
