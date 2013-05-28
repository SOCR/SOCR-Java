

package JSci.maths.wavelet.splines;

import JSci.maths.wavelet.*;
import JSci.maths.*;

/****************************************
* This class is used to generate sum of
* Diracs (splines of order 0) to be used
* as wavelets or related functions. It can
* also be used for basic interpolation.
* @author Daniel Lemire
*****************************************/
public class SumOfDiracs  extends Spline implements Filter, Cloneable   {
	protected final static int filtretype=-1;
	private double[] vecteur;
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
  * SumOfDiracs object
  ******************************************/
  public boolean equals(Object a) {
    if(a!=null && (a instanceof SumOfDiracs) && vecteur.length==((SumOfDiracs)a).dimension()) {
      SumOfDiracs iv=(SumOfDiracs)a;
        for(int i=0;i<vecteur.length;i++) {
          if(vecteur[i]!=iv.getValue(i))
            return false;
          }
        return true;
      } else
    return false;
  }

  /********************************************
  * Return a copy of this object
  *********************************************/
	public Object clone() {
    SumOfDiracs sod=(SumOfDiracs) super.clone();
    if(vecteur!=null)
      sod.vecteur=ArrayMath.copy(this.vecteur);
    return(sod);
	}


	/****************************************
  * This method return the number of "scaling"
  * functions at the previous scale given a
  * number of scaling functions. The answer
  * is always smaller than the provided value
  * (about half since this is a dyadic
  * implementation). This serves as the defintion
  * for the "Filter type".
  * It is used by the interface
  * "Filter".
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
	public double[] lowpass (double[] gete) {
		if(gete.length<2) {
			throw new IllegalArgumentException("The array is not long enough : "+gete.length+" < 2");
		}
		double[] data=Cascades.oversample(gete);
		int dl=data.length-1;
		double[] sortie=new double[2*gete.length-1];
		double[] vg={0,1d,0};
		double[] v0={1d,0};
		for(int k=1;k<=dl-1;k++) {
			sortie[k]=ArrayMath.scalarProduct(ArrayMath.extract(k-1,k+1,data),vg);
		}
		sortie[0]=ArrayMath.scalarProduct(ArrayMath.extract(0,1,data),v0);
		sortie[dl]=ArrayMath.scalarProduct(ArrayMath.extract(dl,dl-1,data),v0);
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
	public double[] highpass (double[] gete) {
		return(lowpass(gete));
	}

	/************************************
	*************************************/
	public SumOfDiracs(double[] v) {
		vecteur=v;
	}
	/************************************
	*************************************/
	public SumOfDiracs() {

	}

	/********************************************
  * Get the i th sampled value of the function.
  * @param i position (knot)
  * @exception IllegalArgumentException if i is not
  *   a within 0 and the last knot (dimension()-1)
	*********************************************/
  public double getValue(int i) {
    if ((i<0) || (i>vecteur.length-1)) {
			throw new IllegalArgumentException("Incorrect parameters : "+i+", "+vecteur.length);
    }
    return(vecteur[i]);
  }
   /*********************************************
   * Set the sampled values of the function
   * to the specified array 
   **********************************************/
	public void setValues(double[] v) {
		vecteur=v;
	}

  /******************************************
  * Compute the mass (integral)
  * @param a left boundary of the interval
  * @param b right boundary of the interval
  **********************************************/
	public double mass(double a, double b) {
		double mass=0;
		for(int i=0;i<vecteur.length;i++) {
			mass+=vecteur[i];
		}
		mass=mass*Math.abs(b-a)/vecteur.length;
		return(mass);
	}



   /*********************************************
   * Set a particular value
   * @param i position (knot)
   * @param d value
   * @exception IllegalArgumentException if the parameter i is negative
   **********************************************/
   	public void setValue(int i, double d) {
  		if (i<0) {
			throw new IllegalArgumentException("The parameter must be positive : "+i);
      	}
       	vecteur[i]=d;
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
	public double[] evaluate (int jfin) {
		return(interpolate(jfin));
	}
}
