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
	
	static int hextoint(String hex){
		int id = 0;
		for (int h = 0; h < hex.length(); h= h+2){
			String sub = hex.subSequence(h, h+2).toString();
			if (id == 0)	id = Integer.valueOf(sub, 16);
			else	id *= Integer.valueOf(sub, 16);             
		}
		return id;
	}
	
	
	static void parsefile(String str, Writer writer){
		str = str.replace(".", "").replace("'", "").replace("_", "").replace("\"", "");
		MeaningObject meaningObject = new MeaningObject();
		Map<String, ArrayList<String>> synonyms_map = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> meaning_map = new HashMap<String, ArrayList<String>>();
		OphackTranslator OHT = new OphackTranslator();
		OHT.setSrcLangCode("en");
		OHT.setResLangCode("ta");
		
		System.out.println(str);
		
		
		
		
		
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
			
			for(int i = 0; i < numberOfDescription+1 ; i++){
			String key = split[5+i+skip];
			String sCurrentLine;
			br = new BufferedReader(new FileReader("C:/Users/kthethi/Workspace_Vella_DT/Dictionary/src/data.adv"));
				//ArrayList<String> meaning = new ArrayList<String>();
				while ((sCurrentLine = br.readLine()) != null) {
					sCurrentLine = sCurrentLine.replace(".", "").replace("'", "").replace("_", "").replace("\"", "");
					String[] sentence_split = sCurrentLine.split("[|]");
					String[] synonym_split = sentence_split[0].split(" ");
					if(key.equals(synonym_split[0])){
						System.out.println();
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
						if(meaning_sentence.length==1){
							meaning.put(meaning_sentence[0], "");
						
							OHT.setSrcString(meaning_sentence[0]);
							String tamil1 = OHT.getTranslatedText();
							
							meaning_tamil.put(tamil1, "");
						}
						if(meaning_sentence.length==2){
							meaning.put(meaning_sentence[0], meaning_sentence[1]);
						
							OHT.setSrcString(meaning_sentence[0]);
							String tamil1 = OHT.getTranslatedText();
							OHT.setSrcString(meaning_sentence[1]);
							String tamil2 = OHT.getTranslatedText();
						
							meaning_tamil.put(tamil1, tamil2);
						}
					}
					//System.out.println("kamal");
					
				}
				//System.out.println("kamal");
			}/*
				System.out.println(synonyms);
				System.out.println(meaning);
				System.out.println(synonyms_tamil);
				System.out.println(meaning_tamil);*/
				meaningObject.setWord(word);
				meaningObject.setSysnonym_en(synonyms);
				//meaningObject.setSysnonym_ta(synonyms_tamil);
				meaningObject.setPart_of_speech("adv");

				meaningObject.setMeaning_en(meaning);
				meaningObject.setMeaning_ta(meaning_tamil);				
			
				Gson gson = new Gson();
				System.out.println(gson.toJson(meaningObject).toString());
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
		}
	
	
	public static void main(String[] args) {
		 
		BufferedReader br = null;
		Writer writer = null;

		
		try {
 
		    writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream("WordMeaning.txt"), "utf-8"));
		
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("C:/Users/kthethi/Workspace_Vella_DT/Dictionary/src/index1.adv"));
 
			while ((sCurrentLine = br.readLine()) != null) {
				parsefile(sCurrentLine, writer);
				
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
