package JSci.maths.wavelet;

import JSci.maths.*;
import JSci.maths.wavelet.*;
import JSci.maths.wavelet.splines.*;

/****************************************
* This class provides support for basic
* operations on MultiscaleFunction.
* Whenever it isn't specified we are working
* over the interval [0,1]
* Be careful when using integrate...
* Indeed, integrate(f,g) is not, in
* general, the same thing as integrate(g,f).
* @author Daniel Lemire
*****************************************/
public final class DiscreteHilbertSpace {
        private DiscreteHilbertSpace() {}

  /*************************************
  * Maximum number of allowed iterations
  * in order to match objects.
  **************************************/
	public static int maxiter=20;

	/***********************************
	* Calculates f+a*g. Careful before
	* using this method since it uses
	* a specific normalisation. Useful
	* for matching pursuit purpose.
	************************************/
	public static double[] add(MultiscaleFunction f,double a ,MultiscaleFunction g,int j1) {
		if(j1<0) {
                        throw new IllegalArgumentException("The precision parameter must be positive : "+j1);
		}
		if (j1>maxiter) {
			throw new IllegalArgumentException("Too many iterations : "+j1);
		}
		int[] Compatibilite=getScalingCoefficients(f,g,j1);
		int K=Compatibilite[0];
		int Ks=Compatibilite[1];
                double[] v1=ArrayMath.scalarMultiply(Cascades.PowerOf2(K+j1),f.evaluate(Ks+j1));
                double[] v2prime=ArrayMath.scalarMultiply(Cascades.PowerOf2(Ks+j1),g.evaluate(K+j1));
                double[] v2=ArrayMath.scalarMultiply(a,v2prime);
		return(ArrayMath.add(v1,v2));
        }
	/***********************************
	* Calculates f+a*g. Careful before
	* using this method since it uses
	* a specific normalisation. Useful
	* for matching pursuit purpose.
	************************************/
	public static DoubleSparseVector add (SparseDiscreteFunction f,double a ,MultiscaleFunction g, int j1) {
		if(j1<0) {
                        throw new IllegalArgumentException("The precision parameter must be positive : "+j1);
		}
		if (j1>maxiter) {
                        throw new IllegalArgumentException("Too many iterations : "+j1);
		}
		int[] Compatibilite=getScalingCoefficients(f,g,j1);
		int K=Compatibilite[0];
		int Ks=Compatibilite[1];
                DoubleSparseVector v1=(DoubleSparseVector)f.Data.scalarMultiply((double) Cascades.PowerOf2(K+j1));
                double[] v2prime=ArrayMath.scalarMultiply(Cascades.PowerOf2(Ks+j1),g.evaluate(K+j1));
                double[] v2=ArrayMath.scalarMultiply(a,v2prime);
                return(v1.add(new DoubleSparseVector(v2)));
	}

	/***********************************
	* Calculates f+a*g. Careful before
	* using this method since it uses
	* a specific normalisation. Useful
	* for matching pursuit purpose.
	************************************/
	public static DoubleSparseVector add (MultiscaleFunction g,double a ,SparseDiscreteFunction f, int j1) {
                return(add(f,a,g,j1));
        }

