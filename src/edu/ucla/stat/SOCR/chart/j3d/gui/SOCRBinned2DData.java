package edu.ucla.stat.SOCR.chart.j3d.gui;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.freehep.j3d.plot.Binned2DData;
import org.freehep.j3d.plot.Rainbow;

import edu.ucla.stat.SOCR.gui.SOCROptionPane;

/**
 * A trivial implementation of Binned2DData for test purposes
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id: SOCRBinned2DData.java,v 1.4 2010/12/09 21:45:26 jiecui Exp $
 */

public class SOCRBinned2DData implements Binned2DData
{
	protected int xBins;
	protected int yBins;
	protected Rainbow rainbow = new Rainbow();
	protected float[][] data;
	protected float zMin, zMax;
	protected boolean rangeZFound= false;
	
	public SOCRBinned2DData() throws IOException
	{
		loadDataFloat(new URL("http://"), "");   
	}
	
	public SOCRBinned2DData(URL base, String fileName) throws IOException
	{
		loadDataFloat(base, fileName);   
	}
	
	public SOCRBinned2DData(URL fileUrl) throws IOException
	{
		loadDataFloat(fileUrl);   
	}
	/*
	 * xbin
	 * yxin
	 * float[0][0]
	 * float[0][1]
	 * .
	 * .
	 */
	public void loadDataFloat(URL base,String fileName) throws IOException{
		if(fileName==null || fileName.length()==0){
			xBins=10;
			yBins=10;
			data = new float[xBins][yBins];
			for (int i=0; i<xBins; i++)
				for (int j=0; j<yBins; j++)
					data[i][j] = 0;
			data[xBins-1][yBins-1]=1; // otherwise can't be normalized
			findZRange();
			return;
		}
		
		//System.out.println("fileName="+fileName);
	//	BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
	
	//	System.out.println("SOCRBinned2DData fileName=*"+fileName+"* codeBase="+base.toString());
		
		InputStream in = (new URL(base,fileName).openStream());
		BufferedReader rder = new BufferedReader(new InputStreamReader(in));
		loadDataFloat(rder);
	}
	
	public void loadDataFloat(URL fileUrl) throws IOException{
	
		InputStream in = (fileUrl.openStream());
		BufferedReader rder = new BufferedReader(new InputStreamReader(in));
		loadDataFloat(rder);
	}
	
	public void loadDataFloat(FileInputStream fin) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(fin));
		loadDataFloat(in);
	}
	
	protected void loadBinSizes(BufferedReader in) throws IOException{
		in.mark(10);
		try{
			xBins = Integer.parseInt(in.readLine());
			yBins = Integer.parseInt(in.readLine());
		}catch(Exception e){
			loadBinSizes();	
			in.reset();
		}
	}
	
	protected void loadBinSizes(){
		loadBinSizes(10,10);
	}
	
	protected void loadBinSizes(int xDefault, int yDefault){
		String s = JOptionPane.showInputDialog("Enter X size", xDefault);
		xBins = Integer.parseInt(s);
		s = JOptionPane.showInputDialog("Enter Y size", yDefault);
		yBins = Integer.parseInt(s);
		//	System.out.println("xBin="+xBins+" yBin="+yBins);
	}
	
	public void loadDataFloat(BufferedReader in) throws IOException{
		loadBinSizes(in);
		data = new float[xBins][yBins];
		
		try{
			for (int i=0; i<xBins; i++)
				for (int j=0; j<yBins; j++)
					data[i][j] = Float.parseFloat(in.readLine());
			in.close();  
		}catch(NumberFormatException e){
			SOCROptionPane.showMessageDialog(null, "Data format error, input data in the format of \"float \" per line is excepted.");
			in.close();
			return;
		}
		
		findZRange();
	}
	protected void findZRange(){
		zMin = data[0][0];
		zMax = data[0][0];
		for (int i=0; i<xBins; i++)
			for (int j=0; j<yBins; j++){
				if (data[i][j]<zMin)
					zMin = data[i][j];
				if (data[i][j]>zMax)
					zMax = data[i][j];
			}
	//	System.out.println("zMin="+zMin+" zMax="+zMax);
		rangeZFound= true;
		rainbow = new Rainbow(zMin, zMax);
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
		if(!rangeZFound)
			findZRange();
		//System.out.println("zMin="+zMin);
		return zMin;
	}
	
	public float zMax()
	{
		if(!rangeZFound)
			findZRange();
	//	System.out.println("zMax="+zMax);
		return zMax;
	}

	public float zAt(int xIndex, int yIndex)
	{
		return data[xIndex][yIndex];
	}

	public javax.vecmath.Color3b colorAt(int xIndex, int yIndex)
	{
		return rainbow.colorFor(zAt(xIndex,yIndex));
	}
	
	 public void getDataFromJTable(JTable dataTable){
		 xBins= dataTable.getRowCount();
		 yBins = dataTable.getColumnCount();
		 
		// System.out.println("xbins="+xBins+ " ybins="+yBins);
		 data = new float[xBins][yBins];
		 for (int i=0; i<xBins; i++)
			 for (int j=0; j<yBins; j++){
				// System.out.println("SOCRBinned2DData getData *"+dataTable.getValueAt(i, j)+"*");
				 try{
					 data[i][j] = (Float)(dataTable.getValueAt(i, j));
				 }catch(ClassCastException e){
					 try{
					 data[i][j] = Float.parseFloat((String)dataTable.getValueAt(i, j));
					 } catch(Exception ee){
						 SOCROptionPane.showMessageDialog(null, "Data format error, check value at ["+i+","+j+"]");							
					 }
				 }
			 }
		 findZRange();
		
	 }
	 
	 public void setDataAt(float value, int xIndex, int yIndex){
		 data[xIndex][yIndex]= value;
		 if (rangeZFound){
			 if (data[xIndex][yIndex]<zMin)
				 zMin = data[xIndex][yIndex];
			 if (data[xIndex][yIndex]>zMax)
				 zMax = data[xIndex][yIndex];
		 }			 
	 }
}
