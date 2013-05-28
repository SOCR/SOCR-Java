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

import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import sun.misc.*;

public class Encryptor
{

	public static void main(String args[]) throws Exception
	{

		if(args.length<1)
		{
			System.out.println("Usage : EncryptPassword text");
			return;
		}

		Security.addProvider(new com.sun.crypto.provider.SunJCE());

		SecretKeyFactory kf = SecretKeyFactory.getInstance ("DES");
		Cipher cipher = Cipher.getInstance("DES");

		DESKeySpec ks = new DESKeySpec(new byte[] { 0x10, 0x23, 0x54, 0x67, 0x01, 0x23, 0x45, 0x67 });
		SecretKey k = kf.generateSecret(ks);
		cipher.init(Cipher.ENCRYPT_MODE, k);

		String amalgam=args[0];
		for(int i=2;i<args.length;i++)
		amalgam+=" "+args;

		byte[] stringBytes=amalgam.getBytes("UTF8");
		byte[] raw=cipher.doFinal(stringBytes);
		BASE64Encoder encoder = new BASE64Encoder();
		String base64 = encoder.encode(raw);
		System.out.println(base64);
	}
}


