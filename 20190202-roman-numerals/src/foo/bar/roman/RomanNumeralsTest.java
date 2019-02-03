package foo.bar.roman;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RomanNumeralsTest {

	@Test
	void test123() {
		RomanNumerals roman = new RomanNumerals();
		assertEquals("CXXIII", roman.convert(123));
	}
	@Test
	void test46() {
		RomanNumerals roman = new RomanNumerals();
		assertEquals("XLVI", roman.convert(46));
	}

	@Test
	void test949() {
		RomanNumerals roman = new RomanNumerals();
		assertEquals("CMXLIX", roman.convert(949));
	}

	@Test
	void test494() {
		RomanNumerals roman = new RomanNumerals();
		assertEquals("CDXCIV", roman.convert(494));
	}

	
}
