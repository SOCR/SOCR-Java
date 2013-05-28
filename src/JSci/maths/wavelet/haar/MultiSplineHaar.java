package JSci.maths.wavelet.haar;

import JSci.maths.*;
import JSci.maths.wavelet.splines.*;
import JSci.maths.wavelet.*;

/****************************************
* Haar Wavelets
* @author Daniel Lemire
*****************************************/
public final class MultiSplineHaar extends Multiresolution implements Filter, NumericalConstants {
	protected final static int filtretype=0;
	public MultiSplineHaar() {}

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
	public  MultiscaleFunction primaryScaling(int n0, int k) {
		return(scaling(n0,k));
	}

	public  MultiscaleFunction dualScaling(int n0, int k) {
		return(scaling(n0,k));
	}
	public  MultiscaleFunction primaryWavelet(int n0, int k) {
		return(wavelet(n0,k));
	}
	public  MultiscaleFunction dualWavelet(int n0, int k) {
		return(wavelet(n0,k));
	}
	static final double[]  vg={1d,1d};
	static final double[]  vog={1.0/SQRT2,-1.0/SQRT2};
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
	public int previousDimension(int k) {
		int i=(int) Math.round(k/2d);
		if(2*i==k) {
			return(i);
		} else {
			throw new IllegalScalingException("Odd number of values into an even Filter. Please change the number of values.");
		}
	}

	/***************************************************
	****************************************************/
	public double[] lowpass (double[] v, double[] param) {
		return(lowpass(v));
	}

	/****************************************
	* This is the implementation of the highpass
  * Filter. It is used by the interface
  * "Filter". Highpass filters are normalized
  * in order to get L2 orthonormality of the
  * resulting wavelets (when it applies).
  * See the class DiscreteHilbertSpace for
  * an implementation of the L2 integration.
	*****************************************/
	public double[] highpass (double[] v, double[] param) {
		return(highpass(v));
	}

	/**************************************

	***************************************/
	public double[] lowpass(double[] gete) {
		if(gete.length<1) {
			throw new IllegalArgumentException("The array is not long enough : "+gete.length+" < 1");
		}
		double[] sortie=new double[2*gete.length];
		for(int k=0;k<gete.length;k++) {
			sortie[2*k]+=gete[k]*vg[0];
			sortie[2*k+1]+=gete[k]*vg[1];
		}
		return(sortie);
	}
	/****************************************
	* This is the implementation of the highpass
  * Filter. It is used by the interface
  * "Filter". Highpass filters are normalized
  * in order to get L2 orthonormality of the
  * resulting wavelets (when it applies).
  * See the class DiscreteHilbertSpace for
  * an implementation of the L2 integration.
	*****************************************/
	public double[] highpass(double[] gete) {
		if(gete.length<1) {
			throw new IllegalArgumentException("The array is not long enough : "+gete.length+" < 1");
		}
		double[] sortie=new double[2*gete.length];
		for(int k=0;k<gete.length;k++) {
			sortie[2*k]+=gete[k]*vog[0];
			sortie[2*k+1]+=gete[k]*vog[1];
		}
		return(sortie);
	}


	/************************************
	*************************************/
	public static PiecewiseConstant scaling(int n0, int k) {
		if((k<0)||(n0<0)||(k>=n0)) {
			throw new IllegalArgumentException("Incorrect parameters : "+n0+", "+k);
		}
		double[] v=new double[n0];
		v[k]=1;
		return(new PiecewiseConstant(v));
	}

	/*************************************
	**************************************/
	public static PiecewiseConstant wavelet(int n0, int k) {
		if((k<0)||(n0<0)||(k>=n0)) {
			throw new IllegalArgumentException("Incorrect parameters : "+n0+", "+k);
		}
		double[] v=new double[2*n0];
		v[2*k]=vog[0];
		v[2*k+1]=vog[1];
		return(new PiecewiseConstant(v));
	}
}
