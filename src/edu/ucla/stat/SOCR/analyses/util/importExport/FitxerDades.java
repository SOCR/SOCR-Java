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
 * Stores name and path of a file
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class FitxerDades {

	private String nom = "";
	private String path = "";
	private String nomNoExt = "";

	public FitxerDades(final String nom, final String path) {
		this.nom = nom;
		this.path = path;
		this.nomNoExt = getFileNameWithoutExtension(nom);
	}

	public FitxerDades(final FitxerDades fd) {
		this.nom = fd.nom;
		this.path = fd.path;
		this.nomNoExt = fd.nomNoExt;
	}

	public FitxerDades() {
	};

	private String getFileNameWithoutExtension(String fileName) {
		int whereDot = fileName.lastIndexOf('.');
		if (0 < whereDot && whereDot <= fileName.length() - 2) {
			return fileName.substring(0, whereDot);
		}
		return "";
	}

	public String getNom() {
		return nom;
	}

	public String getNomNoExt() {
		return nomNoExt;
	}

	public void setNom(final String nom) {
		this.nom = nom;
		this.nomNoExt = getFileNameWithoutExtension(nom);
	}

	public String getPath() {
		return path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public String getRuta() {
		return path + nom;
	}

}