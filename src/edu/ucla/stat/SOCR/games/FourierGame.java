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

package edu.ucla.stat.SOCR.games;

import edu.ucla.stat.SOCR.core.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import edu.ucla.stat.SOCR.games.wavelet.basisCanvas;
import sun.audio.*;

/**
 * This class models a simple interactive scatterplot. The user clicks on the
 * scatterplot to generate points. The regression line and summary statistics
 * are shown.
 */
public class FourierGame extends Game implements ComponentListener {
    Dimension winSize;
    Image dbimage;

    Random random;
    public static final int sampleCount = 720;
    public static final int halfSampleCount = sampleCount / 2;
    public static final double halfSampleCountFloat = sampleCount / 2;


    //Fourier applet;
    JButton sineButton;
    JButton rectButton;
    JButton fullRectButton;
    JButton triangleButton;
    JButton sawtoothButton;
    JButton squareButton;
    JButton noiseButton;
    JButton blankButton;
    JButton clipButton;
    JButton resampleButton;
    JButton quantizeButton;
    JButton playButton;
    double magcoef[];
    double phasecoef[];
    static final double pi = 3.14159265358979323846;
    static final double step = 2 * pi / sampleCount;
    double func[];
    int maxTerms = 160;
    int selectedCoef;
    static final int SEL_NONE = 0;
    static final int SEL_FUNC = 1;
    static final int SEL_MAG = 2;
    static final int SEL_PHASE = 3;
    int selection;
    int dragX, dragY;
    boolean dragging;
    

    static final int to_ulaw[] = { 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3,
            3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9,
            9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14,
            14, 14, 14, 15, 15, 15, 15, 16, 16, 17, 17, 18, 18, 19, 19, 20, 20, 21,
            21, 22, 22, 23, 23, 24, 24, 25, 25, 26, 26, 27, 27, 28, 28, 29, 29, 30,
            30, 31, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46,
            47, 49, 51, 53, 55, 57, 59, 61, 63, 66, 70, 74, 78, 84, 92, 104, 254,
            231, 219, 211, 205, 201, 197, 193, 190, 188, 186, 184, 182, 180, 178,
            176, 175, 174, 173, 172, 171, 170, 169, 168, 167, 166, 165, 164, 163,
            162, 161, 160, 159, 159, 158, 158, 157, 157, 156, 156, 155, 155, 154,
            154, 153, 153, 152, 152, 151, 151, 150, 150, 149, 149, 148, 148, 147,
            147, 146, 146, 145, 145, 144, 144, 143, 143, 143, 143, 142, 142, 142,
            142, 141, 141, 141, 141, 140, 140, 140, 140, 139, 139, 139, 139, 138,
            138, 138, 138, 137, 137, 137, 137, 136, 136, 136, 136, 135, 135, 135,
            135, 134, 134, 134, 134, 133, 133, 133, 133, 132, 132, 132, 132, 131,
            131, 131, 131, 130, 130, 130, 130, 129, 129, 129, 129, 128, 128, 128,
            128 };

    int getrand(int x) {
        int q = random.nextInt();
        if (q < 0) q = -q;
        return q % x;
    }

    basisCanvas cv;

