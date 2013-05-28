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

package edu.ucla.stat.SOCR.analyses.util.inicial;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Properties;

import edu.ucla.stat.SOCR.analyses.util.errors.ErrorProperties;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Reads configuration file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class AlmacenPropiedades {

	private static HashMap<String, String> propiedades;

	public AlmacenPropiedades() throws Exception {
		FesLog.LOG.info("Creada una nova instancia de l'objecte");

		try {
			final InputStream f = getClass().getResourceAsStream("dendo.ini");

			final Properties propiedadesTemporales = new Properties();
			propiedadesTemporales.load(f);
			f.close();

			AlmacenPropiedades.propiedades = new HashMap(propiedadesTemporales);
		} catch (final FileNotFoundException e) {
			FesLog.LOG.warning("No s'ha trobat l'arxiu d'inici" + e);
			throw new FileNotFoundException("No s'ha trobat l'arxiu d'inici");
		} catch (final Exception e) {
			String msg_err = "ERROR al formar l'arxiu de propietats " + "\n"
					+ e.getStackTrace();
			FesLog.LOG
					.throwing("AlmacenPropiedades", "AlmacenPropiedades()", e);
			throw new Exception(msg_err);
		}
	}

	public static String getPropiedad(final String nombre)
			throws ErrorProperties {
		final String valor = AlmacenPropiedades.propiedades.get(nombre);
		if (valor == null) {
			String msg_err = Language.getLabel(66) + " " + nombre;
			FesLog.LOG.warning(msg_err);
			throw new ErrorProperties(msg_err);
		}
		return valor;
	}
}