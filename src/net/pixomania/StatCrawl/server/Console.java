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
package net.pixomania.StatCrawl.server;

import java.io.IOException;
import java.io.PipedInputStream;
import javax.swing.SwingUtilities;

/**
 *  This class takes the data printed to System.out.println and forwards it to a JTextArea
 * @author galaxyAbstractor
 */
class Console extends Thread {
    PipedInputStream pi;

    public Console(PipedInputStream pi) {
        this.pi = pi;
    }

    @Override
    public void run() {
        final byte[] buf = new byte[1024];
        try {
            while (true) {
                final int len = pi.read(buf);
                if (len == -1) {
                    break;
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
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