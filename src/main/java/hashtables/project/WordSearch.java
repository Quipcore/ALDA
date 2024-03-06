package hashtables.project;

/**
 * Detta är uppgift 5.18 från kursboken, där man ska implementera en lösning på ordpusselspelet. För VG måste man få med åtminstone två av utökningarna..
 */


/**
 * a. Implement the word puzzle program using the algorithm described at the end
 * of the chapter.
 *
 * b. We can get a big speed increase by storing, in addition to each word W, all of
 * W’s prefixes. (If one of W’s prefixes is another word in the dictionary, it is stored
 * as a real word.) Although this may seem to increase the size of the hash table
 * drastically, it does not, because many words have the same prefixes. When a scan
 * is performed in a particular direction, if the word that is looked up is not even
 * in the hash table as a prefix, then the scan in that direction can be terminated
 * early. Use this idea to write an improved program to solve the word puzzle.
 *
 * c. If we are willing to sacrifice the sanctity of the hash table ADT, we can speed up
 * the program in part (b) by noting that if, for example, we have just computed
 * the hash function for “excel,” we do not need to compute the hash function for
 * “excels” from scratch. Adjust your hash function so that it can take advantage of
 * its previous calculation.
 *
 * d. In Chapter 2, we suggested using binary search. Incorporate the idea of using
 * prefixes into your binary search algorithm. The modification should be simple.
 * Which algorithm is faster?
 */
public class WordSearch {
}
