package JSci.maths.wavelet;

import JSci.maths.*;

/*********************************************
* This class is meant to be used for
* Fast Wavelet Transform, Matching Pursuit
* and related signal processing algorithm.
* The basic idea is to automatically build
* and compress a library of "basis functions".
* Using Morse Coding this class delivers
* very fast code without sacrificing
* anything. The only fee is the one you pay
* to build the object, and it is a one time
* fee.
*
* Note : this class should be rewritten to
* use the java.util collections starting with
* jdk1.2.
*
* This class is not meant to be used directly
* but rather, you should build on it.
*
* It supports only 1D data.
**********************************************/
public class BasisFunctionLibrary implements Cloneable {
  //threshold for morse coding
  private double MorseThreshold=0.5;
	protected DiscreteFunction[] Fprimary;
	protected DiscreteFunction[] Fdual;
	protected DiscreteFunction DFunction;

  public Object clone() {
    try {
      BasisFunctionLibrary c=(BasisFunctionLibrary) super.clone();
      c.MorseThreshold=this.MorseThreshold;
      c.DFunction=(DiscreteFunction) this.DFunction.clone();
      c.Fprimary=cloneArrayDiscreteFunction(this.Fprimary);
      c.Fdual=cloneArrayDiscreteFunction(this.Fdual);
      return(c);
    } catch (CloneNotSupportedException cnse) {
      throw new InternalError();
    }
  }

  public void setMorseThreshold(double p) {
    if((p<0)||(p>1)) {
      throw new IllegalArgumentException("The threshold must be between 0 and 1 :"+p);
    }
    MorseThreshold=p;
  }

  public double getMorseThreshold() {
    return(MorseThreshold);
  }


	public void includeFourier () {
		for(int k=0;k<=Math.floor(DFunction.dimension()/2d);k++) {
			add(new Cosine(DFunction.dimension(),k));
		}

		for(int k=1;k<Math.ceil(DFunction.dimension()/2d);k++) {
			add(new Sine(DFunction.dimension(),k));
		}

	}

	public void includeMasslessFourier () {
		for(int k=1;k<=Math.floor(DFunction.dimension()/2d);k++) {
			add(new Cosine(DFunction.dimension(),k));
		}
		for(int k=1;k<Math.ceil(DFunction.dimension()/2d);k++) {
			add(new Sine(DFunction.dimension(),k));
		}
	}

	public DiscreteFunction getPrimary (int k) {
		return(Fprimary[k]);
	}

	public DiscreteFunction getDual(int k) {
		return(Fdual[k]);
	}

  protected BasisFunctionLibrary() {
  }

  public BasisFunctionLibrary(DiscreteFunction f) {
    setData(f);
  }

	/***********************************
	* Allows the user to change the
	* DiscreteFunction (see constructor).
  * @exception IllegalArgumentException if you
  *   try to change the number of data values
  *   (dimension of the DiscreteFunction)
	************************************/
	public void setData(DiscreteFunction f) {
    if(DFunction!=null) {
      if(f.dimension()!=DFunction.dimension()) {
        throw new IllegalArgumentException("You cannot change the dimension of the data object. Please create a new object.");
      }
    }
		DFunction= (DiscreteFunction) f.clone();
	}

  /********************************
  * get a copy of the data object
  * (no direct access)
  *********************************/
	public DiscreteFunction getData () {
		return((DiscreteFunction) DFunction.clone());
	}

  /*************************************
  * Clone an arry of DiscreteFunction.
  * This method is needed to handle
  * eventual null object.
  **************************************/
	protected static DiscreteFunction[] cloneArrayDiscreteFunction (MultiscaleFunction[] a) {
		if(a!=null) {

			DiscreteFunction[] b=new DiscreteFunction[a.length];
			for(int k=0;k<a.length;k++) {
				if(a[k]!=null) {
					b[k]=(DiscreteFunction) a[k].clone();
				}
			}
			return(b);
		} else {
			return(null);
		}
	}

  private DiscreteFunction toDiscreteFunction(MultiscaleFunction f) {
    int j=0;
    while(f.dimension(j)<DFunction.dimension()) {
      j++;
      if(j>20) {
        throw new IllegalScalingException("Could not match the added object with internal data object in 20 iterations.");
      }
    }
    if(f.dimension(j)!=DFunction.dimension()) {
        throw new IllegalScalingException("Could not match the added object with internal data object.");
    }
    double[] eval=ArrayMath.scalarMultiply(1/Math.sqrt((double)Cascades.PowerOf2(j)),f.evaluate(j));
    int nonzero=0;
    for(int k=0;k<eval.length;k++) {
      if(eval[k]!=0)
        nonzero++;
    }
    if(nonzero>(MorseThreshold*(double) eval.length))
      return(new DiscreteFunction(eval));
    else {
      return(new SparseDiscreteFunction(eval));

    }
  }

