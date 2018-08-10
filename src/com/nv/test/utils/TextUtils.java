package com.nv.test.utils;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * @author naveenvemulapalli
 *
 */
public class TextUtils {

	public static void appendToPane(JTextPane textPane, String msg, Color color) throws BadLocationException
    {

		StyledDocument doc = textPane.getStyledDocument();
		
		SimpleAttributeSet sas = new SimpleAttributeSet();
		StyleConstants.setForeground(sas, color);
		//StyleConstants.setBold(keyWord, true);
		
		doc.insertString(doc.getLength(), msg, sas);
    }
	
	
	public static void appendHtmlToPane(JTextPane tp, String msg, String c) throws BadLocationException, IOException
    {	
	
    HTMLEditorKit kit = new HTMLEditorKit();
    HTMLDocument doc = new HTMLDocument();
    tp.setEditorKit(kit);
    tp.setDocument(doc);
    kit.insertHTML(doc, doc.getLength(), "<font color='"+c+"'>"+msg+"</font>", 0, 0, null);
    }
}
