/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.pixomania.StatCrawl.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author galaxyAbstractor
 */
public class Md5 {
    /**
     * Generate an md5 hash
     * @param buffer Byte-array of a String
     * @return an md5 hash as a String
     */
    public static String getMD5Digest(byte[] buffer) {
        String resultHash = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] result = new byte[md5.getDigestLength()];
            md5.reset();
            md5.update(buffer);
            result = md5.digest();

            StringBuilder buf = new StringBuilder(result.length * 2);

            for (int i = 0; i < result.length; i++) {
                int intVal = result[i] & 0xff;
                if (intVal < 0x10) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(intVal));
            }

            resultHash = buf.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return resultHash;
    }
}
