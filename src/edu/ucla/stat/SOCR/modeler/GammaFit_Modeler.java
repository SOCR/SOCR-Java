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

package edu.ucla.stat.SOCR.modeler;

//import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.modeler.gui.ModelerConstant;
import edu.ucla.stat.SOCR.util.*;
//import edu.ucla.stat.SOCR.modeler.*;
//import JSci.awt.*;
//import java.lang.*;
//import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

//import JSci.maths.*;
//import JSci.maths.statistics.*;
import edu.ucla.stat.SOCR.core.*;
/**This class Designs a simple Gamma model fitting curve. */
public class GammaFit_Modeler extends Modeler implements ItemListener  {
	public edu.ucla.stat.SOCR.distributions.GammaDistribution stdGamma;
	public float kernalVar = 0;
	private double minX = 0;
	private double maxX = 0;
	private int modelCt=1;
	public JTextField shapeParamField = new JTextField("1.0",3);
	public JLabel shapeParamLabel = new JLabel("Shape");
	public JTextField scaleParamField = new JTextField("1.0",3);
	public JLabel scaleParamLabel = new JLabel("Scale");

	private int numberOfQuantiles=100;

	// public DoubleVector coeffs;
	private int dataPts;
	public javax.swing.table.TableColumn clm2;
	private int runs = 0;
	public JTextField kernalVarField = new JTextField("1", 3);
	private JTextArea statsTable = new JTextArea(6, 25);
	private double mn = 0;
	private double sd = 1;
	private double[] modelX;
	private double[] modelY;
	private ButtonGroup buttonGroup = new ButtonGroup();
	public JCheckBox estimateParams = new JCheckBox("Estimate Parameters");
	public JCheckBox userParams = new JCheckBox("User Parameters",true);
	private int modelType = CONTINUOUS_DISTRIBUTION_TYPE;
	static boolean isContinuous = true;
	public static final int SLICE_SIZE = 4001;

  //  public JCheckBox rawCheck = new JCheckBox("Raw Data",false);
     /**This method initializes the applet, including the toolbar,
     * scatterplot, and statistics table.*/

    public GammaFit_Modeler() {
    }

    public GammaFit_Modeler(JPanel controlpanel) {
        controlpanel.add(estimateParams);
        controlpanel.add(userParams);
    	//SOCRModeler.addToGridBag(controlpanel, estimateParams, 0, 0, 1, 1, 1.0, 0.0);
        //SOCRModeler.addToGridBag(controlpanel, userParams, 0, 1, 1, 1, 1.0, 0.0);
        addParams(controlpanel);
        buttonGroup.add(estimateParams);
        buttonGroup.add(userParams);
        estimateParams.addItemListener(this);
        userParams.addItemListener(this);
        controlpanel.repaint();
        controlpanel.repaint();
    }



    /**This method resets the experiment, including the scatterplot,
     * the record table, and the statistics table.*/

    public void addParams(JPanel controlpanel) {
        controlpanel.add(shapeParamLabel);
        controlpanel.add(shapeParamField);
        controlpanel.add(scaleParamLabel);
        controlpanel.add(scaleParamField);
    	//SOCRModeler.addToGridBag(controlpanel,shapeParamLabel , 0, 2, 1, 1, 1.0, 0.0);
        //SOCRModeler.addToGridBag(controlpanel,shapeParamField , 0, 3, 1, 1, 1.0, 0.0);
        //SOCRModeler.addToGridBag(controlpanel,scaleParamLabel , 0, 4, 1, 1, 1.0, 0.0);
        //SOCRModeler.addToGridBag(controlpanel,scaleParamField , 0, 5, 1, 1, 1.0, 0.0);
    }

    public void registerObservers(ObservableWrapper o) {
        o.addJCheckBox(estimateParams);
        o.addJCheckBox(userParams);
        o.addJTextField(shapeParamField);
        o.addJTextField(scaleParamField);
        }

    public int getModelType() {
        return modelType;

    }

    public double getLowerLimit() {
      	//return Double.NEGATIVE_INFINITY;
      	return 0;
      }

      public double getUpperLimit() {
      	return Double.POSITIVE_INFINITY;
      }

     public void toggleParams(boolean istrue) {
        if(istrue) {

            shapeParamLabel.setVisible(true);
            shapeParamField.setVisible(true);
            scaleParamLabel.setVisible(true);
            scaleParamField.setVisible(true);
        }else {
            shapeParamLabel.setVisible(false);
            shapeParamField.setVisible(false);
            scaleParamLabel.setVisible(false);
            scaleParamField.setVisible(false);
        }
    }

