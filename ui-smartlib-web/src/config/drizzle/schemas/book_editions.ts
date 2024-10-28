import {  sqliteTable, text } from "drizzle-orm/sqlite-core";
import { v4 as uuidv4 } from 'uuid';
import { relations } from "drizzle-orm";
import { bookImageSchema } from "./book_images";
import { bookCopieSchema } from "./book_copies";

export const bookEditionSchema = sqliteTable('books_edition',{
    id: text('id').primaryKey().$defaultFn(() => uuidv4()),
    name:text('name').notNull(),
})

export const bookEditionSchemaRelations = relations(bookEditionSchema, ({ many }) => ({
    images: many(bookImageSchema),
    copies: many(bookCopieSchema) 
}));