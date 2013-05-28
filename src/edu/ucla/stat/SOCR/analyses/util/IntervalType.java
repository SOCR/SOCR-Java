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

package edu.ucla.stat.SOCR.analyses.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ucla.stat.SOCR.analyses.exception.DataIsEmptyException;
import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.distributions.ChiSquareDistribution;
import edu.ucla.stat.SOCR.distributions.FisherDistribution;
import edu.ucla.stat.SOCR.distributions.NormalDistribution;
import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import edu.ucla.stat.SOCR.experiments.util.BootStrap;
import edu.ucla.stat.SOCR.experiments.util.BootStrapSort;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

public enum IntervalType {

    xbar_sigmaKnown {
    	double xBar;
        public String toString() {
        	if(bootStrap)
        		return "Mean - BootStrap";
        	else
            return "Mean - Population Variance Known";
        }

        public double[] getConfidenceIntervals() {
            double x_bar = getMean();
            double s = getKnownStdDev();
           // System.out.println("knownSD ="+s);
       
            double[] alphaValues = this.getAlpha();
            double[] ci_array = new double[2];
            if(bootStrap){
            	//System.out.println("xbar_sigmaKnown bootStrap=true"+"1-halfAlpha="+(1-halfAlpha[getCvIndex()]));
            		  bootStrapData=getBootStrapData();	 
            		  // get xbar for all bootSrapSample
            		  double[] xBarBootStrap = getBootStrapMeans();
            		  // System.out.println("bootStrapBar size"+xBarBootStrap.length);
            		  try{            			  
            			  ci_array=  BootStrapSort.getBounds(xBarBootStrap, halfAlpha[getCvIndex()]); //upper[0]. lower[1]            			 
            		  }catch(Exception e){}
            		  xBar= x_bar;
            	
            }else{
	            ci_array[0] = x_bar + (alphaValues[0] * s) / Math.sqrt(getSampleSize()); //upper
	            ci_array[1] = x_bar + (alphaValues[1] * s) / Math.sqrt(getSampleSize()); //lower
	            xBar= x_bar;
            }
            
            return ci_array;
        }

        double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new NormalDistribution(0, 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }
        
        public double getX_bar(){
        	 return(xBar);
        }
    },


    xbar_sigmaUnknown {
    	double xBar;
    	
        public String toString() {
        	if(bootStrap)
        		return "";
        	else
        		return "Mean - Population Variance Unknown";
        }

        public double[] getConfidenceIntervals() {
            double x_bar = getMean();
            double s = getStdDev();
        
            double[] alphaValues = this.getAlpha();
            double[] ci_array = new double[2];
           
            if(bootStrap){
            	bootStrapData=getBootStrapData();	 
            	//get xbar for all 1K bootSrapSample
            	double[] xBarBootStrap = getBootStrapMeans();
            	//  System.out.println("bootStrapBar size"+xBarBootStrap.length);
            	try{	  
            		ci_array =  BootStrapSort.getBounds(xBarBootStrap, halfAlpha[getCvIndex()]); //upper[0]. lower[1]           	     			 
            	}catch(Exception e){ }
            	xBar= x_bar;
           	 
            }else{
                ci_array[0] = x_bar + (alphaValues[0] * s) / Math.sqrt(getSampleSize());//upper
                ci_array[1] = x_bar + (alphaValues[1] * s) / Math.sqrt(getSampleSize());//lower
                xBar= x_bar;
            }
            return ci_array;
        }

        double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new StudentDistribution(getSampleSize() - 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }
        
       public  double getX_bar(){
       	 return(xBar);
       }
    },

