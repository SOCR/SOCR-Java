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
 It's Online, Therefore, It Exists!
 ***************************************************/

package edu.ucla.stat.SOCR.util;

/**
 * Package: edu.ucla.stat.SOCR.util
 * URL: http://javaxden.blogspot.com/2007/09/launch-web-browser-through-java.html
 *
 * @author Hari
 */
import java.lang.reflect.Method;

public class BrowserControl
{
    /**
     * Method to Open the Broser with Given URL
     *
     * @param url
     */
    public static void openUrl(String url)
    {
        String os = System.getProperty("os.name");
        Runtime runtime = Runtime.getRuntime();
        try
        {
// Block for Windows Platform
            if (os.startsWith("Windows"))
            {
                String cmd = "rundll32 url.dll,FileProtocolHandler " + url;
                Process p = runtime.exec(cmd);
            }
//Block for Mac OS
            else if (os.startsWith("Mac OS"))
            {
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});
            }
//Block for UNIX Platform
            else
            {
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                {
                    if (runtime.exec(new String[]{"which", browsers[count]}).waitFor() == 0)
                    {
                        browser = browsers[count];
                    }
                }
                if (browser == null)
                {
                    throw new Exception("Could not find web browser");
                }
                else
                {
                    runtime.exec(new String[]{browser, url});
                }
            }
        }
        catch (Exception x)
        {
            System.err.println("Exception occurd while invoking Browser!");
            x.printStackTrace();
        }
    }
}
