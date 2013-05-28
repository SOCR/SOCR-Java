// package edu.ucla.stat.SOCR.Graph
package JSci.awt;

import java.awt.*;
import java.awt.event.*;
import JSci.awt.*;
import JSci.maths.*;
import java.applet.Applet;
import javax.swing.*;
import JSci.maths.statistics.*;

/**
* Sample program demonstrating use of LinearMath.leastSquaresFit method
* and the LineTraceModel graph class for Polynomial Fitting.
* @author Ivo Dinov
* @version 1.0
*/
public final class CurveFitterApplet extends JApplet {
        private JLabel fnLabel=new JLabel("y = ?", SwingConstants.CENTER);
        private LineTraceModel1 graph=new LineTraceModel1(-10.0f,10.0f,-10.0f,10.0f);
        private JButton fitButton=new JButton("Polynomial Fit");
        private JButton clearButton=new JButton("Clear Display");
	  private JTextArea resultingFit = new JTextArea(5, 40);
	  private JTextField polyDegreeField = new JTextField("4", 3);
	  private JLabel degreeLabel = new JLabel ("Model Degree");

	  public int poly_degree = 1;

	  public double data[][];
	  public double modelX[], modelY[];
	  public Graph2DModel model;
	  public DoubleVector coeffs;

    	private boolean inAnApplet = true;
     	//Hack to avoid ugly message about system event access check.
    	public CurveFitterApplet() {
        this(true);
    	}

    	public CurveFitterApplet(boolean inAnApplet) {
        this.inAnApplet = inAnApplet;
        if (inAnApplet) {
            getRootPane().putClientProperty("defeatSystemEventQueueCheck",
                                            Boolean.TRUE);
        }
    	}

 
    public void init() {
        setContentPane(makeContentPane());
    }

