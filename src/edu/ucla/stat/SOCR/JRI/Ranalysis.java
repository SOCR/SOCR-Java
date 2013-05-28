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

import java.io.*;
import java.lang.reflect.Array;
import java.lang.String;

import java.util.Vector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


import edu.ucla.stat.SOCR.JRI.Rengine;
import edu.ucla.stat.SOCR.JRI.REXP;
import edu.ucla.stat.SOCR.JRI.TextConsole;


public class Ranalysis{

	static final boolean DEBUG = true;
	static final boolean R_VERBOSE = true;	

    protected static final String DEFAULT_ELEMENT_NAME = "*";
    protected static final String DEFAULT_ANALYSIS_NAME = "analysis_name";
    protected static final String DEFAULT_DATAVALUE_NAME = "data_value";
	protected static final String DEFAULT_IN_FNAME = "analysis_xml/in.xml";
	protected static final String DEFAULT_OUT_FNAME = "analysis_xml/out.xml";
	protected static final String DEFAULT_TEMPLATE_FNAME = "analysis_xml/template.xml";

	Rengine re;
	XMLtree myXML;

	//----input variables
	int num_indep;
	String indep_name[];
	String indep_data_type[];
	String indep_data_value[][];
	String dep_name, dep_data_type, dep_data_value[];	
	String interaction[];
	String parameter_name[], parameter_value[];
	int num_interaction, num_parameter;
		
	//----


    /**
     *  Constructor
     */
    public Ranalysis(String[] Rargs) {
		re = Rstart(Rargs);

		myXML = new XMLtree();
 		re.jriWriteConsole("no xml file provided, do a test lm analysis instead!\n");	
		lm();
		//anova();
		re.jriWriteConsole("no xml file provided, do a test lm analysis instead!--done\n");
		RLoop(re);
    }//Ranalysis

	/**
	 *
	 */
	public Ranalysis(String[] Rargs, String in_fName, String t_fName, String out_fName){
		re =Rstart(Rargs);
		myXML = new XMLtree(in_fName);

		runXMLFile(t_fName);

		myXML.writeXMLFile(out_fName);
		Rend(re);
	}//Ranalysis

	
	public Ranalysis(Document input_xml, Document template_xml){
		re = Rstart(null);
		myXML = new XMLtree(input_xml);
		
		runXML(template_xml);
		Rend(re);
	}//Ranalysis

	public Document getAnalysisResults(){
		return myXML.getXMLroot();
	}

	protected void RLoop(Rengine re){
		// so far we used R as a computational slave without REPL
	    // now we start the loop, so the user can use the console

		if (true) {
			System.out.println("Now the console is yours ... have fun");
			re.startMainLoop();
		} else {
			re.end();
			System.out.println("end");
		}

	}//RLoop

	protected void Rend(Rengine re){
		re.end();
		System.out.println("end");
	}

	protected Rengine Rstart(String[] Rargs){
		  System.out.println("Creating Rengine (with arguments)");
	// 1) we pass the arguments from the command line
	// 2) we won't use the main loop at first, we'll start it later
	// 3) the callbacks are implemented by the TextConsole class above

		TextConsole tc = new TextConsole();	
		Rengine re=new Rengine(Rargs, false, tc);
		tc.setVerbose(R_VERBOSE);

		System.out.println("Rengine created, waiting for R");
	// the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return null;
        } else{ 
			return re;
		}	
	}
 
	//
    // Create a URL object from either a URL string or a plain file name.
    //
    static URL createURL(String name) throws Exception {
        try {
                URL u = new URL(name);
                return u;
        } catch (MalformedURLException ex) {
        }
        URL u = new URL("file:" + new File(name).getAbsolutePath());
        return u;
    }//createURL    


	//read template from a xml file
	protected void runXMLFile(String t_fName){
				
		String elName = DEFAULT_ANALYSIS_NAME;
        String attributeName = null;
		
		XMLtree x =	new XMLtree(t_fName);		
		Document t_xml = x.getXMLroot();

		runXML(t_xml);

	}	//end of readXMLFile(Rengine, fName)


