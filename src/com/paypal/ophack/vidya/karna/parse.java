package com.paypal.ophack.vidya.karna;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class parse {
	
	static Map<String, String> storeMeaning = new HashMap<String, String>();
	static Map<String, String> Words = new HashMap<String, String>();
	
	static int hextoint(String hex){
		int id = 0;
		for (int h = 0; h < hex.length(); h= h+2){
			String sub = hex.subSequence(h, h+2).toString();
			if (id == 0)	id = Integer.valueOf(sub, 16);
			else	id *= Integer.valueOf(sub, 16);             
		}
		return id;
	}
	
	
	static boolean parsefile(String str, Writer writer){
		//str = str.replace(".", "").replace("'", "").replace("_", "").replace("\"", "");
		MeaningObject meaningObject = new MeaningObject();
		Map<String, ArrayList<String>> synonyms_map = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> meaning_map = new HashMap<String, ArrayList<String>>();
		OphackTranslator OHT = new OphackTranslator();
		OHT.setSrcLangCode("en");
		OHT.setResLangCode("ta");
		
		//System.out.println(str);
		
		
		
		
		
		String[] split = str.split(" ");
		String word = split[0];
		int numberOfDescription = Integer.parseInt(split[2]);
		int skip = Integer.parseInt(split[3]);
		//System.out.println(numberOfDescription);
		
		Set<String> synonyms = new HashSet<String>();
		Map<String, String> meaning = new HashMap<String, String>();
		Map<String, String> meaning_tamil = new HashMap<String, String>();

		//Set<String> synonyms_tamil = new HashSet<String>();
		BufferedReader br = null;
		
		
		try {
			
			OHT.setSrcString(word);
			String word_tamil = OHT.getTranslatedText();

			
			for(int i = 0; i < numberOfDescription ; i++){
			String key = split[5+i+skip+1];
			String CurrentLine;
					
					CurrentLine = storeMeaning.get(key);//
					CurrentLine = CurrentLine.replace(".", "").replace("'", "").replace("_", "").replace("\"", "");
					String[] sentence_split = CurrentLine.split("[|]");
					String[] synonym_split = sentence_split[0].split(" ");
					if(key.equals(synonym_split[0])){
						//System.out.println();
						int numberOfSynonym = hextoint(synonym_split[3]);
						for (int j = 0; j < numberOfSynonym*2 ; j+=2){
							if(synonym_split[4+j].equals(word)){
								continue;
							}
							synonyms.add(synonym_split[4+j]);
							//OHT.setSrcString(synonym_split[4+j]);
							//synonyms_tamil.add(OHT.getTranslatedText());
						}
						String[] meaning_sentence = sentence_split[1].split(";");
						String tamil_meaning="";
						if(meaning_sentence.length==1){
							meaning.put(meaning_sentence[0], "");
						
							OHT.setSrcString(meaning_sentence[0]);
							tamil_meaning = OHT.getTranslatedText();
							
							meaning_tamil.put(tamil_meaning, "");
						}
						if(meaning_sentence.length==2){
							meaning.put(meaning_sentence[0], meaning_sentence[1]);
						
							OHT.setSrcString(meaning_sentence[0]);
							tamil_meaning = OHT.getTranslatedText();
							OHT.setSrcString(meaning_sentence[1]);
							String tamil_sentence = OHT.getTranslatedText();
						
							meaning_tamil.put(tamil_meaning, tamil_sentence);
						}
						if(tamil_meaning.contains("ERROR OCCURED WHILE FETCHING")){
							System.out.println("ab lagg gyi");
							return false;
						}
					}
					//System.out.println("kamal");
					
				
				//System.out.println("kamal");
			}/*
				System.out.println(synonyms);
				System.out.println(meaning);
				System.out.println(synonyms_tamil);
				System.out.println(meaning_tamil);*/
				meaningObject.setWord(word);
				meaningObject.setWord_tamil(word_tamil);
				
				meaningObject.setSysnonym_en(synonyms);
				//meaningObject.setSysnonym_ta(synonyms_tamil);
				meaningObject.setPart_of_speech("adv");

				meaningObject.setMeaning_en(meaning);
				meaningObject.setMeaning_ta(meaning_tamil);				
			
				Gson gson = new Gson();
				//System.out.println(gson.toJson(meaningObject).toString());
			    writer.write(gson.toJson(meaningObject).toString());
			    writer.write("\n");
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				
			}
			}
		return true;
		}
	
	
	public static void main(String[] args) throws InterruptedException {
		 
		BufferedReader br = null;
		Writer writer = null;

		try {
			 
			  String sCurrentLine;
	 
				br = new BufferedReader(new FileReader("C:/Users/kthethi/Workspace_Vella_DT/Karna_Vidya/src/data_all.txt"));
	 
				while ((sCurrentLine = br.readLine()) != null) {
					storeMeaning.put(sCurrentLine.split(" ")[0], sCurrentLine);
					
					//System.out.println(sCurrentLine);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		try {
			 
			  String sCurrentLine;
	 
				br = new BufferedReader(new FileReader("C:/Users/kthethi/Workspace_Vella_DT/Karna_Vidya/src/index_all.txt"));
	 
				while ((sCurrentLine = br.readLine()) != null) {
					Words.put(sCurrentLine.split(" ")[0], sCurrentLine);
					
					System.out.println(sCurrentLine);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		try {
 
		    writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream("5000_meaning.txt", true), "utf-8"));
		
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("C:/Users/kthethi/Workspace_Vella_DT/Karna_Vidya/src/5000.txt"));
 
			//int i =1;
			while ((sCurrentLine = br.readLine()) != null) {
				String s = sCurrentLine;
					if(!parsefile(sCurrentLine, writer)){
						break;
					}
				//	i++;
				//}
				//System.out.println(sCurrentLine);
			}
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
 
	}
}
