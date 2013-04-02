package pl.com.rejkowicz;

import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class WOptions extends WActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.woptions);
		toast("options");
	}
	@Override
	protected void onStop() {
		super.onStop();		
		EditText op = (EditText)findViewById(R.id.query);
		
		if(op.getText().toString().length() ==0){
			return;
		}
		
		String[] query = op.getText().toString().split("\n");
		
		int fails = 0;
		for (String q : query) {
			try{	
				dbHelp.getWritableDatabase().execSQL(q);
			}catch(SQLiteException e){
				toast(e.getMessage());
				Log.i(WActivity.LOG_TAG, e.getMessage());
				++fails;
			}catch(Exception e){
				toast("wtf");
			}
		}
		toast(Integer.toString(fails) + " fails");
	}
}