    public Container makeContentPane() {
	  JPanel pane = new JPanel();
        //pane.setBackground(new Color(255,255,204));
        //pane.setBorder(BorderFactory.createMatteBorder(1,1,2,2,Color.black));
	  pane.setLayout(new BorderLayout());

		    	resultingFit.setFont(new Font("TimesRoman", Font.BOLD+Font.ITALIC, 14));
			JScrollPane areaScrollPane = new JScrollPane(resultingFit);
        			areaScrollPane.setPreferredSize(new Dimension(250, 90));
        			areaScrollPane.setBorder(
            			BorderFactory.createCompoundBorder(
                				BorderFactory.createCompoundBorder(
                                		BorderFactory.createTitledBorder("Results"),
                                		BorderFactory.createEmptyBorder(5,5,5,5)),
                				areaScrollPane.getBorder()));

                fitButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                                fitCurve();
                        }
                });
                clearButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                                graph.clear();
					  resultingFit.setText("Use the mouse to draw a curve and click FIT to get a\nResulting Polynomial Fit:");
					  model = null;
					  //graph.modelY = null;
                        }
                });

		    resultingFit.setRows(2);
		    resultingFit.setText("Use the mouse to draw a curve and click FIT to get a\nResulting Polynomial Fit:");
		    pane.add(areaScrollPane, "North");
                pane.add(graph,"Center");
                Panel buttonPanel=new Panel();

		    buttonPanel.add(degreeLabel);
		    buttonPanel.add(polyDegreeField);

                buttonPanel.add(fitButton);
                buttonPanel.add(clearButton);
                pane.add(buttonPanel,"South");
                //pane.setSize(500,400);
                //setVisible(true);

        return pane;
    }

        public void fitCurve() {
            model=graph.getModel();
		model.firstSeries();
                
            //if (model != null && model.seriesLength() >0 )
		//{   model.firstSeries();
                data = new double[2][model.seriesLength()];
		    modelX = new double[model.seriesLength()];
		    modelY = new double[model.seriesLength()];

                for(int i=0;i<data[0].length;i++) {
                        data[0][i]=model.getXCoord(i);
                        data[1][i]=model.getYCoord(i);
                }

		    poly_degree = Integer.parseInt(polyDegreeField.getText());
		    //System.err.println("degree chosen = "+poly_degree);
                
                coeffs=LinearMath.leastSquaresFit(poly_degree, data);
                StringBuffer poly=new StringBuffer(20);
                poly.append("y = ");
                           
                
            if (coeffs.dimension()>0 && !(Double.isNaN(coeffs.getComponent(0))) )
		{   poly.append((float)coeffs.getComponent(0));
                for(int i=1;i<coeffs.dimension();i++)
                 {      if ((float)coeffs.getComponent(i)>=0)
				  poly.append(' ').append('+').append((float)coeffs.getComponent(i)).append("x^").append(i);
				else
			        poly.append(' ').append((float)coeffs.getComponent(i)).append("x^").append(i);
			}
		    resultingFit.setText(poly.toString());

			// PLOT the actual Model Fit ...
                // Draw a polyline between each point of the fit:
		    // (X, Y) = (data[0][i] , evaluateModel(float X) )
			modelX = data[0];

	// For getting the actual values ....
	//System.out.println("Here are the observed data and their Model Estimates:");
	//System.out.println(" Index\t\tX\t\tData\t\tModel");

	for(int i=0;i<data[0].length;i++)
                     {      modelY[i] = evaluateModel(modelX[i]);
                            	//System.out.println(String.valueOf(i)+"\t"+
					//	String.valueOf(modelX[i])+"\t"+
					//	String.valueOf(data[1][i])+"\t"+
					//	String.valueOf(modelY[i])
	           			//);
	  }
	//System.out.println("============================================");

		      graph.setModel(data[0].length, modelX, modelY);  //  LineTraceModel1 graph;

			// Print the RESIDUAL MEAN SQUARE ERROR of the model RMSE=
			float RMSE = 0;
			for(int i=0;i<data[0].length;i++)
                        RMSE += (modelY[i] -data[1][i])*(modelY[i] -data[1][i]);

			if (data[0].length>0) RMSE /= data[0].length;
			else RMSE = 0;
		
			resultingFit.append("\nRMSE="+ String.valueOf(RMSE));


			/**************  Add the Chi-Square Goodness of fit Test  ******/
			// 1. Calculate the Chi-Square statistics
			double ChiSquareValue = 0, ChiSquareP_value = 0;
			for(int i=0;i<data[0].length;i++)
                  {      if (modelY[i] > 0)
				    ChiSquareValue += (modelY[i] -data[1][i])*(modelY[i] -data[1][i])/modelY[i];
				 else if (modelY[i] < 0)
				    ChiSquareValue -= (modelY[i] -data[1][i])*(modelY[i] -data[1][i])/modelY[i];
			 }

			resultingFit.append("\tXo="+ String.valueOf(ChiSquareValue)+
				" ~ Chi-Square(df="+data[0].length+")" );

			// 2. Find the corresponding P-value from Chi-Square resource
			ChiSqrDistribution CSD = new ChiSqrDistribution(data[0].length);
				// JSci.maths.statistics.ChiSqrDistribution 
			ChiSquareP_value = 1- CSD.cumulative(ChiSquareValue);
			if (ChiSquareP_value <0) ChiSquareP_value =0;
			else if (ChiSquareP_value >1) ChiSquareP_value =1;

			// 3. Print the results
			resultingFit.append("\t P-value = "+ChiSquareP_value);
			/****************/
	
	     } // if (model != null)
	     else 
		   
                 resultingFit.setText("Please first draw your curve. \nThen Fit a Polynomial Model ...");
                 //resultingFit.setText(Double.toString(coeffs.getComponent(0)));
        }

		
    public double evaluateModel(double X)
    {  double Y=0;
       for(int i=0;i<coeffs.dimension();i++)
            Y += (float)coeffs.getComponent(i) * Math.pow(X, i);
       return Y;
     }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Curve Fitter");

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        CurveFitterApplet applet = new CurveFitterApplet(false);
        frame.setContentPane(applet.makeContentPane());
        frame.pack();
        frame.setVisible(true);
    }

}

