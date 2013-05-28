package JSci.maths.wavelet.daubechies5;

import JSci.maths.wavelet.*;
import JSci.maths.*;

/******************************************
* Daubechies wavelets adapted to the
* interval by Meyer. Thanks to Pierre Vial
* for the filters.
* @author Daniel Lemire
*****************************************/
public final class Daubechies5 extends Multiresolution implements Filter, NumericalConstants {
	protected final static int filtretype=8;
	protected final static int minlength=16;
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
		return(new Scaling5(n0,k));
	}

	public  MultiscaleFunction dualScaling(int n0, int k) {
		return(new Scaling5(n0,k));
	}
	public  MultiscaleFunction primaryWavelet(int n0, int k) {
		return(new Wavelet5(n0,k));
	}
	public  MultiscaleFunction dualWavelet(int n0, int k) {
		return(new Wavelet5(n0,k));
	}

  
	final static double[] vg={
       0.0386547959548,
       0.0417468644215,
       - 0.0553441861166,
      0.281990696854,
      1.02305296689,
      0.89658164838,
      0.0234789231361,
       - 0.247951362613,
      - 0.0298424998687,
      0.0276321529578};

	  final static double[] v0temp={
    	0.5149588736E+00,
    	0.8572148847E+00};
  final static double[] v1temp={
   -0.7062608421E-01,
    0.4242755162E-01,
   -0.9680855135E+00,
    0.2366902992E+00};
  final static double[] v2temp={
    0.1166375834E+00,
   -0.7006826369E-01,
    0.2569251462E-01,
    0.1524482921E+00,
   -0.8988377800E+00,
    0.3868800469E+00};
  final static double[] v3temp={
    0.4678469852E+00,
   -0.2810519985E+00,
    0.8265463674E-01,
    0.5280457443E+00,
    0.8615307089E-01,
   -0.2053530811E+00,
   -0.4422572318E+00,
    0.4138613016E+00};
  final static double[] v4temp={
   -0.1341363044E+00,
    0.8058035557E-01,
   -0.3810281736E-01,
   -0.2103133707E+00,
    0.3026059798E-01,
    0.2107415482E+00,
   -0.8553851730E+00,
   -0.3335028061E+00,
   -0.1496490635E+00,
    0.1385649939E+00};
  final static double[] v5temp={
   -0.2809362553E+00,
    0.1687682052E+00,
   -0.4001455131E-01,
   -0.2777441959E+00,
    0.9006724682E-02,
    0.2482896416E+00,
   -0.9917949547E-01,
    0.8098971461E+00,
   -0.2582323890E+00,
   -0.1241046836E+00,
   -0.4791583558E-01,
    0.4436684941E-01};
  final static double[] v6temp={
   -0.8360649632E-01,
    0.5022533782E-01,
   -0.1533783538E-01,
   -0.9668361515E-01,
   -0.4952912706E-02,
    0.6191148032E-01,
   -0.5197035907E-01,
    0.2312568194E+00,
    0.6976664051E+00,
    0.6350643725E+00,
    0.1189068293E-01,
   -0.1727260340E+00,
   -0.2133407205E-01,
    0.1975391957E-01};
  final static double[] v7temp={
   -0.1591308791E-02,
    0.9559546817E-03,
    0.3515007474E-03,
    0.7914817671E-03,
   -0.1039035431E-02,
   -0.2096326217E-02,
    0.2722494187E-01,
    0.2963704669E-01,
   -0.3930998378E-01,
    0.1995579157E+00,
    0.7233513164E+00,
    0.6339755574E+00,
    0.1659420760E-01,
   -0.1753213559E+00,
   -0.2110191042E-01,
    0.1953895348E-01};


	final static double[] vd0temp={
    0.9116496405E+00,
    0.4109682870E+00};
	final static double[] vd1temp={
   -0.1759262671E+00,
    0.3902566773E+00,
    0.6301011968E+00,
    0.6478596732E+00};
	final static double[] vd2temp={
    0.2825379610E-03,
   -0.6267530579E-03,
    0.4175827162E+00,
   -0.4056820940E+00,
    0.6289312762E+00,
    0.5152588582E+00};
	final static double[] vd3temp={
   -0.5113299639E-01,
    0.1134281628E+00,
    0.4836999717E+00,
   -0.5526531145E+00,
   -0.2269223725E+00,
   -0.5499800740E+00,
    0.2214856571E+00,
    0.2051843068E+00};
	final static double[] vd4temp={
    0.2756002147E-02,
   -0.6113630773E-02,
   -0.1460819570E+00,
    0.1465088170E+00,
    0.1654940540E+00,
    0.3172833766E-01,
    0.9500926016E+00,
   -0.1444971260E-01,
    0.1180200176E+00,
    0.1092786192E+00};
	final static double[] vd5temp={
    0.1425700173E-01,
   -0.3162626148E-01,
   -0.7075177701E-01,
    0.9173486581E-01,
   -0.6745433251E-01,
    0.2118550693E+00,
   -0.1051600438E-01,
    0.9395202801E+00,
    0.1694709068E+00,
   -0.1464208651E+00,
    0.4001747247E-01,
    0.3705349502E-01};
	final static double[] vd6temp={
   -0.1315787444E-02,
    0.2918807090E-02,
    0.1730939444E-01,
   -0.1895045515E-01,
   -0.1537869850E-02,
   -0.2706707215E-01,
   -0.1556669346E+00,
   -0.6193750436E-05,
    0.6330645158E+00,
    0.7286274296E+00,
    0.1987062136E+00,
   -0.3988654553E-01,
    0.2953429005E-01,
    0.2734677134E-01};
	final static double[] vd7temp={
   -0.8561071719E-04,
    0.1899099809E-03,
   -0.9013424188E-06,
   -0.1367686226E-03,
   -0.3599933730E-03,
    0.3327378221E-03,
    0.1961899121E-01,
   -0.2117649690E-01,
   -0.1753299891E+00,
    0.1662647625E-01,
    0.6339762696E+00,
    0.7234044479E+00,
    0.1993976399E+00,
   -0.3913415866E-01,
    0.2951949191E-01,
    0.2733306926E-01};

	final static double[] v0=ArrayMath.scalarMultiply(SQRT2,v0temp);
	final static double[] v1=ArrayMath.scalarMultiply(SQRT2,v1temp);
	final static double[] v2=ArrayMath.scalarMultiply(SQRT2,v2temp);
	final static double[] v3=ArrayMath.scalarMultiply(SQRT2,v3temp);
	final static double[] v4=ArrayMath.scalarMultiply(SQRT2,v4temp);
	final static double[] v5=ArrayMath.scalarMultiply(SQRT2,v5temp);
	final static double[] v6=ArrayMath.scalarMultiply(SQRT2,v6temp);
	final static double[] v7=ArrayMath.scalarMultiply(SQRT2,v7temp);

	final static double[] vd0=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd0temp));
	final static double[] vd1=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd1temp));
	final static double[] vd2=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd2temp));
	final static double[] vd3=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd3temp));
	final static double[] vd4=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd4temp));
	final static double[] vd5=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd5temp));
	final static double[] vd6=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd6temp));
	final static double[] vd7=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd7temp));

	/********************************************
	* On définit ici le filtre comme tel par le
	* vecteur phvg (filtre passe-haut).
	*********************************************/
	final static double[] vgtemp=ArrayMath.scalarMultiply(1.0/SQRT2,vg);
	final static double[] phvg=WaveletMath.lowToHigh(vgtemp);
	final static double[] phv0={
    0.6182708675E+00,
   -0.3714168701E+00,
   -0.1886088129E+00,
   -0.5203639573E+00,
    0.1728721711E+00,
    0.3655404713E+00,
    0.9964278013E-01,
    0.2321755628E-02,
    0.8297752060E-04,
   -0.7683163866E-04};
	final static double[] phv1={
   -0.9199155757E-01,
    0.5526253651E-01,
    0.1276201402E+00,
    0.4846229512E+00,
    0.3921178348E+00,
    0.7493106089E+00,
    0.1219411317E+00,
   -0.8181830470E-01,
    0.2258502019E-01,
    0.4446446219E-02,
    0.3345391169E-02,
   -0.3097607805E-02};
	final static double[] phv2={
   -0.5206990422E-01,
    0.3128020722E-01,
   -0.1303591078E-01,
   -0.7446233534E-01,
    0.2030318262E-02,
    0.5628764191E-01,
   -0.1851188211E+00,
    0.7400777444E-02,
    0.6246714288E+00,
   -0.7215672985E+00,
    0.1975651316E+00,
    0.3986516515E-01,
    0.2939208418E-01,
   -0.2721509824E-01};
	final static double[] phv3={
   -0.1137543931E-02,
    0.6833623070E-03,
    0.2512696118E-03,
    0.5657891708E-03,
   -0.7427524153E-03,
   -0.1498554635E-02,
    0.1946169563E-01,
    0.2118598397E-01,
   -0.1754541390E+00,
   -0.1648697186E-01,
    0.6339417098E+00,
   -0.7234098228E+00,
    0.1993922999E+00,
    0.3913868175E-01,
    0.2951943631E-01,
   -0.2733301778E-01};

	final static double[] phvd0temp={
    0.3667623662E+00,
   -0.8135877873E+00,
    0.3605867008E+00,
    0.2389793582E+00,
    0.1721476353E-01,
   -0.1262774479E+00,
    0.1354530290E-01,
    0.7338813977E-03,
   -0.1058396455E-03,
   -0.9800040389E-04};
	final static double[] phvd1temp={
    0.2440133283E-01,
   -0.5412939880E-01,
    0.1933279241E+00,
   -0.1487960520E+00,
   -0.7215807244E+00,
    0.6068591878E+00,
    0.1499494360E+00,
   -0.1537729673E+00,
    0.4017810927E-02,
    0.4249831221E-01,
   -0.5115722859E-02,
   -0.4736816190E-02};
	final static double[] phvd2temp={
   -0.2747616845E-02,
    0.6095029686E-02,
    0.2424194881E-01,
   -0.2799508244E-01,
   -0.5956481976E-02,
   -0.3440854634E-01,
   -0.1090515083E-01,
   -0.2236505386E+00,
    0.7232999959E+00,
   -0.6265589773E+00,
    0.1577095589E-01,
    0.1743929125E+00,
   -0.2107998910E-01,
   -0.1951865580E-01};
	final static double[] phvd3temp={
   -0.1197613924E-03,
    0.2656663148E-03,
   -0.1260893172E-05,
   -0.1913265215E-03,
   -0.5035970875E-03,
    0.4654691185E-03,
    0.2744513531E-01,
   -0.2962394021E-01,
   -0.3913691676E-01,
   -0.1993634542E+00,
    0.7234039987E+00,
   -0.6339835070E+00,
    0.1660226434E-01,
    0.1753282263E+00,
   -0.2110183265E-01,
   -0.1953888146E-01};

	final static double[] phvd0=ArrayMath.invert(phvd0temp);
	final static double[] phvd1=ArrayMath.invert(phvd1temp);
	final static double[] phvd2=ArrayMath.invert(phvd2temp);
	final static double[] phvd3=ArrayMath.invert(phvd3temp);
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

	public Daubechies5 () {}
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
		for(int k=8;k<=dl0-8;k++) {
			for(int L=-5;L<5;L++){
				sortie[2*k+L-3]+=vg[L+5]*gete[k];
			}
		}
		sortie=ArrayMath.add(sortie,gete[0],v0,0);
		sortie=ArrayMath.add(sortie,gete[1],v1,0);
		sortie=ArrayMath.add(sortie,gete[2],v2,0);
		sortie=ArrayMath.add(sortie,gete[3],v3,0);
		sortie=ArrayMath.add(sortie,gete[4],v4,0);
		sortie=ArrayMath.add(sortie,gete[5],v5,0);
		sortie=ArrayMath.add(sortie,gete[6],v6,0);
		sortie=ArrayMath.add(sortie,gete[7],v7,0);
		int p0=sortie.length-vd0.length;
		int p1=sortie.length-vd1.length;
		int p2=sortie.length-vd2.length;
		int p3=sortie.length-vd3.length;
		int p4=sortie.length-vd4.length;
		int p5=sortie.length-vd5.length;
		int p6=sortie.length-vd6.length;
		int p7=sortie.length-vd7.length;
		sortie=ArrayMath.add(sortie,gete[dl0],vd0,p0);
		sortie=ArrayMath.add(sortie,gete[dl0-1],vd1,p1);
		sortie=ArrayMath.add(sortie,gete[dl0-2],vd2,p2);
		sortie=ArrayMath.add(sortie,gete[dl0-3],vd3,p3);
		sortie=ArrayMath.add(sortie,gete[dl0-4],vd4,p4);
		sortie=ArrayMath.add(sortie,gete[dl0-5],vd5,p5);
		sortie=ArrayMath.add(sortie,gete[dl0-6],vd6,p6);
		sortie=ArrayMath.add(sortie,gete[dl0-7],vd7,p7);

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
		if(gete.length<8) {
			throw new IllegalScalingException("The array is not long enough : "+gete.length+" < 8");
		}
		double[] sortie=new double[2*gete.length+filtretype];
		int dl0=gete.length-1;
		for(int k=4;k<=dl0-4;k++) {
			for(int L=-5;L<5;L++){
				sortie[2*k+L + 5 ]+=phvg[L+5]*gete[k];
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
		sortie=ArrayMath.add(sortie,gete[3],phv3,0);
		int p3=sortie.length-phvd3.length;
		sortie=ArrayMath.add(sortie,gete[dl0-3],phvd3,p3);

		return(sortie);
	}


	public double[] evalScaling (int n0, int k, int j1) {
		return(Cascades.evalScaling(this,n0,j1,k));
	}


	public double[] evalWavelet (int n0, int k, int j1) {
		return(Cascades.evalWavelet(this,filtretype,n0,j1,k));
	}

}
