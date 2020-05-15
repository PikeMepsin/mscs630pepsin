/*
 * Mike Pepsin
 * testEncryption.java
 * Project: Guilty PlAESures
 * This file is for testing the methods in mp3Driver.java for functionality
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public class testEncryption {
  
  /**
   * Encrypts mp3 file and passes it to Decrypt() for decryption
   * @param args
   */
  public static void main(String[] args) {
    File file = new File("C:\\Users\\Mike\\eclipse-workspace1\\GuiltyPlAESures\\goodmeme.mp3");
    byte[] mp3Bytes;
    String key = null, encrypted = "";
    try {
      mp3Bytes = mp3Driver.mp3Parser(file);
      key = mp3Driver.generateAESKey(128);
        encrypted = mp3Driver.encrypt(mp3Driver.stringToKey(key), mp3Bytes);
        System.out.println(key);
        System.out.println(encrypted);
        //System.out.println(key);
        Decrypt(key, encrypted);
    } 
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }	
  }

  /**
   * Decrypts mp3 file and writes it to disk
   * @param key
   * @param encrypted
   */
  public static void Decrypt(String key, String encrypted) {
    byte[] decryptedMP3Bytes;
    try {
      decryptedMP3Bytes = mp3Driver.decrypt(mp3Driver.stringToKey(key), encrypted);
      mp3Driver.writeMP3(decryptedMP3Bytes);
      //System.out.println(Arrays.toString(decryptedMP3Bytes));
    }
    catch (InvalidKeyException e) {
      e.printStackTrace();
    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    catch (NoSuchPaddingException e) {
      e.printStackTrace();
    }
    catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }		
  }
}
