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
import java.awt.event.*;
import javax.swing.*;
import java.net.URL;

public class SOCRLogoAnimator extends JPanel implements ActionListener {

   protected ImageIcon images[];       // array of images

   Dimension size;
   int maxCount = 10, count, countDelta = -1;
   float alphaStep;

   // Makes the code require too much CPU time - avoid the rescaling/resampling ....
   AlphaComposite composite;

   protected int totalImages = 30,     // number of images
                 currentImage = 0,     // current image index
                 animationDelay = 50,  // millisecond delay
                 width,                // image width
                 height;               // image height

   protected String imageName = "SOCR_IMG_";  // base image name
   protected Timer animationTimer;  // Timer drives animation

   // initialize SOCRLogoAnimator by loading images
   public SOCRLogoAnimator()
   {	System.err.println("SOCRLogoAnimator::constructorDefault::totalIamges="+
		totalImages);

      initializeAnimation();
   }

  public SOCRLogoAnimator( int count, int delay, String name, Dimension _size )
   {	
      size = _size;
	this.setSize(size);
	totalImages = count;
      animationDelay = delay;
      imageName = name;
      initializeAnimation();
   }

   // initialize animation
   protected void initializeAnimation()
   {	Image image;
	size = this.getSize();
      images = new ImageIcon[ totalImages ];
	URL fileName;
	composite = AlphaComposite.SrcOver;

	//System.err.println("SOCRLogoAnimator::initializeAnimation::totalIamges="+
		//totalImages);

      // load images
      for ( int count = 0; count < images.length; ++count )
      {  try{
		fileName = new URL("http://socr.stat.ucla.edu/jars/SOCR/images/"+
				imageName + count + ".gif");
		images[ count ] = new ImageIcon(fileName);

		image = images[ count ].getImage();
		image = image.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
		images[ count ] = new ImageIcon(image);

	    }
	    catch (Exception e) { System.err.println("!!!!!!!!!Exception = "+ e);
		}
	 }
 
      width = images[ 0 ].getIconWidth();   // get icon width
      height = images[ 0 ].getIconHeight(); // get icon height

	maxCount = 10;
	count = maxCount;                       // Set repaint counter
    	alphaStep = 1.0f/count;
   }

   // display current image 
   public void paintComponent( Graphics g )
   {
      super.paintComponent( g );

	Graphics2D g2D = (Graphics2D)g;
	g2D.setBackground(Color.white);
      g2D.setComposite(composite);             // Set current alpha

      images[ currentImage ].paintIcon( this, g2D, 0, 0 );
      currentImage = ( currentImage + 1 ) % totalImages;
   }

   // respond to Timer's event
   public void actionPerformed( ActionEvent actionEvent )
   {
      if ( images[ currentImage ].getImageLoadStatus() ==
           MediaTracker.COMPLETE ) {

         repaint();  // repaint animator

			//Update alpha composite for next frame
                       if(count ==maxCount)
                         countDelta = -1;
                       else if(count == 0)
                         countDelta = 1;
                       count += countDelta;
                       composite = AlphaComposite.getInstance(
                              AlphaComposite.SRC_OVER,count*alphaStep);
      }
   }

   // start or restart animation
   public void startAnimation()
   {
      if ( animationTimer == null ) {
         currentImage = 0;  
         animationTimer = new Timer( animationDelay, this );
         animationTimer.start();
      }
      else  // continue from last image displayed
         if ( ! animationTimer.isRunning() )
            animationTimer.restart();
   }

   // stop animation timer
   public void stopAnimation()
   {
      animationTimer.stop();
   }

   // return minimum size of animation
   public Dimension getMinimumSize()
   { 
      return getPreferredSize(); 
   }

   // return preferred size of animation
   public Dimension getPreferredSize()
   {
      return new Dimension( width, height );
   }

   // execute animation in a JFrame
   public static void main( String args[] )
   {
      // create SOCRLogoAnimator
      SOCRLogoAnimator animation = new SOCRLogoAnimator();

      // set up window
      JFrame window = new JFrame( "SOCR Logo Animator" );

      Container container = window.getContentPane();
      container.add( animation );
         
      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

      // size and display window
      window.pack();
      Insets insets = window.getInsets();

      window.setSize( animation.getPreferredSize().width + 
         insets.left + insets.right,
         animation.getPreferredSize().height +
         insets.top + insets.bottom );

      window.setVisible( true );
      animation.startAnimation();  // begin animation

   }  // end method main

}  // end class SOCRLogoAnimator
