package com.paypal.ophack.vidya.karna;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class WordMeaningStore {
	private HashMap<String, MeaningObject> wordMap;
	private static WordMeaningStore instance;
	
	public static WordMeaningStore getInstance(){
		if(instance == null){
			instance = new WordMeaningStore();
		}
		return instance;
	}
	
	private WordMeaningStore(){
		wordMap = new HashMap<String, MeaningObject>();
		BufferedReader br;
		String word;
		Gson gson = new Gson();
		MeaningObject wordObject;
		try {
			br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/WordMeaning.txt")));
			while ((word = br.readLine())!=null) {
				wordObject = gson.fromJson(word, MeaningObject.class);
				wordMap.put(wordObject.getWord(), wordObject);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public MeaningObject getMeaningObject(String word){
		return wordMap.get(word);
	}
}