	/***********************************
	* Calculates f+a*g. Careful before
	* using this method since it uses
	* a specific normalisation. Useful
	* for matching pursuit purpose.
	************************************/
	public static double[] add(DiscreteFunction f,double a ,DiscreteFunction g) {
                if((g instanceof SparseDiscreteFunction)&&(f instanceof SparseDiscreteFunction)) {
                        integrate((SparseDiscreteFunction)f,(SparseDiscreteFunction)g);
                }
                if(g instanceof SparseDiscreteFunction) {
                        integrate(f,(SparseDiscreteFunction)g) ;
                }
                if(f instanceof SparseDiscreteFunction) {
                        integrate((SparseDiscreteFunction)f,g) ;
                }
                double[] v1=f.evaluate();
                double[] v2prime=g.evaluate(0);
                double[] v2=ArrayMath.scalarMultiply(a,v2prime);
		return(ArrayMath.add(v1,v2));
	}
	/***********************************
	* Calculates f+a*g. Careful before
	* using this method since it uses
	* a specific normalisation. Useful
	* for matching pursuit purpose.
	************************************/
	public static DoubleSparseVector add (SparseDiscreteFunction f,double a ,SparseDiscreteFunction g) {
                DoubleSparseVector v2=(DoubleSparseVector)g.Data.scalarMultiply(a);
		return(v2.add(f.Data));
	}
	/***********************************
	* Calculates f+a*g. Careful before
	* using this method since it uses
	* a specific normalisation. Useful
	* for matching pursuit purpose.
	************************************/
	public static DoubleSparseVector add (DiscreteFunction f,double a ,SparseDiscreteFunction g) {
                if(f instanceof SparseDiscreteFunction) {
                        integrate((SparseDiscreteFunction)f,g);
                }
                DoubleSparseVector v2=(DoubleSparseVector)g.Data.scalarMultiply(a);
                return(v2.add(new DoubleSparseVector(f.evaluate())));
	}
	/***********************************
	* Calculates f+a*g. Careful before
	* using this method since it uses
	* a specific normalisation. Useful
	* for matching pursuit purpose.
	************************************/
	public static DoubleSparseVector add (SparseDiscreteFunction f,double a ,DiscreteFunction g) {
                if(g instanceof SparseDiscreteFunction) {
                        integrate(f,(SparseDiscreteFunction)f) ;
                }
		return(add(g,a,f));
	}


	/***********************************************
	************************************************/
	public static double integrate (MultiscaleFunction f,MultiscaleFunction g, int j1, double a, double b) {
		if(j1<0) {
                        throw new IllegalArgumentException("The precision parameter must be positive : "+j1);
		}
		if(j1>maxiter) {
			throw new IllegalArgumentException("Too many iterations : "+j1);
		}
		int[] Compatibilite=getScalingCoefficients(f,g,j1);
		int K=Compatibilite[0];
		int Ks=Compatibilite[1];
		double massbrute=integrate(f,g,j1,K,Ks);
		double mass=massbrute*Math.abs(b-a);//Normalisation
		return(mass);
	}

	public static double integrate (DiscreteFunction f,DiscreteFunction g) {
// System.out.println(f.evaluate().length+" "+g.evaluate().length);
                if((f instanceof SparseDiscreteFunction)&&(g instanceof SparseDiscreteFunction)) {
                        integrate((SparseDiscreteFunction)f,(SparseDiscreteFunction)g);
                }
                if(f instanceof SparseDiscreteFunction) {
                        integrate((SparseDiscreteFunction)f,g);
                }
                if(g instanceof SparseDiscreteFunction) {
                        integrate(f,(SparseDiscreteFunction)g);
                }
		return ArrayMath.scalarProduct(f.evaluate(),g.evaluate());
	}
	public static double integrate (SparseDiscreteFunction f,DiscreteFunction g) {
                if(g instanceof SparseDiscreteFunction) {
                        integrate(f,(SparseDiscreteFunction)g) ;
                }
		return f.Data.scalarProduct(new DoubleVector(g.evaluate()));
	}
	public static double integrate (DiscreteFunction f,SparseDiscreteFunction g) {
                if(f instanceof SparseDiscreteFunction) {
                        integrate((SparseDiscreteFunction)f,g) ;
                }
		return g.Data.scalarProduct(new DoubleVector(f.evaluate()));
	}
  /************************
  * This method could be
  * optimized further
  *************************/
	public static double integrate(SparseDiscreteFunction f,SparseDiscreteFunction g) {
		return g.Data.scalarProduct(new DoubleVector(f.evaluate()));
	}

