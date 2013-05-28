package JSci.maths.wavelet;

import JSci.maths.*;
import JSci.util.*;

public class SparseDiscreteFunction extends DiscreteFunction  implements Cloneable {
	public DoubleSparseVector Data;

	public SparseDiscreteFunction(double[] v) {
		setData(v);
	}
  /*******************************
  * Return a String representation
  * of the object
  ********************************/
  public String toString() {
    return(Data.toString());
  }
  /**********************
  * Makes the L2norm of the
  * internal array=1.
  ***********************/
  public void normalize() {
    Data.normalize();
  }

	public void setData (double[] v) {
		Data=new DoubleSparseVector(v);
	}
	/************************************************
  * Return as an array the sampled values
  * of the function
	*************************************************/
	public double[] evaluate() {
		return(VectorToolkit.toArray(Data));
	}

  /*****************************************
  * Check if another object is equal to this
  * DiscreteFunction object
  ******************************************/
  public boolean equals(Object a) {
    if((a!=null) && (a instanceof SparseDiscreteFunction))  {
        SparseDiscreteFunction iv=(SparseDiscreteFunction)a;
        return Data.equals(iv.Data);
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
                return Data.mass()/(Data.dimension()-1)*Math.abs(b-a);
	}
	/***************************
  * Compute the L2 norm of the
  * signal
	****************************/
  public double norm () {
    return(Data.norm());
  }
	/***************************
  * Compute the L2 norm of the
  * function
  * The parameter doesn't do anything.
  * @param j number of iterations
	****************************/
  public double norm (int j) {
    return(Data.norm());
  }
  /********************************************
  * Return a copy of this object
  *********************************************/
	public Object clone() {
    SparseDiscreteFunction sdf=(SparseDiscreteFunction) super.clone();
    sdf.Data = new DoubleSparseVector(VectorToolkit.toArray(Data));
    return(sdf);
	}
  /*****************************************
  * Tells you how many samples you'll get
  * from this function (will not depend
  * on the parameter)
  ******************************************/
	public int dimension(int jfin) {
		return(Data.dimension());
	}
  /*****************************************
  * Tells you how many samples you'll get
  * from this function
  ******************************************/
	public int dimension() {
		return(Data.dimension());
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
		return(Data.dimension());
	}
}

