package com.paypal.ophack.vidya.karna;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
	private JLabel meaningEnLabel;
	private JTextArea meaningEnArea;
	private JLabel meaningOthLabel;
	private JTextArea meaningOthArea;
	private JButton searchButton;
	private JTextField wordField;
	private JTabbedPane tabs;
	private FocusableTextArea synonymArea;
	private JEditorPane wikiArea;

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
						.getText().toLowerCase());
				if (wordMeaning == null) {
					meaningEnArea.setText("Word not found");
					meaningOthArea.setText("Word not found");
					synonymArea.setText("Word not found");
					return;
				}
				Map<String, String> meaningsEn = wordMeaning.getMeaning_en();
				String meaning = "The " + wordMeaning.getPart_of_speech() + " has " + meaningsEn.keySet().size() + "senses\n\n"; 
				for (String m : meaningsEn.keySet()) {
					meaning += m + " : " + meaningsEn.get(m) + "\n";
				}
				meaningEnArea.setText(meaning);
				
				Map<String, String> meanings = wordMeaning.getMeaning_ta();
				meaning = wordMeaning.getWord_tamil() + "\n";
				for (String m : meanings.keySet()) {
					meaning += m + " : " + meanings.get(m) + "\n";
				}
				meaningOthArea.setText(meaning);
				
				Set<String> synonyms = wordMeaning.getSysnonym_en();
				String synonym = "";
				for(String s : synonyms){
					synonym += s + "\n";
				}
				synonymArea.setText(synonym);
				meaningEnArea.requestFocus();
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
		
		JPanel titleLabels = new JPanel();
		titleLabels.setLayout(new FlowLayout());
		meaningEnLabel = new JLabel("Meaning English");
		meaningEnLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		meaningEnLabel.getAccessibleContext().setAccessibleDescription(
				"Meaning of word");
		titleLabels.add(meaningEnLabel);
		titleLabels.add(Box.createRigidArea(new Dimension(10, 20)));
		meaningOthLabel = new JLabel("Meaning Tamil");
		meaningOthLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		meaningOthLabel.getAccessibleContext().setAccessibleDescription(
				"Meaning of word");
		titleLabels.add(meaningOthLabel);
		meaningPanel.add(titleLabels);
		
		JPanel meaningAreas = new JPanel();
		meaningAreas.setLayout(new FlowLayout());
		meaningEnArea = new FocusableTextArea();
		meaningEnArea.setPreferredSize(new Dimension(WIDTH/2, 150));
		JScrollPane enMeaningScroll = new JScrollPane(meaningEnArea);
		meaningAreas.add(enMeaningScroll);
		meaningAreas.add(Box.createRigidArea(new Dimension(10, 150)));
		
		meaningOthArea = new FocusableTextArea();
		meaningOthArea.setPreferredSize(new Dimension(WIDTH/2, 150));
		JScrollPane othMeaningScroll = new JScrollPane(meaningOthArea);
		meaningAreas.add(othMeaningScroll);
		
		meaningPanel.add(meaningAreas);
		frame.add(meaningPanel);
		frame.add(Box.createRigidArea(new Dimension(WIDTH, 10)));
	}

	private void addTabbedMenu(JPanel frame) {
		tabs = new JTabbedPane();
		tabs.setPreferredSize(new Dimension(WIDTH, 300));
		tabs.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				if (tabs.getSelectedIndex() == 0) {
					synonymArea.requestFocus();
				} else if (tabs.getSelectedIndex() == 1){
					wikiArea.requestFocus();
				}
			}
		});
		for (int i = 0; i < tabNames.length; i++) {
			Component component = null;
			if(i == 0){
				synonymArea = new FocusableTextArea();
				synonymArea.setPreferredSize(new Dimension(WIDTH, 200));
				component = synonymArea;
			} else if (i == 1){
				wikiArea = new JEditorPane();
				wikiArea.setEditable(false);
				wikiArea.setContentType("text/html");
				wikiArea.addFocusListener(new FocusListener() {
					public String getResponseString(HttpResponse response) {
						BufferedReader br;
						try {
							br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						} catch (IllegalStateException | IOException e1) {
							return null;
						}
						String responseStr = "";
				        String line = "";
				        try {
							while ((line = br.readLine()) != null) {
							    responseStr += line;
							}
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}        
				        
						return responseStr;
					}
					
					public String getUrlEncoded(String str){
						return str.replaceAll(" ", "%20");
					}
					
					@Override
					public void focusLost(FocusEvent arg0) {
						
					}
					
					@SuppressWarnings("unchecked")
					@Override
					public void focusGained(FocusEvent arg0) {
						CloseableHttpClient httpclient = HttpClientBuilder.create().build();
						String url = "http://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&exintro=&titles="+getUrlEncoded(wordField.getText().toLowerCase());
						HttpGet getRequest = new HttpGet(url);;
						try {
							HttpResponse resp = httpclient.execute(getRequest);
							String responseJson = getResponseString(resp);
							Gson gson = new Gson();
							JsonObject obj = gson.fromJson(responseJson, JsonObject.class);
							Set<Entry<String, JsonElement>> entries = gson.fromJson(gson.fromJson(obj.get("query"), JsonObject.class).get("pages"), JsonObject.class).entrySet();
							Iterator<Entry<String, JsonElement>> it = entries.iterator();
							String article = null;
							if(it.hasNext()){
								Entry<String, JsonElement> entry = it.next();
								article = gson.fromJson(entry.getValue().toString(), JsonObject.class).get("extract").getAsString();
							}
							article = article.replaceAll("<[^>]*>", "");
							wikiArea.setText(article);
						} catch (ClientProtocolException e) {
							wikiArea.setText("No details found");
						} catch (IOException e) {
							wikiArea.setText("No details found");
						}
						wikiArea.setCaretPosition(0);
					}
				});
				component = new JScrollPane(wikiArea);
			}
			tabs.addTab(tabNames[i], component);
		}
		frame.add(tabs);
	}

	class UITraversalPolicy extends FocusTraversalPolicy {

		@Override
		public Component getComponentAfter(Container aContainer,
				Component aComponent) {
			if (aComponent.equals(wordField)) {
				return searchButton;
			}
			else if (aComponent.equals(searchButton)) {
				return meaningEnArea;
			}
			else if (aComponent.equals(meaningEnArea)){
				return meaningOthArea;
			}
			else if (aComponent.equals(meaningOthArea)) {
				tabs.setSelectedIndex(0);
				return synonymArea;
			}
			else if (aComponent.equals(synonymArea)){
				tabs.setSelectedIndex(1);
				return wikiArea;
			}
			else if (aComponent.equals(wikiArea)){
				return wordField;
			}
			return null;
		}

		@Override
		public Component getComponentBefore(Container aContainer,
				Component aComponent) {
			if (aComponent.equals(wordField)) {
				return wordField;
			}
			else if (aComponent.equals(searchButton)) {
				return wordField;
			}
			else if (aComponent.equals(meaningEnArea)){
				return wordField;
			}
			else if (aComponent.equals(meaningOthArea)) {
				return meaningEnArea;
			}
			else if (aComponent.equals(synonymArea)){
				return meaningOthArea;
			}
			else if (aComponent.equals(wikiArea)){
				tabs.setSelectedIndex(0);
				return synonymArea;
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
			this.setEditable(false);
			this.setLineWrap(true);
			this.setWrapStyleWord(true);
			this.setFont(new Font("Arial Unicode MS",Font.PLAIN,16));
		}
	}
}