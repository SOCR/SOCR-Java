/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/
package edu.ucla.stat.SOCR.util;

import java.util.zip.*;
import java.io.*;

public class GZIP {
	static String testString = "snoopy is a dog.";

	public static ByteArrayOutputStream compressString(String input) {
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			GZIPOutputStream out = new GZIPOutputStream(byteOutputStream);
			ByteArrayInputStream  in = new ByteArrayInputStream(input.getBytes());		   byte[] buf = new byte[1024];
			int len;

			while ((len = in.read(buf, 0, 1024)) > 0) {
			  out.write(buf, 0, len);
			}
			in.close();
			out.finish();
			out.close();
			//System.out.println("GZIP out String = " + byteOutputStream.toString());
	    } catch (IOException e) {
		    byteOutputStream =  null;
	    }
	    return byteOutputStream;

	}
	public static String decompressString(ByteArrayOutputStream input) {
		byte[] tempBuffer = input.toByteArray() ;
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(tempBuffer);
		OutputStream out = null;
		try {
			GZIPInputStream in = new GZIPInputStream(byteInputStream);
			out = new ByteArrayOutputStream();

			// Transfer bytes from the compressed file to the output file
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();
		} catch (IOException e) {
			return null;
		}
		return out.toString();
 	}


	public static boolean compressFile(String inputFileName, String outputFileName) {
		try {
			GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(outputFileName));
			FileInputStream in = new FileInputStream(inputFileName);

			byte[] buf = new byte[1024];

			int len;

			while ((len = in.read(buf, 0, 1024)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();

			// Complete the GZIP file
			out.finish();
			out.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}


	public static boolean decompressFile(String inputFileName, String outputFileName) {
		try {
			GZIPInputStream in = new GZIPInputStream(new FileInputStream(inputFileName));
			OutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
	    } catch (IOException e) {
		    return false;
	    }
	    return true;
	}

	public static Object compressObject(Object input) {
		return input;
	}
	public static Object decompressObject(Object input) {
		return input;
	}

	public static void main(String args[]) {/*
	    try {
		   // Create the GZIP output stream
		   String outFilename = "outfile.gzip";
		   GZIPOutputStream out = new GZIPOutputStream(new ByteArrayOutputStream());

		   // Open the input file
		   //String inFilename = "analysis_output.xml";
		   //BufferedInputStream in = new BufferedInputStream(new StringWriter());

			//String myString = "myString";
			byte currentXMLBytes[] = testString.getBytes();
			ByteArrayInputStream in = new ByteArrayInputStream(currentXMLBytes);

		   // Transfer bytes from the input file to the GZIP output stream
		   byte[] buf = new byte[1024];
		   int len;
		   while ((len = in.read(buf)) > 0) {
			  out.write(buf, 0, len);

		   }
	    	//System.out.println(out.toString());
		   in.close();

		   // Complete the GZIP file
		   out.finish();
		   out.close();
	    } catch (IOException e) {
	    }
	   try {
		   // Open the compressed file
		   String inFilename = "outfile.gzip";
		   GZIPInputStream in = new GZIPInputStream(new FileInputStream(inFilename));

		   // Open the output file
		   String outFilename = "analysis_output.xml.unzipped";
		   OutputStream out = new FileOutputStream(outFilename);

		   // Transfer bytes from the compressed file to the output file
		   byte[] buf = new byte[1024];
		   int len;
		   while ((len = in.read(buf)) > 0) {
			  out.write(buf, 0, len);
		   }

		   // Close the file and stream
		   in.close();
		   out.close();
	    } catch (IOException e) {
	    }

		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		String testString = "snoopy is a dog.";
	    try {
		   // Create the GZIP output stream
		   //String outFilename = "outfile.gzip";
		   //GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(outFilename));
			//ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		   GZIPOutputStream out = new GZIPOutputStream(byteOutputStream);
		   // Open the input file
		   String inFilename = "hello.txt";
		   //FileInputStream in = new FileInputStream(inFilename);
			ByteArrayInputStream  in = new ByteArrayInputStream(testString.getBytes());


		   // Transfer bytes from the input file to the GZIP output stream
		   //char[] charbuf = new char[1024];
		   byte[] buf = new byte[1024];

		   int len;

		   while ((len = in.read(buf, 0, 1024)) > 0) {

			  out.write(buf, 0, len);
		   }
		   in.close();

		   // Complete the GZIP file
		   out.finish();
		   out.close();
      		System.out.println("GZIP out String = " + byteOutputStream.toString());
	    } catch (IOException e) {
	    }


		byte[] tempBuffer = byteOutputStream.toByteArray() ;
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(tempBuffer);


	   try {
		   // Open the compressed file
		   //String inFilename = "outfile.gzip";
		  // GZIPInputStream in = new GZIPInputStream(new FileInputStream(inFilename));
		  GZIPInputStream in = new GZIPInputStream(byteInputStream);

		   // Open the output file
		   String outFilename = "output.txt";
			//ByteArrayOutputStream finalByteOutputStream = new ByteArrayOutputStream();
		   OutputStream out = new ByteArrayOutputStream();

		   // Transfer bytes from the compressed file to the output file
		   byte[] buf = new byte[1024];
		   int len;
		  System.out.println("-----------------");
		   while ((len = in.read(buf)) > 0) {
			  out.write(buf, 0, len);
		   }

		   // Close the file and stream
		   in.close();
		   System.out.println("Final result = " + out.toString());
		   out.close();
		   //System.out.println("Final result = " + finalByteOutputStream.toString());
	    } catch (IOException e) {
	    }
 	}*/
		String test = "this is a dog and this is a cat and that is a donkey.";
		ByteArrayOutputStream testOutput = compressString(test);
		System.out.println(testOutput.toString());

		System.out.println("----------------------");
		String testResult = decompressString(testOutput);
		System.out.println(testResult);
		System.out.println(compressFile("analysis_output.xml", "outfile.gzip"));



	}



}
