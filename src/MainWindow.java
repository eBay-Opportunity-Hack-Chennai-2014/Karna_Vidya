package src;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainWindow {

	public static void main(String[] args) {
		DictionaryUI ui = new DictionaryUI();
		ui.render();
	}
}

class DictionaryUI {
	private String[] tabNames = { "Meaning", "Synonyms", "Wikipedia" };
	private JFrame mainFrame;
	private JPanel searchPanel;
	private JButton searchButton;
	private JTextField wordField;
	private JTabbedPane tabs;
	private JTextArea[] textArea = new JTextArea[3];

	public void render() {
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setBounds(100, 100, 700, 500);
		mainFrame.setTitle("WordSearch");
		mainFrame.setFocusTraversalPolicy(new UITraversalPolicy());
		initUI();
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	private void initUI() {
		mainFrame.setLayout(new BorderLayout(2, 1));
		addSearchPanel(mainFrame, 0);
		addTabbedMenu(mainFrame, 1);
	}

	private void addSearchPanel(JFrame frame, int gridLocation) {
		searchPanel = new JPanel();
		wordField = new JTextField(50);
		wordField.getAccessibleContext().setAccessibleDescription(
				"Enter the word to search here");
		searchPanel.add(wordField);
		searchButton = new JButton("Search");
		searchButton.getAccessibleContext().setAccessibleDescription(
				"Press enter to search");
		searchPanel.add(searchButton);
		searchPanel.setPreferredSize(new Dimension(600, 70));
		frame.add(searchPanel, BorderLayout.PAGE_START);
	}

	private void addTabbedMenu(JFrame frame, int gridLocation) {
		tabs = new JTabbedPane();
		tabs.setPreferredSize(new Dimension(600, 300));
		for (int i = 0; i < tabNames.length; i++) {
			JTextArea textArea = getTextArea(tabNames[i]);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setText("The quick brown fox jumped over a lazy dog. The quick brown fox jumped over a lazy dog. The quick brown fox jumped over a lazy dog. ");
			textArea.setEditable(false);
			tabs.addTab(tabNames[i], textArea);
		}
		frame.add(tabs, BorderLayout.CENTER);
	}

	private JTextArea getTextArea(String tabName) {
		for (int i = 0; i < tabNames.length; i++) {
			if (tabName.equals(tabNames[i])) {
				return textArea[i];
			}
		}
		return null;
	}
	
	private int indexOfArea(JTextArea area){
		for (int i = 0; i < textArea.length; i++) {
			if (area.equals(textArea[i])) {
				return i;
			}
		}
		return -1;
	}

	class UITraversalPolicy extends FocusTraversalPolicy {

		@Override
		public Component getComponentAfter(Container aContainer, Component aComponent) {
			if(aComponent instanceof JTextArea){
				int currentAreaIndex = indexOfArea((JTextArea) aComponent);
				if(currentAreaIndex < tabNames.length - 1){
					return textArea[currentAreaIndex + 1];
				} else {
					return searchPanel;
				}
			}
			return null;
		}

		@Override
		public Component getComponentBefore(Container aContainer, Component aComponent) {
			
			return null;
		}

		@Override
		public Component getDefaultComponent(Container aContainer) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Component getFirstComponent(Container aContainer) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Component getLastComponent(Container aContainer) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}