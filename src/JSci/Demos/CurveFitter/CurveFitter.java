package JSci.Demos.CurveFitter;


import java.awt.*;
import java.awt.event.*;
import JSci.awt.*;
import JSci.maths.*;

/**
* Sample program demonstrating use of LinearMath.leastSquaresFit method
* and the LineTraceModel graph class for Polynomial Fitting.
* @author Ivo Dinov
* @version 1.0
*/
public final class CurveFitter extends Frame {
        private Label fnLabel=new Label("y = ?",Label.CENTER);
        private LineTraceModel1 graph=new LineTraceModel1(-10.0f,10.0f,-10.0f,10.0f);
        private Button fitButton=new Button("Polynomial Fit");
        private Button clearButton=new Button("Clear Display");
	  private TextArea resultingFit = new TextArea(5, 40);
	  private TextField polyDegreeField = new TextField("4", 3);
	  private Label degreeLabel = new Label ("Model Degree");

	  public int poly_degree = 1;

	  public double data[][];
	  public double modelX[], modelY[];
	  public Graph2DModel model;
	  public DoubleVector coeffs;

        public static void main(String arg[]) {
                new CurveFitter();
        }
        public CurveFitter() {
                super("Curve Fitter");

		    resultingFit.setFont(new Font("TimesRoman", Font.BOLD+Font.ITALIC, 14));

                addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent evt) {
                                dispose();
                                System.exit(0);
                        }
                });
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
		    add(resultingFit, "North");
                add(graph,"Center");
                Panel buttonPanel=new Panel();

		    buttonPanel.add(degreeLabel);
		    buttonPanel.add(polyDegreeField);

                buttonPanel.add(fitButton);
                buttonPanel.add(clearButton);
                add(buttonPanel,"South");
                setSize(500,400);
                setVisible(true);
        }

        private void fitCurve() {
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
	
	     } // if (model != null)
	     else 
		   resultingFit.setText("Please first draw your curve. \nThen Fit a Polynomial Model ...");

        }

		
public double evaluateModel(double X)
{     double Y=0;
      for(int i=0;i<coeffs.dimension();i++)
            Y += (float)coeffs.getComponent(i) * Math.pow(X, i);
      return Y;
 }


}

