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

package edu.ucla.stat.SOCR.experiments.util;

import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.core.ValueSetter;
import edu.ucla.stat.SOCR.distributions.ChiSquareDistribution;
import edu.ucla.stat.SOCR.distributions.NormalDistribution;
import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import edu.ucla.stat.SOCR.distributions.FisherDistribution;

/**
Rahul Gidwani - UCLA Statistics
 */

public enum IntervalType{

    xbar_sigmaKnown {
    	double[] xBar;
        public String toString() {
        	//System.out.println("IntervalType toString bootStrap"+bootStrap);
        	if(bootStrap)
        		return "Mean - BootStrap";
        	else
        		return "Mean - Population Variance Known";
        }

        public double[][] getConfidenceIntervals() {
            double x_bar[] = getMeans();
            double s[] = getKnownStdDev();
            xBar = new double[getNTrials()];
            double[] alphaValues = this.getAlpha();
            double[][] ci_array = new double[getNTrials()][2];
            
            if(bootStrap){
            	//System.out.println("xbar_sigmaKnown bootStrap=true"+"1-halfAlpha="+(1-halfAlpha[getCvIndex()]));

            	for (int i = 0; i < getNTrials(); i++) {
            		  bootStrapDataForOneTrial=getBootStrapData(i);	 
            		  // get xbar for all bootSrapSample
            		  double[] xBarBootStrap = getBootStrapMeans();
            		  // System.out.println("bootStrapBar size"+xBarBootStrap.length);
            		  try{            			  
            			  ci_array[i] =  BootStrapSort.getBounds(xBarBootStrap, halfAlpha[getCvIndex()]); //upper[0]. lower[1]            			 
            		  }catch(Exception e){}
            		  xBar[i]= x_bar[i];
            	}
            }else {
	            for (int i = 0; i < getNTrials(); i++) {
	                ci_array[i][0] = x_bar[i] + (alphaValues[0] * s[i]) / Math.sqrt(getSampleSize()); //upper
	                ci_array[i][1] = x_bar[i] + (alphaValues[1] * s[i]) / Math.sqrt(getSampleSize()); //lower
	                xBar[i]= x_bar[i];
	            }
            }
            return ci_array;
        }

        public double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new NormalDistribution(0, 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }
        
        public double[] getX_bar(){
        	 return(xBar);
        }
    },