	public static double integrate(Cosine f,DiscreteFunction g) {
                if(g instanceof SparseDiscreteFunction) {
                        integrate(f,(SparseDiscreteFunction)g) ;
                }
		return ArrayMath.scalarProduct(f.evaluate(),g.evaluate());
	}

	public static double integrate(Sine f,DiscreteFunction g) {
                if(g instanceof SparseDiscreteFunction) {
                        integrate(f,(SparseDiscreteFunction)g) ;
                }
		return ArrayMath.scalarProduct(f.evaluate(),g.evaluate());
	}

	public static double integrate(DiscreteFunction f,Sine g) {
                if(f instanceof SparseDiscreteFunction) {
                        integrate((SparseDiscreteFunction)f,g) ;
                }
		return ArrayMath.scalarProduct(f.evaluate(),g.evaluate());
	}

	public static double integrate(DiscreteFunction f,Cosine g) {
                if(f instanceof SparseDiscreteFunction) {
                        integrate((SparseDiscreteFunction)f,g);
                }
		return ArrayMath.scalarProduct(f.evaluate(),g.evaluate());
	}
	public static double integrate (Cosine f,SparseDiscreteFunction g) {
		return g.Data.scalarProduct(new DoubleVector(f.evaluate()));
	}

	public static double integrate (Sine f,SparseDiscreteFunction g) {
		return g.Data.scalarProduct(new DoubleVector(f.evaluate()));
	}

	public static double integrate (SparseDiscreteFunction f,Sine g) {
		return f.Data.scalarProduct(new DoubleVector(g.evaluate()));
        }

	public static double integrate (SparseDiscreteFunction f,Cosine g) {
		return f.Data.scalarProduct(new DoubleVector(g.evaluate()));
	}



	/***********************************************
	************************************************/
	public static double integrate (MultiscaleFunction f,MultiscaleFunction g, int j1) {
		if(j1<0) {
      throw new IllegalArgumentException("The precision parameter must be positive : "+j1);
		}
		if (j1>maxiter) {
			throw new IllegalArgumentException("Too many iterations : "+j1);
		}
		int[] Compatibilite=getScalingCoefficients(f,g,j1);
		int K=Compatibilite[0];
		int Ks=Compatibilite[1];
		return(integrate(f,g,j1,K,Ks));
	}


	/***********************************************
	************************************************/
	private static double integrate (MultiscaleFunction f,MultiscaleFunction g, int j1, int K, int Ks) {
		if(j1<0) {
      throw new IllegalArgumentException("The precision parameter must be positive : "+j1);
		}
		if (j1>maxiter) {
			throw new IllegalArgumentException("Too many iterations : "+j1);
		}
		double massbrute=ArrayMath.scalarProduct(f.evaluate(Ks+j1),g.evaluate(K+j1));
		int normalisation=Cascades.PowerOf2(Math.min(Ks,K)+j1);
		return(massbrute/normalisation);
	}

	/***********************************************
	***********************************************/
	public static double integrate (PiecewiseConstant f,MultiscaleFunction g, int j1, double a, double b) {
		if(j1<0) {
throw new IllegalArgumentException("The precision parameter must be positive : "+j1);
		}
		if (j1>maxiter) {
			throw new IllegalArgumentException("Too many iterations : "+j1);
		}
		LinearSpline ftest=new LinearSpline(new double[f.dimension(0)+1]);
		int[] Compatibilite=getScalingCoefficients(ftest,g,j1);
		int K=Compatibilite[0];
		int Ks=Compatibilite[1];

		double[] fdata=f.evaluate(K);
		double[] gdata=g.evaluate(Ks+j1);

		int sampling= (int) Math.round((gdata.length-1)/(double)fdata.length);
		double massbrute=0.0;
		for(int k=0;k<fdata.length;k++) {
			double moyenne=0.0;
			for(int l=sampling*k;l<=sampling*(k+1);l++) {
				moyenne+=gdata[l]/(sampling+1);
			}
			massbrute+=moyenne*fdata[k];
		}
		double mass=massbrute*Math.abs(b-a);//Normalisation
		return(mass);
	}
	/***********************************************
	***********************************************/
	public static double integrate (PiecewiseConstant f,MultiscaleFunction g, int j1) {
		if(j1<0) {
throw new IllegalArgumentException("The precision parameter must be positive : "+j1);
		}
		if (j1>maxiter) {
			throw new IllegalArgumentException("Too many iterations : "+j1);
		}
		return(integrate(f,g,j1,0.0,1.0));
	}

