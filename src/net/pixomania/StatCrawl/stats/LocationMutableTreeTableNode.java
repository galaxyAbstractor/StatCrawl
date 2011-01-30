/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.pixomania.StatCrawl.stats;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

/**
 *
 * @author galaxyAbstractor
 */
public class LocationMutableTreeTableNode extends DefaultMutableTreeTableNode {
    
    public LocationMutableTreeTableNode() {
        super();
    }
    
    public LocationMutableTreeTableNode(Object userObject) {
        super(userObject);
    }
    
    public LocationMutableTreeTableNode(Object userObject, boolean allowChildren) {
        super(userObject, allowChildren);
    }
    
    @Override
    public int getColumnCount() {
        return 2;
    }
    
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
    
    @Override
    public boolean isEditable(int column) {
        return false;
    }

}
