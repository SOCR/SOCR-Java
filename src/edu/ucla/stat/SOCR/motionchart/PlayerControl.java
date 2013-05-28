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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import java.io.Serializable;

/**
 * Package: edu.ucla.stat.SOCR.motionchart
 * User: Khashim
 * Date: Sep 26, 2008
 * Time: 11:36:54 PM
 *
 * @author Jameel
 */
public class PlayerControl extends JPanel
{
    Class cl = this.getClass();
    private ImageIcon playIcon = new ImageIcon(cl.getResource("resources/start.png"));
    private ImageIcon pauseIcon = new ImageIcon(cl.getResource("resources/pause.png"));
    private ImageIcon backIcon = new ImageIcon(cl.getResource("resources/seek-backward.png"));
    private ImageIcon forwardIcon = new ImageIcon(cl.getResource("resources/seek-forward.png"));

    protected JSlider progressBar;
    protected JButton playButton;
    protected JButton skipRightButton;
    protected JButton skipLeftButton;
    protected JButton pressedButton;

    protected static final double DEFAULT_FPS = 20.0;
    protected double fps = DEFAULT_FPS;
    protected double effFPS = DEFAULT_FPS;
    protected Timer timer;

    protected int count;

    protected PlayerEventListener eventListener = createPlayerEventListener();

    private boolean skip = false;
    private int state = 0;
    private ImageIcon[] playStates = new ImageIcon[] {playIcon, pauseIcon};

    /**
     * The changeListener (no suffix) is the listener we add to the
     * slider.  By default this listener just forwards events
     * to ChangeListeners (if any) added directly to the PlayerControl.
     *
     * @see #addChangeListener
     * @see #createChangeListener
     */
    protected ChangeListener changeListener = createChangeListener();

    /**
     * Only one <code>ChangeEvent</code> is needed per PlayerControl instance since the
     * event's only (read-only) state is the source property.  The source
     * of events generated here is always "this". The event is lazily
     * created the first time that an event notification is fired.
     *
     * @see #fireStateChanged
     */
    protected transient ChangeEvent changeEvent = null;

    public PlayerControl()
    {
        this(100);
    }

    public PlayerControl(int numValues)
    {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        playButton = new JButton(playIcon);
        playButton.setPreferredSize(new Dimension(32,28));
        playButton.addActionListener(eventListener);
        playButton.setActionCommand("playButton");
        playButton.addMouseListener(eventListener);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        add(playButton, c);

        progressBar = new JSlider(JSlider.HORIZONTAL);
        progressBar.setValue(0);
        progressBar.addChangeListener(changeListener);
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        add(progressBar, c);

        skipLeftButton = new JButton(backIcon);
        skipLeftButton.setPreferredSize(new Dimension(32,28));
        skipLeftButton.addMouseListener(eventListener);
        c.gridx = 2;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        add(skipLeftButton, c);

        skipRightButton = new JButton(forwardIcon);
        skipRightButton.setPreferredSize(new Dimension(32,28));
        skipRightButton.addMouseListener(eventListener);
        c.gridx = 3;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        add(skipRightButton, c);

        setCount(numValues);
    }

    public void setValue(int value)
    {
        progressBar.setValue(value);
    }

    public int getValue()
    {
        return progressBar.getValue();
    }

    public void setCount(int count)
    {
        this.count = count;

        setEffectiveFramesPerSecond(this.fps);

        progressBar.setMaximum(count - 1);

        setEnabled(true);

        if(count <= 1)
        {
            setEnabled(false);
        }
    }

    public int getCount()
    {
        return count;
    }

    protected void setTimerInterval()
    {
        int interval = (int)(1.0 / effFPS * 1000);
        timer = new Timer(interval, eventListener);
    }

    protected void setEffectiveFramesPerSecond(double fps)
    {
        effFPS = fps;

        if(this.count < 100)
        {
            effFPS = (fps * count / 100.0);
        }

        setTimerInterval();
    }

    public void setFramesPerSecond(double fps)
    {
        this.fps = (fps <= 0.05) ? 0.05 : fps;
        setEffectiveFramesPerSecond(this.fps);
    }

    public double getFramesPerSecond()
    {
        return fps;
    }

