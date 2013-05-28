
package JSci.maths.wavelet.cdf3_5;

import JSci.maths.*;
import JSci.maths.wavelet.splines.*;
import JSci.maths.wavelet.*;

/******************************************
* Cohen-Daubechies-Feauveau
* with N=3 and
* Ntilde=5 adapted to the interval
* by Deslauriers-Dubuc-Lemire
* @author Daniel Lemire
*****************************************/
public final class MultiSpline3_5 implements Filter {
	public MultiSpline3_5() {}

	static final double[] vg={1/4d, 3/4d, 3/4d, 1/4d};
	static final double[] v0={3/4d, 1/4d};

	// constantes pour le filtre passe-haut

	static final double[] og={5/1024d, 15/1024d, -19/1024d, -97/1024d, 13/512d, 175/512d, -175/512d, -13/512d, 97/1024d, 19/1024d, -15/1024d, -5/1024d};
	static final double[] o0={-7917/10240d, 2877/10240d, -26593/204800d, -1139/204800d, 3951/102400d, 53/20480d, -451/40960d, -453/204800d, 21/12800d, 7/12800d};
	static final double[] o1={6237/10240d, -1197/10240d, -38927/204800d, 79779/204800d, -22911/102400d, -613/20480d, 1971/40960d, 2133/204800d, -81/12800d, -27/12800d};
	static final double[] o2={-1405/2048d, 205/2048d, 5897/24576d, -2183/8192d, -813/4096d, 5455/12288d, -1395/8192d, -337/8192d, 9/512d, 3/512d};
	static final double[] on0_1=ArrayMath.scalarMultiply(-1.0,ArrayMath.invert(o0));
	static final double[] on0_2=ArrayMath.scalarMultiply(-1.0,ArrayMath.invert(o1));
	static final double[] on0_3=ArrayMath.scalarMultiply(-1.0,ArrayMath.invert(o2));


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
		int i=(int) Math.round((k-2)/2d+2);
		if(2*(i-2)+2==k) {
			return(i);
		} else {
			throw new IllegalScalingException("Odd number of values into an even Filter. Please change the number of values/iterations: "+k);
		}
	}

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
		if(gete.length<3) {
			throw new IllegalScalingException("The array is not long enough : "+gete.length+" < 3");
		}
		double[] sortie=new double[2*gete.length-2];
		for(int k=1;k<gete.length-1;k++) {
			sortie[2*k-2]+=gete[k]*vg[0];
			sortie[2*k-1]+=gete[k]*vg[1];
			sortie[2*k]+=gete[k]*vg[2];
			sortie[2*k+1]+=gete[k]*vg[3];
		}
		sortie[0]+=v0[0]*gete[0];
		sortie[1]+=v0[1]*gete[0];
		sortie[sortie.length-1]+=v0[0]*gete[gete.length-1];
		sortie[sortie.length-2]+=v0[1]*gete[gete.length-1];
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
		int n0=v.length+2;
		double[] ans=ArrayMath.scalarMultiplyFast(v[0],wavelet(n0,0).interpolate(0));
		for(int k=1;k<v.length;k++) {
			ans=ArrayMath.add(ans,ArrayMath.scalarMultiply(v[k],wavelet(n0,k).interpolate(0)));
		}
		return(ans);
	}


	/************************************
	*************************************/
	public static QuadraticSpline scaling(int n0, int k) {
		if((k<0)||(n0<0)||(k>=n0)) {
			throw new IllegalArgumentException("Incorrect parameters : "+n0+", "+k+" !");
		}
		if(n0<CDF3_5.minlength) {
			throw new IllegalScalingException(n0,CDF3_5.minlength);
		}
		double[] v=new double[n0];
		v[k]=1;
		return(new QuadraticSpline(v));
	}

	/*************************************
	**************************************/
	public static QuadraticSpline wavelet(int n0, int k) {
		if((k<0)||(n0<0)||(k>=n0-2)) {
			throw new IllegalArgumentException("Incorrect parameters : "+n0+", "+k);
		}
		if(n0<CDF3_5.minlength) {
			throw new IllegalScalingException(n0,CDF3_5.minlength);
		}
		double[] v=new double[2*n0-2];
		if ((k>2)&&(k<n0-5)) {
			v=ArrayMath.padding(v.length,2*k-4,og);
		} else if (k==0) {
			v=ArrayMath.padding(v.length,0,o0);
		} else if (k==1) {
			v=ArrayMath.padding(v.length,0,o1);
		} else if (k==2) {
			v=ArrayMath.padding(v.length,0,o2);
		} else if (k==n0-3) {
			v=ArrayMath.padding(v.length,v.length-on0_1.length,on0_1);
		} else if  (k==n0-4) {
			v=ArrayMath.padding(v.length,v.length-on0_2.length,on0_2);
		} else if (k==n0-5) {
			v=ArrayMath.padding(v.length,v.length-on0_3.length,on0_3);
		} else {
			throw new IllegalArgumentException("Oups!");
		}
		return(new QuadraticSpline(v));
	}
}
