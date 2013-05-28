
package JSci.maths.wavelet.daubechies6;

import JSci.maths.wavelet.*;
import JSci.maths.*;
import JSci.maths.wavelet.*;

/******************************************
* Daubechies wavelets adapted to the
* interval by Meyer. Thanks to Pierre Vial
* for the filters.
* @author Daniel Lemire
*****************************************/
public final class Daubechies6 extends Multiresolution implements Filter, NumericalConstants {
	protected final static int filtretype=10;
	protected final static int minlength=20;
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
		return(new Scaling6(n0,k));
	}

	public  MultiscaleFunction dualScaling(int n0, int k) {
		return(new Scaling6(n0,k));
	}
	public  MultiscaleFunction primaryWavelet(int n0, int k) {
		return(new Wavelet6(n0,k));
	}
	public  MultiscaleFunction dualWavelet(int n0, int k) {
		return(new Wavelet6(n0,k));
	}

	final static double[] vg={
       0.0217847003266,
       0.00493661237185,
      - 0.166863215412,
       - 0.0683231215866,
       0.694457972958,
       1.11389278393,
       0.477904371333,
       - 0.102724969862,
       - 0.0297837512985,
       0.06325056266,
       0.00249992209279,
       - 0.0110318675094};

	final static double[] v0temp={
    0.8035811728E+00,
   -0.5951951771E+00};
	final static double[] v1temp={
    0.2409444375E+00,
    0.3253023901E+00,
    0.8139624945E+00,
   -0.4166403612E+00};
	final static double[] v2temp={
   -0.1713726513E+00,
   -0.2313725672E+00,
    0.2850349985E+00,
    0.2770986033E+00,
    0.5575844464E+00,
   -0.6694543662E+00};
	final static double[] v3temp={
   -0.2876454491E-02,
   -0.3883540664E-02,
   -0.5655242446E-01,
   -0.1151783354E+00,
    0.5730520088E+00,
    0.4076173301E+00,
   -0.4430745754E+00,
   -0.5409776766E+00};
	final static double[] v4temp={
    0.9214100804E-01,
    0.1244008388E+00,
    0.7306532844E-01,
    0.2931573652E+00,
   -0.1321263657E+00,
   -0.2417688989E-01,
   -0.3632781234E+00,
    0.6792063579E-01,
    0.1887648043E+00,
   -0.8333645213E+00};
	final static double[] v5temp={
   -0.1061491551E+00,
   -0.1433134304E+00,
   -0.2622879500E+00,
   -0.6856964195E+00,
    0.2766563635E+00,
   -0.8836692218E-01,
    0.3328884755E+00,
    0.1288342707E+00,
    0.5008585553E-01,
   -0.4619028205E+00,
    0.8828610310E-02,
   -0.3895963779E-01};
	final static double[] v6temp={
   -0.2023876977E-01,
   -0.2732464068E-01,
    0.4220601869E-01,
    0.4941654301E-01,
    0.3803171587E-01,
    0.8472554296E-01,
    0.1946516826E+00,
   -0.6992838718E-01,
    0.9438761468E+00,
    0.1295249721E+00,
   -0.7015973081E-01,
    0.1735085659E+00,
    0.4990121912E-02,
   -0.2202083175E-01};
	final static double[] v7temp={
    0.5456011297E-01,
    0.7366235691E-01,
   -0.1344279366E+00,
   -0.1735568822E+00,
    0.3102530241E-02,
   -0.1659152277E+00,
   -0.3869255752E+00,
    0.2453596290E+00,
    0.2217987487E+00,
    0.1874151629E+00,
    0.7052410111E+00,
   -0.3333463173E+00,
   -0.4413043778E-01,
    0.1047852370E+00,
    0.3298337216E-02,
   -0.1455518125E-01};
	final static double[] v8temp={
   -0.4678407876E-02,
   -0.6316382647E-02,
    0.3592640703E-03,
   -0.6935341643E-02,
    0.4493043023E-02,
    0.4405183141E-02,
    0.2315284183E-01,
   -0.9374856771E-02,
   -0.1151952189E+00,
   -0.4165806865E-01,
    0.4741171777E+00,
    0.7976083337E+00,
    0.3391664841E+00,
   -0.7534748748E-01,
   -0.2114679112E-01,
    0.4508434214E-01,
    0.1768528334E-02,
   -0.7804311311E-02};
	final static double[] v9temp={
    0.4182788817E-04,
    0.5647240556E-04,
   -0.3442465609E-04,
    0.1028288644E-05,
    0.1389588842E-03,
    0.7128147780E-04,
   -0.1053053091E-03,
    0.2899067329E-03,
    0.1541349274E-01,
    0.3547130473E-02,
   -0.1181216165E+00,
   -0.4823069885E-01,
    0.4910659254E+00,
    0.7876202163E+00,
    0.3379287356E+00,
   -0.7263467678E-01,
   -0.2106028481E-01,
    0.4472486618E-01,
    0.1767711923E-02,
   -0.7800708584E-02};

	final static double[] vd0temp={
    0.6512594203E+00,
    0.7588551689E+00};

	final static double[] vd1temp={
   -0.6896282953E-01,
    0.5918480130E-01,
   -0.9848250307E+00,
    0.1478544768E+00};

	final static double[] vd2temp={
   -0.1667727153E-01,
    0.1431265231E-01,
    0.1090239805E+00,
    0.7126760308E+00,
   -0.1719730277E+00,
    0.6709313982E+00};

	final static double[] vd3temp={
    0.9785571254E-01,
   -0.8398105097E-01,
    0.6834310191E-02,
    0.1247808574E+00,
    0.7236789907E-01,
   -0.1108818957E+00,
    0.1804007431E+00,
    0.9579551851E+00};

	final static double[] vd4temp={
   -0.5378738739E+00,
    0.4616103858E+00,
    0.2733949616E-01,
   -0.2535533852E+00,
    0.3484637175E+00,
    0.3309876136E+00,
   -0.2950887461E+00,
    0.1958019515E+00,
    0.6099724771E-01,
    0.2691738861E+00};

	final static double[] vd5temp={
   -0.2335808916E+00,
    0.2004621729E+00,
    0.5067312783E-01,
    0.1483313327E+00,
    0.4193562472E+00,
   -0.6838768540E-01,
    0.8092621294E+00,
   -0.1702431983E+00,
   -0.5881963586E-01,
   -0.1104006534E+00,
    0.5469174586E-02,
    0.2413483588E-01};

	final static double[] vd6temp={
    0.7905558067E-01,
   -0.6784653218E-01,
   -0.2413743535E-02,
    0.4795434784E-01,
   -0.2979345169E-01,
   -0.5477003063E-01,
    0.1575224560E+00,
   -0.5400590327E-01,
    0.8600210456E+00,
    0.4427411664E+00,
   -0.5350546863E-01,
   -0.1279990455E+00,
    0.3964078767E-02,
    0.1749302183E-01};

	final static double[] vd7temp={
    0.4472356538E-01,
   -0.3838234812E-01,
   -0.1245771188E-02,
    0.2792643621E-01,
   -0.2285669131E-01,
   -0.3338969826E-01,
    0.4201463682E-01,
   -0.2161240035E-01,
   -0.6527800067E-01,
    0.3288435947E+00,
    0.7890314114E+00,
    0.4902682296E+00,
   -0.4839771981E-01,
   -0.1181080824E+00,
    0.3500297801E-02,
    0.1544640999E-01};

	final static double[] vd8temp={
   -0.2366701878E-04,
    0.2031134472E-04,
   -0.3373738591E-03,
   -0.2266339823E-02,
   -0.2165041937E-02,
    0.1906206337E-02,
   -0.7087323880E-02,
    0.2020684368E-02,
    0.4460763770E-01,
   -0.2107187698E-01,
   -0.7263524048E-01,
    0.3379418668E+00,
    0.7876410397E+00,
    0.4910540301E+00,
   -0.4831177914E-01,
   -0.1179902457E+00,
    0.3490713064E-02,
    0.1540411365E-01};

	final static double[] vd9temp={
   -0.1333785556E-05,
    0.1144672190E-05,
    0.4933806493E-06,
    0.2205985224E-05,
   -0.3259284258E-06,
   -0.2564526296E-05,
   -0.1197305244E-05,
   -0.1010124086E-06,
   -0.7800395608E-02,
    0.1767376409E-02,
    0.4472504864E-01,
   -0.2106027212E-01,
   -0.7263753168E-01,
    0.3379294037E+00,
    0.7876411418E+00,
    0.4910559454E+00,
   -0.4831174259E-01,
   -0.1179901111E+00,
    0.3490712084E-02,
    0.1540410933E-01};

	final static double[] v0=ArrayMath.scalarMultiply(SQRT2,v0temp);
	final static double[] v1=ArrayMath.scalarMultiply(SQRT2,v1temp);
	final static double[] v2=ArrayMath.scalarMultiply(SQRT2,v2temp);
	final static double[] v3=ArrayMath.scalarMultiply(SQRT2,v3temp);
	final static double[] v4=ArrayMath.scalarMultiply(SQRT2,v4temp);
	final static double[] v5=ArrayMath.scalarMultiply(SQRT2,v5temp);
	final static double[] v6=ArrayMath.scalarMultiply(SQRT2,v6temp);
	final static double[] v7=ArrayMath.scalarMultiply(SQRT2,v7temp);
	final static double[] v8=ArrayMath.scalarMultiply(SQRT2,v8temp);
	final static double[] v9=ArrayMath.scalarMultiply(SQRT2,v9temp);

	final static double[] vd0=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd0temp));
	final static double[] vd1=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd1temp));
	final static double[] vd2=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd2temp));
	final static double[] vd3=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd3temp));
	final static double[] vd4=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd4temp));
	final static double[] vd5=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd5temp));
	final static double[] vd6=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd6temp));
	final static double[] vd7=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd7temp));
	final static double[] vd8=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd8temp));
	final static double[] vd9=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd9temp));


	/********************************************
	* On définit ici le filtre comme tel par le
	* vecteur phvg (filtre passe-haut).
	*********************************************/
	final static double[] vgtemp=ArrayMath.scalarMultiply(1.0/SQRT2,vg);
	final static double[] phvg=WaveletMath.lowToHigh(vgtemp);
	final static double[] phv0={
   -0.4056098474E+00,
   -0.5476194186E+00,
    0.3345888248E+00,
   -0.8469145514E-02,
    0.2860429450E-02,
    0.4344317225E+00,
   -0.1793040681E+00,
    0.4501354334E+00,
   -0.7823562605E-02,
   -0.2169451337E-03,
    0.3443284776E-06,
   -0.1518815163E-05};
	final static double[] phv1={
   -0.2714331420E+00,
   -0.3664656082E+00,
    0.8617650411E-01,
   -0.2747405494E+00,
   -0.5148935461E+00,
   -0.3097406490E+00,
   -0.2206953036E+00,
   -0.5444907507E+00,
    0.4953215623E-01,
   -0.2013924147E-01,
   -0.1989883688E-02,
    0.4859370755E-02,
    0.1437935110E-03,
   -0.6345441585E-03};
	final static double[] phv2={
   -0.7388896229E-01,
   -0.9975850151E-01,
    0.1991481291E+00,
    0.2684432545E+00,
   -0.1045213665E-01,
    0.2405920775E+00,
    0.5401581689E+00,
   -0.3490563093E+00,
   -0.6165711214E-01,
   -0.1943705670E+00,
    0.5024780923E+00,
   -0.3169028187E+00,
   -0.3084242027E-01,
    0.7560970841E-01,
    0.2218060423E-02,
   -0.9788044508E-02};
	final static double[] phv3={
    0.2329741271E-02,
    0.3145415646E-02,
   -0.1484555312E-03,
    0.3513131058E-02,
   -0.2412185766E-02,
   -0.2301643677E-02,
   -0.1162880173E-01,
    0.4467413223E-02,
    0.4333253688E-01,
    0.1764767584E-01,
   -0.6404967457E-01,
   -0.3432838094E+00,
    0.7870194497E+00,
   -0.4897071000E+00,
   -0.4826799594E-01,
    0.1178085687E+00,
    0.3490290194E-02,
   -0.1540224757E-01};
	final static double[] phv4={
   -0.2118182730E-04,
   -0.2859787559E-04,
    0.1743279790E-04,
   -0.5207298811E-06,
   -0.7036938958E-04,
   -0.3609725256E-04,
    0.5332707089E-04,
   -0.1468100425E-03,
   -0.7805460453E-02,
   -0.1796282458E-02,
    0.4479149901E-01,
    0.2101925260E-01,
   -0.7264258737E-01,
   -0.3379188415E+00,
    0.7876414895E+00,
   -0.4910573861E+00,
   -0.4831174654E-01,
    0.1179901294E+00,
    0.3490712054E-02,
   -0.1540410920E-01};
	final static double[] phvd0temp={
    0.4378023718E+00,
   -0.3757277153E+00,
   -0.6873475431E-01,
   -0.1032247812E+00,
    0.7266439214E+00,
    0.3259673475E+00,
   -0.1186803367E+00,
   -0.5853826628E-01,
   -0.3032048461E-03,
   -0.4670847828E-04,
    0.1703850068E-05,
    0.7518930104E-05};
	final static double[] phvd1temp={
   -0.9713836045E-01,
    0.8336541004E-01,
    0.1006861439E+00,
    0.5919694503E+00,
    0.3702101657E+00,
   -0.5544630394E+00,
   -0.4149317683E+00,
   -0.7460178119E-01,
    0.2790428530E-01,
    0.5912930318E-02,
   -0.1712035030E-02,
   -0.3636645827E-02,
    0.1436692465E-03,
    0.6339957943E-03};
	final static double[] phvd2temp={
   -0.8958975532E-01,
    0.7688709850E-01,
    0.1185119667E-02,
   -0.6467017684E-01,
    0.3743730652E-01,
    0.7423011440E-01,
   -0.1113332259E+00,
    0.5103740064E-01,
    0.4816519926E+00,
   -0.7799800339E+00,
    0.3345221160E+00,
    0.7239621136E-01,
   -0.2085354320E-01,
   -0.4433542083E-01,
    0.1748540164E-02,
    0.7716105825E-02};
	final static double[] phvd3temp={
    0.4166215409E-04,
   -0.3575500495E-04,
    0.6680919371E-03,
    0.4483753011E-02,
    0.4274088494E-02,
   -0.3773957888E-02,
    0.1399084946E-01,
   -0.3990648231E-02,
   -0.1177575130E+00,
    0.4833340703E-01,
    0.4910525706E+00,
   -0.7876666869E+00,
    0.3379295212E+00,
    0.7264103605E-01,
   -0.2106021362E-01,
   -0.4472460633E-01,
    0.1767709932E-02,
    0.7800699800E-02};
	final static double[] phvd4temp={
    0.2633834884E-05,
   -0.2260391508E-05,
   -0.9742819475E-06,
   -0.4356173277E-05,
    0.6436128145E-06,
    0.5064186603E-05,
    0.2364326456E-05,
    0.1994698649E-06,
    0.1540349180E-01,
   -0.3490049659E-02,
   -0.1179904012E+00,
    0.4831170232E-01,
    0.4910559595E+00,
   -0.7876411054E+00,
    0.3379294202E+00,
    0.7263751587E-01,
   -0.2106029251E-01,
   -0.4472490177E-01,
    0.1767711864E-02,
    0.7800708325E-02};

	final static double[] phvd0=ArrayMath.invert(phvd0temp);
	final static double[] phvd1=ArrayMath.invert(phvd1temp);
	final static double[] phvd2=ArrayMath.invert(phvd2temp);
	final static double[] phvd3=ArrayMath.invert(phvd3temp);
	final static double[] phvd4=ArrayMath.invert(phvd4temp);

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


	public Daubechies6 () {}
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
		for(int k=10;k<=dl0-10;k++) {
			for(int L=-6;L<6;L++){
				sortie[2*k+L-4]+=vg[L+6]*gete[k];
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
		sortie=ArrayMath.add(sortie,gete[8],v8,0);
		sortie=ArrayMath.add(sortie,gete[9],v9,0);
		int p0=sortie.length-vd0.length;
		int p1=sortie.length-vd1.length;
		int p2=sortie.length-vd2.length;
		int p3=sortie.length-vd3.length;
		int p4=sortie.length-vd4.length;
		int p5=sortie.length-vd5.length;
		int p6=sortie.length-vd6.length;
		int p7=sortie.length-vd7.length;
		int p8=sortie.length-vd8.length;
		int p9=sortie.length-vd9.length;
		sortie=ArrayMath.add(sortie,gete[dl0],vd0,p0);
		sortie=ArrayMath.add(sortie,gete[dl0-1],vd1,p1);
		sortie=ArrayMath.add(sortie,gete[dl0-2],vd2,p2);
		sortie=ArrayMath.add(sortie,gete[dl0-3],vd3,p3);
		sortie=ArrayMath.add(sortie,gete[dl0-4],vd4,p4);
		sortie=ArrayMath.add(sortie,gete[dl0-5],vd5,p5);
		sortie=ArrayMath.add(sortie,gete[dl0-6],vd6,p6);
		sortie=ArrayMath.add(sortie,gete[dl0-7],vd7,p7);
		sortie=ArrayMath.add(sortie,gete[dl0-8],vd8,p8);
		sortie=ArrayMath.add(sortie,gete[dl0-9],vd9,p9);
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
		if(gete.length<10) {
			throw new IllegalScalingException("The array is not long enough : "+gete.length+" < 10");
		}
		double[] sortie=new double[2*gete.length+filtretype];
		int dl0=gete.length-1;
		for(int k=5;k<=dl0-5;k++) {
			for(int L=-6;L<6;L++){
				sortie[2*k+L + 6 ]+=phvg[L+6]*gete[k];
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
		sortie=ArrayMath.add(sortie,gete[4],phv4,0);
		int p4=sortie.length-phvd4.length;
		sortie=ArrayMath.add(sortie,gete[dl0-4],phvd4,p4);

		return(sortie);
	}



	public double[] evalScaling (int n0, int k, int j1) {
		return(Cascades.evalScaling(this,n0,j1,k));
	}


	public double[] evalWavelet (int n0, int k, int j1) {
		return(Cascades.evalWavelet(this,filtretype,n0,j1,k));
	}

}