    proportion_wald {
        private double p_hat;
        private double p_s;
        private final static double continuityCorrectionValue = 0.0;
        double xBar;
        
        public String toString() {
        	if(bootStrap)
        		return "Proportion - BootStrap";
        	else
        		return "Proportion - Normal Approximation";
        }

        public double[] getConfidenceIntervals() {
            double[] ci_array = new double[2];
           
            p_hat = getPHat();
            p_s = getProportionStdErrForAllTrials();           
            double[] alphaValues = this.getAlpha();
            
           // if (leftCutOffValue != null && rightCutOffValue != null) {
            if(bootStrap){
        		
        			bootStrapData=getBootStrapData();
        			double[] p_hatBootStrap = getBootStrapPHat(); //[bootStrapSize]
        			try{	  
             			  ci_array =  BootStrapSort.getBounds(p_hatBootStrap, halfAlpha[getCvIndex()]); //upper[0]. lower[1]    
             			  ci_array[0] =scaleProportionData(ci_array[0]);
             			  ci_array[1] =scaleProportionData(ci_array[1]);
             			//  System.out.println(" bootStrap ci[0]="+ci_array[i][0]+" ci[1]= "+ci_array[i][1]);
             		  }catch(Exception e){		}
            		xBar=scaleProportionData(p_hat);
            		
        		
        	}else{
                //	System.out.println("p_hat["+i+"]="+p_hat[i]);
                	double ci_temp =p_hat + (alphaValues[0] * p_s);
                	if(ci_temp>1)
                		ci_temp =1;
                    ci_array[0] = scaleProportionData(ci_temp);//upper
                  //  System.out.print(ci_temp);
                    ci_temp = p_hat + (alphaValues[1] * p_s);
                    if (ci_temp<0)
                    	ci_temp=0;
                 //   System.out.println(" : "+ci_temp);
                    ci_array[1] = scaleProportionData(ci_temp);//lower
                    xBar=scaleProportionData(p_hat);
                   
        	}
         //  }
            return ci_array;
        }

        double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new NormalDistribution(0, 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }

        public double getX_bar(){
        	return(xBar);
       }
 
        private double getPHat() {
            double[] sampleData = getSampleData();
           
            int counter = 0;
            for (int j = 0; j < getSampleSize(); j++) {
            	if (sampleData[j] <= rightCutOffValue && sampleData[j] >= leftCutOffValue)
            		counter++;
            }
              
            double p_hat = (double)counter/(double)getSampleSize();
            
            return p_hat;
        }

        
        private double getProportionStdErrForAllTrials() {
       
        	double p_s = Math.sqrt(((p_hat * (1 - p_hat)) / (getSampleSize())));
            
            return p_s;
        }

    },


    proportion_approx {
    	double xBar;
        private double p_hat;
        private double p_s;

        public String toString() {
        	if(bootStrap)
	    	    return "";
	       else 
            return "Proportion - Quadratic";
        }     
        
        public double[] getConfidenceIntervals() {
            double[] ci_array = new double[2];
           
            double[] alphaValues = this.getAlpha();           
            p_hat = getPHat();
            p_s= getProportionStdErrForAllTrials();           
            
            if (leftCutOffValue != null && rightCutOffValue != null) {
            	if(bootStrap){
            		bootStrapData=getBootStrapData();
            		double[] p_hatBootStrap = getBootStrapPHat();
            		try{	  
            			ci_array =  BootStrapSort.getBounds(p_hatBootStrap, halfAlpha[getCvIndex()]); //upper[0]. lower[1]    
            			ci_array[0] = scaleProportionData(ci_array[0]);
            			ci_array[1] = scaleProportionData(ci_array[1]);
            		}catch(Exception e){		}
            		xBar = scaleProportionData(p_hat);		
            	}else{
	            	double ci_temp = (p_hat + Math.pow(alphaValues[0],2)/(2*getSampleSize())+ (alphaValues[0] * p_s))/(1+Math.pow(alphaValues[0],2)/(getSampleSize()));
	            	if(ci_temp>1)
	            		ci_temp =1;
	            	ci_array[0] = scaleProportionData(ci_temp);//upper
	 
	            	ci_temp = ci_temp = (p_hat + Math.pow(alphaValues[1],2)/(2*getSampleSize())+ (alphaValues[1] * p_s))/(1+Math.pow(alphaValues[1],2)/(getSampleSize()));
	            	if (ci_temp<0)
	            		ci_temp=0;
	            	ci_array[1] = scaleProportionData(ci_temp);//lower
	            	xBar=scaleProportionData(p_hat);   
            	}
            }
            
            return ci_array;
        }
        
        
       public double getX_bar(){
        	return(xBar);
       }
        
        double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new NormalDistribution(0, 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }

        private double getPHat() {
            double p_hat;
            double[] sampleData = getSampleData();
           
            int counter = 0;
            for (int j = 0; j < getSampleSize(); j++) {
            	if (sampleData[j] <= rightCutOffValue && sampleData[j] >= leftCutOffValue)
            		counter++;
            }
              
            p_hat = (double)counter/(double)getSampleSize();
            
            return p_hat;
        }
        
        private double getProportionStdErrForAllTrials() {
        	double[] alphaValues = this.getAlpha();
            double p_s;   
        
            p_s = Math.sqrt(((p_hat * (1 - p_hat)) / (getSampleSize()))+ Math.pow(alphaValues[0],2)/(4*getSampleSize()*getSampleSize()));
            
            return p_s;
        }

    },
    
