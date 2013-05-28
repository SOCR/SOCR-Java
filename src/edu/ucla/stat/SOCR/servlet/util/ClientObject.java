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
package edu.ucla.stat.SOCR.servlet.util;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.interfaces.*;
import com.sun.crypto.provider.SunJCE;

public class ClientObject implements java.io.Serializable {
	private Key key;
	private String stringAttachment;
	private String userName = "";
	private String password = "";

	public ClientObject(Key key, String stringAttachment, String userNameInput, String passwordInput) {
		this.key = key;
		this.stringAttachment = stringAttachment;
		this.userName = userNameInput;
		this.password = passwordInput;
		//System.out.println("ClientObject this.userName = " + this.userName);
		//System.out.println("ClientObject this.password = " + this.password);


	}

	public Key getKey() {
		return this.key;
	}
	public String getStringAttachment() {
		return this.stringAttachment;
	}

	public String getUserName() {
		return this.userName;
	}
	public String getPassword() {
		return this.password;
	}
}
