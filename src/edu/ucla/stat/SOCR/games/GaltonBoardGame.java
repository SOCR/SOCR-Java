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

package edu.ucla.stat.SOCR.games;

import java.awt.*;
import java.awt.event.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models a simple Galton board game. The user clicks on buttons to
 * move right or left. The applet shows the path through the Galton board, the
 * corresponding bit string, and the corresponding subset.
 */
public class GaltonBoardGame extends Game {
    private int i, j, bit, index;
    private String bitString, subset;
    private Button leftButton = new Button("0");
    private Button rightButton = new Button("1");
    private GaltonBoard galtonBoard = new GaltonBoard(15);

    /** This method initializes the experiment. */
    public  GaltonBoardGame() {
        setName("Galton Board Game");
        leftButton.addActionListener(this);
        rightButton.addActionListener(this);
        addTool(leftButton);
        addTool(rightButton);
        addGraph(galtonBoard);
        reset();
    }

    /**
     * This method handles the actions associated with the left and right
     * buttons.
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == leftButton) {
            galtonBoard.moveBall(0);
            try {
                play("sounds/0.au");
            } catch (Exception e) {
                ;
            }
            setText();
        } else if (event.getSource() == rightButton) {
            galtonBoard.moveBall(1);
            try {
                play("sounds/0.au");
            } catch (Exception e) {
                ;
            }
            setText();
        } else super.actionPerformed(event);
    }

    /** This method sets the text in the record table. */
    public void setText() {
        i = galtonBoard.getRow();
        j = galtonBoard.getColumn();
        getRecordTable().setText(
                "Bit String\t" + galtonBoard.getBitString() + "\nSubset\t"
                        + galtonBoard.getSubset() + "\nC(" + i + ", " + j + ")\t "
                        + (int) Distribution.comb(i, j));
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        galtonBoard.reset();
        setText();
    }
}