    proportion_exact {
    	double xBar;
    	private double p_hat;
        public String toString() {
        	if(bootStrap)
	    	    return "";
        	else 
        		return "Proportion - Exact";
        }
 
        public double[] getConfidenceIntervals() {
            double[] ci_array = new double[2];  
           
            p_hat = getPHat();
            double right = getRightFLimit();
            double left = getLeftFLimit();
            double temp;
            if (leftCutOffValue != null && rightCutOffValue != null) {
             
            	if(bootStrap){
            	
            		bootStrapData=getBootStrapData();
            		double[] p_hatBootStrap = getBootStrapPHat();
            		try{	  
            			ci_array =  BootStrapSort.getBounds(p_hatBootStrap, halfAlpha[getCvIndex()]); //upper[0]. lower[1]    
            			ci_array[0] = scaleProportionData(ci_array[0]);
            			ci_array[1] = scaleProportionData(ci_array[1]);
            		}catch(Exception e){		}
            		xBar=scaleProportionData(p_hat);
    	
            	}else{  
                	temp = ((1+getX())*right);
                	double ci_temp =temp/(temp+getSampleSize()-getX());                
                    if(ci_temp > 1)
                		ci_temp = 1;
                    ci_array[0] = scaleProportionData(ci_temp);//upper
                
                    temp = (getX()*left);
                    ci_temp = temp/(temp+getSampleSize()-getX()+1);
                    if(ci_temp < 0)
                		ci_temp = 0;
                    ci_array[1] = scaleProportionData(ci_temp);//lower

                    xBar = scaleProportionData(p_hat);
            	}
                
            }
            return ci_array;
        }
 
        
        public double getX_bar(){
         	return(xBar);
        }
         
        private double getPHat() {
            double p_hat ;
            double[] sampleData = getSampleData();
           
                int counter = 0;
                for (int j = 0; j < getSampleSize(); j++) {
                    if (sampleData[j] <= rightCutOffValue && sampleData[j] >= leftCutOffValue)
                        counter++;
                }
              
                p_hat = (double)counter/(double)getSampleSize();
            
            return p_hat;
        }
        
          
        double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new NormalDistribution(0, 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }
 
        private int getX() {
        	int x;
            double[] sampleData = getSampleData();
           
            int counter = 0;
            for (int j = 0; j < getSampleSize(); j++) {
            	if (sampleData[j] <= rightCutOffValue && sampleData[j] >= leftCutOffValue)
            		counter++;
            }
            x = counter;
            
            return x;
        }
 
        private double getLeftFLimit() {
            Distribution criticalValueDistribution ;
            double left;
          
            criticalValueDistribution = new FisherDistribution(2*getX(), 2*(getSampleSize()-getX()+1));
            left = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            
            return left;
        }
        
        private double getRightFLimit() {
            Distribution criticalValueDistribution ;
            double right;
          
            criticalValueDistribution = new FisherDistribution(2*getX()+1, 2*(getSampleSize()-getX()));
            right = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
           
            return right;
        }
    },
    
    sigma {
    	double xBar;
    	//private double p_hat;
        public String toString() {
        	if(bootStrap)
	    	    return "Variance - BootStrap";
        	else 
        		return "Variance - Chi Square Distribution";
        }

        public double[] getConfidenceIntervals() {
            double[] ci_array = new double[2];
            double[] alphaValues = this.getAlpha();
            double s = getStdDev();
           
            
            if(bootStrap){
            	
	            	bootStrapData=getBootStrapData();
	    			double[] varBootStrap = getBootStrapVariance();
	    			try{	  
	         			  ci_array =  BootStrapSort.getBounds(varBootStrap, halfAlpha[getCvIndex()]);
	    			}catch(Exception e){}
	    			xBar=s*s;
            	
            }
            else{
            	ci_array[0] = (getSampleSize() - 1) * Math.pow(s, 2) / alphaValues[1];//upper
            	ci_array[1] = (getSampleSize() - 1) * Math.pow(s, 2) / alphaValues[0];//lower
            	// xBar[i]= (ci_array[i][0]+ci_array[i][1])/2;
            	xBar=s*s;
            }
            
            return ci_array;
        }

        public double getX_bar(){       	
        	return(xBar);
       }
        
        double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new ChiSquareDistribution(getSampleSize() - 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }

    },
    
