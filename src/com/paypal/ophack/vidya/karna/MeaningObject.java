package com.paypal.ophack.vidya.karna;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


public class MeaningObject {

	String word;
	Set<String> synonym_en;
	//Set<String> synonym_ta;
	String Part_of_speech;
	Map<String, String> meaning_en;
	Map<String, String> meaning_ta;
	
	public Set<String> getSysnonym_en() {
		return synonym_en;
	}
	public void setSysnonym_en(Set<String> sysnonym_en) {
		this.synonym_en = sysnonym_en;
	}
	public Map<String, String> getMeaning_en() {
		return meaning_en;
	}
	public void setMeaning_en(Map<String, String> meaning_en) {
		this.meaning_en = meaning_en;
	}
	public Map<String, String> getMeaning_ta() {
		return meaning_ta;
	}
	public void setMeaning_ta(Map<String, String> meaning_ta) {
		this.meaning_ta = meaning_ta;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getPart_of_speech() {
		return Part_of_speech;
	}
	public void setPart_of_speech(String part_of_speech) {
		Part_of_speech = part_of_speech;
	}
	
	
}
