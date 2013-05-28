
package JSci.maths.wavelet.daubechies8;

import JSci.maths.wavelet.*;
import JSci.maths.*;

/******************************************
* Daubechies wavelets adapted to the
* interval by Meyer. Thanks to Pierre Vial
* for the filters.
* @author Daniel Lemire
*****************************************/
public final class Daubechies8 extends Multiresolution implements Filter, NumericalConstants {
	protected final static int filtretype=14;
	protected final static int minlength=28;
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
		return(new Scaling8(n0,k));
	}

	public  MultiscaleFunction dualScaling(int n0, int k) {
		return(new Scaling8(n0,k));
	}
	public  MultiscaleFunction primaryWavelet(int n0, int k) {
		return(new Wavelet8(n0,k));
	}
	public  MultiscaleFunction dualWavelet(int n0, int k) {
		return(new Wavelet8(n0,k));
	}

	final static double[] vg={
      0.00267279339281,
       - 0.000428394300246,
       - 0.0211456865284,
       0.00538638875377,
       0.0694904659113,
       - 0.0384935212634,
       - 0.0734625087609,
       0.515398670374,
       1.09910663054,
       0.68074534719,
       - 0.0866536154058,
       - 0.202648655286,
       0.0107586117505,
       0.0448236230437,
       - 0.000766690896228,
       - 0.0047834585115};


	final static double[] v0temp={
    0.8145313640E+00,
   -0.5801195196E+00};
	final static double[] v1temp={
    0.2546778816E+00,
    0.3575868684E+00,
    0.3176080089E+00,
   -0.8404736527E+00};
	final static double[] v2temp={
   -0.2118740650E+00,
   -0.2974870959E+00,
   -0.5809146479E+00,
   -0.4102927684E+00,
    0.5953490052E+00,
   -0.7980217044E-01};
	final static double[] v3temp={
    0.7371113089E-01,
    0.1034959624E+00,
    0.3790561153E+00,
    0.2096111511E+00,
    0.5261835609E+00,
   -0.4930256875E+00,
    0.6765214691E-01,
   -0.5212625063E+00};
	final static double[] v4temp={
   -0.1657029358E-01,
   -0.2326593637E-01,
    0.2835607555E-01,
   -0.4204253027E-02,
    0.5232892196E-01,
    0.3363143855E+00,
    0.2707791658E+00,
   -0.2181631699E+00,
    0.1282592034E+00,
   -0.8632205676E+00};
	final static double[] v5temp={
    0.4725307654E-01,
    0.6634686747E-01,
   -0.1768605708E+00,
   -0.2428779496E-01,
   -0.1728053705E+00,
   -0.2496491113E+00,
   -0.1026485332E+00,
   -0.7015597057E-01,
    0.4504320556E+00,
   -0.6366908518E-01,
   -0.1272105456E+00,
   -0.7941822291E+00};
	final static double[] v6temp={
   -0.3150600071E+00,
   -0.4423679063E+00,
    0.4664265832E+00,
   -0.1074191330E+00,
    0.4591536250E-01,
   -0.1495306795E-01,
    0.3948485068E+00,
    0.2753380149E+00,
   -0.2521499192E+00,
    0.4757966099E-01,
    0.4479299855E-01,
   -0.3909345922E+00,
   -0.2305781603E-01,
   -0.1438599401E+00};
	final static double[] v7temp={
   -0.1628374040E+00,
   -0.2286359419E+00,
    0.1439556412E+00,
   -0.9221803771E-01,
   -0.1559638710E+00,
   -0.4526846422E+00,
    0.7680278703E-01,
    0.2798722613E+00,
    0.6073428962E+00,
   -0.1277567678E+00,
    0.7075076739E-02,
    0.4371314130E+00,
    0.1227511608E-01,
    0.4457322339E-01,
   -0.1100591484E-02,
   -0.6866696512E-02};
	final static double[] v8temp={
    0.1422343910E-01,
    0.1997077647E-01,
   -0.5755686815E-01,
   -0.8943575462E-02,
   -0.5113148256E-01,
   -0.2870337098E-01,
   -0.5044680204E-01,
   -0.7048754871E-01,
    0.7802410165E-01,
   -0.3357971353E-02,
    0.9574788820E+00,
   -0.6034410649E-01,
   -0.8780892537E-01,
   -0.2208827842E+00,
    0.1042138073E-01,
    0.4380269399E-01,
   -0.7294541741E-03,
   -0.4551135007E-02};
	final static double[] v9temp={
   -0.2847286228E-01,
   -0.3997803654E-01,
    0.1712399543E-01,
   -0.1916575373E-01,
   -0.2592997362E-01,
    0.5065243826E-02,
    0.4279291071E-01,
   -0.3263024185E-01,
   -0.3163483356E-01,
    0.1965128227E-01,
    0.1870478101E+00,
   -0.5633846528E-01,
    0.8568892098E+00,
    0.4372612498E+00,
   -0.6703059990E-01,
   -0.1500716835E+00,
    0.8519695716E-02,
    0.3506433161E-01,
   -0.6219669046E-03,
   -0.3880511557E-02};
	final static double[] v10temp={
   -0.1332967539E-01,
   -0.1871586512E-01,
    0.2153245326E-01,
   -0.3865012867E-02,
    0.4740812821E-02,
    0.3654642397E-02,
    0.1552794626E-01,
    0.1184719866E-01,
   -0.1683351520E-01,
    0.2573274217E-02,
    0.5123819976E-01,
   -0.3022844964E-01,
   -0.4903142965E-01,
    0.3621831839E+00,
    0.7774835776E+00,
    0.4809708581E+00,
   -0.6128478594E-01,
   -0.1432550194E+00,
    0.7610815937E-02,
    0.3170366925E-01,
   -0.5425512917E-03,
   -0.3385029882E-02};
	final static double[] v11temp={
    0.1198029623E-03,
    0.1682123545E-03,
   -0.4574159978E-03,
   -0.6498394141E-04,
   -0.5037600110E-03,
   -0.1039508974E-02,
    0.1793350305E-03,
    0.1895383041E-03,
    0.1581295871E-02,
   -0.2137732665E-03,
   -0.1495199763E-01,
    0.3830464294E-02,
    0.4910595228E-01,
   -0.2718959315E-01,
   -0.5195946631E-01,
    0.3644408152E+00,
    0.7771868548E+00,
    0.4813611500E+00,
   -0.6127351228E-01,
   -0.1432947954E+00,
    0.7607500914E-02,
    0.3169516934E-01,
   -0.5421324438E-03,
   -0.3382416650E-02};
	final static double[] v12temp={
   -0.1467161418E-04,
   -0.2060004793E-04,
    0.1949164967E-04,
   -0.5844488030E-05,
    0.7485554110E-06,
    0.9490983180E-05,
   -0.5287598342E-04,
   -0.9424653819E-05,
    0.1650150426E-06,
   -0.8931253371E-05,
    0.1889628005E-02,
   -0.3041026881E-03,
   -0.1495041138E-01,
    0.3806880495E-02,
    0.4913803765E-01,
   -0.2721895815E-01,
   -0.5194591141E-01,
    0.3644417925E+00,
    0.7771857621E+00,
    0.4813596873E+00,
   -0.6127335999E-01,
   -0.1432942437E+00,
    0.7607487339E-02,
    0.3169508789E-01,
   -0.5421323322E-03,
   -0.3382415953E-02};
	final static double[] v13temp={
   -0.1287522004E-07,
   -0.1807777568E-07,
    0.1467373756E-07,
   -0.6047676057E-08,
   -0.2557954799E-08,
    0.6767773484E-08,
   -0.4138623196E-07,
   -0.1152590396E-07,
   -0.7816962231E-09,
   -0.6457861266E-08,
   -0.1973430468E-08,
   -0.1721336728E-09,
    0.1889952080E-02,
   -0.3029230007E-03,
   -0.1495225720E-01,
    0.3808752194E-02,
    0.4913717957E-01,
   -0.2721903007E-01,
   -0.5194583809E-01,
    0.3644418949E+00,
    0.7771857517E+00,
    0.4813596513E+00,
   -0.6127335907E-01,
   -0.1432942384E+00,
    0.7607487325E-02,
    0.3169508781E-01,
   -0.5421323318E-03,
   -0.3382415951E-02};

	final static double[] vd0temp={
    0.8154844606E+00,
    0.5787789687E+00};
	final static double[] vd1temp={
   -0.1247831717E+00,
    0.1758162321E+00,
   -0.3656686728E+00,
    0.9054304139E+00};
	final static double[] vd2temp={
    0.2997291603E-02,
   -0.4223105639E-02,
    0.1242773642E-01,
    0.6252204333E-02,
   -0.9904302529E+00,
    0.1372135453E+00};
	final static double[] vd3temp={
    0.1966225161E-01,
   -0.2770359932E-01,
   -0.7546094737E+00,
   -0.2966686120E+00,
    0.3499437320E-01,
    0.3331775846E+00,
   -0.2987492141E+00,
    0.3740523778E+00};
	final static double[] vd4temp={
    0.4323222764E-02,
   -0.6091308037E-02,
    0.7954572256E-01,
    0.3390409091E-01,
    0.9217898627E-01,
    0.6563318928E+00,
    0.3523462443E+00,
   -0.1251353548E+00,
    0.4042229670E+00,
    0.4999957374E+00};
	final static double[] vd5temp={
   -0.2660208461E-01,
    0.3748164289E-01,
   -0.9461286592E-03,
   -0.1132648426E-01,
   -0.9275963658E-02,
   -0.6461897138E-01,
    0.1372894292E-01,
    0.6267297020E-01,
    0.2202320256E+00,
   -0.8389760645E-01,
   -0.1403041536E+00,
    0.9561140215E+00};
	final static double[] vd6temp={
    0.2524537323E+00,
   -0.3557007197E+00,
    0.1516837508E-01,
    0.1099880629E+00,
    0.1862464672E-01,
    0.1115880939E+00,
    0.2254220157E-01,
   -0.4913049824E-02,
   -0.5563920979E+00,
    0.2664015938E+00,
    0.3095222627E+00,
    0.2269636732E+00,
   -0.8010927222E-01,
    0.4998095408E+00};
	final static double[] vd7temp={
    0.2456578749E+00,
   -0.3461255340E+00,
    0.9925065785E-02,
    0.1050746034E+00,
   -0.4318903116E-01,
   -0.3334514090E+00,
    0.2290161361E+00,
    0.5487762846E+00,
    0.5044722993E+00,
   -0.1254772285E-02,
    0.1029941228E+00,
   -0.1417543694E+00,
   -0.2283734323E-01,
    0.2364674480E+00,
   -0.3231151084E-02,
    0.2015946352E-01};
	final static double[] vd8temp={
   -0.2349913172E+00,
    0.3310966326E+00,
   -0.5311446508E-01,
   -0.1181288132E+00,
   -0.9603473986E-02,
   -0.4380266939E-01,
    0.2600141912E+00,
    0.8361504832E-01,
    0.2510639110E-01,
   -0.1008073945E+00,
    0.8418631631E+00,
    0.7567187201E-01,
   -0.5157252055E-01,
   -0.1011324247E+00,
    0.1353427731E-01,
   -0.5842857844E-01,
   -0.8943337330E-03,
    0.5579834492E-02};
	final static double[] vd9temp={
    0.1175808867E+00,
   -0.1656684005E+00,
    0.2116820474E-01,
    0.5692304840E-01,
   -0.3212417295E-03,
   -0.1449706939E-01,
    0.7608433230E-02,
    0.8842036371E-01,
   -0.1106702915E+00,
    0.1150658911E+00,
    0.1320221928E+00,
    0.5853544071E-01,
    0.8673441259E+00,
   -0.3405381015E+00,
   -0.7026544508E-01,
    0.1612247059E+00,
    0.8814953584E-02,
   -0.3651364229E-01,
   -0.6354725161E-03,
    0.3964774371E-02};
	final static double[] vd10temp={
   -0.1869200178E+00,
    0.2633654264E+00,
   -0.1715897751E-01,
   -0.8383072732E-01,
   -0.5454552299E-04,
    0.1716903650E-01,
   -0.2459571155E-01,
   -0.1067048762E+00,
    0.1392253947E+00,
   -0.1312182020E+00,
   -0.6903207429E-01,
   -0.6174129657E-01,
    0.4567820059E+00,
    0.6659677770E+00,
    0.4075238681E+00,
   -0.8959489682E-01,
   -0.3070551742E-01,
    0.5834412826E-01,
    0.4209662663E-02,
   -0.1666652373E-01,
   -0.3299795784E-03,
    0.2058774443E-02};
	final static double[] vd11temp={
   -0.5983387981E-04,
    0.8430437497E-04,
   -0.7367030616E-03,
   -0.3221424476E-03,
    0.2377031466E-03,
    0.1801086072E-02,
    0.6525360795E-02,
    0.1852858079E-02,
   -0.1304362553E-01,
    0.4142993226E-02,
    0.2877868990E-01,
    0.7490490000E-02,
   -0.1429947491E+00,
   -0.6137476013E-01,
    0.4811678727E+00,
    0.7773035028E+00,
    0.3644701648E+00,
   -0.5200003920E-01,
   -0.2722180405E-01,
    0.4914754640E-01,
    0.3808981800E-02,
   -0.1495346270E-01,
   -0.3029283977E-03,
    0.1889999515E-02};
	final static double[] vd12temp={
    0.2133198342E-03,
   -0.3005620786E-03,
    0.1154368505E-03,
    0.1343825608E-03,
    0.1922522728E-04,
    0.1082819743E-03,
   -0.9178438545E-03,
   -0.5253253664E-03,
    0.5862560755E-03,
   -0.1372988867E-03,
   -0.3162915788E-02,
   -0.5366827388E-03,
    0.3167918922E-01,
    0.7662389238E-02,
   -0.1433799225E+00,
   -0.6122357795E-01,
    0.4813668294E+00,
    0.7771700444E+00,
    0.3644410683E+00,
   -0.5194231988E-01,
   -0.2721896821E-01,
    0.4913678918E-01,
    0.3808752164E-02,
   -0.1495225825E-01,
   -0.3029205498E-03,
    0.1889950551E-02};
	final static double[] vd13temp={
   -0.2204602645E-06,
    0.3106227586E-06,
   -0.2497015754E-06,
   -0.1915445903E-06,
   -0.3628131005E-07,
   -0.2161647330E-06,
    0.1096077744E-05,
    0.4502869954E-06,
   -0.6259648891E-06,
    0.1952007624E-06,
   -0.5855423375E-06,
   -0.5654617843E-08,
   -0.3382391093E-02,
   -0.5421772240E-03,
    0.3169514253E-01,
    0.7607457819E-02,
   -0.1432942434E+00,
   -0.6127334917E-01,
    0.4813596518E+00,
    0.7771857494E+00,
    0.3644418948E+00,
   -0.5194583784E-01,
   -0.2721902992E-01,
    0.4913717967E-01,
    0.3808752014E-02,
   -0.1495225834E-01,
   -0.3029205147E-03,
    0.1889950333E-02};


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
	final static double[] v10=ArrayMath.scalarMultiply(SQRT2,v10temp);
	final static double[] v11=ArrayMath.scalarMultiply(SQRT2,v11temp);
	final static double[] v12=ArrayMath.scalarMultiply(SQRT2,v12temp);
	final static double[] v13=ArrayMath.scalarMultiply(SQRT2,v13temp);

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
	final static double[] vd10=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd10temp));
	final static double[] vd11=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd11temp));
	final static double[] vd12=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd12temp));
	final static double[] vd13=ArrayMath.invert(ArrayMath.scalarMultiply(SQRT2,vd13temp));



	/********************************************
	* On définit ici le filtre comme tel par le
	* vecteur phvg (filtre passe-haut).
	*********************************************/
	final static double[] vgtemp=ArrayMath.scalarMultiply(1.0/SQRT2,vg);
	final static double[] phvg=WaveletMath.lowToHigh(vgtemp);
	final static double[] phv0={
   -0.2620979145E+00,
   -0.3680051517E+00,
    0.3007938848E+00,
   -0.1223236495E+00,
   -0.3815411480E-01,
    0.2223806092E+00,
   -0.7414398574E+00,
   -0.2856615192E+00,
    0.7435240137E-01,
   -0.3958129970E-01,
   -0.3312367172E-01,
    0.5296663936E-03,
   -0.2698631364E-04,
    0.4827845400E-07,
   -0.2405836391E-08,
   -0.1119370789E-07};
	final static double[] phv1={
   -0.1530044792E-01,
   -0.2148297772E-01,
   -0.1411089928E+00,
   -0.6710033379E-01,
   -0.2754652268E+00,
   -0.5621677849E+00,
   -0.2521929541E+00,
    0.8489402134E-01,
   -0.5756119710E+00,
   -0.4252477763E+00,
   -0.1717046011E-02,
    0.4224061134E-03,
   -0.2349772977E-03,
   -0.4160927634E-03,
    0.3225252044E-04,
    0.1266155053E-03,
   -0.2565139468E-05,
   -0.1600415263E-04};
	final static double[] phv2={
    0.1495099502E+00,
    0.2099231961E+00,
    0.1776433332E+00,
    0.2017476699E+00,
    0.4784490465E+00,
    0.5947478107E-01,
   -0.3620530588E+00,
    0.6528531425E+00,
    0.5286410695E-01,
   -0.2222121460E+00,
    0.5727745934E-01,
   -0.1143513897E+00,
    0.5375754041E-01,
    0.8506084034E-02,
   -0.4048226504E-02,
   -0.7458590086E-02,
    0.5621800279E-03,
    0.2220046324E-02,
   -0.4426269435E-04,
   -0.2761592227E-03};
   	final static double[] phv3={
    0.2437646831E-01,
    0.3422639182E-01,
   -0.4003214950E-01,
    0.6820587728E-02,
   -0.9838965803E-02,
   -0.9366231848E-02,
   -0.2696011202E-01,
   -0.2069757161E-01,
    0.3436761211E-01,
   -0.5103853901E-02,
   -0.1475095258E+00,
    0.6694319985E-01,
    0.4773719174E+00,
   -0.7755787502E+00,
    0.3636972524E+00,
    0.5218600648E-01,
   -0.2717665152E-01,
   -0.4912168611E-01,
    0.3801081650E-02,
    0.1492736485E-01,
   -0.3021310554E-03,
   -0.1885024820E-02};
	final static double[] phv4={
   -0.2526109188E-03,
   -0.3546846973E-03,
    0.8694036319E-03,
    0.1010912744E-03,
    0.9035494282E-03,
    0.1885121065E-02,
   -0.4586723661E-03,
   -0.3637255134E-03,
   -0.2829586692E-02,
    0.3593205922E-03,
    0.3169382593E-01,
   -0.7649438160E-02,
   -0.1432336703E+00,
    0.6121586703E-01,
    0.4813866596E+00,
   -0.7771843511E+00,
    0.3644396836E+00,
    0.5194280857E-01,
   -0.2721872350E-01,
   -0.4913606047E-01,
    0.3808725019E-02,
    0.1495209647E-01,
   -0.3029202893E-03,
   -0.1889948926E-02};
	final static double[] phv5={
    0.2622395072E-04,
    0.3682039585E-04,
   -0.3484560037E-04,
    0.1044400200E-04,
   -0.1346357753E-05,
   -0.1696820074E-04,
    0.9452329486E-04,
    0.1683706766E-04,
   -0.2973670447E-06,
    0.1596726796E-04,
   -0.3381844248E-02,
    0.5442476019E-03,
    0.3169178699E-01,
   -0.7604144409E-02,
   -0.1432957713E+00,
    0.6127323127E-01,
    0.4813597833E+00,
   -0.7771855710E+00,
    0.3644418761E+00,
    0.5194577328E-01,
   -0.2721902826E-01,
   -0.4913717014E-01,
    0.3808751988E-02,
    0.1495225819E-01,
   -0.3029205141E-03,
   -0.1889950329E-02};
	final static double[] phv6={
    0.2304180410E-07,
    0.3235242306E-07,
   -0.2626045803E-07,
    0.1082307302E-07,
    0.4577834066E-08,
   -0.1211150005E-07,
    0.7406609086E-07,
    0.2062688911E-07,
    0.1399229053E-08,
    0.1155742488E-07,
    0.3531713891E-08,
    0.3080661641E-09,
   -0.3382419079E-02,
    0.5421367809E-03,
    0.3169508577E-01,
   -0.7607487647E-02,
   -0.1432942382E+00,
    0.6127335935E-01,
    0.4813596512E+00,
   -0.7771857518E+00,
    0.3644418948E+00,
    0.5194583812E-01,
   -0.2721902992E-01,
   -0.4913717967E-01,
    0.3808752014E-02,
    0.1495225834E-01,
   -0.3029205147E-03,
   -0.1889950333E-02};

	final static double[] phvd0temp={
       0.2423397898E+00,
      -0.3414504387E+00,
      -0.1594658038E+00,
       0.3529902230E-01,
       0.1178925154E-02,
       0.5541735135E-02,
      -0.3397784131E+00,
      -0.6081576576E+00,
       0.3969712638E+00,
      -0.2244673049E+00,
       0.3235661995E+00,
       0.1864123221E-02,
      -0.3711413269E-03,
       0.1409795475E-06,
       0.1765799666E-07,
      -0.1432302065E-07};
	final static double[] phvd1temp={
       0.4513529643E-01,
      -0.6359445463E-01,
       0.4156183867E+00,
       0.1864214898E+00,
       0.8260455402E-01,
       0.5471723539E+00,
      -0.2060895161E+00,
       0.3195284622E+00,
      -0.1900505437E-01,
      -0.5728513561E+00,
       0.8401643267E-01,
      -0.7395446869E-02,
      -0.5473278975E-03,
       0.9194626204E-03,
       0.4880437220E-04,
      -0.2033336993E-03,
      -0.3477949227E-05,
       0.2169926352E-04};
	final static double[] phvd2temp={
       0.1250013206E+00,
      -0.1761235982E+00,
      -0.2969401780E+00,
      -0.6849590110E-01,
       0.1052599951E-01,
       0.9784281131E-01,
       0.7121369985E+00,
      -0.1923488214E+00,
      -0.1670869235E+00,
      -0.4966168654E+00,
      -0.1731874885E+00,
      -0.1213007854E-01,
       0.3720006761E-01,
      -0.2322200303E-01,
      -0.2923066799E-02,
       0.6878894624E-02,
       0.3616735843E-03,
      -0.1510315965E-02,
      -0.2565448314E-04,
       0.1600607975E-03};
	final static double[] phvd3temp={
       0.1176229912E+00,
      -0.1657277246E+00,
       0.1156398803E-01,
       0.5306159526E-01,
      -0.1350101618E-03,
      -0.1210974010E-01,
       0.8506023466E-02,
       0.6454872425E-01,
      -0.7545099201E-01,
       0.7860638961E-01,
       0.1423462206E-01,
       0.3160238725E-01,
      -0.6318521633E-01,
      -0.3456069087E+00,
       0.7552228032E+00,
      -0.4695151924E+00,
      -0.5958716642E-01,
       0.1395811538E+00,
       0.7391464073E-02,
      -0.3081345978E-01,
      -0.5261056159E-03,
       0.3282423723E-02};
	final static double[] phvd4temp={
      -0.1408148250E-03,
       0.1984044131E-03,
       0.3171629950E-03,
       0.7015727951E-04,
      -0.1485516761E-03,
      -0.1095011310E-02,
      -0.2896265919E-02,
      -0.6063657602E-03,
       0.6809567802E-02,
      -0.2202777079E-02,
      -0.1350306650E-01,
      -0.3747971165E-02,
       0.4898531345E-01,
       0.2723198199E-01,
      -0.5177607522E-01,
      -0.3645627219E+00,
       0.7771651866E+00,
      -0.4813205851E+00,
      -0.6127118296E-01,
       0.1432859367E+00,
       0.7607311222E-02,
      -0.3169412534E-01,
      -0.5421276476E-03,
       0.3382386726E-02};
	final static double[] phvd5temp={
      -0.1190143232E-03,
       0.1676880751E-03,
      -0.6429765381E-04,
      -0.7493111227E-04,
      -0.1071266241E-04,
      -0.6032714131E-04,
       0.5119585945E-03,
       0.2931623842E-03,
      -0.3270646712E-03,
       0.7655763607E-04,
       0.1767780640E-02,
       0.2998801694E-03,
      -0.1494339773E-01,
      -0.3839392904E-02,
       0.4918502279E-01,
       0.2719124334E-01,
      -0.5194987783E-01,
      -0.3644331897E+00,
       0.7771862176E+00,
      -0.4813616291E+00,
      -0.6127339397E-01,
       0.1432944583E+00,
       0.7607487270E-02,
      -0.3169508804E-01,
      -0.5421323123E-03,
       0.3382415829E-02};
	final static double[] phvd6temp={
       0.1231735693E-06,
      -0.1735483443E-06,
       0.1395294094E-06,
       0.1070254954E-06,
       0.2027240624E-07,
       0.1207834424E-06,
      -0.6124273534E-06,
      -0.2515754905E-06,
       0.3497458016E-06,
      -0.1090603783E-06,
       0.3271625017E-06,
       0.3159481233E-08,
       0.1889936443E-02,
       0.3029455986E-03,
      -0.1495228891E-01,
      -0.3808735528E-02,
       0.4913718250E-01,
       0.2721902439E-01,
      -0.5194583841E-01,
      -0.3644418935E+00,
       0.7771857517E+00,
      -0.4813596514E+00,
      -0.6127335907E-01,
       0.1432942384E+00,
       0.7607487325E-02,
      -0.3169508781E-01,
      -0.5421323318E-03,
       0.3382415951E-02};

	final static double[] phvd0=ArrayMath.invert(phvd0temp);
	final static double[] phvd1=ArrayMath.invert(phvd1temp);
	final static double[] phvd2=ArrayMath.invert(phvd2temp);
	final static double[] phvd3=ArrayMath.invert(phvd3temp);
	final static double[] phvd4=ArrayMath.invert(phvd4temp);
	final static double[] phvd5=ArrayMath.invert(phvd5temp);
	final static double[] phvd6=ArrayMath.invert(phvd6temp);

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


	public Daubechies8 () {}
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
		for(int k=14;k<=dl0-14;k++) {
			for(int L=-8;L<8;L++){
				sortie[2*k+L-6]+=vg[L+8]*gete[k];
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
		sortie=ArrayMath.add(sortie,gete[10],v10,0);
		sortie=ArrayMath.add(sortie,gete[11],v11,0);
		sortie=ArrayMath.add(sortie,gete[11],v11,0);
		sortie=ArrayMath.add(sortie,gete[12],v12,0);
		sortie=ArrayMath.add(sortie,gete[13],v13,0);


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
		int p10=sortie.length-vd10.length;
		int p11=sortie.length-vd11.length;
		int p12=sortie.length-vd12.length;
		int p13=sortie.length-vd13.length;
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
		sortie=ArrayMath.add(sortie,gete[dl0-10],vd10,p10);
		sortie=ArrayMath.add(sortie,gete[dl0-11],vd11,p11);
		sortie=ArrayMath.add(sortie,gete[dl0-12],vd12,p12);
		sortie=ArrayMath.add(sortie,gete[dl0-13],vd13,p13);

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
		if(gete.length<14) {
			throw new IllegalScalingException("The array is not long enough : "+gete.length+" < 14");
		}
		double[] sortie=new double[2*gete.length+filtretype];
		int dl0=gete.length-1;
		for(int k=7;k<=dl0-7;k++) {
			for(int L=-8;L<8;L++){
				sortie[2*k+L + 8 ]+=phvg[L+8]*gete[k];
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
		sortie=ArrayMath.add(sortie,gete[5],phv5,0);
		int p5=sortie.length-phvd5.length;
		sortie=ArrayMath.add(sortie,gete[dl0-5],phvd5,p5);
		sortie=ArrayMath.add(sortie,gete[6],phv6,0);
		int p6=sortie.length-phvd6.length;
		sortie=ArrayMath.add(sortie,gete[dl0-6],phvd5,p6);
		return(sortie);
	}


	public double[] evalScaling (int n0, int k, int j1) {
		return(Cascades.evalScaling(this,n0,j1,k));
	}


	public double[] evalWavelet (int n0, int k, int j1) {
		return(Cascades.evalWavelet(this,filtretype,n0,j1,k));
	}

}
