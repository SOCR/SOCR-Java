package edu.ucla.stat.SOCR.chart.j3d.gui;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import java.io.InputStream;

import javax.swing.JOptionPane;

import edu.ucla.stat.SOCR.gui.SOCROptionPane;

/**
 * A trivial implementation of Binned2DData for test purposes
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id: SOCRBinnedBinary2DData.java,v 1.4 2010/12/09 19:18:56 jiecui Exp $
 */

public class SOCRBinnedBinary2DData extends SOCRBinned2DData
{
	
	public SOCRBinnedBinary2DData() throws IOException
	{
		loadDataFloat(new URL("http://"), "");   
		xBins=0;
		yBins=0;
	}
	
	public SOCRBinnedBinary2DData(URL base, String fileName) throws IOException
	{
		if(fileName==null || fileName.length()==0)
			super.loadDataFloat(base, fileName);
		else this.loadDataFloat(base, fileName);   
	}
	
	public SOCRBinnedBinary2DData(URL url,  int x, int y) throws IOException
	{
		xBins = x;
		yBins = y;
	
		this.loadDataFloat(url);   
	}
	
	public SOCRBinnedBinary2DData(URL base, String fileName, int x, int y) throws IOException
	{
		xBins = x;
		yBins = y;
	//	System.out.println("SOCRBinnedBinary2DData  fileName=*"+fileName+"*codebase="+base.toString());
		
		if(fileName==null || fileName.length()==0){
			super.loadDataFloat(base, fileName);
			return;
		}
	
		this.loadDataFloat(base, fileName);   
	}
	
	public void loadDataFloat(URL url) throws IOException{
		if(xBins==0 ||yBins ==0)
			loadBinSizes(10,10);
		InputStream is    = (url.openStream());
		BufferedInputStream bis = new BufferedInputStream(is);
		loadDataFloat(getBytesFromFile(bis));	
		
	}
/*	public void loadDataFloat(URL base, String fileName) throws IOException{
			//System.out.println("SOCRBinnedBinary2DData fileName="+fileName);
			
			if(fileName==null || fileName.length()==0){
				super.loadDataFloat(base, fileName);
				return;
			}
	
			//	InputStream is    = (new URL(base, fileName).openStream());
			//BufferedInputStream bis = new BufferedInputStream(is);
			//loadDataFloat(getBytesFromFile(bis));
			
			
			String pathName = base.toString()+fileName;
			if(pathName.indexOf("file:")!=-1){
				pathName = pathName.substring(pathName.indexOf(':')+1);  
				File file = new File(pathName);	
				loadDataFloat(getBytesFromFile(file));
				}
			else if(pathName.indexOf("http:")!=-1){
				InputStream is    = (new URL(base, fileName).openStream());
				BufferedInputStream bis = new BufferedInputStream(is);
				//loadDataFloat(getBytesFromRemoteFile(is));
				try{
					File file = new File((new URL(base,fileName)).toURI());	
					loadDataFloat(getBytesFromRemoteFile2(file));
				}
				catch(Exception e){
				}
				
			}
	}*/
	
