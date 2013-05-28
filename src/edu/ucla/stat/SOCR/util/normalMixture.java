/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/
package edu.ucla.stat.SOCR.util;

import java.lang.Math;
//import edu.ucla.stat.SOCR.distribution.*;


public class normalMixture {

    //Variables decleration
    private int[] zVar;
    private double tolerance = (double) 0.001;
    private double[] rawData;
    private normComponent[] compArray;
    private int mixCount;
    private edu.ucla.stat.SOCR.distributions.NormalDistribution  Mix;

    public normalMixture(int mixCt, float[] rawDat, double tol) {
		//////System.out.println("normalMixture constructor rawDat.length = " + rawDat.length);

	    double[] temp = new double[rawDat.length];
	    for(int i = 0; i<rawDat.length;i++)
		   temp[i] = (double) rawDat[i];

		mixInit(mixCt,temp ,tol);
    }


    public normalMixture(int mixCt,double[] rawDat ,double tol) {
	    mixInit(mixCt,rawDat ,tol);

    }

	public void mixInit(int mixCt,double[] rawDat ,double tol) {
		////System.out.println("normalMixture mixInit rawDat.length = " + rawDat.length);
		edu.ucla.stat.SOCR.distributions.ContinuousUniformDistribution meanDistribution;
		edu.ucla.stat.SOCR.distributions.ContinuousUniformDistribution sdDistribution;
		
		mixCount = mixCt;
		rawData = rawDat;
		compArray = new normComponent[mixCount];
		////System.out.println("normalMixture mixInit compArray = " + compArray);

		double min = minDouble(rawData);
		double max = maxDouble(rawData);
		////System.out.println("normalMixture mixInit min = " + min);
		////System.out.println("normalMixture mixInit max = " + max);

		double increment = (max - min)/(mixCount+1);
		zVar = new int[rawData.length];
		////System.out.println("normalMixture mixInit increment = " + increment);
		////System.out.println("normalMixture mixInit zVar = " + zVar);

		// this part causes problem.
		Mix = new edu.ucla.stat.SOCR.distributions.NormalDistribution(rawDat, true);
		Mix.initialize();
		////System.out.println("normalMixture mixInit Mix = " + Mix);


		for(int i=0;i<rawData.length;i++)
			zVar[i] = 0;
		//Math.random();
		//Math.random()

		double[] weight = new double[mixCount];
		//////System.out.println("normalMixture mixInit weight = " + weight);

		double wtSum = 0;

		for(int i=0;i<mixCount;i++) {
			weight[i] = Math.random();
			wtSum = wtSum + weight[i];
		}

		for(int i=0;i<mixCount;i++) {		
			// Dec 17, 2009: Updated the Mixture Initialization to introduce more randomness in the
			// selection of the means and variances of the initial Gaussian mixture components
			
			if (max==min) max=min+0.1;
			else if (max<min) { 
				double temp = min; 
				min=max; 
				max = temp; 
			}
			meanDistribution = 
				new edu.ucla.stat.SOCR.distributions.ContinuousUniformDistribution((min-(max-min)/5), 
						max+(max-min)/5);
			sdDistribution = 
				new edu.ucla.stat.SOCR.distributions.ContinuousUniformDistribution((max-min)/(10*(mixCount+1)), 
						(max-min)/(5*(mixCount+1)));
			
			compArray[i] = new normComponent(meanDistribution.simulate(), sdDistribution.simulate(),weight[i]/wtSum);	
			
			// OLD Code as of dec 16, 2009
			// compArray[i] = new normComponent((double)(max-min)*Math.random(), 
			//					Mix.getVariance()*Math.random()/mixCt,weight[i]/wtSum);
		}
		//compArray[i] = new normComponent((double)(min+(i+1)*increment + Math.random()*.2 ), Math.sqrt(Mix.getVariance()/mixCt)+ Math.random()*.2,(double)1/mixCount);
		//System.out.println("normalMixture mixInit end");
	}





    public void updateEStep() {
      double denominator = 0;
      double pVal = 0;
       double temp  = 0;
       int index = 0;
      for(int i=0;i<zVar.length;i++) {
          denominator = 0;
          pVal = 0;
          temp  = 0;
          index = 0;
          for(int j=0;j<mixCount;j++)
          denominator = denominator + compArray[j].getDensity(rawData[i]);
         // Hard Coding values
         for(int j=0;j<mixCount;j++) {
          temp = compArray[j].getDensity(rawData[i])/denominator;
          if(temp>=pVal) {
          pVal = temp;
          index = j;
          }

          }
        zVar[i] = index;
      }
    }


     public void updateMStep() {
     double wt = 0;
     double mu = 0;
     double var = 0;
        for(int i=0;i<mixCount;i++) {
             wt = 0;
             mu = 0;
             var = 0;
                for(int j = 0;j< zVar.length;j++ )  {

                if(zVar[j] ==i) {
                wt = wt+1;
                mu = mu + rawData[j];

                }



                }
                mu = mu/wt;

                for(int j = 0;j< zVar.length;j++ )  {

                if(zVar[j] ==i)
                var = var + Math.pow(rawData[j] - mu,2);

                }

                var = var/wt;


        wt = wt/zVar.length;
        compArray[i].updateComponent(mu, Math.sqrt(var) , wt);

        }



    }

     public double getMixDensity(double y) {
     double density = 0;
     for(int i=0;i<mixCount;i++)
         density = density + compArray[i].getDensity(y);
       return density;

     }

     public double getKernalDensity(int i,double y) {
     double density = 0;
     density = compArray[i].getDensity(y);
     return density;
     }


    public double getMean(int i) {
    if(mixCount >=i)
        return compArray[i].getMean();
    else
        return 0;
    }

     public double getVariance(int i) {
    if(mixCount >=i)
        return compArray[i].getVariance();
    else
        return 0;
    }
     public int getCount() {
         return mixCount;
     }

     public double getWeight(int i) {
    	 if(mixCount >=i)
    		 return compArray[i].getWeight();
    	 else
    		 return 0;
     }

     public double[] getWeights() {
    	 double [] weights = new double [getCount()];
    	 for (int i=0; i< getCount(); i++)
    		 weights[i] = compArray[i].getWeight();
    	 return weights;
     }




     public double maxDouble(double[] X) {
      	double iMax = X[0];
          	for(int i =0; i< X.length; i++) {
          		if( X[i] > iMax)
              		iMax = X[i];
          	}
      	return iMax;
      }

      public double minDouble(double[] X) {
      	double iMin = X[0];
          	for(int i =0; i< X.length; i++) {
          		if( X[i] < iMin)
              	iMin = X[i];
          	}
      	return iMin;
      }





}

