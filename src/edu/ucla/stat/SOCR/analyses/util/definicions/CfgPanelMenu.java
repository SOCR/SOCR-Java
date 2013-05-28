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

package edu.ucla.stat.SOCR.analyses.util.definicions;

import java.awt.Color;
import java.awt.Font;

import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.tipus.metodo;
import edu.ucla.stat.SOCR.analyses.util.tipus.rotacioNoms;
import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Settings available in the user interface
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class CfgPanelMenu {

	// DATA
	private tipusDades eTipusDades = tipusDades.DISTANCIA;

	// METHOD
	private metodo eMetodo = metodo.UNWEIGHTED_AVERAGE;

	// PRECISION
	private int decimalsSignificatius = 0;

	// TREE
	private Orientation eOrientacioDendograma = Orientation.NORTH;
	private boolean franjaVisible = true;
	private Color colorMarge = Color.LIGHT_GRAY;

	// NODES
	private int radiBullets = 0;
	private boolean nomsVisibles = true;
	private Font fontNoms;
	private Color colorNoms;
	private rotacioNoms eRotNoms = rotacioNoms.VERTICAL;

	// AXIS
	private boolean escalaVisible = true;
	private Color colorEix = Color.BLACK;
	private double valMin = 0.0;
	private double valMax = 1.0;
	private double increment = 0.1;
	private boolean etiquetaEscalaVisible = true;
	private Font fontLabels;
	private Color colorLabels = Color.BLACK;
	private int tics = 10;
	private int axisDecimals = 0;

	public tipusDades getTipusDades() {
		return eTipusDades;
	}

	public void setTipusDades(final tipusDades tipusDades) {
		eTipusDades = tipusDades;
	}

	public metodo getMetodo() {
		return eMetodo;
	}

	public void setMetodo(final metodo metodo) {
		eMetodo = metodo;
	}

	public int getDecimalsSignificatius() {
		return decimalsSignificatius;
	}

	public void setDecimalsSignificatius(final int decimalsSignificatius) {
		this.decimalsSignificatius = decimalsSignificatius;
	}

	public Orientation getOrientacioDendograma() {
		return eOrientacioDendograma;
	}

	public void setOrientacioDendograma(final Orientation orientacio) {
		eOrientacioDendograma = orientacio;
	}

	public boolean isFranjaVisible() {
		return this.franjaVisible;
	}

	public void setFranjaVisible(final boolean franjaVisible) {
		this.franjaVisible = franjaVisible;
	}

	public rotacioNoms getRotNoms() {
		return eRotNoms;
	}

	public void setRotNoms(final rotacioNoms rotNoms) {
		eRotNoms = rotNoms;
	}

	public boolean isNomsVisibles() {
		return nomsVisibles;
	}

	public void setNomsVisibles(final boolean nomsVisibles) {
		this.nomsVisibles = nomsVisibles;
	}

	public int getRadiBullets() {
		return this.radiBullets;
	}

	public void setRadiBullets(int radiBullets) {
		this.radiBullets = radiBullets;
	}

	public Font getFontNoms() {
		return fontNoms;
	}

	public void setFontNoms(final Font fontNoms) {
		this.fontNoms = fontNoms;
	}

	public Color getColorNoms() {
		return colorNoms;
	}

	public void setColorNoms(final Color colorNoms) {
		this.colorNoms = colorNoms;
	}

	public boolean isEscalaVisible() {
		return escalaVisible;
	}

	public void setEscalaVisible(final boolean escalaVisible) {
		this.escalaVisible = escalaVisible;
	}

	public boolean isEtiquetaEscalaVisible() {
		return etiquetaEscalaVisible;
	}

	public void setEtiquetaEscalaVisible(final boolean etiquetaVisible) {
		etiquetaEscalaVisible = etiquetaVisible;
	}

	public Color getColorEix() {
		return colorEix;
	}

	public void setColorEix(final Color colorEix) {
		this.colorEix = colorEix;
	}

	public Color getColorLabels() {
		return colorLabels;
	}

	public void setColorLabels(final Color colorLabels) {
		this.colorLabels = colorLabels;
	}

	public double getValMin() {
		return valMin;
	}

	public void setValMin(final double valMin) {
		this.valMin = valMin;
	}

	public double getValMax() {
		return valMax;
	}

	public void setValMax(final double valMax) {
		this.valMax = valMax;
	}

	public double getIncrement() {
		return increment;
	}

	public void setIncrement(final double increment) {
		this.increment = increment;
	}

	public int getTics() {
		return tics;
	}

	public void setTics(final int tics) {
		this.tics = tics;
	}

	public int getAxisDecimals() {
		return axisDecimals;
	}

	public void setAxisDecimals(final int axisDecimals) {
		this.axisDecimals = axisDecimals;
	}

	public Font getFontLabels() {
		return fontLabels;
	}

	public void setFontLabels(final Font fontLabels) {
		this.fontLabels = fontLabels;
	}

	public Color getColorMarge() {
		return this.colorMarge;
	}

	public void setColorMarge(Color colorMarge) {
		this.colorMarge = colorMarge;
	}

	@Override
	public String toString() {
		String str;
		try {
			str = "/// DATA  ///\n";
			str += "Tipus Dades: " + this.getTipusDades();
			str += "\nMetode: " + this.getMetodo();
			str += "\nPrecisio: " + this.getDecimalsSignificatius();
			str += "\n\n/// TREE  ///\n";
			str += "\nOrientacio Dendrograma: "
					+ this.getOrientacioDendograma();
			str += "\nFranja visible: " + this.isFranjaVisible();
			str += "\n\n/// NAMES ///\n";
			str += "\nNoms Visibles: " + this.isNomsVisibles();
			str += "\nRadi Bullets: " + this.getRadiBullets();
			str += "\nOrientacio Noms: " + this.getRotNoms();
			str += "\nFont Noms: " + this.getFontNoms();
			str += "\nColor Noms: " + this.getColorNoms();
			str += "\n\n/// AXIS  ///\n";
			str += "\nEix Visible: " + this.isEscalaVisible();
			str += "\nColor Eix" + this.getColorEix();
			str += "\nLabels Visible: " + this.isEtiquetaEscalaVisible();
			str += "\nColor Labels" + this.getColorLabels();
			str += "\nValor Minim: " + Double.toString(this.getValMin());
			str += "\nValor Maxim: " + Double.toString(this.getValMax());
			str += "\nIncrement: " + Double.toString(this.getIncrement());
			str += "\nTicks: " + Double.toString(this.getTics());
			str += "\nDecimals Axis: "
					+ Integer.toString(this.getAxisDecimals());
			str += "\nFont Axis: " + this.getFontLabels();
		} catch (Exception e) {
			str = "";
		}
		return str;
	}

}
