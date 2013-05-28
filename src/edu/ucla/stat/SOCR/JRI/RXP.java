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
package edu.ucla.stat.SOCR.JRI;

//package org.rosuda.JRI;

public class RXP
{
    public final int XT_STR         = 1;
    public final int XT_STRARRAY    = 2;
    public final int XT_INTARRAY    = 3;
    public final int XT_DOUBLEARRAY = 4;
    
    Rengine eng;
    long xp;
    int rtype;
    int xt;
    Object o;

    public RXP(Rengine re, long exp) {
        eng=re; xp=exp;
        rtype=re.rniExpType(xp);
        if (rtype==16) {
            String[] s=re.rniGetStringArray(xp);
            if (s!=null && s.length==1) {
                o=s[0]; xt=XT_STR;
            } else {
                o=s; xt=XT_STRARRAY;
            }
        } else if (rtype==13) {
            o=re.rniGetIntArray(xp);
            xt=XT_INTARRAY;
        } else if (rtype==14) {
            o=re.rniGetDoubleArray(xp);
            xt=XT_DOUBLEARRAY;
        } else xt=0;
    }

    public String asString() {
        if (o==null) return null;
        if (xt==XT_STR) return (String)o;
        if (xt==XT_STRARRAY) {
            String[] sa = (String[])o;
            return (sa.length>0)?sa[0]:null;
        }
        return null;
    }

    public String[] asStringArray() {
        if (o==null) return null;
        if (xt==XT_STR) {
            String[] sa=new String[1];
            sa[0]=(String)o;
            return sa;
        }
        if (xt==XT_STRARRAY) return (String[])o;
        return null;
    }

    public double[] asDoubleArray() {
        if (o==null || xt!=XT_DOUBLEARRAY) return null;
        return (double[])o;
    }

    public int[] asIntArray() {
        if (o==null || xt!=XT_INTARRAY) return null;
        return (int[])o;
    }

    public String toString() {
        return "RXP["+((xt==0)?("unknown/"+rtype):((xt==XT_STR)?"string":((xt==XT_STRARRAY)?"String[]":((xt==XT_INTARRAY)?"int[]":((xt==XT_DOUBLEARRAY)?"double[]":"???")))))+", id="+xp+", o="+o+"]";
    }
}
