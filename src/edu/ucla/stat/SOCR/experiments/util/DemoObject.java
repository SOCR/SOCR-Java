package edu.ucla.stat.SOCR.experiments.util;

import java.awt.Image;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

  /**
         * The ObjectData class calculates the transformations and generates
         * random coordinates for all objects and generates random sizes
         * for shapes.
         */
   	class DemoObjectData extends Object {
        	boolean movingOb= false;
        	boolean print= false;
        	
            Object object;
            String name;
            Paint paint;
            static final int UP = 0;
            static final int DOWN = 1;
            double x, y;
            double ix, iy;
            int rotate;
            double scale, shear;
            int scaleDirection, shearDirection;
            int fromx, fromy, tox, toy;
            AffineTransform at = new AffineTransform();
            AffineTransform from = new AffineTransform();
            AffineTransform to = new AffineTransform();
    
	    // generates random transformation direction and factor
            public DemoObjectData(Object object, Paint paint, double size) {
                this.object = object;
                this.paint = paint;
                rotate = (int)(Math.random() * 360);
                //scale = Math.random() * 1.5;
                scale = size;
                scaleDirection = Math.random() > 0.5 ? UP : DOWN;
                shear = Math.random() * 0.5;
                shearDirection = Math.random() > 0.5 ? UP : DOWN;
            }
    

            public void setName(String n){
            	
            	name= n;
            	//System.out.println("setting name"+name);
            }
            
            public void setFrom(int index){
            	
            	if(object instanceof Image){
            	fromx= (int)(80*(index+1)*scale);
                fromy = 0;
            	} else if(object instanceof JComponent){
                	fromx= (int)(20*(index+1)*scale);
                    fromy = 0;
                	}
                
               
            }
            
            public void setStillTo(int index){
            	if(object instanceof Image){
	            	fromx=(int) (scale*(80+80*(index+1)));
	                fromy = 400;
            	}
            	else if(object instanceof JComponent){
                	fromx=(int)( scale*(100+20*(index+1)));
                    fromy = 400;
                	}
            }
            
            public void setTo(int index){
            	if(object instanceof Image){
	            	tox= (int)(scale*(80+80*(index+1)));
	                toy = 400;
            	}else if(object instanceof JComponent){
                	tox= (int)(scale*(100+20*(index+1)));
                    toy = 400;
                	}
                
                x = fromx;
                y = fromy;
            
            
                ix = (tox-fromx)/32 ; 
                iy = -(fromy-toy)/32;
               
              //  System.out.println("iy="+iy+" ix="+ix);
            }
            
            public void setMoving(boolean b){
            	movingOb = b;
            }
	    /* 
             * generates random coordinate values for all objects
             * and generates random size values for shapes
             */
            public void reset(int w, int h) {
            
                x = fromx;
                y = fromy;
                          	
                double ww = 20 + Math.random()*((w == 0 ? 400 : w)/4);
                double hh = 20 + Math.random()*((h == 0 ? 300 : h)/4);
                
              
                
                if (object instanceof Ellipse2D) {
                    ((Ellipse2D) object).setFrame(0, 0, ww, hh);
                } else if (object instanceof Rectangle2D) {
                    ((Rectangle2D) object).setRect(0, 0, ww, ww);
                } else if (object instanceof RoundRectangle2D) {
                    ((RoundRectangle2D) object).setRoundRect(0, 0, hh, hh, 20, 20); 
                } else if (object instanceof Arc2D) {
                    ((Arc2D) object).setArc(0, 0, hh, hh, 45, 270, Arc2D.PIE);
                } else if (object instanceof QuadCurve2D) {
                    ((QuadCurve2D) object).setCurve(0, 0, w*.2, h*.4, w*.4, 0);
                } else if (object instanceof CubicCurve2D) {
                        ((CubicCurve2D) object).setCurve(0,0,30,-60,60,60,90,0);
                } else if (object instanceof GeneralPath) {
          
                    GeneralPath p = new GeneralPath();
                    float size = (float) ww;
                    p.moveTo(- size / 2.0f, - size / 8.0f);
                    p.lineTo(+ size / 2.0f, - size / 8.0f);
                    p.lineTo(- size / 4.0f, + size / 2.0f);
                    p.lineTo(+         0.0f, - size / 2.0f);
                    p.lineTo(+ size / 4.0f, + size / 2.0f);
                    p.closePath();
                    object = p;
                }
            }
            
            public void reset(int w, int h, int fromX, int fromY, int toX, int toY){
          
            	 x = fromX;
                 y = fromY;
                 
             	
                // double ww = 20 + Math.random()*((w == 0 ? 400 : w)/4);
               //  double hh = 20 + Math.random()*((h == 0 ? 300 : h)/4);
                 double ww = 20 + Math.random()*(toX-fromX)/4;
                 double hh = 20 + Math.random()*(toY-fromY)/4; 
                 
                 if (object instanceof Ellipse2D) {
                     ((Ellipse2D) object).setFrame(0, 0, ww, hh);
                 } else if (object instanceof Rectangle2D) {
                     ((Rectangle2D) object).setRect(0, 0, ww, ww);
                 } else if (object instanceof RoundRectangle2D) {
                     ((RoundRectangle2D) object).setRoundRect(0, 0, hh, hh, 20, 20); 
                 } else if (object instanceof Arc2D) {
                     ((Arc2D) object).setArc(0, 0, hh, hh, 45, 270, Arc2D.PIE);
                 } else if (object instanceof QuadCurve2D) {
                     ((QuadCurve2D) object).setCurve(0, 0, w*.2, h*.4, w*.4, 0);
                 } else if (object instanceof CubicCurve2D) {
                         ((CubicCurve2D) object).setCurve(0,0,30,-60,60,60,90,0);
                 } else if (object instanceof GeneralPath) {
                 	System.out.println("ObjectData reset image?");
                     GeneralPath p = new GeneralPath();
                     float size = (float) ww;
                     p.moveTo(- size / 2.0f, - size / 8.0f);
                     p.lineTo(+ size / 2.0f, - size / 8.0f);
                     p.lineTo(- size / 4.0f, + size / 2.0f);
                     p.lineTo(+  0.0f, - size / 2.0f);
                     p.lineTo(+ size / 4.0f, + size / 2.0f);
                     p.closePath();
                     object = p;
                 }
                 
             /*    from.setToIdentity();
                 from.rotate(Math.toRadians(0), fromx, fromy);
                 from.translate(fromx, fromy);
                 
                 to.setToIdentity();
                 to.rotate(Math.toRadians(0), tox, toy);
                 to.translate(tox, toy);
                 */
            }

	    /*
             * calculates new transformation factors for the
             * chosen transformation and the current direction
             */    
            public void step(int w, int h, Demo demo) {
            	
            	
            	//System.out.println("OB step");
            	//System.out.println("od : "+name+" from ("+ fromx+","+fromy+") -> ("+tox+","+toy+") at ("+at.getTranslateX()+"," +at.getTranslateY()+")");
            
            	if(demo.stepCount >1)
            		print= false;
            	
                if(demo.stepCount >5 && demo.finishingStep<1)
                	demo.doShear= true;
                
            	if (demo.doRotate) {
                    if ((rotate+=5) == 360) {
                        rotate = 0;
                    }
                    at.rotate(Math.toRadians(rotate), x, y);
                }
                
                if(movingOb){
                	at.setToIdentity();
                	at.translate(x, y);
                	if(print)
                		System.out.println("drawing moving ob "+name+ " atx="+x+" aty="+y  );
                }            	
                else {
                	from.setToIdentity();
                	from.translate(fromx, fromy);
                	from.scale(scale, scale);
                	if(print)
                		System.out.println("drawing still ob "+name+ " fromx"+fromx+" fromy"+fromy  );
                	return;
                }
               // to.translate(tox, toy);
                
              /*  if (demo.doTranslate) {
                    x += ix;
                    y += iy;
                    if (x > w) {
                        x = w - 1;
                        ix = Math.random() * -w/32 - 1;
                    }
                    if (x < 0) {
                        x = 2;
                        ix = Math.random() * w/32 + 1;
                    }
                    if (y > h ) {
                        y = h - 2;
                        iy = Math.random() * -h/32 - 1;
                    }
                    if (y < 0) {
                        y = 2;
                        iy = Math.random() * h/32 + 1;
                    }
                }*/
                
                if (demo.doTranslate) {	
                    x += ix;
                    y += iy;
                    
                    if (x > w || x<0 || Math.abs(x-tox)<5) {
                      ix = 0;
                      x=tox;
                     // demo.stopCount++;
                    }
                   
                    if (y>toy || y< 0 ) {
                    //  System.out.println("outof boung setting iy=0");
                        iy = 0; 
                        x= tox;
                        demo.stopCount++;
                    }
                    if(iy==0){
                    	x=tox;
                    	demo.finishingStep++;
                    }
                   
                }
                if (demo.doScale && scaleDirection == UP) {
                    if ((scale += 0.05) > 1.5) {
                        scaleDirection = DOWN;
                    }
                } else if (demo.doScale && scaleDirection == DOWN) {
                    if ((scale -= .05) < 0.5) {
                        scaleDirection = UP;
                    }
                }
               // if (demo.doScale) {
                    at.scale(scale, scale);
              //  }
                if (demo.doShear && shearDirection == UP) {
                    if ((shear += 0.05) > 0.5) {
                        shearDirection = DOWN;
                    }
                } else if (demo.doShear && shearDirection == DOWN) {
                    if ((shear -= .05) < -0.5) {
                        shearDirection = UP;
                    }
                }
                if (demo.doShear) {
                    at.shear(shear, shear);
                }
            }
        } // End ObjectData class

