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

package edu.ucla.stat.SOCR.analyses.util.importExport;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Stores the largest number of decimal digits in a sequence of numbers
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class ComptaDecimals {

	private long max = 0;

	public int inValue(final Double numd) {
		long prec;
		String tmp1, tmp2;

		prec = Math.round(numd);
		if (prec == numd) {
			prec = 0;
		} else {
			tmp1 = String.valueOf(prec);
			tmp2 = String.valueOf(numd);
			prec = tmp2.length() - tmp1.length() - 1;
		}
		max = Math.max(prec, max);
		return (int) max;
	}

	public void reinicia() {
		max = 0;
	}

	public int getPrecisio() {
		return (int) max;
	}
}
