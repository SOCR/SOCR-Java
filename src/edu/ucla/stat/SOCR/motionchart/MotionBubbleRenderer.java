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
 It's Online, Therefore, It Exists!
 ***************************************************/

package edu.ucla.stat.SOCR.motionchart;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.*;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.BooleanList;
import org.jfree.util.ObjectList;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * Date: Dec 2, 2008
 * Time: 12:36:52 PM
 *
 * @author Jameel
 */
@SuppressWarnings({"InconsistentJavaDoc"})
public class MotionBubbleRenderer extends XYBubbleRenderer implements PlotChangeListener, AxisChangeListener, DatasetChangeListener
{
    private static final Color DEFAULT_COLOR = new Color(204, 204, 204, 255);
    private static final Color DEFAULT_OUTLINE_PAINT = Color.lightGray;
    private static final BasicStroke DEFAULT_OUTLINE_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
            BasicStroke.JOIN_BEVEL);
    private static final double SIZE_MULTIPLIER = 0.75;
    private MotionDataSet dataset;
    private int highlightSeries = -1;
    private int highlightItem = -1;
    private Ellipse2D.Double currCircle;
    private ValueAxis domainAxis;
    private ValueAxis rangeAxis;
    private double domainAxisLength = 0;
    private double rangeAxisLength = 0;
    private double domainZoomMultiplier = 1;
    private double rangeZoomMultiplier = 1;

    /** The item label anchor offset. */
    private double itemLabelAnchorOffset = 2.0;

    /** The adjacent offset. */
    private static final double ADJ = Math.cos(Math.PI / 6.0);

    /** The opposite offset. */
    private static final double OPP = Math.sin(Math.PI / 6.0);

    /** A list of items that are "selected" and should be highlighted  */
    private ObjectList selectedItems;

    /** A list of categories that are "selected" and should be highlighted  */
    private HashMap<Object,Boolean> selectedCategories;

    /**
     * Creates a new MotionBubbleRenderer instance.
     *
     * @param dataset the dataset to be rendered (an {@link edu.ucla.stat.SOCR.motionchart.MotionDataSet} is expected).
     */
    public MotionBubbleRenderer(MotionDataSet dataset)
    {
        this(dataset, SCALE_ON_BOTH_AXES);
    }

    /**
     * Constructs a new MotionBubbleRenderer with the specified type of scaling.
     *
     * @param dataset   the dataset that the renderer will use
     * @param scaleType the type of scaling (must be one of:
     *                  {@link #SCALE_ON_BOTH_AXES}, {@link #SCALE_ON_DOMAIN_AXIS},
     *                  {@link #SCALE_ON_RANGE_AXIS}).
     */
    public MotionBubbleRenderer(MotionDataSet dataset, int scaleType)
    {
        super(scaleType);
        this.dataset = dataset;
        dataset.addChangeListener(this);
        this.selectedItems = new ObjectList();
        this.selectedCategories = new HashMap<Object,Boolean>();
        this.setBaseSeriesVisible(false, false);
    }

    /**
     * Draws the visual representation of a single data item.
     *
     * @param g2             the graphics device.
     * @param state          the renderer state.
     * @param dataArea       the area within which the data is being drawn.
     * @param info           collects information about the drawing.
     * @param plot           the plot (can be used to obtain standard color
     *                       information etc).
     * @param domainAxis     the domain (horizontal) axis.
     * @param rangeAxis      the range (vertical) axis.
     * @param dataset        the dataset (a {@link edu.ucla.stat.SOCR.motionchart.MotionDataSet} is expected).
     * @param series         the series index (zero-based).
     * @param item           the item index (zero-based).
     * @param crosshairState crosshair information for the plot
     *                       (<code>null</code> permitted).
     * @param pass           the pass index.
     */
    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass)
    {
        if (!(dataset instanceof MotionDataSet))
        {
            throw new IllegalArgumentException("The dataset must be of type MotionDataSet.");
        }

        // return straight away if the item is not visible
        if (!getItemVisible(series, item))
        {
            return;
        }

        PlotOrientation orientation = plot.getOrientation();

        Ellipse2D.Double shape = (Ellipse2D.Double)getItemShape(series, item);

        if (shape.getWidth() != 0 && shape.getHeight() != 0)
        {
            Ellipse2D.Double circle = translateShape(shape, plot, dataArea);
            g2.setPaint(getItemPaint(series, item));
            g2.fill(circle);
            g2.setStroke(getItemOutlineStroke(series, item));
            g2.setPaint(getItemOutlinePaint(series, item));
            g2.draw(circle);

            if (isItemLabelVisible(series, item))
            {
                currCircle = circle;
                if (orientation == PlotOrientation.VERTICAL)
                {
                    drawItemLabel(g2, orientation, dataset, series, item,
                            circle.getCenterX(), circle.getCenterY(), false);
                }
                else if (orientation == PlotOrientation.HORIZONTAL)
                {
                    drawItemLabel(g2, orientation, dataset, series, item,
                            circle.getCenterY(), circle.getCenterX(), false);
                }
            }

            // add an entity if this info is being collected
            EntityCollection entities;
            if (info != null)
            {
                entities = info.getOwner().getEntityCollection();
                if (entities != null && circle.intersects(dataArea))
                {
                    addEntity(entities, circle, dataset, series, item,
                            circle.getCenterX(), circle.getCenterY());
                }
            }

            int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
            int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
            updateCrosshairValues(crosshairState, shape.getCenterX(), shape.getCenterY(), domainAxisIndex,
                    rangeAxisIndex, circle.getCenterX(), circle.getCenterY(), orientation);
        }
    }

    /**
     * Translates the shape so that it displays correctly given the plot and dataArea.
     *
     * @param shape     the shape to translate
     * @param plot      the plot that will be used to translate the shape
     * @param dataArea  the dataArea that the shape will be translated to
     * @return          The translated shape
     */
    @SuppressWarnings({"SuspiciousNameCombination"})
    protected Ellipse2D.Double translateShape(Ellipse2D.Double shape, XYPlot plot, Rectangle2D dataArea)
    {
        Ellipse2D.Double circle = null;

        //double x = shape.getCenterX();
        //double y = shape.getCenterY();
        double z = shape.getWidth();

        PlotOrientation orientation = plot.getOrientation();

        ValueAxis domainAxis = plot.getDomainAxis();
        ValueAxis rangeAxis = plot.getRangeAxis();
        RectangleEdge domainAxisLocation = plot.getDomainAxisEdge();
        RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();

        double transX1 = domainAxis.valueToJava2D(shape.getX(), dataArea, domainAxisLocation);
        double transX2 = domainAxis.valueToJava2D(shape.getX() + shape.getWidth(), dataArea, domainAxisLocation);
        //The upper-left corner is the lower-left on the graph (screen origin vs. graph origin)
        double transY1 = rangeAxis.valueToJava2D(shape.getY() + shape.getHeight(), dataArea, rangeAxisLocation);
        double transY2 = rangeAxis.valueToJava2D(shape.getY(), dataArea, rangeAxisLocation);

        double width = z * domainAxis.getRange().getLength() * domainZoomMultiplier * SIZE_MULTIPLIER;
        double height = z * rangeAxis.getRange().getLength() * rangeZoomMultiplier * SIZE_MULTIPLIER;
        double transWidth = domainAxis.lengthToJava2D(width, dataArea, domainAxisLocation);//transX2 - transX1;
        double transHeight = rangeAxis.lengthToJava2D(height, dataArea, rangeAxisLocation);//transY2 - transY1;

        double transX = (transX1 + transX2) / 2.0;
        double transY = (transY1 + transY2) / 2.0;

        switch (getScaleType())
        {
            case SCALE_ON_DOMAIN_AXIS:
                transHeight = transWidth;
                break;
            case SCALE_ON_RANGE_AXIS:
                transWidth = transHeight;
                break;
            default:
                break;
        }
        transWidth = Math.abs(transWidth);
        transHeight = Math.abs(transHeight);

        if (orientation == PlotOrientation.VERTICAL)
        {
            circle = new Ellipse2D.Double(transX - transWidth / 2.0,
                    transY - transHeight / 2.0, transWidth, transHeight);
        }
        else if (orientation == PlotOrientation.HORIZONTAL)
        {
            circle = new Ellipse2D.Double(transY - transHeight / 2.0,
                    transX - transWidth / 2.0, transHeight, transWidth);
        }

        return circle;
    }

    /**
     * Calculates the item label anchor point.
     *
     * @param anchor      the anchor.
     * @param x           the x coordinate.
     * @param y           the y coordinate.
     * @param orientation the plot orientation.
     * @return The anchor point (never <code>null</code>).
     */
    @Override
    protected Point2D calculateLabelAnchorPoint(ItemLabelAnchor anchor, double x, double y, PlotOrientation orientation)
    {
        Point2D result = null;
        double sizeX = 2.0 * this.itemLabelAnchorOffset;
        double sizeY = 2.0 * this.itemLabelAnchorOffset;

        if(currCircle != null)
        {
            sizeX = currCircle.getWidth() / 2.0 + this.itemLabelAnchorOffset;
            sizeY = currCircle.getHeight() / 2.0 + this.itemLabelAnchorOffset;
        }

        if (anchor == ItemLabelAnchor.CENTER) {
            result = new Point2D.Double(x, y);
        }
        else if (anchor == ItemLabelAnchor.INSIDE1) {
            result = new Point2D.Double(x + OPP * this.itemLabelAnchorOffset,
                    y - ADJ * this.itemLabelAnchorOffset);
        }
        else if (anchor == ItemLabelAnchor.INSIDE2) {
            result = new Point2D.Double(x + ADJ * this.itemLabelAnchorOffset,
                    y - OPP * this.itemLabelAnchorOffset);
        }
        else if (anchor == ItemLabelAnchor.INSIDE3) {
            result = new Point2D.Double(x + this.itemLabelAnchorOffset, y);
        }
        else if (anchor == ItemLabelAnchor.INSIDE4) {
            result = new Point2D.Double(x + ADJ * this.itemLabelAnchorOffset,
                    y + OPP * this.itemLabelAnchorOffset);
        }
        else if (anchor == ItemLabelAnchor.INSIDE5) {
            result = new Point2D.Double(x + OPP * this.itemLabelAnchorOffset,
                    y + ADJ * this.itemLabelAnchorOffset);
        }
        else if (anchor == ItemLabelAnchor.INSIDE6) {
            result = new Point2D.Double(x, y + this.itemLabelAnchorOffset);
        }
        else if (anchor == ItemLabelAnchor.INSIDE7) {
            result = new Point2D.Double(x - OPP * this.itemLabelAnchorOffset,
                    y + ADJ * this.itemLabelAnchorOffset);
        }
        else if (anchor == ItemLabelAnchor.INSIDE8) {
            result = new Point2D.Double(x - ADJ * this.itemLabelAnchorOffset,
                    y + OPP * this.itemLabelAnchorOffset);
        }
        else if (anchor == ItemLabelAnchor.INSIDE9) {
            result = new Point2D.Double(x - this.itemLabelAnchorOffset, y);
        }
        else if (anchor == ItemLabelAnchor.INSIDE10) {
            result = new Point2D.Double(x - ADJ * this.itemLabelAnchorOffset,
                    y - OPP * this.itemLabelAnchorOffset);
        }
        else if (anchor == ItemLabelAnchor.INSIDE11) {
            result = new Point2D.Double(x - OPP * this.itemLabelAnchorOffset,
                    y - ADJ * this.itemLabelAnchorOffset);
        }
        else if (anchor == ItemLabelAnchor.INSIDE12) {
            result = new Point2D.Double(x, y - this.itemLabelAnchorOffset);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE1) {
            result = new Point2D.Double(
                    x + sizeX * OPP,
                    y - sizeY * ADJ);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE2) {
            result = new Point2D.Double(
                    x + sizeX * ADJ,
                    y - sizeY * OPP);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE3) {
            result = new Point2D.Double(x + sizeX,
                    y);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE4) {
            result = new Point2D.Double(
                    x + sizeX * ADJ,
                    y + sizeY * OPP);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE5) {
            result = new Point2D.Double(
                    x + sizeX * OPP,
                    y + sizeY * ADJ);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE6) {
            result = new Point2D.Double(x,
                    y + sizeY);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE7) {
            result = new Point2D.Double(
                    x - sizeX * OPP,
                    y + sizeY * ADJ);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE8) {
            result = new Point2D.Double(
                    x - sizeX * ADJ,
                    y + sizeY * OPP);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE9) {
            result = new Point2D.Double(x - sizeX,
                    y);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE10) {
            result = new Point2D.Double(
                    x - sizeX * ADJ,
                    y - sizeY * OPP);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE11) {
            result = new Point2D.Double(
                x - sizeX * OPP,
                y - sizeY * ADJ);
        }
        else if (anchor == ItemLabelAnchor.OUTSIDE12) {
            result = new Point2D.Double(x,
                    y - sizeY);
        }
        return result;
    }

    /**
     * Returns the flag that controls whether a series is visible.
     *
     * @param series the series index (zero-based).
     * @return The flag (possibly <code>null</code>).
     * @see #setSeriesVisible(int, Boolean)
     */
    @Override
    public Boolean getSeriesVisible(int series)
    {
        if (series < 0 || series >= dataset.getSeriesCount())
        {
            return null;
        }

        return isSeriesVisible(series);
    }

    /**
     * Returns indices of all visible series.
     *
     * @return The list of visible series.
     * @see #getSeriesVisible(int)
     */
    public ArrayList<Integer> getVisibleSeries()
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        int count = dataset.getSeriesCount();

        for (int i = 0; i < count; i++)
        {
            if (isSeriesVisible(i))
            {
                list.add(i);
            }
        }

        return list;
    }

    /**
     * Returns the paint used to fill data items as they are drawn.
     * <p/>
     * The default implementation passes control to the
     * <code>lookupSeriesPaint()</code> method. You can override this method
     * if you require different behaviour.
     *
     * @param row    the row (or series) index (zero-based).
     * @param column the column (or category) index (zero-based).
     * @return The paint (never <code>null</code>).
     */
    @Override
    public Paint getItemPaint(int row, int column)
    {
        Color paint = dataset.getColor(row, column);

        if (paint == null)
        {
            paint = DEFAULT_COLOR;
        }

        if (shouldHighlight(row, column))
        {
            return new Color(paint.getRed(), paint.getGreen(), paint.getBlue(), 255);
        }

        return new Color(paint.getRed(), paint.getGreen(), paint.getBlue(), 127);
    }

    /**
     * Sets the item to be highlighted (use (-1, -1) for no highlight).
     *
     * @param series the series index (zero-based).
     * @param item   the item index (zero-based).
     */
    public void setHighlightedItem(int series, int item)
    {
        if (isHighlightedItem(series, item))
        {
            return;  // nothing to do
        }
        this.highlightSeries = series;
        this.highlightItem = item;
        notifyListeners(new RendererChangeEvent(this));
    }

    /**
     * Determines whether or not the item in the series is
     * highlighted.
     *
     * @param series    the series index (zero-based).
     * @param item      the item index (zero-based).
     * @return          <code>true</code> if the item in the series is highlighted. <code>false</code> otherwise.
     */
    public boolean isHighlightedItem(int series, int item)
    {
        return (this.highlightSeries == series && this.highlightItem == item);
    }

    /**
     * Sets the item to be selected.
     *
     * @param series the series index (zero-based).
     * @param item   the item index (zero-based).
     */
    public void setSelectedItem(int series, int item)
    {
        setSelectedItem(series, item, true);
    }

    /**
     * Sets the item to be selected.
     *
     * @param series    the series index (zero-based).
     * @param item      the item index (zero-based).
     * @param selected  <code>true</code> if the item should be selected. <code>false</code> otherwise.
     */
    public void setSelectedItem(int series, int item, boolean selected)
    {
        BooleanList list = (BooleanList)selectedItems.get(series);
        if(list == null)
        {
            list = new BooleanList();
            selectedItems.set(series, list);
        }

        list.setBoolean(item, selected);
    }

    /**
     * Determines whether or not the item in the series is
     * selected.
     *
     * @param series    the series index (zero-based).
     * @param item      the item index (zero-based).
     * @return          <code>true</code> if the item in the series is selected. <code>false</code> otherwise.
     */
    public boolean isSelectedItem(int series, int item)
    {
        BooleanList list = (BooleanList)selectedItems.get(series);
        return list != null && list.getBoolean(item) != null && list.getBoolean(item);
    }

    /**
     * Sets the category to be selected.
     *
     * @param category  the category key.
     */
    public void setSelectedCategory(Object category)
    {
        setSelectedCategory(category, true);
    }

    /**
     * Sets the category to be selected.
     *
     * @param category  the category key.
     * @param selected  <code>true</code> if the item should be selected. <code>false</code> otherwise.
     */
    public void setSelectedCategory(Object category, boolean selected)
    {
        selectedCategories.put(category, selected);
    }

    /**
     * Determines whether or not the category is selected.
     *
     * @param category  the category key.
     * @return          <code>true</code> if the category is selected. <code>false</code> otherwise.
     */
    public boolean isSelectedCategory(Object category)
    {
        Boolean selected = selectedCategories.get(category);
        return (selected != null) && selected;
    }

    /**
     * Determines whether or not the item should be highlighted.
     *
     * @param series    the series index (zero-based).
     * @param item      the item index (zero-based).
     * @return          <code>true</code> if the item should be highlighted. <code>false</code> otherwise.
     */
    private boolean shouldHighlight(int series, int item)
    {
        return isHighlightedItem(series, item) || isSelectedItem(series, item)
                || isSelectedCategory(dataset.getCategory(series,item));
    }

    /**
     * Returns the paint used to outline data items as they are drawn.
     * <p/>
     * The default implementation passes control to the
     * {@link #lookupSeriesOutlinePaint} method.  You can override this method
     * if you require different behaviour.
     *
     * @param row    the row (or series) index (zero-based).
     * @param column the column (or category) index (zero-based).
     * @return The paint (never <code>null</code>).
     */
    @Override
    public Paint getItemOutlinePaint(int row, int column)
    {
        if (shouldHighlight(row, column))
        {
            return ((Color) getItemPaint(row, column)).darker();
        }

        return super.getItemOutlinePaint(row, column);
    }

    /**
     * Returns the stroke used to outline data items.  The default
     * implementation passes control to the
     * {@link #lookupSeriesOutlineStroke(int)} method. You can override this
     * method if you require different behaviour.
     *
     * @param row    the row (or series) index (zero-based).
     * @param column the column (or category) index (zero-based).
     * @return The stroke (never <code>null</code>).
     */
    @Override
    public Stroke getItemOutlineStroke(int row, int column)
    {
        Number z = dataset.getZ(row, column);

        BasicStroke stroke = DEFAULT_OUTLINE_STROKE;

        if (shouldHighlight(row, column))
        {
            stroke = new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL);
        }

        if (z == null)
        {
            stroke = new BasicStroke(stroke.getLineWidth(), stroke.getEndCap(), stroke.getLineJoin(),
                    stroke.getMiterLimit(), new float[]{5.0f, 5.0f}, 0.0f);
        }

        return stroke;
    }

    /**
     * Returns <code>true</code> if an item label is visible, and
     * <code>false</code> otherwise.
     *
     * @param row    the row index (zero-based).
     * @param column the column index (zero-based).
     * @return A boolean.
     */
    @Override
    public boolean isItemLabelVisible(int row, int column)
    {
        return shouldHighlight(row, column) || super.isItemLabelVisible(row, column);
    }

    /**
     * Returns the paint used to draw an item label.
     *
     * @param row    the row index (zero based).
     * @param column the column index (zero based).
     * @return The paint (never <code>null</code>).
     */
    @Override
    public Paint getItemLabelPaint(int row, int column)
    {
        return super.getItemLabelPaint(row, column);
    }

    /**
     * Returns a shape used to represent a data item.
     * <p/>
     * The default implementation passes control to the getSeriesShape method.
     * You can override this method if you require different behaviour.
     *
     * @param row    the row (or series) index (zero-based).
     * @param column the column (or category) index (zero-based).
     * @return The shape (never <code>null</code>).
     */
    @Override
    public Shape getItemShape(int row, int column)
    {
        Number x = dataset.getX(row, column);
        Number y = dataset.getY(row, column);
        Number z = dataset.getZ(row, column);

        if (x == null || y == null)
        {
            return new Ellipse2D.Double(0, 0, 0, 0);
        }

        if (z == null)
        {
            z = 1.0;
        }

        double dx = x.doubleValue();
        double dy = y.doubleValue();
        double dz = z.doubleValue();

        return new Ellipse2D.Double(dx - dz / 2.0, dy - dz / 2.0, dz, dz);
    }

    /**
     * Sets the plot that the renderer is assigned to.
     *
     * @param plot the plot (<code>null</code> permitted).
     */
    @Override
    public void setPlot(XYPlot plot)
    {
        XYPlot prevPlot = getPlot();
        if(prevPlot != null && plot != prevPlot)
        {
            prevPlot.removeChangeListener(this);
            ValueAxis prevDomainAxis = prevPlot.getDomainAxis();
            ValueAxis prevRangeAxis = prevPlot.getRangeAxis();

            if(prevDomainAxis != null)
                prevDomainAxis.removeChangeListener(this);

            if(prevRangeAxis != null)
                prevRangeAxis.removeChangeListener(this);
        }

        super.setPlot(plot);

        domainAxis = plot.getDomainAxis();
        rangeAxis = plot.getRangeAxis();

        if(domainAxis != null)
        {
            domainAxisLength = domainAxis.getRange().getLength();
            domainAxis.addChangeListener(this);
        }

        if(rangeAxis != null)
        {
            rangeAxisLength = rangeAxis.getRange().getLength();
            rangeAxis.addChangeListener(this);
        }

        plot.addChangeListener(this);
    }

    /**
     * Receives notification of a plot change event.
     *
     * @param event the event.
     */
    public void plotChanged(PlotChangeEvent event)
    {
        XYPlot plot = (XYPlot)event.getPlot();

        if(plot.getDomainAxis() != domainAxis)
        {
            if(domainAxis != null)
                domainAxis.removeChangeListener(this);
            domainAxis = plot.getDomainAxis();
            domainAxisLength = domainAxis.getRange().getLength();
            domainAxis.addChangeListener(this);
        }

        if(plot.getRangeAxis() != rangeAxis)
        {
            if(rangeAxis != null)
                rangeAxis.removeChangeListener(this);
            rangeAxis = plot.getRangeAxis();
            rangeAxisLength = rangeAxis.getRange().getLength();
            rangeAxis.addChangeListener(this);
        }
    }

    /**
     * Receives notification of an axis change event.
     *
     * @param event the event.
     */
    public void axisChanged(AxisChangeEvent event)
    {
        if(event.getAxis() == domainAxis)
        {
            domainZoomMultiplier = domainAxisLength / domainAxis.getRange().getLength();
        }
        else if(event.getAxis() == rangeAxis)
        {
            rangeZoomMultiplier = rangeAxisLength / rangeAxis.getRange().getLength();
        }
    }

    /**
     * Receives notification of an dataset change event.
     *
     * @param event information about the event.
     */
    public void datasetChanged(DatasetChangeEvent event)
    {
        if(domainAxis != null)
        {
            domainZoomMultiplier = 1.0;
            domainAxisLength = domainAxis.getRange().getLength();
        }

        if(rangeAxis != null)
        {
            rangeZoomMultiplier = 1.0;
            rangeAxisLength = rangeAxis.getRange().getLength();
        }
    }
}
