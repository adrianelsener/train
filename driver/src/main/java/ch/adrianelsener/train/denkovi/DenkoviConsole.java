package ch.adrianelsener.train.denkovi;

import ch.adrianelsener.train.denkovi.DenkoviWrapper.IpAddress;
import ch.adrianelsener.train.denkovi.DenkoviWrapper.Jp;

import com.adventnet.snmp.snmp2.SnmpAPI;

public class DenkoviConsole {

	public static void main(String[] args) {
		new DenkoviConsole(IpAddress.fromValue("172.16.100.2")).doTimesIn2(30);
	}

	public DenkoviConsole(IpAddress address) {
		a = new DenkoviWrapper(address);
	}

	private final Board a;

	private void doTimesIn2(int times) {

		long sleepduration = 50;
		for (int i = 0; i < times; i++) {
			System.out.println("Durchlauf " + i);
			System.out.println("Auf on Setzen");
			doOn();
			try {
				Thread.sleep(sleepduration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Auf off Setzen");
			doOff();
			try {
				Thread.sleep(sleepduration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		a.close();
	}

	private void doOff() {
		try {
			int test = 0;
			test = test & 253;
			a.SNMP_SET(161, Jp.P3, SnmpAPI.INTEGER, Integer.toString(test),
					"private");
			System.out.println("P3 changed !");
		} catch (NumberFormatException e1) {
			System.err.println("Error: Unable to connect with the target!");
		}

	}

	private void doOn() {
		try {
			int test = 0;
			test = test | 2;
			a.SNMP_SET(161, Jp.P3, SnmpAPI.INTEGER, Integer.toString(test),
					"private");
			System.out.println("P3 changed !");
		} catch (NumberFormatException e1) {
			System.err.println("Error: Unable to connect with the target!");
		}

	}

}
