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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import edu.ucla.stat.SOCR.analyses.util.parser.Escalado;
import edu.ucla.stat.SOCR.analyses.util.tipus.Orientation;
import edu.ucla.stat.SOCR.analyses.util.tipus.rotacioNoms;
import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Name of node figure
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class NomsDendo {
	private Color color = Color.BLACK;
	private Font font;
	private Escalado parser;
	private final tipusDades tipDades;
	LinkedList<Cercle> bullets;

	public NomsDendo(final LinkedList<Cercle> c, final tipusDades tipDades) {
		bullets = c;
		this.tipDades = tipDades;
	}

	public void setEscala(final Escalado e) {
		parser = e;
	}

	public LinkedList<Cercle> getBullets() {
		return this.bullets;
	}

	public Escalado getEscala() {
		return parser;
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

	public void dibuixa(final Graphics2D g, final Orientation orDendo,
			final rotacioNoms orNoms) {
		double x, y;
		int angle_rot = 0;
		String txt;
		final FontRenderContext renderContext = new FontRenderContext(null,
				true, true);
		final AffineTransform rot = new AffineTransform();
		final Font ft = this.getFont();
		Font fr;
		TextLayout tl;

		if (orNoms.equals(rotacioNoms.HORITZ))
			angle_rot = 0;
		else if (orNoms.equals(rotacioNoms.INCLINAT)) {
			if (tipDades.equals(tipusDades.PESO)) {
				if (orDendo.equals(Orientation.NORTH)
						|| orDendo.equals(Orientation.WEST))
					angle_rot = -45;
				else
					angle_rot = 45;
			} else {
				if (orDendo.equals(Orientation.EAST)
						|| orDendo.equals(Orientation.SOUTH))
					angle_rot = -45;
				else
					angle_rot = 45;
			}
		} else if (orNoms.equals(rotacioNoms.VERTICAL))
			angle_rot = -90;
		else
			angle_rot = 0;

		rot.rotate(Math.toRadians(angle_rot));
		fr = ft.deriveFont(rot);

		// posem color a la font
		final Color color_original = g.getColor();
		g.setColor(this.getColor());

		double maxy = 0.0, maxx = 0.0, bigy = 0.0;// , miny = 0.0;
		for (final Cercle c : bullets) {
			x = c.getPosReal().getX();
			y = c.getPosReal().getY();

			txt = String.valueOf(c.getNom());
			tl = new TextLayout(txt, fr, renderContext);

			if (Math.abs(tl.getBounds().getMaxY()) > Math.abs(maxy))
				maxy = tl.getBounds().getMaxY();
			if (Math.abs(tl.getBounds().getMaxX()) > Math.abs(maxx))
				maxx = tl.getBounds().getMaxX();
			if (Math.abs(tl.getBounds().getY()) > Math.abs(bigy))
				bigy = tl.getBounds().getY();
		}

		for (final Cercle c : bullets) {
			x = c.getPosReal().getX();
			y = c.getPosReal().getY();

			txt = String.valueOf(c.getNom());
			tl = new TextLayout(txt, fr, renderContext);

			// rang de valors
			if ((orDendo == Orientation.EAST) || (orDendo == Orientation.WEST)) {
				y = this.getEscala().getHeightValues() - c.getPosReal().getX();
				x = this.getEscala().parserX(0);
				y = this.getEscala().parserY(y);
			} else {
				y = this.getEscala().parserY(0);
				x = this.getEscala().parserX(x);
			}

			// corregim la desviacio del text al rotar
			if (rotacioNoms.HORITZ.equals(orNoms)) /* ROTACIO HORITZ */
			{
				if (Orientation.NORTH.equals(orDendo)
						|| Orientation.SOUTH.equals(orDendo))
					x -= (tl.getBounds().getCenterX());
				else // ORIENTACIO EST I OEST
				{
					if (tipDades.equals(tipusDades.PESO)) {
						if (Orientation.WEST.equals(orDendo)) {
							y -= tl.getBounds().getHeight() / 2;
							x += Math.abs(maxx) - tl.getBounds().getMaxX();
						} else
							y += tl.getBounds().getCenterY();
					}
					if (tipDades.equals(tipusDades.DISTANCIA)) {
						if (Orientation.EAST.equals(orDendo)) {
							y -= tl.getBounds().getHeight() / 2;
							x += Math.abs(maxx) - tl.getBounds().getMaxX();
						} else
							y += tl.getBounds().getCenterY();
					}
				}
			} else if (rotacioNoms.INCLINAT.equals(orNoms)) /* ROTACIO INCLINAT */
			{
				if (tipDades.equals(tipusDades.PESO)) {
					if (Orientation.SOUTH.equals(orDendo))
						y += Math.abs(maxy) - tl.getBounds().getMinY();
					else if (Orientation.WEST.equals(orDendo)) {
						x += Math.abs(maxx) - tl.getBounds().getMaxX();
						y -= tl.getBounds().getHeight();
					}
				} else {
					if (Orientation.NORTH.equals(orDendo))
						y += Math.abs(maxy) - tl.getBounds().getMinY();
					else if (Orientation.EAST.equals(orDendo)) {
						x += Math.abs(maxx) - tl.getBounds().getMaxX();
						y -= tl.getBounds().getHeight();
					}
				}
			} else /* ROTACIO VERTICAL */
			{
				if (tipDades.equals(tipusDades.PESO)) {
					if (Orientation.SOUTH.equals(orDendo)) {
						y += Math.abs(bigy) + tl.getBounds().getY();
						x += tl.getBounds().getWidth() / 2;
					} else if (Orientation.NORTH.equals(orDendo))
						x += tl.getBounds().getWidth() / 2;
					else {
						y -= tl.getBounds().getHeight() / 2;
						x -= tl.getBounds().getMinX();
					}
				} else if (tipDades.equals(tipusDades.DISTANCIA)) {
					if (Orientation.NORTH.equals(orDendo)) {
						y += Math.abs(bigy) + tl.getBounds().getY();
						x += tl.getBounds().getWidth() / 2;
					} else if (Orientation.SOUTH.equals(orDendo))
						x += tl.getBounds().getWidth() / 2;
					else {
						y -= tl.getBounds().getHeight() / 2;
						x -= tl.getBounds().getMinX();
					}
				}

			}
			g.scale(1, -1);
			tl.draw(g, (float) x, (float) -y);
			g.scale(1, -1);
		}

		g.setColor(color_original); // restaurem el color original
	}

	public tipusDades getTipDades() {
		return this.tipDades;
	}
}
