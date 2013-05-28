package JSci.Demos.FourierDisplay;


import java.awt.*;
import java.awt.event.*;
import JSci.awt.*;
import JSci.maths.*;

/**
* Sample program demonstrating use of FourierMath and LineGraph classes.
* @author Mark Hale
* @version 1.3
*/
public final class FourierDisplay extends Frame {
        private final int N=128;
        private List fns=new List(4);
        private Checkbox inverse=new Checkbox("inverse");
        private DefaultGraph2DModel model=new DefaultGraph2DModel();
        private double signal[];
        private boolean doInverse=false;

        public static void main(String arg[]) {
                new FourierDisplay();
        }
        public FourierDisplay() {
                super("Fourier Display");
                addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent evt) {
                                dispose();
                                System.exit(0);
                        }
                });
                float xAxis[]=new float[N];
                for(int i=0;i<N;i++)
                        xAxis[i]=i-N/2;
                model.setXAxis(xAxis);
                model.addSeries(xAxis);
                model.addSeries(xAxis);
                fns.add("Gaussian");
                fns.add("Top hat");
                fns.add("Constant");
                fns.add("Square");
                fns.add("Triangle");
                fns.add("Sine");
                fns.select(5);
                fns.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent evt) {
                                switch(fns.getSelectedIndex()) {
                                        case 0 : signal=gaussianSignal(N);
                                                break;
                                        case 1 : signal=tophatSignal(1.0,N);
                                                break;
                                        case 2 : signal=constantSignal(1.0,N);
                                                break;
                                        case 3 : signal=squareWave(1.0,N);
                                                break;
                                        case 4 : signal=triangleWave(1.0,N);
                                                break;
                                        case 5 : signal=sineWave(1.0,N);
                                                break;
                                }
                                displayTransform();
                        }
                });
                inverse.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent evt) {
                                doInverse=!doInverse;
                                displayTransform();
                        }
                });
                LineGraph graph=new LineGraph(model);
                graph.setColor(0,Color.red);
                Panel cntrl=new Panel();
                cntrl.add(fns);
                cntrl.add(inverse);
                add(graph,"Center");
                add(cntrl,"South");
                signal=sineWave(1.0,N);
                displayTransform();
                setSize(400,400);
                setVisible(true);
        }
        private void displayTransform() {
                Complex result[];
                if(doInverse)
                        result=FourierMath.sort(FourierMath.inverseTransform(signal));
                else
                        result=FourierMath.sort(FourierMath.transform(signal));
                float realpart[]=new float[N];
                float imagpart[]=new float[N];
                for(int i=0;i<N;i++) {
                        realpart[i]=(float)result[i].real();
                        imagpart[i]=(float)result[i].imag();
                }
                model.changeSeries(0,realpart);
                model.changeSeries(1,imagpart);
        }

// A selection of test signals

        /**
        * Under transform should give something like exp(-x^2).
        * Real spectrum.
        */
        private static double[] gaussianSignal(int n) {
                double data[]=new double[n];
                double x;
                for(int i=0;i<n;i++) {
                        x=(i-n/2);
                        data[i]=Math.exp(-x*x);
                }
                return data;
        }
        /**
        * Under transform should give something like cos(x)/x.
        * Real spectrum.
        */
        private static double[] tophatSignal(double amplitude,int n) {
                double data[]=new double[n];
                int i=0;
                for(;i<n/4;i++)
                        data[i]=0.0;
                for(;i<3*n/4;i++)
                        data[i]=amplitude;
                for(;i<n;i++)
                        data[i]=0.0;
                return data;
        }
        /**
        * Under transform should give a delta-function at origin.
        * Real spectrum.
        */
        private static double[] constantSignal(double amplitude,int n) {
                double data[]=new double[n];
                for(int i=0;i<n;i++)
                        data[i]=amplitude;
                return data;
        }
        /**
        * Under transform should give something like i*sin(x)/x.
        * Complex spectrum.
        */
        private static double[] squareWave(double amplitude,int n) {
                double data[]=new double[n];
                int i=0;
                for(;i<n/2;i++)
                        data[i]=-amplitude;
                for(;i<n;i++)
                        data[i]=amplitude;
                return data;
        }
        /**
        * Under transform should give something like i*sin(x)/x^2.
        * Complex spectrum.
        */
        private static double[] triangleWave(double amplitude,int n) {
                double data[]=new double[n];
                double gradient=amplitude*4.0/n;
                int i=0;
                for(;i<n/4;i++)
                        data[i]=-gradient*i;
                for(;i<3*n/4;i++)
                        data[i]=-2.0*amplitude+gradient*i;
                for(;i<n;i++)
                        data[i]=4.0*amplitude-gradient*i;
                return data;
        }
        /**
        * Under transform should give two delta-functions at +/- frequency.
        * Complex spectrum.
        */
        private static double[] sineWave(double amplitude,int n) {
                double data[]=new double[n];
                double w=NumericalConstants.TWO_PI/n*16.0;
                for(int i=0;i<n;i++)
                        data[i]=amplitude*Math.sin((i-n/2)*w);
                return data;
        }
}

