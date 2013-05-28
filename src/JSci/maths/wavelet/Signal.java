package JSci.maths.wavelet;

import JSci.maths.*;
import JSci.maths.wavelet.*;
import JSci.maths.wavelet.splines.*;
import java.io.*;
import java.util.Arrays;

/****************************************
* This class use the linear spline as
* a general model for a signal. While this
* is a reasonnable design choice, this
* can certainly be overwritten if
* necessary.  Basic operations on signal
* are supported.
* @author Daniel Lemire
*****************************************/
public class Signal extends LinearSpline implements NumericalConstants, Cloneable  {
	private Filter filterdual;
	final static double normalisation=1.0/SQRT2;
	private Double[] param;
  /***************************************
  * This epsilon is used to verify that the
  * FFT on real numbers really returns real
  * numbers when the iFFT is applied to it.
  * This is a common safety test which could
  * fail for very large numbers.
  *****************************************/
	final static double epsilon=0.000001;
  /********************************************
  * Return a copy of this object
  *********************************************/
	public Object clone() {
    Signal s=(Signal) super.clone();
    if(filterdual!=null)
      s.filterdual=filterdual;
      //the previous line is unsafe?
    if(param!=null) {
      s.param=new Double[param.length];
      System.arraycopy(s.param,0,param,0,param.length);
    }
    return(s);
	}
  /************************************
  * This method generates a copy of the
  * current object with a different data
  * content.
  **************************************/
  private Signal copy(double[] v) {
		if(filterdual!=null) {
			if(param!=null) {
				double[] p=new double[param.length];
				for(int k=0;k<p.length;k++) {
					p[k]=param[k].doubleValue();
				}
				return(new Signal(filterdual,v,p));
			} else {
				return(new Signal(filterdual,v));
			}
		} else {
			return(new Signal(v));
		}
  }
	/******************************
	*******************************/
	public Signal () {
  }

	/***********************************
	************************************/
	public Signal (double[] v) {
		super(v);
  	}

	/******************************
	*******************************/
	public Signal (Filter f,double[] v, double[] p) {
		super(v);
		filterdual=f;
		for(int k=0;k<p.length;k++) {
			param[k]=new Double(p[k]);
		}
	}

	/********************************
	*********************************/
	public Signal(Filter f) {
		filterdual=f;
	}

	/*********************************
	**********************************/
	public Signal (Filter f,double[] v) {
		super(v);
		filterdual=f;
	}


	/********************************************
  * Get the sampled values of the sample as
  * an array.
	*********************************************/
	public double[] getValues() {
		return(this.interpolate(0));
	}

	/*********************************
  * set the signal associated Filter
	**********************************/
	public void setFilter(Filter f) {
		filterdual=f;
	}

	/***********************************
  * Set the parameter of the Filter (if
  * it applies).
	************************************/
	public void setParameter(double[] p) {
		for(int k=0;k<p.length;k++) {
			param[k]=new Double(p[k]);
		}
	}

	/***********************************
  * Set the parameter of the Filter (if
  * it applies).
	************************************/
	public void setParameter(Double[] p) {
		param=p;
	}

	/***********************************
  * Throws away the parameter of the Filter
	************************************/
	public void removeParameter() {
		param=null;
	}

	/******************************************************
	* Set the Signal to the specified length scraping or
  * padding the beginning if necessary
	*******************************************************/
	public void setLengthFromEnd(int longueur) {
    double[] newvec=ArrayMath.setLengthFromEnd(this.evaluate(0),longueur);
    this.setValues(newvec);
	}

  /*****************************************************
  * Resample the signal using linear interpolation
  ******************************************************/
	public void resample(int newl) {
    double[] newvec=EngineerMath.resample(this.evaluate(0),newl);
    this.setValues(newvec);
	}

	/******************************************************
	* Set the Signal to the specified length scraping or
  * padding the end if necessary
  *******************************************************/
	public void setLengthFromBeginning(int longueur) {
    double[] newvec=ArrayMath.setLengthFromBeginning(this.evaluate(0),longueur);
    this.setValues(newvec);
	}

	/***********************************
  * Set the data for the signal
	************************************/
	public void setData(double[] v) {
		setValues(v);
	}

