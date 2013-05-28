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

/**This class gives author, copyright, version, and other metadata information when the user
clicks on the About button.*/
public class AboutDialog extends JDialog implements ActionListener, WindowListener {
	private JButton okButton = new JButton("OK");
	private JTextArea textArea = new JTextArea(10, 50);
	private JPanel buttonBar = new JPanel();

  	/**This method creates a new About Dialog with a specified frame owner and a specified message.*/
  	public AboutDialog(Frame owner, String name) {
		super(owner, "About " + name, true);
		okButton.addActionListener(this);
		addWindowListener(this);
		//Panel
		buttonBar.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonBar.add(okButton);
		//Text area
		textArea.setEditable(false);
		//Dialog
 		getContentPane().add(textArea, BorderLayout.CENTER);
		getContentPane().add(buttonBar, BorderLayout.SOUTH);
		pack();
	}

	/**This method sets the message text.*/
	public void setText(String text){
		textArea.setText(text);
	}

	/**This message handles the event generated when the user clicks on the OK button;
	the dialog is disposed.*/
	public void actionPerformed(ActionEvent event){
		if (event.getSource() == okButton) dispose();
	}

	/**This method handles the event generated when the user clicks the close button;
	the dialog is disposed.*/
	public void windowClosing(WindowEvent event){
		if (event.getWindow() == this) dispose();
	}

	//These methods are not handled.*/
	public void windowOpened(WindowEvent event){}
	public void windowClosed(WindowEvent event){}
	public void windowActivated(WindowEvent event){}
	public void windowDeactivated(WindowEvent event){}
	public void windowConflicted(WindowEvent event){}
	public void windowIconified(WindowEvent event){}
	public void windowDeiconified(WindowEvent event){}
}
