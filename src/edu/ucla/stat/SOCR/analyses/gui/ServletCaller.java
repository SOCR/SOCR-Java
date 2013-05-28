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
/* test program. annieche 20051212 */

package edu.ucla.stat.SOCR.analyses.gui;

// and now The inevidable "Hello World" example :) 

// tell the compiler where to find the methods you will use. 
// required when you create an applet 
import java.applet.*; 
// required to paint on screen 
import java.awt.*; 
  

// the start of an applet - HelloWorld will be the executable class 
// Extends applet means that you will build the code on the standard Applet class 
public class ServletCaller extends Applet 
{ 

// The method that will be automatically called  when the applet is started 
     public void init() 
     { 
 // It is required but does not need anything. 
     } 
  

// This method gets called when the applet is terminated 
// That's when the user goes to another page or exits the browser. 
     public void stop() 
     { 
     // no actions needed here now. 
     } 
  

// The standard method that you have to use to paint things on screen 
// This overrides the empty Applet method, you can't called it "display" for example. 

     public void paint(Graphics g) 
     { 
 //method to draw text on screen 
 // String first, then x and y coordinate. 
      g.drawString("Hey hey hey",20,20); 
      g.drawString("Hellooow World",20,40); 

     } 

} 

 
