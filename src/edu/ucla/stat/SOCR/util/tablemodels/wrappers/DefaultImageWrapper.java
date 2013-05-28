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

package edu.ucla.stat.SOCR.util.tablemodels.wrappers;

import javax.swing.*;

/**
 * <code>DefaultImageWrapper</code>  class is used by {@link edu.ucla.stat.SOCR.util.tablemodels.renderers.DefaultImageCellRenderer}
 * as a container for image and its properties.
 */
public class DefaultImageWrapper {
    private ImageIcon image;
    private boolean keepAspectRatio;
    private boolean fitCellRect;
    private String pathToImage;

    /**
     * Constructs a <code>DefaultImageWrapper</code> with the specified image, path to image, fitCellRect
     * and keepAspectRatio properties.
     *
     * @param image
     * @param pathToImage
     * @param fitCellRect
     * @param keepAspectRatio
     */
    public DefaultImageWrapper(ImageIcon image, String pathToImage, boolean fitCellRect, boolean keepAspectRatio) {
        this.image = image;
        this.keepAspectRatio = keepAspectRatio;
        this.fitCellRect = fitCellRect;
        this.pathToImage = pathToImage;
    }

    /**
     * Returns image that holds by this wrapper.
     *
     * @return image that holds by this wrapper.
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     * @return <code>true</code> if image keep aspect ratio,  <code>false</code> otherwise.
     */
    public boolean isKeepAspectRatio() {
        return keepAspectRatio;
    }

    /**
     * Indicates whether the image should fit the cell.
     *
     * @return <code>true</code> if image fit the cell,  <code>false</code> otherwise.
     */
    public boolean isFitCellRect() {
        return fitCellRect;
    }

    /**
     * Returns path to image that wrapper keeps.
     *
     * @return path to image that wrapper keeps.
     */
    public String getPathToImage() {
        return pathToImage;
    }
}
