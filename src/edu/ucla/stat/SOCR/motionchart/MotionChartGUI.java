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

import edu.ucla.stat.SOCR.util.BrowserControl;
import edu.ucla.stat.SOCR.util.EnhancedDefaultTableModel;
import edu.ucla.stat.SOCR.util.EnhancedExcelAdapter;
import edu.ucla.stat.SOCR.util.RowHeaderTable;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * User: Khashim
 * Date: Sep 19, 2008
 * Time: 11:28:18 AM
 *
 * @author Jameel
 */
public class MotionChartGUI extends JApplet implements ChangeListener, DatasetChangeListener, ActionListener, PropertyChangeListener
{
    private JMenuBar menuBar;
    private JScrollPane pane;
    private JSplitPane splitPane;
    private JTabbedPane tabbedPanelContainer;
    private JPanel mainPanel;
    private RowHeaderTable tablePanel;

    private MotionChart chart;
    private JPanel chartPanel;
    private EnhancedDefaultTableModel tableModel;
    private MotionTableModel mTableModel;
    private MotionDataSet dataset;
    private MotionBubbleRenderer renderer;
    private PlayerControl control;
    private JTextField keyText;
    private JPanel rightPanel;
    private MotionMappingControl map;
    private JButton resetButton;

    private EnhancedExcelAdapter adapter;

    private int prevSeries = 0;
    private int currSeries = 0;

    private boolean applet;

    private static final Double[][] DEFAULT_DATA = new Double[][]{
            {0.0, 0.1, 0.2, 0.3, 0.4},
            {0.0, 0.5, 0.6, 0.7, 0.8},
            {0.0, 0.9, 0.10, 0.11, 0.12},
            {1.0, 0.2, 0.3, 0.4, 0.5},
            {1.0, 0.6, 0.7, 0.8, 0.9},
            {1.0, 0.10, 0.11, 0.12, 0.13}};

//    private static final Double[][] DEFAULT_DATA = new Double[6][6];
    private static final String[] DEFAULT_COLUMN_NAMES = new String[]
            {"Time", "X", "Y", "Size", "Color", "Category"};

    public MotionChartGUI()
    {
        this(true);
    }

