package com.paypal.ophack.vidya.karna;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.gson.Gson;

public class ParseTamilMeanings {

	static Map<String, String> storeMeaning = new HashMap<String, String>();
	static Map<String, String> Words = new HashMap<String, String>();

	static Map<String, MeaningObject> dictionary = new HashMap<String, MeaningObject>();

	static int hextoint(String hex) {
		int id = 0;
		for (int h = 0; h < hex.length(); h = h + 2) {
			String sub = hex.subSequence(h, h + 2).toString();
			if (id == 0)
				id = Integer.valueOf(sub, 16);
			else
				id *= Integer.valueOf(sub, 16);
		}
		return id;
	}

	public static void main(String[] args) {

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new InputStreamReader(ParseTamilMeanings.class.getClassLoader().getResourceAsStream("resources/data_all.txt")));
			
			while ((sCurrentLine = br.readLine()) != null) {
				storeMeaning.put(sCurrentLine.split(" ")[0], sCurrentLine);

				// System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		try {

			String sCurrentLine;

			br = new BufferedReader(new InputStreamReader(ParseTamilMeanings.class.getClassLoader().getResourceAsStream("resources/index_all.txt")));
			
			while ((sCurrentLine = br.readLine()) != null) {
				Words.put(sCurrentLine.split(" ")[0], sCurrentLine);

				// System.out.println();
			}
			System.out.println(Words.get("regulator"));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		ReadWordFileFilldictionary("");

	}

	private static void ReadWordFileFilldictionary(String string) {
		
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		Writer writer = null;

		try {

			String FirstCurrentLine;
			String SecondCurrentLine;

			br1 = new BufferedReader(new InputStreamReader(ParseTamilMeanings.class.getClassLoader().getResourceAsStream("resources/5000_meaning.txt")));
			br2 = new BufferedReader(new InputStreamReader(ParseTamilMeanings.class.getClassLoader().getResourceAsStream("resources/5000_meaning_tamil.txt")));
			
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("WordMeaning.txt", true), "utf-8"));

			String wordMeaningENString = "";
			String wordMeaningTAString = "";

			while ((FirstCurrentLine = br1.readLine()) != null) {
				// System.out.println(FirstCurrentLine);
				SecondCurrentLine = br2.readLine();
				if (FirstCurrentLine.contains("#")) {

					MeaningObject wordDetailObject = new MeaningObject();
					Map<String, String> meaning = new HashMap<String, String>();
					Map<String, String> meaning_tamil = new HashMap<String, String>();

					System.out.println(wordMeaningENString);

					String[] wordMeaningENArray = wordMeaningENString
							.split(";");
					String[] wordMeaningTAArray = wordMeaningTAString
							.split(";");

					wordDetailObject
							.setSysnonym_en(GetSynonyms(wordMeaningENArray[1], wordDetailObject));

					for (int i = 2; i < wordMeaningENArray.length; i++) {
						if ((wordMeaningENArray.length - i) == 1) {
							meaning.put(wordMeaningENArray[i], "");
						} else {
							meaning.put(wordMeaningENArray[i], wordMeaningENArray[i+1]);
							meaning_tamil.put(wordMeaningTAArray[i],
									wordMeaningTAArray[i + 1]);
						}
					}
					wordDetailObject.setWord(wordMeaningENArray[1]);
					wordDetailObject.setWord_tamil(wordMeaningTAArray[1]);

					wordDetailObject.setMeaning_en(meaning);
					wordDetailObject.setMeaning_ta(meaning_tamil);

					Gson gson = new Gson();
					// System.out.println(gson.toJson(wordDetailObject).toString());
					writer.write(gson.toJson(wordDetailObject).toString());
					writer.write("\n");
					wordMeaningENString = "";
					wordMeaningTAString = "";

				} else {
					wordMeaningENString = wordMeaningENString + ";"
							+ FirstCurrentLine;
					wordMeaningTAString = wordMeaningTAString + ";"
							+ SecondCurrentLine;
				}
				// storeMeaning.put(sCurrentLine.split(" ")[0], sCurrentLine);

				// System.out.println(sCurrentLine);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br1 != null)
					br1.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	private static HashSet<String> GetSynonyms(String word, MeaningObject wordDetailObject) {
		HashSet<String> synonyms = new HashSet<String>();

		String str = Words.get(word);//
		if (str != null && !str.equals("")) {
			str = str.replace("'", "").replace("\"", "");

			String[] split = str.split(" ");
			switch(split[1]){
			case "n":
				wordDetailObject.setPart_of_speech("noun");
				break;
			case "a":
				wordDetailObject.setPart_of_speech("adjective");
				break;
			case "r":
				wordDetailObject.setPart_of_speech("adverb");
				break;
			case "v":
				wordDetailObject.setPart_of_speech("verb");
				break;
			default :
				wordDetailObject.setPart_of_speech("");
				break;	
			}
			int numberOfDescription = Integer.parseInt(split[2]);
			int skip = Integer.parseInt(split[3]);
			// System.out.println(numberOfDescription);

			// HashSet<String> synonyms = new HashSet<String>();

			for (int i = 0; i < numberOfDescription; i++) {
				String key = split[5 + i + skip + 1];
				String CurrentLine;

				// System.out.println(key);
				CurrentLine = storeMeaning.get(key);//
				// System.out.println(CurrentLine);
				CurrentLine = CurrentLine.replace(".", "").replace("'", "")
						.replace("_", "").replace("\"", "");
				String[] sentence_split = CurrentLine.split("[|]");
				String[] synonym_split = sentence_split[0].split(" ");
				if (key.equals(synonym_split[0])) {
					// System.out.println();
					int numberOfSynonym = hextoint(synonym_split[3]);
					for (int j = 0; j < numberOfSynonym * 2; j += 2) {
						if (synonym_split[4 + j].equals(word)) {
							continue;
						}
						synonyms.add(synonym_split[4 + j]);
						// OHT.setSrcString(synonym_split[4+j]);
						// synonyms_tamil.add(OHT.getTranslatedText());
					}
				}
			}

			/*
			 * System.out.println(synonyms); System.out.println(meaning);
			 * System.out.println(synonyms_tamil);
			 * System.out.println(meaning_tamil);
			 */
			/*
			 * meaningObject.setWord(word);
			 * 
			 * meaningObject.setSysnonym_en(synonyms);
			 * //meaningObject.setSysnonym_ta(synonyms_tamil);
			 * meaningObject.setPart_of_speech("adv");
			 * 
			 * meaningObject.setMeaning_en(meaning);
			 * meaningObject.setMeaning_ta(meaning_tamil);
			 * 
			 * Gson gson = new Gson();
			 * System.out.println(gson.toJson(meaningObject).toString());
			 * writer.write(gson.toJson(meaningObject).toString());
			 */
			// TODO Auto-generated method stub
		}
		return synonyms;

	}
}
