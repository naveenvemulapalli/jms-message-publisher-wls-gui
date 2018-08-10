package com.nv.test.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author naveenvemulapalli
 *
 */
public class FileUtils {

	  public static String fileToString(File file)
	  throws IOException {
	      int len;
	      char[] chr = new char[4096];
	      final StringBuffer buffer = new StringBuffer();
	      final FileReader reader = new FileReader(file);
	      try {
	          while ((len = reader.read(chr)) > 0) {
	              buffer.append(chr, 0, len);
	          }
	      } finally {
	          reader.close();
	      }
	      return buffer.toString();
	  
	}
}