    sigma2 {
    	double xBar;
    	private double p_hat;
        public String toString() {
        	if(bootStrap)
	    	    return "";
        	else
        		return "Variance - Asymptotically Distribution Free";
        }

        public double[] getConfidenceIntervals() {
            double[] ci_array = new double[2];

            double[] alphaValues = this.getAlpha();
          
            double s = getStdDev();
            double m4 = getM4();
            
            if(bootStrap){
	          
	            	bootStrapData=getBootStrapData();
	    			double[] varBootStrap = getBootStrapVariance();
	    			try{	  
	         			  ci_array =  BootStrapSort.getBounds(varBootStrap, halfAlpha[getCvIndex()]);
	    			}catch(Exception e){}
	    			xBar=s*s;
	        	
            }else{
           
            	//s[i]= s[i]*Math.sqrt((double)(n-1)/(double)n);
            	
            //	System.out.println("s[i]=+"+ s[i]+ " m4[i]="+m4[i]);
                ci_array[1] = s*s+alphaValues[1]*Math.sqrt(m4-Math.pow(s,4))/Math.sqrt(getSampleSize());//lower
                ci_array[0] = s*s+alphaValues[0]*Math.sqrt(m4-Math.pow(s,4))/Math.sqrt(getSampleSize());//upper
          //     System.out.println("ci_array["+i+"][1]="+ci_array[i][1]);
          //     System.out.println("ci_array["+i+"][0]="+ci_array[i][0]);
                xBar=s*s; 
               // System.out.println("xBar[i]=+"+ xBar[i]);
            }
            return ci_array;
        }

        public double getX_bar(){       	
        	return(xBar);
       }
        
        double[] getAlpha() {
            double[] alphaValues = new double[2];
            Distribution criticalValueDistribution = new NormalDistribution(0, 1);
            alphaValues[0] = criticalValueDistribution.inverseCDF(1 - halfAlpha[getCvIndex()]);
            alphaValues[1] = criticalValueDistribution.inverseCDF(halfAlpha[getCvIndex()]);
            return alphaValues;
        }
        
        double getM4(){
        	double m4;
        	double temp;
        	double x_bar =  getMean();
        	double[] sampleData = getSampleData();
        	
        	temp=0;
        	for (int j = 0; j < getSampleSize(); j++) {
        		temp+=Math.pow((sampleData[j]-x_bar),4);
        	}
        	m4=temp/(getSampleSize());
        	       	 
        	return m4;
        }
        
    };
    


    private int sampleSize;
    private int cvIndex;
    private double[] sampleData;
    double[] halfAlpha = ConfidenceControlPanel.HALF_ALPHA;
    Double leftCutOffValue, rightCutOffValue;
    double knownVariance;
    //Double meanValue, SDValue;
    private static boolean bootStrap = false;
    private BootStrap bs;
    private int bootStrapSize;
    private static double[][] bootStrapData;
    
    public void setBootStrap(boolean flag){
    	bootStrap = flag;
    	if(bootStrap){
    		bs= new BootStrap();
    		bs.setBootStrapSize(bootStrapSize);
        	bootStrapData = new double[bootStrapSize][sampleSize];
    	}
    }
    
    public void updateIntervalType(int sampleSize, int cvIndex, double[] samples) {

        this.sampleSize = sampleSize;
        this.cvIndex = cvIndex;
        this.sampleData = new double[sampleSize];
        setSampleData(samples);
    }
     
    IntervalType() {
     }

  
    public final void setSampleData(double input[]) {
      
    	for (int j = 0; j < getSampleSize(); j++) {
    		sampleData[j] =input[j];
            
        }
    }

    public final void setSampleData(String input[]) {
     
    	for (int j = 0; j < getSampleSize(); j++) {
    		sampleData[j] =Double.parseDouble(input[j]);
    	}
        
    }
    
    public double[][] getBootStrapData(){ 
 
    	if (bootStrap){
    		double[][] bootStrapData = bs.getBootStrapData(sampleData,sampleSize);
    		return bootStrapData;
    	}
    	else return null;
    
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
       bootStrapData = new double[b][sampleSize];
    }
    
    public double[] getBootStrapPHat() {
    	double[] p_hatBootStrap = new double[bootStrapSize];
    	for (int i = 0; i < bootStrapSize; i++) {
    		 int counter = 0;
                for (int j = 0; j < getSampleSize(); j++) {
                	if (bootStrapData[i][j] <= rightCutOffValue && bootStrapData[i][j] >= leftCutOffValue)
                		counter++;
                }	              
                p_hatBootStrap[i] = (double)counter/(double)getSampleSize(); 
    	 }
    	 return p_hatBootStrap;
    }
    
    
    public final double getMean() {
        double x_bar = 0;
        
        try{
        	x_bar = AnalysisUtility.mean(sampleData);
        }catch(DataIsEmptyException e){
        	
        }
  
        return x_bar;
    }
    
