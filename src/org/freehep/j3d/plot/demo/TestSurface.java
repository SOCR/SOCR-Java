package org.freehep.j3d.plot.demo;

import org.freehep.j3d.plot.SurfacePlot;
import java.applet.Applet;
import java.awt.BorderLayout;
import com.sun.j3d.utils.applet.MainFrame;
import java.io.IOException;

/**
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id: TestSurface.java,v 1.1 2010/05/10 17:43:44 jiecui Exp $
 */
public class TestSurface extends Applet 
{
	TestSurface() throws IOException
	{
		setLayout(new BorderLayout());
		SurfacePlot surf = new SurfacePlot();
		surf.setData(new TestBinned2DData());
        	add(surf,BorderLayout.CENTER);
	}
	
	public static void main(String[] argv) throws IOException
	{
		new MainFrame(new TestSurface(),300,300);
	}

}
