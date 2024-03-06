package hashtables;

import java.util.Arrays;

public class ISBN10 {

	private char[] isbn;

	public ISBN10(String isbn) {
		if (isbn.length() != 10)
			throw new IllegalArgumentException("Wrong length, must be 10");
		if (!checkDigit(isbn))
			throw new IllegalArgumentException("Not a valid isbn 10");
		this.isbn = isbn.toCharArray();
	}

	private boolean checkDigit(String isbn) {
		int sum = 0;
		for (int i = 0; i < 9; i++) {
			sum += Character.getNumericValue(isbn.charAt(i)) * (10 - i);
		}
		int checkDigit = (11 - (sum % 11)) % 11;

		return isbn.endsWith(checkDigit == 10 ? "X" : "" + checkDigit);
	}

	@Override
	public String toString() {
		return new String(isbn);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;

		ISBN10 isbn10 = (ISBN10) object;

        return Arrays.equals(isbn, isbn10.isbn);
    }

	//Created with help from PROG2 slides
	@Override
	public int hashCode() {

		int hash = 13;

		for(char c : isbn){
			hash = hash * 37 + (int) c;
		}

		return hash;
	}
}
