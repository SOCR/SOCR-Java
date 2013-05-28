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
import JSci.maths.*;
import JSci.util.*;

/**********************************
* Cohen-Daubechies-Feauveau
* with N=2 and
* Ntilde=4 adapted to the interval
* by Deslauriers-Dubuc-Lemire
***********************************/
public class FastCDF2_4 implements FWT {
  static int FilterType = 1;

  static final float[] scale = {0.03314563036811941f , -0.06629126073623882f , -0.17677669529663687f , 0.41984465132951254f , 0.9943689110435824f , 0.41984465132951254f , -0.17677669529663687f , -0.06629126073623882f , 0.03314563036811941f};
  static final float[] wavelet = {-0.5f , 1.0f , -0.5f};
  static final float[][] scaleLeft = {{1.0275145414117017f , 0.7733980419227863f , -0.22097086912079608f , -0.3314563036811941f , 0.16572815184059705f},
                        {-0.22189158107546605f , 0.4437831621509321f , 0.902297715576584f , 0.5800485314420897f , -0.25687863535292543f , -0.06629126073623882f , 0.03314563036811941f},
                        {0.07549838028293866f , -0.15099676056587732f , -0.0957540432856783f , 0.34250484713723395f , 1.0330388131397217f , 0.41984465132951254f , -0.17677669529663687f , -0.06629126073623882f , 0.03314563036811941f},
                        {-0.013810679320049755f , 0.02762135864009951f , 0.011048543456039804f , -0.04971844555217912f , -0.18506310288866673f , 0.41984465132951254f , 0.9943689110435824f , 0.41984465132951254f , -0.17677669529663687f , -0.06629126073623882f , 0.03314563036811941f }};
  static final float[][] scaleRight = {{ 0.03314563036811941f , -0.06629126073623882f , -0.17677669529663687f , 0.41984465132951254f , 0.9943689110435824f , 0.41984465132951254f , -0.18506310288866673f , -0.04971844555217912f , 0.011048543456039804f , 0.02762135864009951f , -0.013810679320049755f},
                        {0.03314563036811941f , -0.06629126073623882f , -0.17677669529663687f , 0.41984465132951254f , 1.0330388131397217f , 0.34250484713723395f , -0.0957540432856783f , -0.15099676056587732f , 0.07549838028293866f},
                        {0.03314563036811941f , -0.06629126073623882f , -0.25687863535292543f , 0.5800485314420897f , 0.902297715576584f , 0.4437831621509321f , -0.22189158107546605f},
                        {0.16572815184059705f , -0.3314563036811941f , -0.22097086912079608f , 0.7733980419227863f , 1.0275145414117017f}};
//  static final float[] waveletLeft = null;
//  static final float[] waveletRight = null;

  static final float[] scalePrimary = { 0.35355339059327373f , 0.7071067811865475f , 0.35355339059327373f };
  static final float[] waveletPrimary = { 0.0234375f , 0.046875f , -0.125f , -0.296875f , 0.703125f , -0.296875f , -0.125f , 0.046875f , 0.0234375f };
  static final float[][] scalePrimaryLeft = {{0.7071067811865475f , 0.35355339059327373f}};
  static final float[][] scalePrimaryRight = {{0.35355339059327373f , 0.7071067811865475f}};
  static final float[][] waveletPrimaryLeft = {{-0.546875f , 0.5696614583333334f , -0.3138020833333333f , -0.103515625f , 0.10677083333333333f , 0.043619791666666664f , -0.01953125f , -0.009765625f },
                      {0.234375f , -0.087890625f , -0.41015625f , 0.673828125f , -0.2421875f , -0.103515625f , 0.03515625f , 0.017578125f}};
  static final float[][] waveletPrimaryRight = {{ 0.017578125f , 0.03515625f , -0.103515625f , -0.2421875f , 0.673828125f , -0.41015625f , -0.087890625f , 0.234375f},
                      {-0.009765625f , -0.01953125f , 0.043619791666666664f , 0.10677083333333333f , -0.103515625f , -0.3138020833333333f , 0.5696614583333334f , -0.546875f}};

