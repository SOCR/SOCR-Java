package JSci.maths;

/**
* This class is dedicated to engineering methods applied to arrays including
* signal processing.
* All methods here are safe, that is, they create copies of arrays whenever
* necessary.
* This makes for slower methods, but the programmer can always
* define his own methods optimized for performance.
* @author Daniel Lemire
*/
public final class EngineerMath extends AbstractMath {
        private EngineerMath() {}

        /**
        * Return a running average over the data.
        * The returned array has the same size as the input array.
        * It is often used in signal processing to estimate the baseline.
        * @param v the data.
        * @param width width of the average.
        * @exception IllegalArgumentException if v.length < width
        * @exception IllegalArgumentException if width is even
        * @exception IllegalArgumentException if v.length = 0
        */
        public static double[] runningAverage(double[] v, int width) {
    if(v.length<width)
      throw new IllegalArgumentException("Array must be at least as long as the required width : "+v.length+" < "+width);
    if((width&1)==0)
      throw new IllegalArgumentException("The parameter 'width' must be odd : "+width);
    if(v.length==0)
      throw new IllegalArgumentException("Empty array, nothing to compute!");
    int halfsize=width >> 1;
    double[] ans=new double[v.length];
    ans[0]=ArrayMath.mean(ArrayMath.extract(0,width-1,v));
    for(int k=1;k<halfsize;k++) {
       ans[k]=ans[0];
    }
    for(int k=halfsize;k<v.length-halfsize-1;k++) {
      ans[k]=ArrayMath.mean(ArrayMath.extract(k-halfsize,k+halfsize,v));
    }

    ans[v.length-halfsize-1]=ArrayMath.mean(ArrayMath.extract(v.length-1-width+1,v.length-1,v));
    for(int k=v.length-halfsize;k<v.length;k++) {
      ans[k]=ans[v.length-halfsize-1];
    }
    return(ans);
  }
        /**
        * Return a running median over the data.
        * The returned array has the same size as the input array.
        * It is often used in signal processing to estimate the baseline.
        * Safer than the running average, but somewhat more costly computationally.
        * @param v the data
        * @param width width of the average
        * @exception IllegalArgumentException if v.length < width
        * @exception IllegalArgumentException if width is even
        */
        public static double[] runningMedian(double[] v, int width) {
    if(v.length<width)
      throw new IllegalArgumentException("Array must be at least as long as the required width : "+v.length+" < "+width);
    if((width&1)==0)
      throw new IllegalArgumentException("The parameter 'width' must be odd : "+width);
    if(v.length==0)
      throw new IllegalArgumentException("Empty array, nothing to compute!");
    int halfsize=width >> 1;
    double[] ans=new double[v.length];
    ans[0]=ArrayMath.median(ArrayMath.extract(0,width-1,v));
    for(int k=1;k<halfsize;k++) {
       ans[k]=ans[0];
    }
    for(int k=halfsize;k<v.length-halfsize-1;k++) {
      ans[k]=ArrayMath.median(ArrayMath.extract(k-halfsize,k+halfsize,v));
    }

    ans[v.length-halfsize-1]=ArrayMath.median(ArrayMath.extract(v.length-1-width+1,v.length-1,v));
    for(int k=v.length-halfsize;k<v.length;k++) {
      ans[k]=ans[v.length-halfsize-1];
    }
    return(ans);
  }

	/**
	* Shannon entropy of an array.
        * Please check that the mass of the array is 1 before using this method.
        * Use the method "entropy" otherwise.
        * @planetmath ShannonsTheoremEntropy
        */
	public static double icf(double[] v) {
		double ans=0;
		for(int j=0;j<v.length;j++) {
			if(v[j]!=0) {
				if (v[j]<0) {
					throw new IllegalArgumentException("You cannot take the ICF of a array having negative components : v ["+j+"] = "+v[j]+" < 0");
				}
				ans-=v[j]*Math.log(v[j]);
			}
		}
		return(ans);
	}

	/**
	* Compute the entropy of an array.
        * The entropy as defined using the absolute value.
        */
	public static double entropy(double[] v) {
		double[] temp=ArrayMath.abs(v);
		double m=ArrayMath.mass(temp);
		if(m==0) return(0);
		temp=ArrayMath.scalarMultiply(1/m, temp);
		return(icf(temp));

	}

	/**
	* Compute the entropy of an array.
        * The entropy as defined using the absolute value.
        */
	public static double entropy(int[] v) {
		int[] temp=ArrayMath.abs(v);
		int m=ArrayMath.mass(temp);
		if(m==0) return(0);
		double[] t2=ArrayMath.scalarMultiply(1.0/m, temp);
		return(icf(t2));
	}
	/**
	* Set an array to the specified length resampling using linear interpolation.
        */
	public static double[] resample(double[] data, int newl) {
                if(newl<=0)
                        throw new IllegalArgumentException("New length must be strictly positive : "+newl+" <=0 ");
                double[] ans=new double[newl];
                double doublepos, mod;
                int base1, base2;
                for(int k=0;k<newl;k++) {
                        doublepos =  k/(double)(newl-1)*(data.length-1);
                        base1 = (int) Math.floor(doublepos);
                        base2 = (int) Math.ceil(doublepos);
                        mod = doublepos-base1;
                        ans[k]=data[base1]*mod+data[base2]*(1.0-mod);
                }
		return(ans);
	}
}