	public void loadDataFloat(byte[] bytes) throws IOException{
		
		//System.out.println("bytes xBins="+xBins+ " bytes.size="+bytes.length);
		if(xBins==0||yBins==0)
			loadBinSizes(256, 256);
		data = new float[xBins][yBins];
		try{
	
		for(int i=0; i<xBins; i++)
			for(int j=0; j<xBins; j++)	{		
				data[i][j] = bytes[i*xBins+j];
				//System.out.println("SOCRBinnedBinary2DData data["+i+"]["+j+"]="+data[i][j]);
			}
		
		}
		catch(NumberFormatException e){
			SOCROptionPane.showMessageDialog(null, "Data format error, input data in the format of \" interger(x position) integer(y position) float(value) \" is excepted.");
			return;
		}
		
		findZRange();
	}
	

public byte[] getBytesFromFile(BufferedInputStream  bis) throws IOException {
		
		//	BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
			
			byte[] bytes = null;	
			try {
				        // Get the size of the file
						long length = bis.available();
				        
				        // You cannot create an array using a long type.
				        // It needs to be an int type.
				        // Before converting to an int type, check
				        // to ensure that file is not larger than Integer.MAX_VALUE.
				        if (length > Integer.MAX_VALUE) {
				            // File is too large
				        }
				    
				        // Create the byte array to hold the data
				      //  System.out.println("SOCRBinnedBinary2DData byte array size="+length);
				        bytes = new byte[(int)length];
				    
				        // Read in the bytes
				        int offset = 0;
				        int numRead = 0;
				        while (offset < bytes.length
				               && (numRead=bis.read(bytes, offset, bytes.length-offset)) >= 0) {
				            offset += numRead;
				        }
				       
				    
				        // Ensure all the bytes have been read in
				        if (offset < bytes.length) {
				            throw new IOException("Could not completely read file "+bis);
				        }
				       
				    }
				    finally {
				        // Close the input stream and return bytes
				        bis.close();
				    }
				    return bytes;
		}


	public byte[] getBytesFromFile(File  file) throws IOException {
		
		//	BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
			InputStream is = new FileInputStream(file);
			byte[] bytes = null;	
			try {
				        // Get the size of the file
						long length = file.length();
				        
				        // You cannot create an array using a long type.
				        // It needs to be an int type.
				        // Before converting to an int type, check
				        // to ensure that file is not larger than Integer.MAX_VALUE.
				        if (length > Integer.MAX_VALUE) {
				            // File is too large
				        }
				    
				        // Create the byte array to hold the data
				      //  System.out.println("SOCRBinnedBinary2DData byte array size="+length);
				        bytes = new byte[(int)length];
				    
				        // Read in the bytes
				        int offset = 0;
				        int numRead = 0;
				        while (offset < bytes.length
				               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				            offset += numRead;
				        }
				       
				    
				        // Ensure all the bytes have been read in
				        if (offset < bytes.length) {
				            throw new IOException("Could not completely read file "+is);
				        }
				       
				    }
				    finally {
				        // Close the input stream and return bytes
				        is.close();
				    }
				    return bytes;
		}

	public  byte[] getBytesFromRemoteFile(InputStream is) throws IOException {
		byte[] bytes = null;
		String inputStreamToString = is.toString();
        bytes = inputStreamToString.getBytes();
        return bytes;
	}
	
	public  byte[] getBytesFromRemoteFile2(File file) throws IOException{
	
	      //File length
				int size = (int)file.length(); 
	      if (size > Integer.MAX_VALUE){
	        System.out.println("File is to larger");
	      }
	      byte[] bytes = new byte[size]; 
	      DataInputStream dis = new DataInputStream(new FileInputStream(file)); 
	      int read = 0;
	      int numRead = 0;
	      while (read < bytes.length && (numRead=dis.read(bytes, read,
	                                                bytes.length-read)) >= 0) {
	        read = read + numRead;
	      }
	      System.out.println("File size: " + read);
	      // Ensure all the bytes have been read in
	      if (read < bytes.length) {
	        System.out.println("Could not completely read: "+file.getName());
	      }
		
		
		return bytes;
    }
	
	/*	  try {
	            final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(myByteArray));
	            ImageIO.write(bufferedImage, "jpg", new File("path/to/image.jpg"));
	            
	          
	            ByteArrayInputStream is
	            int size = is.available();
	            char[] theChars = new char[size];
	            byte[] bytes    = new byte[size];
	           
	            
	            is.read(bytes, 0, size);
	            for (int i = 0; i < size;)
	                theChars[i] = (char)(bytes[i++]&0xff);
	            
	            return new String(theChars);
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	
		BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
		loadDataFloat(in);
	}*/
	

}
