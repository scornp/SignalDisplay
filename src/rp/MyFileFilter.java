package rp;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: R Philp
 * Date: 14-Apr-2006
 * Time: 20:14:25
 * A component of the MDAQ project to isolate data chunks
 * that contain meteor data
 */

//public class MyFileFilter extends javax.swing.filechooser.FileFilter {
    public class MyFileFilter extends FileFilter {
    String [] extensions = null;
    /**
     * set up acceptable file extension list
     */
   MyFileFilter (String [] extensions){
        this.extensions = extensions;
    }

    /**
     * Default all extensions are ok
     */
       MyFileFilter (){
        this.extensions = extensions;
    }

  /**
    This is the one of the methods that is declared in
    the abstract class
   */
  public boolean accept(File f)
  {
    //if it is a directory -- we want to show it so return true.
    if (f.isDirectory())
      return true;


      // all extensions are ok
      if (extensions == null) return true;

      // other wise use the extension list
          String extension = getExtension(f);
      for (int i = 0; i < extensions.length; i++){
          if (extension.equals(extensions[i])) return true;

      }
    return false;
  }

  /**
    Again, this is declared in the abstract class

    The description of this filter
   */
  public String getDescription()  {
      return "Master index files";
  }

  /**
    Method to get the extension of the file, in lowercase
   */
  public static String getExtension(File f)  {
    String s = f.getName();
    int i = s.lastIndexOf('.');
    if (i > 0 &&  i < s.length() - 1)
      return s.substring(i+1).toLowerCase();
    return "";
  }

      /**
    Method to get the extension of the file, in lowercase
   */
  public static String getExtension(String s)  {
    int i = s.lastIndexOf('.');
    if (i > 0 &&  i < s.length() - 1)
      return s.substring(i+1).toLowerCase();
    return "";
  }
}

