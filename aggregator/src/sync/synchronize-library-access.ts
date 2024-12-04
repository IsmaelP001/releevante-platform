import { dbConnection } from "../config/db.js";
import { executeGet } from "../htttp-client/http-client.js";
import { ApiRequest } from "../htttp-client/model.js";
import { LibraryAccess } from "../model/client.js";

const slid = process.env.slid;

export const synchronizeLibraryAccess = async (token: string) => {
  let totalRecordsSynced = 0;
  const request: ApiRequest = {
    resource: `sl/${slid}/accesses?status=not_synced`,
    token,
  };
  const response = await executeGet<LibraryAccess[]>(request);
  const accesses = response.context.data;
  if (accesses && accesses.length) {
    totalRecordsSynced += await insertUsers(accesses);
  }
  console.log("TOTAL USER RECORDS SYNCHRONIZED: " + totalRecordsSynced);
  return totalRecordsSynced;
};

const insertUsers = async (accesses: LibraryAccess[]) => {
  const stmt = dbConnection.prepare(
    "INSERT INTO user VALUES (@id, @access_id, @credential, @is_active, @expires_at, @created_at, @updated_at)"
  );
  let dbChanges = 0;

  accesses
    .filter((item) => !item.isSync)
    .forEach((access) => {
      try {
        dbChanges += stmt.run({
          id: access.userId,
          access_id: access.id,
          credential: access.credential.value,
          is_active: (access.isActive && 1) || 0,
          expires_at: access.expiresAt,
          created_at: access.createdAt,
          updated_at: access.createdAt,
        }).changes;
      } catch (error: any) {
        console.log(`skipping error in insertUsers and continue processing ....${error.message}`);
      }
    });

  return dbChanges;
};
