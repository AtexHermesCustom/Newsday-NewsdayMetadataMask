package com.atex.h11.newsday.metadata.component;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class CheckBoxList extends JList {
	private static final long serialVersionUID = 1L;
	private boolean enabled;
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	/**
	 * This method initializes CheckBoxList	
	 * 	
	 * @param none
	 * @return com.atex.h11.sph.metadata.component.CheckBoxList
	 */	
	public CheckBoxList() {
		setCellRenderer(new CellRenderer());

		addMouseListener(
			new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (enabled) {
						int index = locationToIndex(e.getPoint());
						if (index != -1) {
							JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
							checkbox.setSelected(! checkbox.isSelected());
							repaint();
						}
					}
	            }
			}
		);

		addKeyListener(
			new KeyListener() {
	            @Override
	            public void keyPressed(KeyEvent e) {
	            	if (enabled) {
		                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
		                    int index = getSelectedIndex();
							if (index != -1) {
								JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
								checkbox.setSelected(! checkbox.isSelected());
								repaint();
							}
		                }
	            	}
	            }

	            @Override
	            public void keyReleased(KeyEvent e) { }

	            @Override
	            public void keyTyped(KeyEvent e) { }				
			}
		);
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		enabled = true; // set enabled initially
	}
	
	/**
	 * This method enables/disables the object
	 * 
	 * @param none
	 * @return none
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		super.setEnabled(enabled);
	}
	
	/**
	 * This method returns the number of selected items
	 * 	
	 * @param none
	 * @return int
	 */	
	public int getSelectedCount() {
		int selectedCount = 0;
		for (int i = 0; i < this.getModel().getSize(); i++) {
			JCheckBox checkbox = (JCheckBox) getModel().getElementAt(i);
			if (checkbox.isSelected()) {
				selectedCount++;
			}
		}		
		return selectedCount;
	}

	/**
	 * This method returns an ArrayList of selected items
	 * 	
	 * @param none
	 * @return ArrayList<String>	
	 */	
	public ArrayList<String> getSelectedList() {
		ArrayList<String> selectedList = new ArrayList<String>(); 
		for (int i = 0; i < this.getModel().getSize(); i++) {
			JCheckBox checkbox = (JCheckBox) getModel().getElementAt(i);
			if (checkbox.isSelected()) {
				selectedList.add(checkbox.getText());
			}
		}
		return selectedList;
	}
	
	/**
	 * This method returns a comma-separated string of the selected items	
	 * 
	 * @param none	
	 * @return String
	 */	
	public String getSelectedListString() {
		String commaSepList = "";
		int i = 0;
		ArrayList<String> arrayList = getSelectedList();
    	for (String s: arrayList) {
	    	s = s.trim();			// trim any leading & trailing spaces
	    	if (! s.isEmpty()) {
	    		if (i > 0) { commaSepList += ","; }
	    		commaSepList += s;
	    		i++;
	    	}
    	}			
    	return commaSepList;
	}
   
	/**
	 * This method sets the selected items given an array list
	 * 	
	 * @param ArrayList<String>
	 * @return none
	 */	
	public void setSelectedFromList(ArrayList<String> valueList) {
		for (int i = 0; i < this.getModel().getSize(); i++) {
			JCheckBox checkbox = (JCheckBox) this.getModel().getElementAt(i);
			checkbox.setSelected(false); // initialize
			for (String value: valueList) {
				if (checkbox.getText().equalsIgnoreCase(value)) {
					checkbox.setSelected(true);
					break;
				}
			}
		}
		repaint();	// needed
	}
	
	/**
	 * This method deselects all items in the list
	 * 	
	 * @param none
	 * @return none
	 */	
	public void deselectAll() {
		for (int i = 0; i < this.getModel().getSize(); i++) {
			JCheckBox checkbox = (JCheckBox) this.getModel().getElementAt(i);
			checkbox.setSelected(false);
		}
		//repaint(); // needed
	}	
	
	/**
	 * This method sets the selected items given a comma-separated string	
	 * 	
	 * @param String
	 * @return none	
	 */	
	public void setSelectedFromListString(String commaSepList) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (String s : commaSepList.split(",")) {
			s = s.trim();
			arrayList.add(s);
		}		
		setSelectedFromList(arrayList);
	}

	protected class CellRenderer implements ListCellRenderer {
		public Component getListCellRendererComponent(
                    JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
			JCheckBox checkbox = (JCheckBox) value;
			checkbox.setBackground(isSelected ?
                 getSelectionBackground() : getBackground());
			checkbox.setForeground(isSelected ?
                 getSelectionForeground() : getForeground());
			checkbox.setEnabled(isEnabled());
			checkbox.setFont(getFont());
			checkbox.setFocusPainted(false);
			checkbox.setBorderPainted(true);
			checkbox.setBorder(isSelected ?
					UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
			return checkbox;
		}
	}

}