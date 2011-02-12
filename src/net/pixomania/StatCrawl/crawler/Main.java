package net.pixomania.StatCrawl.crawler;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import net.pixomania.StatCrawl.db.Db;
import net.pixomania.StatCrawl.db.DbSingleton;

/**
 *
 * @author vigge
 */
public class Main extends javax.swing.JFrame {

    public static boolean toggled = false;
    public static DefaultTableModel model;
    
    static {
       System.setProperty("swing.defaultlaf", "org.pushingpixels.substance.api.skin.SubstanceGeminiLookAndFeel");
    }
    /** Creates new form Main */
    public Main() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        urlToolBar = new javax.swing.JToolBar();
        urlLabel = new javax.swing.JLabel();
        urlField = new javax.swing.JTextField();
        crawlToggleButton = new javax.swing.JToggleButton();
        fillerLabel = new javax.swing.JLabel();
        statusBar = new org.jdesktop.swingx.JXStatusBar();
        statusLabel = new javax.swing.JLabel();
        pendingLabel = new javax.swing.JLabel();
        crawledLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jXTable1 = new org.jdesktop.swingx.JXTable();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        fileMenuSeparator1 = new javax.swing.JPopupMenu.Separator();
        closeMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        urlToolBar.setFloatable(false);
        urlToolBar.setRollover(true);

        urlLabel.setText("URL:");
        urlToolBar.add(urlLabel);
        urlToolBar.add(urlField);

        crawlToggleButton.setText("Crawl");
        crawlToggleButton.setFocusable(false);
        crawlToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        crawlToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        crawlToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crawlToggleButtonActionPerformed(evt);
            }
        });
        urlToolBar.add(crawlToggleButton);

        fillerLabel.setText(" ");
        urlToolBar.add(fillerLabel);

        statusLabel.setText("Status");
        statusBar.add(statusLabel);

        pendingLabel.setText("Pending: 0");
        statusBar.add(pendingLabel);

        crawledLabel.setText("Crawled: 0");
        statusBar.add(crawledLabel);

        model = new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Url", "Progress"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, javax.swing.JProgressBar.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        };
        jXTable1.setModel(model);
        jXTable1.setEditable(false);
        jXTable1.setRolloverEnabled(false);
        jXTable1.setRowSelectionAllowed(false);
        jXTable1.setShowGrid(true);
        jXTable1.setSortable(false);
        jXTable1.setSortsOnUpdates(false);
        jScrollPane1.setViewportView(jXTable1);
        jXTable1.getColumn("Progress").setCellRenderer(new ProgRenderer());

        fileMenu.setText("File");

        newMenuItem.setText("New...");
        fileMenu.add(newMenuItem);

        openMenuItem.setText("Open...");
        fileMenu.add(openMenuItem);

        saveMenuItem.setText("Save...");
        fileMenu.add(saveMenuItem);
        fileMenu.add(fileMenuSeparator1);

        closeMenuItem.setText("Close");
        fileMenu.add(closeMenuItem);

        menuBar.add(fileMenu);

        toolsMenu.setText("Tools");
        menuBar.add(toolsMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
            .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
            .addComponent(urlToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(urlToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-491)/2, (screenSize.height-330)/2, 491, 330);
    }// </editor-fold>//GEN-END:initComponents

    private void crawlToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crawlToggleButtonActionPerformed
        // Fetch the button from the event
        JToggleButton btn = (JToggleButton) evt.getSource();

        // Create a new crawler
        Crawler c = new Crawler();

        // If the button is selected we should run, if the button get's untoggled we should cancel
        if(btn.isSelected()){
            if(!urlField.getText().startsWith("http://") &&
               !urlField.getText().startsWith("https://")) {
                JOptionPane.showMessageDialog(rootPane, "Invalid URL\n Either there is no URL, or it doesn't start with http(s)://");
                btn.setSelected(false);
            } else {
                Db db = DbSingleton.getDb();
                db.insertPending(urlField.getText());
                toggled = true;
                c.execute();
            }
        } else {
            c.cancel(true);
            toggled = false;
        }
    }//GEN-LAST:event_crawlToggleButtonActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JToggleButton crawlToggleButton;
    private static javax.swing.JLabel crawledLabel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPopupMenu.Separator fileMenuSeparator1;
    private javax.swing.JLabel fillerLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXTable jXTable1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private static javax.swing.JLabel pendingLabel;
    private javax.swing.JMenuItem saveMenuItem;
    private org.jdesktop.swingx.JXStatusBar statusBar;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JTextField urlField;
    private javax.swing.JLabel urlLabel;
    private javax.swing.JToolBar urlToolBar;
    // End of variables declaration//GEN-END:variables

}
