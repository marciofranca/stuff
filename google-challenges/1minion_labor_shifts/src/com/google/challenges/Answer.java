package com.google.challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Answer {
	public static int[] answer(int[] data, int n) {
		Map<Integer, Integer> count = new HashMap<Integer, Integer>();

		for (int i = 0; i < data.length; i++) {
			int key = data[i];

			int value;
			if (count.containsKey(key)) {
				value = count.get(key);
			} else {
				value = 0;
			}
			count.put(key, value + 1);
		}

		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < data.length; i++) {
			if (count.get(data[i]) < n + 1) {
				result.add(data[i]);
			}
		}
		return toPrimitive(result);
	}

	private static int[] toPrimitive(List<Integer> list) {
		int[] result = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null) {
				result[i] = list.get(i);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		/*
		 * int[] data = { 5, 10, 15, 10, 7 }; int n = 1; int[] data = { 1, 2,
		 * 3}; int n = 0; int[] data = { 1, 2, 2, 3, 3, 3, 4, 5, 5}; int n = 1;
		 * int[] data = { 1, 2, 3 }; int n = 6;
		 */

		int[] data = { 1, 2, 2, 3, 3, 3, 4, 5, 5, 100, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE };
		int n = 1;

		int[] result = answer(data, n);

		System.out.println(Arrays.toString(result));

	}
}