	/********************************
	* Fast Wavelet Transform
	*********************************/
	public FWTCoef fwt(int J) {
		if (J>20) {
			throw new IllegalArgumentException("Too many iterations.");
		}
		if (J<0) {
			throw new IllegalArgumentException("Cannot have a negative number of iterations.");
		}
		double[] data=this.interpolate(0);
		double[][] fwt=new double[J+1][];
		for(int j=1;j<=J;j++) {
			fwt[j]=highpassProject(data);
			data=lowpassProject(data);
		}
		fwt[0]=data;
		FWTCoef t=new FWTCoef(fwt);
		return(t);
	}

	/********************************
	* The Fast Wavelet Transform
  * with Wavelet packets
  * @param J number of iterations
  * @param cout cost function
	*********************************/
	public FWTPacketCoef fwtPacket(int J, MappingND cout) {
		if (J>20) {
			throw new IllegalArgumentException("Too many iterations.");
		}
		if (J<0) {
			throw new IllegalArgumentException("Cannot have a negative number of iterations.");
		}
		double[] data=this.interpolate(0);
		double[][] fwt=new double[J+1][];
		double[] choix1,choix2;
		boolean[] choixStandard=new boolean[J];
		for(int j=0;j<J;j++) {
			choix1=highpassProject(data);
			choix2=lowpassProject(data);
			if(cout.map(choix1)[0]< cout.map(choix2)[0]) {
				fwt[j]=choix1;
				data=choix2;
				choixStandard[j]=true;
			} else {
				data=choix1;
				fwt[j]=choix2;
				choixStandard[j]=false;
			}
		}
		fwt[J]=data;
		FWTPacketCoef t=new FWTPacketCoef(fwt,choixStandard);
		return(t);
	}


	/************************************************
  * Project the array according to the lowpass Filter
	* @param v data array
	* @author Daniel Lemire
	************************************************/
	private double[] lowpassProject (double[] v) {
		int l=filterdual.previousDimension(v.length);
		double[] Eche;
		double[] ans=new double[l];
	  if (param!=null) {
      for(int i=0;i<l;i++) {
				double[] p=new double[param.length];
				for(int k=0;k<p.length;k++) {
					p[k]=param[k].doubleValue();
				}
				Eche=filterdual.lowpass(delta(i,l,normalisation),p);
			  ans[i]=scalarProduct(v,Eche);
		  }
    } else {
      for(int i=0;i<l;i++) {
				Eche=filterdual.lowpass(delta(i,l,normalisation));
			  ans[i]=scalarProduct(v,Eche);
		  }
    }

/*
                for(int i=0;i<l;i++) {
			if (param!=null) {
				double[] p=new double[param.length];
				for(int k=0;k<p.length;k++) {
					p[k]=param[k].doubleValue();
				}
				Eche=filterdual.lowpass(delta(i,l,normalisation),p);
			} else {
				Eche=filterdual.lowpass(delta(i,l,normalisation));
			}
			ans[i]=ArrayMath.scalarProduct(v,Eche);
		}
*/
		return(ans);
	}

	private static double scalarProduct (double[] w0,double[] w1) {
		double sortie=0.0;
		for(int k=0;k<w0.length;k++){
			sortie+=w0[k]*w1[k];
		}
		return(sortie);
	}



	/****************************************************
  * Project the data according to the lowpass Filter
	* @author Daniel Lemire
	****************************************************/
	public double[] lowpassProject() {
		double[] data=this.interpolate(0);
		return(lowpassProject(data));
	}

	/******************************************
  * Project the signal according the the
  * highpass Filter
	* @param v data
	* @author Daniel Lemire
	********************************************/
	private double[] highpassProject (double[] v) {
		int l=filterdual.previousDimension(v.length);
		int lOnd=v.length-l;
		double[] Onde;
		double[] ans=new double[lOnd];
		if (param!=null) {
      for(int i=0;i<lOnd;i++) {
          double[] p=new double[param.length];
          for(int k=0;k<p.length;k++) {
            p[k]=param[k].doubleValue();
          }
          Onde=filterdual.highpass(delta(i,lOnd,1),p);
          ans[i]=scalarProduct(v,Onde);
      }
    } else {

      for(int i=0;i<lOnd;i++) {


				  Onde=filterdual.highpass(delta(i,lOnd,1));

          ans[i]=scalarProduct(v,Onde);
      }

		}

/*
		for(int i=0;i<lOnd;i++) {
			if (param!=null) {
				double[] p=new double[param.length];
				for(int k=0;k<p.length;k++) {
					p[k]=param[k].doubleValue();
				}
				Onde=filterdual.highpass(delta(i,lOnd,1),p);
			} else {
				Onde=filterdual.highpass(delta(i,lOnd,1));
			}
			ans[i]=ArrayMath.scalarProduct(v,Onde);
		}
*/
		return(ans);
	}


