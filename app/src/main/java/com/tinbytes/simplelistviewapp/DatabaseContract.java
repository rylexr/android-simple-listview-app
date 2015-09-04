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

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
  public static final String AUTHORITY = "com.tinbytes.simplelistviewapp.SimpleContentProvider";
  public static final String DB_NAME = "simplelistview.db";
  public static final int DB_VERSION = 1;

  public interface AnimalColumns {
    String ID = BaseColumns._ID;
    String NAME = "name";
    String CATEGORY = "category";
  }

  public static final class AnimalTable implements AnimalColumns {
    public static final String TABLE_NAME = "animal";
    public static final String URI_PATH = "animal";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + URI_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + URI_PATH;
    public static final String CONTENT_NOTE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + URI_PATH;

    private AnimalTable() {
    }
  }
}
