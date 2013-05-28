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

package edu.ucla.stat.SOCR.analyses.util.parser.EPS.figures;

import java.awt.Color;
import java.awt.Graphics2D;

import edu.ucla.stat.SOCR.analyses.util.parser.EPS.EPSWriter;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Figura;
import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.utils.MiMath;
import edu.ucla.stat.SOCR.analyses.util.definicions.Coordenada;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Line EPS figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class LiniaEPS extends Figura {
	private double alcada;

	public LiniaEPS(final double x, final double y, final double alcada,
			final int prec) {
		super(x, y, prec);
		this.alcada = alcada;
	}

	public LiniaEPS(final double x, final double y, final double alcada,
			final int prec, final Color c) {
		super(x, y, prec, c);
		this.alcada = alcada;
	}

	public LiniaEPS(final Coordenada<Double> pos, final double alcada,
			final int prec) {
		super(pos.getX(), pos.getY(), prec);
		this.alcada = alcada;
	}

	public LiniaEPS(final Coordenada<Double> pos, final double alcada,
			final int prec, final Color c) {
		super(pos.getX(), pos.getY(), prec, c);
		this.alcada = alcada;
	}

	public double getAlcada() {
		return alcada;
	}

	public void setAlcada(final double h) {
		alcada = h;
	}

	@Override
	public void dibuixa(final Orientation or) {
		double x1, y1, x2, y2;
		double xx1, yy1, xx2, yy2;
		double v_max, v_min;
		int prec = getPrecisio();

		// Ajustem la posicio a la precisio dels calculs
		xx1 = this.getPosReal().getX();
		yy1 = MiMath.Arodoneix(this.getPosReal().getY(), prec);
		xx2 = this.getPosReal().getX();
		yy2 = MiMath.Arodoneix(this.getAlcada(), prec);

		v_max = this.getEscala().get_Max_Y();
		v_min = this.getEscala().get_Min_Y();
		if (or == Orientation.EAST) {
			// invertim
			x1 = yy1;
			yy1 = (v_max - xx1);
			xx1 = x1;
			x2 = yy2;
			yy2 = (v_max - xx2);
			xx2 = x2;
		} else if (or == Orientation.WEST) {
			// invertim
			y1 = (v_max - xx1);
			xx1 = this.getEscala().get_Min_X()
					+ (this.getEscala().get_Max_X() - yy1);
			yy1 = y1;
			y2 = (v_max - xx2);
			xx2 = this.getEscala().get_Min_X()
					+ (this.getEscala().get_Max_X() - yy2);
			yy2 = y2;
		} else if (or == Orientation.SOUTH) {
			// desplacem
			yy1 = v_min + (v_max - yy1);
			yy2 = v_min + (v_max - yy2);
		}

		x1 = this.getEscala().parserX(xx1);
		y1 = this.getEscala().parserY(yy1);
		x2 = this.getEscala().parserX(xx2);
		y2 = this.getEscala().parserY(yy2);

		EPSWriter.writeLine("gsave");
		EPSWriter.writeLine(EPSWriter.setRGBColor(
				this.getColor().getRed() / 255f,
				this.getColor().getGreen() / 255f,
				this.getColor().getBlue() / 255f));
		if (or == Orientation.NORTH) {
			EPSWriter.writeLine(EPSWriter.dLine((float) (EPSWriter.xmin + x1),
					(float) (EPSWriter.ymax + y1 - 0.5),
					(float) (EPSWriter.xmin + x2),
					(float) (EPSWriter.ymax + y2 + 0.5)));
		} else if (or == Orientation.SOUTH) {
			EPSWriter.writeLine(EPSWriter.dLine((float) (EPSWriter.xmin + x1),
					(float) (EPSWriter.ymax + y1 + 0.5),
					(float) (EPSWriter.xmin + x2),
					(float) (EPSWriter.ymax + y2 - 0.5)));
		} else if (or == Orientation.EAST) {
			EPSWriter.writeLine(EPSWriter.dLine(
					(float) (EPSWriter.xmin + x1 - 0.5),
					(float) (EPSWriter.ymax + y1),
					(float) (EPSWriter.xmin + x2 + 0.5),
					(float) (EPSWriter.ymax + y2)));
		} else if (or == Orientation.WEST) {
			EPSWriter.writeLine(EPSWriter.dLine(
					(float) (EPSWriter.xmin + x1 + 0.5),
					(float) (EPSWriter.ymax + y1),
					(float) (EPSWriter.xmin + x2 - 0.5),
					(float) (EPSWriter.ymax + y2)));
		}
		EPSWriter.writeLine("grestore");

	}

	@Override
	public void dibuixa(final Graphics2D g, final Orientation or) {
	}

}
