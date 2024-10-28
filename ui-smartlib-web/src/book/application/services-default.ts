import { BookCategory, Book, BookEdition, BookCopy } from "../domain/models";
import { BookRepository } from "../domain/repositories";
import { SearchCriteria } from "./dto";
import { BookService } from "./services";

class DefailtBookServiceImpl implements BookService {
  constructor(private bookRepository: BookRepository) {}
    findBookEdition(edition: { id: string; }): Promise<BookEdition> {
        throw new Error("Method not implemented.");
    }

  findAllBookCategory(): Promise<BookCategory[]> {
    throw new Error("Method not implemented.");
  }

  findAllBookByCategory(category: BookCategory): Promise<Book[]> {
    throw new Error("Method not implemented.");
  }

  async findAllBookCopiesAvailableByEdition(
    bookEdition: BookEdition
  ): Promise<BookCopy[]> {
    return this.bookRepository.findAllBookCopiesAvailable(bookEdition);
  }

  findAllBookCopiesAvailableLocallyByEdition(
    bookEdition: BookEdition
  ): Promise<BookCopy[]> {
    throw new Error("Method not implemented.");
  }

  findAllBookBySearchCriteria(searchCriteria: SearchCriteria): Promise<Book[]> {
    throw new Error("Method not implemented.");
  }
}
