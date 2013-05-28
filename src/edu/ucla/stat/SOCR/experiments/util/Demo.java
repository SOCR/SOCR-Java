package edu.ucla.stat.SOCR.experiments.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Vector;

import javax.swing.JPanel;

 /**
     * The Demo class performs the transformations and the painting.
     */
 	class Demo extends JPanel implements Runnable {

    	TransformAnim anim;
    	String expChoice;

       // private static TexturePaint texture;
        int  numOfOrigImage;
        // creates the TexturePaint pattern
      /*  static {
            BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            Graphics2D gi = bi.createGraphics();
            gi.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            gi.setColor(Color.red);
            gi.fillOval(0,0,9,9);
           // texture = new TexturePaint(bi,new Rectangle(0,0,10,10));
        }*/
    
     //   private static BasicStroke bs = new BasicStroke(6); 
        private static Font fonts[] = {
                    new Font("Times New Roman", Font.PLAIN, 48),
                    new Font("serif", Font.BOLD + Font.ITALIC, 24),
                    new Font("Courier", Font.BOLD, 36),
                    new Font("Arial", Font.BOLD + Font.ITALIC, 64),
                    new Font("Helvetica", Font.PLAIN, 52)};
        private static String strings[] = {
                    "Transformation", "Rotate", "Translate",
                    "Shear", "Scale" };
       // private static String imgs[] = { "redDie.gif","card9.gif", "duke.gif"};
        private static String cards[] = new String[53];
        private static Coin headCoin, tailCoin, empCoin;
        private static Coin numCoins[];
        
     /*   private static Paint paints[] = { 
                    Color.red, Color.blue, texture, Color.green, Color.magenta, 
                    Color.orange, Color.pink, Color.cyan, 
                    new Color(0, 255, 0, 128), new Color(0, 0, 255, 128), 
                    Color.yellow, Color.lightGray, Color.white};*/
        public Vector vector = new Vector(1000);
        public int numShapes, numStrings, numImages;
        private Thread thread;
        private BufferedImage bimg;
        protected boolean doRotate = false;
        protected boolean doTranslate = true;
        protected boolean doScale = false;
        protected boolean doShear;
        
        protected int finishingStep=0;
        protected int stepCount=0;
        protected int startingStep;
        
        protected int stopCount=0;

    
        public Demo(TransformAnim a) {
        	anim = a;
        	doRotate = a.g_doRotate;
        	doTranslate = doTranslate;
        	doScale = a.g_doScale;
        	doShear = a.g_doShear;
        	 
            setBackground(Color.black);

            for (int i=0; i<53; i++)
            	cards[i] = "card"+i+".gif";
            
            headCoin = new Coin(1,  10, Color.red, new Color(0, 180, 0), Color.white);
            headCoin.setTossed(true);
            headCoin.setValue(1.0);
            tailCoin = new Coin(1,  10, Color.red, new Color(0, 180, 0), Color.white);
            tailCoin.setTossed(true);
            tailCoin.setValue(0.0);
            
            empCoin = new Coin(1,  10, Color.red, new Color(0, 180, 0), Color.white);
            empCoin.setTossed(true);
            empCoin.setValue(2.0);
            
            // initializes numbers of Strings, Images and Shapes
           // setStrings(2);
           // setImages(3);
      
          //  setShapes(2);
            setSize(new Dimension(500, 400));
        }
    
        public void setExpChoice(String exp){
        	System.out.println("setting "+exp);
        	vector = new Vector(1000);
        	expChoice = exp;
        }
        
        public Image getImage(String name) {
        	
            URL url = TransformAnim.class.getResource(name);
          
            Image img = getToolkit().getImage(url);
            try {
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(img, 0);
                tracker.waitForID(0);
            } catch (Exception e) {}
            return img;
        }

    
        // adds images to a Vector
      /*  public void setImages(int num) {
           
            if (num < numImages) {
                Vector v = new Vector(vector.size());
                for (int i = 0; i < vector.size(); i++) {
                    if (((DemoObjectData) vector.get(i)).object instanceof Image) {
                        v.addElement(vector.get(i));
                    }
                }
                vector.removeAll(v);
                v.setSize(num);
                vector.addAll(v);
            } else {
                Dimension d = getSize();
                for (int i = numImages; i < num; i++) {
                    Object obj = getImage(imgs[i % imgs.length]);
                    DemoObjectData od = new DemoObjectData(obj, Color.black,1);
                    od.reset(d.width, d.height);
                    vector.addElement(od);
                }
            }
            numImages = num;
        }*/

        public void setFromObs(double[] cardValue, int length) {
  
        		numOfOrigImage=length;
                Dimension d = getSize();
                 
                double scale =1;
            	if(length>10)
            		scale = 1/(double)length*7;
            	if(scale<0.5)
            		scale = 0.5;
            	
                for (int i =0; i < length; i++) {
                	//the still from cards
                	DemoObjectData od = null;
                	if(expChoice.equals("Card")){
	                    Object obj = getImage("cards/"+cards[(int)cardValue[i]]);
	                    od = new DemoObjectData(obj, Color.black, scale); 
	                    od.setName("card"+cardValue[i]);
                	}else if (expChoice.equals("Coin")){
	                    Object obj;
	                    if(cardValue[i]==1)
	                    	obj = headCoin;
	                    else obj = tailCoin;
	                    od = new DemoObjectData(obj, Color.black, scale); 
	                    od.setName("card"+cardValue[i]);
                	}else {
                		 Object obj;
                		 Coin numCoin = new Coin(1,  10, Color.red, new Color(0, 180, 0), Color.white);
                         numCoin.setTossed(true);
                         numCoin.set2Sides(false);
                         
                		 obj = numCoin;
 	                    numCoin.setValue(cardValue[i]);
 	                    od = new DemoObjectData(obj, Color.black, scale); 
 	                    od.setName("card"+cardValue[i]);
                	}
               
                    od.setFrom(i);
               //     System.out.println("from card "+od.name+" fromx"+od.fromx);
                    od.reset(d.width, d.height, od.fromx, od.fromy, od.fromx, d.height);
                    od.setMoving(false);
                    //set from and to
                    vector.addElement(od);
                    
                }    
        }
        
        public void removeToObs(){
      
        	int length = vector.size();
        	 for (int i = length-1; i>length/3-1; i--) {
        		 vector.remove(i);
        	 } 	
        }
        
        public void setToObs(double[] inValue,  int[] fromIndex, int length) {

            Dimension d = getSize();
        	DemoObjectData od = null;
        	Object obj = null;
           	
        	//the moving at card
        	double scale =1;
        	if(length>10)
        		scale = 1/(double)length*7;
        	if(scale<0.5)
        		scale = 0.5;
        	 
        for (int i =0; i < length; i++) {
        	if(expChoice.equals("Card")){
            	obj = getImage("cards/"+cards[(int)inValue[i]]);
            	od = new DemoObjectData(obj, Color.black, scale); 
            	od.setName("card"+inValue[i]);
        	}else if (expChoice.equals("Coin")){
               
                if(inValue[i]==1.0)
                	obj = headCoin;
                else obj = tailCoin;
                od = new DemoObjectData(obj, Color.black, scale); 
                od.setName("coin");
        	}else{
        		 Coin numCoin = new Coin(1,  10, Color.red, new Color(0, 180, 0), Color.white);
                 numCoin.setTossed(true);
                 numCoin.set2Sides(false);
                 obj = numCoin;
                 numCoin.setValue(inValue[i]);
        		 od = new DemoObjectData(obj, Color.black, scale); 
                 od.setName("number");
        	}
             
        	od.setFrom(fromIndex[i]);
        	od.setTo(i);
              // System.out.println("at card "+od.name+" fromx"+od.fromx+ "->tox"+od.tox);
        	od.setMoving(true);
        	od.reset(d.width, d.height, od.fromx, od.fromy, od.tox, od.toy);
               //set from and to
        	vector.addElement(od);
 	
        }  
        // the still to card
            for (int i =0; i < length; i++) {
            
            	
            	if(expChoice.equals("Card")){
            		obj = getImage("cards/"+cards[52]);
	            	od = new DemoObjectData(obj, Color.black, scale); 
	            	od.setName("card"+inValue[i]);
            	}
            	else {
            		obj= empCoin;
            		od = new DemoObjectData(obj, Color.black, scale); 
            		od.setName("coin");
            	}
            	
            	
            	od.setStillTo(i);
            	od.reset(d.width, d.height, od.fromx, od.fromy, od.fromx, d.height);
            	od.setMoving(false);
               //  System.out.println("to card "+od.name+" fromx"+od.fromx);
                 //set from and to
            	vector.addElement(od);
            }	
         
            
            System.out.println("vector length"+vector.size());
    }
        // adds Strings to a Vector
       /* public void setStrings(int num) {
    
            if (num < numStrings) {
                Vector v = new Vector(vector.size());
                for (int i = 0; i < vector.size(); i++) {
                    if (((DemoObjectData) vector.get(i)).object instanceof DemoTextData) {
                        v.addElement(vector.get(i));
                    }
                }
                vector.removeAll(v);
                v.setSize(num);
                vector.addAll(v);
            } else {
                Dimension d = getSize();
                for (int i = numStrings; i < num; i++) {
                    int j = i % fonts.length;
                    int k = i % strings.length;
                    Object obj = new DemoTextData(strings[k], fonts[j]); 
                    DemoObjectData od = new DemoObjectData(obj, paints[i%paints.length], 1);
                    od.reset(d.width, d.height);
                    vector.addElement(od);
                }
            }
            numStrings = num;
        }*/
            

        // adds Shapes to a Vector
     /*   public void setShapes(int num) {
    
            if (num < numShapes) {
                Vector v = new Vector(vector.size());
                for (int i = 0; i < vector.size(); i++) {
                    if (((DemoObjectData) vector.get(i)).object instanceof Shape) {
                        v.addElement(vector.get(i));
                    }
                }
                vector.removeAll(v);
                v.setSize(num);
                vector.addAll(v);
            } else {
                Dimension d = getSize();
                for (int i = numShapes; i < num; i++) {
                    Object obj = null;
                    switch (i % 7) {
                        case 0 : obj = new GeneralPath(); break;
                        case 1 : obj = new Rectangle2D.Double(); break;
                        case 2 : obj = new Ellipse2D.Double(); break;
                        case 3 : obj = new Arc2D.Double(); break;
                        case 4 : obj = new RoundRectangle2D.Double(); break;
                        case 5 : obj = new CubicCurve2D.Double(); break;
                        case 6 : obj = new QuadCurve2D.Double(); break;
                    }
                    DemoObjectData od = new DemoObjectData(obj, paints[i%paints.length], 1);
                    od.reset(d.width, d.height);
                    vector.addElement(od);
                }
            } 
            numShapes = num;
        }*/

            
        // calls DemoObjectData.reset for each item in vector
        public void reset(int w, int h) {

            for (int i = 0; i < vector.size(); i++) {
                ((DemoObjectData) vector.get(i)).reset(w, h);
            }
        }

    public void removeAll(){
    	vector.removeAllElements();
    	vector = new Vector(1000);
    }
        // calls DemoObjectData.step for each item in vector
        public synchronized  void step(int w, int h) {
        	//System.out.println("-------");'
        	if (vector.size()==0)
        		return;
        	
        	stepCount++;
        	
            for (int i = 0; i < vector.size(); i++) {
            	//System.out.println("moving?"+((DemoObjectData) vector.get(i)).movingOb);
                ((DemoObjectData) vector.get(i)).step(w, h, this);
            }
           // System.out.println("*-------");
            
     //  System.out.println("*stopCount="+stopCount);
            if (stopCount>vector.size()/3 && finishingStep <vector.size()*9){
            	//this.doRotate = true;
            	this.doShear= false;
            	//System.out.println("shear= false");
            }else if(finishingStep >vector.size()*3){
            	finishingStep=0;
            	//this.doRotate = false;
            	removeToObs();
            	//stop();
            	notifyObservers();
            }
            
         //   System.out.println("finishingStep="+finishingStep);
        }
    
    
        public void drawDemoAt(int w, int h, Graphics2D g2) {
        	       		   
            for (int i = 0; i < vector.size(); i++) {
                DemoObjectData od = (DemoObjectData) vector.get(i);
                if (!od.movingOb)
                	continue;
             
                g2.setTransform(od.at);
                g2.setPaint(od.paint);
                if (od.object instanceof Image) {
                	//if(stepCount==1)
                		//	System.out.println("drawing image at :"+od.at.toString());
                    g2.drawImage((Image) od.object, 0, 0, this);
                } else if (od.object instanceof DemoTextData) {
                    g2.setFont(((DemoTextData) od.object).font);
                    g2.drawString(((DemoTextData) od.object).string, 0, 0);
                }/* else if (od.object instanceof QuadCurve2D 
                        || od.object instanceof CubicCurve2D) 
                {
                    g2.setStroke(bs);
                    g2.draw((Shape) od.object);
                } else if (od.object instanceof Shape) {
                    g2.fill((Shape) od.object);
                }else if (od.object instanceof JComponent){
                	((Coin) od.object).paintCoin(g2, 0, 0);
                	//g2.drawImage((Image)od.object, 0, 0, this);
                
                }*/
               
            }
        }
    
        public void drawDemoFrom(int w, int h, Graphics2D g2) {
            
            for (int i = 0; i < vector.size(); i++) {
                DemoObjectData od = (DemoObjectData) vector.get(i);
                if (od.movingOb)
                	continue;
                
                g2.setTransform(od.from);
                g2.setPaint(od.paint);
                if (od.object instanceof Image) {
                	//if(stepCount==1)
                		//System.out.println("drawing image from :"+od.from.toString());
                    g2.drawImage((Image) od.object, 0, 0, this);
                } else if (od.object instanceof DemoTextData) {
                    g2.setFont(((DemoTextData) od.object).font);
                    g2.drawString(((DemoTextData) od.object).string, 0, 0);
                }/* else if (od.object instanceof QuadCurve2D 
                        || od.object instanceof CubicCurve2D) 
                {
                    g2.setStroke(bs);
                    g2.draw((Shape) od.object);
                } else if (od.object instanceof Shape) {
                    g2.fill((Shape) od.object);
                }else if (od.object instanceof JComponent){
                	((Coin) od.object).paintCoin(g2, 0, 0 );
                	
                	//g2.drawImage((Image)od.object, 0, 0, this);
                }*/
            }
        }
        
    /*    public void drawDemoTo(int w, int h, Graphics2D g2) {
            
            for (int i = 0; i < vector.size(); i++) {
                DemoObjectData od = (DemoObjectData) vector.get(i);
                g2.setTransform(od.to);
                g2.setPaint(od.paint);
                if (od.object instanceof Image) {
                	//System.out.println("drawing image");
                    g2.drawImage((Image) od.object, 0, 0, this);
                } else if (od.object instanceof DemoTextData) {
                    g2.setFont(((DemoTextData) od.object).font);
                    g2.drawString(((DemoTextData) od.object).string, 0, 0);
                } else if (od.object instanceof QuadCurve2D 
                        || od.object instanceof CubicCurve2D) 
                {
                    g2.setStroke(bs);
                    g2.draw((Shape) od.object);
                } else if (od.object instanceof Shape) {
                    g2.fill((Shape) od.object);
                }
            }
        }*/
        public Graphics2D createGraphics2D(int w, int h) {
            Graphics2D g2 = null;
            if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
                bimg = (BufferedImage) createImage(w, h);
                reset(w, h);
            } 
            g2 = bimg.createGraphics();
            g2.setBackground(getBackground());
            g2.clearRect(0, 0, w, h);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY);
            return g2;
        }
    
    
        public void paint(Graphics g) {
        
            Dimension d = getSize();
            step(d.width, d.height);
            Graphics2D g2 = createGraphics2D(d.width, d.height);
            drawDemoFrom(d.width, d.height, g2);
            drawDemoAt(d.width, d.height, g2);
           
         //   drawDemoTo(d.width, d.height, g2);
            g2.dispose();
            g.drawImage(bimg, 0, 0, this);
            
        }
    
    
        public void start() {
        	System.out.println("demo start");
        	stepCount=0;
        	doShear = false;
        	
        	if(thread==null)
        		thread = new Thread(this);
        		thread.setPriority(Thread.MIN_PRIORITY);
        	try{
        		thread.start();   	
        	}catch(Exception e){
        		;
        	}
        }

	    public void notifyObservers(){
	    	 stopCount=0;
	    	 anim.observable.notifyObservers();
	    }
	    
        public synchronized void stop() {
        //	thread = null;
     
            stopCount=0;
            anim.observable.notifyObservers();
        }
    
    
        public void run() {
            Thread me = Thread.currentThread();
            while (thread == me) {
                repaint();
                try {
                    thread.sleep(200);
                } catch (InterruptedException e) { break; }
            }
            thread = null;
        }
    }
    