	/********************************************
  * Project the signal according the the
  * highpass Filter
	* @author Daniel Lemire
	*********************************************/
	public double[] highpassProject() {
		double[] data=this.interpolate(0);
		return(highpassProject(data));
	}

	/*************************************************
  * return a kronecker
	* @param l length
	* @param i position
	* @author Daniel Lemire
	*************************************************/
	private double[] delta(int i, int l, double a) {
		if((i<0)||(i>l)||(l<0)) {
			throw new IllegalArgumentException("This Kronecker doesn't exist.");
		}
		double[] v=new double[l];
		v[i]=a;
		return(v);
	}

	/***************************
  * Compute the L2 norm of the
  * signal
	****************************/
	public double norm() {
		double[] data=this.interpolate(0);
		return(ArrayMath.norm(data));
	}

	/***********************************
	* @author Don Cross
        * @author Daniel Lemire
	************************************/
    public Complex[] fft() {
		double[] data=this.interpolate(0);
		return(fft(data));
	}

	/***********************************
	* This is merely a copy of the FFT
  * method found in the class FourierMath
  * with some changes... optimized for
  * double[] arrays.
	* @author Don Cross
	************************************/
	public static Complex[] fft(double[] data) {
                return FourierMath.transform(data);
        }

	public static Complex[] fft(Complex[] data) {
                return FourierMath.transform(data);
        }
  /*********************************
  * Return the absolute value of
  * the FFT
  **********************************/
	public double[] absFFT() {
		Complex[] fft=fft();
		double[] answer=new double[fft.length];
		for(int i=0;i<fft.length;i++) {
			answer[i]=fft[i].mod();
		}
		return (answer);
	}
	public static double[] absFFT(double[] data) {
		Complex[] fft=fft(data);
		double[] answer=new double[fft.length];
		for(int i=0;i<fft.length;i++) {
			answer[i]=fft[i].mod();
		}
		return (answer);
	}
        /**************************************
        * Also noted iFFT in other packages.
        * This is the inverse to the FFT.
        ***************************************/
        public static Complex[] fftInverse(Complex data[]) {
                return FourierMath.inverseTransform(data);
        }


        /**
        * Reverse bits.
        * @author Don Cross
        */
        private static int reverseBits(int index,int numBits) {
                int i,rev;
                for(i=rev=0;i<numBits;i++) {
                        rev=(rev<<1)|(index&1);
                        index>>=1;
                }
                return rev;
        }

        /*****************************************
        * Check if another object is equal to this
        * Signal object
        ******************************************/
	public boolean equals(Signal b) {
                return(Arrays.equals(this.getValues(),b.getValues()));
        }
	/*******************************************************
        * Will make the signal a given dimension
        ********************************************************/
	public void setDimensionFromEnd(int dimension) {
		double[] data=this.interpolate(0);
		double[] ans=new double[dimension];
		int debut;
		if(dimension-data.length<0) debut=data.length-dimension;
		else debut=0;
		for(int k=debut;k<data.length;k++) {
			ans[k+dimension-data.length]=data[k];
		}
		super.setValues(ans);
	}

  /********************************************************
  * Will make the signal a given dimension
  *********************************************************/
	public void setDimensionFromBeginning(int dimension) {
		double[] data=this.interpolate(0);
		double[] ans=new double[dimension];
		int debut;
		if(dimension-data.length<0) debut=data.length-dimension;
		else debut=0;
		for(int k=0;k<data.length-debut;k++) {
			ans[k]=data[k];
		}
		super.setValues(ans);
	}

