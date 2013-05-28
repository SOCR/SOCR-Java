
package JSci.maths.wavelet.daubechies4;

import JSci.maths.wavelet.*;
import JSci.maths.*;

/******************************************
* Daubechies wavelets adapted to the
* interval by Meyer. Thanks to Pierre Vial
* for the filters.
* @author Daniel Lemire
*****************************************/
public final class Daubechies4 extends Multiresolution implements Filter, NumericalConstants {
	protected final static int filtretype=6;
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
		return(new Scaling4(n0,k));
	}

	public  MultiscaleFunction dualScaling(int n0, int k) {
		return(new Scaling4(n0,k));
	}
	public  MultiscaleFunction primaryWavelet(int n0, int k) {
		return(new Wavelet4(n0,k));
	}
	public  MultiscaleFunction dualWavelet(int n0, int k) {
		return(new Wavelet4(n0,k));
	}

  
	final static double[] vg={
      -0.107148901418,
       -0.0419109651251,
       0.703739068656,
       1.13665824341,
       0.421234534204,
       -0.140317624179,
       -0.0178247014417,
       0.045570345896
	};

	final static double[] v0temp={
    0.7983434920E+00,
    0.6022023488E+00};
	final static double[] v1temp={
   -0.3918024327E-01,
    0.5194149822E-01,
   -0.4817281609E+00,
    0.8739021503E+00};
	final static double[] v2temp={
    0.1774707150E-01,
   -0.2352740580E-01,
   -0.1232594861E+00,
   -0.6575127688E-01,
   -0.9620570014E-01,
    0.9850684416E+00};
	final static double[] v3temp={
   -0.2636405192E-01,
    0.3495099166E-01,
    0.8114147375E+00,
    0.4440233637E+00,
    0.3192581817E+00,
    0.1636579832E+00,
   -0.4282797155E-01,
    0.1094933054E+00};
	final static double[] v4temp={
   -0.1670338745E-01,
    0.2214378721E-01,
   -0.1643714751E-01,
   -0.1112580065E-01,
    0.2995602574E+00,
    0.2728668922E-01,
    0.8472064764E+00,
   -0.4270166998E+00,
   -0.3309408518E-01,
    0.8460780753E-01};
	final static double[] v5temp={
    0.2727915769E-02,
   -0.3616415322E-02,
   -0.5206157868E-01,
   -0.2836107693E-01,
   -0.4413123462E-01,
   -0.1285294872E-01,
    0.4543141690E+00,
    0.8282235028E+00,
    0.3000539798E+00,
   -0.1037443976E+00,
   -0.1262470890E-01,
    0.3227612835E-01};

	final static double[] vd0temp={
    0.7629809303E+00,
   -0.6464209928E+00};
	final static double[] vd1temp={
    0.1555526564E+00,
    0.1836012627E+00,
    0.4620817399E+00,
   -0.8535657052E+00};
	final static double[] vd2temp={
    0.3793246643E+00,
    0.4477229057E+00,
    0.4284467089E+00,
    0.3973740378E+00,
   -0.2021221018E+00,
   -0.5228106220E+00};
	final static double[] vd3temp={
    0.2385999808E+00,
    0.2816233343E+00,
    0.1056438723E+00,
    0.1612498770E+00,
    0.8548427132E+00,
    0.2929411663E+00,
   -0.3647382801E-01,
   -0.9324840384E-01};
	final static double[] vd4temp={
    0.6526723701E-01,
    0.7703595299E-01,
    0.7744666349E-01,
    0.7039069048E-01,
   -0.6529437593E-01,
    0.2555397028E+00,
    0.8099281093E+00,
    0.4965300820E+00,
   -0.2995738718E-01,
   -0.7658857572E-01};
	final static double[] vd5temp={
    0.1517778948E-02,
    0.1791458518E-02,
   -0.3127686151E-02,
   -0.1031248163E-02,
    0.3237561439E-01,
   -0.1322822647E-01,
   -0.9898430026E-01,
    0.2979273659E+00,
    0.8037308261E+00,
    0.4975940939E+00,
   -0.2963560969E-01,
   -0.7576592454E-01
	};
	final static double[] v0=ArrayMath.scalarMultiply(SQRT2,v0temp);
	final static double[] v1=ArrayMath.scalarMultiply(SQRT2,v1temp);
	final static double[] v2=ArrayMath.scalarMultiply(SQRT2,v2temp);
	final static double[] v3=ArrayMath.scalarMultiply(SQRT2,v3temp);
	final static double[] v4=ArrayMath.scalarMultiply(SQRT2,v4temp);
	final static double[] v5=ArrayMath.scalarMultiply(SQRT2,v5temp);

	final static double[] vd0=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd0temp));
	final static double[] vd1=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd1temp));
	final static double[] vd2=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd2temp));
	final static double[] vd3=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd3temp));
	final static double[] vd4=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd4temp));
	final static double[] vd5=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd5temp));
	/********************************************
	* On définit ici le filtre comme tel par le
	* vecteur phvg (filtre passe-haut).
	*********************************************/
	final static double[] vgtemp=ArrayMath.scalarMultiply(1.0/SQRT2,vg);
	final static double[] phvg=WaveletMath.lowToHigh(vgtemp);
	final static double[] phv0={
    0.5979027428E+00,
   -0.7926434769E+00,
   -0.1659403671E-01,
    0.6477069526E-01,
    0.9713044594E-01,
   -0.1797030610E-01,
   -0.3192898087E-03,
    0.8162911886E-03};
	final static double[] phv1={
    0.4823971249E-01,
   -0.6395169431E-01,
    0.3010034664E+00,
    0.1718883936E+00,
   -0.8873256413E+00,
   -0.3991915695E-01,
    0.2462565991E+00,
   -0.1524149055E+00,
   -0.9080945357E-02,
    0.2321619929E-01};
	final static double[] phv2={
   -0.1162436086E-02,
    0.1541048928E-02,
    0.2218479707E-01,
    0.1208539488E-01,
    0.1880547055E-01,
    0.5476976811E-02,
   -0.8114432093E-01,
   -0.3089428579E+00,
    0.8028339176E+00,
   -0.4957693566E+00,
   -0.2962669767E-01,
    0.7574314021E-01};
	final static double[] phvd0temp={
   -0.4071236735E+00,
   -0.4805345164E+00,
    0.7323866385E+00,
    0.2189246571E+00,
    0.1377492261E+00,
    0.6432723244E-02,
   -0.5725128723E-03,
   -0.1463677232E-02};
	final static double[] phvd1temp={
   -0.1510687974E+00,
   -0.1783088929E+00,
   -0.2220387683E+00,
   -0.1860863787E+00,
    0.4453190824E+00,
   -0.7578721319E+00,
    0.2811170011E+00,
    0.9317065817E-01,
   -0.1190456353E-01,
   -0.3043501623E-01};
	final static double[] phvd2temp={
   -0.3568796396E-02,
   -0.4212306878E-02,
    0.7354216552E-02,
    0.2424802855E-02,
   -0.7612569411E-01,
    0.3110390153E-01,
    0.4970737955E+00,
   -0.8039165747E+00,
    0.2978757587E+00,
    0.9927560396E-01,
   -0.1260377436E-01,
   -0.3222260742E-01};
   final static double[] phvd0=ArrayMath.invert(phvd0temp);
   final static double[] phvd1=ArrayMath.invert(phvd1temp);
   final static double[] phvd2=ArrayMath.invert(phvd2temp);
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


	public Daubechies4 () {}

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
		for(int k=6;k<=dl0-6;k++) {
			for(int L=-4;L<4;L++){
				sortie[2*k+L-2]+=vg[L+4]*gete[k];
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
	public double[] highpass(double[] gete) {
		double[] sortie=new double[2*gete.length+filtretype];
		int dl0=gete.length-1;
		for(int k=3;k<=dl0-3;k++) {
			for(int L=-4;L<4;L++){
				sortie[2*k+ L + 4]+=phvg[L+4]*gete[k];
			}
		}
		sortie=ArrayMath.add(sortie,gete[0],phv0,0);
		int p0=sortie.length-phvd0.length;
		sortie=ArrayMath.add(sortie,gete[dl0],phvd0,p0);
		sortie=ArrayMath.add(sortie,gete[1],phv1,0);
		int p1=sortie.length-phvd1.length;
		sortie=ArrayMath.add(sortie,gete[dl0-1],phvd1,p1);
		sortie=ArrayMath.add(sortie,gete[2],phv2,0);
		int p2=sortie.length-phvd2.length;
		sortie=ArrayMath.add(sortie,gete[dl0-2],phvd2,p2);

		return(sortie);

	}


	public double[] evalScaling (int n0, int k, int j1) {
		return(Cascades.evalScaling(this,n0,j1,k));
	}

	public double[] evalWavelet (int n0, int k, int j1) {
		return(Cascades.evalWavelet(this,filtretype,n0,j1,k));
	}

}