    public double[] generateSamples( int sampleCount) {
        double[] dat = new double[sampleCount];
         stdGamma = new edu.ucla.stat.SOCR.distributions.GammaDistribution(Double.parseDouble(shapeParamField.getText()),Double.parseDouble(scaleParamField.getText()));
        for(int i = 0; i< sampleCount;i++)
            dat[i] = stdGamma.simulate();
        return dat;

    }
    public double[] returnModelX() {
        return modelX;
        }

        public double[] returnModelY() {
        return modelY;
        }

        public void itemStateChanged(ItemEvent event){
            if(event.getSource() == userParams || event.getSource() == estimateParams) {
             if(estimateParams.isSelected())
                 //estimateParams.
                 //javax.swing.JCheckbox kjd = new JCheckBox();

                 toggleParams(false);
             else
                 toggleParams(true);

           }
       }

        public int getModelCount() {
            return modelCt;
            }

	public void fitCurve(float[] rawDat, double minx, double maxx,JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
		fitCurve(rawDat, (float)minx, (float)maxx, resultPanelTextArea, rescaleClicked, scaleUp, initReset);
	}

	public void fitCurve(float[] rawDat, float minx, float maxx,
			JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
        double[] Gammadat;
        int datalength = 0;
        try{


            int count = 0;

            if(estimateParams.isSelected())
                stdGamma = new edu.ucla.stat.SOCR.distributions.GammaDistribution(rawDat);
            else
                stdGamma = new edu.ucla.stat.SOCR.distributions.GammaDistribution(Double.parseDouble(shapeParamField.getText()),Double.parseDouble(scaleParamField.getText()));


            resultPanelTextArea.setText("Shape = "+ Double.toString(stdGamma.getShape())+"\n Scale = " + Double.toString(stdGamma.getScale())+"\n\n");
            //float minx = 0;
           // float maxx = 1;
            float ind = 1;
           // minx = graph.getMinX();
           // maxx = graph.getMaxX();
		int number = SLICE_SIZE; // number of delta x. was 20
		ind = (maxx-minx)/(number - 2);

            modelX = new double[number];
            modelY = new double[number];
            for(int i = 0; i<number ;i++) {
                modelX[i] = minx+ind*i;
                modelY[i] = stdGamma.getDensity(modelX[i]);
                //if (modelY[i] == Double.POSITIVE_INFINITY) {
			 //	 modelY[i] = 190;
			 //}
                if (i < (int) (SLICE_SIZE/100)) {
			 	//System.out.println("GammaFit_Modeler modelX["+i+"] = " + modelX[i] + " modelY["+i+"] = " + modelY[i]);
			}

            }
            double maxy = modelY[0];
            for(int i = 0; i<modelY.length;i++){
                if(modelY[i]>maxy)
                    maxy = modelY[i];
            }
           // double dataMaxy = (double) graph.getMaxY();

            //System.out.println("X0 "+modelX[0]);
            //System.out.println("X1 "+modelX[1]);
            //System.out.println("y0 "+modelY[0]);
            //System.out.println("y1 "+modelY[1]);
            //graph.setModel(modelX.length, modelX, modelY);

    		double[] temp = new double [rawDat.length];
    		double[] x = new double[numberOfQuantiles];
    		double[] y = new double[numberOfQuantiles];
    			//	 Model Quantiles
    		for (int i = 0; i < numberOfQuantiles; i++)  y[i] = stdGamma.getQuantile((i+0.5)/numberOfQuantiles);
    			//	 Data Quantiles
    		for (int i= 0; i<rawDat.length; i++) temp[i] = (double)rawDat[i];
    		x = edu.ucla.stat.SOCR.util.AnalysisUtility.getQuantileArray(temp);
    		resultPanelTextArea.append(getKSModelTestString(stdGamma.getName(), numberOfQuantiles, x, y));
            
            
        } catch(Exception e) {
            e.printStackTrace();
            resultPanelTextArea.setText(e.getMessage());
        }




    }

    public String getDescription() {
        String desc = new String();
        desc = "none";
        return desc;
        }
        /** return the instructions for using this modeler*/
    	public String getInstructions() {
    		String instructions = new String();
    	    instructions = "1. none\n 2. none";
    	    return instructions;
    	}
    	/** return the references for this modeler*/
    	public String getResearch() {
    		String research = new String();
    	    research = "none";
    	    return research;
    	}

    	public boolean isContinuous() {
		return this.isContinuous;
	}
	public double getGraphLowerLimit() {
		return 0;
	}
	public double getGraphUpperLimit() {
		return ModelerConstant.GRAPH_UPPER_LIMIT;
	}
	public boolean useInitButton() {
		return false;
	}
}





