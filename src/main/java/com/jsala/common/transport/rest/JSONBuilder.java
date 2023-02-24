/**
 * Class JSONBuilder
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: Use this when you don't have an object to put your arguments into to serialize or JSONify
 */

package com.jsala.common.transport.rest;

public class JSONBuilder {
	String jsonString;
	String delimiter;

	// CONSTRUCTORS
	public JSONBuilder() {
		this.jsonString = "{";
		this.delimiter = "";
	}
	public JSONBuilder(String str) {
		this.jsonString = str;
		this.delimiter = ",";
	}
	// METHODS
	private String addKey(String key) {
		return this.delimiter + "\"" + key + "\":";
	}
	private String addStringValue(String value) {
		return "\"" + value + "\"";
	}
	public JSONBuilder addKeyValue(String key, String value) {
		return new JSONBuilder(this.jsonString + this.addKey(key) + "\"" + value + "\"");
	}
	public JSONBuilder addKeyValue(String key, int value) {
		return new JSONBuilder(this.jsonString + this.addKey(key) + value);
	}
	public JSONBuilder addKeyValue(String key, double value) {
		return new JSONBuilder(this.jsonString + this.addKey(key) + value);
	}
	public JSONBuilder addKeyValue(String key, float value) {
		return new JSONBuilder(this.jsonString + this.addKey(key) + value);
	}
	public JSONBuilder addStringArray(String key, String[] values) {
		String addition = this.addKey(key) + "[";
		String delimiter = "";
		for(String val : values) {
			addition += delimiter + val;
		}
		return new JSONBuilder(this.jsonString + addition + "]");
	}
	public String build() {
		return this.jsonString + "}";
	}

	@Override
	public String toString() {
		return this.jsonString;
	}
}
