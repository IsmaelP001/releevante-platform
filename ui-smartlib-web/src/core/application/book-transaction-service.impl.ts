import { BookServiceFacade } from "@/book/application/service.definitions";
import { Cart } from "../domain/cart.model";
import {
  BookTransaction,
  BookTransactionItem,
  BookTransactionItemStatus,
  BookTransactions,
  BookTransactionStatus,
  TransactionStatusEnum,
  TransactionType,
} from "../domain/loan.model";
import { BookTransactionService } from "./service.definition";
import { v4 as uuidv4 } from "uuid";
import { BookTransactionRepository } from "../domain/repositories";
import { UserId } from "@/identity/domain/models";
import { SettingsFacade } from "./settings.facade";
import { MaxBookItemThresholdExceeded } from "@/errors/custom-errors";

export class DefaultBookTransactionService implements BookTransactionService {
  constructor(
    private bookTransactionRepository: BookTransactionRepository,
    private bookService: BookServiceFacade,
    private librarySettingsService: SettingsFacade
  ) {}

  newTransactionStatus(
    status: BookTransactionStatus
  ): Promise<BookTransactionStatus> {
    return this.bookTransactionRepository.newTransactionStatus(status);
  }

  newTransactionItemStatus(
    status: BookTransactionItemStatus
  ): Promise<BookTransactionItemStatus> {
    return this.bookTransactionRepository.newTransactionItemStatus(status);
  }

  async checkout(cart: Cart): Promise<BookTransactions> {
    const bookCopySearch = cart.cartItems.map(
      ({ isbn, qty, transactionType }) => ({
        isbn,
        qty,
        transactionType,
      })
    );

    const loanItems: BookTransactionItem[] = (
      await this.bookService.findAvailableCopiesByIsbnForRent(
        bookCopySearch.filter(
          (item) => item.transactionType == TransactionType.RENT
        )
      )
    ).map(({ book_isbn, id, at_position }) => ({
      id: uuidv4(),
      isbn: book_isbn,
      cpy: id,
      position: at_position,
    }));

    const transactions: BookTransactions = {};

    const currentDate = new Date().toISOString();

    if (loanItems.length) {
      // check if user has a pending loan
      // if so check is current loan items + this loan items <= 4;

      const currentLoans = await this.getUserLoans(cart.userId);

      if (currentLoans?.length) {
        const allItems = currentLoans.flatMap((loan) => loan.items);

        if (allItems.length) {
          const librarySetting =
            await this.librarySettingsService.getLibrarySetting();

          const isMaxBookThresholdExceeded =
            allItems.length + loanItems.length > librarySetting.maxBooksPerLoan;

          if (isMaxBookThresholdExceeded) {
            throw new MaxBookItemThresholdExceeded(currentLoans);
          }
        }
      }

      const loanId = uuidv4();
      const loanStatus: BookTransactionStatus = {
        id: uuidv4(),
        transactionId: loanId,
        status: TransactionStatusEnum.PENDING,
        createdAt: currentDate,
      };

      const bookLoan: BookTransaction = {
        id: loanId,
        clientId: cart.userId,
        items: loanItems,
        createdAt: currentDate,
        transactionType: TransactionType.RENT,
        returnsAt: currentDate,
        status: [loanStatus],
      };

      transactions["rent"] = bookLoan;
    }

    const purchasedItems = (
      await this.bookService.findAvailableCopiesByIsbnForPurchase(
        bookCopySearch.filter((item) => item.transactionType == "PURCHASE")
      )
    ).map(({ book_isbn, id, at_position }) => ({
      id: uuidv4(),
      isbn: book_isbn,
      cpy: id,
      position: at_position,
    }));

    if (purchasedItems.length) {
      const purchaseId = uuidv4();
      const purchaseStatus: BookTransactionStatus = {
        id: uuidv4(),
        transactionId: purchaseId,
        status: TransactionStatusEnum.PENDING,
        createdAt: currentDate,
      };

      const bookPurchase: BookTransaction = {
        id: purchaseId,
        clientId: cart.userId,
        items: purchasedItems,
        createdAt: currentDate,
        transactionType: TransactionType.PURCHASE,
        status: [purchaseStatus],
      };

      transactions["purchase"] = bookPurchase;
    }

    const itemsCount = Object.keys(transactions)
      .map((key) => transactions[key].items.length)
      .reduce((a, b) => a + b, 0);

    if (itemsCount !== cart.cartItems.length) {
      throw new Error("failed to find all items selected");
    }

    await this.bookTransactionRepository.save(transactions);

    return transactions;
  }

  getUserLoans(clientId: UserId): Promise<BookTransaction[]> {
    return this.bookTransactionRepository.getUserLoans(clientId);
  }
}
