
package JSci.maths.wavelet.cdf2_4;

import JSci.maths.wavelet.*;
import JSci.maths.*;

/******************************************
* Cohen-Daubechies-Feauveau
* with N=2 and
* Ntilde=4 adapted to the interval
* by Deslauriers-Dubuc-Lemire
* @author Daniel Lemire
*****************************************/
public final class CDF2_4 extends Multiresolution implements Filter {
	protected final static int filtretype=1;
	protected final static int minlength=5;
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
		return(MultiSpline2_4.scaling(n0,k));
	}

	public  MultiscaleFunction dualScaling(int n0, int k) {
		return(new DualScaling2_4(n0,k));
	}
	public  MultiscaleFunction primaryWavelet(int n0, int k) {
		return(MultiSpline2_4.wavelet(n0,k));
	}
	public  MultiscaleFunction dualWavelet(int n0, int k) {
		return(new DualWavelet2_4(n0,k));
	}

	/********************************************
	*********************************************/
	final static double[] vg={3/64d,-3/32d,-1/4d,19/32d,45/32d,19/32d,-1/4d,-3/32d,3/64d};
	final static double[] v3={-5/256d,5/128d,1/64d,-9/128d,-67/256d,19/32d,45/32d,19/32d,-1/4d,-3/32d,3/64d};
	final static double[] v2={41/384d,-41/192d,-13/96d,31/64d,187/128d,19/32d,-1/4d,-3/32d,3/64d};
	final static double[] v1={-241/768d,241/384d,245/192d,105/128d,-93/256d,-3/32d,3/64d};
	final static double[] v0={93/64d,35/32d,-5/16d,-15/32d,15/64d};
	final static double[] vd0=ArrayMath.invert(v0);
	final static double[] vd1=ArrayMath.invert(v1);
	final static double[] vd2=ArrayMath.invert(v2);
	final static double[] vd3=ArrayMath.invert(v3);
	/********************************************
	*********************************************/
	final static double[] phvg={-1/2d,1d,-1/2d};
	final static double[] phv0={1d,-1/2d};

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
			throw new IllegalScalingException("Even number of values into an odd Filter! Change the number of iterations/values.");
		}
	}


	public CDF2_4 () {}
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
  * "Filter". Highpass filters are normalized
  * in order to get L2 orthonormality of the
  * resulting wavelets (when it applies).
  * See the class DiscreteHilbertSpace for
  * an implementation of the L2 integration.
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
		if(gete.length<8) {
			throw new IllegalScalingException("The array is not long enough : "+gete.length+" < 8");
		}
		double[] sortie=new double[2*gete.length-1];
		int dl0=gete.length-1;
		for(int k=4;k<=dl0-4;k++) {
			for(int L=-4;L<=4;L++){
				sortie[2*k+L]+=vg[L+4]*gete[k];
			}
		}
		sortie=ArrayMath.add(sortie,gete[0],v0,0);
		sortie=ArrayMath.add(sortie,gete[1],v1,0);
		sortie=ArrayMath.add(sortie,gete[2],v2,0);
		sortie=ArrayMath.add(sortie,gete[3],v3,0);
		int p0=sortie.length-vd0.length;
		int p1=sortie.length-vd1.length;
		int p2=sortie.length-vd2.length;
		int p3=sortie.length-vd3.length;
		sortie=ArrayMath.add(sortie,gete[dl0],vd0,p0);
		sortie=ArrayMath.add(sortie,gete[dl0-1],vd1,p1);
		sortie=ArrayMath.add(sortie,gete[dl0-2],vd2,p2);
		sortie=ArrayMath.add(sortie,gete[dl0-3],vd3,p3);
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
	public double[] highpass(double[] v) {
		if(v.length<2) {
			throw new IllegalScalingException("The array is not long enough : "+v.length+" < 2");
		}
		double[] data=Cascades.supersample(v);
		int dl=data.length-1;
		double[] sortie=new double[data.length];
		/********************************************
		*********************************************/
		for(int k=1;k<=dl-1;k++) {
			sortie[k]=ArrayMath.scalarProduct(ArrayMath.extract(k-1,k+1,data),phvg);
		}
		sortie[0]=ArrayMath.scalarProduct(ArrayMath.extract(0,1,data),phv0);
		sortie[dl]=ArrayMath.scalarProduct(ArrayMath.extract(dl,dl-1,data),phv0);
		return(sortie);
	}

	/************************************
	**************************************/
	public double[] evalScaling (int n0, int k, int j1) {
		return(Cascades.evalScaling(this,n0,j1,k));
	}

	/************************************

	**************************************/
	public double[] evalWavelet (int n0, int k, int j1) {
		return(Cascades.evalWavelet(this,n0,j1,k));
	}

}