    public MotionChartGUI(boolean appletMode)
    {
        applet = appletMode;
    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                JFrame demo = new JFrame();
                MotionChartGUI chart = new MotionChartGUI();
                chart.init();
                demo.add(chart, BorderLayout.CENTER);
                demo.setSize(800, 600);
                demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                demo.setVisible(true);
            }
        });
    }

    public void init()
    {
        setNativeLookAndFeel();
        //setJavaLookAndFeel();
        setName("Motion Chart");

        initMenuBar();
        setJMenuBar(menuBar);

        pane = new JScrollPane();
        splitPane = new JSplitPane();

        tabbedPanelContainer = new JTabbedPane();
        initMainPanel();
        tabbedPanelContainer.addTab("Chart", mainPanel);
        initTablePanel();
        tabbedPanelContainer.addTab("Data", tablePanel);

        initRightPanel();

        splitPane.setLeftComponent(tabbedPanelContainer);
        splitPane.setRightComponent(rightPanel);
        splitPane.setResizeWeight(0.8);
        splitPane.setOneTouchExpandable(true);
        splitPane.setMinimumSize(new Dimension(400, 300));

        pane.setViewportView(splitPane);
        add(pane);
    }

    private void initMenuBar()
    {
        menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        file.addSeparator();
        JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_X);
        exit.setActionCommand("Exit");
        exit.addActionListener(this);
        file.add(exit);

        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);

        JMenuItem helpItem = new JMenuItem("View Help");
        helpItem.setMnemonic(KeyEvent.VK_H);
        helpItem.setActionCommand("View Help");
        helpItem.addActionListener(this);
        help.add(helpItem);

        help.addSeparator();

        JMenuItem about = new JMenuItem("About SOCR MotionCharts");
        about.setMnemonic(KeyEvent.VK_A);
        about.setActionCommand("About");
        about.addActionListener(this);
        help.add(about);

        if(!applet)
            menuBar.add(file);
        //menuBar.add(Box.createHorizontalGlue());
        menuBar.add(help);
    }

    private void initMainPanel()
    {
        mainPanel = new JPanel();


        tableModel = new EnhancedDefaultTableModel(DEFAULT_DATA, DEFAULT_COLUMN_NAMES);
        mTableModel = new MotionTableModel(tableModel);
        mTableModel.setKeyMapping(0);
        mTableModel.setXAxisMapping(1);
        mTableModel.setYAxisMapping(2);
        mTableModel.setSizeMapping(3);
        mTableModel.setColorMapping(4);
        mTableModel.setCategoryMapping(5);
        dataset = new MotionDataSet(mTableModel);
        dataset.addChangeListener(this);

        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        chart = new MotionChart(dataset);
        renderer = chart.getRenderer();
        renderer.setSeriesVisible(0, true);

        chartPanel = chart.getChartPanel();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 20, 5, 20);
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        mainPanel.add(chartPanel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 80, 5, 20);
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        control = new PlayerControl(dataset.getSeriesCount());
        control.setFramesPerSecond(25.0);
        control.addChangeListener(this);
        mainPanel.add(control, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 80, 5, 20);
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        keyText = new JTextField();
        keyText.setPreferredSize(new Dimension(200, 30));
        keyText.setHorizontalAlignment(JTextField.CENTER);
        keyText.setEditable(false);
        setKeyText();
        mainPanel.add(keyText, c);
    }

    private void initTablePanel()
    {
        tablePanel = new RowHeaderTable(tableModel);
        adapter = new EnhancedExcelAdapter(tablePanel.getDataTable());
    }

    private void initRightPanel()
    {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

        initMap();
        rightPanel.add(map);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        initResetButton();
        rightPanel.add(resetButton);
    }

    private void initMap()
    {
        map = new MotionMappingControl(mTableModel);
        map.addPropertyChangeListener(this);
    }

    private void initResetButton()
    {
        resetButton = new JButton("Reset Data");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setActionCommand("Reset Data");
        resetButton.addActionListener(this);
    }

    public static void setNativeLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            System.out.println("Error setting native Look and Feel: " + e);
        }
    }

    public static void setJavaLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception e)
        {
            System.out.println("Error setting Java Look and Feel: " + e);
        }
    }

    public void setKeyText()
    {
        if (dataset.getSeriesCount() < 1)
        {
            keyText.setEnabled(false);
        }
        else
        {
            keyText.setText(dataset.getSeriesKey(currSeries).toString());
        }
    }

    protected void showAbout()
    {
        String message = "UCLA SOCR Project\n";
        message += "Authors:\n";
        message += "   Ivo Dinov\n";
        message += "   Jameel Al-Aziz\n";
        message += "See http://socr.ucla.edu\n";
        JOptionPane.showMessageDialog(this, message,
                "About SOCR MotionCharts", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();
        if(action.equals("Reset Data"))
        {
            tableModel.setDataVector(DEFAULT_DATA, DEFAULT_COLUMN_NAMES);
        }
        else if(action.equals("View Help"))
        {
            try
            {
                //getAppletContext().showDocument(new URL("http://socr.ucla.edu/"), "_blank");
                BrowserControl.openUrl("http://wiki.stat.ucla.edu/socr/index.php/Help_pages_for_SOCR_Motion_Charts");
            } catch(Exception ex) {}
        }
        else if(action.equals("About"))
        {
            try
            {
                BrowserControl.openUrl("http://wiki.stat.ucla.edu/socr/index.php/About_pages_for_SOCR_Motion_Charts");
            } catch(Exception ex) {}
            //showAbout();
        }
        else if(action.equals("Exit"))
        {
            System.exit(0);
        }
    }

    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e a ChangeEvent object
     */
    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == control)
        {
            if (currSeries >= dataset.getSeriesCount())
            {
                currSeries = 0;
            }

            prevSeries = currSeries;
            currSeries = ((PlayerControl) e.getSource()).getValue();
            renderer.setSeriesVisible(prevSeries, false);
            renderer.setSeriesVisible(currSeries, true);
            setKeyText();
        }
    }

    /**
     * Receives notification of an dataset change event.
     *
     * @param event information about the event.
     */
    public void datasetChanged(DatasetChangeEvent event)
    {
        control.setCount(dataset.getSeriesCount());
        setKeyText();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        String propertyName = evt.getPropertyName();
        String message = "We've detected that the column you're mapping to is a column of 'Strings'.\n";
        message += "If you'd like to have the column data interpreted as dates or time, please enter a parse string.\n";
        message += "See http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html for more information.\n\n";

        if(propertyName.equals("KeyMapping") || propertyName.equals("XAxisMapping")
                || propertyName.equals("YAxisMapping") || propertyName.equals("SizeMapping")
                || propertyName.equals("ColorMapping") || propertyName.equals("CategoryMapping"))
        {
            Integer col = (Integer)evt.getNewValue();
            if(col == null || col == -1)
            {
                return;
            }

            Class colClass = mTableModel.getColumnClass(col);

            if(colClass == String.class)
            {
                String input = JOptionPane.showInputDialog(this, message,
                        "Data Type", JOptionPane.WARNING_MESSAGE);

                if(!(input == null || input.equals("")))
                {
                    mTableModel.setColumnParseString(input, col);
                }
            }
        }
    }

    /**
     * Returns information about this applet. An applet should override
     * this method to return a <code>String</code> containing information
     * about the author, version, and copyright of the applet.
     * <p>
     * The implementation of this method provided by the
     * <code>Applet</code> class returns <code>null</code>.
     *
     * @return  a string containing information about the author, version, and
     *          copyright of the applet.
     */
    @Override
    public String getAppletInfo()
    {
        return "Name: Statistics Online Compute Resource (SOCR)\r\n"
                + "Author: Ivo Dinov, Ph.D.\r\n"
                + "Institution: UCLA Statistics/Neurology\r\n"
                + "Version: 1.0\r\n"
                + "Date: 2000-2004 (06/29/2003)\r\n"
                + "URL: http://www.stat.ucla.edu/%7Edinov/courses_students.dir/Applets.dir/OnlineResources.html";
    }
}