    /**
     * Sets whether or not this component is enabled.
     * A component that is enabled may respond to user input,
     * while a component that is not enabled cannot respond to
     * user input.  Some components may alter their visual
     * representation when they are disabled in order to
     * provide feedback to the user that they cannot take input.
     * <p>Note: Disabling a component does not disable it's children.
     * <p/>
     * <p>Note: Disabling a lightweight component does not prevent it from
     * receiving MouseEvents.
     *
     * @param enabled true if this component should be enabled, false otherwise
     * @beaninfo preferred: true
     * bound: true
     * attribute: visualUpdate true
     * description: The enabled state of the component.
     * @see java.awt.Component#isEnabled
     * @see java.awt.Component#isLightweight
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        progressBar.setEnabled(enabled);
        playButton.setEnabled(enabled);
        skipRightButton.setEnabled(enabled);
        skipLeftButton.setEnabled(enabled);

        super.setEnabled(enabled);
    }

    protected PlayerEventListener createPlayerEventListener() {
        return new PlayerEventListener();
    }

    /**
     * Subclasses that want to handle model ChangeEvents differently
     * can override this method to return their own ChangeListener
     * implementation.  The default ChangeListener just forwards
     * ChangeEvents to the ChangeListeners added directly to the PlayerControl.
     *
     * @see #fireStateChanged
     */
    protected ChangeListener createChangeListener() {
        return new SliderChangeListener();
    }

    /**
     * Adds a ChangeListener to the player control.
     *
     * @param l the ChangeListener to add
     * @see #fireStateChanged
     * @see #removeChangeListener
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener from the player control.
     *
     * @param l the ChangeListener to remove
     * @see #fireStateChanged
     * @see #addChangeListener

     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * Returns an array of all the <code>ChangeListener</code>s added
     * to this PlayerControl with addChangeListener().
     *
     * @return all of the <code>ChangeListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[])listenerList.getListeners(
                ChangeListener.class);
    }

    /**
     * Send a ChangeEvent, whose source is this PlayerControl, to
     * each listener.  This method method is called each time
     * a ChangeEvent is received from the model.
     *
     * @see #addChangeListener
     * @see javax.swing.event.EventListenerList
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }

    private void changeState()
    {
        state = (state + 1) % 2;
        playButton.setIcon(playStates[state]);
        fireStateChanged();
    }

    private void updateProgressBar()
    {
        int curr = progressBar.getValue();

        if(pressedButton == skipLeftButton)
        {
            int min = progressBar.getMinimum();

            if(min < curr)
            {
                progressBar.setValue(--curr);
            }
        }
        else if (pressedButton == skipRightButton || pressedButton == playButton)
        {
            int max = progressBar.getMaximum();

            if(curr < max)
            {
                progressBar.setValue(++curr);
            }
            else if (pressedButton == playButton)
            {
                timer.stop();
                changeState();
            }
        }
    }

    protected class PlayerEventListener implements ActionListener, MouseListener
    {
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();

            if(e.getSource() == timer)
            {
                updateProgressBar();
                return;
            }

            if(action != null) {
                if(action.equals("playButton"))
                {
                    changeState();
                    pressedButton = playButton;

                    switch(state)
                    {
                        case 0:
                            timer.stop();
                            break;
                        case 1:
                            timer.setInitialDelay(timer.getDelay());
                            timer.start();
                            break;
                    }
                }
            }
        }

        public void mouseClicked(MouseEvent e)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void mousePressed(MouseEvent e)
        {
            timer.setInitialDelay(200);
            if(e.getSource() == skipLeftButton)
            {
                skip = true;
                pressedButton = skipLeftButton;
                updateProgressBar();
                timer.start();
            }
            else if (e.getSource() == skipRightButton)
            {
                skip = true;
                pressedButton = skipRightButton;
                updateProgressBar();
                timer.start();
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            if(skip == true)
            {
                skip = false;
                timer.stop();
            }
        }

        public void mouseEntered(MouseEvent e)
        {
            if(e.getSource() == playButton)
            {
                //MouseOver
            }
        }

        public void mouseExited(MouseEvent e)
        {
            if(e.getSource() == playButton)
            {
                //playButton.setIcon(playStates[state]);
            }
        }
    }

    /**
     * We pass Change events along to the listeners with the
     * the PlayerControl (instead of the slider) as the event source.
     */
    private class SliderChangeListener implements ChangeListener, Serializable
    {
        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    }
}
