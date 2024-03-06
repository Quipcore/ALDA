package hashtables;

import java.util.Arrays;

public class MyString {

	private char[] data;
	
	public MyString(String title) {
		data = title.toCharArray();
	}

	public Object length() {
		return data.length;
	}
	
	@Override
	public String toString() {
		return new String(data);
	}


	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;

		MyString myString = (MyString) object;

        return Arrays.equals(data, myString.data);
    }

	//Created with help from PROG2 slides
	@Override
	public int hashCode() {
		int hash = 17;

		for(char c : data){
			hash = hash * 29 + (int) c;
		}

		return hash;
	}
}
