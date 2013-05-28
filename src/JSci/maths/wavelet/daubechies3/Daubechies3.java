
package JSci.maths.wavelet.daubechies3;

import JSci.maths.wavelet.*;
import JSci.maths.*;

/******************************************
* Daubechies wavelets adapted to the
* interval by Meyer. Thanks to Pierre Vial
* for the filters.
* @author Daniel Lemire
*****************************************/
public final class Daubechies3 extends Multiresolution implements Filter, NumericalConstants {
	protected final static int filtretype=4;
	protected final static int minlength=8;
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
		return(new Scaling3(n0,k));
	}

	public  MultiscaleFunction dualScaling(int n0, int k) {
		return(new Scaling3(n0,k));
	}
	public  MultiscaleFunction primaryWavelet(int n0, int k) {
		return(new Wavelet3(n0,k));
	}
	public  MultiscaleFunction dualWavelet(int n0, int k) {
		return(new Wavelet3(n0,k));
	}

  
	final static double[] vgtemp={
	.332670552950,
	.806891509311,
	.459877502118,
	-.135011020010,
	-.085441273882,
	.035226291882};

	final static double[] v0temp={
	0.689047760315,
  	0.724715933318};

	final static double[] v1temp={
  	0.0306457241755,
	-0.0291374408035,
	-0.986047279405,
  	0.161005005859};
	final static double[] v2temp={
  	0.0172802631418,
	-0.0164297845102,
  	0.00443452721664,
  	0.0208960300827,
	-0.924034270866,
  	0.380966943247};
	final static double[] v3temp={
  	0.027026368393,
	-0.0256962180001,
  	0.0639688879804,
  	0.381971870809,
  	0.176750229211,
  	0.404677356783,
	-0.747429432607,
  	0.308155135811};

	final static double[] vd0temp={
	  0.889500699418,
	  0.456933808921};
	final static double[] vd1temp={
	- 0.198411021088,
	  0.386241373662,
	  0.832668382346,
	  0.343677222149};
	final static double[] vd2temp={
	  0.0390630495665,
	- 0.0760429831027,
	- 0.143921176769,
	  0.456707890812,
	  0.807766275151,
	  0.33303120718};
	final static double[] vd3temp={
	- 0.000276799784313,
	  0.000538838660959,
	  0.0349752642807,
	- 0.085504178774,
	- 0.134969198046,
	  0.459895243878,
	  0.806892290508,
	  0.332670875027};




	final static double[] vg=ArrayMath.scalarMultiply(SQRT2,vgtemp);
	final static double[] vd0=ArrayMath.scalarMultiply(SQRT2,ArrayMath.invert(vd0temp));
	final static double[] vd1=ArrayMath.scalarMultiply(SQRT2,ArrayMath.invert(vd1temp));
	final static double[] vd2=ArrayMath.scalarMultiply(SQRT2,ArrayMath.invert(vd2temp));
	final static double[] vd3=ArrayMath.scalarMultiply(SQRT2,ArrayMath.invert(vd3temp));
	final static double[] v0=ArrayMath.scalarMultiply(SQRT2,v0temp);
	final static double[] v1=ArrayMath.scalarMultiply(SQRT2,v1temp);
	final static double[] v2=ArrayMath.scalarMultiply(SQRT2,v2temp);
	final static double[] v3=ArrayMath.scalarMultiply(SQRT2,v3temp);

	/********************************************
	* On définit ici le filtre comme tel par le
	* vecteur phvg (filtre passe-haut).
	*********************************************/
	final static double[] phvg=WaveletMath.lowToHigh(vgtemp);

	final static double[] phv0={
  	0.720522830617,
	-0.685061028561,
  	0.0259917491699,
	-0.101939535681,
  	0.0200641216304,
	-0.00827216838973};

	final static double[] phv1={
  	0.0639675079235,
	-0.0608192341872,
  	0.151405112504,
  	0.904072212264,
	-0.0510574707719,
	-0.18071490589,
  	0.315790060572,
	-0.130196008824};

	final static double[] phvd0temp={
	-0.409742284317,
  	0.797634233593,
	-0.419058128916,
	-0.117669342504,
  	0.0741631890253,
  	0.0305764886814};

	final static double[] phvd0=ArrayMath.invert(phvd0temp);

	final static double[] phvd1temp={
	-0.00261427398522,
  	0.00508913652903,
  	0.330328738376,
	-0.807556085169,
  	0.460310974517,
  	0.13519444871,
	-0.0854338960798,
	-0.0352232501168};


	final static double[] phvd1=ArrayMath.invert(phvd1temp);

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


	public Daubechies3 () {}
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
			throw new IllegalScalingException("The array is not long enough : "+gete.length+" < "+minlength);
		}
		double[] sortie=new double[2*gete.length-filtretype];
		int dl0=gete.length-1;
		for(int k=4;k<=dl0-4;k++) {
			for(int L=-3;L<3;L++){
				sortie[2*k+L-1]+=vg[L+3]*gete[k];
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
	public double[] highpass(double[] gete) {
		double[] sortie=new double[2*gete.length+filtretype];
		int dl0=gete.length-1;
		for(int k=2;k<=dl0-2;k++) {
			for(int L=-3;L<3;L++){
				sortie[2*k+L + 3 ]+=phvg[L+3]*gete[k];
			}
		}
		sortie=ArrayMath.add(sortie,gete[0],phv0,0);
		int p0=sortie.length-phvd0.length;
		sortie=ArrayMath.add(sortie,gete[dl0],phvd0,p0);
		sortie=ArrayMath.add(sortie,gete[1],phv1,0);
		int p1=sortie.length-phvd1.length;
		sortie=ArrayMath.add(sortie,gete[dl0-1],phvd1,p1);

		return(sortie);
	}


	public double[] evalScaling (int n0, int k, int j1) {
		return(Cascades.evalScaling(this,n0,j1,k));
	}

	public double[] evalWavelet (int n0, int k, int j1) {
		return(Cascades.evalWavelet(this,filtretype,n0,j1,k));
	}

}