	/********************************************
  * Simplistic FFT denoising.
	* @param k frequency to denoised
	*********************************************/
	public void denoiseByFFT(int k){
		if(k<1) {
			throw new IllegalArgumentException ("This parameter must be 1 or more : "+k);
		}
		double[] data=interpolate(0);
		if(k>data.length-2) {
			if (data.length<4) {
				throw new IllegalArgumentException ("Your signal is too short to be denoised : "+data.length+" < 4");
			}
			throw new IllegalArgumentException ("Since you signal has dimension "+data.length+", the parameter must be at most : "+(data.length-2));
		}
		Complex[] ff=Signal.fft(data);
		ff[k+1]=Complex.ZERO;
		ff[data.length-1-k]=Complex.ZERO;
		Complex[] tf=Signal.fftInverse(ff);
		for(int l=0;l<data.length;l++) {
				data[l]=tf[l].real();
				if(Math.abs(tf[l].imag())>epsilon) {
					throw new IllegalArgumentException("Complex values detected during synthesis. Please get in touch with Daniel Lemire at Daniel.Lemire@Tintin.net to report this error.");
				}
		}
    super.setValues(data);
	}

  /**********************************************
  * Return the entropy of the signal
  ***********************************************/
  public double entropy () {
    return(EngineerMath.entropy(this.evaluate(0)));
  }

	/*****************************************
  * Apply the given array as a convolution
  * Filter and return a new Signal.
  * As one often want to compare the result
  * to the original signal, this method is
  * "safe", that is, it won't change the current
  * object.
  * @param f an array containing the coefficients
  *   of the convolution Filter
	******************************************/
	public Signal filter (double[] f) {
		double[] data=super.interpolate(0);
		if(data.length-(f.length-1)<=0) {
			throw new IllegalArgumentException("Your signal is too short for this Filter : "+data.length+", "+f.length);
		}
		double[] ans=new double[data.length-(f.length-1)];
		for(int k=0;k<data.length-(f.length-1);k++) {
			for(int l=0;l<f.length;l++) {
				ans[k]+=f[l]*data[k+l];
			}
		}
    return(copy (ans));
	}
  /******************************************
  * Apply the median Filter of a window of
  * size 2*n+1.
  * exception IllegalArgumentException if the
  *   parameter n is negative
  ********************************************/
  public Signal medianFilter (int n) {
    if(n<0) throw new IllegalArgumentException("The parameter must be positive: "+n+" < 0");
		double[] data=super.interpolate(0);
		if(data.length-2*n<=0) {
			throw new IllegalArgumentException("Your signal is too short for this Filter : "+data.length+" - "+(2*n)+" = "+(data.length-2*n));
		}
		double[] ans=new double[data.length-2*n];
    double[] vtemp=new double[2*n+1];
		for(int k=0;k<data.length-2*n;k++) {
      for(int l=0;l<2*n+1;l++) {
        vtemp[l]=data[k+l];
      }
      ans[k]=ArrayMath.median(vtemp);
		}
    return(copy (ans));

  }
  /****************************************************
  * This denoising method will identify
  * "short peaks" in the signal and take them away.
  * Short peaks are defined from a comparison
  * with the median filtered signal.
  * Only "significative" peaks are detected (see parameter
  * p).
  * This method won't denoise near the boundaries.
  * "Short" refers here to the time-domain and
  * not the amplitude.
  * param p percentage of the range (max-min) considered
  *   as a significative step
  * param n length of the peak in the time domain
  * exception IllegalArgumentException if p is not between
  *   0 and 1
  * exception IllegalArgumentException if the
  *   parameter n is negative
  *******************************************************/
  public Signal denoiseShortPeaks (double p, int n) {
    if((p<0)||(p>1)) {
      throw new IllegalArgumentException("The parameter p must be between 0 and 1: "+p);
    }
    double[] values=this.interpolate(0);
    double range=ArrayMath.max(values)-ArrayMath.min(values);
    double threshold=range*p;
    double[] med=(this.medianFilter(n)).interpolate(0);
    for(int k=n;k<values.length-n;k++) {
      if(Math.abs(values[k]-med[k-n])>threshold) {
        values[k]=med[k-n];
      }
    }
    return(copy(values));
  }
}

