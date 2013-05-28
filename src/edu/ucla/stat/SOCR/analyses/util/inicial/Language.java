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
import java.util.Vector;

import javax.swing.JOptionPane;

import edu.ucla.stat.SOCR.analyses.util.errors.Warning;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Each label in the program is identified by an index and it is assigned a string
 * that has been previously loaded from a file; the language of the application
 * can be changed just replacing the file to load
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class Language {

	private final String sPath;
	private int index = 0;
	private static Vector<String> lbl_words = null;

	public Language(final String path) throws Exception {
		String errMsg;

		sPath = path;
		try {
			this.carregaWords();
		} catch (Warning w) {
                    w.printStackTrace();
			JOptionPane.showMessageDialog(null, w.getMessage(), "Dendrogram",
					JOptionPane.OK_OPTION);
			FesLog.LOG.warning(w.getMessage());
		} catch (final Exception e) {
                    e.printStackTrace();
			errMsg = Language.getLabel(68);
			FesLog.LOG.throwing("Language", "Language(final String path)", e);
			throw new Exception(errMsg);
		}
	}

	public int numWords() {
		return index;
	}

	public static String getLabel(final int i) {
   
		return Language.lbl_words.get(i);
	}

	private void setLabel(final String lbl) {
		Language.lbl_words.add(index++, lbl);
	}

	private void carregaWords() throws Exception {
		InputStream archivo;
		InputStreamReader fr;
		BufferedReader br;
		String linea, errS;

		Language.lbl_words = new Vector<String>();
	
	        this.getDefault_Words();

	}

	private void getDefault_Words() {
		final String[] str = {
				"Are you sure you want to exit?",
				"Color selection",
				"Invalid value in 'Precision' field",
				"Invalid value in 'Minimum value' field",
				"Invalid value in 'Maximum value' field",
				"Invalid value in 'Ticks separation' field",
				"Invalid value in 'Labels every' field",
				"Error",
				"The file was not loaded",
				"Working data",
				"Ambiguous file. It will be taken as a parity type file",
				"The loaded file is not compatible with this application",
				"Asymmetric data / Two different values for the same data pair",
				"Error at line",
				". The third column must contain a numeric value",
				"Not all the distances have been assigned",
				"Incompatible file",
				"The cluster is not in the group",
				"Unable to recover the cluster",
				"Incompatible max. and min. values",
				"File",
				"Load",
				"View",
				"Settings",
				"Clustering algor.:",
				"DENDROGRAM",
				"Tree orientation:",
				"Distances",
				"Weights",
				"TREE",
				"NODES",
				"Show labels",
				"Nodes",
				"Labels orientat.:",
				"Font",
				"Color",
				"AXIS",
				"Show axis",
				"Color",
				"Show labels",
				"Color",
				"Minimum value:",
				"Maximum value:",
				"Ticks separation:",
				"Labels every",
				"Font",
				"Exit",
				"An unknown event was received",
				"Show bands",
				"Labels decimals:",
				"Invalid value in 'Labels decimals' field",
				"Precision:",
				"",
				"",
				"Bold",
				"Italic",
				"Plain",
				"Selected font",
				"Font selection",
				"Font",
				"OK",
				"Cancel",
				"Root",
				"",
				"Unable to process the dendrogram figure",
				"Warning: File header not found. Data by default will be loaded instead",
				"Property not found",
				"Unable to create the properties file",
				"",
				"Unknown error",
				"Unable to recover the cluster",
				"Unable to save the distance",
				"Unable to recover the matrix",
				"Unable to initialize the matrix",
				"Unable to load the dendrogram properties",
				"Save as",
				"Unable to save the image",
				"Null image",
				"Unable to build the hierarchy tree",
				"",
				"Save as",
				"Unable to create the file",
				"Existing file. Do you want to overwrite it?",
				"Cannot write",
				"Cannot load",
				"Icon not found",
				"Unable to recover the precision value",
				"Save dendrogram as Newick tree",
				"North",
				"South",
				"East",
				"West",
				"Horizontal",
				"Oblique",
				"Vertical",
				"Show dendrogram details",
				"Save dendrogram as JPG",
				"Save dendrogram as PNG",
				"Save dendrogram as TXT",
				"Save dendrogram as EPS",
				"The number of rows is larger than the number of columns",
				"A number not equal to '0' has been found in the diagonal (line",
				"A non-numeric character has been found in a row or column larger than 1",
				"There is no data in the file",
				"The columns count is wrong at line", " of the file",
				"File not found", "Cannot read/write to file",
				"Cannot write to file", "Cannot read from file", "Update", "",
				"No file loaded", "Nodes size:", "Type of measure:", " ticks",
				"Save ultrametric matrix as TXT",
				"Show ultrametric deviation measures", "Version", "Developers",
				"Advisors", "GNU Public License", "Project website",
				"Ultrametric deviation measures", "Tutorial online" };

		Language.lbl_words = new Vector<String>();
		index = 0;
		for (final String s : str) {
			Language.lbl_words.add(index++, s);
		}
	}

}