    /**
     * This method initializes the applet, including the toolbar, scatterplot,
     * and statistics table.
     */
    public FourierGame() {
        setName("Fourier Game");
        createValueSetter("Number of Terms", Distribution.DISCRETE, 1, maxTerms);
        createValueSetter("Playing Frequency", Distribution.DISCRETE, 0, 100);
	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	setAppletInfo("This Fourier-Game Applet allows the user to visually inspect the analysis and synthesis\n"
			+"steps in the discrete Fourier transform. And to manually create a 1D signal, or chose one of the\n"
			+ "signals already predefined in the applet. Then the user may bring the mouse over the data (top panel)\n"
			+"or the Fourier transform of the data (bottom panel) and observe the effects of a signal intensity or\n"
			+"a Fourier coefficient on each other. The applet also allows local manipulations of these\n"
			+"parameters to tease out their individual contributions in the space or frequency domain, respectively.\n"
			+"Finally the user may play with the Fourier-coefficient thresholding, by choosing the number\n"
			+"of frequency terms used in the synthesis of the Fourier approximation to the original signal.\n"
			+"This work extends previous developments by Paul Falstad.\n");

        selectedCoef = -1;
        magcoef = new double[maxTerms];
        phasecoef = new double[maxTerms];
        func = new double[sampleCount + 1];
        //this.getContentPane().setLayout(new basisLayout());

        cv = new basisCanvas(this);
        cv.addComponentListener(this);
        cv.addMouseMotionListener(this);
        cv.addMouseListener(this);

        //this.getContentPane().add(cv);

        addGraph(cv);
        sineButton = new JButton("Sine");
        sineButton.addActionListener(this);
        //addTool(sineButton);
        addTool(sineButton);
        triangleButton = new JButton("Triangle");
        triangleButton.addActionListener(this);
        //addTool(triangleButton);
        addTool(triangleButton);
        sawtoothButton = new JButton("Sawtooth");
        sawtoothButton.addActionListener(this);
        //addTool(sawtoothButton);
        addTool(sawtoothButton);
        squareButton = new JButton("Square");
        squareButton.addActionListener(this);
        //addTool(squareButton);
        addTool(squareButton);
        noiseButton = new JButton("Noise");
        noiseButton.addActionListener(this);
        //addTool(noiseButton);
        addTool(noiseButton);
        clipButton = new JButton("Clip");
        clipButton.addActionListener(this);
        //addTool(clipButton);
        addTool(sineButton);
        resampleButton = new JButton("Resample");
        resampleButton.addActionListener(this);
        //addTool(resampleButton);
        addTool(resampleButton);
        quantizeButton = new JButton("Quantize");
        quantizeButton.addActionListener(this);
        //addTool(quantizeButton);
        addTool(quantizeButton);
        rectButton = new JButton("Rectify");
        rectButton.addActionListener(this);
        //addTool(rectButton);
        addTool(rectButton);
        fullRectButton = new JButton("Full Rectify");
        fullRectButton.addActionListener(this);
        //addTool(fullRectButton);
        addTool(fullRectButton);
        addTool(Box.createHorizontalStrut(10));

        playButton = new JButton("Play");
        playButton.addActionListener(this);
        //addTool(playButton);
        addTool(playButton);
        blankButton = new JButton("Clear");
        blankButton.addActionListener(this);
        //addTool(blankButton);
        addTool(blankButton);

        random = new Random();
        reinit();
        cv.setBackground(Color.black);
        cv.setForeground(Color.lightGray);

        //handleResize();
        //resize(500, 500);
        //this.setSize(new Dimension(500,500));

        //Event listener
        //xyScatter.addMouseListener(this);
        //xyScatter.addMouseMotionListener(this);
        //refreshButton.addActionListener(this);
        //toolbar
        //addTool(refreshButton);
        //addTool(mouseLabel);
        //Graphs
        //addGraph(xyScatter);
        //Tables
        //statsTable.setEditable(false);
        //addTable(statsTable);
        //reset();

    }

    void reinit() {
        doSawtooth();
    }

    void handleResize() {
        Dimension d = winSize = cv.getSize();
        System.out.println(d.height);
        if (winSize.width == 0) dbimage = applet.createImage(10, 10);
        else dbimage = applet.createImage(d.width, d.height);
    }

    void doSawtooth() {
        int x;
        for (x = 0; x != sampleCount; x++)
            func[x] = (x - sampleCount / 2) / halfSampleCountFloat;
        func[sampleCount] = func[0];
        transform();
    }

    void doTriangle() {
        int x;
        for (x = 0; x != halfSampleCount; x++) {
            func[x] = (x * 2 - halfSampleCount) / halfSampleCountFloat;
            func[x + halfSampleCount] = ((halfSampleCount - x) * 2 - halfSampleCount)
                    / halfSampleCountFloat;
        }
        func[sampleCount] = func[0];
        transform();
    }

    void doSine() {
        int x;
        for (x = 0; x != sampleCount; x++) {
            func[x] = java.lang.Math.sin((x - halfSampleCount) * step);
        }
        func[sampleCount] = func[0];
        transform();
    }

    void doRect() {
        int x;
        for (x = 0; x != sampleCount; x++)
            if (func[x] < 0) func[x] = 0;
        func[sampleCount] = func[0];
        transform();
    }

    void doFullRect() {
        int x;
        for (x = 0; x != sampleCount; x++)
            if (func[x] < 0) func[x] = -func[x];
        func[sampleCount] = func[0];
        transform();
    }

