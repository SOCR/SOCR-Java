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
import java.io.*;

import java.awt.*;
import javax.swing.*;

import edu.ucla.stat.SOCR.JRI.Rengine;
import edu.ucla.stat.SOCR.JRI.REXP;
import edu.ucla.stat.SOCR.JRI.RMainLoopCallbacks;

class TextConsole2 implements RMainLoopCallbacks
{
    JFrame f;
	
	boolean R_VERBOSE= true;

	public void setVerbose(boolean v){
		R_VERBOSE =v;
	}

    public JTextArea textarea = new JTextArea();

    public TextConsole2() {
        f = new JFrame();
        f.getContentPane().add(new JScrollPane(textarea));
        f.setSize(new Dimension(800,600));
      //  f.show();
    }

    public void rWriteConsole(Rengine re, String text) {
				
       if (R_VERBOSE) textarea.append(text);
    }
    
    public void rBusy(Rengine re, int which) {
        if(R_VERBOSE) System.out.println("rBusy("+which+")");
    }
    
    public String rReadConsole(Rengine re, String prompt, int addToHistory) {
        System.out.print(prompt);
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            String s=br.readLine();
            return (s==null||s.length()==0)?s:s+"\n";
        } catch (Exception e) {
            System.out.println("jriReadConsole exception: "+e.getMessage());
        }
        return null;
    }
    
    public void rShowMessage(Rengine re, String message) {
        System.out.println("rShowMessage \""+message+"\"");
    }
    
    public String rChooseFile(Rengine re, int newFile) {
	FileDialog fd = new FileDialog(f, (newFile==0)?"Select a file":"Select a new file", (newFile==0)?FileDialog.LOAD:FileDialog.SAVE);
//	fd.show();
	String res=null;
	if (fd.getDirectory()!=null) res=fd.getDirectory();
	if (fd.getFile()!=null) res=(res==null)?fd.getFile():(res+fd.getFile());
	return res;
    }
    
    public void   rFlushConsole (Rengine re) {
	}
    
    public void   rLoadHistory  (Rengine re, String filename) {
    }			
    
    public void   rSaveHistory  (Rengine re, String filename) {
    }			
}
/*
public class rtest2 {
    public static void main(String[] args) {
        System.out.println("Press <Enter> to continue (time to attach the debugger if necessary)");
        try { System.in.read(); } catch(Exception e) {};
        System.out.println("Creating Rengine (with arguments)");
		Rengine re=new Rengine(args, true, new TextConsole2());
        System.out.println("Rengine created, waiting for R");
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }
	
		System.out.println("Letting go; use main loop from now on");
		lm(re);
		anova(re);
    }

	protected static void lm(Rengine re){
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
	
		  re.jriWriteConsole("\n=============reprint from return summary==========\n");
		  String  s[] =x.asStringArray();
		  if (s!=null) {
			  int i=0; while (i<s.length) { re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n"); i++; }
			  }	
		  else	{re.jriWriteConsole("s==null!!!!!!!");}	
		  re.jriWriteConsole("============done reprint summary!!!=================\n");	
		  
		  //model[]
		  e = re.rniParse("capture.output(model[])",1);
		  r = re.rniEval(e,0);
		  //re.eval("capture.output(model[])");			 
		  x=new REXP(re, r);		
		  re.jriWriteConsole("=========reprint from return model[]:=============\n");
		  s =x.asStringArray();
		  if (s!=null) {
			  int i=0; while (i<s.length) { re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n"); i++; }
			  }	
		  else	{re.jriWriteConsole("s==null!!!!!!!");}	
		  re.jriWriteConsole("=========done reprint(model[])=========\n");	
	
		  re.jriWriteConsole("===============================================\n");	

	}	//end of lm

	protected static void anova(Rengine re){

		re.jriWriteConsole("\n========anova()==============\n");	
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
		  re.jriWriteConsole("\n=============reprint from return anova(model)==========\n");
		  String  s[] =x.asStringArray();
		  if (s!=null) {
			  int i=0; while (i<s.length) { re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n"); i++; }
			  }	
		  else	{re.jriWriteConsole("s==null!!!!!!!");}	
		  re.jriWriteConsole("=========done reprint anova!!!====================\n");	
		  
		  //model[]
		  e = re.rniParse("capture.output(model[])",1);
		  r = re.rniEval(e,0);
		  //re.eval("capture.output(model[])");			 
		  x=new REXP(re, r);		
		  re.jriWriteConsole("============reprint from return model[]:=============\n");
		  s =x.asStringArray();
		  if (s!=null) {
			  int i=0; while (i<s.length) { re.jriWriteConsole("["+i+"] \""+s[i]+"\"\n"); i++; }
			  }	
		  else	{re.jriWriteConsole("s==null!!!!!!!\n");}	
		  re.jriWriteConsole("=============done reprint(model[])===============\n");	
	
	}//end of anova


}
*/