  public FastCDF2_4() {
  }

  
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
  
  
  
  public void transform (float[] v) {
    int last;
    for (last = v.length;last >= 15; last = (last + FilterType) /2) {
      transform(v , last);
    }
  }

  public void invTransform(float[] v) {
    int last;
    for (last = v.length;last >= 15;last = last/2 + FilterType);
    for (;2 * last - FilterType <= v.length;last = 2 * last - FilterType) {
      invTransform(v,last);
    }
  }


  public static  void transform (float[] v,int last) {
    float[] ans = new float[last];
    int half = (last + FilterType) / 2;
    if (2* half - FilterType != last)
    	
      throw new IllegalArgumentException ("Illegal subband : "+last+" within array of length "+v.length);
    //lowpass
    for(int k = 0; k < scaleLeft.length ; k++) {
      for(int l = 0;l <scaleLeft[k].length; l++) {
        ans[k]+=scaleLeft[k][l] * v[l];
      }
    }
    for (int k = scaleLeft.length; k < half - scaleRight.length; k++) {
      for(int l = 0;l <scale.length; l++) {

        ans[k]+=scale[l] * v[2 * k + l - scaleLeft.length];

      }
    }
    for(int k =0 ; k < scaleRight.length ; k++) {
      for(int l = 0;l <scaleRight[k].length; l++) {
        ans[k + half - scaleRight.length]+=scaleRight[k][l] * v[v.length - scaleRight[k].length + l];
      }
    }
    //highpass
    for (int k = 0; k < half - FilterType; k++) {
      for(int l = 0;l <wavelet.length; l++) {
        ans[k + half] += wavelet[l] * v[2 * k + l];
  //      System.out.println((k + half)+" "+(2 * k + l)+" "+wavelet[l] * v[2 * k + l]);
      }
    }
    System.arraycopy(ans,0,v,0,last);
  }


  public static void invTransform (float[] v, int last) {
    float[] ans = new float[2*last - FilterType];
    //scale coefficients
    for(int k = 0; k < scalePrimaryLeft.length; k++) {
      for(int l = 0; l < scalePrimaryLeft[k].length; l++) {
        ans[l] += scalePrimaryLeft[k][l] * v[k];
      }
    }
    for(int k = scalePrimaryLeft.length; k < last-scalePrimaryRight.length; k++) {
      for(int l = 0; l < scalePrimary.length; l++) {
        ans[2 * k - FilterType + l] += scalePrimary[l]*v[k];
      }
    }
    for(int k = 0; k < scalePrimaryRight.length; k++) {
      for(int l = 0; l < scalePrimaryRight[k].length; l++) {
        ans[l - scalePrimaryRight[k].length + ans.length] += scalePrimaryRight[k][l] * v[k + last - scalePrimaryRight.length];
      }
    }
    //wavelet coefficients
    for(int k = 0; k < waveletPrimaryLeft.length; k++) {
      for(int l = 0; l < waveletPrimaryLeft[k].length; l++) {
        ans[l] += waveletPrimaryLeft[k][l] * v[k + last];
      }
    }
    for(int k = waveletPrimaryLeft.length; k < last - FilterType -waveletPrimaryRight.length; k++) {
      for(int l = 0; l < waveletPrimary.length; l++) {
        ans[2 * (k- FilterType) -1  + l ] += waveletPrimary[l]*v[k + last];
      }
    }
    for(int k = 0; k < waveletPrimaryRight.length; k++) {
      for(int l = 0; l < waveletPrimaryRight[k].length; l++) {
        ans[l - waveletPrimaryRight[k].length + ans.length] += waveletPrimaryRight[k][l] * v[k + 2* last - FilterType- waveletPrimaryRight.length];
      }
    }
    System.arraycopy(ans,0,v,0,ans.length);
  }


}

