package foo.bar.roman;

import java.util.Arrays;
import java.util.List;

public class RomanNumerals {

	public String convert(int number) {
		List<String> pairs = Arrays.asList("M,1000", "D,500", "C,100", "L,50", "X,10", "V,5", "I,1");
		String res = "";
		String[] pair;
		for (String s : pairs) {
			pair = s.split(",");
			int val = Integer.parseInt(pair[1]);
			while (number >= val) {
				res += pair[0];
				number -= val;
				continue;
			}

		}
		return reduce(res);
	}

	private String reduce(String roman) {
		List<String> pairs = Arrays.asList("DCCCC,CM", "CCCC,CD", "LXXXX,XC", "XXXX,XL", "VIIII,IX", "IIII,IV");
		String[] pair;
		for (String s : pairs) {
			pair = s.split(",");
			roman = roman.replace(pair[0], pair[1]);
		}
		return roman;
	}
}
