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

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.ui.TextAnchor;

import javax.swing.event.EventListenerList;
import java.awt.*;

/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * User: Khashim
 * Date: Sep 15, 2008
 * Time: 2:58:23 PM
 *
 * @author Jameel
 */
public class MotionChart implements DatasetChangeListener, ChartChangeListener
{
    private MotionDataSet dataset;
    private MotionBubbleRenderer renderer;
    private XYPlot plot;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private static ChartTheme currentTheme = new StandardChartTheme("JFree");

    /** Storage for registered change listeners. */
    private EventListenerList changeListeners;

    /**
     * A flag that can be used to enable/disable notification of chart change
     * events.
     */
    private boolean notify;

    /**
     * Creates a new chart based on the supplied dataset.  The chart will have
     * a default title of "Motion Chart".
     * A default font is used for the title, and the chart will
     * not have a legend.
     *
     * @param dataset the dataset (<code>null</code> not permitted).
     */
    public MotionChart(MotionDataSet dataset)
    {
        this("SOCR Motion Charts", JFreeChart.DEFAULT_TITLE_FONT, dataset, false);
    }

    /**
     * Creates a new chart with the given title and dataset.
     * A default font is used for the title, and the chart will
     * not have a legend added automatically.
     *
     * @param title the chart title (<code>null</code> permitted).
     * @param dataset  the dataset (<code>null</code> not permitted).
     */
    public MotionChart(String title, MotionDataSet dataset)
    {
        this(title, JFreeChart.DEFAULT_TITLE_FONT, dataset, false);
    }

