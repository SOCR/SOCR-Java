package edu.ucla.stat.SOCR.chart.j3d.gui;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import edu.ucla.stat.SOCR.gui.SOCROptionPane;

/**
 * A trivial implementation of Binned2DData for test purposes
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id: SOCRBinnedTriplet2DData.java,v 1.4 2010/12/09 19:18:56 jiecui Exp $
 */

public class SOCRBinnedTriplet2DData extends SOCRBinned2DData
{
	
	public SOCRBinnedTriplet2DData() throws IOException
	{
		loadDataFloat(new URL("http://"), "");   
	}
	
	public SOCRBinnedTriplet2DData(URL base, String fileName) throws IOException
	{
		loadDataFloat(base, fileName);   
	}
	public SOCRBinnedTriplet2DData(URL fileUrl) throws IOException
	{
		loadDataFloat(fileUrl);   
	}
	/*
	 * xbin
	 * ybin
	 * 0 0 float[0][0]
	 * 2 3 float[2][3]
	 * 
	 */
	public void loadDataFloat(URL base, String fileName) throws IOException{
		//System.out.println("SOCRBinnedTriplet2DData fileName="+fileName);
		if(fileName==null || fileName.length()==0){
			super.loadDataFloat(base, fileName);
			return;
		}
        /*System.out.println(fileName);
		BufferedReader in = new BufferedReader(new FileReader(fileName));

		loadDataFloat(in);*/
		
		InputStream in = (new URL(base,fileName).openStream());
		BufferedReader rder = new BufferedReader(new InputStreamReader(in));
		loadDataFloat(rder);
	}
	public void loadDataFloat(URL fileUrl) throws IOException{
		
		InputStream in = (fileUrl.openStream());
		BufferedReader rder = new BufferedReader(new InputStreamReader(in));
		loadDataFloat(rder);
	}
	
	public void loadDataFloat(BufferedReader in) throws IOException{
		String line;
		
		loadBinSizes(in);
		data = new float[xBins][yBins];
		
		try{
			while((line= in.readLine())!=null){
				//System.out.println("*"+line+"*");
				StringTokenizer lnTkns = new StringTokenizer(line," ,\t");
				int i = Integer.parseInt(lnTkns.nextToken());
				int j = Integer.parseInt(lnTkns.nextToken());
				if(i<xBins && j <yBins)
					data[i][j] = Float.parseFloat(lnTkns.nextToken());
			}
			in.close();  
		}
		catch(NumberFormatException e){
			SOCROptionPane.showMessageDialog(null, "Data format error, input data in the format of \" interger(x position) integer(y position) float(value) \" is excepted.");
			in.close();
			return;
		}
		
		findZRange();
	}
	
	public void loadDataFloat(FileInputStream fin) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(fin));
		loadDataFloat(in);
	}
	
	 public void getDataFromJTable(JTable dataTable){
		 xBins = dataTable.getRowCount();
		 yBins = xBins;
		 
		// System.out.println("xbins="+xBins+ " ybins="+yBins);
		 data = new float[xBins][yBins];
		 for (int i=0; i<xBins; i++)
			 for (int j=0; j<yBins; j++){
				// System.out.println("getData *"+dataTable.getValueAt(i, j)+"*");
				 data[i][j] = (Float)(dataTable.getValueAt(i, j));
			 }
		 findZRange();		
	 }
	 
}
