/**
 * Class TimingLog
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Use this to collect the times for buyers or sellers
 */

package common;

public class TimingLog {
	double[] times;
	public TimingLog(int numUsers) {
		times = new double[numUsers];
	}
}
