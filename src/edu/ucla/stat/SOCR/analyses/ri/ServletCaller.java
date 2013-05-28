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
package edu.ucla.stat.SOCR.analyses.ri;
import java.net.URLConnection;
import java.net.URL;
import java.net.HttpURLConnection;

public class ServletCaller {
	public static String getAnalysisServletOutput(String inputXml) {
		java.io.BufferedWriter bWriter = null;
		URLConnection connection = null;

		String resultString = "";

		bWriter = null;
		connection = null;
		String target= ServletConstant.ANALYSIS_SERVLET;
		String message = "\nTHIS MESSAGE IS SENT FROM THE CLIENT APPLET   \n\r";

		try {
			// construct url connection.
			URL url = new URL(target);
			connection = (HttpURLConnection) url.openConnection();
			((HttpURLConnection)connection).setRequestMethod("POST");
			connection.setDoOutput(true);
			// send data to the server
			bWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(connection.
			getOutputStream()));
			bWriter.write(message);
			bWriter.flush();
			bWriter.close();

			// receive data (some analysis output) from the server
			java.io.BufferedReader bReader = null;
			bReader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
			String line;
			StringBuffer sb = new StringBuffer();
			while ( (line = bReader.readLine()) != null) {
				sb.append(line);
			}
			resultString = sb.toString();
			bReader.close();

			((HttpURLConnection)connection).disconnect();
		}
		catch (java.io.IOException ex) {
			resultString += ex.toString();
			//throw ex;
		}
		finally {
			if (bWriter != null) {
				try {
					bWriter.close();
				} catch (Exception ex) {
					resultString += ex.toString();
				}
			}
			if (connection != null) {
				try {
					((HttpURLConnection)connection).disconnect();
				} catch (Exception ex) {
					resultString += ex.toString();
				}
			}
		}
		return resultString;
	}
}
