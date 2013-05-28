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

package edu.ucla.stat.SOCR.analyses.util.definicions;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Stores width and height
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Dimensions<tipo> {

	tipo w, h;

	public Dimensions(final tipo w, final tipo h) {
		this.w = w;
		this.h = h;
	}

	public tipo getWidth() {
		return w;
	}

	public void setWidth(final tipo w) {
		this.w = w;
	}

	public tipo getHeight() {
		return h;
	}

	public void setHeight(final tipo h) {
		this.h = h;
	}

}
