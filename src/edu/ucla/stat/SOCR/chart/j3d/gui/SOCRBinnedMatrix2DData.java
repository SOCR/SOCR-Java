package edu.ucla.stat.SOCR.chart.j3d.gui;
import java.io.BufferedReader;
import java.io.FileInputStream;
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
 * @version $Id: SOCRBinnedMatrix2DData.java,v 1.4 2010/12/09 19:18:56 jiecui Exp $
 */

public class SOCRBinnedMatrix2DData extends SOCRBinned2DData
{
	
	public SOCRBinnedMatrix2DData() throws IOException
	{
		loadDataFloat(new URL("http://"),"");   
	}
	
	public SOCRBinnedMatrix2DData(URL base, String fileName) throws IOException
	{
		if(fileName==null || fileName.length()==0)
			super.loadDataFloat(base, fileName);
		else this.loadDataFloat(base, fileName);   
	}
	
	public SOCRBinnedMatrix2DData(URL fileUrl) throws IOException
	{
		loadDataFloat(fileUrl);   
	}
	/*
	 * xbin
	 * ybin
	 * float float float float
	 * float float float float
	 * float float float float
	 */
	public void loadDataFloat(URL base, String fileName) throws IOException{
		//System.out.println("SOCRBinnedTriplet2DData fileName="+fileName);
		if(fileName==null || fileName.length()==0){
			super.loadDataFloat(base, fileName);
			return;
		}

		//BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
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
		//System.out.println("xBins="+xBins+" yBins="+yBins);
		try{
			int i=0;
		
			while((line= in.readLine())!=null && i<xBins){
				//System.out.println("*"+line+"*");
				StringTokenizer lnTkns = new StringTokenizer(line," ,\t");
				int j=0;
				while(lnTkns.hasMoreTokens()) {   
	    			String h = lnTkns.nextToken();
	    			//System.out.println("h="+h);
	    			if(i<xBins && j <yBins){
	    				data[i][j] = Float.parseFloat(h);
	    				//System.out.print(data[i][j]+",");
	    			}
	    			j++;
				}
				i++;
				//System.out.println();
			}
			in.close();  
		}
		catch(NumberFormatException e){
			SOCROptionPane.showMessageDialog(null, "Data format error, input data in the format of \"float(value) float(value) float(value) ...\" for each line is excepted.");
			in.close();
			return;
		}
		
		findZRange();
	}
	
}
