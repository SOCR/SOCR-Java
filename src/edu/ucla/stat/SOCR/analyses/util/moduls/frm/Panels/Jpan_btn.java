/*
 * Copyright (C) Justo Montiel, David Torres, Sergio Gomez, Alberto Fernandez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see
 * <http://www.gnu.org/licenses/>
 */

package edu.ucla.stat.SOCR.analyses.util.moduls.frm.Panels;

import edu.ucla.stat.SOCR.analyses.gui.*;
import edu.ucla.stat.SOCR.analyses.util.importExport.DadesExternes;
import edu.ucla.stat.SOCR.analyses.util.importExport.FitxerDades;
import edu.ucla.stat.SOCR.analyses.util.inicial.Language;

import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import edu.ucla.stat.SOCR.analyses.util.methods.Reagrupa;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmInternalFrame;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.InternalFrameData;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.children.FrmPiz;
import edu.ucla.stat.SOCR.analyses.util.parser.Fig_Pizarra;
import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.tipus.metodo;
import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;
import edu.ucla.stat.SOCR.analyses.util.definicions.Config;
import edu.ucla.stat.SOCR.analyses.util.definicions.MatriuDistancies;

// added to enalbe "Frame"

import edu.ucla.stat.SOCR.analyses.gui.Analysis;
import edu.ucla.stat.SOCR.analyses.gui.AnalysisPanel;
import edu.ucla.stat.SOCR.gui.OKDialog;
import edu.ucla.stat.SOCR.util.EditableHeader;

import java.awt.*;
//import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.*;

// added to get graphPanel

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.example.ChiSquareModelFitExamples;
import edu.ucla.stat.SOCR.analyses.example.ClusteringExamples;
import edu.ucla.stat.SOCR.analyses.xml.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;


import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog.TipLog;  // added

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk;
import edu.ucla.stat.SOCR.analyses.util.inicial.*;

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog;
import edu.ucla.stat.SOCR.analyses.util.inicial.Language;
import edu.ucla.stat.SOCR.analyses.util.inicial.Parametres_Inicials;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


