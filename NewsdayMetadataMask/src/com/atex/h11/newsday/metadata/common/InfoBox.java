package com.atex.h11.newsday.metadata.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JOptionPane;

public class InfoBox {
	
	public static void ShowMessage(String msg, String title) {
        JOptionPane.showMessageDialog(
        	null, msg, "InfoBox: " + title, JOptionPane.INFORMATION_MESSAGE);
    }

	public static void ShowMessage(String msg, String title, int msgType) {
        JOptionPane.showMessageDialog(
        	null, msg, "InfoBox: " + title, msgType);
    }	
	
	public static void ShowException(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		ShowMessage(errors.toString(), "Exception", JOptionPane.ERROR_MESSAGE);
	}
}
