package com.atex.h11.newsday.metadata.filter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class AlphaNumericDocumentFilter extends DocumentFilter {
    private String regex = "[^0-9a-zA-Z]+"; // default regex: allow numbers and all letters, both upper and lower case
    
	public AlphaNumericDocumentFilter() { }
	
	public AlphaNumericDocumentFilter(boolean allowLowercaseChars, boolean allowUppercaseChars) {
		regex = "[^0-9";
		if (allowLowercaseChars) { regex += "a-z"; }
		if (allowUppercaseChars) { regex += "A-Z"; }
		regex += "]+";
	}
	
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        super.replace(fb, offset, length, text.replaceAll(regex, ""), attrs);    	
    }
}
