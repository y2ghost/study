package study.ywork.jpa.hello;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
class Book {
	@Id
	String isbn;

	@NotNull
	String title;

	Book() {
	}

	Book(String isbn, String title) {
		this.isbn = isbn;
		this.title = title;
	}
}