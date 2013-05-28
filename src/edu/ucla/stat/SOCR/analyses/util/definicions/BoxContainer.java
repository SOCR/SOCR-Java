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
 * Defines an area on the screen, position, measures and accepted values range
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class BoxContainer {

	private double corner_x = 0.0, corner_y = 0.0;

	private double width = 0.0, height = 0.0;

	private double val_max_X = 0.0, val_max_Y = 0.0;

	private double val_min_X = 0.0, val_min_Y = 0.0;

	public BoxContainer() {
	}

	public BoxContainer(final double corner_x, final double corner_y,
			final double width, final double height, final double val_max_X,
			final double val_max_Y, final double val_min_X,
			final double val_min_Y) {
		this.corner_x = corner_x;
		this.corner_y = corner_y;
		this.width = width;
		this.height = height;
		this.val_max_X = val_max_X;
		this.val_max_Y = val_max_Y;
		this.val_min_X = val_min_X;
		this.val_min_Y = val_min_Y;
	}

	public BoxContainer(final double corner_x, final double corner_y,
			final double width, final double height) {
		this.corner_x = corner_x;
		this.corner_y = corner_y;
		this.width = width;
		this.height = height;
	}

	public double getCorner_x() {
		return corner_x;
	}

	public void setCorner_x(final double corner_x) {
		this.corner_x = corner_x;
	}

	public double getCorner_y() {
		return corner_y;
	}

	public void setCorner_y(final double corner_y) {
		this.corner_y = corner_y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(final double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(final double height) {
		this.height = height;
	}

	public double getVal_max_X() {
		return val_max_X;
	}

	public void setVal_max_X(final double val_max_X) {
		this.val_max_X = val_max_X;
	}

	public double getVal_max_Y() {
		return val_max_Y;
	}

	public void setVal_max_Y(final double val_max_Y) {
		this.val_max_Y = val_max_Y;
	}

	public double getVal_min_X() {
		return val_min_X;
	}

	public void setVal_min_X(final double val_min_X) {
		this.val_min_X = val_min_X;
	}

	public double getVal_min_Y() {
		return val_min_Y;
	}

	public void setVal_min_Y(final double val_min_Y) {
		this.val_min_Y = val_min_Y;
	}
}
