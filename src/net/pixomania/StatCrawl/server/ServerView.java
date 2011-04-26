/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ServerView.java
 *
 * Created on 2011-apr-08, 11:07:08
 */
package net.pixomania.StatCrawl.server;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

/**
 *
 * @author galaxyAbstractor
 */
public class ServerView extends javax.swing.JFrame {
    private PipedInputStream piOut;
    private PipedOutputStream poOut;
    private DbServer server = new DbServer();
    static {
       System.setProperty("swing.defaultlaf", "org.pushingpixels.substance.api.skin.SubstanceGeminiLookAndFeel");
    }
    
    public static int getPort(){
        try {
            return Integer.parseInt(portField.getText());
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Illegal port number");
            tglServerTgl.setSelected(false);
            
        }
        return -1;
    }
    /** Creates new form ServerView */
    public ServerView() {
       initComponents();
       // Misses characters :S
//       try {
//            // Set up System.out
//            piOut = new PipedInputStream();
//            poOut = new PipedOutputStream(piOut);
//            System.setOut(new PrintStream(poOut, true));
//            new Console(piOut).start();
//        } catch (IOException ex) {
//
//        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        portLbl = new javax.swing.JLabel();
        portField = new javax.swing.JTextField();
        tglServerTgl = new javax.swing.JToggleButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("StatCrawl - Server");

        console.setColumns(20);
        console.setEditable(false);
        console.setRows(5);
        jScrollPane1.setViewportView(console);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        portLbl.setText("Port:");
        jToolBar1.add(portLbl);
        jToolBar1.add(portField);

        tglServerTgl.setText("Toggle Server");
        tglServerTgl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglServerTglActionPerformed(evt);
            }
        });
        jToolBar1.add(tglServerTgl);

        jMenu1.setText("File");

        jMenuItem1.setText("Quit");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem2.setText("Settings");
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Add ID");
        jMenu2.add(jMenuItem3);
        jMenu2.add(jSeparator1);

        jMenuItem4.setText("Switch to statview");
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-761)/2, (screenSize.height-338)/2, 761, 338);
    }// </editor-fold>//GEN-END:initComponents

    private void tglServerTglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglServerTglActionPerformed
        // Fetch the button from the event
        JToggleButton btn = (JToggleButton) evt.getSource();
        
        // If the button is selected we should run, if the button get's untoggled we should cancel
        if(btn.isSelected()){
            server.start();
        } else {
            server.stop();
        }
    }//GEN-LAST:event_tglServerTglActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ServerView().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextArea console;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private static javax.swing.JTextField portField;
    private javax.swing.JLabel portLbl;
    private static javax.swing.JToggleButton tglServerTgl;
    // End of variables declaration//GEN-END:variables
}
