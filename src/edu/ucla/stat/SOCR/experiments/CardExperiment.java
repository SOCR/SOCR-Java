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

package edu.ucla.stat.SOCR.experiments;

import java.awt.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * The card experiment consists of drawing a sample of cards, without
 * replacement, form a standard deck
 */
public class CardExperiment extends Experiment {
    public URL url;
    private int cards = 10, n = 5;
    //Objects
    private MediaTracker tracker;
    private Image[] cardImage = new Image[53];
    private JPanel toolbar = new JPanel();
    private Deck deck;
    private JLabel definitionLabel = new JLabel(
            "Yi: Denomination of Card i;  Zi: Suit of Card i");
    private String suit[] = { "clubs", "diamonds", "hearts", "spades" };

    /** Initialize the experiment: add scrollbar, cards, record table */
    public CardExperiment() {
        setName("CardExperiment");
    }
    
    public void initialize() {
        createValueSetter("n", Distribution.DISCRETE, 1, cards, n);

        tracker = new MediaTracker(applet);
        //cardLabel.setText("test1");
        try {

            int i = 0;
            for (int d = 0; d <= 12; d++) // denomination
            {
                for (int s = 0; s < 4; s++) // suit 0=clubs, 1=diamonds,
                // 2=hearts, 3=spades
                { //showStatus("Image " + String.valueOf(i));
                    cardImage[i] = applet.getImage(new URL(applet.getCodeBase()
                            .toString()
                            + "images/cards/"
                            + suit[s]
                            + String.valueOf(d + 1)
                            + ".jpg"));

                    //System.out.println((cardImage[i]).toString());

                    //System.out.println("CardExperiment::IMAGE = "
                    // +parentCodeBase.toString()+
                    //		"images/cards/"+suit[s]+ String.valueOf(d+1) + ".jpg");

                    tracker.addImage(cardImage[i], i);
                    //System.err.println("InsideCardExperiment::Prior to call
                    // to Card.setImage ...");
                    Card.setImage(cardImage[i], i);
                    //System.err.println("InsideCardExperiment::After a call to
                    // Card.setImage!");
                    i++;
                }
            }

            /** *** Get the last 53-rd card: Joker * */
            //Change path relative to codebase.
            cardImage[i] = Toolkit.getDefaultToolkit().getImage(
                    new URL(applet.getCodeBase().toString() + "images/cards/"
                            + "joker.jpg"));

            tracker.addImage(cardImage[i], i);
            Card.setImage(cardImage[i], i);

            try {
                tracker.waitForAll();
                deck = new Deck(cards);
            }

            catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Warning in CardExperiment.java");
            }
        } catch (Exception e) { //System.out.println("break:");
            e.printStackTrace();
        }

        //Toolbars
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(definitionLabel);
        addToolbar(toolbar);
        //Cards
        addGraph(deck);
        //reset();
        deck.validate();
        deck.repaint();
    }

    /**
     * This method defines the experimnet: a sample of cards is chosen from the
     * deck
     */
    public void doExperiment() {
        super.doExperiment();
        deck.deal(n);
    }

    /** Reset the experiment. Show the card backs and reset the record table */
    public void reset() {
        super.reset();
        JTextArea recordTable = getRecordTable();
        for (int i = 0; i < n; i++)
            recordTable.append("\tY" + (i + 1) + "\tZ" + (i + 1));
        deck.showCards(n, false);
    }

    /**
     * Update the experiment: show the cards in the sample and update the record
     * text
     */
    public void update() {
        super.update();
        String record = "";
        for (int i = 0; i < n; i++)
            record = record + "\t" + deck.getCard(i).getValue() + "\t"
                    + deck.getCard(i).getSuit();
        getRecordTable().append(record);
        deck.showCards(n, true);
    }

    /** Handle scrollbar event: select the sample size */
    public void update(Observable o, Object arg) {
        n = getValueSetter(0).getValueAsInt();
        reset();
    }
}

