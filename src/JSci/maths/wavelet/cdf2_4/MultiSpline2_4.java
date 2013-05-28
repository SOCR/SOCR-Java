
package JSci.maths.wavelet.cdf2_4;

import JSci.maths.*;
import JSci.maths.wavelet.splines.*;
import JSci.maths.wavelet.*;

/******************************************
* Cohen-Daubechies-Feauveau
* with N=2 and
* Ntilde=4 adapted to the interval
* by Deslauriers-Dubuc-Lemire
* @author Daniel Lemire
*****************************************/
public final class MultiSpline2_4 implements Filter {

	public MultiSpline2_4() {}

	// constantes pour le filtre passe-bas
	static final double[] vg={1/2d,1d,1/2d};
	static final double[] v0={1d,1/2d};

	// constantes pour le filtre passe-hat
	static final double[] og={3/128d,3/64d,-1/8d,-19/64d,45/64d,-19/64d,-1/8d,3/64d,3/128d};
	static final double[] o0={-35/64d,875/1536d,-241/768d,-53/512d,41/384d,67/1536d,-5/256d,-5/512d};
	static final double[] on0_1=ArrayMath.invert(o0);
	static final double[] o1={15/64d,-45/512d,-105/256d,345/512d,-31/128d,-53/512d,9/256d,9/512d};
	static final double[] on0_2=ArrayMath.invert(o1);


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
			throw new IllegalScalingException("Even number of values presented to an odd Filter. Please change the number of values/iterations.");
		}
	}

	/***************************************************
	****************************************************/
	public double[] lowpass (double[] v, double[] param) {
		return(lowpass(v));
	}
	/***************************************************
	****************************************************/
	public double[] highpass (double[] v, double[] param) {
		return(highpass(v));
	}

	/**************************************
	***************************************/
	public double[] lowpass (double[] donnee) {
		if(donnee.length<2) {
			throw new IllegalScalingException("The array is not long enough : "+donnee.length+" < 2");
		}
		double[] sortie=new double[2*donnee.length-1];
		for(int k=1;k<donnee.length-1;k++) {
			sortie[2*k-1]+=donnee[k]*vg[0];
			sortie[2*k]+=donnee[k]*vg[1];
			sortie[2*k+1]+=donnee[k]*vg[2];
		}
		sortie[0]+=v0[0]*donnee[0];
		sortie[1]+=v0[1]*donnee[0];
		sortie[sortie.length-1]+=v0[0]*donnee[donnee.length-1];
		sortie[sortie.length-2]+=v0[1]*donnee[donnee.length-1];
		return(sortie);
	}

	/**************************************
	***************************************/
	public double[] highpass(double[] v) {
		int n0=v.length+1;
		double[] ans=ArrayMath.scalarMultiplyFast(v[0],wavelet(n0,0).interpolate(0));
		for(int k=1;k<v.length;k++) {
			ans=ArrayMath.add(ans,ArrayMath.scalarMultiply(v[k],wavelet(n0,k).interpolate(0)));
		}
		return(ans);
	}

	/************************************
	*************************************/
	public static LinearSpline hat(int n0, int k) {
		if((k<0)||(n0<0)||(k>=n0)) {
			throw new IllegalArgumentException("Incorrect parameters : "+n0+", "+k+" !");
		}
		if(n0<CDF2_4.minlength) {
			throw new IllegalScalingException(n0,CDF2_4.minlength);
		}
		double[] v=new double[n0];
		v[k]=1;
		return(new LinearSpline(v));
	}

	/************************************
	*************************************/
	public static LinearSpline scaling(int n0, int k) {
		return(hat(n0,k));
	}

	/*************************************
	**************************************/
	public static LinearSpline wavelet(int n0, int k) {
		if((k<0)||(n0<0)||(k>=n0-1)) {
			throw new IllegalArgumentException("Incorrect parameters : "+n0+", "+k);
		}
		if(n0<CDF2_4.minlength) {
			throw new IllegalScalingException(n0,CDF2_4.minlength);
		}
		double[] v=new double[2*n0-1];
		if ((k>1)&&(k<n0-3)) {
			v=ArrayMath.padding(v.length,2*k-4+1,og);
		} else if (k==0) {
			v=ArrayMath.padding(v.length,0,o0);
		} else if (k==1) {
			v=ArrayMath.padding(v.length,0,o1);
		} else if (k==n0-2) {
			v=ArrayMath.padding(v.length,v.length-on0_1.length,on0_1);
		} else if  (k==n0-3) {
			v=ArrayMath.padding(v.length,v.length-on0_2.length,on0_2);
		} else {
			throw new IllegalArgumentException("Oups!");
		}
		return(new LinearSpline(v));
	}
}
