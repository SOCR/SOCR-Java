package JSci.awt;

import java.awt.*;

/**
* A graph layout arranges components in the style of a graph.
* Available regions are:
* <code>Title</code>
* <code>Graph</code>
* <code>X-axis</code>
* <code>Y-axis</code>
* @version 0.5
* @author Mark Hale
*/
public final class GraphLayout implements LayoutManager2 {
        private final static Dimension zeroDim=new Dimension();
        private Component title,graph,xaxis,yaxis;
        public GraphLayout() {}
        public void addLayoutComponent(String name,Component comp) {}
        public void addLayoutComponent(Component comp,Object constraint) {
                if(constraint.equals("Title"))
                        title=comp;
                else if(constraint.equals("Graph"))
                        graph=comp;
                else if(constraint.equals("X-axis"))
                        xaxis=comp;
                else if(constraint.equals("Y-axis"))
                        yaxis=comp;
        }
        public void removeLayoutComponent(Component comp) {
                if(comp.equals(title))
                        title=null;
                if(comp.equals(graph))
                        graph=null;
                if(comp.equals(xaxis))
                        xaxis=null;
                if(comp.equals(yaxis))
                        yaxis=null;
        }
        public void layoutContainer(Container parent) {
                synchronized(parent.getTreeLock()) {
                        Dimension size=parent.getSize();
                        Insets insets=parent.getInsets();
                        int width=size.width-insets.left-insets.right;
                        int height=size.height-insets.top-insets.bottom;
                        int graphLeftPad=0,graphAxisPad=0;
                        if(graph instanceof Graph2D) {
                                graphLeftPad=((Graph2D)graph).leftAxisPad;
                                graphAxisPad=((Graph2D)graph).axisPad;
                        } else if(graph instanceof CategoryGraph2D) {
                                graphLeftPad=((CategoryGraph2D)graph).leftAxisPad;
                                graphAxisPad=((CategoryGraph2D)graph).axisPad;
                        }
                        int yaxisWidth=getMinimumSize(yaxis).width;
                        int graphWidth=width-yaxisWidth;
                        int titleWidth=graphWidth-graphLeftPad-graphAxisPad;
                        int xaxisWidth=titleWidth;
                        int titleHeight=getMinimumSize(title).height;
                        int xaxisHeight=getMinimumSize(xaxis).height;
                        int graphHeight=height-titleHeight-xaxisHeight;
                        int yaxisHeight=graphHeight-2*graphAxisPad;
                        if(title!=null)
                                title.setBounds(insets.left+yaxisWidth+graphLeftPad,insets.top,titleWidth,titleHeight);
                        if(graph!=null)
                                graph.setBounds(insets.left+yaxisWidth,insets.top+titleHeight,graphWidth,graphHeight);
                        if(yaxis!=null)
                                yaxis.setBounds(insets.left,insets.top+titleHeight+graphAxisPad,yaxisWidth,yaxisHeight);
                        if(xaxis!=null)
                                xaxis.setBounds(insets.left+yaxisWidth+graphLeftPad,height-xaxisHeight,xaxisWidth,xaxisHeight);
                }
        }
        public void invalidateLayout(Container parent) {}
        public float getLayoutAlignmentX(Container parent) {
                return 0.5f;
        }
        public float getLayoutAlignmentY(Container parent) {
                return 0.5f;
        }
        public Dimension minimumLayoutSize(Container parent) {
                return calculateLayoutSize(parent.getInsets(),
                        getMinimumSize(title),
                        getMinimumSize(graph),
                        getMinimumSize(xaxis),
                        getMinimumSize(yaxis));
        }
        private static Dimension getMinimumSize(Component comp) {
                if(comp==null)
                        return zeroDim;
                else
                        return comp.getMinimumSize();
        }
        public Dimension preferredLayoutSize(Container parent) {
                return calculateLayoutSize(parent.getInsets(),
                        getMinimumSize(title),
                        getPreferredSize(graph),
                        getMinimumSize(xaxis),
                        getMinimumSize(yaxis));
        }
        private static Dimension getPreferredSize(Component comp) {
                if(comp==null)
                        return zeroDim;
                else
                        return comp.getPreferredSize();
        }
        public Dimension maximumLayoutSize(Container parent) {
                return calculateLayoutSize(parent.getInsets(),
                        getMaximumSize(title),
                        getMaximumSize(graph),
                        getMaximumSize(xaxis),
                        getMaximumSize(yaxis));
        }
        private static Dimension getMaximumSize(Component comp) {
                if(comp==null)
                        return zeroDim;
                else
                        return comp.getMaximumSize();
        }
        private static Dimension calculateLayoutSize(Insets insets,Dimension title,Dimension graph,Dimension xaxis,Dimension yaxis) {
                int width=insets.left+insets.right;
                int height=insets.top+insets.bottom;
                if(title.width>yaxis.width+graph.width)
                        width+=title.width;
                else
                        width+=yaxis.width+graph.width;
                height+=title.height+xaxis.height;
                if(yaxis.height>graph.height)
                        height+=yaxis.height;
                else
                        height+=graph.height;
                return new Dimension(width,height);
        }
}

