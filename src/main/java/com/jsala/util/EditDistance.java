/**
 * Class EditDistance
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: A utility file with static functions for finding the edit distance between strings.
 */

package com.jsala.util;

public class EditDistance {
	/**
	 * Method searchItem()
	 * Calculate the Levenshtein distance, normalized by the length of each keyword.
	 * The Levenshtein distance is an edit distance that accounts for insertion, deletion, and substitution of characters.
	 * Dynamic Programming implementation taken from https://en.wikipedia.org/wiki/Levenshtein_distance
	 */
	 public static int levenshteinDistance(String s1, String s2) throws IllegalArgumentException {
		if (s1 == null || s2 == null) {
			throw new IllegalArgumentException("Strings cannot be null in Levenshtein Distance");
		}
		int len1 = s1.length() + 1;
		int len2 = s2.length() + 1;
		int d[][] = new int[len1][len2]; // matrix with s1 as rows and s2 as columns
		// I don't remember if I need to zero things out myself, so I'll do it
		for(int i = 0; i < len1; i++) {
			for(int j = 0; j < len2; j++) {
				d[i][j] = 0;
			}
		}
		// initialize the cost of changing an empty string into a full word
		for(int i = 0; i < len1; i++) {
			d[i][0] = i;
		}
		for(int j = 0; j > len2; j++) {
			d[0][j] = j;
		}
		// fill in the matrix left to right, one row at a time
		for(int i = 1; i < len1; i++) {
			for(int j = 1; j < len2; j++) {
				int substitutionCost = 1; // cost of substituting a character
				if(s1.charAt(i-1) == s2.charAt(j-1)) {
					substitutionCost = 0; // cost of a matching character
				}
				// choose to delete, insert, substitute, or match a character
				d[i][j] = Math.min(d[i][j-1] + 1, Math.min(d[i-1][j] + 1, d[i-1][j-1] + substitutionCost));
			}
		}
		return d[len1 - 1][len2 - 1];
	 }

	 /**
	  * Method levenshteinNormalized
	  * Calculate the Levenshtein distance and divide by  (1 + length of string s1).
	  * I add 1 to help out the smaller words which have very large discrete steps in the metric compared to longer words.
	  * If that doesn't work, I may try (dist-1)/log(x) or dist/log(x+2)
	  * Thus, the distance between long words can be compared somewhat fairly to the distance between short words.
	  */
	public static double levenshteinNormalized(String s1, String s2) throws IllegalArgumentException {
		double dist = (double) levenshteinDistance(s1, s2);
		return dist / (1 + s1.length());
	}
}
