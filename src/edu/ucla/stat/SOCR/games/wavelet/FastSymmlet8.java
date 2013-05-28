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

/************************
* This is a very fast implementation of the
* Fast Wavelet Transform. It uses in-place computations
* for less memory usage. Data length should be
* a power of 2 a be at least of length 8.
* Handles boundaries by assuming periodicity.
* Ideal for image processing or processing large
* amount of data. Uses floats for more performance.
* Safety is minimal, so be careful!
* @author Daniel Lemire
*************************/
public final class FastSymmlet8 implements FWT {

  public FastSymmlet8() {
  }

  static final float[] scale = {0.0322231006040782f,
                                -0.0126039672622638f,
                                -0.0992195435769564f,
                                0.297857795605605f,
                                0.803738751805386f,
                                0.497618667632563f,
                                -0.0296355276459604f,
                                -0.0757657147893567f};
  static final float[] wavelet = {-scale[7],scale[6],-scale[5],scale[4],-scale[3],scale[2],-scale[1],scale[0]};

  
  public void transform(double[] v) {
  float[] temp = new float[v.length];
  for(int i=0;i<v.length;i++)
  	temp[i] = (float)v[i];
  transform(temp);
  for(int i=0;i<v.length;i++)
  	v[i] = (double)temp[i];
  }
  
  public  void invTransform(double[] v) {
    float[] temp = new float[v.length];
    for(int i=0;i<v.length;i++)
    	temp[i] = (float)v[i];
    invTransform(temp);
    for(int i=0;i<v.length;i++)
    	v[i] = (double)temp[i];
    }
  public static void transform (float[] v,int last) {
    float[] ans=new float[last];
    final int half=last/2;
    try {
      for(int k=0;/*k<half*/;k++) {
          ans[k+half]=v[(2*k+0)]*wavelet[0]+v[(2*k+1)]*wavelet[1]+v[(2*k+2)]*wavelet[2]+v[(2*k+3)]*wavelet[3]+v[(2*k+4)]*wavelet[4]+v[(2*k+5)]*wavelet[5]+v[(2*k+6)]*wavelet[6]+v[(2*k+7)]*wavelet[7];
          ans[k]=v[(2*k+0)]*scale[0]+v[(2*k+1)]*scale[1]+v[(2*k+2)]*scale[2]+v[(2*k+3)]*scale[3]+v[(2*k+4)]*scale[4]+v[(2*k+5)]*scale[5]+v[(2*k+6)]*scale[6]+v[(2*k+7)]*scale[7];
      }
    } catch (IndexOutOfBoundsException e) {}
    ans[last-3]=v[last-6]*wavelet[0]+v[last-5]*wavelet[1]+v[last-4]*wavelet[2]+v[last-3]*wavelet[3]+v[last-2]*wavelet[4]+v[last-1]*wavelet[5]+v[0]*wavelet[6]+v[1]*wavelet[7];
    ans[half-3]=v[last-6]*scale[0]+v[last-5]*scale[1]+v[last-4]*scale[2]+v[last-3]*scale[3]+v[last-2]*scale[4]+v[last-1]*scale[5]+v[0]*scale[6]+v[1]*scale[7];
    ans[last-2]=v[last-4]*wavelet[0]+v[last-3]*wavelet[1]+v[last-2]*wavelet[2]+v[last-1]*wavelet[3]+v[0]*wavelet[4]+v[1]*wavelet[5]+v[2]*wavelet[6]+v[3]*wavelet[7];
    ans[half-2]=v[last-4]*scale[0]+v[last-3]*scale[1]+v[last-2]*scale[2]+v[last-1]*scale[3]+v[0]*scale[4]+v[1]*scale[5]+v[2]*scale[6]+v[3]*scale[7];
    ans[last-1]=v[last-2]*wavelet[0]+v[last-1]*wavelet[1]+v[0]*wavelet[2]+v[1]*wavelet[3]+v[2]*wavelet[4]+v[3]*wavelet[5]+v[4]*wavelet[6]+v[5]*wavelet[7];
    ans[half-1]=v[last-2]*scale[0]+v[last-1]*scale[1]+v[0]*scale[2]+v[1]*scale[3]+v[2]*scale[4]+v[3]*scale[5]+v[4]*scale[6]+v[5]*scale[7];
    System.arraycopy(ans,0,v,0,last);
  }

