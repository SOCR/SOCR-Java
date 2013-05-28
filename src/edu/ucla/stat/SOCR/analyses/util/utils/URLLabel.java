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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.JLabel;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Label with link
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class URLLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	private URI url;

	public URLLabel(String url, String text) {
		super(text);
		setForeground(Color.blue);
		try {
			this.url = new URI(url);
			addMouseListener(new Clicked());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class Clicked extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent me) {
			Desktop desktop = null;
			if (Desktop.isDesktopSupported())
				desktop = Desktop.getDesktop();
			try {
				desktop.browse(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
