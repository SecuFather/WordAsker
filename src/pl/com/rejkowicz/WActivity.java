package pl.com.rejkowicz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class WActivity extends Activity {
	public static final String LOG_TAG = "WAskerInfo:";
	protected static DBHelper dbHelp;
	protected Cursor rows;
	protected String word1, word2;

	protected void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	protected void nextActivity(Class<?> dist, String[] keys, String[] values) {
		Intent intent = new Intent(this, dist);

		try {
			for (int i = 0; i < keys.length; ++i) {
				intent.putExtra(keys[i], values[i]);
			}
		} catch (NullPointerException ex) {
			Log.i(LOG_TAG, "No keys or values");
		} finally {			
			startActivity(intent);
		}
	}
		
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		bundle = getIntent().getExtras();		
		if(bundle != null){
			word1 = bundle.getString(DBHelper.WORD1);
			word2 = bundle.getString(DBHelper.WORD2);
		}		
		
		dbHelp.setWords(word1, word2);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int id, MenuItem item) {
		switch(item.getItemId()){
		case R.id.gotoMenu:
			nextActivity(WMenu.class, null, null);
			break;
		case R.id.gotoBase:
			nextActivity(WBaseList.class, new String[]{ "action" }, 
						 new String[]{ getString(R.string.base) });
			break;
		case R.id.gotoOptions:
			nextActivity(WOptions.class, null, null);
			break;
		}
		
		return super.onMenuItemSelected(id, item);
	}
	
}