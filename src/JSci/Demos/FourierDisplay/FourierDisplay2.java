package JSci.Demos.FourierDisplay;

import java.awt.*;
import java.awt.event.*;
import JSci.awt.*;
import JSci.maths.*;

/**
* Sample program demonstrating use of FourierMath and LineGraph classes.
* @author Mark Hale
* @version 1.0
*/
public final class FourierDisplay2 extends Frame {
        private final int N=128;
        private List fns=new List(4);
        private DefaultGraph2DModel signalModel=new DefaultGraph2DModel();
        private DefaultGraph2DModel transformModel=new DefaultGraph2DModel();
        private double signal[];

        public static void main(String arg[]) {
                new FourierDisplay2();
        }
        public FourierDisplay2() {
                super("Fourier Display 2");
                addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent evt) {
                                dispose();
                                System.exit(0);
                        }
                });
                float xAxis[]=new float[N];
                for(int i=0;i<N;i++)
                        xAxis[i]=i-N/2;
                signalModel.setXAxis(xAxis);
                signalModel.addSeries(xAxis);
                transformModel.setXAxis(xAxis);
                transformModel.addSeries(xAxis);
                transformModel.addSeries(xAxis);
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
                                displaySignal();
                                displayTransform();
                        }
                });
                LineGraph signalGraph=new LineGraph(signalModel);
                signalGraph.setColor(0,Color.red);
                LineGraph transformGraph=new LineGraph(transformModel);
                transformGraph.setColor(0,Color.red);
                Panel graphs=new Panel();
                graphs.setLayout(new GridLayout(1,2));
                graphs.add(signalGraph);
                graphs.add(transformGraph);
                add(graphs,"Center");
                add(fns,"South");
                signal=sineWave(1.0,N);
                displaySignal();
                displayTransform();
                setSize(600,400);
                setVisible(true);
        }
        private void displaySignal() {
                signalModel.changeSeries(0,signal);
        }
        private void displayTransform() {
                Complex result[]=FourierMath.sort(FourierMath.transform(signal));
                float realpart[]=new float[N];
                float imagpart[]=new float[N];
                for(int i=0;i<N;i++) {
                        realpart[i]=(float)result[i].real();
                        imagpart[i]=(float)result[i].imag();
                }
                transformModel.changeSeries(0,realpart);
                transformModel.changeSeries(1,imagpart);
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