    void doSquare() {
        int x;
        for (x = 0; x != halfSampleCount; x++) {
            func[x] = -1;
            func[x + halfSampleCount] = 1;
        }
        func[sampleCount] = func[0];
        transform();
    }

    void doNoise() {
        int x;
        int blockSize = 3;
        for (x = 0; x != sampleCount / blockSize; x++) {
            double q = java.lang.Math.random() * 2 - 1;
            int i;
            for (i = 0; i != blockSize; i++)
                func[x * blockSize + i] = q;
        }
        func[sampleCount] = func[0];
        transform();
    }

    void doBlank() {
        int x;
        for (x = 0; x <= sampleCount; x++)
            func[x] = 0;
        transform();
    }

    void doSetFunc() {
        int i;
        int periodWidth = winSize.width / 3;
        for (i = 0; i != sampleCount + 1; i++) {
            int x = periodWidth * i / sampleCount;
            int j;
            double dy = 0;
            int terms = getValueSetter(0).getValueAsInt();
            for (j = 0; j != terms; j++) {
                dy += magcoef[j]
                        * java.lang.Math.cos(step * (i - halfSampleCount) * j
                                + phasecoef[j]);
            }
            func[i] = dy;
        }
        transform();
    }

    void doClip() {
        int x;
        double mult = 1.2;
        for (x = 0; x != sampleCount; x++) {
            func[x] *= mult;
            if (func[x] > 1) func[x] = 1;
            if (func[x] < -1) func[x] = -1;
        }
        func[sampleCount] = func[0];
        transform();
    }

    void doResample() {
        int chunksize = 60;
        int x, i;
        for (x = 0; x != sampleCount; x += chunksize) {
            for (i = 1; i != chunksize; i++)
                func[x + i] = func[x];
        }
        func[sampleCount] = func[0];
        transform();
    }

    void doQuantize() {
        int x;
        for (x = 0; x != sampleCount; x++) {
            func[x] = java.lang.Math.round(func[x] * 2) / 2.;
        }
        func[sampleCount] = func[0];
        transform();
    }

    static int freqs[] = { 25, 32, 40, 50, 64, 80, 100, 125, 160, 200, 250, 320,
            400, 500, 800, 1000, 1600, 2000 };

    int getFreq() {
        return (int) (27.5 * java.lang.Math
                .exp(getValueSetter(1).getValue() * .04158883084));
    }

    void doPlay() {
        doSetFunc();
        byte b[] = new byte[16000];
        int i;
        double mx = 0;
        for (i = 0; i != sampleCount; i++) {
            if (func[i] > mx) mx = func[i];
            if (func[i] < -mx) mx = -func[i];
        }
        double mult = 127 / mx;
        int pd = 8000 / getFreq();
        double f = 720. / pd;
        for (i = 0; i != 16000; i++) {
            int x = (int) (.5 + func[(int) ((i % pd) * f)] * mult);
            b[i] = (byte) to_ulaw[x + 128];
        }
        AudioDataStream ads = new AudioDataStream(new AudioData(b));
        AudioPlayer.player.start(ads);
        updateGame();
        cv.repaint();
    }

    void transform() {
        int x, y;
        double epsilon = .00001;
        for (y = 0; y != maxTerms; y++) {
            double coef = 0;
            for (x = 0; x != sampleCount + 1; x++) {
                int simp = (x == 0 || x == sampleCount) ? 1 : ((x & 1) + 1) * 2;
                double s = java.lang.Math.cos(step * (x - halfSampleCount) * y);
                coef += s * func[x] * simp;
            }
            // simpson = 2pi/(3*sampleCount) (f(0) + 4f(1) + 2f(2) ...)
            // integral(...)/pi
            // result = coef * 2/3*sampleCount
            double acoef = coef * (2.0 / (3.0 * sampleCount));
            //System.out.print("acoef " + y + " " + coef + "\n");
            coef = 0;
            for (x = 0; x != sampleCount + 1; x++) {
                int simp = (x == 0 || x == sampleCount) ? 1 : ((x & 1) + 1) * 2;
                double s = java.lang.Math.sin(step * (x - halfSampleCount) * y);
                coef += s * func[x] * simp;
            }
            double bcoef = coef * (2.0 / (3.0 * sampleCount));
            if (acoef < epsilon && acoef > -epsilon) acoef = 0;
            if (bcoef < epsilon && bcoef > -epsilon) bcoef = 0;
            if (y == 0) {
                magcoef[0] = acoef / 2;
                phasecoef[0] = 0;
            } else {
                magcoef[y] = java.lang.Math.sqrt(acoef * acoef + bcoef * bcoef);
                phasecoef[y] = java.lang.Math.atan2(-bcoef, acoef);
            }
            // System.out.print("phasecoef " + phasecoef[y] + "\n");
        }
    }