//read template from a xml document
	protected void runXML(Document t_xml){
				
		String elName = DEFAULT_ANALYSIS_NAME;
        String attributeName = null;
      
		//	get analysis_name
		String method_name = myXML.getValueByName(elName);
		re.jriWriteConsole("calling--"+method_name +"-- \n");

		if 	(method_name.toLowerCase().indexOf("linear")!=-1)
			do_lm(t_xml);
		else if (method_name.toLowerCase().indexOf("anova")!=-1)	
		    do_anova(t_xml);
		else if (method_name.toLowerCase().indexOf("ancova")!=-1)	
			do_ancova(t_xml);
		else if (method_name.toLowerCase().indexOf("logistic")!=-1)	
			do_glm(t_xml);
		else if (method_name.toLowerCase().indexOf("time_series_arima")!=-1)	
			do_timeSeries(t_xml);

		re.jriWriteConsole("END calling --"+method_name+"-- \n");	

	}	//end of readXMLFile(Rengine, fName)

	protected void readInput(){

		NodeList els= myXML.getElementsByName("independent");
		num_indep=els.getLength();	
		if (els ==null || num_indep ==0)
			{System.out.println("no independent is found !"); return;}

		indep_name = new String[num_indep];
        indep_data_type = new String[num_indep];
		indep_data_value = new String[num_indep][];

		if (num_indep >1)
			System.out.println("found "+num_indep+" independents!");
		for (int i=0; i<num_indep; i++){
			Element el = (Element)els.item(i);
			indep_name[i] = myXML.getValueByName(el,"name");
			indep_data_type[i]  = myXML.getValueByName(el, "data_type");
			indep_data_value[i]  = myXML.getValueByName(el, "data_value").split(",( )?");	
			
			if (DEBUG){
				System.out.println("indep_name ="+indep_name[i]);	
				System.out.println("indep_datatype ="+indep_data_type[i]);		

				System.out.println("indep_data_value ="+indep_data_value[i][0]);
			}
		}
		
		els= myXML.getElementsByName("dependent");
		if (els ==null ||els.getLength()==0)
			{System.out.println("no dependent is found !"); return;}

		if (els.getLength()>1)
			{System.out.println("found more than one dependents!"); return;}
		else {
			Element el = (Element)els.item(0);
			dep_name = myXML.getValueByName(el,"name");
			dep_data_type  = myXML.getValueByName(el, "data_type");
			dep_data_value  = myXML.getValueByName(el, "data_value").split(",( )?");	
			
			if (DEBUG){
				System.out.println("dep_name ="+dep_name);	
				System.out.println("dep_datatype ="+dep_data_type);	
				System.out.println("dep_data_value ="+dep_data_value[0]);
			}
		}

		num_interaction=0;
		els= myXML.getElementsByName("interaction");	
		if (els ==null ||els.getLength()==0)
			{System.out.println("no interaction is found !");
			interaction=null;}
		else {
			num_interaction = els.getLength();
			interaction = new String[num_interaction];
			for (int i=0; i<num_interaction; i++){
				Element el = (Element)els.item(i);
				interaction[i] = myXML.getValueByName(el,"name");

				if (DEBUG){
					System.out.println("interaction["+i+"]="+interaction[i]);
				}
			}//for
		}

		num_parameter=0;
		els= myXML.getElementsByName("parameter");	
		if (els ==null ||els.getLength()==0)
			{System.out.println("no parameter is found !");
			parameter_name=null;parameter_value=null;
			}
		else {
			num_parameter = els.getLength();
			parameter_name= new String[num_parameter];
			parameter_value= new String[num_parameter];
			for (int i=0; i<num_parameter; i++){
				Element el = (Element)els.item(i);
				parameter_name[i] = myXML.getValueByName(el,"name");
				parameter_value[i] = myXML.getValueByName(el,"data_value");

				if (DEBUG){
					System.out.println("parameter["+i+"]="+parameter_name[i]+"="+parameter_value[i]);
				}
			}//for
		}


	}	//readInput

	protected void  setInput(){
		
		int xx[], yy[];
		double xxx[], yyy[];
		
		if (DEBUG) 
			System.out.println("num_indep="+num_indep);
        for (int i=0; i<num_indep; i++){
			try {
				xx = parseInt(indep_data_value[i]);
				long x1 = re.rniPutIntArray(xx);
				re.rniAssign(indep_name[i], x1, 0);
				
			}catch(java.lang.NumberFormatException e){
				xxx = parseNumber(indep_data_value[i]);
				long x1 = re.rniPutDoubleArray(xxx);
				re.rniAssign(indep_name[i], x1, 0);
			}
		}

		for (int i=0; i<num_parameter; i++){
			xx = new int[1];
			xx[0] =Integer.parseInt(parameter_value[i]);
			long x1 = re.rniPutIntArray(xx);
			re.rniAssign(parameter_name[i], x1, 0);
		}

		try {
			 yy = parseInt(dep_data_value);
			 long y1 = re.rniPutIntArray(yy);
			 re.rniAssign(dep_name, y1, 0);
		 }catch(java.lang.NumberFormatException e){
			 yyy = parseNumber(dep_data_value);
			 long y1 = re.rniPutDoubleArray(yyy);
			 re.rniAssign(dep_name, y1, 0);		
		 }

		long e, r;
		//x<-factor(x)
		for (int i=0; i<num_indep; i++){
			if (indep_data_type[i].equalsIgnoreCase("factor")){
				e = re.rniParse("factor("+indep_name[i]+")",1);
				r = re.rniEval(e,0);
				re.rniAssign(indep_name[i], r, 0);	
			}
		}
 
	}//setInput


	public void do_lm(Document t_xml){
		re.jriWriteConsole("========do_lm()==============\n");	
 
		if (DEBUG)	System.out.println("START: do_lm");

		readInput();

		//done with reading XML
		//start to prepare writing XML
		myXML.flush(t_xml);
		//myXML.print();

		setInput();

		REXP x;
		String[] s;	
		long e, r;
		
		 //model <- lm(y~x)
		String  formulae = makeFormulae("lm");

		 e = re.rniParse(formulae,1);
		 r = re.rniEval(e,0);
		 re.rniAssign("model", r, 0);

		 //summary <- summary(model)
		 e = re.rniParse("summary(model)",1);
		 r = re.rniEval(e,0);
		 re.rniAssign("summary",r,0);

		 e = re.rniParse("capture.output(model$fitted)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from model$fitted \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i+1]+"\"\n");
				  if (i>0)
					 sb.append(" "+s[i+1]);
				 else sb.append(s[i+1]);
				 i+=2;}
			 myXML.insert("fitted", sb.toString()); 	
		 }

		 e = re.rniParse("capture.output(summary$coef)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from summary$coef \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; 
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				 i++;}
			 myXML.insertChildren("intercept", s[1].substring(12)); 
			 for (int j=0; j<num_indep; j++)
				 if(j>0)
					 myXML.insertSiblingWithChildren("predictor", s[2+j]);
				 else 	
					 myXML.insertChildren("predictor", s[2+j]);
		 }	
		 
		 e = re.rniParse("capture.output(summary$r.sq)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from summary$r.sq \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				 if (i>0)
					 sb.append(" "+s[i].substring(4));
				 else sb.append(s[i].substring(4));
				
				 i++;}
			 myXML.insert("multiple_r_squared", sb.toString()); 	
		 }

		 e = re.rniParse("capture.output(summary$adj.r)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from summary$adj.r \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0;  StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				 if (i>0)
					 sb.append(" "+s[i].substring(4));
				 else sb.append(s[i].substring(4));
				 i++;}
			 myXML.insert("adjusted_r_squared", sb.toString()); 	
		 }

		 e = re.rniParse("capture.output(summary$resi)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from summary$resi \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+(i+1)+"] \""+s[i+1]+"\"\n");
				 if (i>0)
					 sb.append(" "+s[i+1]);
				 else sb.append(s[i+1]);
				 i+=2;}
			 myXML.insert("residuals", sb.toString()); 	
		 }
		 
		  //lm_result<-summary(model)
		  e = re.rniParse("capture.output(summary(model))",1);
		  r = re.rniEval(e,0);
		  //re.eval("capture.output(summary(model))");
		  // re.rniAssign("lm_result", r, 0);	
		  x=new REXP(re, r);		
		  re.jriWriteConsole("reprint from return summary\n");

		  s = x.asStringArray();
		  if (s!=null) {
			  int i=0; 
			  while (i<s.length) { 
				  re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");

				  if (s[i].indexOf("Residual standard error")!=-1){
					  insert("residual_standard_error", s[i]); 
					i++;	continue;
				  }
				  if (s[i].indexOf("F-statistic")!=-1){
					  insert("f_statistics", s[i]); 
					 i++;	continue;
				  }
				  i++;
			  }//while 
		  }	
		  else	{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		   re.jriWriteConsole("done reprint summary!!!\n");
		
		 do_plotdata();
		   //   System.out.println("after inserting all the return value");
		   //   myXML.print();
		   
	}

	/*# ARIMA (auro regression integrated moving average.)
# INPUT:
# INPUT_1. data in time series. REQUIRED. (in this example, it's LakeHuron)
# INPUT_2. index: default is 1 to length(data), if not sepcified.
# INPUT_3. p, d, q, of ARIMA model. REQUIRED.
# INPUT_4. number of box.test p-values: default is 10 if not specified.
# INPUT_5. For Prediction. number of aheads, if prediction is to be made.

# OUTPUT:
# OUTPUT_1. ACF (a in my code)
# OUTPUT_2. RESIDUALS (STANDARDIZED, from StructTS, see help(StructTS) for details.) (t$resid in my code)
# OUTPUT_3. RESIDUALS from fit (residuals, not standardized)
# OUTPUT_4. BOX test's p.values
# OUTPUT_5. predicted values
# OUTPUT_6. predicted SE


# using arima with a R built-in example, data is LakeHuron.
# x = INPUT_1, the data. I name it x because I don't want to type the whole word.
x = c(580.38,581.86,580.97,580.80,579.79,580.39,580.42,580.82,581.40,581.32,581.44,581.68,581.17,580.53,580.01,579.91,579.14,579.16,579.55,579.67,578.44,578.24,579.10,579.09,579.35,578.82,579.32,579.01,579.00,579.80,579.83,579.72,579.89,580.01,579.37,578.69,578.19,578.67,579.55,578.92,578.09,579.37,580.13,580.14,579.51,579.24,578.66,578.86,578.05,577.79,576.75,576.75,577.82,578.64,580.58,579.48,577.38,576.90,576.94,576.24,576.84,576.85,576.90,577.79,578.18,577.51,577.23,578.42,579.61,579.05,579.26,579.22,579.38,579.10,577.95,578.12,579.75,580.85,580.41,579.96,579.61,578.76,578.18,577.21,577.13,579.10,578.25,577.91,576.89,575.96,576.80,577.68,578.38,578.52,579.74,579.31,579.89,579.96)

# INPUT_2
index =  c(1875,1876,1877,1878,1879,1880,1881,1882,1883,1884,1885,1886,1887,1888,1889,1890,1891,1892,1893,1894,1895,1896,1897,1898,1899,1900,1901,1902,1903,1904,1905,1906,1907,1908,1909,1910,1911,1912,1913,1914,1915,1916,1917,1918,1919,1920,1921,1922,1923,1924,1925,1926,1927,1928,1929,1930,1931,1932,1933,1934,1935,1936,1937,1938,1939,1940,1941,1942,1943,1944,1945,1946,1947,1948,1949,1950,1951,1952,1953,1954,1955,1956,1957,1958,1959,1960,1961,1962,1963,1964,1965,1966,1967,1968,1969,1970,1971,1972)

# INPUT_3
p = 1
d = 0
q = 1
fit = arima(x, order = c(p, d, q)) # need to specify these parameters. INPUT_2
r = fit$resid 	# THIS IS OUTPUT_3. this goes into <residuals></residuals>

t = StructTS(x)
#use summary(variable) to see what's in there.
summary(fit)
#summary(t)
t$resid # OUTPUT_2 --> this goes into <standardardized_residuals></standardardized_residuals>

fit  #output ->go to coefficients

a = acf(r) # autocorrelation. WANT THIS. OUTPUT_1. this goes into <acf></acf>
# type a to see what's in there. But I don't know how to extract the data.
a

# INPUT_5 
numberAhead = 8
x.pred = predict(fit, n.ahead = numberAhead) # INPUT_5 is n.ahead
x.pred
x.pred$pred # WANT THIS. OUTPUT_5 <prediction><predicted></predicted><prediction>
x.pred$se	# WANT THIS. OUTPUT_6 <prediction><standard_errors></standard_errors><prediction

# INPUT_4
boxMaxLag = 10 # INPUT_4: 10 is default but the user can input a different number from the XML.

bv = rep(0, boxMaxLag) # a place to store box test p values
for (i in 1:boxMaxLag) {
	b = Box.test(fit$resid, lag = i)# default gof.lag = 1
	bv[i] = b$p.value
}

bv #  WANT THIS. OUTPUT_4, p-values of Box.test results.
	 */

	public void do_timeSeries(Document t_xml){	
		re.jriWriteConsole("========timeSeriesArima()==============\n");	
		if (DEBUG)	System.out.println("START: do_timeSeries");

		readInput();
		setInput(); //set timeseries,index, p,d,q,numberAhead, boxMaxLag

		//done with reading XML
		//prepare to write XML

		myXML.flush(t_xml);
 
		REXP x;
		String[] s;	
		long e, r;
		
		//fit=arima(x,order=c(p,d,q))
		String formulae =makeFormulae("arima");

		//fit<-formulae
		e = re.rniParse(formulae,1);
		r = re.rniEval(e,0);
		re.rniAssign("fit", r, 0);	
		
		//r=fit$resid
		e = re.rniParse("fit$resid",1);
		r = re.rniEval(e,0);
		re.rniAssign("r",r,0);

		e = re.rniParse("capture.output(fit$resid)",1);
		r = re.rniEval(e,0);
		x=new REXP(re, r);		
		re.jriWriteConsole("reprint from return fit$resid\n");

		s =x.asStringArray();
		if (s!=null) {
			String res = getTimeSeriesResult(s);
			myXML.insert("residuals", res.trim()); //output3
		}
		else	
			{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		re.jriWriteConsole("done reprint fit$resid!!!\n");	

		//t=StructTS(x)
		e = re.rniParse("StructTS("+dep_name+")",1);
		r = re.rniEval(e,0);
		re.rniAssign("t",r,0);

		//summary(fit)
		e = re.rniParse("summary(fit)",1);
		r = re.rniEval(e,0);

		e = re.rniParse("capture.output(t$resid)",1);
		r = re.rniEval(e,0);
		x=new REXP(re, r);		
		re.jriWriteConsole("reprint from return t$resid\n");

		s =x.asStringArray();
		if (s!=null) {
			String res = getTimeSeriesResult(s);
			myXML.insert("standardardized_residuals", res.trim());//output2
		}
		else	
			{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		re.jriWriteConsole("done reprint t$resid!!!\n");	

		e = re.rniParse("capture.output(fit)",1);
		r = re.rniEval(e,0);
		x=new REXP(re, r);		
		re.jriWriteConsole("reprint from return fit\n");

		s =x.asStringArray();
		if (s!=null) {
			 int i=0; 
			 int start=0;
			  while (i<s.length) { 
				  re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				  if (s[i].indexOf("Coefficients")!=-1){
					  start=i; i++;continue;
				  }
				
				  if(start!=0 && i == (start+2)){
					  myXML.insertChildren("coefficients", s[i].substring(s[i].lastIndexOf("]")+1).trim());//
				  }
 				  else  if(start!=0 && i == (start+3)){
					  myXML.insertChildren("coefficients_standard_errors", s[i].substring(s[i].lastIndexOf("s.e.")+4).trim());//
				  }
				  i++;
			  }//while
		}
		else	
			{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		re.jriWriteConsole("done reprint fit!!!\n");	

		//a=acf(r)
		e = re.rniParse("acf(r)",1);
		r = re.rniEval(e,0);
		re.rniAssign("a",r,0);

		e = re.rniParse("capture.output(a)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from a \n");
		 s = x.asStringArray();
		  if (s!=null) {
			  StringBuffer res_index= new StringBuffer();
			  StringBuffer res_value= new StringBuffer();
			  int i=3; 
			  while (i<s.length) { 
				  re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				  if (i%2==0)
					  res_value.append(s[i].substring(s[i].lastIndexOf("]")+1)+" ");
				  else res_index.append(s[i].substring(s[i].lastIndexOf("]")+1)+" ");
				   i++;
			  }//while 
			  myXML.insert("index", res_index.toString().trim()); //output 1
			  myXML.insert("value", res_value.toString().trim()); //output 
			  re.jriWriteConsole("done reprint from a  \n");
		  }	
		  else	{ re.jriWriteConsole("a ==null!!!!!!!\n");}	


		//dep.pred = predict(fit,n.ahead=numberAhead)
		e = re.rniParse("predict(fit,n.ahead="+parameter_value[3]+")",1);
		r = re.rniEval(e,0);
		re.rniAssign(dep_name+".pred",r,0);

		 e = re.rniParse("capture.output("+dep_name+".pred$pred)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from x.pred$pred \n");

		 s =x.asStringArray();
		if (s!=null) {
			String res = getTimeSeriesResult(s);
			myXML.insert("predicted", res.trim()); //output 5
		}
		else	
			{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		re.jriWriteConsole("done reprint x.pred$pred!!!\n");	

	 e = re.rniParse("capture.output("+dep_name+".pred$se)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from x.pred$se \n");

		 s =x.asStringArray();
		if (s!=null) {
			String res = getTimeSeriesResult(s);
			myXML.insert("prediction_standard_errors", res.trim()); //output 6
		}
		else	
			{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		re.jriWriteConsole("done reprint x.pred$se!!!\n");	

		//bv=rep(0, boxmaxlag)
		e = re.rniParse("rep(0,"+parameter_value[4]+")",1);
		r = re.rniEval(e,0);
		re.rniAssign("bv",r,0);

		//for (i in 1:boxmaxlag){
		//  b= Box.test(fit$resid, la=i)
		//	bv[i] = b$p.value}

		e = re.rniParse("for (i in 1:"+parameter_value[4]+"){\n b= Box.test(fit$resid, la=i) \n bv[i] = b$p.value\n}",1);
		r = re.rniEval(e,0);


		e = re.rniParse("capture.output(bv)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from bv \n");
 
		 s = x.asStringArray();
		  if (s!=null) {
			  StringBuffer res= new StringBuffer();
			  int i=0; 
			  while (i<s.length) { 
				  re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				  res.append(s[i].substring(s[i].lastIndexOf("]")+1)+" ");
				  i++;
			  }//while 
			  myXML.insert("p_values", res.toString().trim()); //output 4
			  re.jriWriteConsole("done reprint from bv \n");
		  }	
		  else	{ re.jriWriteConsole("bv==null!!!!!!!\n");}	

		  re.jriWriteConsole("done reprint summary!!!\n");
		

		   //do_plotdata();

		re.jriWriteConsole("======================\n");	
	}

	public void do_glm(Document t_xml){
		re.jriWriteConsole("========do_glm()==============\n");	
 
		if (DEBUG)	System.out.println("START: do_glm");

		readInput();

		//done with reading XML
		//start to prepare writing XML
		myXML.flush(t_xml);
		//myXML.print();

		setInput();

		REXP x;
		String[] s;	
		long e, r;
		
		 //model <- glm(y~x)
		String  formulae = makeFormulae("glm");

		 e = re.rniParse(formulae,1);
		 r = re.rniEval(e,0);
		 re.rniAssign("model", r, 0);

		 //summary <- summary(model)
		 e = re.rniParse("summary(model)",1);
		 r = re.rniEval(e,0);
		 re.rniAssign("summary",r,0);

		 e = re.rniParse("capture.output(model$fitted)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from model$fitted \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i+1]+"\"\n");
				  if (i>0)
					 sb.append(" "+s[i+1]);
				 else sb.append(s[i+1]);
				 i+=2;}
			 myXML.insert("fitted", sb.toString()); 	
		 }

		 e = re.rniParse("capture.output(summary$coef)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from summary$coef \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; 
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				 i++;}
			 myXML.insertChildren("intercept", s[1].substring(12)); 
			 for (int j=0; j<num_indep; j++)
				 if(j>0)
					 myXML.insertSiblingWithChildren("predictor", s[2+j]);
				 else 	
					 myXML.insertChildren("predictor", s[2+j]);
		 }	
		 
		 e = re.rniParse("capture.output(summary$r.sq)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from summary$r.sq \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				 if (i>0)
					 sb.append(" "+s[i].substring(4));
				 else sb.append(s[i].substring(4));
				
				 i++;}
			 myXML.insert("multiple_r_squared", sb.toString()); 	
		 }

		 e = re.rniParse("capture.output(summary$adj.r)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from summary$adj.r \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0;  StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				 if (i>0)
					 sb.append(" "+s[i].substring(4));
				 else sb.append(s[i].substring(4));
				 i++;}
			 myXML.insert("adjusted_r_squared", sb.toString()); 	
		 }

		 e = re.rniParse("capture.output(model$resi)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from model$resi \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+(i+1)+"] \""+s[i+1]+"\"\n");
				 if (i>0)
					 sb.append(" "+s[i+1]);
				 else sb.append(s[i+1]);
				 i+=2;}
			 myXML.insert("residuals", sb.toString()); 	
		 }
		 
		  //lm_result<-summary(model)
		  e = re.rniParse("capture.output(summary(model))",1);
		  r = re.rniEval(e,0);
		  //re.eval("capture.output(summary(model))");
		  // re.rniAssign("lm_result", r, 0);	
		  x=new REXP(re, r);		
		  re.jriWriteConsole("reprint from return summary\n");

		  s = x.asStringArray();
		  if (s!=null) {
			  int i=0; 
			  while (i<s.length) { 
				  re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");

				  if (s[i].indexOf("Residual standard error")!=-1){
					  insert("residual_standard_error", s[i]); 
					i++;	continue;
				  }
				  if (s[i].indexOf("F-statistic")!=-1){
					  insert("f_statistics", s[i]); 
					 i++;	continue;
				  }
				  i++;
			  }//while 
		  }	
		  else	{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		   re.jriWriteConsole("done reprint summary!!!\n");
		
		 do_plotdata();
		   //   System.out.println("after inserting all the return value");
		   //   myXML.print();
		   
	}


	public void do_anova(Document t_xml){	
		re.jriWriteConsole("========anova_xml_file()==============\n");	
		if (DEBUG)	System.out.println("START: do_anova");

		readInput();
		setInput();

		//done with reading XML
		//prepare to write XML

		myXML.flush(t_xml);
 
		REXP x;
		String[] s;	
		long e, r;
		
		//model <- lm(y~x)
		String formulae =makeFormulae("lm");

		//model<-formulae
		e = re.rniParse(formulae,1);
		r = re.rniEval(e,0);
		re.rniAssign("model", r, 0);	
		
		//anova(model)
		e = re.rniParse("capture.output(anova(model))",1);
		r = re.rniEval(e,0);
		x=new REXP(re, r);		
		re.jriWriteConsole("reprint from return anova\n");

		s =x.asStringArray();
		if (s!=null) {
			int i=0; while (i<s.length) {  
				re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n"); 
				if (s[i].indexOf("Response:")!=-1){
					for (int j=0; j<num_indep; j++)
						if (j>0)
							myXML.insertSiblingWithChildren("predictor", s[2+j+i]);
						else  myXML.insertChildren("predictor", s[2+j+i]);
					i++;	continue;
				}	
				if (s[i].indexOf("Residuals")!=-1){
					insert("residual_source", s[i]); 
					i++;	continue;
				}
				i++;
			}	//while	
		}
		else	
			{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		re.jriWriteConsole("done reprint anova!!!\n");	

		e = re.rniParse("capture.output(model$resi)",1);
		r = re.rniEval(e,0);
		x=new REXP(re, r);		
		re.jriWriteConsole("reprint from model$resi \n");
		s = x.asStringArray();
		if (s!=null) {
			int i=0; 
			while (i<s.length) { 
				re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				i++;}
			myXML.insert("residuals", s[1]); 	
		}

		 e = re.rniParse("capture.output(model$fit)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from model$fit \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; 
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				 i++;}
			 myXML.insert("fitted", s[1]); 	
		 }

		 do_plotdata();

		re.jriWriteConsole("======================\n");	
	}


	protected void do_ancova(Document t_xml){
		re.jriWriteConsole("========ancova_xml_file()==============\n");	
		if (DEBUG)	System.out.println("START: do_ancova");

		readInput();
		setInput();	
		//done with reading XML
		//prepare to write XML
		myXML.flush(t_xml);

		REXP x;
		String[] s;	
		long e, r;
	
		//model <- lm(y~x)
		String formulae= makeFormulae("lm");

		e = re.rniParse(formulae,1);
		r = re.rniEval(e,0);
		re.rniAssign("model", r, 0);	
		
		//summary(aov(model));
		e = re.rniParse("capture.output(summary(aov(model)))",1);
		r = re.rniEval(e,0);
		x=new REXP(re, r);		
		re.jriWriteConsole("reprint from summary(aov(model))\n");
		
		s =x.asStringArray();
		int i=0; while (i<s.length) {  
			re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");	
			if (s[i].indexOf("Residuals")!=-1){
				myXML.insertChildren("residual_source", s[i].substring("residuals".length()+4)); 
				i++;	continue;
			}
			i++;	
		}
		for (int j=0; j<num_indep; j++)
			if (j>0)
				myXML.insertSiblingWithChildren("predictor", s[1+j]);
			else  myXML.insertChildren("predictor", s[1+j]);

		 e = re.rniParse("capture.output(model$fitted)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from model$fitted \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i+1]+"\"\n");
				  if (i>0)
					 sb.append(" "+s[i+1]);
				 else sb.append(s[i+1]);
				 i+=2;}
			 myXML.insert("fitted", sb.toString()); 	
		 }
		 
		 e = re.rniParse("capture.output(model$resi)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from model$resi \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+(i+1)+"] \""+s[i+1]+"\"\n");
				 if (i>0)
					 sb.append(" "+s[i+1]);
				 else sb.append(s[i+1]);
				 i+=2;}
			 myXML.insert("residuals", sb.toString()); 	
		 }
		
		do_plotdata();

		re.jriWriteConsole("======================\n");	

	}

	protected String makeFormulae(String command){
		StringBuffer formulae =new StringBuffer();


		formulae.append(command+"("+dep_name+"~");
		for (int i=0; i<num_indep; i++) {
			if (i>0)
				formulae.append("+"+indep_name[i]);	
			else formulae.append(indep_name[i]);
		}

		if 	(num_interaction >0){
			String[] s;	
			for (int i=0; i<num_interaction; i++){
				formulae.append("+");
				s = interaction[i].split(",( )?");	
				for(int j=0; j<Array.getLength(s); j++)
				if (j>0)	
					formulae.append("*"+s[j]);
				else	 formulae.append(s[j]);	
			}
		}
		if (command.equals("glm")){
			formulae.append(", family=binomial(link=logit)");
		}
		if (command.equals("arima")){
			formulae =new StringBuffer();
			formulae.append(command+"("+dep_name);
			formulae.append(", order=c("+parameter_name[0]+","+parameter_name[1]+","+parameter_name[2]+")");
		}

		formulae.append(")");
		if (DEBUG)	 System.out.println("formulae ="+formulae.toString());
		return formulae.toString();	

	}

	protected void do_plotdata(){
		long e,r;
		REXP x;	
		String[] s;
	
		 //Y2<-qqnorm(resi(model),plot=FALSE)$y
		 //X<-qqnorm(resi(model),plot=FALSE)$x
		 //Y1 <-Y2/sd(Y2)
		 e = re.rniParse("capture.output(qqnorm(resid(model),plot=FALSE)$x)",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from qqnorm(resid(model),plot=FALSE)$x \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n");
				 if (i>0) sb.append(" "+s[i].substring(5));
				 else sb.append(s[i].substring(5));
				 i++;}
			 myXML.insert("theoretical_quantiles", sb.toString()); 	
		 }

		 e = re.rniParse("qqnorm(resid(model),plot=FALSE)$y",1);
		 r = re.rniEval(e,0);
		 re.rniAssign("Y2", r, 0);
		 e = re.rniParse("capture.output(Y2/sd(Y2))",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from Y2/sd(Y2) \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+(i+1)+"] \""+s[i+1]+"\"\n");
				 if (i>0) sb.append(" "+s[i+1]);
				 else sb .append(s[i+1]);
				 i+=2;}
			 myXML.insert("standardized_residuals", sb.toString()); 	
		 }
			
		 //s<-sqrt(deviance(model)/df.residual(model))
		 //r<-resid(model)
		 //hii<-lm.influence(model,do.coef=FALSE)$hat
		 //rs <- resid(model)/(s*sqrt(1-hii))   
		 //sqrt(abs(rs))
		 e = re.rniParse("sqrt(deviance(model)/df.residual(model))",1);
		 r = re.rniEval(e,0);
		 re.rniAssign("s", r, 0);
		 e = re.rniParse("resid(model)",1);
		 r = re.rniEval(e,0);
		 re.rniAssign("r", r, 0);
		 e = re.rniParse("lm.influence(model,do.coef=FALSE)$hat",1);
		 r = re.rniEval(e,0);
		 re.rniAssign("hii", r, 0);
		 e = re.rniParse("resid(model)/(s*sqrt(1-hii))",1);
		 r = re.rniEval(e,0);
		 re.rniAssign("rs", r, 0);

		 e = re.rniParse("capture.output(sqrt(abs(rs)))",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from sqrt(abs(rs)) \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+(i+1)+"] \""+s[i+1]+"\"\n");
				 if (i>0) sb.append( " "+ s[i+1]);
				 else sb.append(s[i+1]);
				 i+=2;}
			 myXML.insert("root_standardized_residuals", sb.toString()); 	
		 }
		
		 //cooks.distance(model, sd =s, res=r)
		 e = re.rniParse("capture.output(cooks.distance(model,sd=s,res=r))",1);
		 r = re.rniEval(e,0);
		 x=new REXP(re, r);		
		 re.jriWriteConsole("reprint from cooks.distance \n");
		 s = x.asStringArray();
		 if (s!=null) {
			 int i=0; StringBuffer sb = new StringBuffer();
			 while (i<s.length) { 
				 re.jriWriteConsole("["+i+"] \""+s[i+1]+"\"\n");
				 if (i>0) sb.append(" "+s[i+1]);
				 else  sb.append(s[i+1]);
				 i+=2;}
			 myXML.insert("cooks_distance", sb.toString()); 	
		 }

	}

	protected void insert(String nName, String value){

		String[] st = value.split(" +");

		StringBuffer  sb = new StringBuffer();
		
		/*if (DEBUG)
			{ System.out.println("nName ="+nName+" value="+value);
			    for 	(int i=0; i<Array.getLength(st); i++)
					System.out.println("i="+i +" :"+st[i]);
					}*/
		// for both

		// for lm
		if (nName.equalsIgnoreCase("residual_standard_error")){
			sb.append(st[3]);
			sb.append (" ");
			sb.append(st[5]);
			myXML.insertChildren(nName,sb.toString());
			return;
		}
		if (nName.equalsIgnoreCase("f_statistics")){
			sb.append(st[1]);
			sb.append(" ");
			sb.append(st[3]);
			sb.append(" ");
			sb.append(st[5]);
			sb.append(" ");
			sb.append(st[8]);
			myXML.insertChildren(nName,sb.toString());
			return;
		}
		// for anova
		if (nName.equalsIgnoreCase("residual_source")){
			int l = "residuals".length()+2;
			myXML.insertChildren(nName,value.substring(l));
			return;
		}
	}//insert

	protected int[] parseInt(String[] ss) throws java.lang.NumberFormatException{
		int k 	= Array.getLength(ss);
		int ii[] = new int[k];
		
		try{
			for (int i=0; i<k; i++){
				//System.out.println("parseInt i="+i+" ss[i]" + ss[i]);
				ii[i] = Integer.parseInt(ss[i]);
			}
			return ii;		
		}catch(java.lang.NumberFormatException e ){
			throw e;
		}	
   	}

	protected double[] parseNumber(String[] ss) {
		int k 	= Array.getLength(ss);
		double ii[] = new double[k];
		
		for (int i=0; i<k; i++){
			//System.out.println("parseNumber i="+i+" ss[i]" + ss[i]);
			ii[i] = Double.parseDouble(ss[i]);
			}
			return ii;		
	}

	//** Prints the specified elements in the given document. 
		public void print(Rengine re,  NodeList elements){
                             
        // is there anything to do?
        if (elements == null) {
            return;
        }

        // print all elements
		int elementCount = elements.getLength();
		if 	(elementCount == 0)
			return;	

		System.out.println("ChildNodes count:"+elementCount);

		for (int i = 0; i < elementCount; i++) {
 			Node el = elements.item(i);

			int type = el.getNodeType();
			String nName = el.getNodeName();
			String 	nValue = el.getNodeValue();

			switch(type){
			case Node.ELEMENT_NODE:{
				System.out.println(i+" type = ele:"+type+":" +nName+":"+nValue);		
				printEl(re, (Element)el);
				break;		
			}
			case Node.TEXT_NODE:{
				System.out.println(i+" type = TEXT:"+type+":"+nName+":"+nValue );
				//leaf	
				if (elementCount == 1)
					re.jriWriteConsole(nValue+"\n");	
				break;
			}
			case Node.DOCUMENT_TYPE_NODE	: {
				System.out.println(i+" type = docType:"+type+":"+nName+":"+nValue );	
				break;
			}
			default:	{
				System.out.println(i+" type ="+type+":"+nName+":"+nValue );	
				break;
			}	
			}
		}
      
    } // print(PrintWriter,Document,String,String)

    //
    // Protected static methods
    //
    //** Prints the specified element. 
    protected  void printEl(Rengine re, Element element) {

        re.jriWriteConsole("<");
        re.jriWriteConsole(element.getNodeName());
        re.jriWriteConsole(">");

		re.jriWriteConsole("\n");	
		print(re, element.getChildNodes());
		return;
		
    } // print(Rengine,Element,NamedNodeMap)
	
   protected String getTimeSeriesResult(String[] s) {

	   int i=0; 
	   StringBuffer res = new StringBuffer();
	   int start=0;
	   while (i<s.length) { 
		   re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n"); 
		   if (s[i].indexOf("Frequency")!=-1){
			   start=1; i++;continue;
		   }
		   if(start!=0)
			   res.append(s[i].substring(s[i].lastIndexOf("]")+1)+" ");
		   i++;
			}	//while	
	   if (DEBUG)  
		   System.out.println("result="+res.toString()+"\n\n");
	   return res.toString();
   }

    /** Normalizes the given string. */
    protected String normalize(String s) {
        StringBuffer str = new StringBuffer();

        int len = (s != null) ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch (ch) {
            case '<': {
                    str.append("&lt;");
                    break;
                }
            case '>': {
                    str.append("&gt;");
                    break;
                }
            case '&': {
                    str.append("&amp;");
                    break;
                }
            case '"': {
                    str.append("&quot;");
                    break;
                }
            case '\r':
            case '\n': {
                    str.append("&#");
                    str.append(Integer.toString(ch));
                    str.append(';');
                    break;
                }
            default: {
                    str.append(ch);
                }
            }
        }

        return str.toString();

    } // normalize(String):String


	//use the xml package within R 
	// not done yet
	protected  void lm_xml(Rengine re){
		System.out.println("========lm()==============");	
			 // x<-(10,20,30,40,50,10,20,30,40,50)
			 //	y<-(100,120,130,140,150,110,120,140,140,160)

		 
		 int xx[] = {10,20,30,40,50,10,20,30,40,50};
		 int yy[] = {100,120,130,140,150,110,120,140,140,160};
		 long x1 = re.rniPutIntArray(xx);
		 re.rniAssign("x", x1, 0);
         long y1 = re.rniPutIntArray(yy);
		 re.rniAssign("y", y1, 0);

		 //model <- lm(y~x)
		 long e = re.rniParse("lm(y~x)",1);
		 long r = re.rniEval(e,0);
		 // re.eval("lm(y~x)");
		  re.rniAssign("model", r, 0);
		
		//library(XML)	load XML package
		  e = re.rniParse("library(XML)",1);	
		  r = re.rniEval(e,0);
		  System.out.println("XML library loaded!!");

		  re.eval("mylm_result<-summary(model)");

		  e = re.rniParse("capture.output(mylm_result)",1);	
		  r = re.rniEval(e,0);

		  //re.eval("capture.output(summary(model))");

		  REXP x=new REXP(re, r);		
		  System.out.println("reprint from return summary");
		  String  s[] =x.asStringArray();
		  
		  if (s!=null) {
			  int i=0; while (i<s.length) { System.out.println("["+i+"] \""+s[i]+"\""); i++; }
			  }	
		  else	{System.out.println("s==null!!!!!!!");}	
		  System.out.println("done reprint summary!!!");	
			
		  System.out.println("======================");	

	}	//end of lm_xml

	//a plain hard coded lm
	protected  void lm(){
		 re.jriWriteConsole("========lm()==============\n");	
			 // x<-(10,20,30,40,50,10,20,30,40,50)
			 //	y<-(100,120,130,140,150,110,120,140,140,160)
		 int xx[] = {10,20,30,40,50,10,20,30,40,50};
		 int yy[] = {100,120,130,140,150,110,120,140,140,160};
		 long x1 = re.rniPutIntArray(xx);
		 re.rniAssign("x", x1, 0);
         long y1 = re.rniPutIntArray(yy);
		 re.rniAssign("y", y1, 0);

		 //model <- lm(y~x)
		 long e = re.rniParse("lm(y~x)",1);
		 long r = re.rniEval(e,0);
		 // re.eval("lm(y~x)");
		  re.rniAssign("model", r, 0);
		
		  //lm_result<-summary(model)
		  e = re.rniParse("capture.output(summary(model))",1);
		  r = re.rniEval(e,0);
		  //re.eval("capture.output(summary(model))");
		  // re.rniAssign("lm_result", r, 0);	
		  REXP x=new REXP(re, r);		
		   re.jriWriteConsole("reprint from return summary\n");
		  String  s[] =x.asStringArray();
		  if (s!=null) {
			  int i=0; while (i<s.length) {  re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n"); i++; }
			  }	
		  else	{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		   re.jriWriteConsole("done reprint summary!!!\n");	
		  
		  //model[]
		  e = re.rniParse("capture.output(model[])",1);
		  r = re.rniEval(e,0);
		  //re.eval("capture.output(model[])");			 
		  x=new REXP(re, r);		
		   re.jriWriteConsole("reprint from return model[]:\n");
		  s =x.asStringArray();
		  if (s!=null) {
			  int i=0; while (i<s.length) {  re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n"); i++; }
			  }	
		  else	{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		   re.jriWriteConsole("done reprint(model[])\n");	
	
		   re.jriWriteConsole("======================\n");	

	}	//end of lm

	protected void anova(){
		int xx[] = {1,1,1,1,1,2,2,2,2,2};
		int yy[] = {100,120,130,140,150,120,120,140,140,160};
		long x1 = re.rniPutIntArray(xx);
		re.rniAssign("x", x1, 0);
		long y1 = re.rniPutIntArray(yy);
		re.rniAssign("y", y1, 0);

		//x<-factor(x)
		long e = re.rniParse("factor(x)",1);
		long r = re.rniEval(e,0);
		re.rniAssign("x", r, 0);	
 
		//model <- lm(y~x)
		e = re.rniParse("lm(y~x)",1);
		r = re.rniEval(e,0);
		re.rniAssign("model", r, 0);	
		
  //a_result<-anova(model)
		  e = re.rniParse("capture.output(anova(model))",1);
		  r = re.rniEval(e,0);
		  REXP x=new REXP(re, r);		
		  re.jriWriteConsole("reprint from return anova\n");
		  String  s[] =x.asStringArray();
		  if (s!=null) {
			  int i=0; while (i<s.length) {  re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n"); i++; }
			  }	
		  else	{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		   re.jriWriteConsole("done reprint anova!!!\n");	
		  
		  //model[]
		  e = re.rniParse("capture.output(model[])",1);
		  r = re.rniEval(e,0);
		  //re.eval("capture.output(model[])");			 
		  x=new REXP(re, r);		
		  re.jriWriteConsole ("reprint from return model[]:\n");
		  s =x.asStringArray();
		  if (s!=null) {
			  int i=0; while (i<s.length) {  re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n"); i++; }
			  }	
		  else	{ re.jriWriteConsole("s==null!!!!!!!\n");}	
		   re.jriWriteConsole("done reprint(model[])\n");	
	
	}//end of anova


	/*  public static String getSummary(Rengine re, RObject o) {
	   //    if (o.getType().equals("function")) return "<html><pre>"+getFunHelp(o.getRName())+"</pre></html>";
        String tip = "";
        String res[] = null;
        REXP x;
        try { x = re.idleEval("suppressWarnings(try(capture.output(summary("+(o.getRName())+")),silent=TRUE))"); } catch (Exception e) { return null;}
        if (x!=null && (res = x.asStringArray()) != null && !res[0].startsWith("Error")) {
            //tip = "<html><pre>";
            int l = -1;
            for (int i = ((l = res.length) > 10?10:l)-1; i >= 0; i--) {
                if (i < l-1) tip = res[i] +"<br>"+ tip;
                else tip = res[i]+"       ";
            }
            tip = "<html><pre>"+tip+(l > 10?"...":"")+"</pre></html>";
        }
        else return null;
        return tip.startsWith("<html><pre>Error")?null:tip;
		}*/


}//end of mytest class
