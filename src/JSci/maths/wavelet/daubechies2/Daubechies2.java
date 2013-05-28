
package JSci.maths.wavelet.daubechies2;

import JSci.maths.wavelet.*;
import JSci.maths.*;

/******************************************
* Daubechies wavelets adapted to the
* interval by Meyer. Thanks to Pierre Vial
* for the filters.
* @author Daniel Lemire
*****************************************/
public final class Daubechies2 extends Multiresolution implements Filter, NumericalConstants {
	protected final static int filtretype=2;
	protected final static int minlength=4;

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
		return(new Scaling2(n0,k));
	}

	public  MultiscaleFunction dualScaling(int n0, int k) {
		return(new Scaling2(n0,k));
	}
	public  MultiscaleFunction primaryWavelet(int n0, int k) {
		return(new Wavelet2(n0,k));
	}
	public  MultiscaleFunction dualWavelet(int n0, int k) {
		return(new Wavelet2(n0,k));
	}


  
	final static double[] vgtemp={0.482962913145,0.836516303738,0.224143868042,-0.129409522551};
	final static double[] v0temp={0.848528137424,-0.529150262213};
	final static double[] v1temp={0.132287565553, 0.212132034356, 0.838525491562,- 0.484122918276};

	final static double[] vd0temp={
	0.848528137424,
  	0.529150262213};

	final static double[] vd1temp={
	-0.132287565553,
  	0.212132034356,
 	0.838525491562,
  	0.484122918276};

	final static double[] vg=ArrayMath.scalarMultiply(SQRT2,vgtemp);
	final static double[] vd0=ArrayMath.scalarMultiply(SQRT2,ArrayMath.invert(vd0temp));
	final static double[] vd1=ArrayMath.scalarMultiply(SQRT2,ArrayMath.invert(vd1temp));
	final static double[] v0=ArrayMath.scalarMultiply(SQRT2,v0temp);
	final static double[] v1=ArrayMath.scalarMultiply(SQRT2,v1temp);


	/********************************************
	* On définit ici le filtre comme tel par le
	* vecteur phvg (filtre passe-haut).
	*********************************************/
	final static double[] phvg=WaveletMath.lowToHigh(vgtemp);
	final static double[] phv0={-0.512347538298, -0.821583836258, 0.216506350947, -0.125};

	final static double[] phvd0temp={
  	0.512347538298,
	-0.821583836258,
  	0.216506350946,
  	0.125};
	final static double[] phvd0=ArrayMath.invert(phvd0temp);

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


	public Daubechies2 () {}

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
		if(gete.length<minlength) {
			throw new IllegalScalingException("The array is not long enough: "+gete.length+" < "+minlength);
		}
		double[] sortie=new double[2*gete.length-2];
		int dl0=gete.length-1;
		for(int k=2;k<=dl0-2;k++) {
			for(int L=-2;L<2;L++){
				sortie[2*k+L]+=vg[L+2]*gete[k];
			}
		}
		sortie=ArrayMath.add(sortie,gete[0],v0,0);
		sortie=ArrayMath.add(sortie,gete[1],v1,0);
		int p0=sortie.length-vd0.length;
		int p1=sortie.length-vd1.length;
		sortie=ArrayMath.add(sortie,gete[dl0],vd0,p0);
		sortie=ArrayMath.add(sortie,gete[dl0-1],vd1,p1);
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
		double[] sortie=new double[2*gete.length+2];
		int dl0=gete.length-1;
		for(int k=1;k<=dl0-1;k++) {
			for(int L=-2;L<2;L++){
				sortie[2*k+L + 2 ]+=phvg[L+2]*gete[k];
			}
		}
		sortie=ArrayMath.add(sortie,gete[0],phv0,0);
		int p0=sortie.length-phvd0.length;
		sortie=ArrayMath.add(sortie,gete[dl0],phvd0,p0);
		return(sortie);
	}


	public double[] evalScaling (int n0, int k, int j1) {
		return(Cascades.evalScaling(this,n0,j1,k));
	}

	public double[] evalWavelet (int n0, int k, int j1) {
		return(Cascades.evalWavelet(this,filtretype,n0,j1,k));
		//return(Cascades.evalWavelet(this,n0,j1,k));
	}

}
