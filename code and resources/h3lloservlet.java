/*
 * Mike Pepsin
 * h3lloservlet.java
 * Project: Guilty PlAESures
 * Servlet functionality for Tomcat servlet for Guilty PlAESures
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

/**
 * Servlet implementation class h3lloservlet
 */
@WebServlet("/guiltyprocess")
@MultipartConfig
public class h3lloservlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
       
  /**
   * @see HttpServlet#HttpServlet()
   */
  public h3lloservlet() {
    super();
        // Auto-generated constructor stub
    }

  /**
   * doGet is called by the Decryption form for turning ciphertext + key into an mp3 file
   * @param HttpServletRequest and HttpServletResponse
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    response.setContentType("text/html");
      
    String key = request.getParameter("decryption key");
    String ciphertext = request.getParameter("ciphertext");
    try { // decrypt and write file
      byte[] mp3file = mp3Driver.decrypt(mp3Driver.stringToKey(key), ciphertext);
        mp3Driver.writeMP3(mp3file);
    }
    catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalArgumentException
      | IOException e) {
      e.printStackTrace();
    }
    out.println("Psst... check your files");      
    out.flush();
  }

  /**
   * doPost is called by the Encryption form for turning an mp3 file into ciphertext + key
   * @param HttpServletRequest and HttpServletResponse
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
      
    PrintWriter out = response.getWriter();
    response.setContentType("text/html");
      
    int keyLength = 128;
    String key = null, encryptext = null;
      
    try { // generate AES key
      key = mp3Driver.generateAESKey(keyLength);
      // out.println(key);
    }
    catch (NoSuchAlgorithmException e) {
      out.println("Error generating key");
    }
    
    // Parse mp3 file
    Part uploadedFile = request.getPart("mp3file");
    InputStream content = uploadedFile.getInputStream();
    byte[] targetArray = IOUtils.toByteArray(content);
    try {
      encryptext = mp3Driver.encrypt(mp3Driver.stringToKey(key), targetArray);
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
      
    // build HTML output
    String htmlRespone = "<html>";
    htmlRespone += "<p>Your key is: " + key + "<br/>";      
    htmlRespone += "Ciphertext: " + encryptext + "</p>";    
    htmlRespone += "</html>";
    out.println(htmlRespone);
  	out.flush();
  }

}