    int getPanelHeight() {
        return winSize.height / 3;
    }

    void centerString(Graphics g, String s, int y) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, (winSize.width - fm.stringWidth(s)) / 2, y);
    }

    // public void paint(Graphics g) {
    //	cv.repaint();
    //  }

    void updateGame() {}

    public void updateGame(Graphics realg) {
        try {
            // if(dbimage == null)
            //   dbimage = createImage(10,10);
            Graphics g = realg;
            //Graphics g =dbimage.getGraphics();
            if (winSize == null || winSize.width == 0) return;
            Color gray1 = new Color(76, 76, 76);
            Color gray2 = new Color(127, 127, 127);
            g.setColor(cv.getBackground());
            g.fillRect(0, 0, winSize.width, winSize.height);
            g.setColor(cv.getForeground());
            int i;
            int ox = -1, oy = -1;
            int panelHeight = getPanelHeight();
            int midy = panelHeight / 2;
            int halfPanel = panelHeight / 2;
            int periodWidth = winSize.width / 3;
            double ymult = .75 * halfPanel;
            for (i = -1; i <= 1; i++) {
                g.setColor((i == 0) ? gray2 : gray1);
                g.drawLine(0, midy + (i * (int) ymult), winSize.width, midy
                        + (i * (int) ymult));
            }
            for (i = 2; i <= 4; i++) {
                g.setColor((i == 3) ? gray2 : gray1);
                g.drawLine(periodWidth * i / 2, midy - (int) ymult, periodWidth * i
                        / 2, midy + (int) ymult);
            }
            g.setColor(Color.white);
            if (!(dragging && selection != SEL_FUNC)) {
                for (i = 0; i != sampleCount + 1; i++) {
                    int x = periodWidth * i / sampleCount;
                    int y = midy - (int) (ymult * func[i]);
                    if (ox != -1) {
                        g.drawLine(ox, oy, x, y);
                        g.drawLine(ox + periodWidth, oy, x + periodWidth, y);
                        g
                                .drawLine(ox + periodWidth * 2, oy, x + periodWidth
                                        * 2, y);
                    }
                    ox = x;
                    oy = y;
                }
            }
            int terms = getValueSetter(0).getValueAsInt();
            if (!(dragging && selection == SEL_FUNC)) {
                g.setColor(Color.red);
                ox = -1;
                for (i = 0; i != sampleCount + 1; i++) {
                    int x = periodWidth * i / sampleCount;
                    int j;
                    double dy = 0;
                    for (j = 0; j != terms; j++) {
                        dy += magcoef[j]
                                * java.lang.Math.cos(step * (i - halfSampleCount)
                                        * j + phasecoef[j]);
                    }
                    int y = midy - (int) (ymult * dy);
                    if (ox != -1) {
                        g.drawLine(ox, oy, x, y);
                        g.drawLine(ox + periodWidth, oy, x + periodWidth, y);
                        g
                                .drawLine(ox + periodWidth * 2, oy, x + periodWidth
                                        * 2, y);
                    }
                    ox = x;
                    oy = y;
                }
            }
            if (selectedCoef != -1 && !dragging) {
                g.setColor(Color.yellow);
                ox = -1;
                ymult *= magcoef[selectedCoef];
                double phase = phasecoef[selectedCoef];
                int x;
                double n = selectedCoef * 2 * pi / periodWidth;
                int dx = periodWidth / 2;
                for (i = 0; i != sampleCount + 1; i++) {
                    x = periodWidth * i / sampleCount;
                    double dy = java.lang.Math.cos(step * (i - halfSampleCount)
                            * selectedCoef + phase);
                    int y = midy - (int) (ymult * dy);
                    if (ox != -1) {
                        g.drawLine(ox, oy, x, y);
                        g.drawLine(ox + periodWidth, oy, x + periodWidth, y);
                        g
                                .drawLine(ox + periodWidth * 2, oy, x + periodWidth
                                        * 2, y);
                    }
                    ox = x;
                    oy = y;
                }
                if (selectedCoef > 0) {
                    int f = getFreq() * selectedCoef;
                    centerString(g, getFreq() * selectedCoef
                            + ((f > 4000) ? " Hz (aliased)" : " Hz"), panelHeight);
                }
            }
            int termWidth = getTermWidth();
            ymult = 1.2 * halfPanel;
            midy = ((panelHeight * 3) / 2) + (int) ymult / 2;
            g.setColor(Color.white);
            centerString(g, "Magnitudes", (int) (panelHeight * 1.16));
            centerString(g, "Phases", (int) (panelHeight * 2.10));
            g.setColor(gray2);
            g.drawLine(0, midy, winSize.width, midy);
            g.setColor(gray1);
            g.drawLine(0, midy - (int) ymult, winSize.width, midy - (int) ymult);
            int dotSize = termWidth - 3;
            for (i = 0; i != terms; i++) {
                int t = termWidth * i + termWidth / 2;
                int y = midy - (int) (magcoef[i] * ymult);
                g.setColor(i == selectedCoef ? Color.yellow : Color.white);
                g.drawLine(t, midy, t, y);
                g.fillOval(t - dotSize / 2, y - dotSize / 2, dotSize, dotSize);
            }
            ymult = .75 * halfPanel;
            midy = ((panelHeight * 5) / 2);
            for (i = -2; i <= 2; i++) {
                g.setColor((i == 0) ? gray2 : gray1);
                g.drawLine(0, midy + (i * (int) ymult) / 2, winSize.width, midy
                        + (i * (int) ymult) / 2);
            }
            ymult /= pi;
            for (i = 0; i != terms; i++) {
                int t = termWidth * i + termWidth / 2;
                int y = midy - (int) (phasecoef[i] * ymult);
                g.setColor(i == selectedCoef ? Color.yellow : Color.white);
                g.drawLine(t, midy, t, y);
                g.fillOval(t - dotSize / 2, y - dotSize / 2, dotSize, dotSize);
            }
            //Graphics realg = cv.getGraphics();

            // realg.drawImage(dbimage, 0, 0, this);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    int getTermWidth() {
        int terms = getValueSetter(0).getValueAsInt();
        int termWidth = winSize.width / terms;
        int maxTermWidth = winSize.width / 30;
        if (termWidth > maxTermWidth) termWidth = maxTermWidth;
        termWidth &= ~1;
        return termWidth;
    }

    void edit(MouseEvent e) {
        if (selection == SEL_NONE) return;
        int x = e.getX();
        int y = e.getY();
        switch (selection) {
        case SEL_MAG:
            editMag(x, y);
            break;
        case SEL_FUNC:
            editFunc(x, y);
            break;
        case SEL_PHASE:
            editPhase(x, y);
            break;
        }
    }

    void editMag(int x, int y) {
        if (selectedCoef == -1) return;
        int panelHeight = getPanelHeight();
        double ymult = 1.2 * panelHeight / 2;
        double midy = ((panelHeight * 3) / 2) + (int) ymult / 2;
        double coef = -(y - midy) / ymult;
        if (selectedCoef > 0) {
            if (coef < 0) coef = 0;
        } else if (coef < -1) coef = -1;
        if (coef > 1) coef = 1;
        if (magcoef[selectedCoef] == coef) return;
        magcoef[selectedCoef] = coef;
        updateGame();
        cv.repaint();
    }

    void editFunc(int x, int y) {
        if (dragX == x) {
            editFuncPoint(x, y);
            dragY = y;
        } else {
            // need to draw a line from old x,y to new x,y and
            // call editFuncPoint for each point on that line. yuck.
            int x1 = (x < dragX) ? x : dragX;
            int y1 = (x < dragX) ? y : dragY;
            int x2 = (x > dragX) ? x : dragX;
            int y2 = (x > dragX) ? y : dragY;
            dragX = x;
            dragY = y;
            for (x = x1; x <= x2; x++) {
                y = y1 + (y2 - y1) * (x - x1) / (x2 - x1);
                editFuncPoint(x, y);
            }
        }
    }

    void editFuncPoint(int x, int y) {
        int panelHeight = getPanelHeight();
        int midy = panelHeight / 2;
        int halfPanel = panelHeight / 2;
        int periodWidth = winSize.width / 3;
        double ymult = .75 * halfPanel;
        int lox = (x % periodWidth) * sampleCount / periodWidth;
        int hix = (((x % periodWidth) + 1) * sampleCount / periodWidth) - 1;
        double val = (midy - y) / ymult;
        if (val > 1) val = 1;
        if (val < -1) val = -1;
        for (; lox <= hix; lox++)
            func[lox] = val;
        func[sampleCount] = func[0];
        updateGame();
        cv.repaint();
    }

    void editPhase(int x, int y) {
        if (selectedCoef == -1) return;
        int panelHeight = getPanelHeight();
        double ymult = .75 * panelHeight / 2;
        double midy = ((panelHeight * 5) / 2);
        double coef = -(y - midy) * pi / ymult;
        if (coef < -pi) coef = -pi;
        if (coef > pi) coef = pi;
        if (phasecoef[selectedCoef] == coef) return;
        phasecoef[selectedCoef] = coef;
        updateGame();
        cv.repaint();
    }

    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {
        updateGame();
        cv.repaint();
    }

    public void componentResized(ComponentEvent e) {
        handleResize();
        updateGame();
        cv.repaint(100);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == triangleButton) {
            doTriangle();
            updateGame();
            cv.repaint();
        }
        if (e.getSource() == sineButton) {
            doSine();
            updateGame();
            cv.repaint();
        }
        if (e.getSource() == rectButton) {
            doRect();
            updateGame();
            cv.repaint();
        }
        if (e.getSource() == fullRectButton) {
            doFullRect();
            updateGame();
            cv.repaint();
        }
        if (e.getSource() == squareButton) {
            doSquare();
            updateGame();
            cv.repaint();
        }
        if (e.getSource() == noiseButton) {
            doNoise();
            updateGame();
            cv.repaint();
        }
        if (e.getSource() == blankButton) {
            doBlank();
            updateGame();
            cv.repaint();
        }
        if (e.getSource() == sawtoothButton) {
            doSawtooth();
            updateGame();
            cv.repaint();
        }
        if (e.getSource() == clipButton) {
            doClip();
            updateGame();
            cv.repaint();
        }
        if (e.getSource() == quantizeButton) {
            doQuantize();
            updateGame();
            cv.repaint();
        }
        if (e.getSource() == resampleButton) {
            doResample();
            updateGame();
            cv.repaint();
        }
        if (e.getSource() == playButton) {
            doPlay();
        }
    }

    public void mouseDragged(MouseEvent e) {
        dragging = true;
        edit(e);
    }

    public void mouseMoved(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) return;
        int x = e.getX();
        int y = e.getY();
        dragX = x;
        dragY = y;
        int panelHeight = getPanelHeight();
        int oldCoef = selectedCoef;
        selectedCoef = -1;
        selection = 0;
        if (y < panelHeight) selection = SEL_FUNC;
        if (y >= panelHeight && y < panelHeight * 3) {
            int termWidth = getTermWidth();
            selectedCoef = x / termWidth;
            if (selectedCoef > getValueSetter(0).getValueAsInt())
                    selectedCoef = -1;
            if (selectedCoef != -1)
                    selection = (y >= panelHeight * 2) ? SEL_PHASE : SEL_MAG;
        }
        if (selectedCoef != oldCoef)
        // updateGame();
                cv.repaint();
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0 && selectedCoef != -1) {
            getValueSetter(0).setValue(selectedCoef + 1);
            updateGame();
            cv.repaint();
        }
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0) return;
        dragging = true;
        edit(e);
    }

    public void mouseReleased(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0) return;
        dragging = false;
        if (selection == SEL_FUNC) transform();
        else if (selection != SEL_NONE) doSetFunc();
        updateGame();
        cv.repaint();
    }

    /*public boolean handleEvent(Event ev) {
        if (ev.id == Event.WINDOW_DESTROY) {
            //applet.destroyFrame();
            return true;
        }
        return applet.handleEvent(ev);
	 //return applet.handleEvent(ev);
	//return applet.processEvent(ev);
    }*/


    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        updateGame();
        cv.repaint();
    }

}