    public double[] getBootStrapMeans() {
    	double[]	xBarBootStrap = new double[bootStrapSize];
    
    	
    		for (int i=0; i<bootStrapSize; i++)
    			try{
    				xBarBootStrap[i] = AnalysisUtility.mean(bootStrapData[i]);
    			} catch(DataIsEmptyException e){
    	        	
    	        }
    	
    	return xBarBootStrap;
    }

    public final double getStdDev() {
        double s=0;
       
        try{
        	s = AnalysisUtility.sampleVariance(sampleData);
        }catch(DataIsEmptyException e){
        	
        }
        //  s[i] = Math.sqrt(distribution.getVariance(sampleData[i]));
        	
        return Math.sqrt(s);
    }
    
    public final double[] getBootStrapVariance() {
    	double[]	varBootStrap= new double[bootStrapSize];
        
    	for(int j=0; j<bootStrapSize; j++)
    		try{
    			varBootStrap[j] = AnalysisUtility.sampleVariance(bootStrapData[j]);
    		}catch(DataIsEmptyException e){
            	
            }
    	
    	return varBootStrap;
    }
    

    public final double getKnownStdDev() {
    	
        return Math.sqrt(knownVariance);
    }
    
    protected static List convertTo1DArray(double[][] twoDArray) {
        List oneDArrayList = new ArrayList();
        for (int i = 0; i < twoDArray.length; i++) {
            for (int j = 0; j < twoDArray[i].length; j++) {
                oneDArrayList.add(new Double(twoDArray[i][j]));
            }
        }
        return oneDArrayList;
    }
    
    protected static List convertTo1DArray(double[] oneDArray) {
        List oneDArrayList = new ArrayList();
        for (int i = 0; i < oneDArray.length; i++) {   
                oneDArrayList.add(new Double(oneDArray[i]));        
        }
        return oneDArrayList;
    }
    
    public static double getMaxValueFrom2DArray(double[][] dataArray) {
        Double maximum = (Double) Collections.max(convertTo1DArray(dataArray));
        return maximum;
    }

    public static double getMinValueFrom2DArray(double[][] dataArray) {
        Double minimum = (Double) Collections.min(convertTo1DArray(dataArray));
        return minimum;
    }
    public static double getMaxValueFrom1DArray(double[] dataArray) {
        Double maximum = (Double) Collections.max(convertTo1DArray(dataArray));
        return maximum;
    }

    public static double getMinValueFrom1DArray(double[] dataArray) {
        Double minimum = (Double) Collections.min(convertTo1DArray(dataArray));
        return minimum;
    }
    public final double scaleProportionData(double ciValue) {
        double min = getMinValueFrom1DArray(getSampleData());
        double max = getMaxValueFrom1DArray(getSampleData());
        double range = Math.abs(max - min);
        double scaledValue = ciValue * range;
        return min + scaledValue;
    }


    public int getSampleSize() {
        return sampleSize;
    }

    public double[] getSampleData() {
        return sampleData;
    }
    
   /* 
    public void setCIMeanValue(Double value) {
        this.meanValue = value;
    }
    public void setCISDValue(Double value) {
        this.SDValue = value;
    }*/

    public void setKnownVarianceValue(Double v) {
    	if (v>0)
    		knownVariance = v;
    	else {
    		knownVariance = 1;
    		
    	}
    }
    
    public double getKnownVarianceValue() {
    	System.out.println("IntervalTyle knownVariance="+knownVariance);
    	return knownVariance ;
    }
    
	public void setCutOffValue(Double left, Double right) {
        this.leftCutOffValue = left;
        this.rightCutOffValue = right;
    }
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

    public double[] BootStrapData2SigleArray(){
    	double[] data = new double[bootStrapSize*sampleSize];
		 int k =0;
		 for (int i=0; i<bootStrapSize; i++)
   		 for (int j=0; j<sampleSize; j++){
   			 data[k]=bootStrapData[i][j];
   			// if (k%100==0)
   			//	System.out.println("data["+k+"]"+ data[k]);
   			 k++;
   		 }
   		  return data;
    }
    
    public abstract String toString();
    public abstract double[] getConfidenceIntervals();
    abstract double[] getAlpha();
    public abstract double getX_bar();

}
