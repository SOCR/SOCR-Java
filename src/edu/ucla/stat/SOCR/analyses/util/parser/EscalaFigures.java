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

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;

import edu.ucla.stat.SOCR.analyses.util.parser.figures.Linia;
import edu.ucla.stat.SOCR.analyses.util.parser.figures.Marge;
import edu.ucla.stat.SOCR.analyses.util.tipus.tipusDades;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Ensure only figures inside the graphical area are stored
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class EscalaFigures {

	private final double val_max, val_min;
	private final int prec;
	private final tipusDades tip;

	public EscalaFigures(final double Val_max, final double Val_min,
			final tipusDades tip, final int prec) {
		val_max = Val_max;
		val_min = Val_min;
		this.tip = tip;
		this.prec = prec;
	}

	public LinkedList<Marge> ParserMarge(final LinkedList<Marge> m) {
		double posYinf, posYsup;
		double x, y, alcada, ample;
		Color c;

		final LinkedList<Marge> lst = new LinkedList<Marge>();
		final Iterator<Marge> it = m.iterator();

		while (it.hasNext()) {
			final Marge rect = it.next();

			x = rect.getPosReal().getX();
			y = rect.getPosReal().getY();
			alcada = rect.getAlcada();
			ample = rect.getAmple();
			c = rect.getColor();

			if (tip.equals(tipusDades.DISTANCIA)) {
				posYinf = y;
				posYsup = y + alcada;
			} else {
				posYinf = y - alcada;
				posYsup = y;
			}

			if ((posYinf <= val_max) && (posYsup >= val_min)) {
				if (posYinf < val_min) {
					posYinf = val_min;
				}

				if (posYsup > val_max) {
					posYsup = val_max;
				}
				y = posYinf;
				alcada = (posYsup - posYinf);
				lst.add(new Marge(x, y, alcada, ample, prec, c));

			} else if ((posYinf <= val_min) && (posYsup >= val_max)) {
				y = val_min;
				alcada = (val_max - val_min);
				lst.add(new Marge(x, y, alcada, ample, prec, c));
			}

		}

		return lst;
	}

	public LinkedList<Linia> ParserLinies(final LinkedList<Linia> l) {
		double posYinf, posYsup, tmp;

		final LinkedList<Linia> lst = new LinkedList<Linia>();
		final Iterator<Linia> it = l.iterator();
		while (it.hasNext()) {
			final Linia lin = it.next();
			posYinf = lin.getPosReal().getY();
			posYsup = lin.getAlcada();
			if (posYinf > posYsup) {
				tmp = posYinf;
				posYinf = posYsup;
				posYsup = tmp;
				lin.setAlcada(posYsup);
				lin.getPosReal().setY(posYinf);
			}

			if ((posYinf < val_max) && (posYsup > val_min)) {
				if (posYinf < val_min) {
					lin.getPosReal().setY(val_min);
				}
				if (posYsup > val_max) {
					lin.setAlcada(val_max);
				}
				lst.add(lin);
			} else if ((posYinf <= val_min) && (posYsup >= val_max)) {
				lin.getPosReal().setY(val_min);
				lin.setAlcada(val_max);
				lst.add(lin);
			}
		}

		return lst;
	}
}
