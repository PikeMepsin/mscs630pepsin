/*
 * Mike Pepsin
 * mp3Driver.java
 * Project: Guilty PlAESures
 * This file contains the methods for encryption/decryption of mp3 files
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.catalina.tribes.util.Arrays;
 
public class mp3Driver {
  /** Algorithm used by AES keys and ciphers. */
  public static final String AES_ALGORITHM = "AES";
  /** Number of bits for AES 128 bit key. */
  public static final int AES_128 = 128;
  /** Number of bits for AES 256 bit key. */
  public static final int AES_256 = 256;
   
  String cryptoKey = "";
  
  /**
   * Parses a file as a byte array. This method is DEPRECATED by h3lloservlet.doPost
   * @param file
   * @return file as a byte array
   * @throws FileNotFoundException
   * @throws IOException
   * @throws Exception
   */
  public static byte[] mp3Parser(File file) throws FileNotFoundException, IOException, Exception {
    // File file = new File("C:\\Users\\Mike\\eclipse-workspace1\\GuiltyPlAESures\\secret.mp3");
    // get base64 encoded version of the key
        
    FileInputStream fis = new FileInputStream(file);
    //System.out.println(file.exists() + "!!");
    //InputStream in = resource.openStream();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] buf = new byte[1024];
    try {
      for (int readNum; (readNum = fis.read(buf)) != -1;) {
        bos.write(buf, 0, readNum); //no doubt here is 0
        //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
        System.out.println("read " + readNum + " bytes,");
      }
    } 
    catch (IOException ex) {
      ex.printStackTrace();
    }
    byte[] bytes = bos.toByteArray();
    // System.out.println(Arrays.toString(bytes));
      return bytes;
    }
    
    public static String getCiphertext(byte[] bytes) {
      return Arrays.toString(bytes);
    }
  
  /**
   * Converts the byte array back to a MP3 file and writes it to project directory
   * @param mp3Transformed byte array
   * @return written MP3 file
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static File writeMP3(byte[] mp3Transformed) throws FileNotFoundException, IOException {
    // writes byte array as mp3 file
    File newMp3 = new File("secret_revealed.mp3");
    FileOutputStream fos = new FileOutputStream(newMp3);
    try {
      fos.write(mp3Transformed);
      fos.flush();
      fos.close();
    } 
    catch (IOException e) {
      System.out.println("Bytes could not be reassembled");
    }
        
    return newMp3;
  }
    
  /**
   * Encrypts a byte array
   *
   * @param key
   * 	 a key that can be used to encrypt
   * @param toEncrypt
   *   the data to encrypt
   * @return encrypted byte array
   * @throws InvalidKeyException
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws IllegalArgumentException
   * @throws IOException
   */
  public static String encrypt(final Key key, byte[] toEncrypt)
    throws InvalidKeyException, NoSuchAlgorithmException,
    NoSuchPaddingException, IllegalArgumentException, IOException {
  
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
      processCipherStream(getEncryptCipher(key),
        StreamUtils.getInputStream(toEncrypt), baos);
    String ciphertext = new String(baos.toByteArray(), "ISO-8859-1");
    return ciphertext;
  }
    
  /**
   * Decrypts byte array
   *
   * @param key
   *   a key that can be used to decrypt
   * @param toDecrypt
   *   the data to decrypt
   * @return decrypted byte array
   * @throws InvalidKeyException
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws IllegalArgumentException
   * @throws IOException
   */
  public static byte[] decrypt(final Key key, String toDecrypt)
    throws InvalidKeyException, NoSuchAlgorithmException,
    NoSuchPaddingException, IllegalArgumentException, IOException {
     
    byte[] decryptStream = toDecrypt.getBytes("ISO-8859-1");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      processCipherStream(getDecryptCipher(key),
        StreamUtils.getInputStream(decryptStream), baos);
    return baos.toByteArray();
  }
    
  /**
   * Generates an AES encryption key
   * @param length
   * @return key as String
   * @throws NoSuchAlgorithmException
   */
  static String generateAESKey(int length) throws NoSuchAlgorithmException {
    KeyGenerator keygen = KeyGenerator.getInstance(AES_ALGORITHM);
    keygen.init(length);
    SecretKey key = keygen.generateKey();
    return keyToString(key);
  } 
    
  /**
   * Generates AES encryption cipher
   * @param key
   * @return instance of encryption cipher
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   */
  public static Cipher getEncryptCipher(final Key key)
    throws NoSuchAlgorithmException, NoSuchPaddingException,
    InvalidKeyException {
     
    Cipher cipher = Cipher.getInstance(key.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return cipher;
  }
    
  /**
   * Generates AES decryption cipher
   * @param key
   * @return instance of decryption cipher
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   */
  public static Cipher getDecryptCipher(final Key key)
    throws NoSuchAlgorithmException, NoSuchPaddingException,
    InvalidKeyException {
        
    Cipher cipher = Cipher.getInstance(key.getAlgorithm());
    cipher.init(Cipher.DECRYPT_MODE, key);
    return cipher;
  }
    
  /**
   * Process a data stream using a cipher.
   *
   * If cipher is initialized to ENCRYPT_MODE, the input stream will be
   * encrypted. If cipher is initialized to DECRYPT_MODE, the input stream
   * will be decrypted.
   *
   * @param cipher
   *            an initialized cipher.
   * @param in
   *            the data to encrypt.
   * @param out
   *            where encrypted data is written.
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   * @throws IOException
   */
  public static void processCipherStream(final Cipher cipher,
    final InputStream in, final OutputStream out)
    throws NoSuchAlgorithmException, NoSuchPaddingException,
    InvalidKeyException, IOException {
        
    CipherOutputStream cos = new CipherOutputStream(out, cipher);
    StreamUtils.transferStream(in, cos);
  }
    
  /**
   * Converts a Key to a String for output purposes
   * @param key
   * @return key as String
   */
  public static String keyToString(Key key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }
    
  /**
   * Converts a String back into a Key
   * @param str
   * @return String as Key
   */
  public static Key stringToKey(String str) {
    byte[] decodedKey = Base64.getDecoder().decode(str);
    // rebuild key using SecretKeySpec
    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    return originalKey;
  }
    
}