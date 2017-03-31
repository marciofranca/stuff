package com.google.challenges;

public class Answer {
	public static int answer(int total_lambs) {

		int geoN = findGeoN(total_lambs);
		int fiboN = findFiboN(total_lambs, geoN);

		return fiboN - geoN;
	}

	public static int findGeoS(int a, int r, int n) {
		int s;
		if (r == 1) {
			s = a * n;
		} else {
			s = (int) ((float) a * (1 - Math.pow(r, n)) / (1 - r));
		}

		return s;
	}

	public static int findGeoN(int s) {
		return (int) (Math.log(s + 1) / Math.log(2));
	}

	public static int fiboRec(int n) {
		if (n == 0)
			return 0;
		if (n == 1)
			return 1;
		return fiboRec(n - 1) + fiboRec(n - 2);
	}

	public static int fiboIte(int n) {
		if (n == 0)
			return 0;
		if (n == 1)
			return 1;

		int prevPrev = 0;
		int prev = 1;
		int result = 0;

		for (int i = 2; i <= n; i++) {
			result = prev + prevPrev;
			prevPrev = prev;
			prev = result;
		}
		return result;
	}

	public static int findFiboS(int n) {
		return fiboIte(n + 2) - 1;
	}

	public static int findFiboN(int s, int n) {
		while (findFiboS(n + 1) <= s) {
			n++;
		}
		return n;
	}

	public static void main(String[] args) {
		int s = 143;
		answer(s);

		// int n = 44;
		// System.out.println(fiboIte(n) + " " + fiboRec(n));

		// int n = 10;
		// System.out.println(findFiboS(n));
		//
		// int s = 143;
		// System.out.println(findFiboN(s, 0));

		//
		// for (int i = (int) Math.pow(10, 9); i > 0; i--) {
		// n = findFiboN(i, 0);
		// System.out.println(n);
		// }

	}
}