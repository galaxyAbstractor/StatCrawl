/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.pixomania.StatCrawl.server;

import java.io.IOException;
import java.io.PipedInputStream;
import javax.swing.SwingUtilities;

/**
 *
 * @author galaxyAbstractor
 */
class Console extends Thread {
    PipedInputStream pi;

    public Console(PipedInputStream pi) {
        this.pi = pi;
    }

    public void run() {
        final byte[] buf = new byte[1024];
        try {
            while (true) {
                final int len = pi.read(buf);
                if (len == -1) {
                    break;
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ServerView.console.append(new String(buf, 0, len));

                        // Make sure the last line is always visible
                        ServerView.console.setCaretPosition(ServerView.console.getDocument().getLength());

                        // Keep the text area down to a certain character size
                        int idealSize = 1000;
                        int maxExcess = 500;
                        int excess = ServerView.console.getDocument().getLength() - idealSize;
                        if (excess >= maxExcess) {
                            ServerView.console.replaceRange("", 0, excess);
                        }
                    }
                });
            }
        } catch (IOException e) {
        }
    }
}