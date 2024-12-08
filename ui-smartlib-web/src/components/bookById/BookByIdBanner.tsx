"use client";
import React from "react";
import SelectLanguage from "./SelectLanguage";
import Rating from "../Rating";
import Image from "next/image";
import { Book } from "@/book/domain/models";
import { useLocale, useTranslations } from "next-intl";

interface BookByIdBannerProps {
  book: Book;
}
export default function BookByIdBanner({ book }: BookByIdBannerProps) {
  const locale = useLocale();
  const t = useTranslations("bookById");

  return (
    <div className="flex gap-5 p-3 rounded-md m-auto bg-white px-5 py-10">
      <div>
        <Image
          src={book?.images?.length ? book.images[0].url : ""}
          width={300}
          height={300}
          className="w-[240px] h-[280px] rounded-xl object-cover bg-gray-200"
          alt={`${book?.bookTitle} book image`}
        />
      </div>
      <div className="space-y-4 flex-1">
        <div className="flex gap-2 mb-5">
          <p className="bg-primary py-1 px-2 rounded-sm text-white text-sm font-medium">
            <span>{book.category?.[`${locale}Category`]}</span>
          </p>
          <p className="bg-secondary py-1 px-2 rounded-sm  text-sm font-medium">
            Semi-used
          </p>
        </div>
        <div className="space-y-1">
          <h2 className="text-4xl font-semibold">{book?.bookTitle}</h2>
          <p className="font-medium text-2xl text-secondary-foreground">
            {book?.author}
          </p>
        </div>
        <div className="flex gap-2 items-center">
          <Rating rating={book?.rating || 0} />
          <p className="text-secondary-foreground text-sm">{book?.rating}</p>
          <p className="text-secondary-foreground text-sm">
            ({book?.votes} votes)
          </p>
        </div>
        <div className="border-t border-secondary pt-3">
          <h4 className="font-medium mb-1">{t("selectLanguage")}</h4>
          <SelectLanguage booklanguages={book?.bookLanguages} />
        </div>
      </div>
    </div>
  );
}
