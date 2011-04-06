/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.pixomania.StatCrawl.crawler;

import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author galaxyAbstractor
 */
public class ProgRenderer extends JProgressBar implements TableCellRenderer {
    
    /**
     * Custom cell renderer for the table cell that holds the progressbar
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return a progressbar
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JProgressBar j = (JProgressBar) value;
        j.setStringPainted(true);
        return j;
    }
    
   

}
