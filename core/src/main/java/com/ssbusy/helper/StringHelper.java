package com.ssbusy.helper;

import java.util.Random;

public final class StringHelper {
	public static final Random rand = new Random();

	public static String randomNumber(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append('0' + rand.nextInt(10));
		}
		return sb.toString();
	}

}
