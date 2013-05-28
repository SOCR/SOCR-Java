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

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * User: Khashim
 * Date: Dec 26, 2008
 * Time: 6:36:24 PM
 *
 * @author Jameel
 */
public class MotionMappingControl extends JPanel implements ActionListener, TableModelListener
{
    protected MotionTableModel model;
    protected JLabel[] labels;
    protected JComboBox[] comboBoxes;

    protected String title = "Mappings";
    protected String[] labelTitles = new String[] {"Key:", "X-Axis:", "Y-Axis:", "Size:", "Color:", "Category:"};
    protected String[] columns;
    protected int count = 6;
    protected GridBagConstraints constraints;

    public MotionMappingControl(MotionTableModel model)
    {
        setName("Mappings");
        setTableModel(model);
        setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(title),
                                BorderFactory.createEmptyBorder(5,5,5,5)));
        setLayout(new GridBagLayout());

        setConstraints();

        update();
    }

    protected void createLabels()
    {
        labels = new JLabel[count];

        for(int i = 0; i < count; i++)
        {
            labels[i] = new JLabel(labelTitles[i]);
        }
    }

    protected void createComboBoxes()
    {
        comboBoxes = new JComboBox[count];
        setColumnNames();

        for(int i = 0; i < count; i++)
        {
            comboBoxes[i] = new JComboBox(columns);
            comboBoxes[i].addActionListener(this);
        }
    }

    protected void setConstraints()
    {
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
    }

    protected void addComponents()
    {
        for(int i = 0; i < count; i++)
        {
            constraints.insets = new Insets(0,0,0,0);
            add(labels[i], constraints);
            constraints.insets = new Insets(0,0,0,0);
            add(comboBoxes[i], constraints);
        }
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(Box.createRigidArea(new Dimension(0,5)), c);
    }

    protected void update()
    {
        removeAll();
        createLabels();
        createComboBoxes();
        addComponents();
        validate();
    }

    protected void setLabelTitles(String[] titles)
    {
        this.labelTitles = titles;
        count = titles.length;
        update();
    }

    protected String[] getLabelTitles()
    {
        return labelTitles;
    }

    protected void setColumnNames()
    {
        int count = model.getColumnCount();
        columns = new String[count+1];
        columns[0] = "";

        for(int c = 1; c <= count; c++)
        {
            columns[c] = model.getColumnName(c-1);
        }
    }

    public void setTitle(String title)
    {
        this.title = title;
        setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(title),
                                BorderFactory.createEmptyBorder(5,5,5,5)));
        validate();
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTableModel(MotionTableModel model)
    {
        this.model = model;
        model.addTableModelListener(this);
    }

    public MotionTableModel getTableModel()
    {
        return this.model;
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        if(!(e.getSource() instanceof JComboBox))
            return;

        JComboBox source = (JComboBox)e.getSource();
        int index = source.getSelectedIndex() - 1;

        if(source == comboBoxes[0])
        {
            Integer oldValue = model.getKeyMapping();
            model.setKeyMapping(index);
            firePropertyChange("KeyMapping", oldValue, new Integer(index));
        }
        else if(source == comboBoxes[1])
        {
            Integer oldValue = model.getXAxisMapping();
            model.setXAxisMapping(index);
            firePropertyChange("XAxisMapping", oldValue, new Integer(index));
        }
        else if(source == comboBoxes[2])
        {
            Integer oldValue = model.getYAxisMapping();
            model.setYAxisMapping(index);
            firePropertyChange("YAxisMapping", oldValue, new Integer(index));
        }
        else if(source == comboBoxes[3])
        {
            Integer oldValue = model.getSizeMapping();
            model.setSizeMapping(index);
            firePropertyChange("SizeMapping", oldValue, new Integer(index));
        }
        else if(source == comboBoxes[4])
        {
            Integer oldValue = model.getColorMapping();
            model.setColorMapping(index);
            firePropertyChange("ColorMapping", oldValue, new Integer(index));
        }
        else if(source == comboBoxes[5])
        {
            Integer oldValue = model.getCategoryMapping();
            model.setCategoryMapping(index);
            firePropertyChange("CategoryMapping", oldValue, new Integer(index));
        }
    }

    /**
     * This fine grain notification tells listeners the exact range
     * of cells, rows, or columns that changed.
     */
    public void tableChanged(TableModelEvent e)
    {
        if(e.getFirstRow() != TableModelEvent.HEADER_ROW)
            return;

        update();
    }
}
