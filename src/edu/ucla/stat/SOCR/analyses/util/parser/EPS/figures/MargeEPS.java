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

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Band EPS figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class MargeEPS extends Figura {
	private double alcada, ample;
	private boolean filled;

	public MargeEPS(final double x, final double y, final double alcada,
			final double ample, final int prec) {
		super(x, y, prec, Color.GRAY);
		this.alcada = alcada;
		this.ample = ample;
		this.filled = true;
	}

	public MargeEPS(final double x, final double y, final double alcada,
			final double ample, final int prec, final Color c) {
		super(x, y, prec, c);
		this.alcada = alcada;
		this.ample = ample;
		this.filled = true;
	}

	public double getAlcada() {
		return alcada;
	}

	public void setAlcada(final double h) {
		alcada = h;
	}

	public double getAmple() {
		return ample;
	}

	public boolean getFilled() {
		return filled;
	}

	public void setFilled(final boolean w) {
		filled = w;
	}

	public void setAmple(final double w) {
		ample = w;
	}

	@Override
	public void dibuixa(final Orientation or) {
		double x1, y1, w, h;
		double xx1, yy1, ww, hh;
		int prec = getPrecisio();

		xx1 = this.getPosReal().getX();
		yy1 = MiMath.Arodoneix(this.getPosReal().getY(), prec);
		ww = this.getPosReal().getX() + this.getAmple();
		hh = MiMath
				.Arodoneix(this.getPosReal().getY() + this.getAlcada(), prec);

		if (or == Orientation.EAST) {
			y1 = yy1;
			yy1 = (this.getEscala().get_Max_Y() - xx1)
					+ this.getEscala().get_Min_Y();
			xx1 = y1;

			h = hh;
			hh = (this.getEscala().get_Max_Y() - ww)
					+ this.getEscala().get_Min_Y();
			ww = h;

			y1 = yy1;
			yy1 = hh;
			hh = y1;
		} else if (or == Orientation.WEST) {
			// rotem P(x,y)
			x1 = xx1;
			xx1 = (this.getEscala().get_Max_X() - yy1)
					+ this.getEscala().get_Min_X();
			yy1 = (this.getEscala().get_Max_Y() - x1)
					+ this.getEscala().get_Min_Y();

			// rotem P(x',y')
			w = ww;
			ww = (this.getEscala().get_Max_X() - hh)
					+ this.getEscala().get_Min_X();
			hh = (this.getEscala().get_Max_Y() - w)
					+ this.getEscala().get_Min_Y();

			// desplacem punt origen eix Y
			y1 = yy1;
			yy1 = hh;
			hh = y1;

			// desplacem punt origen eix X
			x1 = xx1;
			xx1 = ww;
			ww = x1;
		} else if (or == Orientation.SOUTH) {
			// rotem P(x,y)
			yy1 = (this.getEscala().get_Max_Y() - yy1)
					+ this.getEscala().get_Min_Y();

			hh = (this.getEscala().get_Max_Y() - hh)
					+ this.getEscala().get_Min_Y();
			h = yy1;
			yy1 = hh;
			hh = h;
		}

		// escalem
		x1 = this.getEscala().parserX(xx1);
		y1 = this.getEscala().parserY(yy1);
		w = this.getEscala().parserX(ww);
		h = this.getEscala().parserY(hh);

		// dibuixem
		EPSWriter.writeLine("gsave");
		if (filled) {
			EPSWriter.writeLine(EPSWriter.setRGBColor(
					this.getColor().getRed() / 255f,
					this.getColor().getGreen() / 255f, this.getColor()
							.getBlue() / 255f));
			EPSWriter
					.writeLine(EPSWriter.fRectangle(
							(float) (EPSWriter.xmin + x1),
							(float) (EPSWriter.ymax + y1),
							(float) (EPSWriter.xmin + w),
							(float) (EPSWriter.ymax + h)));
		} else {
			Color color = Color.BLACK;
			EPSWriter.writeLine(EPSWriter.setRGBColor(color.getRed() / 255f,
					color.getGreen() / 255f, color.getBlue() / 255f));
			EPSWriter
					.writeLine(EPSWriter.dRectangle(
							(float) (EPSWriter.xmin + x1),
							(float) (EPSWriter.ymax + y1),
							(float) (EPSWriter.xmin + w),
							(float) (EPSWriter.ymax + h)));
		}
		EPSWriter.writeLine("grestore");

	}

	@Override
	public void dibuixa(Graphics2D g, Orientation or) {
	}
}
