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

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Calculate smart axis bounds and ticks
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class SmartAxis {

	public enum NiceType {NICE_FLOOR, NICE_CEIL, NICE_ROUND}

	private double min, max;
	double saMin, saMax, saTicksSize;

	public SmartAxis(double min, double max) {
		this.min = min;
		this.max = max;
		calculateSmartAxis();
	}

	public double smartMin() {
		return saMin;
	}

	public double smartMax() {
		return saMax;
	}

	public double smartTicksSize() {
		return saTicksSize;
	}

	public void calculateSmartAxis() {
		saMin = min;
		saMax = max;
		roundAxisLimits();
		roundTicks();
	}

	private void roundAxisLimits() {
		int nrange;

		if (min == max) {
			switch (sign(min)) {
			case 0:
				min = -1.0;
				max = +1.0;
				break;
			case 1:
				min /= 2.0;
				max *= 2.0;
				break;
			case -1:
				min *= 2.0;
				max /= 2.0;
				break;
			}
		}

		saMin = min;
		saMax = max;

		if (sign(saMin) == sign(saMax)) {
			nrange = (int) -Math.rint(Math.log10(Math.abs(2 * (saMax - saMin) / (saMax + saMin))));
			nrange = Math.max(0, nrange);
		} else {
			nrange = 0;
		}
		saMin = niceNum(saMin, nrange, NiceType.NICE_FLOOR);
		saMax = niceNum(saMax, nrange, NiceType.NICE_CEIL);
		if (sign(saMin) == sign(saMax)) {
			if (saMax / saMin > 5.0) {
				saMin = 0.0;
			} else if (saMin / saMax > 5.0) {
				saMax = 0.0;
			}
		}
	}

	private void roundTicks() {
		final int numTicks = 10;

		saTicksSize = niceNum((saMax - saMin) / (numTicks - 1), 0, NiceType.NICE_ROUND);
	}

	private int sign(double x) {
		return (int) Math.round(Math.signum(x));
	}

	public double niceNum(double x, int nrange, NiceType round) {
		long xsign;
		double f, y, fexp, rx, sx;

		if (x == 0.0) {
			return 0.0;
		}

		xsign = sign(x);
		x = Math.abs(x);

		fexp = Math.floor(Math.log10(x)) - nrange;
		sx = x / Math.pow(10.0, fexp) / 10.0;  // scaled x
		rx = Math.floor(sx);                   // rounded x
		f = 10.0 * (sx - rx);                  // fraction between 0 and 10

		if ((round == NiceType.NICE_FLOOR && xsign == +1) || (round == NiceType.NICE_CEIL  && xsign == -1)) {
			y = (int) Math.floor(f);
		} else if ((round == NiceType.NICE_FLOOR && xsign == -1) || (round == NiceType.NICE_CEIL  && xsign == +1)) {
			y = (int) Math.ceil(f);
		} else {                               // round == NiceType.NICE_ROUND
			if (f < 1.5)
				y = 1;
			else if (f < 3.0)
				y = 2;
			else if (f < 7.0)
				y = 5;
			else
				y = 10;
		}

		sx = rx + (double) y / 10.0;

		return xsign * sx * 10.0 * Math.pow(10.0, fexp);
	}

}
