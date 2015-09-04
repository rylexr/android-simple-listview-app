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
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SimpleListViewActivity extends AppCompatActivity implements
    LoaderManager.LoaderCallbacks<Cursor> {
  private static final int ANIMAL_LOADER = 1;

  private ListView lvAnimals;
  private SimpleCursorAdapter scaAnimals;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_list_view);
    lvAnimals = (ListView) findViewById(R.id.list);
    scaAnimals = new SimpleCursorAdapter(
        this,
        android.R.layout.simple_list_item_2,
        null,
        new String[]{DatabaseContract.AnimalColumns.NAME, DatabaseContract.AnimalColumns.CATEGORY},
        new int[]{android.R.id.text1, android.R.id.text2},
        0);
    lvAnimals.setAdapter(scaAnimals);
    lvAnimals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // ListView Clicked item index
        Cursor c = (Cursor) lvAnimals.getItemAtPosition(position);
        // Show Alert
        Toast.makeText(getApplicationContext(),
            "Position " + position + " - Animal " + c.getString(c.getColumnIndex(DatabaseContract.AnimalColumns.NAME)), Toast.LENGTH_LONG).show();
      }
    });

    getSupportLoaderManager().initLoader(ANIMAL_LOADER, null, this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_simple_list_view, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_add_animal:
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(this)
            .setTitle("New Animal")
            .setView(input)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                ContentValues cv = new ContentValues();
                cv.put(DatabaseContract.AnimalColumns.NAME, input.getText().toString());
                cv.put(DatabaseContract.AnimalColumns.CATEGORY, "Unknown");
                Uri uri = getContentResolver().insert(DatabaseContract.AnimalTable.CONTENT_URI, cv);
                if (uri != null) {
                  getContentResolver().notifyChange(uri, null);
                }
              }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
              }
            })
            .show();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    switch (id) {
      case ANIMAL_LOADER:
        return new CursorLoader(this, DatabaseContract.AnimalTable.CONTENT_URI, null, null, null, null);
      default:
        throw new IllegalArgumentException("Unknown loader ID = " + id);
    }
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    scaAnimals.changeCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    scaAnimals.changeCursor(null);
  }
}


