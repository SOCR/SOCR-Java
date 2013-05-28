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
package edu.ucla.stat.SOCR.analyses.gui;

import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**A Multi-Way ANOVA Analysis. */
public class CategoricalAnalysis extends Analysis {


	// This ANALYSIS specific Actions:
	protected Action Example1Action;
  	protected Action Example2Action;
  	protected Action Example3Action;
  	protected Action Example4Action;
	protected Action ComputeAction;
  	protected Action ClearAction;

	//objects
	private JToolBar toolBar;

	private Frame frame;

	/**Initialize the Analysis*/
	public void init(){
		super.init();
		frame = getFrame(this);
		setName("ANOVA Statistical Analysis");

		//Distribution
		 edu.ucla.stat.SOCR.core.Distribution distribution = new FisherDistribution();

		// Create the toolBar
    		toolBar = new JToolBar();
    		createActionComponents(toolBar);

		controlPanel.add(toolBar);

		validate();
		reset();
	}


	/** Create the actions for the buttons */
       protected void createActionComponents(JToolBar toolBar){
           JButton button = null;

           // FIRST button for one-way example
           Example1Action = new AbstractAction(){
           public void actionPerformed(ActionEvent e) {
               	// Create First Example
               	ANOVAExampleData Example1 = new ANOVAExampleData(1,0);
     		   	dataTable = Example1.getExample();
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));
                }
              };

		button = toolBar.add(Example1Action);
       	button.setText("Example 1");
       	button.setToolTipText("This is a One-Way ANOVA Example");

       	// SECOND button for Two-way example
           Example2Action = new AbstractAction(){
           	   public void actionPerformed(ActionEvent e) {
               	// Create Second Example
               	int c = (int) (Math.random()*2) + 2;
               	ANOVAExampleData Example2 = new ANOVAExampleData(c,0);
		   	dataTable = Example2.getExample();
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));
                }
            };

		button = toolBar.add(Example2Action);
       	button.setText("Example 2");
       	button.setToolTipText("This is a Two-Way ANOVA Example");

		// THIRD button for Two-way example
           Example3Action = new AbstractAction(){
               public void actionPerformed(ActionEvent e) {
               	// Create Third Example
               	ANOVAExampleData Example3 = new ANOVAExampleData(3,0);
			dataTable = Example3.getExample();
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));
               }
           };

		button = toolBar.add(Example3Action);
       	button.setText("Example 3");
       	button.setToolTipText("This is another Two-Way ANOVA Example");

		// FORTH button for Two-way example
           Example4Action = new AbstractAction(){
              public void actionPerformed(ActionEvent e) {
               	// Create Fourth RANDOM Example
               	ANOVAExampleData Example4 = new ANOVAExampleData(4,0);
			dataTable = Example4.getExample();
			dataPanel.removeAll();
		   	dataPanel.add(new JScrollPane(dataTable));
               }
            };

       	button = toolBar.add(Example4Action);
       	button.setText("Random Example");
       	button.setToolTipText("This is a RANDOMLY GENERATED Two-Way ANOVA Example");

       	// Third button for computing the result of ANOVA
       	ComputeAction = new AbstractAction(){
           	   public void actionPerformed(ActionEvent e) {
               	resultPanelTextArea.setText("Calculating...");

			resultPanelTextArea.setText("Data is:\n");
			for (int k =0; k < dataTable.getRowCount(); k++)
			{  for (int i = 0; i < dataTable.getColumnCount();  i++)
			   {	resultPanelTextArea.append(
					(String)dataTable.getValueAt(k, i)+" ");
			    }
			   resultPanelTextArea.append("\n");
			 }
			resultPanelTextArea.append("\nResults are:\n\n\n");
		   	doAnalysis();
               }
             };
   /*		button = toolBar.add(ComputeAction);
       	button.setText("Compute");
       	button.setToolTipText("Press it to compute ANOVA");

       	// Fourth button for clearing both windows
      	ClearAction = new AbstractAction(){
           		public void actionPerformed(ActionEvent e) {
  		   		reset();	// Need to work out what this means
               		resultPanelTextArea.setText("");
               	}
            };

       	button = toolBar.add(ClearAction);
       	button.setText("Clear");
       	button.setToolTipText("Clears All Windows"); */
      }


	/**This method sets up the analysis protocol, when the applet starts*/
	public void start(){

	}

	/**This method defines the specific statistical Analysis to be carried our on
		the user specified data. ANOVA is done in this case. */
	public void doAnalysis(){
		String d = dataText;

		int i, j, k;
		// Putting the two variables that this function needs here
        	final double zero = 0.00001;
        	String newln = System.getProperty("line.separator");

		Matrix All;
		try { All = new Matrix(dataTable);
		}
		catch (Exception e) { return; }

		Matrix b = All.sub(0,All.rows-1,0,0);	// dependent in first column
		Matrix A = All.sub(0,All.rows-1, 1, All.columns-1);	// factors
		double grandMean = b.average();
		double grandSum = b.sum(); // grand sum of observations
		int[] l = new int[3]; // vector to store number of partitions for each factor

		double[] SS = new double[3];			// Sum of squares/factor&interaction
		int[] FC = new int[3];				// FC = factor counter
		int[] DF = new int[3];				// DF = degrees of freedom
		double[] MS = new double[3];			// MS = mean square
		double[] F = new double[3];			// F value/factor
		double SST = 0;					// SST = total sum of squares
		double SSE = 0;					// error sum of squares
		double DFE = 0;					// error d.f.
		double MSE = 0;					// mean square error
		Matrix a;						// a working column vector

		// get the number of levels for each column
		for (j=0; j<A.columns; j++)  {
	    		a = new Matrix(A.sub(0, A.rows-1, j, j));	// get the j-th column of A
	    		l[j] = (int)a.max();
		}

		// fix for one-way:
		if(A.columns==1)  l[1] = 1;

		// now calculate the total sum of squares
		for(i=0; i<b.rows; i++)
	    		SST = SST + Math.pow((b.element[i][0] - grandMean),2);

		// now calculate the SS for each factor (and interactions)
		// right now, this is one or two-way only code--need to generalize
		double [] TSS = new double[3];	// temp sum
		for(i=1; i<=l[0]; i++){
	    	   for(j=1; j<=l[1]; j++){
			for(k=0; k<b.rows; k++){
		    		if(A.element[k][0]==i){
					TSS[0]=TSS[0]+b.element[k][0];
					FC[0]++;
		    		}
		    		if(A.columns>1){
					if(A.element[k][1]==j){
			    			TSS[1]=TSS[1]+b.element[k][0];
			    			FC[1]++;
					}
					if((A.element[k][0]==i)&&(A.element[k][1]==j)){
			    			TSS[2]=TSS[2]+b.element[k][0];
			    			FC[2]++;
					}
		    		}
			   }
			if((j==1)&&(FC[0]!=0)){SS[0] = SS[0] + (TSS[0]*TSS[0])/FC[0];}
			if((i==1)&&(FC[1]!=0)){SS[1] = SS[1] + (TSS[1]*TSS[1])/FC[1];}
			if(FC[2]!=0){SS[2] = SS[2] + (TSS[2]*TSS[2])/FC[2];}
			TSS[0] = 0; TSS[1] = 0; TSS[2] = 0;
			FC[0] = 0; FC[1] = 0; FC[2] = 0;
	    	   }
	    	}

		// cacluate the interaction total SS:
		double gst = Math.pow(grandSum,2);
		SS[0] = SS[0] - gst/b.rows;
		if(A.columns>1){
	    		SS[1] = SS[1] - gst/b.rows;
	    		SS[2] = SS[2] - gst/b.rows;
	    		SS[2] = SS[2] - SS[1] - SS[0];
		}

		SSE = SST - SS[0] - SS[1] - SS[2];
		DF[0] = l[0] - 1;
		DF[1] = l[1] - 1;
		DF[2] = l[0]*l[1] - 1 - (l[0] - 1 + l[1] - 1);
		DFE = b.rows - 1 - DF[2] - DF[1] - DF[0];

		if(DFE==0){    // adjust for no replication 2 way
	    		DFE = DF[2];
	    		SSE = SST - SS[0] - SS[1];
		}

		MS[0] = SS[0]/DF[0];
		MS[1] = SS[1]/DF[1];

		if(DF[2]!=0) MS[2] = SS[2]/DF[2];
		MSE = SSE/DFE;
		F[0] = MS[0]/MSE;
		F[1] = MS[1]/MSE;
		F[2] = MS[2]/MSE;

		// build the report
		edu.ucla.stat.SOCR.core.Distribution distribution = new FisherDistribution(DF[0], (int)DFE);
		double fScore = distribution.getCDF((double)F[0]);
			fScore = 1-fScore;	//Right Tail ...

		String cs = "Analysis of Variance Results" + newln +
	        		"---------------------------------------" + newln ;
		cs = cs + monoString("Factor")+monoString("d.f.")+monoString("SS")+
	    		monoString(" F")+monoString(" P(X>F)")+ newln;
		cs = cs + monoString("  1")  + monoString(DF[0])  + monoString(SS[0]) +
	    		monoString(F[0])  +
			monoString(fScore) + newln;
		if(A.columns==2){
			distribution = new FisherDistribution(DF[1], (int)DFE);
					fScore = distribution.getCDF((double)F[1]);
			cs = cs + monoString("  2") + monoString(DF[1])  +
			monoString(SS[1]) + monoString(F[1])  +
						monoString(fScore) + newln;

			if(DF[2]!=DFE){
			 distribution = new FisherDistribution(DF[2], (int)DFE);
						fScore = distribution.getCDF((double)F[2]);
						cs = cs + monoString("Interaction")+
						monoString(DF[2]) +
						monoString(SS[2])+ monoString(F[2]) +
								monoString(fScore) + newln;
	    		}
		}

		cs = cs + monoString("Error") + monoString(DFE)  +
			monoString(SSE) + newln + newln;
		cs = cs +"Grand Mean:        	" + grandMean  + newln;
		cs = cs +"Number of Factors: 	" + A.columns +  newln;
		cs = cs +"Total sum of squares: " + SST +  newln;
		resultPanelTextArea.append(cs);
	}

	/** convert a generic string s to a fixed length one. */
    	public String monoString(String s) {
		String sAdd = new String(s + "                                      ");
		return sAdd.substring(0,14);
    	}

	/** convert a generic double s to a "nice" fixed length string */
    	public String monoString(double s) {
		final double zero = 0.00001;
        	Double sD = new Double(s);
		String sAdd = new String();
		if(s>zero)
	    		sAdd = new String(sD.toString());
		else  sAdd = "<0.00001";

		sAdd=sAdd.toLowerCase();
		int i=sAdd.indexOf('e');
		if(i>0)
	    		sAdd = sAdd.substring(0,4)+"E"+sAdd.substring(i+1,sAdd.length());
		else if(sAdd.length()>10)
				sAdd = sAdd.substring(0,10);

		sAdd = sAdd + "                                      ";
		return sAdd.substring(0,14);
    	}

	/** convert a generic integer s to a fixed length string */
    	public String monoString(int s) {
		Integer sD = new Integer(s);
		String sAdd = new String(sD.toString());
		sAdd = sAdd + "                                      ";
		return sAdd.substring(0,14);
    	}


	/**This method resets the Analysis, including the record table, the data,
	the random variables, and the graphs and tables for the selected variables.*/
	//public void reset(){
  		//getRecordTable().append("\tY\tM\tU\tV\tZ");
  		//diceBoard.showDice(0);
		//for (int i = 0; i < 5; i++) rv[i].reset();
		//rvGraph.reset();
		//rvTable.reset();
	//}

	/**This method updates the display, including the record table, the dice,
	and the graph and table for the selected variable*/
	public void update(){
		//diceBoard.showDice(n);
		//getRecordTable().append("\t" + sum + "\t" + format(average) + "\t" + min + "\t" + max + "\t" + aces);
		//rvGraph.repaint();
		//rvTable.update();
	}

	/**This method handles the events associated with the die probabilities
	button.*/
//	public void actionPerformed(ActionEvent e){
		/** if (e.getSource() == dieButton){
			Point fp = frame.getLocationOnScreen(), dp;
		}*/
//	}

}

