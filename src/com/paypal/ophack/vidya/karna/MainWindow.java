package com.paypal.ophack.vidya.karna;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainWindow {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	DictionaryUI ui = new DictionaryUI();
        		ui.render();
            }
        });
	}
}

class DictionaryUI {
	private int WIDTH = 600;
	
	private String[] tabNames = { "Synonyms", "Wikipedia" };
	private JFrame mainFrame;
	private JPanel searchPanel;
	private JPanel meaningPanel;
	private JLabel meaningLabel;
	private JButton searchButton;
	private JTextField wordField;
	private JTabbedPane tabs;
	private ArrayList<JTextArea> textAreas;
	
	public DictionaryUI(){
		textAreas = new ArrayList<JTextArea>();
		for(int i = 0; i <= tabNames.length; i++){
			textAreas.add(new JTextArea());
		}
	}

	public void render() {
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setBounds(100, 100, 700, 500);
		mainFrame.setTitle("WordSearch");
		mainFrame.setFocusTraversalPolicy(new UITraversalPolicy());
		initUI();
		mainFrame.pack();
		mainFrame.setVisible(true);
		wordField.requestFocus();
	}

	private void initUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		addSearchPanel(panel);
		addMeaningPanel(panel);
		addTabbedMenu(panel);
		mainFrame.setContentPane(panel);
	}

	private void addSearchPanel(JPanel frame) {
		searchPanel = new JPanel();
		wordField = new JTextField(50);
		wordField.getAccessibleContext().setAccessibleDescription(
				"Enter the word to search here");
		searchPanel.add(wordField);
		searchButton = new JButton("Search");
		searchButton.getAccessibleContext().setAccessibleDescription(
				"Press enter to search");
		searchPanel.add(searchButton);
		searchPanel.setPreferredSize(new Dimension(WIDTH, 70));
		frame.add(searchPanel);
	}
	
	private void addMeaningPanel(JPanel frame){
		meaningPanel = new JPanel();
		meaningPanel.setLayout(new BorderLayout());
		meaningLabel = new JLabel("Meaning");
		meaningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		meaningLabel.getAccessibleContext().setAccessibleDescription(
				"Meaning of word");
		meaningPanel.add(meaningLabel, BorderLayout.NORTH);
		JTextArea meaningArea = textAreas.get(tabNames.length);
		meaningArea.setText("Meaning");
		meaningArea.setPreferredSize(new Dimension(WIDTH, 150));
		meaningArea.setEditable(false);
		meaningArea.setLineWrap(true);
		meaningArea.setWrapStyleWord(true);
		meaningArea.setText("The quick brown fox jumped over a lazy dog. The quick brown fox jumped over a lazy dog. The quick brown fox jumped over a lazy dog. ");
		meaningPanel.add(meaningArea, BorderLayout.SOUTH);
		frame.add(meaningPanel);
		frame.add(Box.createRigidArea(new Dimension(WIDTH, 10)));
	}

	private void addTabbedMenu(JPanel frame) {
		tabs = new JTabbedPane();
		tabs.setPreferredSize(new Dimension(WIDTH, 300));
		for (int i = 0; i < tabNames.length; i++) {
			JTextArea textArea = getTextArea(tabNames[i]);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setText("The quick brown fox jumped over a lazy dog. The quick brown fox jumped over a lazy dog. The quick brown fox jumped over a lazy dog. ");
			textArea.setEditable(false);
			tabs.addTab(tabNames[i], textArea);
		}
		frame.add(tabs);
	}

	private JTextArea getTextArea(String tabName) {
		for (int i = 0; i < tabNames.length; i++) {
			if (tabName.equals(tabNames[i])) {
				return textAreas.get(i);
			}
		}
		return null;
	}
	
	private int indexOfArea(JTextArea area){
		for (int i = 0; i < textAreas.size(); i++) {
			if (area.equals(textAreas.get(i))) {
				return i;
			}
		}
		return -1;
	}

	class UITraversalPolicy extends FocusTraversalPolicy {

		@Override
		public Component getComponentAfter(Container aContainer, Component aComponent) {
			if(aComponent.equals(wordField)){
				return searchButton;
			}
			if(aComponent.equals(searchButton)){
				return textAreas.get(tabNames.length);
			}
			if(aComponent instanceof JTextArea){
				int currentAreaIndex = indexOfArea((JTextArea) aComponent);
				if(currentAreaIndex == tabNames.length){
					tabs.setSelectedIndex(0);
					return getTextArea(tabNames[tabs.getSelectedIndex()]);
				}
				else if(currentAreaIndex < tabNames.length - 1){
					tabs.setSelectedIndex(currentAreaIndex + 1);
					return getTextArea(tabNames[tabs.getSelectedIndex()]);
				} else {
					return wordField;
				}
			}
			return null;
		}

		@Override
		public Component getComponentBefore(Container aContainer, Component aComponent) {
			if(aComponent.equals(wordField)){
				return wordField;
			}
			if(aComponent.equals(searchButton)){
				return wordField;
			}
			if(aComponent instanceof JTextArea){
				int currentAreaIndex = indexOfArea((JTextArea) aComponent);
				if(currentAreaIndex == 0){
					return wordField;
				}
				if(currentAreaIndex < tabNames.length - 1 && currentAreaIndex > 0){
					return textAreas.get(currentAreaIndex - 1);
				} else {
					return searchPanel;
				}
			}
			return null;
		}

		@Override
		public Component getDefaultComponent(Container aContainer) {
			return wordField;
		}

		@Override
		public Component getFirstComponent(Container aContainer) {
			return wordField;
		}

		@Override
		public Component getLastComponent(Container aContainer) {
			return searchButton;
		}
	}
}