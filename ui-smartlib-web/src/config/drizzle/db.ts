import { drizzle } from 'drizzle-orm/better-sqlite3';
import Database from 'better-sqlite3';
import * as schema from './schemas';

const sqlite = new Database('sqlite-dev.db');

export const db = drizzle({ client: sqlite ,schema});