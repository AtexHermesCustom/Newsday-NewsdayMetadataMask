package com.atex.h11.newsday.metadata.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.unisys.media.commonservices.dialogs.metadata.custom.NodeValueInspector;
import com.unisys.media.commonservices.dialogs.metadata.view.ICustomMetadataPanel;
import com.atex.h11.newsday.metadata.sp.CustomMetadataPanel;

public class TestSPMetadata {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame jFrame = new JFrame();
		final ICustomMetadataPanel customPane = new CustomMetadataPanel();
		final HashMap<String, String> metadata = new HashMap<String, String>();
		
		// sample metadata
		metadata.put("PUB", "NEWSDAY");
		metadata.put("DESK", "ALL");
		metadata.put("REPORTER1", "Reporter 3 name");
		metadata.put("REPORTER2", "Reporter 2 name");
		metadata.put("REPORTER3", "Reporter 1 name");
		metadata.put("REPORTER1_EMAIL", "email1@email.com");
		metadata.put("REPORTER2_EMAIL", "email2@email.com");
		metadata.put("REPORTER3_EMAIL", "email3@email.com");
		metadata.put("ASSIGN_LEN", "123");
		metadata.put("CONTRIBUTOR", "contributor value");
		metadata.put("STORY_GROUP", "story group");
		metadata.put("STORY_TYPE", "story type value");
		metadata.put("LABEL", "label value");
		metadata.put("DESCRIPTION", "this is the description\nthis is the 2nd line");
		metadata.put("PRINT_EXTRA", "print extra here");
		metadata.put("PRINT_SECTION", "print section value");
		metadata.put("SEQUENCE", "sequence value");
		metadata.put("HOMEPAGE", "www.google.com/help");
		metadata.put("ARRIVAL_STATUS", "LIVE");
		metadata.put("EMBARGO_FLAG", "TRUE");
		metadata.put("EMBARGO_DATE", "");
		metadata.put("EMBARGO_TIME", "");
		metadata.put("CATEGORIES", "cat1,cat2,cat3");
		metadata.put("COMMUNITIES", "com1,com2,com3");
		metadata.put("PRIORITY", "MEDIUM");
		metadata.put("DIGITAL_EXTRA1", "digital extra1");
		metadata.put("DIGITAL_EXTRA2", "digital extra2");
		metadata.put("PRINT_PAGE", "99");
		metadata.put("EXCLUSIVE_FLAG", "FALSE");		
		
		jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (customPane.canActionBePerformed(ICustomMetadataPanel.OK_BUTTON)) {
					printHashMap(customPane.getMetadataValues());
					System.exit(0);
				}
			}
		});
		
		try {
			JPanel panel = new JPanel();
			
			// dummy inspector
			NodeValueInspector inspector = null;
						
			// args: inspector, metadata hashmap, readonly=false
			panel.add(customPane.getPanel(inspector, metadata, false));
			panel.add(okButton);
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			
			jFrame.add(panel);
			jFrame.setTitle(customPane.getTitle());
			jFrame.pack();
			
		} catch (Exception e) {
			e.printStackTrace();
			
			System.out.println("Error:" + e.toString());
		}		

		jFrame.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent e) {
			}

			public void windowClosed(WindowEvent e) {
				System.out.println("End of main()");
				printHashMap(customPane.getMetadataValues());
				System.exit(0);
			}

			public void windowClosing(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowOpened(WindowEvent e) {
			}
		});
		
		jFrame.setVisible(true);
	}
	
	public static void printHashMap(HashMap<String, String> map) {
		// sort map by key
		Map<String, String> treeMap = new TreeMap<String, String>(map);
		
		System.out.println("printHashMap:");
		System.out.println("<key>=<value>");
		for (String s : treeMap.keySet()) {
			System.out.println(s + "=" + treeMap.get(s));
		};
		System.out.println("--end--");
	}	

}
