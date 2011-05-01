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

import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.pixomania.StatCrawl.db.Db;
import net.pixomania.StatCrawl.db.DbSingleton;

/**
 * The view that shows the stats
 * @author galaxyAbstractor
 */
public class StatView extends javax.swing.JFrame {
    static {
      System.setProperty("swing.defaultlaf", "org.pushingpixels.substance.api.skin.SubstanceGeminiLookAndFeel");
    }
    private Db db = DbSingleton.getDb();
    /** Creates new form Main */
    public StatView() {
        initComponents();
        
        // Here we set the stats
        
        // Get the IP stats for the labels and set them
        int[] ip = db.getIPTotalCount();
        totalIPCrawlsLbl.setText(ip[0]+"");
        totalIPCountLbl.setText(ip[1]+"");
        
        // Get the Host stats for the labels and set them
        int[] host = db.getHostTotalCount();
        totalHostCrawlsLbl.setText(host[0]+"");
        totalHostCountLbl.setText(host[1]+"");
        
        // Here we create a model for the IP table
        Object[][] topIPs = db.getTopIPs();
        TableModel topIpModel = new DefaultTableModel(topIPs, new String[]{"IP","Count"});
        topIPTable.setModel(topIpModel);
        
        // To be able to sort the numbers correctly, we have to make a new RowSorter
        // and a comparator to go with it, to sort ints
        TableRowSorter<TableModel> ipSorter = new TableRowSorter<TableModel>(topIPTable.getModel());
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer s1, Integer s2) {
                return s1.compareTo(s2);
            }
        };
        // Set the comparator on the count column which contains the ints
        ipSorter.setComparator(1, comparator);
        topIPTable.setRowSorter(ipSorter);
        
        // Now create the model for the hosts
        Object[][] topHosts = db.getTopHosts();
        TableModel topHostModel = new DefaultTableModel(topHosts, new String[]{"Host","Count"});
        topHostTable.setModel(topHostModel);
        
        // create an identical sorter for this model as for the IP model
        TableRowSorter<TableModel> hostSorter = new TableRowSorter<TableModel>(topHostTable.getModel());
        
        // Still, column 1/count is the one containing the ints
        hostSorter.setComparator(1, comparator);
        topHostTable.setRowSorter(hostSorter);
        
        // Now get the image stats and set them on the labels associated
        Object[] images = db.getImageTotalCount();
        totalImagesLbl.setText(images[0]+"");
        imageTotalTypesLbl.setText(images[1]+"");
        totalSizeLbl.setText(images[2]+"");
        averageSizeLbl.setText(images[3]+"");
        
        // Create its model
        Object[][] topImages = db.getTopImageTypes();
        TableModel topImagesModel = new DefaultTableModel(topImages, new String[]{"Type","Size(MB)","Average Size(KB)", "Count"});
        topImageTable.setModel(topImagesModel);
        // We need to make a new TableRowSorter as we have Doubles now, not Integers
        TableRowSorter<TableModel> imageSorter = new TableRowSorter<TableModel>(topImageTable.getModel());
        Comparator<Double> doubleComparator = new Comparator<Double>() {
            @Override
            public int compare(Double s1, Double s2) {
                return s1.compareTo(s2);
            }
        };
        // Set the doubleComparator for column 1 and 2, which contains the doubles,
        // and the integer comparator for the third column, count
        imageSorter.setComparator(1, doubleComparator);
        imageSorter.setComparator(2, doubleComparator);
        imageSorter.setComparator(3, comparator);
        topImageTable.setRowSorter(imageSorter);
        
        // Fetch the error stats and associate them with the correct labels
        int[] errors = db.getErrorTotalCount();
        totalErrorTypesLbl.setText(errors[1]+"");
        totalErrorsLbl.setText(errors[0]+"");
        
        // Create its model
        Object[][] topErrors = db.getTopErrors();
        TableModel topErrorModel = new DefaultTableModel(topErrors, new String[]{"Error","Count"});
        topErrorsTable.setModel(topErrorModel);
        TableRowSorter<TableModel> errorSorter = new TableRowSorter<TableModel>(topErrorsTable.getModel());
        // Column 1, count, is the column that needs sorting abillities
        errorSorter.setComparator(1, comparator);
        topErrorsTable.setRowSorter(errorSorter);
        
        // Set the crawler stats
        int[] crawlerStats = db.getCrawlerStats();
        sitesCrawledLbl.setText(crawlerStats[0]+"");
        pendingLbl.setText(crawlerStats[1]+"");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ArrayList<Location> countries = db.getCountryStats();

        LocationMutableTreeTableNode root = new LocationMutableTreeTableNode(countries, true);

        for(int i = 0; i < countries.size();i++){
            ArrayList<Location> regions = countries.get(i).getList();

            LocationMutableTreeTableNode country = new LocationMutableTreeTableNode(countries.get(i), true);
            for(int n = 0; n < regions.size();n++){
                ArrayList<Location> cities = regions.get(n).getList();

                LocationMutableTreeTableNode region = new LocationMutableTreeTableNode(regions.get(n), true);
                for(int x = 0; x < cities.size();x++){

                    LocationMutableTreeTableNode city = new LocationMutableTreeTableNode(cities.get(x), false);
                    region.add(city);
                }
                country.add(region);
            }
            root.add(country);
        }
        jXTreeTable1 = new org.jdesktop.swingx.JXTreeTable(new LocationTreeTableModel(root));
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        totalIPCountLbl = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        topIPTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        totalIPCrawlsLbl = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        totalHostCountLbl = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        topHostTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        totalHostCrawlsLbl = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        imageTotalTypesLbl = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        topImageTable = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        totalImagesLbl = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        totalSizeLbl = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        averageSizeLbl = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        totalErrorTypesLbl = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        topErrorsTable = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        totalErrorsLbl = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        sitesCrawledLbl = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        pendingLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("StatCrawl Statistics");

        jScrollPane1.setViewportView(jXTreeTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Country", jPanel1);

        jPanel2.setMinimumSize(new java.awt.Dimension(602, 521));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("IP-addreses"));
        jPanel3.setMinimumSize(new java.awt.Dimension(301, 476));
        jPanel3.setPreferredSize(new java.awt.Dimension(331, 476));

        jLabel1.setText("Total IPs:");

        totalIPCountLbl.setText("0");

        jLabel3.setText("Top IPs:");

        topIPTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IP", "Count"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(topIPTable);

        jLabel2.setText("Total crawls:");

        totalIPCrawlsLbl.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(231, 231, 231)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(totalIPCountLbl)
                    .addComponent(totalIPCrawlsLbl))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(totalIPCountLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(totalIPCrawlsLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        jPanel2.add(jPanel3, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Hosts"));
        jPanel4.setMinimumSize(new java.awt.Dimension(301, 501));
        jPanel4.setPreferredSize(new java.awt.Dimension(331, 501));

        jLabel4.setText("Total Hosts:");

        totalHostCountLbl.setText("0");

        jLabel6.setText("Top Hosts:");

        topHostTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Host", "Count"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(topHostTable);

        jLabel7.setText("Total crawls:");

        totalHostCrawlsLbl.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 219, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalHostCrawlsLbl)
                    .addComponent(totalHostCountLbl))
                .addGap(33, 33, 33))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(totalHostCountLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(totalHostCrawlsLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        jPanel2.add(jPanel4, gridBagConstraints);

        jTabbedPane1.addTab("IP/Host", jPanel2);

        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Images"));
        jPanel6.setMinimumSize(new java.awt.Dimension(301, 476));
        jPanel6.setPreferredSize(new java.awt.Dimension(331, 476));

        jLabel5.setText("Total Types:");

        imageTotalTypesLbl.setText("0");

        jLabel8.setText("Top Types:");

        topImageTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Type", "Size (MB)", "Average Size (MB)", "Count"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(topImageTable);

        jLabel9.setText("Total Images:");

        totalImagesLbl.setText("0");

        jLabel13.setText("Total Size:");

        totalSizeLbl.setText("0");

        jLabel15.setText("Average Size:");

        averageSizeLbl.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 236, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(imageTotalTypesLbl)
                    .addComponent(totalImagesLbl)
                    .addComponent(totalSizeLbl)
                    .addComponent(averageSizeLbl))
                .addContainerGap())
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(imageTotalTypesLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalImagesLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalSizeLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(averageSizeLbl)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.add(jPanel6);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Errors"));
        jPanel7.setMinimumSize(new java.awt.Dimension(301, 501));
        jPanel7.setPreferredSize(new java.awt.Dimension(331, 501));

        jLabel10.setText("Total Error Types:");

        totalErrorTypesLbl.setText("0");

        jLabel11.setText("Top Errors:");

        topErrorsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Error", "Count"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(topErrorsTable);

        jLabel12.setText("Total Errors:");

        totalErrorsLbl.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 193, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalErrorsLbl)
                    .addComponent(totalErrorTypesLbl))
                .addGap(33, 33, 33))
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(totalErrorTypesLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(totalErrorsLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.add(jPanel7);

        jTabbedPane1.addTab("Pages", jPanel5);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Crawler"));

        jLabel14.setText("Sites crawled:");

        sitesCrawledLbl.setText("0");

        jLabel17.setText("Pending:");

        pendingLbl.setText("0");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                        .addComponent(sitesCrawledLbl))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                        .addComponent(pendingLbl)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(sitesCrawledLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(pendingLbl))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(423, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(442, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Crawler", jPanel8);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 538, Short.MAX_VALUE)
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-683)/2, (screenSize.height-576)/2, 683, 576);
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StatView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel averageSizeLbl;
    private javax.swing.JLabel imageTotalTypesLbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private org.jdesktop.swingx.JXTreeTable jXTreeTable1;
    private javax.swing.JLabel pendingLbl;
    private javax.swing.JLabel sitesCrawledLbl;
    private javax.swing.JTable topErrorsTable;
    private javax.swing.JTable topHostTable;
    private javax.swing.JTable topIPTable;
    private javax.swing.JTable topImageTable;
    private javax.swing.JLabel totalErrorTypesLbl;
    private javax.swing.JLabel totalErrorsLbl;
    private javax.swing.JLabel totalHostCountLbl;
    private javax.swing.JLabel totalHostCrawlsLbl;
    private javax.swing.JLabel totalIPCountLbl;
    private javax.swing.JLabel totalIPCrawlsLbl;
    private javax.swing.JLabel totalImagesLbl;
    private javax.swing.JLabel totalSizeLbl;
    // End of variables declaration//GEN-END:variables

}
