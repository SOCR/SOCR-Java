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

public class HistogramUtil{
	 double default_bin=1;
     double bin_size;
     double max_x, min_x;
     int bin_count;
   //  int min_bin=1, max_bin;
     int[] y_freq;
     double[] x_start, x_end;
     
     public void histogram(double[][] raw_xvalue, int count1, int count2){
    	 double[] data= new double[count1*count2];
    	 
    	 System.out.println(count1 +" *"+ count2+"="+count1*count2);
    	 int k=0;
    	 for (int i=0; i<count1; i++)
    		 for (int j=0; j<count2; j++){
    			 data[k]=raw_xvalue[i][j];
    			// if (k%100==0)
    			//	System.out.println("data["+k+"]"+ data[k]);
    			 k++;
    		 }
    	 
    	 histogram(data, count1*count2);  		 
     }

     public void histogram(double[] raw_xvalue, int data_count){
    	
    	    
    		//	System.out.println("......");
    		
    	     default_bin = (int)Math.sqrt(data_count);  // for range >1
    			
    	     max_x=0;
    	     min_x=0;
    	     for (int i=0; i<data_count; i++){
    	    	 if (raw_xvalue[i]>max_x)
    	    		 max_x= raw_xvalue[i];
    	    	 if (raw_xvalue[i]<min_x)
    	    		 min_x= raw_xvalue[i];
    	     }
    	   //  System.out.println("max_x="+max_x);
    	  //   System.out.println("min_x="+min_x);
    	     double range = max_x - min_x;
    	  //   System.out.println("range="+range+ " "+min_x+"->"+max_x);
    		
    	     if (range < 1){	
    	    	 // min_bin = 0;
    	    	 // max_bin = 1;
    			
    	    	 int t= 10;
    	    	 for (int i=1; i<5; i++){
    	    		 
    	    		 if (range>(double)1/t){
    	    			 default_bin = (double)1/t;
    	    			 bin_size = (double)1/t;
    	    			 break;
    	    		 }
    	    		 else t*=10;
    	    	 }	
    			} else {
    				default_bin =(int) (range)/Math.sqrt(data_count);  // for range >1
    				//max_bin = (int)(Math.round(range)+1.0);
    			//	min_bin = 1;
    				bin_size = default_bin;
    			}

    				 //setRange and setValue method doesn't work when max_bin-min_bin<=1
    	
    		//
    		
    		
    		System.out.println("bin_size="+ bin_size);
    		bin_count = (int)((max_x-min_x)/bin_size)+1;
    		
    		System.out.println("bin_count="+bin_count +"\n");
    		
    		y_freq= new int[bin_count];
    		for (int i =0; i<data_count; i++){
    			int j = (int)((raw_xvalue[i]-min_x)/bin_size);		
    			//System.out.println("j="+j);
    			y_freq[j]++;
    		}
    			
    		x_start = new double[bin_count];
    		x_end = new double[bin_count];
    	
    		for (int i=0; i<bin_count; i++){
    		
    		   x_start[i]=min_x+i*(this.bin_size);
    		   x_end[i]=x_start[i]+(this.bin_size);
    		//	System.out.println("x_bin["+i+"]="+x_bin[i]);
    		//	System.ouhttp://www.echoshadow.com/bbs/rightAction.do?bid=22&pages=1t.println("y_freq["+i+"]="+y_freq[i]);
    		}   		
    }
     
     // 95% bin     
     public double getUpperCutOffValue(){
    	 int index = (int)((bin_count)*0.95);
    	 return (x_end[index]);
     }
     
     // 5%bin
     public double getLowerCutOffValue(){
    	 int index = (int)((bin_count)*0.05);
    	 return (x_start[index]);
     
     }
     
}