	/**********************************************
	***********************************************/
	public static double integrate (SumOfDiracs f,MultiscaleFunction g, int j1, double a, double b) {
		if(j1<0) {
throw new IllegalArgumentException("The precision parameter must be positive : "+j1);
		}
		if (j1>maxiter) {
			throw new IllegalArgumentException("Too many iterations : "+j1);
		}
		return(integrate(f,g,j1));
	}

	/**********************************************
	***********************************************/
	public static double integrate (SumOfDiracs f,MultiscaleFunction g, int j1) {
		if(j1<0) {
                        throw new IllegalArgumentException("The precision parameter must be positive : "+j1);
		}
		if (j1>maxiter) {
			throw new IllegalArgumentException("Too many iterations : "+j1);
		}
		int[] Compatibilite=getScalingCoefficients(f,g,j1);
		int K=Compatibilite[0];
		int Ks=Compatibilite[1];
		double[] fdata=f.evaluate(Ks+j1);
		double[] gdata=g.evaluate (K+j1);
		if(fdata.length==gdata.length) {
			return(ArrayMath.scalarProduct(fdata,gdata));
		} else {
			throw new IllegalArgumentException("This type of integration is not handled by this class. Please use 0 as the precision parameter: "+j1+" != 0");
		}
	}


	/*************************************************
	*************************************************/
	public static double integrate (SumOfDiracs f,double[] v) {
		if(f.dimension()!=v.length) {
			throw new IllegalArgumentException("Can only integrate if the number of knots is the same: "+f.dimension()+", "+v.length);
		}
		double somme=0.0;
		for(int L=0;L<v.length;L++) {
			somme+=f.getValue(L)*v[L];
		}
		return(somme);
	}

	/******************************************************
	* While this method is public (for transparency reasons)
	* it should not be called by other classes unless
	* absolutely necessary.
	*******************************************************/
	public static int[] getScalingCoefficients(MultiscaleFunction f,MultiscaleFunction g, int j1) {
		int[] sortie=new int[2];
		for(sortie[0]=0;f.dimension(j1)>g.dimension(sortie[0]+j1);sortie[0]++) {
			if((f.dimension(j1)<g.dimension(sortie[0]+j1))||(sortie[0]>maxiter)) {
				throw new IllegalArgumentException("The objects are incompatible and cannot be integrated according to this class (1) : "+f.dimension(j1)+", "+g.dimension(j1)+" : "+sortie[0]+"\nYou might have to write you own method.");
			}
		}
		for(sortie[1]=0;f.dimension(sortie[1]+j1)<g.dimension(j1+sortie[0]);sortie[1]++) {
			if((f.dimension(j1+sortie[1])>g.dimension(sortie[0]+j1))||(sortie[1]>maxiter)) {
				throw new IllegalArgumentException("The objects are incompatible and cannot be integrated according to this class (2)  : "+f.dimension(j1)+", "+g.dimension(j1)+" : "+sortie[1]+"\nYou might have to write you own method.");
			}
		}
		if(f.dimension(sortie[1]+j1)!=g.dimension(sortie[0]+j1)) {
			throw new IllegalArgumentException("The objects are incompatible and cannot be integrated according to this class (?) :"+f.dimension(j1)+", "+g.dimension(j1)+" : "+sortie[0]+" "+sortie[1]+"\nYou might have to write you own method.");
		}
		return(sortie);
	}
}

