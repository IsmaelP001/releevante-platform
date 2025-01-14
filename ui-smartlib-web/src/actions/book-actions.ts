"use server";

import { bookServiceFacade } from "@/book/application";
import {
  BookByFtagsVibes,
  BooksPagination,
  FtagsEnum,
} from "@/book/domain/models";
import { unstable_cache } from "next/cache";

export async function FetchAllBookCategories() {
  try {
    return await bookServiceFacade.findAllBookCategory();
  } catch (error) {
    throw new Error("Error fetching all books categories" + error);
  }
}

export async function FetchAllBookBySearchCriteria(searchParam: string) {
  try {
    return await bookServiceFacade.findAllBookBySearchCriteria(searchParam);
  } catch (error) {
    throw new Error("Error fetching books by seach criteria" + error);
  }
}

export async function FetchAllBooks({ limit }: { limit?: number }) {
  try {
    return await bookServiceFacade.findAllBooks({ limit: limit || 20 });
  } catch (error) {
    throw new Error("Error fetching ftags " + error);
  }
}

export async function FetchBookById(isbn: string) {
  try {
    return await bookServiceFacade.findBookById(isbn);
  } catch (error) {
    throw new Error("Error fetching book by id" + error);
  }
}

export async function FetchAllBookByCategory() {
  try {
    return await bookServiceFacade.findAllBookByCategory();
  } catch (error) {
    throw new Error("Error fetching books by category" + error);
  }
}

export const LoanLibraryInventory = unstable_cache(
  async function (pagination: BooksPagination) {
    try {
      return await bookServiceFacade.loanLibraryInventory();
    } catch (error) {
      throw new Error("Error to load library inventory" + error);
    }
  },['library_inventory'],{
    revalidate:900
  }
);

export async function FetchFtagsBy(tagName: FtagsEnum) {
  try {
    return await bookServiceFacade.getFtagsByType(tagName);
  } catch (error) {
    throw new Error("Error fetching books by category" + error);
  }
}

export async function FetchBookByFtagsVibes(tagNames: BookByFtagsVibes) {
  try {
    return await bookServiceFacade.findBooksByVibeTags(tagNames);
  } catch (error) {
    throw new Error("Error fetching books by category" + error);
  }
}
