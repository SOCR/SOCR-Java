
package JSci.maths.wavelet.daubechies7;

import JSci.maths.wavelet.*;
import JSci.maths.*;

/******************************************
* Daubechies wavelets adapted to the
* interval by Meyer. Thanks to Pierre Vial
* for the filters.
* @author Daniel Lemire
*****************************************/
public final class Daubechies7 extends Multiresolution implements Filter, NumericalConstants {
	protected final static int filtretype=12;
	protected final static int minlength=24;
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
		return(new Scaling7(n0,k));
	}

	public  MultiscaleFunction dualScaling(int n0, int k) {
		return(new Scaling7(n0,k));
	}
	public  MultiscaleFunction primaryWavelet(int n0, int k) {
		return(new Wavelet7(n0,k));
	}
	public  MultiscaleFunction dualWavelet(int n0, int k) {
		return(new Wavelet7(n0,k));
	}

  
	final static double[] vg={
      0.0037926585342,
      - 0.0014812259146,
       - 0.0178704316511,
      0.043155452582,
       0.0960147679355,
       - 0.070078291222,
      0.0246656594886,
      0.758162601964,
       1.08578270981,
       0.408183939725,
       - 0.198056706807,
       - 0.152463871896,
       0.00567134268574,
       0.014521394762};

	final static double[] v0temp={
    0.7766665156E+00,
    0.6299119967E+00};
	final static double[] v1temp={
   -0.1148964921E+00,
    0.1416646431E+00,
   -0.9671641580E+00,
    0.1769842267E+00};
	final static double[] v2temp={
   -0.3218471381E-01,
    0.3968298692E-01,
    0.1563278738E+00,
    0.8016258409E+00,
   -0.5564532662E+00,
    0.1438984865E+00};
	final static double[] v3temp={
    0.2211311440E-01,
   -0.2726494430E-01,
    0.4246022560E-01,
    0.2682115073E+00,
    0.2563887092E+00,
   -0.5363572642E+00,
   -0.6324950327E+00,
    0.4142017603E+00};
	final static double[] v4temp={
    0.1199996300E-01,
   -0.1479566908E-01,
   -0.5636972038E-01,
   -0.2884098615E+00,
   -0.2710188640E+00,
    0.6266429769E+00,
   -0.5971809113E+00,
    0.2582226700E+00,
    0.5436350287E-01,
    0.1445242950E+00};
	final static double[] v5temp={
    0.4175845107E-02,
   -0.5148717734E-02,
   -0.7440855354E-02,
   -0.3382983614E-01,
   -0.3079310698E-01,
    0.7981928189E-01,
    0.5722707580E-01,
    0.2319141981E+00,
   -0.9107619432E+00,
   -0.3104299380E+00,
    0.3358655707E-01,
    0.8599791766E-01};
	final static double[] v6temp={
   -0.1031229858E+00,
    0.1271481897E+00,
    0.1056363589E-01,
   -0.1109935195E+00,
   -0.1694835353E+00,
   -0.1066751168E+00,
    0.3522103457E-01,
    0.1052218705E+00,
   -0.2477521992E-02,
   -0.9262193855E-01,
   -0.8918295201E+00,
   -0.3112729161E+00,
    0.3153823148E-01,
    0.8075320692E-01};
	final static double[] v7temp={
   -0.1636840253E+00,
    0.2018185116E+00,
    0.2397992313E-01,
   -0.1367621625E+00,
   -0.2287872293E+00,
   -0.2411625975E+00,
    0.1547745353E+00,
    0.1738003308E+00,
   -0.2203021645E+00,
    0.7991821052E+00,
    0.4097263186E-01,
    0.7425536593E-01,
   -0.2186097493E+00,
   -0.7664414437E-01,
    0.7725319564E-02,
    0.1978057424E-01};
	final static double[] v8temp={
   -0.5079391756E-01,
    0.6262769271E-01,
    0.6693967094E-02,
   -0.4652396311E-01,
   -0.7498687931E-01,
   -0.6670242581E-01,
    0.2830429724E-01,
    0.3953767158E-01,
    0.1915788427E-01,
    0.1080996941E+00,
   -0.9385371148E-01,
    0.5278533401E+00,
    0.7495761660E+00,
    0.2923381676E+00,
   -0.1429933474E+00,
   -0.1070457309E+00,
    0.4143065991E-02,
    0.1060826335E-01};
	final static double[] v9temp={
   -0.1495677487E-02,
    0.1844134781E-02,
    0.3796088187E-03,
   -0.3726476076E-03,
   -0.1313050363E-02,
   -0.4257093968E-02,
    0.4767087176E-02,
    0.2983259636E-02,
   -0.1353810327E-01,
    0.3517346347E-01,
    0.6457953904E-01,
   -0.4929701131E-01,
    0.1763779822E-01,
    0.5365060286E+00,
    0.7675636132E+00,
    0.2885569171E+00,
   -0.1400433929E+00,
   -0.1077912821E+00,
    0.4010358474E-02,
    0.1026846759E-01};
	final static double[] v10temp={
   -0.3038841290E-04,
    0.3746819061E-04,
    0.1201067262E-04,
    0.1591583744E-04,
    0.1765202958E-04,
   -0.5058087991E-04,
   -0.2976878538E-03,
   -0.5384490113E-03,
    0.2571676204E-02,
   -0.9401364213E-03,
   -0.1271271848E-01,
    0.3052112329E-01,
    0.6789733930E-01,
   -0.4954313507E-01,
    0.1743713195E-01,
    0.5361004301E+00,
    0.7677643255E+00,
    0.2886299560E+00,
   -0.1400472373E+00,
   -0.1078082259E+00,
    0.4010244932E-02,
    0.1026817686E-01};
	final static double[] v11temp={
   -0.4587815337E-07,
    0.5656667234E-07,
    0.1705493214E-07,
    0.1813824211E-07,
    0.3222871934E-07,
   -0.2080479474E-07,
   -0.2019192222E-06,
   -0.3625456178E-06,
   -0.1335197462E-06,
    0.6674082282E-07,
    0.2681764400E-02,
   -0.1047381271E-02,
   -0.1263630033E-01,
    0.3051551953E-01,
    0.6789269080E-01,
   -0.4955283591E-01,
    0.1744125510E-01,
    0.5361019173E+00,
    0.7677643170E+00,
    0.2886296318E+00,
   -0.1400472404E+00,
   -0.1078082377E+00,
    0.4010244872E-02,
    0.1026817671E-01};


	final static double[] vd0temp={
    0.7004199839E+00,
    0.7137309340E+00};

	final static double[] vd1temp={
    0.1053864722E-01,
   -0.1034210340E-01,
    0.6658963651E+00,
    0.7458981223E+00};

	final static double[] vd2temp={
   -0.1757138133E+00,
    0.1724367832E+00,
   -0.1665581445E+00,
    0.1535673633E+00,
   -0.4422666014E+00,
    0.8321453685E+00};

	final static double[] vd3temp={
    0.1613128649E+00,
   -0.1583044098E+00,
   -0.3601719723E+00,
    0.3170674120E+00,
    0.7182714497E+00,
    0.3180082506E+00,
   -0.1351100609E+00,
    0.2887289881E+00};

	final static double[] vd4temp={
    0.2626733923E+00,
   -0.2577745821E+00,
    0.5300977359E-01,
   -0.5460956119E-01,
   -0.7894206983E-01,
    0.8761351181E-01,
    0.1138292645E+00,
   -0.8840156389E-02,
    0.1186027472E+00,
    0.9042968950E+00};

	final static double[] vd5temp={
   -0.9906552530E-01,
    0.9721797156E-01,
    0.1575818939E+00,
   -0.1379327181E+00,
    0.2550022277E+00,
    0.1514595280E+00,
    0.8750776361E+00,
    0.6499770717E-01,
    0.2324452664E+00,
   -0.9349432145E-01,
   -0.4623379279E-01,
    0.1183889785E+00};

	final static double[] vd6temp={
   -0.6250197855E-01,
    0.6133632819E-01,
    0.7570194334E-02,
   -0.5024723655E-02,
    0.2646032111E-01,
   -0.9402290802E-02,
    0.6149372377E-01,
    0.5681683063E-01,
    0.2562598477E-01,
    0.2756686809E-01,
    0.9366103730E+00,
   -0.3120657710E+00,
   -0.3336171928E-01,
    0.8542222229E-01};

	final static double[] vd7temp={
   -0.4715917315E+00,
    0.4627966328E+00,
   -0.9443328396E-02,
    0.2151032037E-01,
    0.4754578984E-01,
   -0.1760708900E+00,
   -0.1591797424E+00,
    0.4829772924E+00,
    0.2074893977E+00,
    0.2895141132E+00,
   -0.4058619450E-01,
    0.1026407474E+00,
    0.3418617349E+00,
   -0.1182037757E+00,
   -0.1210726949E-01,
    0.3100049663E-01};

	final static double[] vd8temp={
   -0.9439529077E-01,
    0.9263483604E-01,
   -0.7325279539E-01,
    0.6801412471E-01,
   -0.2325217040E-01,
   -0.7869955176E-01,
   -0.8937965975E-01,
    0.4016014208E-01,
    0.1328240580E+00,
    0.6204454273E-01,
    0.1623006055E+00,
    0.6626531984E+00,
   -0.6074912864E+00,
    0.2552288166E+00,
    0.1574205947E+00,
   -0.1004613457E+00,
   -0.4839078318E-02,
    0.1239039332E-01};

	final static double[] vd9temp={
    0.1594142646E+00,
   -0.1564412180E+00,
    0.7441639637E-02,
   -0.1106491958E-01,
   -0.2825478866E-01,
    0.5459386943E-01,
   -0.9775758534E-02,
   -0.1478196128E+00,
   -0.1113587794E+00,
   -0.8536948628E-01,
    0.2751605400E+00,
    0.6421729864E+00,
    0.6324608023E+00,
   -0.1525303792E-01,
   -0.8448334026E-01,
    0.8762658370E-01,
    0.3318133734E-01,
   -0.1602650231E-01,
   -0.1102323809E-02,
    0.2822484916E-02};

	final static double[] vd10temp={
   -0.6037533035E-02,
    0.5924934159E-02,
    0.1534054302E-02,
   -0.1202064256E-02,
    0.3182693183E-02,
   -0.2822206008E-03,
    0.8251845825E-02,
    0.6110061754E-02,
    0.6347460794E-02,
    0.1773857064E-02,
   -0.1077767325E+00,
   -0.1373538162E+00,
    0.2848100511E+00,
    0.7687892964E+00,
    0.5372655282E+00,
    0.1673410769E-01,
   -0.4968993169E-01,
    0.6802066194E-01,
    0.3052110650E-01,
   -0.1264613714E-01,
   -0.1047456655E-02,
    0.2681998325E-02};

	final static double[] vd11temp={
   -0.2814830317E-04,
    0.2762334252E-04,
   -0.2993814193E-04,
    0.2750781612E-04,
    0.1564675966E-04,
   -0.1442059454E-04,
    0.2143345853E-04,
   -0.4969368764E-04,
   -0.5136649938E-04,
    0.2578289227E-04,
    0.1026729934E-01,
    0.4010109626E-02,
   -0.1078074809E+00,
   -0.1400480006E+00,
    0.2886301179E+00,
    0.7677642441E+00,
    0.5361017227E+00,
    0.1744136474E-01,
   -0.4955281693E-01,
    0.6789267353E-01,
    0.3051551282E-01,
   -0.1263630236E-01,
   -0.1047384891E-02,
    0.2681814574E-02};

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



	/********************************************
	* On définit ici le filtre comme tel par le
	* vecteur phvg (filtre passe-haut).
	*********************************************/
	final static double[] vgtemp=ArrayMath.scalarMultiply(1.0/SQRT2,vg);
	final static double[] phvg=WaveletMath.lowToHigh(vgtemp);
	final static double[] phv0={
    0.3969456608E+00,
   -0.4894245622E+00,
   -0.1235692328E+00,
   -0.2582101893E-01,
   -0.1328855813E+00,
   -0.1202996209E-01,
    0.3801419403E+00,
    0.6231415519E+00,
    0.1905526450E+00,
    0.5893964132E-02,
    0.1561741876E-03,
    0.6031669262E-05,
   -0.4927938364E-06,
   -0.1261756223E-05};
	final static double[] phv1={
   -0.3233700290E+00,
    0.3987075576E+00,
    0.1351807407E+00,
    0.2096521373E+00,
    0.4833387568E+00,
    0.3720043480E+00,
    0.2556921125E+00,
    0.4668718999E+00,
    0.1373682453E+00,
    0.2872512719E-02,
   -0.1360874965E-02,
   -0.1949202336E-02,
    0.8743490990E-03,
    0.3629247193E-03,
   -0.2999653169E-04,
   -0.7680570657E-04};
	final static double[] phv2={
   -0.2823703425E+00,
    0.3481559189E+00,
    0.2903665693E-01,
   -0.3033125208E+00,
   -0.4606818655E+00,
   -0.2824761359E+00,
    0.5936310480E-01,
    0.2414473445E+00,
    0.2265766807E+00,
   -0.4453082198E+00,
    0.3158425440E+00,
   -0.6883194783E-02,
   -0.3045582749E-01,
   -0.4030240270E-01,
    0.1788874193E-01,
    0.7517433396E-02,
   -0.6122399330E-03,
   -0.1567631908E-02};
	final static double[] phv3={
   -0.5050738626E-02,
    0.6227440642E-02,
    0.1186122150E-02,
   -0.1781771641E-02,
   -0.5423430571E-02,
   -0.1518205990E-01,
    0.2487591795E-01,
    0.2339536126E-01,
   -0.1088432050E+00,
    0.1555511237E+00,
    0.2777338303E+00,
   -0.7671513525E+00,
    0.5369254850E+00,
   -0.1606926084E-01,
   -0.5015309817E-01,
   -0.6812578033E-01,
    0.3052715070E-01,
    0.1268650625E-01,
   -0.1047058590E-02,
   -0.2680979085E-02};
	final static double[] phv4={
   -0.1153350842E-03,
    0.1422054166E-03,
    0.4560875753E-04,
    0.6053691674E-04,
    0.6687225073E-04,
   -0.1932039563E-03,
   -0.1135318214E-02,
   -0.2053589490E-02,
    0.9849438060E-02,
   -0.3601089889E-02,
   -0.1080997299E+00,
    0.1400686730E+00,
    0.2886474191E+00,
   -0.7677274911E+00,
    0.5360863094E+00,
   -0.1744690015E-01,
   -0.4955276912E-01,
   -0.6789145494E-01,
    0.3051552376E-01,
    0.1263634496E-01,
   -0.1047384658E-02,
   -0.2681813977E-02};
	final static double[] phv5={
   -0.1756594007E-06,
    0.2165838647E-06,
    0.6530033661E-07,
    0.6944811113E-07,
    0.1233981379E-06,
   -0.7965760361E-07,
   -0.7731119139E-06,
   -0.1388121081E-05,
   -0.5112227575E-06,
    0.2555383959E-06,
    0.1026798462E-01,
   -0.4010231020E-02,
   -0.1078082259E+00,
    0.1400472648E+00,
    0.2886296214E+00,
   -0.7677643207E+00,
    0.5361019171E+00,
   -0.1744125426E-01,
   -0.4955283493E-01,
   -0.6789269348E-01,
    0.3051551317E-01,
    0.1263630340E-01,
   -0.1047384889E-02,
   -0.2681814568E-02};

	final static double[] phvd0temp={
    0.3287269241E+00,
   -0.3225962277E+00,
   -0.3486576217E-01,
    0.2200880144E-01,
   -0.4013253310E+00,
   -0.8807413471E-01,
    0.7139363748E-01,
    0.7005967686E+00,
    0.2475030985E+00,
   -0.2451712749E+00,
    0.7181777146E-02,
    0.2499604984E-04,
   -0.8073680719E-08,
   -0.3908981471E-07};
	final static double[] phvd1temp={
    0.5957737342E-01,
   -0.5846626641E-01,
    0.4582097589E+00,
   -0.4107166017E+00,
    0.2022914839E+00,
    0.2997172270E+00,
   -0.3986325494E+00,
   -0.6261552962E-01,
    0.5532851486E+00,
   -0.1200134025E+00,
    0.1616443953E-01,
   -0.3615532478E-02,
   -0.1770925212E-02,
    0.1363254906E-02,
    0.5071037744E-04,
   -0.1298432110E-03};
	final static double[] phvd2temp={
    0.1154439687E-01,
   -0.1132909600E-01,
   -0.3933408112E+00,
    0.3508326132E+00,
   -0.1045297744E+00,
   -0.1942431291E+00,
    0.4053278706E-01,
   -0.3956498210E+00,
    0.7001774443E+00,
   -0.5344583491E-01,
   -0.6098922304E-02,
   -0.7677883475E-01,
    0.1158196639E+00,
   -0.4412947160E-01,
   -0.2089669169E-01,
    0.1616290641E-01,
    0.5971498449E-03,
   -0.1528993947E-02};
	final static double[] phvd3temp={
    0.3435795707E-01,
   -0.3371718752E-01,
    0.3919592661E-02,
   -0.4452130171E-02,
   -0.2728348640E-02,
    0.1439788762E-01,
    0.1012896303E-01,
   -0.3223449962E-01,
   -0.2185300516E-01,
   -0.2044704093E-01,
    0.6843499760E-01,
    0.2980333628E-01,
    0.4539318191E-01,
   -0.5452752162E+00,
    0.7607344157E+00,
   -0.2839752561E+00,
   -0.1395508493E+00,
    0.1071639975E+00,
    0.4000222290E-02,
   -0.1024251403E-01};
	final static double[] phvd4temp={
   -0.1619528496E-02,
    0.1589324589E-02,
    0.3554364827E-03,
   -0.2723954613E-03,
    0.8549575210E-03,
   -0.9551275473E-04,
    0.2187750783E-02,
    0.1520814609E-02,
    0.1580292226E-02,
    0.5022963426E-03,
   -0.1263138978E-01,
   -0.2981485818E-01,
    0.6690165825E-01,
    0.4983337040E-01,
    0.1775563260E-01,
   -0.5362889286E+00,
    0.7677311001E+00,
   -0.2885962362E+00,
   -0.1400458666E+00,
    0.1078059333E+00,
    0.4010225471E-02,
   -0.1026812703E-01};
	final static double[] phvd5temp={
   -0.7351578516E-05,
    0.7214472934E-05,
   -0.7819175985E-05,
    0.7184425358E-05,
    0.4086432729E-05,
   -0.3766364036E-05,
    0.5597958787E-05,
   -0.1297860916E-04,
   -0.1341567344E-04,
    0.6733817189E-05,
    0.2681585429E-02,
    0.1047349568E-02,
   -0.1263610581E-01,
   -0.3051571178E-01,
    0.6789282065E-01,
    0.4955281636E-01,
    0.1744120464E-01,
   -0.5361018885E+00,
    0.7677643218E+00,
   -0.2886296370E+00,
   -0.1400472405E+00,
    0.1078082380E+00,
    0.4010244871E-02,
   -0.1026817671E-01};

	final static double[] phvd0=ArrayMath.invert(phvd0temp);
	final static double[] phvd1=ArrayMath.invert(phvd1temp);
	final static double[] phvd2=ArrayMath.invert(phvd2temp);
	final static double[] phvd3=ArrayMath.invert(phvd3temp);
	final static double[] phvd4=ArrayMath.invert(phvd4temp);
	final static double[] phvd5=ArrayMath.invert(phvd5temp);

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


	public Daubechies7 () {}
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
		for(int k=12;k<=dl0-12;k++) {
			for(int L=-7;L<7;L++){
				sortie[2*k+L-5]+=vg[L+7]*gete[k];
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
		if(gete.length<12) {
			throw new IllegalScalingException("The array is not long enough : "+gete.length+" < "+12);
		}
		double[] sortie=new double[2*gete.length+filtretype];
		int dl0=gete.length-1;
		for(int k=6;k<=dl0-6;k++) {
			for(int L=-7;L<7;L++){
				sortie[2*k+L + 7 ]+=phvg[L+7]*gete[k];
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

		return(sortie);
	}


	public double[] evalScaling (int n0, int k, int j1) {
		return(Cascades.evalScaling(this,n0,j1,k));
	}


	public double[] evalWavelet (int n0, int k, int j1) {
		return(Cascades.evalWavelet(this,filtretype,n0,j1,k));
	}

}
