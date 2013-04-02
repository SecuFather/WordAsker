package pl.com.rejkowicz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "word_asker_v29521.db";
	public static final int DATABASE_VERSION = 1;

	public static final String WORD_ID = "word_id";
	public static final String WORD1 = "word1";
	public static final String WORD2 = "word2";
	public static final String GOOD = "good_answers";
	public static final String BAD = "bad_answers";
	public static final String LEVEL = "learning_level";
	public static final String LEFT = "left_to_ask";

	private static final String LANGUAGE_TABLE = "language_table";
	private static final String LANGUAGE_NAME = "language_name";
	private static final String LANGUAGE_ID = "language_id";
		
	private static final String OPTIONS = "wakser_options";
	private static final String OPTION = "wakser_option";
	private static final String OPTION_VALUE = "wakser_option_value";	

	private String word1, word2;

	private List<String> tables;
	private int chosenTable;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		tables = getTablesNames();
	}

	@Override
	public void onCreate(SQLiteDatabase base) {
		try {
			base.execSQL("CREATE TABLE " + LANGUAGE_TABLE + "(" + LANGUAGE_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + LANGUAGE_NAME
					+ " varchar(32));");
			/*base.execSQL(
					"CREATE TABLE " + OPTIONS + "(" +
					OPTION + " varchar(8), " + OPTION_VALUE + " varchar(8));");*/			
		} catch (SQLiteException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase base, int arg1, int arg2) {

	}

	public boolean createTable(String lang1, String lang2) {
		try {
			String temp = lang1;
			lang1 += "_to_" + lang2;
			lang2 += "_to_" + temp;

			getWritableDatabase().execSQL(
					"CREATE TABLE " + lang1 + "(" + WORD1 + " varchar(128), "
							+ WORD2 + " varchar(128)," + GOOD + " integer, "
							+ BAD + " integer, " + LEVEL + " integer, " + LEFT
							+ " integer, " + WORD_ID + " integer primary key autoincrement);");

			getWritableDatabase().execSQL(
					"CREATE TABLE " + lang2 + "(" + WORD1 + " varchar(128), "
							+ WORD2 + " varchar(128)," + GOOD + " integer, "
							+ BAD + " integer, " + LEVEL + " integer, " + LEFT
							+ " integer, " + WORD_ID + " integer primary key autoincrement);");

			getWritableDatabase().execSQL(
					"INSERT INTO " + LANGUAGE_TABLE + " VALUES(null,'" + lang1
							+ "');");

			getWritableDatabase().execSQL(
					"INSERT INTO " + LANGUAGE_TABLE + " VALUES(null,'" + lang2
							+ "');");

			tables = getTablesNames();
			return true;
		} catch (SQLException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
		return false;
	}

	public boolean editTable(int index, String newLang1, String newLang2) {
		if (index < 0) {
			index = chosenTable;
		}

		try {
			String temp = newLang1;
			newLang1 += "_to_" + newLang2;
			newLang2 += "_to_" + temp;

			getWritableDatabase().execSQL(
					"ALTER TABLE " + tables.get(index) + " RENAME TO "
							+ newLang1 + ";");
			getWritableDatabase().execSQL(
					"UPDATE " + LANGUAGE_TABLE + " SET " + LANGUAGE_NAME + "='"
							+ newLang1 + "' WHERE " + LANGUAGE_NAME + "='"
							+ tables.get(index % 2 == 0 ? index++ : index--)
							+ "';");

			getWritableDatabase().execSQL(
					"ALTER TABLE " + tables.get(index) + " RENAME TO "
							+ newLang2 + ";");
			getWritableDatabase().execSQL(
					"UPDATE " + LANGUAGE_TABLE + " SET " + LANGUAGE_NAME + "='"
							+ newLang2 + "' WHERE " + LANGUAGE_NAME + "='"
							+ tables.get(index) + "';");

			tables = getTablesNames();
			return true;
		} catch (IndexOutOfBoundsException ex) {
			Log.i(WActivity.LOG_TAG, "No such table");
		} catch (SQLException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
		return false;
	}

	public void deleteTable(int index) {
		if (index < 0) {
			index = chosenTable;
		}
		try {
			getWritableDatabase().execSQL(
					"DELETE FROM " + LANGUAGE_TABLE + " WHERE " + LANGUAGE_NAME
							+ "='" + tables.get(index) + "';");
			getWritableDatabase().execSQL(
					"DROP TABLE " + tables.get(index) + ";");

			getWritableDatabase().execSQL(
					"DELETE FROM " + LANGUAGE_TABLE + " WHERE " + LANGUAGE_NAME
							+ "='"
							+ tables.get(index % 2 == 0 ? ++index : --index)
							+ "';");
			getWritableDatabase().execSQL("DROP TABLE " + tables.get(index));

			tables = getTablesNames();
			if (chosenTable >= tables.size()) {
				chosenTable = tables.size() - 2;
			}
		} catch (SQLException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		} catch (IndexOutOfBoundsException ex) {
			Log.i(WActivity.LOG_TAG, "No such table!");
		}
	}

	public void setTable(int index) {
		try {
			chosenTable = index;
			tables.get(index);

		} catch (IndexOutOfBoundsException ex) {
			Log.i(WActivity.LOG_TAG, "No such table");
		}
	}

	public boolean insert(String w1, String w2, String good, String bad,
			String level) {
		if (w1.length() == 0 || w1.length() == 0 || good.length() == 0
				|| bad.length() == 0 || level.length() == 0) {
			return false;
		}

		try {
			getWritableDatabase().execSQL(
					"INSERT INTO " + tables.get(chosenTable) + " VALUES('" + w1
							+ "','" + w2 + "','" + good + "','" + bad + "','"
							+ level + "','0', null);");
			getWritableDatabase().execSQL(
					"INSERT INTO "
							+ tables.get(chosenTable % 2 == 0 ? chosenTable + 1
									: chosenTable - 1) + " VALUES('" + w2
							+ "','" + w1 + "','" + good + "','" + bad + "','"
							+ level + "','0', null);");
			return true;
		} catch (SQLException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
		return false;
	}

	public String[] selectWords(String key, String w1, String w2, boolean left) {
		try {
			String query = "SELECT DISTINCT " + key + " FROM "
					+ tables.get(chosenTable);

			if (left) {
				query += " WHERE " + DBHelper.LEFT + ">0 AND ";
			}

			if ((w1 != null || w2 != null) && !left) {
				query += " WHERE ";
			}

			if (w1 != null && w2 != null) {
				query += DBHelper.WORD1 + "='" + w1 + "' AND " + DBHelper.WORD2
						+ "='" + w2 + "'";
			}
			if (w1 != null && w2 == null) {
				query += DBHelper.WORD1 + "='" + w1 + "'";
			}
			if (w1 == null && w2 != null) {
				query += DBHelper.WORD2 + "='" + w2 + "'";
			}
			query += ";";

			Cursor rows = getReadableDatabase().rawQuery(query, null);

			String[] array = null;
			if (rows.moveToFirst()) {
				array = new String[rows.getCount()];
				int i = 0;
				do {
					array[i] = rows.getString(0);
				} while (rows.moveToNext() && ++i != 0);
			}
			return array;
		} catch (SQLException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
		return null;
	}

	public Cursor getRow(String w1, String w2) {
		try {
			Cursor row = getReadableDatabase().rawQuery(
					"SELECT * FROM " + tables.get(chosenTable) + " WHERE "
							+ DBHelper.WORD1 + "='" + w1 + "' AND "
							+ DBHelper.WORD2 + "='" + w2 + "';", null);
			return row;
		} catch (SQLException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
		return null;
	}

	public void deleteWords() {
		try {
			String query = "DELETE FROM " + tables.get(chosenTable);
			if (word1 != null && word2 != null) {
				query += " WHERE " + DBHelper.WORD1 + "='" + word1 + "' AND "
						+ DBHelper.WORD2 + "='" + word2 + "'";
			}
			if (word1 != null && word2 == null) {
				query += " WHERE " + DBHelper.WORD1 + "='" + word1 + "'";
			}
			if (word1 == null && word2 != null) {
				query += " WHERE " + DBHelper.WORD2 + "='" + word2 + "'";
			}
			query += ";";
			getWritableDatabase().execSQL(query);

			query = "DELETE FROM "
					+ tables.get(chosenTable % 2 == 0 ? chosenTable + 1
							: chosenTable - 1);
			if (word1 != null && word2 != null) {
				query += " WHERE " + DBHelper.WORD2 + "='" + word1 + "' AND "
						+ DBHelper.WORD1 + "='" + word2 + "'";
			}
			if (word1 != null && word2 == null) {
				query += " WHERE " + DBHelper.WORD2 + "='" + word1 + "'";
			}
			if (word1 == null && word2 != null) {
				query += " WHERE " + DBHelper.WORD1 + "='" + word2 + "'";
			}
			query += ";";
			getWritableDatabase().execSQL(query);
		} catch (SQLException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
	}

	public boolean editWord(String newWord1, String newWord2) {
		if (newWord1.length() == 0 || newWord2.length() == 0)
			return false;

		try {
			getWritableDatabase().execSQL(
					"UPDATE " + tables.get(chosenTable) + " SET "
							+ DBHelper.WORD1 + "='" + newWord1 + "', "
							+ DBHelper.WORD2 + "='" + newWord2 + "' "
							+ " WHERE " + DBHelper.WORD1 + "='" + word1
							+ "' AND " + DBHelper.WORD2 + "='" + word2 + "';");
			getWritableDatabase().execSQL(
					"UPDATE "
							+ tables.get(chosenTable % 2 == 0 ? chosenTable + 1
									: chosenTable - 1) + " SET "
							+ DBHelper.WORD2 + "='" + newWord1 + "', "
							+ DBHelper.WORD1 + "='" + newWord2 + "' "
							+ " WHERE " + DBHelper.WORD2 + "='" + word1
							+ "' AND " + DBHelper.WORD1 + "='" + word2 + "';");
			return true;
		} catch (SQLException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
		return false;
	}

	public List<String> getTablesNames() {
		tables = new ArrayList<String>();
		try {
			Cursor rows = getReadableDatabase().rawQuery(
					"SELECT * FROM " + LANGUAGE_TABLE + " ORDER BY "
							+ LANGUAGE_ID + " ASC;", null);

			while (rows.moveToNext())
				tables.add(rows.getString(1));
		} catch (SQLException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}

		return tables;
	}

	public void setWords(String w1, String w2) {
		word1 = w1;
		word2 = w2;
	}

	public String getCurrentTable() {
		return tables.get(chosenTable);
	}

	public int getCurrentPosition() {
		return chosenTable;
	}

	public String getQuestion() {
		try {
			Cursor rows = getReadableDatabase().rawQuery(
					"SELECT * FROM " + tables.get(chosenTable) + " WHERE "
							+ DBHelper.LEFT + ">0 ORDER BY " + DBHelper.LEFT
							+ " DESC LIMIT 50;", null);

			if (rows.moveToFirst()) {

				Random rand = new Random();
				int pos = rand.nextInt(rows.getCount());
				pos -= rand.nextInt(pos + 1);
				rows.moveToPosition(pos);

				return rows.getString(0);
			}
		} catch (SQLException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
		return null;
	}

	public void prepareBase() {
		try {
			getWritableDatabase().execSQL(
					"UPDATE " + tables.get(chosenTable) + 
					" SET " + DBHelper.LEFT + "=0;");
			getWritableDatabase().execSQL(
					"UPDATE " + tables.get(chosenTable) + 
					" SET " + DBHelper.LEFT + "=" + DBHelper.LEVEL +
					" WHERE " + DBHelper.LEVEL + ">1;");
			
			int toLearn = 50 - getReadableDatabase().rawQuery(
					"SELECT " + DBHelper.WORD_ID + " FROM " + tables.get(chosenTable) +
					" WHERE " + DBHelper.LEFT + ">0;", null).getCount();
			if(toLearn > 0){
				Cursor temp = getReadableDatabase().rawQuery(
						"SELECT " + DBHelper.WORD_ID + " FROM " + tables.get(chosenTable) +
						" WHERE " + DBHelper.LEVEL + ">0" +
						" ORDER BY RANDOM() " +
						" LIMIT " + Integer.toString(toLearn) + ";", null);				
				if(temp.moveToFirst()){
					do{
						getWritableDatabase().execSQL(
							"UPDATE " + tables.get(chosenTable) + 
							" SET " + DBHelper.LEFT + "=" + DBHelper.LEVEL +
							" WHERE " + DBHelper.WORD_ID + "=" + Integer.toString(temp.getInt(0)) + ";");
					}while(temp.moveToNext());
				}
			}
		} catch (SQLiteException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
	}

	public void easyAnswer() {
		try {
			getWritableDatabase().execSQL(
					"UPDATE " + tables.get(chosenTable) + " SET " + GOOD + "="
							+ GOOD + "+ 1, " + LEFT + "=" + LEFT + "- 1"
							+ " WHERE " + WORD1 + "='" + word1 + "' AND "
							+ WORD2 + "='" + word2 + "';");
		} catch (SQLiteException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
	}

	public void hardAnswer() {
		try {
			getWritableDatabase().execSQL(
					"UPDATE " + tables.get(chosenTable) + " SET " + BAD + "="
							+ BAD + "+ 1" + " WHERE " + WORD1 + "='" + word1
							+ "' AND " + WORD2 + "='" + word2 + "';");
		} catch (SQLiteException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
	}

	public void sureAnswer() {
		try {
			getWritableDatabase().execSQL(
					"UPDATE " + tables.get(chosenTable) + " SET " + LEVEL + "="
							+ LEVEL + "- 1, " + GOOD + "=" + GOOD + "+ 1, "
							+ LEFT + "=" + LEFT + "- 1" + " WHERE " + WORD1
							+ "='" + word1 + "' AND " + WORD2 + "='" + word2
							+ "';");
		} catch (SQLiteException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
	}

	public void unknownAnswer() {
		try {
			getWritableDatabase().execSQL(
					"UPDATE " + tables.get(chosenTable) + " SET " + LEVEL + "="
							+ LEVEL + "+ 1, " + BAD + "=" + BAD + "+ 1, "
							+ LEFT + "=" + LEFT + "+ 1" + " WHERE " + WORD1
							+ "='" + word1 + "' AND " + WORD2 + "='" + word2
							+ "';");
		} catch (SQLiteException ex) {
			Log.i(WActivity.LOG_TAG, ex.getMessage());
		}
	}
	
}
