package hashtables;

public class LinearProbingHashTable<T> extends ProbingHashTable<T> {

	/*
	 * Denna metod ska skrivas klart. Den ska använda linjär sondering och hela tiden öka med ett.
	 */
	@Override
	protected int findPos(T x) {

		int offset = 0;
		int currentPos = myhash(x);

		while (continueProbing(currentPos,x)){
			currentPos = myhash(x) + f(offset++);
			if (currentPos >= capacity())
				currentPos -= capacity();
		}

		return currentPos;
	}

	private int f(int i){
		return i;
	}

}
