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

package edu.ucla.stat.SOCR.analyses.util.moduls.frm;

import javax.swing.JInternalFrame;

/**
 * <p>
 * <b>MultiDendrograms</b>
 * </p>
 *
 * Internal frames
 *
 * @author Justo Montiel, David Torres, Sergio G&oacute;mez, Alberto Fern&aacute;ndez
 *
 * @since JDK 6.0
 */
public class FrmInternalFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private static int openFrameCount = 0;
	private static final int xOffset = 20, yOffset = 30;

	private InternalFrameData ifd;

	public FrmInternalFrame(String title, boolean isUpdate, int oldx, int oldy) {

		super(title, true, // resizable
				true, // closable
				true, // maximizable
				true);// iconifiable

		String tit;

		tit = title;
		if (title.equals("SINGLE_LINKAGE")) {
			tit = "Single Linkage";
		} else if (title.equals("COMPLETE_LINKAGE")) {
			tit = "Complete Linkage";
		} else if (title.equals("UNWEIGHTED_AVERAGE")) {
			tit = "Unweighted Average";
		} else if (title.equals("WEIGHTED_AVERAGE")) {
			tit = "Weighted Average";
		} else if (title.equals("UNWEIGHTED_CENTROID")) {
			tit = "Unweighted Centroid";
		} else if (title.equals("WEIGHTED_CENTROID")) {
			tit = "Weighted Centroid";
		} else if (title.equals("JOINT_BETWEEN_WITHIN")) {
			tit = "Joint Between-Within";
		}
		setTitle(tit);

		if (isUpdate) {
			setLocation(oldx, oldy);
		} else {
			setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
		}
		openFrameCount++;
	}

	public static void decreaseOpenFrameCount() {
		openFrameCount--;
	}

	public void setInternalFrameData(final InternalFrameData ifd) {
		this.ifd = ifd;
	}

	public InternalFrameData getInternalFrameData() {
		return ifd;
	}

}
