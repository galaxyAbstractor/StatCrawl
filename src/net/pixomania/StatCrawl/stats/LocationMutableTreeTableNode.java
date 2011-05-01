/*******************************************************************
* StatCrawl
*
* Copyright (c) 2011, http://pixomania.net
*
* Licensed under the BSD License
* Redistributions of files must retain the above copyright notice.
*
* Please see LICENSE.txt for more info.
*
* @copyright Copyright 2011, pixomania, http://pixomania.net
* @license BSD license (http://www.opensource.org/licenses/bsd-license.php)
********************************************************************/
package net.pixomania.StatCrawl.stats;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

/**
 * This class handles the different nodes in the TreeTable in SwingX. It's based
 * upon the DefaultMutableTreeTableNode
 * @author galaxyAbstractor
 */
public class LocationMutableTreeTableNode extends DefaultMutableTreeTableNode {
    
    public LocationMutableTreeTableNode() {
        super();
    }
    
    /**
     * Create a new node
     * @param userObject the node to create
     */
    public LocationMutableTreeTableNode(Object userObject) {
        super(userObject);
    }
    
    public LocationMutableTreeTableNode(Object userObject, boolean allowChildren) {
        super(userObject, allowChildren);
    }
    
    /**
     * Get the number of columns
     * @return 2 (we only need 2 in the location, one for name and one for count)
     */
    @Override
    public int getColumnCount() {
        return 2;
    }
    
    /**
     * Get the value
     * @param column The column of the data
     * @return a value (Name or Count)
     */
    @Override
    public Object getValueAt(int column) {
        Location l = (Location) getUserObject();
        
        switch(column){
            case 0:
                return l.name;
            case 1:
                return l.count;
            default: 
                assert false;
        }
        return null;
    }
    
    /**
     * Disable editing
     * @param column the column index 
     * @return false
     */
    @Override
    public boolean isEditable(int column) {
        return false;
    }

}
