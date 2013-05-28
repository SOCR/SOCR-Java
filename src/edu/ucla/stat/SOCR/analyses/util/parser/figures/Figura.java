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

package edu.ucla.stat.SOCR.analyses.util.parser.figures;

import java.awt.Color;
import java.awt.Graphics2D;

import edu.ucla.stat.SOCR.analyses.util.parser.Escalado;
import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.definicions.Coordenada;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Abstract class for all figures to be drawn in the graphical area
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public abstract class Figura {

	private Color color = Color.BLACK;
	private Coordenada<Double> org;
	private Escalado parser;
	private final int prec;

	public Figura(final double x, final double y, final int p) {
		org = new Coordenada<Double>(x, y);
		prec = p;
	}

	public Figura(final double x, final double y, final int p, final Color c) {
		org = new Coordenada<Double>(x, y);
		color = c;
		prec = p;
	}

	public Coordenada<Double> getPosReal() {
		return org;
	}

	public void setPosReal(final Coordenada<Double> pos) {
		org = pos;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(final Color c) {
		color = c;
	}

	public void setEscala(final Escalado e) {
		parser = e;
	}

	public Escalado getEscala() {
		return parser;
	}

	public int getPrecisio() {
		return prec;
	}

	public abstract void dibuixa(Orientation or);

	public abstract void dibuixa(Graphics2D g, Orientation or);
}
