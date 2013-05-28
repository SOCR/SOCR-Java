package org.freehep.j3d.plot.demo;
import org.freehep.j3d.plot.*;
import java.io.*;

import javax.swing.JTable;

/**
 * A trivial implementation of Binned2DData for test purposes
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id: TestBinned2DData.java,v 1.1 2010/05/10 17:43:44 jiecui Exp $
 */

public class TestBinned2DData implements Binned2DData
{
	private int xBins;
	private int yBins;
	private Rainbow rainbow = new Rainbow();
	private float[][] data;
	
	public TestBinned2DData() throws IOException
	{
	//	System.out.println("TestBinned2DData(), loading test.data");
	//	Exception e = new Exception();
	//	e.printStackTrace();
		loadData("test.data");   
	}
	
	public TestBinned2DData(String fileName) throws IOException
	{
		loadData(fileName);   
	}
	
	public void loadData(String fileName) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
	      xBins = Integer.parseInt(in.readLine());
	      yBins = Integer.parseInt(in.readLine());
	      data = new float[xBins][yBins];
	      for (int i=0; i<xBins; i++)
	         for (int j=0; j<yBins; j++)
	            data[i][j] = Float.parseFloat(in.readLine());
	      in.close();  
	}

	public int xBins()
	{
		return xBins;
	}

	public int yBins()
	{
		return yBins;
	}

	public float xMin()
	{
		return 0f;
	}

	public float xMax()
	{
		return 1f;
	}

	public float yMin()
	{
		return 0f;
	}

	public float yMax()
	{
		return 1f;
	}

	public float zMin()
	{
		return 0f;
	}
	public float zMax()
	{
		return 1f;
	}

	public float zAt(int xIndex, int yIndex)
	{
		return data[xIndex][yIndex];
	}

	public javax.vecmath.Color3b colorAt(int xIndex, int yIndex)
	{
		return rainbow.colorFor(zAt(xIndex,yIndex));
	}
	 public void setData(JTable dataTable){
		 xBins= dataTable.getRowCount();
		 yBins = dataTable.getColumnCount();
		 data = new float[xBins][yBins];
	      for (int i=0; i<xBins; i++)
	         for (int j=0; j<yBins; j++){
	        //	 System.out.println(dataTable.getValueAt(i, j));
	            data[i][j] = Float.parseFloat((String)dataTable.getValueAt(i, j));
	         }
		 
	 }
}