	/**********************************************
	* Add a clone of the given MultiscaleFunctions
	* to the internal arrays of MultiscaleFunctions.
	***********************************************/
	public void add(MultiscaleFunction fprimary, MultiscaleFunction fdual) {
		if((fprimary==null)||(fdual==null)) {
			throw new NullPointerException("You cannot add a null object to the internal arrays.");
		}
		if(Fprimary!=null) {
			DiscreteFunction[] backup=Fprimary;
			Fprimary=new DiscreteFunction[backup.length+1];
      System.arraycopy(backup,0,Fprimary,0,backup.length);
			Fprimary[backup.length]=toDiscreteFunction((MultiscaleFunction) fprimary.clone());
		} else {
			Fprimary=new DiscreteFunction[1];
			Fprimary[0]=toDiscreteFunction((MultiscaleFunction) fprimary.clone());
		}
		if(Fdual!=null) {
			DiscreteFunction[] backup=Fdual;
			Fdual=new DiscreteFunction[backup.length+1];
      System.arraycopy(backup,0,Fdual,0,backup.length);
			Fdual[backup.length]=toDiscreteFunction((MultiscaleFunction) fdual.clone());
		} else {
			Fdual=new DiscreteFunction[1];
			Fdual[0]=toDiscreteFunction((MultiscaleFunction) fdual.clone());
		}
	}

	/********************************************
	* Add the MultiscaleFunction to both the primary
	* and dual internal arrays.
	*********************************************/
	public void add(MultiscaleFunction f) {
		add(f,f);
	}

	/********************************************
	* Add the array of MultiscaleFunction to both
  * the primary and dual internal arrays.
	*********************************************/
	public void add(MultiscaleFunction[] f) {
    for(int k=0;k<f.length;k++) {
		  add(f[k],f[k]);
    }
	}

  /**************************************************
  * Attempt to add every possible functions belonging
  * to a multiresolution. Entirely automatic!
  * Limited to dyadic multiresolutions.
  ***************************************************/
	public void add (Multiresolution mr) {
		int n=DFunction.dimension();
	 	try {
			do {
				n=mr.previousDimension(n);
				for(int k=0;k<n;k++) {
					this.add(mr.primaryScaling(n,k),mr.dualScaling(n,k));
				}
				for(int k=0;k<n-mr.getFilterType();k++) {
					this.add(mr.primaryWavelet(n,k),mr.dualWavelet(n,k));
				}

			} while (true);
		} catch (IllegalScalingException e) {}

	}

	public double[] getResidues() {
		double[] coef=getWeigths();
		double[] ans=new double[coef.length];
		for(int k=0;k<ans.length;k++) {
			ans[k]=ArrayMath.norm(DiscreteHilbertSpace.add(DFunction,-coef[k],Fprimary[k]));
		}
		return(ans);
	}

	public double getResidue(int k) {
		return(ArrayMath.norm(DiscreteHilbertSpace.add(DFunction,-getWeigth(k),Fprimary[k])));
	}

	public double[] getWeigths() {
		double[] ans=new double[getSize()];
		for(int k=0;k<getSize();k++) {
			ans[k]=DiscreteHilbertSpace.integrate(DFunction,Fdual[k]);//normalisation[k];
		}
		return(ans);
	}

	public double getWeigth(int k) {
		return(DiscreteHilbertSpace.integrate(DFunction,Fdual[k]));//normalisation[k];
	}

	public double norm() {
		return(ArrayMath.norm(DFunction.evaluate(0)));
	}




	/**********************************
	* We will now attempt to match
	* each MultiscaleFunction to the length
	* of the data.
	***********************************/
	public int getSize() {
		if(Fprimary==null) {
			return(0);
		}
		return(Fprimary.length);
	}

	protected static double norm(double a, double b) {
		return(Math.sqrt(a*a+b*b));
	}

	private double[][] scalarMultiply(double[][] v, double a) {
		double[][] ans=new double[v.length][];
		for(int k=0;k<v.length;k++) {
			ans[k]=new double[v[k].length];
			for(int l=0;l<v[k].length;l++) {
				ans[k][l]=a*v[k][l];
			}
		}
		return(ans);
	}
	/*************************************
	* Checks whether whether or not the
	* biorthogonality is satisfied
  * If so, the return array should
  * <I>roughly</I> be
  * filled with ones.
	**************************************/
	public double[] checkBiorthogonality() {
		double[] ans=new double[getSize()];
		for(int k=0;k<getSize();k++) {
        ans[k]=DiscreteHilbertSpace.integrate(Fprimary[k],Fdual[k]);
    }
		return(ans);
	}

	protected static double[][] add(double[][] v, double[][] w) {
		if(v.length!=w.length) {
			throw new IllegalArgumentException("Incompatible types "+v.length+", "+w.length );
		}
		double[][] ans=new double[v.length][];
		for(int k=0;k<v.length;k++) {
			ans[k]=new double[v[k].length];
			if(v[k].length!=w[k].length) {
				throw new IllegalArgumentException("Incompatible types "+v.length+", "+w.length+" : " +v[k].length+", "+w[k].length );
			}
			for(int l=0;l<v[k].length;l++) {
				ans[k][l]=v[k][l]+w[k][l];
			}
		}
		return(ans);
	}
}

