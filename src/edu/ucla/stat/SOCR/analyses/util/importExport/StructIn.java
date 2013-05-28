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
 * Two elements related by a value
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class StructIn<elem> {

	private elem c1;
	private elem c2;
	private double val;

	public StructIn(final elem c1, final elem c2, final double val) {
		this.c1 = c1;
		this.c2 = c2;
		this.val = val;
	}

	public elem getC1() {
		return c1;
	}

	public void setC1(final elem c1) {
		this.c1 = c1;
	}

	public elem getC2() {
		return c2;
	}

	public void setC2(final elem c2) {
		this.c2 = c2;
	}

	public double getVal() {
		return val;
	}

	public void setVal(final double val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return (c1.toString() + "\t" + c2.toString() + "\t" + val);
	}

}
