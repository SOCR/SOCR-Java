
package JSci.maths.wavelet;

import JSci.maths.*;

/****************************************
* This class is a standard implementation
* of the Cascades algorithm.
* @author Daniel Lemire
*****************************************/
public final class Cascades extends AbstractMath {
	private Cascades(){}

	/****************************************
  * This method return the number of "scaling"
  * functions at the previous scale given a
  * number of scaling functions. The answer
  * is always smaller than the provided value
  * (about half since this is a dyadic
  * implementation). This relates to the same idea
  * as the "filter type".
	*****************************************/
	public static int previousDimension (int filtretype, int k) {
		int i=(int) Math.round((k+filtretype)/2d);
		if(2*i-filtretype==k) {
			return(i);
		} else {
			if(2*Math.round(k/2d)==k) {
				throw new IllegalScalingException("Odd number of values into an even filter! Change the number of values/iterations: "+k);
			} else {
				throw new IllegalScalingException("Even number of values into an odd filter! Change the number of values/iterations: "+k);
			}
		}
	}

	public static double[] evalScaling (Filter filtre,int n0, int j1, int k) {
		if (j1>20) {
			throw new IllegalArgumentException("Too many iterations : "+j1);
		}
		if(n0<=0) {
			throw new IllegalArgumentException("Must have a positive number of scaling functions : "+n0+" < 0.");
		}
		double[] init=new double[n0];
		if((k>=init.length)||(k<0)) {
			throw new IllegalArgumentException("There are "+init.length+" scaling functions going from 0 to "+(init.length-1)+" and you are trying to get the "+k+"th function.");

		}
		init[k]=1;/* initialisation */
		return(evaluation(filtre, j1, init));
	}


	public static double[] evalWavelet (Filter filtre, int filtretype, int n0, int j1, int k) {
		if (j1>20) {
			throw new IllegalArgumentException("Too many iterations : "+j1);
		}
		if(n0-filtretype<=0) {
			throw new IllegalArgumentException("With "+n0+" scaling functions and a filter of type "+filtretype+", you are left with no wavelets. Please change the number of scaling functions, the multiresolution or the number of iterations");
		}
		double[] init=new double[n0-filtretype];
		if((k>=init.length)||(k<0)) {
			throw new IllegalArgumentException("There are "+init.length+" wavelets going from 0 to "+(init.length-1)+" and you are trying to get to the "+k+"th wavelet.");
		}
		init[k]=1;
		double[] filtreWavelet=filtre.highpass(init);
		return(evaluation(filtre,j1,filtreWavelet));
	}

	public static double[] evalWavelet (Filter filtre,int n0, int j1, int k) {
		return(evalWavelet(filtre,1,n0,j1,k));
	}

	public static double[] evalWaveletHaar (Filter filtre,int n0, int j1, int k) {
		return(evalWavelet(filtre,0,n0,j1,k));
	}

	public static double[] evalWaveletQuadratic (Filter filtre, int n0, int j1, int k) {
		return(evalWavelet(filtre,2,n0,j1,k));
	}

    public static int PowerOf2(int pwrOf2) {
    	if(pwrOf2<0) {
			throw new IllegalArgumentException("This parametre must be positive : "+pwrOf2+" < 0");
		}
		int reponse=1;
        for(int i=pwrOf2;i>0;i--) {
			reponse=reponse*2;
        }
        return(reponse);
    }


    /************************************************
    * method used to oversample according to
    * the lazy (Dirac Delta Function) interpolation
    *************************************************/
    public static double[] oversample(double data[]) {
		double answer[]=new double[2*data.length-1];
		for(int i=0;i<data.length-1;i++) {
			answer[2*i]=data[i];
		}
		answer[2*data.length-2]=data[data.length-1];
		return answer;
  	}

    /*********************************************
    * Method used to oversample according to the
    * Haar transform
    **********************************************/
    public static double[] doublesample(double data[]) {
		int Nombre=data.length;
		double answer[]=new double[2*Nombre];
		for(int i=0;i<Nombre;i++) {
			answer[2*i+1]=data[i];
		}
		return answer;
        }
    /**************************************************
    * Special oversampling for the linear spline
    * interpolation
    ***************************************************/
    public static double[] supersample(double data[]) {
		int Nombre=data.length;
		double answer[]=new double[2*Nombre+1];
		for(int i=0;i<Nombre;i++) {
			answer[2*i+1]=data[i];
		}
		return answer;
        }
  /**********************************
  * Special oversampling for the
  * quadratic spline interpolation
  ***********************************/
	public static double[] quadraticOversample( double[] v) {
		double[] ans=new double[2*v.length-2];
		ans[0]=v[0];
		ans[ans.length-1]=v[v.length-1];
		for(int k=1;k<v.length-1;k++) {
			ans[2*k-1]=v[k];
		}
		return(ans);
	}
  /****************************************************
  * Starting with n0 scaling functions and
  * going jfin scales ahead (iterating jfin times),
  * tells you how many scaling functions you'll have.
  * (Dyadic multiresolutions of type filtretype.)
  * @param filtertype filter type
  * @param n0 number of scaling functions initially
  * @param jfin number of iterations
  ******************************************************/
	public static int dimension(int n0, int jfin, int filtertype) {
		return(PowerOf2(jfin)*(n0-filtertype)+filtertype);

	}

  /****************************************************
  * Starting with n0 scaling functions and
  * going jfin scales ahead (iterating jfin times),
  * tells you how many scaling functions you'll have.
  * (Linear spline and dyadic multiresolution of type 1.)
  * @param n0 number of scaling functions initially
  * @param jfin number of iterations
  ******************************************************/
	public static int dimension(int n0, int jfin) {
		return(Cascades.PowerOf2(jfin)*(n0-1)+1);
	}
  /****************************************************
  * Starting with n0 scaling functions and
  * going jfin scales ahead (iterating jfin times),
  * tells you how many scaling functions you'll have.
  * (Haar and dyadic multiresolution of type 0.)
  * @param n0 number of scaling functions initially
  * @param jfin number of iterations
  ******************************************************/
	public static int dimensionHaar(int n0, int jfin) {
		return(Cascades.PowerOf2(jfin)*(n0));
	}

	public static double[] evaluation (Filter filtre, int j1, double[] init) {
		if (j1<0) {
			throw new IllegalArgumentException("Incorrect parameters : "+j1+" < 0 ");
		}
		if (j1>20) {
			throw new IllegalArgumentException("Excessive number of iterations: "+j1);
		}
		double[] data=init;
		for (int j=0;j<j1;j++){
			data=filtre.lowpass(data);
		}
		return(data);
	}

}
