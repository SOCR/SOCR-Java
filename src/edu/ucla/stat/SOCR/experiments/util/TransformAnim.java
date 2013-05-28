package edu.ucla.stat.SOCR.experiments.util;

/*
 * @(#)TransformAnim.java	1.6  98/12/03
 *
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */


import java.util.Observable;
import java.util.Observer;

import javax.swing.JApplet;


/**
 * The TransformAnim class performs animation of shapes, text and images 
 * rotating, scaling, shearing and translating around a surface.
 */
public class TransformAnim extends JApplet{

    Demo demo;
   
    public boolean g_doRotate = false;
    public boolean g_doTranslate = true;
    public boolean g_doScale = false;
    public boolean g_doShear;
    
    public void init() {
        getContentPane().add(demo = new Demo(this));
        getContentPane().add("North", new DemoControls(this, demo));
        
    }

    public void init(int rowCount, String expChoice) {
    	
    	demo.setExpChoice(expChoice);
    	getContentPane().removeAll();
        getContentPane().add(demo);
        getContentPane().add("North", new DemoControls(this, demo, rowCount));
        
    }

    public void print(){
    	System.out.println(demo.vector.size()+ "****");
    	for (int i=0; i<demo.vector.size(); i++){
    		DemoObjectData ob = (DemoObjectData)demo.vector.get(i);
    		System.out.println("ob "+ob.name + " moving="+ob.movingOb+ " fromx="+ob.fromx+ " fromy"+ob.fromy +" tox="+ob.tox+" toy="+ob.toy);
    	}
    	System.out.println("****------");
    }
    public void start() {
        demo.start();
    }
  
    public void stop() {
        demo.stop();
    }

  /*public void setImages(int num){
	  demo.setImages(num);
  }*/
  public void setFromObs(double[] cards, int num){
	//  System.out.println("demo setFromCards");
	  demo.setFromObs(cards, num);
  }
  public void setToObs(double[] cards, int[] fromIndex, int num){
	 // System.out.println("demo setToCards");
	  demo.setToObs(cards, fromIndex, num);
  }
  
  protected Observable observable = new Observable() {
      public void notifyObservers() {
    	 // System.out.println("Anim notify Observer");
          setChanged();
          super.notifyObservers(TransformAnim.this);
      }
  };
  
  public void addObserver(Observer o) {	
	  if (observable.countObservers()<1){
		//  System.out.println("Anim adding Observer");
	      observable.addObserver(o);
	  }
  }
  
   



 /*   public static void main(String argv[]) {
        final TransformAnim demo = new TransformAnim();
        demo.init();
        JFrame f = new JFrame("Java 2D(TM) Demo - TransformAnim");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
            public void windowDeiconified(WindowEvent e) { demo.start(); }
            public void windowIconified(WindowEvent e) { demo.stop(); }
        });
        f.getContentPane().add("Center", demo);
        f.pack();
        f.setSize(new Dimension(1050,360));
        f.show();
        demo.start();
    }*/
}

