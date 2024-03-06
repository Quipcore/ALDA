package hashtables;

/*
 * Denna klass ska förberedas för att kunna användas som nyckel i en hashtabell. 
 * Du får göra nödvändiga ändringar även i klasserna MyString och ISBN10.
 * 
 * Hashkoden ska räknas ut på ett effektivt sätt och följa de regler och 
 * rekommendationer som finns för hur en hashkod ska konstrueras. Notera i en 
 * kommentar i koden hur du har tänkt när du konstruerat din hashkod.
 */
public class Book {
	private final MyString title;
	private final MyString author;
	private final ISBN10 isbn;
	private final MyString content;
	private int price;

	public Book(String title, String author, String isbn, String content, int price) {
		this.title = new MyString(title);
		this.author = new MyString(author);
		this.isbn = new ISBN10(isbn);
		this.content = new MyString(content);
		this.price = price;
	}

	public MyString getTitle() {
		return title;
	}

	public MyString getAuthor() {
		return author;
	}

	public ISBN10 getIsbn() {
		return isbn;
	}

	public MyString getContent() {
		return content;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return String.format("\"%s\" by %s Price: %d ISBN: %s lenght: %s", title, author, price, isbn,
				content.length());
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;

		Book book = (Book) object;

		if (price != book.price) return false;
		if (!title.equals(book.title)) return false;
		if (!author.equals(book.author)) return false;
		if (!isbn.equals(book.isbn)) return false;
        return content.equals(book.content);
    }

	//Created with help from PROG2 slides
	@Override
	public int hashCode() {
		int hash = 11;

		hash = hash * 31 + hashObject(title);
		hash = hash * 31 + hashObject(author);
		hash = hash * 31 + hashObject(isbn);
		hash = hash * 31 + hashObject(content);

		if(hash < 0){
			hash *= -1;
		}

		System.out.println(hash);

		return hash;
	}

	//Taken more or less from PROG2 Lecture slide
	private int hashObject(Object object) {
		return object == null ? 0 : object.hashCode();
	}
}
