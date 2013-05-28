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
/**
 * AnalysisPanel.java 0.1 06/05/03 Copyright (C) 2003 Ivo D. Dinov, Ph.D., Jianming
 * He This code was originially written by Ivo, Jianming modified it.
 */

package edu.ucla.stat.SOCR.analyses.jri.gui;

import java.awt.*;
import java.awt.event.*;
import java.text.*;

import javax.swing.*;
import edu.ucla.stat.SOCR.core.SOCRApplet;
import edu.ucla.stat.SOCR.core.SOCRAnalyses;
import edu.ucla.stat.SOCR.analyses.model.*;

public class AnalysisPanel extends JPanel //implements MouseListener
    {
    private int leftMargin;
    private int rightMargin;
    private int topMargin;
    private int bottomMargin;
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    /**
     *
     * @uml.property name="analysis"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private edu.ucla.stat.SOCR.analyses.jri.gui.Analysis analysis;

    /**
     *
     * @uml.property name="domain"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    //private Domain domain;


    private int type;
    private int xPosition = 0 ;
    private int yPosition = 0 ;
    private double left;
    private double right;
    private double leftx;
    private double rightx;

    /**
     *
     * @uml.property name="container"
     * @uml.associationEnd multiplicity="(1 1)" inverse="graphPanel:edu.ucla.stat.SOCR.core.SOCRDistributions"
     */
    SOCRAnalyses container;


    private Font font = new Font("sansserif", Font.PLAIN, 12);


    public DecimalFormat decimalFormat = new DecimalFormat();

   // public AnalysisPanel(SOCRDistributions container) {
    public AnalysisPanel(SOCRAnalyses container) {
        this.container = container;
        //setScale(0, 1, 0, 1);
        //setMargins(30, 30, 30, 30);
        setBackground(Color.BLUE);
        setFont(font);
        //addMouseMotionListener(this);
        //addMouseListener(this);
    }

    /**
     *
     * @uml.property name="analysis"
     */

   public void setAnalysis(Analysis analysis) {
        if (analysis != null) {
            analysis = analysis;
            //domain = analysis.getDomain();
            //type = analysis.getType();
            //left = domain.getLowerBound();
            //right = domain.getUpperBound();

        }
        repaint();
    }

    /** This method sets the minimum and maximum values on the x and y axes */
}
