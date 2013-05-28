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

package edu.ucla.stat.SOCR.analyses.util.moduls.frm.children;

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

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.FrmPrincipalDesk;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.XYBox;
import edu.ucla.stat.SOCR.analyses.util.parser.EscalaFigures;
import edu.ucla.stat.SOCR.analyses.util.parser.EscaladoBox;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Cercle;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Escala;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Linia;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Marge;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.NomsDendo;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.NomsLabelsEscala;
import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.tipus.rotacioNoms;
import edu.ucla.stat.SOCR.analyses.util.utils.BoxFont;
import edu.ucla.stat.SOCR.analyses.util.definicions.BoxContainer;
import edu.ucla.stat.SOCR.analyses.util.definicions.Config;
import edu.ucla.stat.SOCR.analyses.util.definicions.Dimensions;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Dendrogram frame
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class FrmPiz extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int CERCLE = 0;
	private static final int LINIA = 1;
	private static final int MARGE = 2;
	private ActionListener al;
	private JPopupMenu menu;

	private EscaladoBox parserDendograma = null;
	private EscaladoBox parserBulles = null;
	private EscaladoBox parserEscala = null;
	private EscaladoBox parserNoms = null;
	private EscaladoBox parserLbl = null;

	// numeros que acompanyen a l'escala
	double width_lbl_escala = 0.0;
	double height_lbl_escala = 0.0;

	// noms dels nodes
	double width_nom_nodes = 0.0;
	double height_nom_nodes = 0.0;

	// dibuixa els cercles
	double height_butlles = 0.0;
	double width_butlles = 0.0;

	// dibuixa l'escala
	double width_escala = 0.0;
	double height_escala = 0.0;

	// dibuixa dendo
	double width_dendograma = 0.0;
	double height_dendograma = 0.0;

	private Config cfg = null;

	private int numClusters;
	private double radi;

	/*
	 * del dendograma nomes mostrarem les figures o la part que estigui dins
	 * d'aquests rangs
	 */
	private double val_Max_show;
	private double val_Min_show;

	private Orientation orientacioClusters = Orientation.NORTH;
	private rotacioNoms orientacioNoms = rotacioNoms.HORITZ;
	private String max_s = "";
	private LinkedList<?>[] figures = { new LinkedList<Cercle>(),
			new LinkedList<Linia>(), new LinkedList<Marge>() };
	protected FrmPiz frmpiz;
	private final FrmPrincipalDesk frm; // main window

	public FrmPiz(final FrmPrincipalDesk f) {
		super();
		frm = f;
		this.initComponentsMenu();
		this.frmpiz = this;
	}

	public void initComponentsMenu() {  // changed to protected
		al = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				String errMsg;

				if (evt.getActionCommand().equals(Language.getLabel(96))) {
					// SAVE TO JPG
					try {
						final BufferedImage buff = FrmPiz.this.dibu();
						frm.savePicture(buff, "jpg");
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						FesLog.LOG
								.throwing("FrmPiz", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(
								frm.getPan_Desk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(97))) {
					// SAVE TO PNG
					try {
						final BufferedImage buff = FrmPiz.this.dibu();
						frm.savePicture(buff, "png");
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						JOptionPane.showInternalMessageDialog(
								frm.getPan_Desk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(95))) {
					// VIEW TREE
					try {
						new PrnArrelHTML(cfg.getMatriu().getArrel(),
								cfg.getPrecision(), cfg);
					} catch (Exception e) {
						FesLog.LOG
								.throwing("FrmPiz", "initComponentsMenu()", e);
						errMsg = Language.getLabel(76);
						JOptionPane.showMessageDialog(frm.getPan_Desk(),
								errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(98))) {
					// SAVE TO TXT
					try {
						frm.saveTXT(cfg.getMatriu().getArrel(),
								cfg.getPrecision(), cfg.getTipusMatriu());
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						FesLog.LOG
								.throwing("FrmPiz", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(
								frm.getPan_Desk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(87))) {
					// SAVE TO NEWICK
					try {
						frm.saveNewick(cfg.getMatriu().getArrel(),
								cfg.getPrecision(), cfg.getTipusMatriu());
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						FesLog.LOG
								.throwing("FrmPiz", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(
								frm.getPan_Desk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand().equals(Language.getLabel(99))) {
					// SAVE TO EPS
					try {
						frm.savePostSript(frmpiz);
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						FesLog.LOG
								.throwing("FrmPiz", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(
								frm.getPan_Desk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand()
						.equals(Language.getLabel(116))) {
					// SAVE ULTRAMETRIC AS TXT
					try {
						frm.saveUltrametricTXT();
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						FesLog.LOG
								.throwing("FrmPiz", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(
								frm.getPan_Desk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (evt.getActionCommand()
						.equals(Language.getLabel(117))) {
					// SHOW ULTRAMETRIC ERRORS
					try {
						frm.showUltrametricErrors();
					} catch (Exception e) {
						errMsg = Language.getLabel(81);
						FesLog.LOG
								.throwing("FrmPiz", "initComponentsMenu()", e);
						JOptionPane.showInternalMessageDialog(
								frm.getPan_Desk(), errMsg, "MultiDendrograms",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};

		menu = new JPopupMenu();
		final JMenuItem me0 = new JMenuItem();
		final JMenuItem me1 = new JMenuItem();
		final JMenuItem me2 = new JMenuItem();
		final JMenuItem me3 = new JMenuItem();
		final JMenuItem me4 = new JMenuItem();
		final JMenuItem me5 = new JMenuItem();
		final JMenuItem me6 = new JMenuItem();
		final JMenuItem me7 = new JMenuItem();

		me0.setText(Language.getLabel(87)); // save newick
		me1.setText(Language.getLabel(95)); // show dendr. details
		me2.setText(Language.getLabel(98)); // save txt
		me3.setText(Language.getLabel(96)); // save jpg
		me4.setText(Language.getLabel(97)); // save png
		me5.setText(Language.getLabel(99)); // save eps
		me6.setText(Language.getLabel(116)); // save ultra as txt
		me7.setText(Language.getLabel(117)); // show ultra details

		me0.addActionListener(al);
		me1.addActionListener(al);
		me2.addActionListener(al);
		me3.addActionListener(al);
		me4.addActionListener(al);
		me5.addActionListener(al);
		me6.addActionListener(al);
		me7.addActionListener(al);

		menu.add(me7);
		menu.add(me1);
		menu.addSeparator();
		menu.add(me6);
		menu.add(me2);
		menu.add(me0);
		menu.addSeparator();
		menu.add(me3);
		menu.add(me4);
		menu.add(me5);

		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	@Override
	protected void processMouseEvent(final MouseEvent evt) {
		if (evt.isPopupTrigger())
			menu.show(evt.getComponent(), evt.getX(), evt.getY());
		else
			super.processMouseEvent(evt);
	}

	public void setConfig(final Config cfg) {
		this.cfg = cfg;

		radi = cfg.getRadi();
		numClusters = cfg.getMatriu().getArrel().getFills();
		orientacioClusters = cfg.getOrientacioDendo();
		orientacioNoms = cfg.getOrientacioNoms();

		val_Max_show = cfg.getValorMaxim();
		val_Min_show = cfg.getValorMinim();
	}

	public void setFigures(final LinkedList[] lst) {
		setFigura(lst);
		FesLog.LOG.finest("Assinada les figures: (" + lst[CERCLE].size() + ", "
				+ lst[LINIA].size() + ", " + lst[MARGE].size() + ")");
	}

	public LinkedList<Object>[] getFigures() {
		return getFigura();
	}

	@Override
	public void update(final Graphics arg0) {
		super.update(arg0);
	}

	private void setAmplades(final Graphics2D g) {
		final Orientation or = cfg.getOrientacioDendo();

		/* dendrogram */
		if (Orientation.NORTH.equals(or) || Orientation.SOUTH.equals(or)) {
			width_dendograma = this.AmpladaBoxClusters();
			height_dendograma = val_Max_show - val_Min_show;
		} else {
			width_dendograma = val_Max_show - val_Min_show;
			height_dendograma = this.AmpladaBoxClusters();
		}

		/* show the scale */
		if (cfg.getConfigMenu().isEscalaVisible()) {
			/* size of the scale */
			if (Orientation.NORTH.equals(or) || Orientation.SOUTH.equals(or)) {
				width_escala = 2 * radi; // east and west
				height_escala = val_Max_show - val_Min_show;
			} else {
				height_escala = 2 * radi; // north and south
				width_escala = val_Max_show - val_Min_show;
			}
		} else {
			width_escala = 0;
			height_escala = 0;
		}

		/* show the bullets */
		double rr = cfg.getConfigMenu().getRadiBullets();
		if ((rr = cfg.getConfigMenu().getRadiBullets()) > 0) {
			if (Orientation.NORTH.equals(or) || Orientation.SOUTH.equals(or)) {
				width_butlles = this.AmpladaBoxClusters();
				height_butlles = 2 * rr;
			} else {
				width_butlles = 2 * rr;
				height_butlles = this.AmpladaBoxClusters();
			}
		} else {
			width_butlles = 0;
			height_butlles = 0;
		}

		/* show the labels of the scale */
		if (cfg.getConfigMenu().isEtiquetaEscalaVisible()) {
			final BoxFont bf = new BoxFont(cfg.getConfigMenu().getFontLabels());
			String txt;
			int ent;
			Dimensions<Double> dim;
			ent = (int) Math.round(val_Max_show);
			txt = Integer.toString(ent);
			if (Orientation.EAST.equals(or) || Orientation.WEST.equals(or)) {
				if (cfg.isTipusDistancia()) {
					dim = bf.getBoxNumberNatural(90, (txt.trim()).length(),
							cfg.getAxisDecimals());
				} else {
					dim = bf.getBoxNumberEnters(90, (txt.trim()).length(),
							cfg.getAxisDecimals());
				}
			} else {
				if (cfg.isTipusDistancia()) {
					dim = bf.getBoxNumberNatural(0, (txt.trim()).length(),
							cfg.getAxisDecimals());
				} else {
					dim = bf.getBoxNumberEnters(0, (txt.trim()).length(),
							cfg.getAxisDecimals());
				}
			}
			width_lbl_escala = dim.getWidth();
			height_lbl_escala = dim.getHeight();
		} else {
			width_lbl_escala = 0;
			height_lbl_escala = 0;
		}

		/* names of the bullets */
		if (cfg.getConfigMenu().isNomsVisibles()) {
			int alf;
			final BoxFont bf = new BoxFont(cfg.getConfigMenu().getFontNoms());
			String tmp;
			Dimensions<Double> dim;

			/* width of names of the bullets */
			if (cfg.getOrientacioNoms().equals(rotacioNoms.HORITZ))
				alf = 0;
			else if (cfg.getOrientacioNoms().equals(rotacioNoms.INCLINAT))
				alf = 45;
			else
				alf = -90;
			if (max_s.equals("")) {
				final Enumeration<String> el = cfg.getHtNoms().elements();
				while (el.hasMoreElements()) {
					tmp = el.nextElement();
					if (tmp.length() > max_s.length())
						max_s = tmp;
				}
			}
			dim = bf.getBox(alf, max_s);

			width_nom_nodes = dim.getWidth();
			height_nom_nodes = dim.getHeight();
		} else {
			width_nom_nodes = 0;
			height_nom_nodes = 0;
		}
	}

	@Override
	public void paint(final Graphics arg0) {
		super.paint(arg0);
		final Graphics2D g2d = (Graphics2D) arg0;
		this.draftDendo(g2d);
	}

	public BufferedImage dibu() {
		Graphics2D g2d;
		final double width_Mon = 50;
		final double height_Mon = 50;
		final BufferedImage buff = new BufferedImage((int) width_Mon,
				(int) height_Mon, BufferedImage.TYPE_INT_RGB);
		g2d = buff.createGraphics();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, (int) width_Mon, (int) height_Mon);
		this.draftDendo(g2d);
		g2d.dispose();

		return buff;
	}

	private void draftDendo(final Graphics2D g2d) {
		BoxContainer boxDendograma, boxBulles, boxEscala, boxEscalalbl, boxNoms;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		/* Symmetric frame where it won't be able to paint anything */
		final double inset_Mon = Parametres_Inicials.getMarco();// 15

		/* World size */
		final double width_Mon = this.getSize().getWidth();
		final double height_Mon = this.getSize().getHeight();

		// recuperem les amplades
		this.setAmplades(g2d);

		// mides del box
		Dimensions<Double> m_d, m_b, m_n, m_e, m_l;

		m_d = new Dimensions<Double>(width_dendograma, height_dendograma);
		m_b = new Dimensions<Double>(width_butlles, height_butlles);
		m_n = new Dimensions<Double>(width_nom_nodes, height_nom_nodes);
		m_e = new Dimensions<Double>(width_escala, height_escala);
		m_l = new Dimensions<Double>(width_lbl_escala, height_lbl_escala);

		/* Calcula l'espai lliure i ubica els box en pantalla */
		final XYBox posbox = new XYBox(cfg, inset_Mon, width_Mon, height_Mon,
				m_d, m_b, m_n, m_e, m_l);

		// definim els box
		boxDendograma = posbox.getBoxDendo();
		boxBulles = posbox.getBoxBulles();
		boxEscala = posbox.getBoxEscala();
		boxEscalalbl = posbox.getBoxLabelsEscala();
		boxNoms = posbox.getBoxNames();

		// els ubiquem a la pantalla
		this.DesplacaPantalla(boxDendograma, height_Mon);
		this.DesplacaPantalla(boxBulles, height_Mon);
		this.DesplacaPantalla(boxEscala, height_Mon);
		this.DesplacaPantalla(boxEscalalbl, height_Mon);
		this.DesplacaPantalla(boxNoms, height_Mon);

		// invertim l'eix de creixement de les y
		g2d.scale(1, -1);
		g2d.setBackground(Color.GREEN);

		/*
		 * calcula el factor que permet passar de coordenades mon a coordenades
		 * pantalla
		 */
		parserDendograma = new EscaladoBox(boxDendograma);
		if (cfg.getConfigMenu().getRadiBullets() > 0)
			parserBulles = new EscaladoBox(boxBulles);
		if (cfg.getConfigMenu().isNomsVisibles())
			parserNoms = new EscaladoBox(boxNoms);
		if (cfg.getConfigMenu().isEscalaVisible())
			parserEscala = new EscaladoBox(boxEscala);
		if (cfg.getConfigMenu().isEtiquetaEscalaVisible())
			parserLbl = new EscaladoBox(boxEscalalbl);

		// rang i tipus de dades del que hem de representar
		final EscalaFigures ef = new EscalaFigures(val_Max_show, val_Min_show,
				cfg.getTipusMatriu(), cfg.getPrecision());

		Marge m;
		final Iterator<Marge> itm = ef.ParserMarge(getFigura()[MARGE])
				.iterator();
		while (itm.hasNext()) {
			m = itm.next();
			m.setEscala(parserDendograma);
			m.setColor(cfg.getConfigMenu().getColorMarge());
			m.setFilled(true);
			m.dibuixa(g2d, orientacioClusters);
		}

		Linia lin;
		final Iterator<Linia> it = ef.ParserLinies(getFigura()[LINIA])
				.iterator();
		while (it.hasNext()) {
			lin = it.next();
			lin.setEscala(parserDendograma);
			lin.dibuixa(g2d, orientacioClusters);
		}

		final Iterator<Marge> itm2 = ef.ParserMarge(getFigura()[MARGE])
				.iterator();
		while (itm2.hasNext()) {
			m = itm2.next();
			m.setEscala(parserDendograma);
			m.setColor(cfg.getConfigMenu().getColorMarge());
			m.setFilled(false);
			m.dibuixa(g2d, orientacioClusters);
		}

		if (cfg.getConfigMenu().getRadiBullets() > 0) { // show bullets
			final Iterator<Cercle> itc = getFigura()[CERCLE].iterator();
			while (itc.hasNext()) {
				final Cercle cer = itc.next();
				cer.setEscala(parserBulles);
				cer.dibuixa(g2d, orientacioClusters);
			}
		}

		if (cfg.getConfigMenu().isNomsVisibles()) { // show names
			NomsDendo nomsD;
			nomsD = new NomsDendo(getFigura()[CERCLE], cfg.getTipusMatriu());
			nomsD.setEscala(parserNoms);
			nomsD.setColor(cfg.getConfigMenu().getColorNoms());
			nomsD.setFont(cfg.getConfigMenu().getFontNoms());
			nomsD.dibuixa(g2d, orientacioClusters, orientacioNoms);
		}

		if (cfg.getConfigMenu().isEscalaVisible()) { // show escala
			Escala esc;
			if (orientacioClusters.equals(Orientation.WEST)
					|| orientacioClusters.equals(Orientation.EAST))
				esc = new Escala(boxEscala.getVal_min_X(),
						boxEscala.getVal_max_X(), cfg.getIncrement(),
						cfg.getTics());
			else
				esc = new Escala(boxEscala.getVal_min_Y(),
						boxEscala.getVal_max_Y(), cfg.getIncrement(),
						cfg.getTics());

			esc.setEscala(parserEscala);
			esc.setColor(cfg.getConfigMenu().getColorEix());
			esc.dibuixa(g2d, orientacioClusters, cfg.getTipusMatriu(),
					cfg.getTics());
		}

		if (cfg.getConfigMenu().isEtiquetaEscalaVisible() && cfg.getTics() > 0) { // show
																					// labels
																					// axis
			NomsLabelsEscala nomsEsc;
			if (orientacioClusters.equals(Orientation.WEST)
					|| orientacioClusters.equals(Orientation.EAST)) {
				nomsEsc = new NomsLabelsEscala(boxEscalalbl.getVal_min_X(),
						boxEscalalbl.getVal_max_X(),
						boxEscalalbl.getVal_max_Y(), cfg.getIncrement(),
						cfg.getTics(), cfg.getAxisDecimals());
			} else {
				nomsEsc = new NomsLabelsEscala(boxEscalalbl.getVal_min_Y(),
						boxEscalalbl.getVal_max_Y(),
						boxEscalalbl.getVal_max_X(), cfg.getIncrement(),
						cfg.getTics(), cfg.getAxisDecimals());
			}
			nomsEsc.setEscala(parserLbl);
			nomsEsc.setColor(cfg.getConfigMenu().getColorLabels());
			nomsEsc.setFont(cfg.getConfigMenu().getFontLabels());
			nomsEsc.dibuixa(g2d, orientacioClusters, cfg.getTipusMatriu());
		}
	}

	private double AmpladaBoxClusters() {
		return ((2 * radi * numClusters) + ((numClusters - 1) * radi));
	}

	private void DesplacaPantalla(final BoxContainer b, final double h_mon) {
		double h;
		h = h_mon - b.getCorner_y();
		b.setCorner_y(-h);
	}

	public void setFigura(LinkedList figura[]) {
		this.figures = figura;
	}

	public LinkedList[] getFigura() {
		return figures;
	}

}
