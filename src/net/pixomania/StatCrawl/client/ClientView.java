package net.pixomania.StatCrawl.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serialize.CollectionSerializer;
import com.esotericsoftware.kryo.serialize.EnumSerializer;
import com.esotericsoftware.kryo.serialize.FieldSerializer;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import net.pixomania.StatCrawl.crawler.Crawler;
import net.pixomania.StatCrawl.crawler.ProgRenderer;
import net.pixomania.StatCrawl.networking.Operation;
import net.pixomania.StatCrawl.networking.Packet;
import net.pixomania.StatCrawl.networking.QueueItem;

/**
 *
 * @author galaxyAbstractor
 */
public class ClientView extends javax.swing.JFrame {

    private Client client;
    private Crawler crawler = new Crawler();
    private static DefaultTableModel model;

    static {
        System.setProperty("swing.defaultlaf", "org.pushingpixels.substance.api.skin.SubstanceGeminiLookAndFeel");
    }

    /** Creates new form ClientView */
    public ClientView() {
        initComponents();
    }

    /**
     * Return the model that we use in the table
     * @return DefaultTableModel
     */
    public static DefaultTableModel getModel() {
        return model;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        hostLbl = new javax.swing.JLabel();
        hostField = new javax.swing.JTextField();
        portLbl = new javax.swing.JLabel();
        portField = new javax.swing.JTextField();
        urlLbl = new javax.swing.JLabel();
        urlField = new javax.swing.JTextField();
        connectTgl = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jXTable1 = new org.jdesktop.swingx.JXTable();
        statusBar = new org.jdesktop.swingx.JXStatusBar();
        statusLabel = new javax.swing.JLabel();
        pendingLabel = new javax.swing.JLabel();
        crawledLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("StatCrawl Client");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        hostLbl.setText("Host:");
        jToolBar1.add(hostLbl);
        jToolBar1.add(hostField);

        portLbl.setText("Port:");
        jToolBar1.add(portLbl);
        jToolBar1.add(portField);

        urlLbl.setText("URL:");
        jToolBar1.add(urlLbl);
        jToolBar1.add(urlField);

        connectTgl.setText("Connect");
        connectTgl.setFocusable(false);
        connectTgl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        connectTgl.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        connectTgl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectTglActionPerformed(evt);
            }
        });
        jToolBar1.add(connectTgl);

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
        jXTable1.setSortable(false);
        jXTable1.setSortsOnUpdates(false);
        jScrollPane1.setViewportView(jXTable1);
        jXTable1.getColumn("Progress").setCellRenderer(new ProgRenderer());

        statusLabel.setText("Status");
        statusBar.add(statusLabel);

        pendingLabel.setText("Pending: 0");
        statusBar.add(pendingLabel);

        crawledLabel.setText("Crawled: 0");
        statusBar.add(crawledLabel);

        jMenu1.setText("File");

        jMenuItem1.setText("Quit");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
            .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-857)/2, (screenSize.height-406)/2, 857, 406);
    }// </editor-fold>//GEN-END:initComponents

    private void connectTglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectTglActionPerformed
        // Fetch the button from the event
        JToggleButton btn = (JToggleButton) evt.getSource();

        // If the button is selected we should run, if the button get's untoggled we should cancel
        if (btn.isSelected()) {
            
            client = ClientSingleton.getClient();

            Kryo kryo = client.getKryo();

            // Register the classes we are sending
            kryo.register(Packet.class, new FieldSerializer(kryo, Packet.class));
            kryo.register(Type.class, new EnumSerializer(Type.class));
            kryo.register(Object.class, new FieldSerializer(kryo, Object.class));
            kryo.register(Collection.class, new CollectionSerializer(kryo));
            kryo.register(QueueItem.class, new FieldSerializer(kryo, QueueItem.class));
            kryo.register(ArrayList.class, new CollectionSerializer(kryo));
            kryo.register(Operation.class, new EnumSerializer(Operation.class));

            client.start();
            client.setName("hej");

            try {

                client.connect(5000, hostField.getText(), Integer.parseInt(portField.getText()), Integer.parseInt(portField.getText()));
                client.addListener(new Listener() {

                    @Override
                    public void received(Connection connection, Object object) {
                        if (object instanceof Packet) {
                            // Get our packet
                            Packet response = (Packet) object;

                            // Check the type of the packet. Different types are defined in the Type enum
                            switch(response.type){
                                case TOCRAWL:
                                    // We got a packet with links to crawl. Lets crawl them!
                                    crawler.setPending((LinkedList<String>) response.data);
                                    break;
                            }
                        }
                    }
                });
                crawler.resetCrawler();
                crawler.start();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(rootPane, "illegal port");
                connectTgl.setSelected(false);
            } catch (IOException e) {
                System.out.println("IOException");
            }

        } else {
            crawler.stopCrawler();
            client.stop();

        }
    }//GEN-LAST:event_connectTglActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ClientView().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton connectTgl;
    private static javax.swing.JLabel crawledLabel;
    private javax.swing.JTextField hostField;
    private javax.swing.JLabel hostLbl;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private org.jdesktop.swingx.JXTable jXTable1;
    private static javax.swing.JLabel pendingLabel;
    private javax.swing.JTextField portField;
    private javax.swing.JLabel portLbl;
    private org.jdesktop.swingx.JXStatusBar statusBar;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField urlField;
    private javax.swing.JLabel urlLbl;
    // End of variables declaration//GEN-END:variables
}
