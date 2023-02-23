/**
 * Class TimingLog
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Use this to collect the times for buyers or sellers
 */

package com.jsala.common;

public class TimingLog {
	double[] times;
	public TimingLog(int numUsers) {
		times = new double[numUsers];
	}
}
