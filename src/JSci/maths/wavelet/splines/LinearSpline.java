
package JSci.maths.wavelet.splines;

import JSci.maths.wavelet.*;
import JSci.maths.*;

/****************************************
* This class is used to generate linear
* splines to be used as wavelets or related
* functions. It can also be used for basic
* interpolation.
* @author Daniel Lemire
*****************************************/
public class LinearSpline extends Spline implements Filter, Cloneable  {
	protected final static int filtretype=1;
	private double[] vecteur;
	static final double[] vg={1/2d,1d,1/2d};
	static final double[] v0={1d,1/2d};
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
				return(filtretype);
	}

  /*******************************
  * Return a String representation
  * of the object
  ********************************/
  public String toString() {
    return(ArrayMath.toString(vecteur));
  }

  /*****************************************
  * Check if another object is equal to this
  * LinearSpline object
  ******************************************/
  public boolean equals(Object a) {
    if(a!=null && (a instanceof LinearSpline) && vecteur.length==((LinearSpline)a).dimension()) {
      LinearSpline iv=(LinearSpline)a;
        for(int i=0;i<vecteur.length;i++) {
          if(vecteur[i]!=iv.getValue(i))
            return false;
          }
        return true;
      } else
    return false;
  }


	/****************************************
  * This method return the number of "scaling"
  * functions at the previous scale given a
  * number of scaling functions. The answer
  * is always smaller than the provided value
  * (about half since this is a dyadic
  * implementation). This relates to the same idea
  * as the "Filter type". It is used by
  * the interface "Filter".
	*****************************************/
	public int previousDimension (int k) {
		int i=(int) Math.round((k+1)/2d);
		if(2*i-1==k) {
			return(i);
		} else {
			throw new IllegalArgumentException("Even number of values into an odd Filter! Change the number of data values/of iterations.");
		}
	}

	/****************************************
	* This is the implementation of the lowpass
  * Filter. It is used by the interface
  * "Filter". Lowpass filters are normalized
  * so that they preserve constants away from
  * the boundaries.
	*****************************************/
	public double[] lowpass (double[] v, double[] param) {
		return(lowpass(v));
	}


	/****************************************
	* This is the implementation of the highpass
  * Filter. It is used by the interface
  * "Filter". This class doesn't have a
  * highpass Filter so that this method is
  * only provided to be compatible with the
  * interface.
	*****************************************/
	public double[] highpass (double[] v, double[] param) {
		return(highpass(v));
	}

	/****************************************
	* This is the implementation of the lowpass
  * Filter. It is used by the interface
  * "Filter". Lowpass filters are normalized
  * so that they preserve constants away from
  * the boundaries.
	*****************************************/
	public double[] lowpass (double[] donnee) {
		if(donnee.length<2) {
			throw new IllegalArgumentException("The array is not long enough : "+donnee.length+" < 2");
		}
		double[] sortie=new double[2*donnee.length-1];
		for(int k=1;k<donnee.length-1;k++) {
			sortie[2*k-1]+=donnee[k]*vg[0];
			sortie[2*k]+=donnee[k]*vg[1];
			sortie[2*k+1]+=donnee[k]*vg[2];
		}
		sortie[0]+=v0[0]*donnee[0];
		sortie[1]+=v0[1]*donnee[0];
		sortie[sortie.length-1]+=v0[0]*donnee[donnee.length-1];
		sortie[sortie.length-2]+=v0[1]*donnee[donnee.length-1];
		return(sortie);
	}

	/****************************************
	* This is the implementation of the highpass
  * Filter. It is used by the interface
  * "Filter". This class doesn't have a
  * highpass Filter so that this method is
  * only provided to be compatible with the
  * interface.
	*****************************************/
	public double[] highpass (double[] donnee) {
		return(lowpass(donnee));
	}

	/***************************************
    ****************************************/
	public LinearSpline(double[] v) {
		vecteur=v;
	}


	/***************************************
	****************************************/
	public LinearSpline() {
	}
  /********************************************
  * Return a copy of this object
  *********************************************/
	public Object clone() {
    LinearSpline sod=(LinearSpline) super.clone();
    if(vecteur!=null)
      sod.vecteur=ArrayMath.copy(this.vecteur);
    return(sod);
	}

	/********************************************
  * Get the i th sampled value of the function.
  * @param i position (knot)
  * @exception IllegalArgumentException if i is not
  *   a within 0 and the last knot (dimension()-1)
	*********************************************/
	public double getValue(int i) {
		if ((i<0) || (i>vecteur.length-1)) {
			throw new IllegalArgumentException("Incorrect parameter : "+i+", "+vecteur.length);
		}
		return(vecteur[i]);
	}

   /*********************************************
   * Set a particular value
   * @param i position (knot)
   * @param d value
   * @exception IllegalArgumentException if the parameter i is negative
   **********************************************/
   public void setValue(int i, double d) {
		if (i<0) {
			throw new IllegalArgumentException("The parameter must be positive: "+i);
		}
		vecteur[i]=d;
	}

   /*********************************************
   * Set the sampled values of the function
   * to the specified array 
   **********************************************/
	public void setValues(double[] v) {
		vecteur=v;
	}

	/*********************************************
  * compute the derivative of the function -
  * useful for numerical analysis
	**********************************************/
	public PiecewiseConstant derive() {
		return(this.derive(0,1));
    }

	/*********************************************
  * compute the derivative of the function -
  * useful for numerical analysis
  * @param a left boundary of the interval
  * @param b right boundary of the interval
	**********************************************/
    public PiecewiseConstant derive(double a, double b) {
		double[] v=new double[vecteur.length-1];
		for(int i=0;i<vecteur.length-1;i++) {
				v[i]=(vecteur[i+1]-vecteur[i])*(vecteur.length-1)/Math.abs(b-a);
		}
		PiecewiseConstant d=new PiecewiseConstant(v);
		return(d);
	}

  /*********************************************
  * Number of knots
  **********************************************/
   public int dimension () {
		return(vecteur.length);
   }

	/*********************************************
  * Number of knots after j iterations
  * @param j number of iterations
	**********************************************/
	public int dimension(int j) {
		return(Cascades.dimension(vecteur.length, j));
	}

	/************************************************
  * Return as an array the interpolated values
  * of the function. Will return the same values
  * as the evaluate method because the Filter
  * is interpolatory.
  * @param j number of iterations
	*************************************************/
   	public double[] interpolate(int j) {
		if (j<0) {
			throw new IllegalArgumentException("This parameter must be postive : "+j);
		}
		return(Cascades.evaluation(this,j,vecteur));
  	}

	/************************************************
  * Return as an array the sampled values
  * of the function
  * @param j number of iterations
	*************************************************/
	public double[] evaluate ( int j1) {
		return(interpolate(j1));
	}


}