import edu.ucla.stat.SOCR.analyses.util.moduls.frm.children.*;
/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Load and Update buttons
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Jpan_btn extends JPanel implements ActionListener,
		InternalFrameListener, PropertyChangeListener {

        Frame fDialog = new Frame();  // auxiliary for dialogs
        JFrame help = new JFrame("Dendrogram");  // added
        
        private static String action1 = "";
        
	private static final long serialVersionUID = 1L;
	// Desktop where the dendrogram is to be shown
	private final FrmPrincipalDesk fr;

	protected Jpan_btn jb;

	// Text to show in the buttons
	private String strLoad, strUpdate;

	// Buttons Load and Update
	private static JButton btnLoad, btnUpdate;

	// Indicates if the buttons Load or Update are being clicked
	public static boolean buttonClicked = false;

	// Indicate if the text fields have correct values
	public static boolean precisionCorrect = false;
	public static boolean axisMinCorrect = false;
	public static boolean axisMaxCorrect = false;
	public static boolean axisSeparationCorrect = false;
	public static boolean axisEveryCorrect = false;
	public static boolean axisDecimalsCorrect = false;

	// Internal frame currently active
	private FrmInternalFrame currentInternalFrame = null;
        
	// File with the input data
	private static FitxerDades fitx = null;
	private DadesExternes de;

	// Text box for the file name
	private static JTextField txtFileName;

	// MultiDendrogram
	private MatriuDistancies multiDendro = null;

	// Progress bar for MultiDendrogram computation
	private JProgressBar progressBar;

	// Swing Worker MultiDendrogram computation
	public class MDComputation extends SwingWorker<Void, Void> {
		private final String action;
		private final tipusDades typeData;
		private final metodo method;
		private final int precision;
		private final int nbElements;
		private double minBase;

		public MDComputation(final String action, final tipusDades typeData,
				final metodo method, final int precision, final int nbElements,
				double minBase) {
			this.action = action;
			this.typeData = typeData;
			this.method = method;
			this.precision = precision;
			this.nbElements = nbElements;
			this.minBase = minBase;
		}

		@Override
		public Void doInBackground() {
			Reagrupa rg;
			MatriuDistancies mdNew;
			double b;
			int progress;

			// Initialize progress property
			progress = 0;
			setProgress(progress);
			while (multiDendro.getCardinalitat() > 1) {
				try {
					rg = new Reagrupa(multiDendro, typeData, method, precision);
					mdNew = rg.Recalcula();
					multiDendro = mdNew;
					b = multiDendro.getArrel().getBase();
					if ((b < minBase) && (b != 0)) {
						minBase = b;
					}
					progress = 100
							* (nbElements - multiDendro.getCardinalitat())
							/ (nbElements - 1);
					setProgress(progress);
				} catch (final Exception e) {
					showError(e.getMessage());
				}
			}
			return null;
		}

		@Override
		public void done() {
			multiDendro.getArrel().setBase(minBase);
			showCalls(action);
			progressBar.setString("");
			progressBar.setBorderPainted(false);
			progressBar.setValue(0);
			fr.setCursor(null); // turn off the wait cursor
		}
	}

	public Jpan_btn(final FrmPrincipalDesk fr) {
		super();
		this.fr = fr;
		this.jb = this;
		this.getPanel();
		this.setVisible(true);
	}

	private void getPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(Language.getLabel(20))); // File
		final GridBagConstraints c = new GridBagConstraints();
		int gridy = 0;

		// btn load
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		strLoad = Language.getLabel(21); // Load
		btnLoad = new JButton(strLoad);
		btnLoad.addActionListener(this);
		add(btnLoad, c);
		// btn update
		c.gridx = 1;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		strUpdate = Language.getLabel(110); // Update
		btnUpdate = new JButton(strUpdate);
		btnUpdate.addActionListener(this);
		btnUpdate.setEnabled(false);
		add(btnUpdate, c);
		gridy++;

		// txt file name
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		txtFileName = new JTextField();
		txtFileName.setText(Language.getLabel(112)); // No file loaded
		txtFileName.addActionListener(this);
		txtFileName.setEditable(false);
		add(txtFileName, c);
		gridy++;

		// progress bar
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		progressBar.setString("");
		progressBar.setBorderPainted(false);
		progressBar.setValue(0);
		add(progressBar, c);
		gridy++;
	}

	public static void enableUpdate() {
		if (precisionCorrect && axisMinCorrect && axisMaxCorrect
				&& axisSeparationCorrect && axisEveryCorrect
				&& axisDecimalsCorrect) {
			btnUpdate.setEnabled(true);
		} else {
			btnUpdate.setEnabled(false);
		}
	}

	public static String getFileNameNoExt() {
		String name = "";
		if (fitx != null) {
			name = fitx.getNomNoExt();
		}
		return name;
	}

	public static void setFileName(String name) {
		txtFileName.setText(name);
	}

	public MatriuDistancies getMatriu() {
		return de.getMatriuDistancies();
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		String action = null;
		FitxerDades fitxTmp;
		boolean ambDades = false;
		InternalFrameData ifd;
		double minBase;
		MDComputation mdComputation;

		if (evt.getActionCommand().equals(strLoad)) {
			// LOAD
                    
                    if (currentInternalFrame != null)
                        currentInternalFrame = null;
                    
			buttonClicked = true;
			action = "Load";
			// Load data from file
			if (fitx == null) {
				fitxTmp = getFitxerDades();
			} else {
				// Last directory
				fitxTmp = getFitxerDades(fitx.getPath());
			}
			if (fitxTmp == null) {
				// Cancel pressed
				ambDades = false;
			} else {
				fitx = fitxTmp;
				ambDades = true;
			}
		} else if (evt.getActionCommand().equals(strUpdate)) {
			// UPDATE        
			buttonClicked = true;
                        
                        if (action1 == "Calculate")
                        {
                            action = "CalUpdate";

                            doLoad(action);
                            return;
                        }
                        
			ifd = currentInternalFrame.getInternalFrameData();
			if ((Jpan_Menu.getTypeData() == ifd.getTypeData())
					&& (Jpan_Menu.getMethod() == ifd.getMethod())
					&& (Jpan_Menu.getPrecision() == ifd.getPrecision())) {
				action = "Redraw";
			} else {
				action = "Reload";
			}
			ambDades = true;
		}
		if (ambDades && (action.equals("Load") || action.equals("Reload"))) {
			try {
				de = new DadesExternes(fitx);
				if (action.equals("Load")) {
					Jpan_Menu.setPrecision(de.getPrecisio());
				}
				multiDendro = null;
				try {
					multiDendro = de.getMatriuDistancies();
					minBase = Double.MAX_VALUE;
					progressBar.setBorderPainted(true);
					progressBar.setString(null);
					fr.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					// Instances of javax.swing.SwingWorker are not reusable,
					// so we create new instances as needed.
					mdComputation = new MDComputation(action,
							Jpan_Menu.getTypeData(), Jpan_Menu.getMethod(),
							Jpan_Menu.getPrecision(),
							multiDendro.getCardinalitat(), minBase);
					mdComputation.addPropertyChangeListener(this);
					mdComputation.execute();
				} catch (final Exception e2) {
					buttonClicked = false;
					showError(e2.getMessage());
				}
			} catch (Exception e1) {
				buttonClicked = false;
				showError(e1.getMessage());
			}
		} else if (ambDades && action.equals("Redraw")) {
			showCalls(action);
		} else {
			buttonClicked = false;
		}
	}

	private void showCalls(final String action) {
                
		if (action.equals("Reload") || action.equals("Redraw") || action.equals("CalRedraw") || action.equals("CalReload") ) {
			currentInternalFrame.doDefaultCloseAction();
		}
                
                show(action, Jpan_Menu.getMethod(), Jpan_Menu.getPrecision());
                
                if (currentInternalFrame == null)
                {
                    txtFileName.setText(fitx.getNom());
                    btnUpdate.setEnabled(true);
                    buttonClicked = false;
                    return;
                }
                
                currentInternalFrame.doDefaultCloseAction();
                
                show(action, Jpan_Menu.getMethod(), Jpan_Menu.getPrecision());
                txtFileName.setText(fitx.getNom());
                btnUpdate.setEnabled(true);
                buttonClicked = false;
                
	}

	public void show(String action, final metodo method, final int precision) {
		boolean isUpdate;
                boolean isCalUpdate;
		FrmInternalFrame pizarra;
		Config cfg;
		InternalFrameData ifd;
		FrmPiz fPiz;
		Fig_Pizarra figPizarra;

		isUpdate = !action.equals("Load");
                isCalUpdate = !action.equals("Calculate");
                
                if (action == "Calculate" || action == "CalReload" || action == "CalRedraw")
                {
                    try {
			pizarra = fr.createInternalFrame(isCalUpdate, method.name());
			cfg = fr.getConfig();
			cfg.setPizarra(pizarra);
			cfg.setFitxerDades(fitx);
			cfg.setMatriu(multiDendro);
			cfg.setHtNoms(de.getTaulaNoms());
			if (!cfg.isTipusDistancia()) {
				if (cfg.getOrientacioDendo().equals(Orientation.NORTH)) {
					cfg.setOrientacioDendo(Orientation.SOUTH);
				} else if (cfg.getOrientacioDendo().equals(Orientation.SOUTH)) {
					cfg.setOrientacioDendo(Orientation.NORTH);
				} else if (cfg.getOrientacioDendo().equals(Orientation.EAST)) {
					cfg.setOrientacioDendo(Orientation.WEST);
				} else if (cfg.getOrientacioDendo().equals(Orientation.WEST)) {
					cfg.setOrientacioDendo(Orientation.EAST);
				}
			}
			ifd = new InternalFrameData(fitx, multiDendro, "");
			pizarra.setInternalFrameData(ifd);
			// Title for the child window
			pizarra.setTitle(fitx.getNom() + " - " + pizarra.getTitle());
			// Load the window to show the dendrogram
			fPiz = new FrmPiz(fr);
			pizarra.add(fPiz);
			// Call Jpan_Menu -> internalFrameActivated()
                        
			pizarra.setVisible(true); 
			if (action.equals("Calculate") || action.equals("CalReload")) {
				Jpan_Menu.ajustaValors(cfg);
			}
                        
                        fr.setCurrentFrame(pizarra);
                        // Convert tree into figures
			figPizarra = new Fig_Pizarra(multiDendro.getArrel(), cfg);
			// Pass figures to the window
			fPiz.setFigures(figPizarra.getFigures());
			fPiz.setConfig(cfg);
                    }   catch (final Exception e) {
                            e.printStackTrace();
                            showError(e.getMessage());
                    }
                    return;
                }
                
		try {
			pizarra = fr.createInternalFrame(isUpdate, method.name());
			cfg = fr.getConfig();
			cfg.setPizarra(pizarra);
			cfg.setFitxerDades(fitx);
			cfg.setMatriu(multiDendro);
			cfg.setHtNoms(de.getTaulaNoms());
			if (!cfg.isTipusDistancia()) {
				if (cfg.getOrientacioDendo().equals(Orientation.NORTH)) {
					cfg.setOrientacioDendo(Orientation.SOUTH);
				} else if (cfg.getOrientacioDendo().equals(Orientation.SOUTH)) {
					cfg.setOrientacioDendo(Orientation.NORTH);
				} else if (cfg.getOrientacioDendo().equals(Orientation.EAST)) {
					cfg.setOrientacioDendo(Orientation.WEST);
				} else if (cfg.getOrientacioDendo().equals(Orientation.WEST)) {
					cfg.setOrientacioDendo(Orientation.EAST);
				}
			}
			ifd = new InternalFrameData(fitx, multiDendro);
			pizarra.setInternalFrameData(ifd);
			// Title for the child window
			pizarra.setTitle(fitx.getNom() + " - " + pizarra.getTitle());
			// Load the window to show the dendrogram
			fPiz = new FrmPiz(fr);
			pizarra.add(fPiz);
			// Call Jpan_Menu -> internalFrameActivated()
                        
			pizarra.setVisible(true); 
			if (action.equals("Load") || action.equals("Reload")) {
				Jpan_Menu.ajustaValors(cfg);
			}
                        
                        fr.setCurrentFrame(pizarra);
                        // Convert tree into figures
			figPizarra = new Fig_Pizarra(multiDendro.getArrel(), cfg);
			// Pass figures to the window
			fPiz.setFigures(figPizarra.getFigures());
			fPiz.setConfig(cfg);
		} catch (final Exception e) {
			e.printStackTrace();
			showError(e.getMessage());
		}
	}

	private FitxerDades getFitxerDades() {
		return this.getFitxerDades(System.getProperty("user.dir"));
	}

        private FitxerDades getFitxerDades(final String sPath) {
		final FileDialog fd = new FileDialog(fDialog, Language.getLabel(9),
				FileDialog.LOAD);
		FitxerDades fitx;

		fitx = new FitxerDades();
		fd.setDirectory(sPath);
		fd.setVisible(true);
		if (fd.getFile() == null) {
			fitx = null;
		} else {
			fitx.setNom(fd.getFile());
			fitx.setPath(fd.getDirectory());
		}
		return fitx;
	}
        
	private FitxerDades getFitxerDades1(final String sPath) {
		
		FitxerDades fitx;

		fitx = new FitxerDades();

		return fitx;
	}

	private void showError(final String msg) {
		JOptionPane.showMessageDialog(null, msg, Language.getLabel(7),
				JOptionPane.ERROR_MESSAGE);
	}


	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		InternalFrameData ifd;
                
		currentInternalFrame = (FrmInternalFrame) e.getSource();
		btnUpdate.setEnabled(true);
		if (!buttonClicked) {
			fr.setCurrentFrame(currentInternalFrame);
			ifd = currentInternalFrame.getInternalFrameData();
			de = ifd.getDadesExternes();
			fitx = de.getFitxerDades();
			setFileName(fitx.getNom());
			multiDendro = ifd.getMultiDendrogram();
			Jpan_Menu.setConfigPanel(ifd);
		}
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		FrmInternalFrame.decreaseOpenFrameCount();
		btnUpdate.setEnabled(false);
		txtFileName.setText(Language.getLabel(112)); // No file loaded
		if (!buttonClicked) {
			Jpan_Menu.clearConfigPanel();
		}
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == "progress") {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}
        
        public void doLoad(String action){
            
                action1 = "Calculate";
            
		FitxerDades fitxTmp;
		boolean ambDades = false;
		InternalFrameData ifd;
		double minBase;
		MDComputation mdComputation;
                // LOAD
                if (action == "Calculate")
                {
                    if (currentInternalFrame != null)
                    currentInternalFrame = null;
                
                    
			buttonClicked = true;
			// Load data from file
			if (fitx == null) {
				fitxTmp = getFitxerDades1("");
			} else {
				// Last directory
				fitxTmp = getFitxerDades1(fitx.getPath());
			}
			if (fitxTmp == null) {
				// Cancel pressed
				ambDades = false;
			} else {
				fitx = fitxTmp;
				ambDades = true;
			}
                } else if (action == "CalUpdate") {
			// UPDATE        
			buttonClicked = true;
                        
			ifd = currentInternalFrame.getInternalFrameData();
			if ((Jpan_Menu.getTypeData() == ifd.getTypeData())
					&& (Jpan_Menu.getMethod() == ifd.getMethod())
					&& (Jpan_Menu.getPrecision() == ifd.getPrecision())) {
				action = "CalRedraw";
			} else {
				action = "CalReload";
			}
			ambDades = true;
		}
                if (ambDades && (action.equals("Calculate") || action.equals("CalReload"))) {
			try {
				de = new DadesExternes(fitx, "");
				if (action.equals("Calculate")) {
					Jpan_Menu.setPrecision(de.getPrecisio());
				}
				multiDendro = null;
				try {
					multiDendro = de.getMatriuDistancies();
					minBase = Double.MAX_VALUE;
					progressBar.setBorderPainted(true);
					progressBar.setString(null);
					fr.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					// Instances of javax.swing.SwingWorker are not reusable,
					// so we create new instances as needed.
					mdComputation = new MDComputation(action,
							Jpan_Menu.getTypeData(), Jpan_Menu.getMethod(),
							Jpan_Menu.getPrecision(),
							multiDendro.getCardinalitat(), minBase);
					mdComputation.addPropertyChangeListener(this);
					mdComputation.execute();
				} catch ( Exception e2) {
					buttonClicked = false;
					showError(e2.getMessage());
				}
			} catch (Exception e1) {
				buttonClicked = false;
				showError(e1.getMessage());
			}
		} else if (ambDades && action.equals("CalRedraw")) {
			showCalls(action);
		} else {
			buttonClicked = false;
		}
	}

}