  public void transform (float[] v) {
    int last;
    for (last=v.length;last>8;last/=2) {
      transform(v,last);
    }
    if(last!=8)
      System.err.println("Careful! this should be a power of 2 : "+v.length);
  }

  public void invTransform(float[] v) {
    int last;
    for (last=8;2*last<=v.length;last*=2) {
      invTransform(v,last);
    }
    if(last!=v.length)
      System.err.println("Careful! this should be a power of 2 : "+v.length);
  }

  public static void invTransform (float[] v, int last) {
    final int ResultingLength=2*last;
    float[] ans=new float[ResultingLength];
    try {
      for(int k=0;/*k<last*/;k++) {
          ans[(2*k+7)]+=scale[7]*v[k]+wavelet[7]*v[k+last] ;
          ans[(2*k+6)]+=scale[6]*v[k]+wavelet[6]*v[k+last] ;
          ans[(2*k+5)]+=scale[5]*v[k]+wavelet[5]*v[k+last] ;
          ans[(2*k+4)]+=scale[4]*v[k]+wavelet[4]*v[k+last] ;
          ans[(2*k+3)]+=scale[3]*v[k]+wavelet[3]*v[k+last] ;
          ans[(2*k+2)]+=scale[2]*v[k]+wavelet[2]*v[k+last] ;
          ans[(2*k+1)]+=scale[1]*v[k]+wavelet[1]*v[k+last] ;
          ans[(2*k+0)]+=scale[0]*v[k]+wavelet[0]*v[k+last] ;
      }
    } catch (IndexOutOfBoundsException e) {}
    ans[ResultingLength-6]+=scale[0]*v[last-3]+wavelet[0]*v[ResultingLength-3] ;
    ans[ResultingLength-5]+=scale[1]*v[last-3]+wavelet[1]*v[ResultingLength-3] ;
    ans[ResultingLength-4]+=scale[2]*v[last-3]+wavelet[2]*v[ResultingLength-3] ;
    ans[ResultingLength-3]+=scale[3]*v[last-3]+wavelet[3]*v[ResultingLength-3] ;
    ans[ResultingLength-2]+=scale[4]*v[last-3]+wavelet[4]*v[ResultingLength-3] ;
    ans[ResultingLength-1]+=scale[5]*v[last-3]+wavelet[5]*v[ResultingLength-3] ;
    ans[0]+=scale[6]*v[last-3]+wavelet[6]*v[ResultingLength-3] ;
    ans[1]+=scale[7]*v[last-3]+wavelet[7]*v[ResultingLength-3] ;
    ans[ResultingLength-4]+=scale[0]*v[last-2]+wavelet[0]*v[ResultingLength-2] ;
    ans[ResultingLength-3]+=scale[1]*v[last-2]+wavelet[1]*v[ResultingLength-2] ;
    ans[ResultingLength-2]+=scale[2]*v[last-2]+wavelet[2]*v[ResultingLength-2] ;
    ans[ResultingLength-1]+=scale[3]*v[last-2]+wavelet[3]*v[ResultingLength-2] ;
    ans[0]+=scale[4]*v[last-2]+wavelet[4]*v[ResultingLength-2] ;
    ans[1]+=scale[5]*v[last-2]+wavelet[5]*v[ResultingLength-2] ;
    ans[2]+=scale[6]*v[last-2]+wavelet[6]*v[ResultingLength-2] ;
    ans[3]+=scale[7]*v[last-2]+wavelet[7]*v[ResultingLength-2] ;
    ans[ResultingLength-2]+=scale[0]*v[last-1]+wavelet[0]*v[ResultingLength-1] ;
    ans[ResultingLength-1]+=scale[1]*v[last-1]+wavelet[1]*v[ResultingLength-1] ;
    ans[0]+=scale[2]*v[last-1]+wavelet[2]*v[ResultingLength-1] ;
    ans[1]+=scale[3]*v[last-1]+wavelet[3]*v[ResultingLength-1] ;
    ans[2]+=scale[4]*v[last-1]+wavelet[4]*v[ResultingLength-1] ;
    ans[3]+=scale[5]*v[last-1]+wavelet[5]*v[ResultingLength-1] ;
    ans[4]+=scale[6]*v[last-1]+wavelet[6]*v[ResultingLength-1] ;
    ans[5]+=scale[7]*v[last-1]+wavelet[7]*v[ResultingLength-1] ;
    System.arraycopy(ans,0,v,0,ans.length);
  }
}

