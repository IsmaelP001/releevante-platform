// export interface Book {
//   isbn: string;
//   correlationId: string;
//   translationId: string;
//   title: string;
//   price: number;
//   qty: number;
//   qtyForSale: number;
//   description: string;
//   descriptionFr: string;
//   descriptionSp: string;
//   author: string;
//   language: string;
//   createdAt: string;
//   updatedAt: string;
//   printLength: number;
//   publishDate: string;
//   dimensions: string;
//   publisher: string;
//   publicIsbn: string;
//   bindingType: string;
//   rating: number;
//   votes: number;
//   images?: Images[];
//   tags?: Tags[];
// }

export interface Book {
  isbn: string;
  translationId: string;
  title: string;
  author: string;
  image: string;
  votes: number;
  rating: number;
}

export interface BookDetails {
  isbn: string;
  correlationId: string;
  translationId: string;
  title: string;
  price: number;
  qty: number;
  qtyForSale: number;
  description: string;
  descriptionFr: string;
  descriptionSp: string;
  dimensions:string
  author: string;
  language: string;
  createdAt: string; 
  updatedAt: string; 
  printLength: number;
  publishDate: string; 
  publisher: string;
  publicIsbn: string;
  bindingType: string;
  rating: number;
  votes: number;
  image: string;
}


// interface Tags {
//   id: string;
//   isbn: string;
//   bookTagId: string;
//   name: string;
//   value: string;
//   valueFr: string;
//   valueSp: string;
//   createdAt: string;
// }

// interface Images {
//   id: string;
//   isbn: string;
//   url: string;
//   sourceUrl: string;
// }

export type SubCategory = {
  id: string;
  en: string;
  fr: string;
  es: string;
};

export interface SubCategoryRelation {
  id: string;
  bookRelations: string[];
}


export interface Category {
  id: string;
  en: string;
  fr: string;
  es: string;
  subCategoryRelations: SubCategoryRelation[];
}

export type SubCategoryMap=Record<string, SubCategory>

export interface CategoryQuery{
  categories:Category[],
  subCategoryMap:SubCategoryMap
}