    xbar_sigmaUnknown {
    	double[] xBar;
    	
        public String toString() {
        	if(bootStrap)
        		return "";
        	else
        		return "Mean - Population Variance Unknown";
        }

        public double[][] getConfidenceIntervals() {
            double x_bar[] = getMeans();
            double s[] = getStdDev();
            xBar = new double[getNTrials()];
            double[] alphaValues = this.getAlpha();
            double[][] ci_array = new double[getNTrials()][2];
            if(bootStrap){
            	//System.out.println("Mean - Population Variance Unknown bootStrap=true");
            
            	/* for (int i = 0; i < getNTrials(); i++) {
            		  bootStrapDataForOneTrial=getBootStrapData(i);
            		  //get P for all bootSrapSample
            		  double[] xBarBootStrap = getBootStrapMeans();
            		  bootStrap2Histogram(bootStrapDataForOneTrial);
            		  ci_array[i][0] = histUtil.getUpperCutOffValue(); //upper
            		  ci_array[i][1] = histUtil.getLowerCutOffValue(); //lower
            		// ci_array[i][0] = x_bar[i] + (alphaValues[0] * s[i]) / Math.sqrt(getBootStrapDataSize());//upper
            		// ci_array[i][1] = x_bar[i] + (alphaValues[1] * s[i]) / Math.sqrt(getBootStrapDataSize());//lower
 	                 xBar[i]= x_bar[i];
            	 }*/
            	 for (int i = 0; i < getNTrials(); i++) {
	           		  bootStrapDataForOneTrial=getBootStrapData(i);	 
	           		  //get xbar for all 1K bootSrapSample
	           		  double[] xBarBootStrap = getBootStrapMeans();
	           		//  System.out.println("bootStrapBar size"+xBarBootStrap.length);
	           		  try{	  
	           			  ci_array[i] =  BootStrapSort.getBounds(xBarBootStrap, halfAlpha[getCvIndex()]); //upper[0]. lower[1]           	     			 
	           		  }catch(Exception e){ }
           		 xBar[i]= x_bar[i];
           	 }
            }
            else {
            	for (int i = 0; i < getNTrials(); i++) {
	                ci_array[i][0] = x_bar[i] + (alphaValues[0] * s[i]) / Math.sqrt(getSampleSize());//upper
	                ci_array[i][1] = x_bar[i] + (alphaValues[1] * s[i]) / Math.sqrt(getSampleSize());//lower
		            xBar[i]= x_bar[i];
	            }
            }
            return ci_array;
        }

        public double[] getAlpha() {
            double[] alphaValues = new double[2];
            
            int sampleSize;
            sampleSize=getSampleSize();
            
            Distribution criticalValueDistribution = new StudentDistribution(sampleSize - 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }
        
       public  double[] getX_bar(){
       	 return(xBar);
       }
    },

    proportion_wald {
        private double p_hat[];
        private double p_s[];
        private final static double continuityCorrectionValue = 0.0;
        double[] xBar;
        
        public String toString() {
        	if(bootStrap)
        		return "Proportion - BootStrap";
        	else
        		return "Proportion - Normal Approximation";
        }

     /*   public double[][] getConfidenceIntervals() {
            double x_bar[] = getMeans();
            double s[] = getKnownStdDev();
            double[] alphaValues = this.getAlpha();
            double[][] ci_array = new double[getNTrials()][2];
            for (int i = 0; i < getNTrials(); i++) {
                ci_array[i][0] = x_bar[i] + (alphaValues[0] * s[i]) / Math.sqrt(getSampleSize());
                ci_array[i][1] = x_bar[i] + (alphaValues[1] * s[i]) / Math.sqrt(getSampleSize());
            }
            return ci_array;
        }*/
        public double[][] getConfidenceIntervals() {
            double[][] ci_array = new double[getNTrials()][2];
            xBar = new double[getNTrials()];
            getPHat();
            p_s= getProportionStdErrForAllTrials();           
            double[] alphaValues = this.getAlpha();
           
            if (leftCutOffValue != null && rightCutOffValue != null) {
            	if(bootStrap){
            		for (int i = 0; i < getNTrials(); i++) {
            			bootStrapDataForOneTrial=getBootStrapData(i);
            			double[] p_hatBootStrap = getBootStrapPHat(); //[bootStrapSize]
            			try{	  
                 			  ci_array[i] =  BootStrapSort.getBounds(p_hatBootStrap, halfAlpha[getCvIndex()]); //upper[0]. lower[1]    
                 			  ci_array[i][0] =scaleProportionData(ci_array[i][0]);
                 			  ci_array[i][1] =scaleProportionData(ci_array[i][1]);
                 			//  System.out.println(" bootStrap ci[0]="+ci_array[i][0]+" ci[1]= "+ci_array[i][1]);
                 		  }catch(Exception e){		}
	            		xBar[i]=scaleProportionData(p_hat[i]);
	            		
            		}
            	}
            	else{
            		for (int i = 0; i < getNTrials(); i++) {
            	
		                //	System.out.println("p_hat["+i+"]="+p_hat[i]);
	            		double ci_temp =p_hat[i] + (alphaValues[0] * p_s[i]);
	            		if(ci_temp>1)
	            			ci_temp =1;
	            		ci_array[i][0] = scaleProportionData(ci_temp);//upper
		                  //  System.out.print(ci_temp);
	            		ci_temp = p_hat[i] + (alphaValues[1] * p_s[i]);
	            		if (ci_temp<0)
	            			ci_temp=0;
	            		//   System.out.println(" : "+ci_temp);
	            		ci_array[i][1] = scaleProportionData(ci_temp);//lower
	            		xBar[i]=scaleProportionData(p_hat[i]);
	            		// System.out.println(" ci[0]="+ci_array[i][0]+" ci[1]= "+ci_array[i][1]);
            		}
            	}
            }
            
            return ci_array;
        }

        public double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new NormalDistribution(0, 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }

        public double[] getX_bar(){
        	return(xBar);
       }
        
      /*  private double[] getPHat(double continuityCorrectionValue) {
            p_hat = new double[getNTrials()];
            double[][] sampleData = getSampleData();
            for (int i = 0; i < getNTrials(); i++) {
                int counter = 0;
                for (int j = 0; j < getSampleSize(); j++) {
                    if (sampleData[i][j] <= rightCutOffValue && sampleData[i][j] >= leftCutOffValue)
                        counter++;
                }
                continuityCorrectionValue = Math.pow(continuityCorrectionValue, 2.0);
                p_hat[i] = (counter + .5 * continuityCorrectionValue) / ((double) getSampleSize() + continuityCorrectionValue);
            }
            return p_hat;
        }*/
      
        
        private double[] getPHat() {
            p_hat = new double[getNTrials()];
            double[][] sampleData = getSampleData();
           
            for (int i = 0; i < getNTrials(); i++) {
	                int counter = 0;
	                for (int j = 0; j < getSampleSize(); j++) {
	                    if (sampleData[i][j] <= rightCutOffValue && sampleData[i][j] >= leftCutOffValue)
	                        counter++;
	                }	              
	                p_hat[i] = (double)counter/(double)getSampleSize();
            }
            
            return p_hat;
        }

        
      /*  private double[] getProportionStdErrForAllTrials(double continuityCorrectionValue) {
            p_s = new double[getNTrials()];
            continuityCorrectionValue = Math.pow(continuityCorrectionValue, 2.0);
            for (int i = 0; i < getNTrials(); i++) {
                p_s[i] = Math.sqrt(((p_hat[i] * (1 - p_hat[i])) / (getSampleSize() + continuityCorrectionValue)));
            }
            return p_s;
        }*/
        
        private double[] getProportionStdErrForAllTrials() {
            p_s = new double[getNTrials()];    
            for (int i = 0; i < getNTrials(); i++) {
                p_s[i] = Math.sqrt(((p_hat[i] * (1 - p_hat[i])) / (getSampleSize())));
            }
            return p_s;
        }

    },


    proportion_approx {
    	double[] xBar;
        private double p_hat[];
        private double p_s[];

        public String toString() {
	       if(bootStrap)
	    	    return "";
	       else return "Proportion - Quadratic";
        }

     /*   public double[][] getConfidenceIntervals() {
            double[][] ci_array = new double[getNTrials()][2];
            xBar = new double[getNTrials()];
            double[] alphaValues = this.getAlpha();
            double continuityCorrectionValue = alphaValues[0];
            getPHat(continuityCorrectionValue);
            getProportionStdErrForAllTrials(continuityCorrectionValue);
            if (leftCutOffValue != null && rightCutOffValue != null) {
                for (int i = 0; i < getNTrials(); i++) {
                    ci_array[i][0] = scaleProportionData(p_hat[i] + (alphaValues[0] * p_s[i]));
                    ci_array[i][1] = scaleProportionData(p_hat[i] + (alphaValues[1] * p_s[i]));
                    xBar[i]=scaleProportionData(p_hat[i]);
                }
            }
            return ci_array;
        }*/
      
        
        public double[][] getConfidenceIntervals() {
            double[][] ci_array = new double[getNTrials()][2];
            xBar = new double[getNTrials()];
            double[] alphaValues = this.getAlpha();           
            getPHat();
            p_s= getProportionStdErrForAllTrials();           
            
            int sampleSize = getSampleSize();
            
            if (leftCutOffValue != null && rightCutOffValue != null) {
            	if(bootStrap){
            		for (int i = 0; i < getNTrials(); i++) {
            			bootStrapDataForOneTrial=getBootStrapData(i);
            			double[] p_hatBootStrap = getBootStrapPHat();
            			try{	  
                 			  ci_array[i] =  BootStrapSort.getBounds(p_hatBootStrap, halfAlpha[getCvIndex()]); //upper[0]. lower[1]    
                 			  ci_array[i][0] = scaleProportionData(ci_array[i][0]);
                			  ci_array[i][1] = scaleProportionData(ci_array[i][1]);
                 		  }catch(Exception e){		}
	            		xBar[i] = scaleProportionData(p_hat[i]);
	            		
            		}
            	}else{
	                for (int i = 0; i < getNTrials(); i++) {
	                	double ci_temp = (p_hat[i] + Math.pow(alphaValues[0],2)/(2*getSampleSize())+ (alphaValues[0] * p_s[i]))/(1+Math.pow(alphaValues[0],2)/(sampleSize));
	                	if(ci_temp>1)
	                		ci_temp =1;
	                    ci_array[i][0] = scaleProportionData(ci_temp);//upper
	 
	                    ci_temp = ci_temp = (p_hat[i] + Math.pow(alphaValues[1],2)/(2*getSampleSize())+ (alphaValues[1] * p_s[i]))/(1+Math.pow(alphaValues[1],2)/(sampleSize));
	                    if (ci_temp<0)
	                    	ci_temp=0;
	                    ci_array[i][1] = scaleProportionData(ci_temp);//lower
	                    xBar[i]=scaleProportionData(p_hat[i]);                  
	                }
            	}
            }
            
            return ci_array;
        }
        
        
       public double[] getX_bar(){
        	return(xBar);
       }
        
       public double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new NormalDistribution(0, 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }
    
      
       private double[] getPHat() {
            p_hat = new double[getNTrials()];
            double[][] sampleData = getSampleData();
           
     
            for (int i = 0; i < getNTrials(); i++) {
	                int counter = 0;
	                
	                for (int j = 0; j < getSampleSize(); j++) {
	                    if (sampleData[i][j] <= rightCutOffValue && sampleData[i][j] >= leftCutOffValue)
	                        counter++;
	                }
	              
	                p_hat[i] = (double)counter/(double)getSampleSize();
            }
            
            return p_hat;
        }
        
        private double[] getProportionStdErrForAllTrials() {
        	double[] alphaValues = this.getAlpha();
            p_s = new double[getNTrials()];    
            for (int i = 0; i < getNTrials(); i++) {
            		p_s[i] = Math.sqrt(((p_hat[i] * (1 - p_hat[i])) / (getSampleSize()))+ Math.pow(alphaValues[0],2)/(4*getSampleSize()*getSampleSize()));
            }
            return p_s;
        }

    },
    
    proportion_exact {
    	double[] xBar;
    	private double p_hat[];
        public String toString() {
        	if(bootStrap)
	    	    return "";
        	else 
        		return "Proportion - Exact";
        }
 
        public double[][] getConfidenceIntervals() {
            double[][] ci_array = new double[getNTrials()][2];  
            xBar = new double[getNTrials()];
            p_hat = getPHat();
            double[] right = getRightFLimitForAllTrials();
            double[] left = getLeftFLimitForAllTrials();
            double temp;
            int sampleSize = getSampleSize();
            
            if (leftCutOffValue != null && rightCutOffValue != null) {
            	if(bootStrap){
            		for (int i = 0; i < getNTrials(); i++) {
            			bootStrapDataForOneTrial=getBootStrapData(i);
            			double[] p_hatBootStrap = getBootStrapPHat();
            			try{	  
                 			  ci_array[i] =  BootStrapSort.getBounds(p_hatBootStrap, halfAlpha[getCvIndex()]); //upper[0]. lower[1]    
                 			  ci_array[i][0] = scaleProportionData(ci_array[i][0]);
                			  ci_array[i][1] = scaleProportionData(ci_array[i][1]);
                 		  }catch(Exception e){		}
	            		xBar[i]=scaleProportionData(p_hat[i]);
	            		
            		}
            	}else{
	                for (int i = 0; i < getNTrials(); i++) {
	                   
	                	temp = ((1+getX()[i])*right[i]);
	                	double ci_temp =temp/(temp+sampleSize-getX()[i]);                
	                    if(ci_temp > 1)
	                		ci_temp = 1;
	                    ci_array[i][0] = scaleProportionData(ci_temp);//upper
	                
	                    temp = (getX()[i]*left[i]);
	                    ci_temp = temp/(temp+sampleSize-getX()[i]+1);
	                    if(ci_temp < 0)
	                		ci_temp = 0;
	                    ci_array[i][1] = scaleProportionData(ci_temp);//lower
	
	                    xBar[i] = scaleProportionData(p_hat[i]);
	                }
            	}
            }
            return ci_array;
        }
 
        
        public double[] getX_bar(){
         	return(xBar);
        }
         
        private double[] getPHat() {
            p_hat = new double[getNTrials()];
            double[][] sampleData = getSampleData();
            
	            for (int i = 0; i < getNTrials(); i++) {
	                int counter = 0;
	                for (int j = 0; j < getSampleSize(); j++) {
	                    if (sampleData[i][j] <= rightCutOffValue && sampleData[i][j] >= leftCutOffValue)
	                        counter++;
	                }
	              
	                p_hat[i] = (double)counter/(double)getSampleSize();
	            }
            
            return p_hat;
        }
        
          
        public double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new NormalDistribution(0, 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }
 
        private int[] getX() {
            int [] x = new int[getNTrials()];
            double[][] sampleData = getSampleData();
            
            	for (int i = 0; i < getNTrials(); i++) {
	            
	                int counter = 0;
	                for (int j = 0; j < getSampleSize(); j++) {
	                    if (sampleData[i][j] <= rightCutOffValue && sampleData[i][j] >= leftCutOffValue)
	                        counter++;
	                }
	                x[i] = counter;
	            }
        	
            return x;
        }
 
        private double[] getLeftFLimitForAllTrials() {
            Distribution criticalValueDistribution ;
            double [] left = new double[getNTrials()];
            int sampleSize = getSampleSize();
            
            for (int i = 0; i < getNTrials(); i++) {
            	criticalValueDistribution = new FisherDistribution(2*getX()[i], 2*(sampleSize-getX()[i]+1));
            	left[i] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            }
            return left;
        }
        private double[] getRightFLimitForAllTrials() {
            Distribution criticalValueDistribution ;
            double [] right = new double[getNTrials()];
            int sampleSize = getSampleSize();
            
            for (int i = 0; i < getNTrials(); i++) {
                criticalValueDistribution = new FisherDistribution(2*getX()[i]+1, 2*(sampleSize-getX()[i]));
            	right[i] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            }
            return right;
        }
    },
    
    sigma {
    	double[] xBar;
        public String toString() {
        	if(bootStrap)
	    	    return "Variance - BootStrap";
        	else 
        		return "Variance - Chi Square Distribution";
        }

        public double[][] getConfidenceIntervals() {
            double[][] ci_array = new double[getNTrials()][2];
            xBar = new double[getNTrials()];
            double[] alphaValues = this.getAlpha();
            double s[] = getStdDev();
            int sampleSize = getSampleSize();
            
            if(bootStrap){
            	for (int i = 0; i < getNTrials(); i++) {
	            	bootStrapDataForOneTrial=getBootStrapData(i);
	    			double[] varBootStrap = getBootStrapVariance();
	    			try{	  
	         			  ci_array[i] =  BootStrapSort.getBounds(varBootStrap, halfAlpha[getCvIndex()]);
	    			}catch(Exception e){}
	    			xBar[i]=s[i]*s[i];
            	}
            }
            else{
            	for (int i = 0; i < getNTrials(); i++) {
         
                ci_array[i][0] = (sampleSize - 1) * Math.pow(s[i], 2) / alphaValues[1];//upper
                ci_array[i][1] = (sampleSize - 1) * Math.pow(s[i], 2) / alphaValues[0];//lower
               // xBar[i]= (ci_array[i][0]+ci_array[i][1])/2;
                xBar[i]=s[i]*s[i];
            	}
            }
            return ci_array;
        }

        public double[] getX_bar(){       	
        	return(xBar);
       }
        
        public double[] getAlpha() {
            double[] alphaValues = new double[2];
            int sampleSize = getSampleSize();
            
            Distribution criticalValueDistribution = new ChiSquareDistribution(sampleSize - 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }

    },
    
    sigma2 {
    	double[] xBar;
    	private double p_hat[];
        public String toString() {
        	if(bootStrap)
	    	    return "";
        	else
        	return "Variance - Asymptotically Distribution Free";
        }

        public double[][] getConfidenceIntervals() {
            double[][] ci_array = new double[getNTrials()][2];
            xBar = new double[getNTrials()];
            double[] alphaValues = this.getAlpha();
          
            double s[] = getStdDev();
            double m4[] = getM4();
            
            int sampleSize = getSampleSize();
            if(bootStrap){
	            for (int i = 0; i < getNTrials(); i++) {
	            	bootStrapDataForOneTrial=getBootStrapData(i);
	    			double[] varBootStrap = getBootStrapVariance();
	    			try{	  
	         			  ci_array[i] =  BootStrapSort.getBounds(varBootStrap, halfAlpha[getCvIndex()]);
	    			}catch(Exception e){}
	    			xBar[i]=s[i]*s[i];
	        	}
            }else{
	            for (int i = 0; i < getNTrials(); i++) {
	            	
	            	//s[i]= s[i]*Math.sqrt((double)(n-1)/(double)n);
	            	
	            //	System.out.println("s[i]=+"+ s[i]+ " m4[i]="+m4[i]);
	                ci_array[i][1] = s[i]*s[i]+alphaValues[1]*Math.sqrt(m4[i]-Math.pow(s[i],4))/Math.sqrt(sampleSize);//lower
	                ci_array[i][0] = s[i]*s[i]+alphaValues[0]*Math.sqrt(m4[i]-Math.pow(s[i],4))/Math.sqrt(sampleSize);//upper
	          //     System.out.println("ci_array["+i+"][1]="+ci_array[i][1]);
	          //     System.out.println("ci_array["+i+"][0]="+ci_array[i][0]);
	                xBar[i]=s[i]*s[i]; 
	               // System.out.println("xBar[i]=+"+ xBar[i]);
	            }
            }
            return ci_array;
        }

        public double[] getX_bar(){       	
        	return(xBar);
       }
        
        public double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new NormalDistribution(0, 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }
        
        double[] getM4(){
        	double m4[] = new double[getNTrials()];
        	double[] temp = new double[getNTrials()];
        	double[] x_bar =  getMeans();
        	double[][] sampleData = getSampleData();
        	
	        	for (int i = 0; i < getNTrials(); i++) {
	        		 temp[i]=0;
	        		 for (int j = 0; j < getSampleSize(); j++) {
	        			 temp[i]+=Math.pow((sampleData[i][j]-x_bar[i]),4);
	        		 }
	        		 m4[i]=temp[i]/(getSampleSize());
	        	 }        	 	        	
        	
        	return m4;
        }
    },
    
   asy_MLE{
    	double[] xBar;
    	
        public String toString() {
        	if(bootStrap)
	    	    return "";
        	else
        		return "Asymptotic Properties of MLE";
        }
        
        public double[][] getConfidenceIntervals() {
            double[][] ci_array = new double[getNTrials()][2];
            xBar = new double[getNTrials()];
            double[] alphaValues = this.getAlpha();
            double s[] = getStdDev();
            double x_bar[] = getMeans();
            int sampleSize = getSampleSize();
            
            Distribution dist = getDistribution();
            for (int i = 0; i < getNTrials(); i++) {            
                // Exp
            	if(dist.getName().toLowerCase().indexOf("exponential")!=-1){
            	//	System.out.println("exp");
            		ci_array[i][0] =  1/x_bar[i]+alphaValues[0]*Math.sqrt(1/(sampleSize*x_bar[i]*x_bar[i])); //upper
            		ci_array[i][1] =  1/x_bar[i]+alphaValues[1]*Math.sqrt(1/(sampleSize*x_bar[i]*x_bar[i])); //lower
            		xBar[i]= 1/x_bar[i];
            	}
                //Poss
            	else if(dist.getName().toLowerCase().indexOf("poisson")!=-1){
            		//System.out.println("poisson");
            		ci_array[i][0] =  x_bar[i]+alphaValues[0]*Math.sqrt(x_bar[i]/sampleSize); //upper
            		ci_array[i][1] =  x_bar[i]+alphaValues[1]*Math.sqrt(x_bar[i]/sampleSize); //lower
            		xBar[i]= x_bar[i];
            	}
               
            }
            return ci_array;
        }
        
        public double[] getX_bar(){
        	return(xBar);
       }
        
        public double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new NormalDistribution(0, 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }
        
    }; 

    private Distribution distribution;
    private int nTrials;
    private int sampleSize;
    private int cvIndex;
    private double[][] sampleData;
    double[] halfAlpha = ConfidenceControlPanelGeneral.HALF_ALPHA;
    Double leftCutOffValue, rightCutOffValue;
    //Double meanValue, SDValue;
    private static boolean bootStrap = false;
    private BootStrap bs;
    private int bootStrapSize=BootStrap.defaultSampleSize;
    private static double[][] bootStrapDataForOneTrial;//[bootstrapsize][samplesize]
	//HistogramUtil histUtil;
	
    public void setBootStrap(boolean flag){
    	bootStrap = flag;
    }
    
    public void updateIntervalType(Distribution distribution, int nTrials, int sampleSize, int cvIndex) {

        this.distribution = distribution;
        this.nTrials = nTrials;
        this.sampleSize = sampleSize;
        this.cvIndex = cvIndex;
        this.sampleData = new double[nTrials][sampleSize];
        sampleData();
        //System.out.println("updateIntervalType called sampleData called, bootStrap="+bootStrap);
    }
     

    public final void sampleData() {
        for (int i = 0; i < nTrials; i++) {
            for (int j = 0; j < sampleSize; j++) {
                sampleData[i][j] = distribution.simulate();
            }
        }
        
        if(bootStrap){
        //	System.out.println(" sampleData get called: bootStrap=true");
        	bs= new BootStrap();
        	bs.setBootStrapSize(bootStrapSize);
        //	bootStrapDataForOneTrial = new double[bootStrapSize][sampleSize];
        }
    }
    
    public double[][] getBootStrapData(int trialIndex){ 
    	if (bootStrap){
    		double[][] bootStrapData = bs.getBootStrapData(sampleData[trialIndex],sampleSize);
    		return bootStrapData;
    	}
    	else return null;
    
    }
    
  //  protected void bootStrap2Histogram(double[][] data){   
  //  	histUtil.histogram(data, bootStrapSize, sampleSize);
 //   }

    public final double[] getMeans() {
        double[] x_bar = new double[nTrials];
        for (int i = 0; i < nTrials; i++) {
        	x_bar[i] = distribution.getMean(sampleData[i]);
        }
        return x_bar;
    }
    public double[] getBootStrapMeans() {
    	double[]	xBarBootStrap= new double[bootStrapSize];
    
    	for(int j=0; j<bootStrapSize; j++)
		  xBarBootStrap[j] = distribution.getMean(bootStrapDataForOneTrial[j]);
    	
    	return xBarBootStrap;
    }
    
    public final double[] getStdDev() {
        double[] s = new double[nTrials];
        for (int i = 0; i < nTrials; i++) {
        	s[i] = Math.sqrt(distribution.getVariance(sampleData[i]));
        }
        return s;
    }
    
    public final double[] getBootStrapVariance() {
    	double[]	varBootStrap= new double[bootStrapSize];
        
    	for(int j=0; j<bootStrapSize; j++)
		  varBootStrap[j] = distribution.getVariance(bootStrapDataForOneTrial[j]);
    	
    	return varBootStrap;
    }
    public final double[] getKnownStdDev() {
        double[] s = new double[nTrials];
     
        double sd= distribution.getSD();
        for (int i = 0; i < nTrials; i++) {
            s[i] = sd;
        }
        return s;
    }
    
    public final double scaleProportionData(double ciValue) {
        double min,max;
       
        min = ConfidenceCanvasGeneral.getMinValueFrom2DArray(getSampleData());
        max = ConfidenceCanvasGeneral.getMaxValueFrom2DArray(getSampleData());
        double range = Math.abs(max - min);
        double scaledValue = ciValue * range;
        return min + scaledValue;
    }


    public int getNTrials() {
        return nTrials;
    }

    public int getSampleSize() {
        return sampleSize;
    }
    
    public int getBootStrapSize() {   	
    	return bootStrapSize;  	
    }
    
    public String getBootStrapSizeString() {
    	if(bootStrap)
    		return "BootStrapSize = "+Integer.toString(bootStrapSize);
    	else return "";
    }
    public void setBootStrapSize(int b) {
       bootStrapSize = b;
       if(bs!=null)
    	   bs.setBootStrapSize(b);
       else bs = new BootStrap(b);
       bootStrapDataForOneTrial = new double[b][sampleSize];
    }
    public double[][] getSampleData() {
        return sampleData;
    }

    Distribution getDistribution(){
    	return this.distribution;
    }
    
    public void setCutOffValue(Double left, Double right) {
        this.leftCutOffValue = left;
        this.rightCutOffValue = right;
    }
  /*  public void setCIMeanValue(Double value) {
        this.meanValue = value;
    }
    public void setCISDValue(Double value) {
        this.SDValue = value;
    }*/

    public Double getLeftCutOffValue() {
        return leftCutOffValue;
    }

    public Double getRightCutOffValue() {
        return rightCutOffValue;
    }
 
    /*public Double getCIMeanValue() {
        return meanValue;
    }
    public Double getCISDValue() {
        return SDValue;
    }*/

    public int getCvIndex() {
        return cvIndex;
    }

    public double[] getBootStrapPHat() {
    	double[] p_hatBootStrap = new double[bootStrapSize];
    	for (int i = 0; i < bootStrapSize; i++) {
    		 int counter = 0;
                for (int j = 0; j < getSampleSize(); j++) {
                	if (bootStrapDataForOneTrial[i][j] <= rightCutOffValue && bootStrapDataForOneTrial[i][j] >= leftCutOffValue)
                		counter++;
                }	              
                p_hatBootStrap[i] = (double)counter/(double)getSampleSize(); 
    	 }
    	 return p_hatBootStrap;
    }
    
   
    public double[] BootStrapData2SigleArray(){
    	double[] data = new double[bootStrapSize*sampleSize];
		 int k =0;
		 for (int i=0; i<bootStrapSize; i++)
   		 for (int j=0; j<sampleSize; j++){
   			 data[k]=bootStrapDataForOneTrial[i][j];
   			// if (k%100==0)
   			//	System.out.println("data["+k+"]"+ data[k]);
   			 k++;
   		 }
   		  return data;
    }
    
    public abstract String toString();
    public abstract double[][] getConfidenceIntervals();
    abstract double[]  getAlpha();
    public abstract double[] getX_bar();

}
