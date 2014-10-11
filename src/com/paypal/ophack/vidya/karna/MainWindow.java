package com.paypal.ophack.vidya.karna;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Map;

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
	private ArrayList<FocusableTextArea> textAreas;

	public DictionaryUI() {
		textAreas = new ArrayList<FocusableTextArea>();
		for (int i = 0; i <= tabNames.length; i++) {
			textAreas.add(new FocusableTextArea());
		}
	}

	public void render() {
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setBounds(100, 100, 700, 500);
		mainFrame.setTitle("WordSearch");
		// mainFrame.setFocusTraversalPolicy(new UITraversalPolicy());
		initUI();
		mainFrame.pack();
		mainFrame.setVisible(true);
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
		wordField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				WordMeaningStore store = WordMeaningStore.getInstance();
				MeaningObject wordMeaning = store.getMeaningObject(wordField
						.getText());
				if (wordMeaning == null) {
					textAreas.get(tabNames.length).setText("Word not found");
					return;
				}
				Map<String, String> meanings = wordMeaning.getMeaning_ta();
				String meaning = "";
				for (String m : meanings.keySet()) {
					meaning += m + " : " + meanings.get(m) + "\n";
				}
				textAreas.get(tabNames.length).setText(null);
				textAreas.get(tabNames.length).setText(meaning);
				
//				Set<String> synonyms = wordMeaning.getSysnonym_en();
				
			}
		});
		searchPanel.add(wordField);
		searchButton = new JButton("Search");
		searchButton.getAccessibleContext().setAccessibleDescription(
				"Press enter to search");
		searchPanel.add(searchButton);
		searchPanel.setPreferredSize(new Dimension(WIDTH, 70));
		frame.add(searchPanel);
	}

	private void addMeaningPanel(JPanel frame) {
		meaningPanel = new JPanel();
		meaningPanel.setLayout(new BoxLayout(meaningPanel, BoxLayout.Y_AXIS));
		meaningLabel = new JLabel("Meaning");
		meaningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		meaningLabel.getAccessibleContext().setAccessibleDescription(
				"Meaning of word");
		meaningPanel.add(meaningLabel);
		final FocusableTextArea meaningArea = textAreas.get(tabNames.length);
		meaningArea.setPreferredSize(new Dimension(WIDTH, 150));
		meaningArea.setEditable(false);
		meaningArea.setLineWrap(true);
		meaningArea.setWrapStyleWord(true);
		meaningArea.setFont(new Font("Arial Unicode MS",Font.PLAIN,16));
		meaningArea.setText("Word not found");
		meaningPanel.add(meaningArea);
		frame.add(meaningPanel);
		frame.add(Box.createRigidArea(new Dimension(WIDTH, 10)));
	}

	private void addTabbedMenu(JPanel frame) {
		tabs = new JTabbedPane();
		tabs.setPreferredSize(new Dimension(WIDTH, 300));
		for (int i = 0; i < tabNames.length; i++) {
			FocusableTextArea textArea = getTextArea(tabNames[i]);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setText("The quick brown fox jumped over a lazy dog. The quick brown fox jumped over a lazy dog. The quick brown fox jumped over a lazy dog. ");
			textArea.setEditable(false);
			tabs.addTab(tabNames[i], textArea);
		}
		frame.add(tabs);
	}

	private FocusableTextArea getTextArea(String tabName) {
		for (int i = 0; i < tabNames.length; i++) {
			if (tabName.equals(tabNames[i])) {
				return textAreas.get(i);
			}
		}
		return null;
	}

	private int indexOfArea(FocusableTextArea area) {
		for (int i = 0; i < textAreas.size(); i++) {
			if (area.equals(textAreas.get(i))) {
				return i;
			}
		}
		return -1;
	}

	class UITraversalPolicy extends FocusTraversalPolicy {

		@Override
		public Component getComponentAfter(Container aContainer,
				Component aComponent) {
			if (aComponent.equals(wordField)) {
				return searchButton;
			}
			if (aComponent.equals(searchButton)) {
				return textAreas.get(tabNames.length);
			}
			if (aComponent instanceof FocusableTextArea) {
				int currentAreaIndex = indexOfArea((FocusableTextArea) aComponent);
				if (currentAreaIndex == tabNames.length) {
					tabs.setSelectedIndex(0);
					return getTextArea(tabNames[tabs.getSelectedIndex()]);
				} else if (currentAreaIndex < tabNames.length - 1) {
					tabs.setSelectedIndex(currentAreaIndex + 1);
					return getTextArea(tabNames[tabs.getSelectedIndex()]);
				} else {
					return wordField;
				}
			}
			return null;
		}

		@Override
		public Component getComponentBefore(Container aContainer,
				Component aComponent) {
			if (aComponent.equals(wordField)) {
				return wordField;
			}
			if (aComponent.equals(searchButton)) {
				return wordField;
			}
			if (aComponent instanceof FocusableTextArea) {
				int currentAreaIndex = indexOfArea((FocusableTextArea) aComponent);
				if (currentAreaIndex == 0) {
					return wordField;
				}
				if (currentAreaIndex < tabNames.length - 1
						&& currentAreaIndex > 0) {
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

	class FocusableTextArea extends JTextArea {
		public FocusableTextArea() {
			super();
			this.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {

				}

				@Override
				public void focusGained(FocusEvent arg0) {
					((FocusableTextArea) (arg0.getComponent())).setCaretPosition(0);
				}
			});
		}
	}
}