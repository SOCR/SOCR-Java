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
package edu.ucla.stat.SOCR.games.wavelet;

import JSci.maths.wavelet.FWT;

public class FastHaar implements FWT {

  public FastHaar() {
  }

  static final float[] scale = {(float)(1.0/Math.sqrt(2)),(float)(1.0/Math.sqrt(2))};
  static final float[] wavelet = {-scale[1],scale[0]};

  private static void transform (float[] v,int last) {
    float[] ans=new float[last];
    int half=last/2;
    try {
      for(int k=0;/*k<half*/;k++) {
          ans[k+half]=v[(2*k+0)]*wavelet[0]+v[(2*k+1)]*wavelet[1];
          ans[k]=v[(2*k+0)]*scale[0]+v[(2*k+1)]*scale[1];
      }
    } catch (IndexOutOfBoundsException e) {}
    System.arraycopy(ans,0,v,0,last);
    
  }

  /****************************************
  * Standard (Haar) transform
  *****************************************/
  public void transform (float[] v) {
    int last;
    for (last=v.length;last>2;last/=2) {
      transform(v,last);
    }
    if(last!=2)
      System.err.println("Careful! this should be a power of 2 : "+v.length);
  }

  public void invTransform(float[] v) {
    int last;
    for (last=2;2*last<=v.length;last*=2) {
      invTransform(v,last);
    }
    if(last!=v.length)
      System.err.println("Careful! this should be a power of 2 : "+v.length);

  }
  
  /****************************************
  * Standard (Haar) transform using double
  *****************************************/
 public void transform (double[] x) {
    int last;
    float[] v = new float[x.length];
    for(int i=0;i<x.length;i++)
       v[i] = (float)x[i];
    
    for (last=v.length;last>2;last/=2) {
      transform(v,last);
    }
    if(last!=2)
      System.err.println("Careful! this should be a power of 2 : "+v.length);
      
     for(int i=0;i<x.length;i++)
       x[i] = (double)v[i];
    
  }

  public void invTransform(double[] x) {
    int last;
     float[] v = new float[x.length];
    for(int i=0;i<x.length;i++)
       v[i] = (float)x[i];
    
    for (last=2;2*last<=v.length;last*=2) {
      invTransform(v,last);
    }
    if(last!=v.length)
      System.err.println("Careful! this should be a power of 2 : "+v.length);
     
     
     for(int i=0;i<x.length;i++)
       x[i] = (double)v[i];
    
  }
  /****************************************
  * Standard (Haar) transform
  *****************************************/
  private static void invTransform (float[] v, int last) {
    int ResultingLength=2*last;
    float[] ans=new float[ResultingLength];
    try {
      for(int k=0;/*k<last*/;k++) {
          ans[(2*k+1)]+=scale[1]*v[k]+wavelet[1]*v[k+last] ;
          ans[(2*k+0)]+=scale[0]*v[k]+wavelet[0]*v[k+last] ;
      }
    } catch (IndexOutOfBoundsException e) {}
    System.arraycopy(ans,0,v,0,ans.length);
  }

}
