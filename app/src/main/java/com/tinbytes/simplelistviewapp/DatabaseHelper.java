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

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
  private static final String[][] ANIMALS = {
      {"Mammal", "Baboon", "Bat", "Beaver", "Buffalo", "Bull", "Cat", "Dog", "Elephant", "Fox", "Gorilla", "Horse", "Panda", "Tiger", "Wolf", "Zebra"},
      {"Fish", "Mollusc", "Bass", "Clams", "Crab", "Lobster", "Octopus", "Salmon", "Tuna"},
      {"Insect", "Ant", "Bee", "Butterfly", "Dragonfly", "Mosquito", "Scorpion", "Wasp"},
      {"Reptile", "Alligator", "Crocodile", "Lizard", "Tortoise", "Turtle"},
      {"Bird", "Albatross", "Canary", "Duck", "Eagle", "Hawk", "Parrot", "Penguin", "Seagull", "Swan", "Woodpecker"}
  };

  public DatabaseHelper(Context context) {
    super(context, DatabaseContract.DB_NAME, null, DatabaseContract.DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // Animal
    db.execSQL("CREATE TABLE " + DatabaseContract.AnimalTable.TABLE_NAME + " (" +
        DatabaseContract.AnimalTable.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
        DatabaseContract.AnimalTable.NAME + " TEXT NOT NULL," +
        DatabaseContract.AnimalTable.CATEGORY + " TEXT NOT NULL)");

    // Insert sample data. There are better ways to do this
    // to avoid the app freeze when the amount of data is big.
    for (String[] animalRow : ANIMALS) {
      String category = animalRow[0];
      for (int i = 1; i < animalRow.length; i++) {
        String name = animalRow[i];
        ContentValues cv2 = new ContentValues();
        cv2.put(DatabaseContract.AnimalColumns.NAME, name);
        cv2.put(DatabaseContract.AnimalColumns.CATEGORY, category);
        db.insert(DatabaseContract.AnimalTable.TABLE_NAME, null, cv2);
      }
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.AnimalTable.TABLE_NAME);
    onCreate(db);
  }
}
