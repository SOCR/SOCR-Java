package JSci.maths.wavelet;

import JSci.maths.*;
import java.util.Arrays;

/**********************************************
* This class is used to be able to mix the wavelet
* and other type of functions such as given signals.
* @author Daniel Lemire
*************************************************/
public class DiscreteFunction extends MultiscaleFunction implements Cloneable  {
	double[] Data;
  protected DiscreteFunction() {}
	public DiscreteFunction(double[] v) {
		setData(v);
	}
  /*******************************
  * Return a String representation
  * of the object
  ********************************/
  public String toString() {
    return(ArrayMath.toString(Data));
  }
  /**********************
  * Makes the L2 norm of the
  * internal array equal to 1.
  ***********************/
  public void normalize() {
    setData(ArrayMath.normalize(Data));
  }

	public void setData (double[] v) {
		Data=ArrayMath.copy(v);
	}
	/************************************************
  * Return as an array the sampled values
  * of the function
	*************************************************/
	public double[] evaluate() {
		return(Data);
	}

  /*****************************************
  * Check if another object is equal to this
  * DiscreteFunction object
  ******************************************/
  public boolean equals(Object a) {
    if((a!=null) && (a instanceof DiscreteFunction))  {
      DiscreteFunction iv=(DiscreteFunction)a;
      if(Arrays.equals(Data,iv.evaluate(0))){
        return true;
      }
    }
    return false;
  }
	/************************************************
  * Return as an array the sampled values
  * of the function
  * @param j number of iterations (doesn't do anything)
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
		double somme=0.0;
		double[] values=evaluate(jfin);
		for(int k=0;k<values.length;k++) {
			somme+=values[k];
		}
        somme=somme/(values.length-1)*Math.abs(b-a);
        return(somme);
	}
	/***************************
  * Compute the L2 norm of the
  * signal
	****************************/
  public double norm () {
    return(ArrayMath.norm(evaluate()));
  }
	/***************************
  * Compute the L2 norm of the
  * function
  * @param j number of iterations
	****************************/
  public double norm (int j) {
    return(ArrayMath.norm(evaluate(j)));
  }
  /********************************************
  * Return a copy of this object
  *********************************************/
	public Object clone() {
    DiscreteFunction df=(DiscreteFunction) super.clone();
    df.Data=ArrayMath.copy(this.Data);
    return(df);
	}
  /*****************************************
  * Tells you how many samples you'll get
  * from this function (will not depend
  * on the parameter)
  ******************************************/
	public int dimension(int jfin) {
		return(Data.length);
	}
  /*****************************************
  * Tells you how many samples you'll get
  * from this function
  ******************************************/
	public int dimension() {
		return(Data.length);
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
		return(Data.length);
	}
}

