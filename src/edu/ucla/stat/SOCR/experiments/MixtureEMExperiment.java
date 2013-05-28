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
/*
 * MixtureEMExperiment.java MixtureEMExperiment.java contains the driver JApplet
 * for the EM algorithm @author Ivo Dinov
 */

package edu.ucla.stat.SOCR.experiments;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MixtureEMExperiment extends Experiment {
    public JComboBox          nKernels;
    public JComboBox          selectmix;
    public JComboBox          EMswitch;
    public JComboBox          selectSpeed;

    public JButton            RandomPoints;
    public JButton            ClearPoints;
    public JButton            InitializeKernel;

    public CurvedGaussMixture CGMix;
    public GaussLineMixture   GLMix;
    public double[]           ws, ws2;
    public volatile Thread    EM_Thread = null;

    public ControlPanel       cPanel;
    public PlotCanvas         pCanvas;
    public Database           DB;

    //Dimension appletDims = getSize();
    public int                xSize      = 700;
    public int                ySize      = 700;

    public MixtureEMExperiment() {
        //getContentPane().setLayout(new BorderLayout());
        DB = new Database(xSize, ySize, Color.black);
        CGMix = new CurvedGaussMixture(xSize, ySize);
        GLMix = new GaussLineMixture(xSize, ySize);

        selectmix = new JComboBox();
        RandomPoints = new JButton("RandomPts");
        ClearPoints = new JButton("ClearPts");
        InitializeKernel = new JButton("InitKernels");
        selectSpeed = new JComboBox();
        nKernels = new JComboBox();
        EMswitch = new JComboBox();

        pCanvas = new PlotCanvas(xSize, ySize);
        cPanel = new ControlPanel();

        addToolbar(cPanel);
        //Add Graph panel
        pCanvas.setMinimumSize(new Dimension(300, 300));
        addGraph(pCanvas);
    } //END:init()

    public void reset() {
        super.reset();
        DB.clearPoints();
    } //END::reset()

    public String getName()
    {
	return new String ("Mixture EM Experiment");
     }

    /**
     * This method updates the display, including the ball panel, the record
     * table, and the random variable graph and table
     */
    public void update() {
        super.update();
        xSize = applet.getHeight();
        ySize = applet.getWidth();
        pCanvas.repaint();
        cPanel.repaint();
    }

    public void doExperiment() {
        super.doExperiment();
        cPanel.stop();
        cPanel.EM();
    } // END::doExperiment()


       /**
	* This method returns basic information about the applet, including copyright
	* information, descriptive informaion, and instructions.
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "This experiments demonstrates the Expectation Maximization (EM) algorithm. \n"
			+ "In this setting the EM is applied as a tool for classisfication.\n"
			+ "\n1. Select random points in the 2D plane (manually by clicking the mouse in the \n"
			+"field of view or by clicking the <RandomPnts> button.\n"
			+"2. Then select the number of cluster that you want to identify\n"
			+"3. Click <InitKernels> to get a different starting condition (EM algorithm is VERY sensitive \n"
			+"to the starting conditions!)\n"
			+"4. Select Normal/Fast/Slow spead of the algorithm (for demo purposes choose Slow)\n"
			+"5. Choose Gaussian or Linear fit for your mixture model\n"
			+"6. Click <EM Run> to start the algorithm. Observe the evolution of the process (convergence is guaranteed!)\n"
			+"7. Finally, use <EM Stop> or <EM 1 Step> to terminate the or take one step at a time\n"
			+"\n You can Segment the initial points based on your Linear/Gaussian fit by pressing <Segment>";
	}


    /***************************************************************************
     * Override of the Experiment.java's default addGraph() method public void
     * addGraph(JComponent c){ //graphCount++; c.setSize(new Dimension(500,
     * 500)); System.out.println("MixtureEMExperiment::addGraph()!\t
     * c.getSize()=("+c.getWidth()+ ", "+c.getHeight()+")!!"); JScrollPane cSP =
     * new JScrollPane(c); //cSP.setMinimumSize(new Dimension(500, 500));
     * //this.getContentPane().add(cSP , BorderLayout.CENTER);
     * //System.out.println("MixtureEMExperiment::addGraph()!\t
     * this.getSize()=("+this.getWidth()+ //", "+this.getHeight()+")!!");
     * //graphs.setLayout(new BorderLayout()); graphs.add(cSP); // ,
     * BorderLayout.CENTER); }
     **************************************************************************/

    // Inner Class ControlPanel
    class ControlPanel extends JPanel implements Runnable, ActionListener {
        int     select;

        boolean runMode      = false;

        int     currentSpeed = 200;  // EM algorithm speed (slow,

        // normal, fast)

        public ControlPanel() {
            ws = new double[Mixture.maxkp];
            ws2 = new double[Mixture.maxkp];

            //setMinimumSize(new Dimension(500,500));

            ws[0] = 0.1;
            ws2[0] = 10;
            for (int i = 1; i < Mixture.maxkp; i++)
                ws[i] = ws2[i] = 1;

            setLayout(new FlowLayout());
            setSelect(3);
            //selectmix = new JComboBox();
            selectmix.addItem("GaussMix");
            selectmix.addItem("LineMix");
            selectmix.setSelectedItem(Integer.toString(0));
            selectmix.addActionListener(this);
            add(selectmix);

            //RandomPoints = new JButton("RandomPts");
            //ClearPoints = new JButton("ClearPts");
            //InitializeKernel = new JButton("InitKernels");

            //System.out.println("FromControlPanelClass::RandomPoints
            // =
            // "+RandomPoints);

            //selectSpeed = new JComboBox();
            selectSpeed.addItem("Normal");
            selectSpeed.addItem("Fast");
            selectSpeed.addItem("Slow");
            selectSpeed.setSelectedItem(Integer.toString(0));
            selectSpeed.addActionListener(this);
            add(selectSpeed);

            RandomPoints.addActionListener(this);
            ClearPoints.addActionListener(this);
            InitializeKernel.addActionListener(this);

            add(RandomPoints);
            add(ClearPoints);
            add(InitializeKernel);

            // startButton.addActionListener(this);

            //nKernels = new JComboBox();
            for (int i = 1; i < 10; i++)
                nKernels.addItem(Integer.toString(i));
            nKernels.setSelectedItem(Integer.toString(1));
            nKernels.addActionListener(this);

            add(nKernels);

            //EMswitch = new JComboBox();
            EMswitch.addItem("EM Stop");
            EMswitch.addItem("EM Run");
            EMswitch.addItem("EM 1 Step");
            EMswitch.addItem("Segment");
            EMswitch.setSelectedItem(Integer.toString(0));
            EMswitch.addActionListener(this);

            add(EMswitch);
        }

        public void start() {
            if (EM_Thread == null) {
                EM_Thread = new Thread(this);
                EM_Thread.start();
            }
        }

        public void stop() {
            EM_Thread = null;
        }

        public void run() {
            Thread thisThread = Thread.currentThread();
            while (EM_Thread == thisThread) {
                try {
                    for (int i = 0; i < 5; i++) {
                        EM();
                    }
                    pCanvas.repaint();
                    Thread.sleep(currentSpeed);
                } catch (InterruptedException e) {}
            }
        }

        private void EM() {
            if (select == 3) {
                CGMix.EM(ws);
            } else if (select == 4) {
                GLMix.EM(ws);
            }
        }

        public void dbpush(int x, int y) {
            if (EM_Thread != null) {
                stop();
                DB.push(x, y);
                start();
            } else {
                DB.push(x, y);
            }
        }

        public void actionPerformed(ActionEvent e) {
            int nK;
            boolean thflag;

            thflag = (EM_Thread != null);

            if (e.getSource() instanceof JComboBox) {
                JComboBox JCB = (JComboBox) e.getSource();
                String JCB_Value = (String) JCB.getSelectedItem();
                System.out.println("JCB_Value = " + JCB_Value);

                if (JCB_Value.equals("EM 1 Step")) {
                    System.out.println("EM 1 Step");
                    stop();
                    EM();
                } else if (JCB_Value.equals("EM Run")) {
                    System.out.println("EM Run");
                    stop();
                    start();
                } else if (JCB_Value.equals("EM Stop")) {
                    System.out.println("EM Stop");
                    stop();
                } else if (JCB_Value.equals("Segment")) {
                    System.out.println("Point Segmenting");
                    stop();
                    pointSegmenting();
                } else if (JCB_Value.equals("GaussMix")) {
                    System.out.println("GaussMix");
                    setSelect(3);
                    CGMix.randomKernels(ws2);
                } else if (JCB_Value.equals("LineMix")) {
                    System.out.println("LinearMix");
                    setSelect(4);
                    GLMix.randomKernels(ws2);
                } else if (JCB_Value.equals("Fast")) {
                    System.out.println("Speed selected: Fast");
                    setSpeed("Fast");
                } else if (JCB_Value.equals("Normal")) {
                    System.out.println("Speed selected: Normal");
                    setSpeed("Normal");
                } else if (JCB_Value.equals("Slow")) {
                    System.out.println("Speed selected: Slow");
                    setSpeed("Slow");
                } else // Number of Kernels
                {
                    stop();
                    nK = Integer.parseInt(JCB_Value);
                    CGMix.setnk(nK + 1, ws);
                    GLMix.setnk(nK + 1, ws);
                    if (thflag) start();
                }
            } else if (e.getActionCommand().equals("ClearPts")) {
                DB.clearPoints();
                applet.repaint();
            } else if (e.getActionCommand().equals("RandomPts")) {
            	// COMPUTE the Range (min & Max) of the X & Y data, by series, if data is given! 
            	// xDataMin <= xDataMax, yDataMin <= yDataMax
            	// DB.setXSize((int)(1.2*(xDataMax-xDataMin)));	// setSize at 120%, on purpose!
            	// DB.setYSize((int)(1.2*(yDataMax-yDataMin)));
            	// int _xStart = (int)(0.5*(xDataMax-xDataMin));
            	// int _yStart = (int)(0.5*(yDataMax-yDataMin));
            	// DB.setStarts(_xStart, _yStart);
            	DB.randomPoints(10);
                //for (int i = 0; i < DB.nPoints(); i++)
                //cPanel.dbpush(DB.xVal(i), DB.yVal(i));
                pCanvas.repaint();
                System.out.println("ButtonRandomPts::DB.nPoints()=" + DB.nPoints());
            } else if (e.getActionCommand().equals("InitKernels")) {
                CGMix.randomKernels(ws);
                GLMix.randomKernels(ws);
            }

            pCanvas.repaint();

            return;
        }

        public void pointSegmenting() {
            for (int i = 0; i < DB.nPoints(); i++) // For each point
            // find the
            // 2-nd level Gaussian
            {
                for (int k = 0; k < CGMix.getnk(); k++) // for
                // each
                // kernel
                {
                    if (CGMix.getKernel(k) instanceof CurvedGaussian
                            && ((CurvedGaussian) (CGMix.getKernel(k))).getPolygon()
                                    .contains(DB.xVal(i), DB.yVal(i)))
                    // point inside the 2nd polygon Ellipse
                    // "CGMix")
                    { //System.out.println("Inside::pointSegmenting()
                        // Poly.N_Points=" +
                        //((CurvedGaussian)(CGMix.getKernel(k))).getPolygon().npoints);
                        DB.setPointColor(i, CGMix.getKernelColor(k));
                        k = CGMix.getnk(); // exit inner
                        // loop
                    }
                }
            }
        }

        public void setSelect(int sel) {
            select = sel;
            pCanvas.setSelect(sel);
        }

        public void setSpeed(String newSpeed) {
            if (newSpeed.toLowerCase().startsWith("fa")) // FAST
                    // Speed
                    // requested
                    currentSpeed = 0;
            if (newSpeed.toLowerCase().startsWith("sl")) // Slow
            // Speed
            // requested
            currentSpeed = 1000;
            else // Normal Speed requested
            currentSpeed = 200;
        }
    } // END::Inner Class ControlPanel

    // Inner Class PlotCanvas
    class PlotCanvas extends JPanel implements MouseListener {

        /**
         * 
         * @uml.property name="select" 
         */
        int select;

        int xsiz, ysiz;

        public PlotCanvas(int xSize, int ySize) {
            setBackground(Color.green);
            xsiz = xSize;
            ysiz = ySize;
            //setSize(new Dimension(xSize, ySize));
            this.addMouseListener(this);
        }

        public void connectControlPanel() {}

        public void paintComponent(Graphics g) {
            super.paintComponents(g);

            //System.out.println("PlotCanvas::repaint()!!");

            //g.clearRect(0, 0, xsiz, ysiz);
            g.clearRect(0, 0,getWidth(), getHeight());
            DB.paint(g);
            
            if (select == 3) {
                CGMix.paint(g);
            } else if (select == 4) {
                GLMix.paint(g);
            }
        }

        /**
         * 
         * @uml.property name="select"
         */
        public void setSelect(int sel) {
            select = sel;
        }

        public void mousePressed(MouseEvent e) {
            cPanel.dbpush(e.getX(), e.getY());
            repaint();
            //System.out.println("mousePressed::DB.nPoints()="+DB.nPoints());
            //System.out.println("e.getX()="+e.getX()+
            // "\te.getY())="+e.getY());
        }

        public void mouseDragged(MouseEvent e) {}

        public void mouseReleased(MouseEvent e) {}

        public void mouseMoved(MouseEvent e) {}

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        public void mouseClicked(MouseEvent e) {}
    } // END::Inner Class PlotCanvas

    // Inner Class CurvedGaussMixture
    class CurvedGaussMixture extends Mixture {
        final int        kmax           = 10;
        CurvedGaussian[] curvedGaussian = new CurvedGaussian[kmax];

        public CurvedGaussMixture(int xSize, int ySize) {
            super(xSize, ySize, DB);

            initKernel(new Uniform(xsiz, ysiz, 0.0), typeuniform, 0);
            for (int i = 1; i < kmax; i++) {
                curvedGaussian[i] = new CurvedGaussian(xsiz, ysiz, 0.0);
                initKernel(curvedGaussian[i], typecurvedgauss, i);
            }
            setnk(2);
        }

        public CurvedGaussian getCurvedGaussian(int kernel_index) {
            return curvedGaussian[kernel_index];
        }
    } // END::Inner Class CurvedGaussMixture

    // Inner Class GaussLineMixture
    class GaussLineMixture extends Mixture {
        final int kmax = 10;

        public GaussLineMixture(int xSize, int ySize) {
            super(xSize, ySize, DB);
            CurvedGaussian CGMix;
            initKernel(new Uniform(xsiz, ysiz, 0.0), typeuniform, 0);
            for (int i = 1; i < kmax; i++) {
                CGMix = new CurvedGaussian(xsiz, ysiz, 0.0);
                CGMix.setplotline();
                initKernel(CGMix, typecurvedgauss, i);
            }
            setnk(2);
        }
    } // Inner Class GaussLineMixture

} // END::Main Class MixtureEMExperiment.java