    /**
     * Creates a new chart with the given title and dataset.  The
     * <code>createLegend</code> argument specifies whether or not a legend
     * should be added to the chart.
     *
     * @param title        the chart title (<code>null</code> permitted).
     * @param titleFont    the font for displaying the chart title
     *                     (<code>null</code> permitted).
     * @param dataset       the dataset
     *                     (<code>null</code> not permitted).
     * @param createLegend a flag indicating whether or not a legend should
     *                     be created for the chart.
     */
    public MotionChart(String title, Font titleFont, MotionDataSet dataset, boolean createLegend)
    {
        if (dataset == null) {
            throw new NullPointerException("Null 'dataset' argument.");
        }

        this.dataset = dataset;

        NumberAxis xAxis = new NumberAxis(dataset.getXLabel());
        xAxis.setAutoRangeIncludesZero(false);
        xAxis.setUpperMargin(0.20);
        NumberAxis yAxis = new NumberAxis(dataset.getYLabel());
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setUpperMargin(0.20);

        plot = new XYPlot(dataset, xAxis, yAxis, null);

        renderer = new MotionBubbleRenderer(dataset,
                XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS);
        renderer.setBaseToolTipGenerator(new MotionToolTipGenerator());
        renderer.setBaseItemLabelGenerator(new MotionItemLabelGenerator());
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));


        plot.setRenderer(renderer);
        plot.setOrientation(PlotOrientation.VERTICAL);

        dataset.addChangeListener(this);

        chart = new JFreeChart(title, titleFont, plot, createLegend);
        chart.addChangeListener(this);

        currentTheme.apply(chart);

        chartPanel = new ChartPanel(chart);
        chartPanel.addChartMouseListener(new MotionMouseListener());
    }

    public JFreeChart getChart()
    {
        return chart;
    }

    public ChartPanel getChartPanel()
    {
        return chartPanel;
    }

    public MotionBubbleRenderer getRenderer()
    {
        return renderer;
    }

    public MotionDataSet getDataset()
    {
        return dataset;
    }

    public XYPlot getPlot()
    {
        return plot;
    }

    /**
     * Returns the main chart title.
     *
     * @return The chart title (possibly <code>null</code>).
     *
     * @see #setTitle(TextTitle)
     */
    public TextTitle getTitle() {
        return chart.getTitle();
    }

    /**
     * Sets the main title for the chart and sends a {@link org.jfree.chart.event.ChartChangeEvent}
     * to all registered listeners.  If you do not want a title for the
     * chart, set it to <code>null</code>.
     *
     * @param title  the title (<code>null</code> permitted).
     *
     * @see #getTitle()
     */
    public void setTitle(TextTitle title) {
        chart.setTitle(title);
    }

    /**
     * Sets the chart title and sends a {@link org.jfree.chart.event.ChartChangeEvent} to all
     * registered listeners.  This is a convenience method that ends up calling
     * the {@link #setTitle(TextTitle)} method.  If there is an existing title,
     * its text is updated, otherwise a new title using the default font is
     * added to the chart.  If <code>text</code> is <code>null</code> the chart
     * title is set to <code>null</code>.
     *
     * @param text  the title text (<code>null</code> permitted).
     *
     * @see #getTitle()
     */
    public void setTitle(String text) {
        chart.setTitle(text);
    }

    /**
     * Receives notification of an dataset change event.
     *
     * @param event information about the event.
     */
    public void datasetChanged(DatasetChangeEvent event)
    {
        plot.getDomainAxis().setLabel(dataset.getXLabel());
        plot.getRangeAxis().setLabel(dataset.getYLabel());
    }

    /**
     * Receives notification of a chart change event.
     *
     * @param event the event.
     */
    public void chartChanged(ChartChangeEvent event)
    {
        notifyListeners(event);
    }

    /**
     * Returns a flag that controls whether or not change events are sent to
     * registered listeners.
     *
     * @return A boolean.
     *
     * @see #setNotify(boolean)
     */
    public boolean isNotify() {
        return this.notify;
    }

    /**
     * Sets a flag that controls whether or not listeners receive
     * {@link ChartChangeEvent} notifications.
     *
     * @param notify  a boolean.
     *
     * @see #isNotify()
     */
    public void setNotify(boolean notify) {
        this.notify = notify;
        // if the flag is being set to true, there may be queued up changes...
        if (notify) {
            notifyListeners(new ChartChangeEvent(this));
        }
    }

    /**
     * Registers an object for notification of changes to the chart.
     *
     * @param listener  the listener (<code>null</code> not permitted).
     *
     * @see #removeChangeListener(ChartChangeListener)
     */
    public void addChangeListener(ChartChangeListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Null 'listener' argument.");
        }
        this.changeListeners.add(ChartChangeListener.class, listener);
    }

    /**
     * Deregisters an object for notification of changes to the chart.
     *
     * @param listener  the listener (<code>null</code> not permitted)
     *
     * @see #addChangeListener(ChartChangeListener)
     */
    public void removeChangeListener(ChartChangeListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Null 'listener' argument.");
        }

        this.changeListeners.remove(ChartChangeListener.class, listener);
    }

    /**
     * Sends a default {@link ChartChangeEvent} to all registered listeners.
     * <P>
     * This method is for convenience only.
     */
    public void fireChartChanged() {
        ChartChangeEvent event = new ChartChangeEvent(this);
        notifyListeners(event);
    }

    /**
     * Sends a {@link ChartChangeEvent} to all registered listeners.
     *
     * @param event  information about the event that triggered the
     *               notification.
     */
    protected void notifyListeners(ChartChangeEvent event) {
        if (this.notify) {
            Object[] listeners = this.changeListeners.getListenerList();
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ChartChangeListener.class) {
                    ((ChartChangeListener) listeners[i + 1]).chartChanged(
                            event);
                }
            }
        }
    }

    /**
     * Tests this chart for equality with another object.
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MotionChart)) {
            return false;
        }

        MotionChart that = (MotionChart) obj;
        if (!chart.equals(that.chart)) {
            return false;
        }

        if (this.notify != that.notify) {
            return false;
        }
        return true;
    }

    /**
     * Clones the object, and takes care of listeners.
     * Note: caller shall register its own listeners on cloned graph.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if the chart is not cloneable.
     */
    public Object clone() throws CloneNotSupportedException {
        MotionChart chart = (MotionChart) super.clone();

        chart.chart = (JFreeChart) this.chart.clone();
        chart.plot = (XYPlot) this.plot.clone();
        chart.chartPanel = new ChartPanel(chart.chart);
        chart.dataset = (MotionDataSet)this.dataset.clone();
        chart.renderer = (MotionBubbleRenderer)renderer.clone();
        chart.changeListeners = new EventListenerList();

        return chart;
    }
}
