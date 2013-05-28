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

package edu.ucla.stat.SOCR.analyses.util.utils;

import edu.ucla.stat.SOCR.analyses.util.inicial.FesLog;

import java.math.BigDecimal;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Rounding and format of numeric values
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class MiMath {

	public static double Arodoneix(final double num, final int prec) {
		double numArrodonit = 0.0;
		double factor;

		final int base = 10;
		FesLog.LOG.info("num=" + num + " prec= " + prec);

		BigDecimal b = new BigDecimal(Double.toString(num));
		factor = Math.pow(base, prec);
		b = b.multiply(new BigDecimal(factor));

		numArrodonit = Math.round(b.doubleValue()) / factor;

		FesLog.LOG.finest("NumArrodonit: " + numArrodonit);
		return numArrodonit;
	}

	public static double redondea(double numero, int decimales) {
		double resultado;
		BigDecimal res;

		res = new BigDecimal(numero).setScale(decimales, BigDecimal.ROUND_UP);
		resultado = res.doubleValue();
		return resultado;
	}

}
