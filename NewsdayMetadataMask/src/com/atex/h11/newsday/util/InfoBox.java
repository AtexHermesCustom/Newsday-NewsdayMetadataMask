package com.atex.h11.newsday.util;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class InfoBox {
	
	private static Component parentComponent = null;
	
	public static void setParentComponent(Component parent) {
		parentComponent = parent;
	}
	
	public static void showMessage(String msg, String title) {
        JOptionPane.showMessageDialog(
        	parentComponent, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

	public static void showMessage(String msg, String title, int msgType) {
        JOptionPane.showMessageDialog(
        	parentComponent, msg, title, msgType);
    }	

	public static void showMessage(Component parent, String msg, String title, int msgType) {
        JOptionPane.showMessageDialog(
        	parent, msg, title, msgType);
    }	
	
	public static void ShowException(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		showMessage(errors.toString(), "Exception", JOptionPane.ERROR_MESSAGE);
	}
}
