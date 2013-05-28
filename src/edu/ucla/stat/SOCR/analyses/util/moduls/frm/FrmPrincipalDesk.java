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

package edu.ucla.stat.SOCR.analyses.util.moduls.frm;

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog;
import edu.ucla.stat.SOCR.analyses.util.inicial.Language;
import edu.ucla.stat.SOCR.analyses.util.inicial.Parametres_Inicials;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.Panels.Jpan_Menu;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.Panels.Jpan_btn;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.Panels.Jpan_btnExit;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.children.DeviationMeasuresBox;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.children.FrmPiz;
import edu.ucla.stat.SOCR.analyses.util.parser.ToNewick;
import edu.ucla.stat.SOCR.analyses.util.parser.ToTXT;
import edu.ucla.stat.SOCR.analyses.util.parser.Ultrametric;
import edu.ucla.stat.SOCR.analyses.util.parser.EPS.EPSExporter;
import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;
import edu.ucla.stat.SOCR.analyses.util.utils.MiMath;
import edu.ucla.stat.SOCR.analyses.util.definicions.Cluster;
import edu.ucla.stat.SOCR.analyses.util.definicions.Config;

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
/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Main frame window
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class FrmPrincipalDesk extends JPanel {


    public Frame fDialog = new Frame();  // added as dummy frame

	private static final long serialVersionUID = 1L;

	public final JPanel pan_West, pan_Exit, pan_Center;

	private final JDesktopPane pan_Desk;

	private final Jpan_btn panBtn;

	private final Jpan_Menu panMenu;

	private Config cfg;
	private JInternalFrame currentFpiz;

	public FrmPrincipalDesk(final String title) {
		FesLog.LOG.info("Creating a New Instance of the Hierachical Clustering Algorithm");

		final int width_win = Parametres_Inicials.getWidth_frmPrincipal();
		final int height_win = Parametres_Inicials.getHeight_frmPrincipal();

		pan_Center = new JPanel();
		pan_Center.setLayout(new BorderLayout());

		pan_Desk = new JDesktopPane();
		pan_Desk.setBackground(Color.LIGHT_GRAY);
		pan_Desk.setBorder(BorderFactory.createTitledBorder(""));

		pan_Center.add(pan_Desk, BorderLayout.CENTER);

		pan_West = new JPanel();
		pan_West.setLayout(new BorderLayout());

		panMenu = new Jpan_Menu(this);
		pan_Exit = new Jpan_btnExit(this);
		panBtn = new Jpan_btn(this);

		pan_West.add(panBtn, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(panMenu);
		pan_West.add(scrollPane, BorderLayout.WEST);
		pan_West.add(pan_Exit, BorderLayout.SOUTH);


		this.setSize(width_win, height_win);
		this.setVisible(true);
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public Config getConfig() {
		cfg = new Config(Jpan_Menu.getCfgPanel());
		cfg.setMatriu(panBtn.getMatriu());
		if (cfg.getValorMaxim() == 0) {
			cfg.getConfigMenu().setValMax(cfg.getCimDendograma());
		}
		return cfg;
	}

	public FrmInternalFrame createInternalFrame(boolean isUpdate,
			String methodName) {
		int x, y, width, height;
		FrmInternalFrame pizarra;

		if (isUpdate) {
			x = currentFpiz.getX();
			y = currentFpiz.getY();
			width = currentFpiz.getWidth();
			height = currentFpiz.getHeight();
		} else {
			x = 0;
			y = 0;
			width = Parametres_Inicials.getWidth_frmDesk();
			height = Parametres_Inicials.getHeight_frmDesk();
		}
		pizarra = new FrmInternalFrame(methodName, isUpdate, x, y);
		pizarra.setSize(width, height);
       
		pizarra.setBackground(Color.BLUE);
		pizarra.setLayout(new BorderLayout());
		pizarra.addInternalFrameListener(panBtn);
                pan_Desk.add(pizarra, BorderLayout.CENTER);
                
		return pizarra;
	}

	public void toGoOut() {
		final String msg = "Are you sure you want to exit?";
		int opt;
		opt = JOptionPane.showConfirmDialog(null, msg, "Font",
				JOptionPane.YES_NO_OPTION);
		if (opt == JOptionPane.YES_OPTION) {
			FesLog.LOG.info("Exit");
			System.exit(0);
		}
	}

	public void savePicture(final BufferedImage buff, final String tipus)
			throws Exception {
		String sPath;
		String sNameNoExt = Jpan_btn.getFileNameNoExt();
		final FileDialog fd = new FileDialog(fDialog,"Unable to load the dendrogram properties" + " "
				+ tipus.toUpperCase(), FileDialog.SAVE);
		fd.setFile(sNameNoExt + "." + tipus);
		fd.setVisible(true);
                fDialog.setVisible(true);

		if (fd.getFile() != null) {
			sPath = fd.getDirectory() + fd.getFile();
			final File fil = new File(sPath);
			try {
				ImageIO.write(buff, tipus, fil);
				FesLog.LOG.info("Imatge Emmagatzemada amb exit");
			} catch (final IOException e) {
				String msg_err = "Save as";
				FesLog.LOG
						.throwing(
								"FrmPrincipalDesk",
								"savePicture(final BufferedImage buff, final String tipus)",
								e);
				throw new Exception(msg_err);
			} catch (Exception e) {
				String msg_err = "Unable to save the image";
				FesLog.LOG
						.throwing(
								"FrmPrincipalDesk",
								"savePicture(final BufferedImage buff, final String tipus)",
								e);
				throw new Exception(msg_err);
			}
		}
	}

	public void savePostSript(FrmPiz frmpiz) throws Exception {
		String sPath;
		String sNameNoExt = Jpan_btn.getFileNameNoExt();
		final FileDialog fd = new FileDialog(fDialog, "Unable to load the dendrogram properties"
				+ " EPS", FileDialog.SAVE);
		fd.setFile(sNameNoExt + ".eps");
		fd.setVisible(true);
                fDialog.setVisible(true);

		if (fd.getFile() != null) {
			sPath = fd.getDirectory() + fd.getFile();
			try {
				new EPSExporter(cfg, frmpiz, sPath);

				FesLog.LOG.info("Imatge EPS emmagatzemada amb exit");
			} catch (Exception e) {
				String msg_err = "Unable to save the image";
				FesLog.LOG.throwing("FrmPrincipalDesk",
						"savePostScript(final BufferedImage buff)", e);
				throw new Exception(msg_err);
			}
		}
	}

	public void saveTXT(Cluster arrel, int precisio, tipusDades tip)
			throws Exception {
		String sPath, msg_box = "" + " TXT";
		String sNameNoExt = Jpan_btn.getFileNameNoExt();
		FileDialog fd = new FileDialog(fDialog, msg_box, FileDialog.SAVE);
		fd.setFile(sNameNoExt + "-tree.txt");
		fd.setVisible(true);
		if (fd.getFile() != null) {
			sPath = fd.getDirectory() + fd.getFile();
			ToTXT saveTXT = new ToTXT(arrel, precisio, tip);
			saveTXT.saveAsTXT(sPath);
		}
	}

	public void saveNewick(Cluster root, int precision, tipusDades typeData)
			throws Exception {
		String msgBox, sPath;
		FileDialog fd;
		double heightBottom, heightMin, heightMax, extraSpace;
		ToNewick toNewick;

		msgBox = "" + " Newick";
		String sNameNoExt = Jpan_btn.getFileNameNoExt();
		fd = new FileDialog(fDialog, msgBox, FileDialog.SAVE);
		fd.setFile(sNameNoExt + "-Newick.txt");
		fd.setVisible(true);
                fDialog.setVisible(true);
		if (fd.getFile() != null) {
			sPath = fd.getDirectory() + fd.getFile();
			if (cfg.getTipusMatriu().equals(tipusDades.DISTANCIA)) {
				heightBottom = 0.0;
			} else {
				heightMin = cfg.getBaseDendograma();
				heightMax = cfg.getCimDendograma();
				extraSpace = (heightMax - heightMin)
						* (0.05 * MiMath.Arodoneix((heightMax - heightMin),
								precision));
				extraSpace = MiMath.Arodoneix(extraSpace, precision);
				heightBottom = heightMax + extraSpace;
			}
			toNewick = new ToNewick(root, precision, typeData, heightBottom);
			toNewick.saveAsNewick(sPath);
		}
	}

	public void saveUltrametricTXT() throws Exception {
		String sPath, msg_box = "" + " TXT";
		String sNameNoExt = Jpan_btn.getFileNameNoExt();
		FileDialog fd = new FileDialog(fDialog, msg_box, FileDialog.SAVE);
		fd.setFile(sNameNoExt + "-ultrametric.txt");
		fd.setVisible(true);
		if (fd.getFile() != null) {
			sPath = fd.getDirectory() + fd.getFile();
			Ultrametric um = new Ultrametric();
			um.saveAsTXT(sPath, cfg.getPrecision());
		}
	}

	public void showUltrametricErrors() {
		DeviationMeasuresBox box = new DeviationMeasuresBox(fDialog);
		box.setVisible(true);
                fDialog.setVisible(true);
	}

	public Jpan_Menu getPan_Menu() {
		return this.panMenu;
	}

	public JDesktopPane getPan_Desk() {
		return this.pan_Desk;
	}

	public void setCurrentFrame(JInternalFrame internalFrame) {
		this.currentFpiz = internalFrame;
	}

	public Jpan_btn getPwBtn() {
		return this.panBtn;
	}

}
