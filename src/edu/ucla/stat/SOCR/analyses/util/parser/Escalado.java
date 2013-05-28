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

package edu.ucla.stat.SOCR.analyses.util.parser;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Transforms world coordinates to screen coordinates
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Escalado {

	private double w = 0.0, h = 0.0;
	private double valor_max_x = 0.0, valor_min_x = 0.0, valor_min_y = 0.0,
			valor_max_y = 0.0;
	private double FactorX = 1.0, FactorY = 1.0;
	private double desplacament_x = 0.0, desplacament_y = 0.0;

	public Escalado() {
	}

	public Escalado(final double max_x, final double max_y, final double min_x,
			final double min_y, final double width, final double heigth) {
		this.setWidth(width);
		this.setHeigth(heigth);
		this.setRangValors(max_x, max_y, min_x, min_y);
	}

	public double getframeWidth() {
		return w;
	}

	public double getframeHeight() {
		return h;
	}

	public double getWidthValues() {
		return valor_max_x - valor_min_x;
	}

	public double getHeightValues() {
		return valor_max_y - valor_min_y;
	}

	public double get_Min_X() {
		return valor_min_x;
	}

	public double get_Min_Y() {
		return valor_min_y;
	}

	public double get_Max_X() {
		return valor_max_x;
	}

	public double get_Max_Y() {
		return valor_max_y;
	}

	private void calculaFactors() {
		FactorX = (1 / (valor_max_x - valor_min_x)) * w;
		FactorY = (1 / (valor_max_y - valor_min_y)) * h;
	}

	public void setWidth(final double width) {
		w = width;
		this.calculaFactors();
	}

	public void setHeigth(final double heigth) {
		h = heigth;
		this.calculaFactors();
	}

	public void setRangValors(final double max_X, final double max_Y,
			final double min_X, final double min_Y) {
		valor_max_x = max_X;
		valor_max_y = max_Y;
		valor_min_x = min_X;
		valor_min_y = min_Y;

		this.calculaFactors();
	}

	public void setDeplaX(final double desplacament) {
		desplacament_x = desplacament;
	}

	public void setDeplaY(final double desplacament) {
		desplacament_y = desplacament;
	}

	public double parserX(final double x) {
		return (desplacament_x + this.parserX_ABS(x));
	}

	public double parserY(final double y) {
		return (desplacament_y + (this.parserY_ABS(y)));
	}

	public double parserX_ABS(final double x) {
		return (FactorX * (x - valor_min_x));
	}

	public double parserY_ABS(final double y) {
		return (FactorY * (y - valor_min_y));
	}
}
