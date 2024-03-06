package graph.project;


/**
 * SL-light
 *
 * En av de tjänster som finns på SL.se är en reseplanerare. Man skriver in varifrån man vill åka och var man vill åka till och sen räknar applikationen ut den bästa resvägen. Den här uppgiften går ut på att implementera en förenklad version av denna tjänst.
 *
 * För betyget G ska du definiera, implementera och dokumentera en lämplig datastruktur för denna typ av tjänst. Naturligtvis är det frågan om en viktad graf, men det räcker inte med att ha med vikten mellan noderna (hållplatser och stationer) utan du måste ju hålla reda på när till exempel en buss går och vilken buss det är. Varje hållplats och station ska också ha kartkoordinater. Grafen som byggs upp ska vara realistisk, så den ska klara riktiga tidtabeller. Du får alltså inte göra alltför stora förenklingar.
 *
 * Obs! Det finns inget krav att implementera en sökalgoritm för betyget G, det räcker med datastrukturen.
 *
 * För betyget VG ska du också implementera en heuristisk sökalgoritm (A* eller motsvarande) som givet starttiden, startpunkten och slutpunkten hittar den bästa färdvägen. Vill du göra uppgiften mer realistisk så kan du dessutom testa att lägga in ytterligare funktionalitet som att man kan gå mellan hållplatser eller att man försöker minimera antalet byten om det inte påverkar restiden för mycket.
 *
 * Data för SL:s tidtabeller finns tillgängligt här i ilearn. För VG ska det fungera med en graf av den storleken. (Precis som för Bacon-talen är tidtabellen några år gammal, men det spelar naturligtvis ingen roll för uppgiften.)
 */
public class SLlight {
}
