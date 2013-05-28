/*
 * DemoGUI.java
 *
 * Created on June 15, 2001, 1:30 PM
 */

package edu.ucla.stat.SOCR.chart.j3d.gui;
import org.freehep.j3d.plot.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 *
 * @author  joyk
 * @version $Id: ChartPanel3D.java,v 1.2 2010/05/17 18:05:15 jiecui Exp $
 */
public class ChartPanel3D extends JPanel
{
	private static JPanel panel;
    private static LegoPlot lego;
    private static SurfacePlot surf = null;
    protected static URL codeBase;
    /** Creates new DemoGUI */
    public ChartPanel3D(URL base) throws java.io.IOException
    {
        super(new BorderLayout());
        codeBase = base;
        
        lego = new LegoPlot();
        lego.setDrawBlocks(true);
        lego.setLinesWhileAnim(true);
        lego.setSparsifyThreshold(300);
        
        Runtime r = Runtime.getRuntime();
		r.gc();
		
        lego.setData(new SOCRBinned2DData(codeBase, ""));
        LegoControlPanel controls = new LegoControlPanel(lego);

        panel = new JPanel(new BorderLayout());
        panel.add(lego,BorderLayout.CENTER);
        panel.add(controls,BorderLayout.SOUTH);
        add(panel,BorderLayout.CENTER);
    }
    
    public void setData(SOCRBinned2DData data){
    	lego.setData(data);
    	if (surf == null) {
             surf = new SurfacePlot();
             
             Runtime r = Runtime.getRuntime();
             r.gc();
     		
             surf.setData(data);
         }
    	else surf.setData(data);
    	
    	 surf.validate();
    	 lego.validate();
    }
   
    public static SurfacePlot getSurfacePlot() throws java.io.IOException
    {
        if (surf == null) {
            surf = new SurfacePlot();
            surf.setData(new SOCRBinned2DData(codeBase, ""));
        }
        panel.remove(lego);
        lego.removeNotify();
        panel.add(surf,BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();

        return surf;
    }
    public static LegoPlot getLegoPlot() {
        panel.remove(surf);
        surf.removeNotify();
        panel.add(lego,BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
        return lego;
    }
}
