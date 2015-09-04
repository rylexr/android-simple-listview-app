/*
 * Copyright 2015 Tinbytes Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tinbytes.simplelistviewapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class SimpleContentProvider extends ContentProvider {
  // Provide a mechanism to identify all the incoming uri patterns.
  private static final int ANIMALS = 1;
  private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    // /animal
    uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.AnimalTable.URI_PATH, ANIMALS);
  }

  private DatabaseHelper dh;

  public boolean onCreate() {
    dh = new DatabaseHelper(getContext());
    return true;
  }

  public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    Cursor c;
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setDistinct(true);
    switch (uriMatcher.match(uri)) {
      case ANIMALS:
        qb.setTables(DatabaseContract.AnimalTable.TABLE_NAME);
        if (sortOrder == null)
          sortOrder = DatabaseContract.AnimalColumns.NAME + " ASC";
        c = qb.query(dh.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    if (getContext() != null) {
      c.setNotificationUri(getContext().getContentResolver(), uri);
    }
    return c;
  }

  public Uri insert(@NonNull Uri uri, ContentValues values) {
    if (values != null) {
      switch (uriMatcher.match(uri)) {
        case ANIMALS:
          SQLiteDatabase db = dh.getWritableDatabase();
          long rowId = db.insert(DatabaseContract.AnimalTable.TABLE_NAME, null, values);
          if (rowId > 0) {
            Uri insertedUri = ContentUris.withAppendedId(DatabaseContract.AnimalTable.CONTENT_URI, rowId);
            if (getContext() != null) {
              getContext().getContentResolver().notifyChange(insertedUri, null);
            }
            return insertedUri;
          }
          throw new SQLException("Failed to insert row - " + uri);
        default:
          throw new IllegalArgumentException("Unknown URI " + uri);
      }
    }
    return null;
  }

  public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    SQLiteDatabase db = dh.getWritableDatabase();
    int count;
    switch (uriMatcher.match(uri)) {
      case ANIMALS:
        count = db.update(DatabaseContract.AnimalTable.TABLE_NAME, values, selection, selectionArgs);
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    if (getContext() != null) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return count;
  }

  public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase db = dh.getWritableDatabase();
    int count;
    switch (uriMatcher.match(uri)) {
      case ANIMALS:
        count = db.delete(DatabaseContract.AnimalTable.TABLE_NAME,
            DatabaseContract.AnimalTable.ID + "=" + uri.getPathSegments().get(1) +
                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    if (getContext() != null) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return count;
  }

  public String getType(@NonNull Uri uri) {
    switch (uriMatcher.match(uri)) {
      case ANIMALS:
        return DatabaseContract.AnimalTable.CONTENT_TYPE;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
  }
}
