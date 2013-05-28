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
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.NumberFormat;
import java.util.Locale;

import edu.ucla.stat.SOCR.analyses.util.parser.Escalado;
import edu.ucla.stat.SOCR.analyses.util.parser.EPS.EPSWriter;
import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;
import edu.ucla.stat.SOCR.analyses.util.utils.MiMath;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Name of distance label EPS figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class NomsLabelsEscalaEPS {
	private final double v_min;
	private double v_max, ample;
	private final double dist, tics;
	private Color color = Color.BLACK;
	private Font font;
	private Escalado parser;
	private final int prec; // precisio

	public NomsLabelsEscalaEPS(final double v_min, final double v_max,
			final double ample, final double dist, final double tics,
			final int prec) {
		this.dist = dist;
		this.tics = tics;
		this.v_min = v_min;
		this.v_max = v_max;
		this.ample = ample;
		this.prec = prec;
	}

	public Escalado getEscala() {
		return parser;
	}

	public void setEscala(final Escalado e) {
		parser = e;
	}

	public double getAlcada() {
		return v_max;
	}

	public void setAlcada(final double h) {
		v_max = h;
	}

	public double getAmple() {
		return ample;
	}

	public void setAmple(final double w) {
		ample = w;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(final Color c) {
		color = c;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(final Font f) {
		font = f;
	}

	public void dibuixa(final Orientation or, final tipusDades tipDades) {
		double y, x, inc, max, min;
		int posX, posY;

		// final Color color_original = g.getColor();
		// final Font font_original = g.getFont();
		final Font ft = this.getFont();
		final Font fr;
		final AffineTransform rot = new AffineTransform();
		final FontRenderContext renderContext = new FontRenderContext(null,
				true, true);
		TextLayout tl;
		String txt;

		// color a aplicar
		EPSWriter.writeLine("gsave");
		EPSWriter.writeLine(EPSWriter.setRGBColor(
				this.getColor().getRed() / 255f,
				this.getColor().getGreen() / 255f,
				this.getColor().getBlue() / 255f));
		if (this.getFont().getPSName().equals("Dialog.plain"))
			EPSWriter.writeLine(EPSWriter.scaleSetFont("/ArialMT", this
					.getFont().getSize()));
		else
			EPSWriter.writeLine(EPSWriter.scaleSetFont("/"
					+ this.getFont().getPSName(), this.getFont().getSize()));

		// cada quant hem de posar un valor
		inc = this.getEscala().parserY_ABS(v_min + dist);
		inc -= this.getEscala().parserY_ABS(v_min);

		if (inc > 0) {
			// Sempre es donara aquest cas ja que hem posat una condicio
			// i no deixem que sigui 0.

			// Formateja la sortida de les dades
			NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
			nf.setMinimumFractionDigits(prec);
			nf.setMaximumFractionDigits(prec);
			nf.setGroupingUsed(false);

			if (or.equals(Orientation.WEST) || or.equals(Orientation.EAST)) {
				rot.rotate(Math.toRadians(-90));
				fr = ft.deriveFont(rot);
				y = this.getEscala().parserY(0);
				min = x = this.getEscala().parserX(v_min);
				max = x = this.getEscala().parserX(v_max);

				double h, num;
				h = tics * dist;

				if (tipDades.equals(tipusDades.DISTANCIA)) {
					h = 0;
					while ((v_min + h) <= v_max) {
						num = v_min + h;
						x = this.getEscala().parserX(num);
						if (or.equals(Orientation.WEST))
							x = min + (max - x);

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, fr, renderContext);

						posX = (int) x + (int) (tl.getBounds().getWidth() / 2);
						posY = (int) y;

						EPSWriter.writeLine(EPSWriter.bottomLeftTextRotated(
								(EPSWriter.xmin + posX),
								(EPSWriter.ymax + posY), 90, txt));

						h += (tics * dist);
					}
				} else {
					h = 0;
					while ((v_max - h) >= v_min) {
						num = v_max - h;
						x = this.getEscala().parserX(num);
						if (or.equals(Orientation.WEST))
							x = min + (max - x);

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, fr, renderContext);

						posX = (int) x + (int) (tl.getBounds().getWidth() / 2);
						posY = (int) y;

						EPSWriter.writeLine(EPSWriter.bottomLeftTextRotated(
								(EPSWriter.xmin + posX),
								(EPSWriter.ymax + posY), 90, txt));

						h += (tics * dist);
					}
				}

			} else if (or.equals(Orientation.NORTH)
					|| or.equals(Orientation.SOUTH)) {
				min = y = this.getEscala().parserY(v_min);
				max = y = this.getEscala().parserY(v_max);
				x = this.getEscala().parserX(0);
				double h, num;
				h = tics * dist;
				h = v_min;

				if (tipDades.equals(tipusDades.DISTANCIA)) {
					// ///////////////////
					double maxx = 0.0;
					while (h <= v_max) {
						num = h;
						y = this.getEscala().parserY(num);
						if (or.equals(Orientation.SOUTH)) {
							y = min + (max - y);
						}

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, ft, renderContext);
						if (Math.abs(tl.getBounds().getMaxX()) > Math.abs(maxx))
							maxx = tl.getBounds().getMaxX();
						h += (tics * dist);
					}
					// ///////////////////

					h = v_min;
					while (h <= v_max) {
						num = h;
						y = this.getEscala().parserY(num);
						if (or.equals(Orientation.SOUTH))
							y = min + (max - y);

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, ft, renderContext);

						posX = (int) (x + (maxx - tl.getBounds().getMaxX()));
						posY = (int) (y - tl.getBounds().getHeight() / 2);

						EPSWriter.writeLine(EPSWriter.bottomLeftText(
								(EPSWriter.xmin + posX),
								(EPSWriter.ymax + posY), txt));

						h += (tics * dist);
					}
				} else {
					// ///////////////////
					h = 0;
					double maxx = 0.0;
					while (MiMath.Arodoneix((v_max - h), 10) >= v_min) {
						num = h;
						y = this.getEscala().parserY(num);
						if (or.equals(Orientation.SOUTH)) {
							y = min + (max - y);
						}

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, ft, renderContext);
						if (Math.abs(tl.getBounds().getMaxX()) > Math.abs(maxx))
							maxx = tl.getBounds().getMaxX();
						h += (tics * dist);
					}
					// ///////////////////

					h = 0;
					while (MiMath.Arodoneix((v_max - h), 10) >= v_min) {
						num = v_max - h;
						y = this.getEscala().parserY(num);
						if (or.equals(Orientation.SOUTH))
							y = min + (max - y);

						txt = String.valueOf(nf.format(num));
						tl = new TextLayout(txt, ft, renderContext);

						posX = (int) (x + (maxx - tl.getBounds().getMaxX()));
						posY = (int) (y - tl.getBounds().getHeight() / 2);

						EPSWriter.writeLine(EPSWriter.bottomLeftText(
								(EPSWriter.xmin + posX),
								(EPSWriter.ymax + posY), txt));

						h += (tics * dist);
					}
				}
			}
		} else
			System.out
					.println("La frequencia no pot ser 0, aixo s'hauria d'haver controlat al capturar les dades de la pantalla");

		EPSWriter.writeLine("grestore");
	}

}
