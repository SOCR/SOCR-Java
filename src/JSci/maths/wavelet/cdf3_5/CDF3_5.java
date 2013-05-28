
package JSci.maths.wavelet.cdf3_5;

import JSci.maths.wavelet.*;
import JSci.maths.*;


/******************************************
* Cohen-Daubechies-Feauveau
* with N=3 and
* Ntilde=5 adapted to the interval
* by Deslauriers-Dubuc-Lemire
* @author Daniel Lemire
*****************************************/
public final class CDF3_5 extends Multiresolution implements Filter {
	protected final static int filtretype=2;
	protected final static int minlength=12;
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
		return(MultiSpline3_5.scaling(n0,k));
	}

	public  MultiscaleFunction dualScaling(int n0, int k) {
		return(new DualScaling3_5(n0,k));
	}
	public  MultiscaleFunction primaryWavelet(int n0, int k) {
		return(MultiSpline3_5.wavelet(n0,k));
	}
	public  MultiscaleFunction dualWavelet(int n0, int k) {
		return(new DualWavelet3_5(n0,k));
	}
	/********************************************

	*********************************************/
	final static double[] v0={8703/5120d, 14851/5120d, -7497/2560d, -4137/2560d, 4703/2560d, 5799/2560d, -3315/1024d, 1105/1024d};
	final static double[] v1={-983/5120d, 2949/5120d, 4977/2560d, 1617/2560d, -2423/2560d, -2559/2560d, 1515/1024d, -505/1024d};
	final static double[] v2={2897/51200d, -8691/51200d, -3863/25600d, 23177/25600d, 116611/76800d, 22521/25600d, -3153/2048d, 2833/6144d, 15/256d, -5/256d};
	final static double[] v3={-789/51200d, 2367/51200d, 531/25600d, -4749/25600d, -869/25600d, 16323/25600d, 4429/2048d, -751/2048d, -97/256d, 19/256d, 15/256d, -5/256d};
	final static double[] v4={7/3200d, -21/3200d, -3/1600d, 37/1600d, -3/1600d, -99/1600d, -55/128d, 189/128d, 175/128d, -13/128d, -97/256d, 19/256d, 15/256d, -5/256d};
	final static double[] v5={0d, 0d, 0d, 0d, 0d, 0d, 17/128d, -51/128d, -13/128d, 175/128d, 175/128d, -13/128d, -97/256d, 19/256d, 15/256d, -5/256d};
	final static double[] vg={-5/256d, 15/256d, 19/256d, -97/256d, -13/128d, 175/128d, 175/128d, -13/128d, -97/256d, 19/256d, 15/256d, -5/256d};
	final static double[] vd0=ArrayMath.invert(v0);
	final static double[] vd1=ArrayMath.invert(v1);
	final static double[] vd2=ArrayMath.invert(v2);
	final static double[] vd3=ArrayMath.invert(v3);
	final static double[] vd4=ArrayMath.invert(v4);
	final static double[] vd5=ArrayMath.invert(v5);


	/********************************************
	*********************************************/
	final static double[] phvg={-1/2d, 3/2d, -3/2d, 1/2d};

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
		return(Cascades.previousDimension(filtretype,k));
	}


	public CDF3_5 () {}

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
		if(gete.length<12) {
			throw new IllegalScalingException("The array is not long enough : "+gete.length+" < 12");
		}
		double[] sortie=new double[2*gete.length-2];
		int dl0=gete.length-1;
		for(int k=6;k<=dl0-6;k++) {
			for(int L=-6;L<=5;L++){
				sortie[2*k+L]+=vg[L+6]*gete[k];
			}
		}
		sortie=ArrayMath.add(sortie,gete[0],v0,0);
		sortie=ArrayMath.add(sortie,gete[1],v1,0);
		sortie=ArrayMath.add(sortie,gete[2],v2,0);
		sortie=ArrayMath.add(sortie,gete[3],v3,0);
		sortie=ArrayMath.add(sortie,gete[4],v4,0);
		sortie=ArrayMath.add(sortie,gete[5],v5,0);

		int p0=sortie.length-vd0.length;
		int p1=sortie.length-vd1.length;
		int p2=sortie.length-vd2.length;
		int p3=sortie.length-vd3.length;
		int p4=sortie.length-vd4.length;
		int p5=sortie.length-vd5.length;

		sortie=ArrayMath.add(sortie,gete[dl0],vd0,p0);
		sortie=ArrayMath.add(sortie,gete[dl0-1],vd1,p1);
		sortie=ArrayMath.add(sortie,gete[dl0-2],vd2,p2);
		sortie=ArrayMath.add(sortie,gete[dl0-3],vd3,p3);
		sortie=ArrayMath.add(sortie,gete[dl0-4],vd4,p4);
		sortie=ArrayMath.add(sortie,gete[dl0-5],vd5,p5);

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
		if(v.length<4) {
			throw new IllegalScalingException("The array is not long enough : "+v.length+" < 4");
		}
		double[] ans=new double[2*v.length+2];
		for(int k=0;k<v.length;k++) {
			ans[2*k]+=v[k]*phvg[0];
			ans[2*k+1]+=v[k]*phvg[1];
			ans[2*k+2]+=v[k]*phvg[2];
			ans[2*k+3]+=v[k]*phvg[3];
		}
		return(ans);
	}

	/************************************
	**************************************/
	public double[] evalScaling (int n0, int k, int j1) {
		return(Cascades.evalScaling(this,n0,j1,k));
	}

	/************************************
	**************************************/
	public double[] evalWavelet (int n0, int k, int j1) {
		return(Cascades.evalWaveletQuadratic(this,n0,j1,k));
	}

}
