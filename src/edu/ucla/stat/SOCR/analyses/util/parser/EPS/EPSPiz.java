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

package edu.ucla.stat.SOCR.analyses.util.parser.EPS;

import edu.ucla.stat.SOCR.analyses.util.inicial.Parametres_Inicials;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import edu.ucla.stat.SOCR.analyses.util.moduls.frm.XYBox;
import edu.ucla.stat.SOCR.analyses.util.moduls.frm.children.FrmPiz;
import edu.ucla.stat.SOCR.analyses.util.parser.EscalaFigures;
import edu.ucla.stat.SOCR.analyses.util.parser.EscaladoBox;
import edu.ucla.stat.SOCR.analyses.util.parser.EPS.figures.CercleEPS;
import edu.ucla.stat.SOCR.analyses.util.parser.EPS.figures.EscalaEPS;
import edu.ucla.stat.SOCR.analyses.util.parser.EPS.figures.LiniaEPS;
import edu.ucla.stat.SOCR.analyses.util.parser.EPS.figures.MargeEPS;
import edu.ucla.stat.SOCR.analyses.util.parser.EPS.figures.NomsDendoEPS;
import edu.ucla.stat.SOCR.analyses.util.parser.EPS.figures.NomsLabelsEscalaEPS;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Cercle;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Linia;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Marge;
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
 * Calculation of the EPS figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class EPSPiz {
	private static final long serialVersionUID = 1L;
	private static final int CERCLE = 0;
	private static final int LINIA = 1;
	private static final int MARGE = 2;

	private EscaladoBox parserDendograma = null;
	private EscaladoBox parserBulles = null;
	private EscaladoBox parserEscala = null;
	private EscaladoBox parserNoms = null;
	private EscaladoBox parserLbl = null;

	// numeros que acompanyen a lescala
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

	private double val_Max_show;
	private double val_Min_show;

	private Orientation orientacioClusters = Orientation.NORTH;
	private rotacioNoms orientacioNoms = rotacioNoms.VERTICAL;
	private String max_s = "";

	private LinkedList figura[] = { new LinkedList<Cercle>(),
			new LinkedList<Linia>(), new LinkedList<Marge>() };

	private FrmPiz frmpiz;
	private final int xmax, ymax;

	public EPSPiz(FrmPiz frmpiz, Config cfg, int xmax, int ymax) {
		this.frmpiz = frmpiz;
		this.figura = frmpiz.getFigures();
		this.xmax = xmax;
		this.ymax = ymax;
		setConfig(cfg);
	}

	public void setConfig(final Config cfg) {
		this.cfg = cfg;

		radi = cfg.getRadi();
		numClusters = cfg.getMatriu().getArrel().getFills();
		orientacioClusters = cfg.getOrientacioDendo();
		orientacioNoms = cfg.getOrientacioNoms();

		if (cfg.getValorMaxim() > 0.0)
			val_Max_show = cfg.getValorMaxim();
		else
			val_Max_show = cfg.getCimDendograma();

		if (cfg.getValorMinim() > 0.0)
			val_Min_show = cfg.getValorMinim();
		else {
			if (cfg.isTipusDistancia())
				val_Min_show = 0.0;
			else
				val_Min_show = cfg.getValorMinim();
		}
	}

	public FrmPiz getFrmpiz() {
		return this.frmpiz;
	}

	public void setFrmpiz(FrmPiz frmpiz) {
		this.frmpiz = frmpiz;
	}

	private double AmpladaBoxClusters() {
		return ((2 * radi * numClusters) + ((numClusters - 1) * radi));
	}

	private void DesplacaPantalla(final BoxContainer b, final double h_mon) {
		double h;
		h = h_mon - b.getCorner_y();
		b.setCorner_y(-h);
	}

	private void setAmplades() {
		final Orientation or = cfg.getOrientacioDendo();

		if (Orientation.NORTH.equals(or) || Orientation.SOUTH.equals(or)) {
			width_dendograma = this.AmpladaBoxClusters();
			height_dendograma = val_Max_show - val_Min_show;
		} else {
			width_dendograma = val_Max_show - val_Min_show;
			height_dendograma = this.AmpladaBoxClusters();
		}

		if (cfg.getConfigMenu().isEscalaVisible()) {

			if (Orientation.NORTH.equals(or) || Orientation.SOUTH.equals(or)) {
				width_escala = 2 * radi;
				height_escala = val_Max_show - val_Min_show;
			} else {
				height_escala = 2 * radi;
				width_escala = val_Max_show - val_Min_show;
			}
		} else {
			width_escala = 0;
			height_escala = 0;
		}

		if ((radi = cfg.getConfigMenu().getRadiBullets()) > 0) {
			if (Orientation.NORTH.equals(or) || Orientation.SOUTH.equals(or)) {
				width_butlles = this.AmpladaBoxClusters();
				height_butlles = radi;// 2 * radi;
			} else {
				width_butlles = radi;// 2 * radi;
				height_butlles = this.AmpladaBoxClusters();
			}
		} else {
			width_butlles = 0;
			height_butlles = 0;
		}

		if (cfg.getConfigMenu().isEtiquetaEscalaVisible()) {
			final BoxFont bf = new BoxFont(cfg.getConfigMenu().getFontLabels());
			String txt;
			int ent;
			Dimensions<Double> dim;
			ent = (int) Math.round(val_Max_show);
			txt = Integer.toString(ent);
			if (Orientation.EAST.equals(or) || Orientation.WEST.equals(or)) {
				if (cfg.isTipusDistancia())
					dim = bf.getBoxNumberNatural(90, (txt.trim()).length(),
							cfg.getAxisDecimals());
				else
					dim = bf.getBoxNumberEnters(90, (txt.trim()).length(),
							cfg.getAxisDecimals());
			} else {
				if (cfg.isTipusDistancia())
					dim = bf.getBoxNumberNatural(0, (txt.trim()).length(),
							cfg.getAxisDecimals());
				else
					dim = bf.getBoxNumberEnters(0, (txt.trim()).length(),
							cfg.getAxisDecimals());
			}
			width_lbl_escala = dim.getWidth();
			height_lbl_escala = dim.getHeight();
		} else {
			width_lbl_escala = 0;
			height_lbl_escala = 0;
		}

		if (cfg.getConfigMenu().isNomsVisibles()) {
			int alf;
			final BoxFont bf = new BoxFont(cfg.getConfigMenu().getFontNoms());
			String tmp;
			Dimensions<Double> dim;

			/* amplades noms bullets */
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

	public void dibuixa() {
		BoxContainer boxDendograma, boxBulles, boxEscala, boxEscalalbl, boxNoms;

		final double inset_Mon = Parametres_Inicials.getMarco(); // 15
		final double width_Mon, height_Mon;

		width_Mon = this.xmax - 72;
		height_Mon = this.ymax - 72;

		// recuperem les amplades
		this.setAmplades();

		// mides del box
		Dimensions<Double> m_d, m_b, m_n, m_e, m_l;

		m_d = new Dimensions<Double>(width_dendograma, height_dendograma);
		m_b = new Dimensions<Double>(width_butlles, height_butlles);
		m_n = new Dimensions<Double>(width_nom_nodes, height_nom_nodes);
		m_e = new Dimensions<Double>(width_escala, height_escala);
		m_l = new Dimensions<Double>(width_lbl_escala, height_lbl_escala);

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

		MargeEPS m;
		Marge m1;
		final Iterator<Marge> itm = ef.ParserMarge(figura[MARGE]).iterator();
		while (itm.hasNext()) {
			m1 = itm.next();
			m = new MargeEPS(m1.getPosReal().getX(), m1.getPosReal().getY(),
					m1.getAlcada(), m1.getAmple(), m1.getPrecisio(),
					m1.getColor());
			m.setEscala(parserDendograma);
			m.setColor(cfg.getConfigMenu().getColorMarge());
			m.setFilled(true);
			m.dibuixa(orientacioClusters);
		}

		LiniaEPS lin = null;
		Linia lin1;
		final Iterator<Linia> it = ef.ParserLinies(figura[LINIA]).iterator();
		while (it.hasNext()) {
			lin1 = it.next();
			lin = new LiniaEPS(lin1.getPosReal().getX(), lin1.getPosReal()
					.getY(), lin1.getAlcada(), lin1.getPrecisio(),
					lin1.getColor());
			lin.setEscala(parserDendograma);
			lin.dibuixa(orientacioClusters);
		}

		final Iterator<Marge> itm2 = ef.ParserMarge(figura[MARGE]).iterator();
		while (itm2.hasNext()) {
			m1 = itm2.next();
			m = new MargeEPS(m1.getPosReal().getX(), m1.getPosReal().getY(),
					m1.getAlcada(), m1.getAmple(), m1.getPrecisio(),
					m1.getColor());
			m.setEscala(parserDendograma);
			m.setColor(cfg.getConfigMenu().getColorMarge());
			m.setFilled(false);
			m.dibuixa(orientacioClusters);
		}

		// show bullets
		if (cfg.getConfigMenu().getRadiBullets() > 0) {
			Cercle cer1;
			CercleEPS cer;
			final Iterator<Cercle> itc = figura[CERCLE].iterator();
			while (itc.hasNext()) {
				cer1 = itc.next();
				cer = new CercleEPS(cer1.getPosReal().getX(), cer1.getPosReal()
						.getY(), cer1.getRadi(), cer1.getPrecisio(),
						cer1.getNom());
				cer.setEscala(parserBulles);
				cer.dibuixa(orientacioClusters);
			}
		}

		// show noms
		if (cfg.getConfigMenu().isNomsVisibles()) {
			NomsDendoEPS nomsD;
			nomsD = new NomsDendoEPS(figura[CERCLE], cfg.getTipusMatriu());
			nomsD.setEscala(parserNoms);
			nomsD.setColor(cfg.getConfigMenu().getColorNoms());
			nomsD.setFont(cfg.getConfigMenu().getFontNoms());
			nomsD.dibuixa(orientacioClusters, orientacioNoms);
		}

		// show escala
		if (cfg.getConfigMenu().isEscalaVisible()) {
			EscalaEPS esc;
			if (orientacioClusters.equals(Orientation.WEST)
					|| orientacioClusters.equals(Orientation.EAST))
				esc = new EscalaEPS(boxEscala.getVal_min_X(),
						boxEscala.getVal_max_X(), cfg.getIncrement(),
						cfg.getTics());
			else
				esc = new EscalaEPS(boxEscala.getVal_min_Y(),
						boxEscala.getVal_max_Y(), cfg.getIncrement(),
						cfg.getTics());

			esc.setEscala(parserEscala);
			esc.setColor(cfg.getConfigMenu().getColorEix());
			esc.dibuixa(orientacioClusters, cfg.getTipusMatriu(), cfg.getTics());
		}

		// show labels escala
		if (cfg.getConfigMenu().isEtiquetaEscalaVisible() && cfg.getTics() > 0) {
			NomsLabelsEscalaEPS nomsEsc;
			if (orientacioClusters.equals(Orientation.WEST)
					|| orientacioClusters.equals(Orientation.EAST)) {
				nomsEsc = new NomsLabelsEscalaEPS(boxEscalalbl.getVal_min_X(),
						boxEscalalbl.getVal_max_X(),
						boxEscalalbl.getVal_max_Y(), cfg.getIncrement(),
						cfg.getTics(), cfg.getAxisDecimals());
			} else {
				nomsEsc = new NomsLabelsEscalaEPS(boxEscalalbl.getVal_min_Y(),
						boxEscalalbl.getVal_max_Y(),
						boxEscalalbl.getVal_max_X(), cfg.getIncrement(),
						cfg.getTics(), cfg.getAxisDecimals());
			}
			nomsEsc.setEscala(parserLbl);
			nomsEsc.setColor(cfg.getConfigMenu().getColorLabels());
			nomsEsc.setFont(cfg.getConfigMenu().getFontLabels());
			nomsEsc.dibuixa(orientacioClusters, cfg.getTipusMatriu());
		}
	}

}
