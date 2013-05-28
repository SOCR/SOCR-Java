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

public class REXP
{
    public static final int XT_STR         = 1;
    public static final int XT_STRARRAY    = 2;
    public static final int XT_INTARRAY    = 3;
    public static final int XT_DOUBLEARRAY = 4;

    /* internal SEXP types in R - taken directly from Rinternals.h */
    public static final int NILSXP	= 0;	/* nil = NULL */
    public static final int SYMSXP	= 1;	/* symbols */
    public static final int LISTSXP	= 2;	/* lists of dotted pairs */
    public static final int CLOSXP	= 3;	/* closures */
    public static final int ENVSXP	= 4;	/* environments */
    public static final int PROMSXP	= 5;	/* promises: [un]evaluated closure arguments */
    public static final int LANGSXP	= 6;	/* language constructs (special lists) */
    public static final int SPECIALSXP	= 7;	/* special forms */
    public static final int BUILTINSXP	= 8;	/* builtin non-special forms */
    public static final int CHARSXP	= 9;	/* "scalar" string type (internal only)*/
    public static final int LGLSXP	= 10;	/* logical vectors */
    public static final int INTSXP	= 13;	/* integer vectors */
    public static final int REALSXP	= 14;	/* real variables */
    public static final int CPLXSXP	= 15;	/* complex variables */
    public static final int STRSXP	= 16;	/* string vectors */
    public static final int DOTSXP	= 17;	/* dot-dot-dot object */
    public static final int ANYSXP	= 18;	/* make "any" args work */
    public static final int VECSXP	= 19;	/* generic vectors */
    public static final int EXPRSXP	= 20;	/* expressions vectors */
    public static final int BCODESXP    = 21;   /* byte code */
    public static final int EXTPTRSXP   = 22;   /* external pointer */
    public static final int WEAKREFSXP  = 23;   /* weak reference */
        
    public static final int FUNSXP	= 99;	/* Closure or Builtin */
        
    /** Engine which this EXP was obtained from. EXPs are valid only for the engine they were obtained from - it's illegal to mix EXP between engines. There is a speacial case when the engine may be null - if a REXP creating was requested but deferred until an engine is available. */
    Rengine eng;
    /** native reference to the SEXP represented in R. It's usually a pointer, but can be any handle obtained from the engine. This reference can be used when calling RNI commands directly. */
    public long xp;
    /** native type of the represented expression (see ...SXP constants in R). Please note that this type is cached and may have changed in the meantime. If the possibility of changing type exists (mainly list/lang) then use rniExpType to make sure */
    public int rtype;
    /** local expression type (see XT_... constants). */
    int xt;
    /** local object representing the contents (if direct conversion exists) */
    Object o;

    /** create a REXP directly from a R SEXP reference. SEXP types STRSXP, INTSXP and REALSXP are automatically converted. All others are represented as SEXP references only. */
    public REXP(Rengine re, long exp) {
        eng=re; xp=exp;
        rtype=re.rniExpType(xp);
        if (rtype==STRSXP) {
            String[] s=re.rniGetStringArray(xp);
            if (s!=null && s.length==1) {
                o=s[0]; xt=XT_STR;
            } else {
                o=s; xt=XT_STRARRAY;
            }
        } else if (rtype==INTSXP) {
            o=re.rniGetIntArray(xp);
            xt=XT_INTARRAY;
        } else if (rtype==REALSXP) {
            o=re.rniGetDoubleArray(xp);
            xt=XT_DOUBLEARRAY;
        } else xt=0;
    }

    /** Obtains R engine object which supplied this REXP.
        @returns {@link Rengine} object */
    Rengine getEngine() { return eng; };
        
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
