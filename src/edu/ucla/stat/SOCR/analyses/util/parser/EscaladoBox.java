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

import edu.ucla.stat.SOCR.analyses.util.definicions.BoxContainer;

/**
 * <p>
 * <b>PROJECTE</b>: Multidendrograma
 * </p>
 *
 * <p>
 * <b>ARXIU:</b> EScaladoBox.java
 * </p>
 *
 * <b>DESCRIPCIO:</b> <blockquote> Afegeix un constructor a la classe
 * <code>Escalado</code>, l'unic objectiu es clarificar el codi. </blockquote>
 *
 * @author: Justo Montiel Borrull
 *
 * @version 1.0, 01/05/2008
 * @since JDK 6.0
 */
/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Simplifies scaling of graphical area
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class EscaladoBox extends Escalado {

	public EscaladoBox(BoxContainer box) {
		super(box.getVal_max_X(), box.getVal_max_Y(), box.getVal_min_X(), box
				.getVal_min_Y(), box.getWidth(), box.getHeight());
		setDeplaX(box.getCorner_x());
		setDeplaY(box.getCorner_y());
	}
}