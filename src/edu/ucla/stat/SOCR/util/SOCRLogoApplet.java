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

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**Customizing an applet via HTML parameters.
*
* HTML parameter "animationdelay" is an int indicating
* milliseconds to sleep between images (default is 50).
*
* HTML parameter "imagename" is the base name of the images
* that will be displayed (i.e., "SOCR_IMG" is the base name
* for images "SOCR_IMG_l0.gif," "SOCR_IMG_1.gif," etc.). The applet
* assumes that images are in an "images" subdirectory of
* the directory in which the applet resides.
*
* HTML parameter "totalimages" is an integer representing the
* total number of images in the animation. The applet assumes
* images are numbered from 0 to totalimages - 1 (default 30).
*/
 
public class SOCRLogoApplet extends JApplet {  
    
	Dimension size;

   // obtain parameters from HTML and customize applet
   public void init()
   {
      String parameter;

      // get animation delay from HTML document
      parameter = getParameter( "animationdelay" );

      int animationDelay = ( parameter == null ?
         50 : Integer.parseInt( parameter ) );

      // get base image name from HTML document
      String imageName = getParameter( "imagename" );

      // get total number of images from HTML document
      parameter = getParameter( "totalimages" );

      int totalImages = ( parameter == null ? 
         0 : Integer.parseInt( parameter ) );

	//System.err.println("SOCRLogoApplet::init::totalIamges="+ totalImages);

      // create instance of SOCRLogoAnimator
      SOCRLogoAnimator animator;

      if ( imageName == null || totalImages == 0 )
         animator = new SOCRLogoAnimator();
      else
      {   //System.err.println("SOCRLogoApplet::init::calling - SOCRLogoAnimator2("+
			//totalImages + ", " + animationDelay +", " + imageName +");");
	    	size = this.getSize();
		animator = new SOCRLogoAnimator( totalImages, 
            animationDelay, imageName, size );
	 }

      // attach animator to applet and start animation
      getContentPane().add( animator );
      animator.startAnimation();
   
   }  // end method init
   
}  // end class SOCRLogoApplet
