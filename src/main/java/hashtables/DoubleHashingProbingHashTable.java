package hashtables;

public class DoubleHashingProbingHashTable<T> extends ProbingHashTable<T> {

	/*
	 * Denna metod ska skrivas klart. Den ska använda bokens förslag på andra
	 * hashalgoritm: f(i) = i * hash2(x), där hash2(x) = R - (x mod R) och R är
	 * ett primtal mindre än tabellstorleken.
	 */

	private final int R = smallerPrimeThanCapacity();

	@Override
	protected int findPos(T x) {

		int currentPos = myhash(x);
		int offset = 0;

		while (continueProbing(currentPos,x)){
			currentPos = myhash(x) + f(offset++,myhash(x));
			if (currentPos >= capacity()) {
				currentPos %= capacity();
			}
		}

		return currentPos;
	}

	private int f(int i, int x){
		return i * hash2(x);
	}

	private int hash2(int x){
		return R - (x % R);
	}

	/*
	 * Denna metod ger ett primtal mindre än tabellens storlek. Detta primtal ska
	 * användas i metoden ovan.
	 */
	protected int smallerPrimeThanCapacity() {
		int n = capacity() - 2;
		while (!isPrime(n)) {
			n -= 2;
		}
		return n;
	}

}
