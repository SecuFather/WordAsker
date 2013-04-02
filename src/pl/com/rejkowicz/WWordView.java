package pl.com.rejkowicz;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WWordView extends WActivity implements OnClickListener{	
	int good, bad, level;
	
	TextView viewWord1, viewWord2, viewGood, viewBad, viewAll, viewRate, viewLevel;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.wwordview);
		
		viewWord1 = (TextView) findViewById(R.id.word1);
		viewWord2 = (TextView) findViewById(R.id.word2);
		viewGood = (TextView) findViewById(R.id.good);
		viewBad = (TextView) findViewById(R.id.bad);
		viewAll = (TextView) findViewById(R.id.allAnswers);
		viewRate = (TextView) findViewById(R.id.rate);
		viewLevel = (TextView) findViewById(R.id.level);
				
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		Cursor row = dbHelp.getRow(word1, word2);
		
		if(row.moveToFirst()){
			word1 = row.getString(0);
			word2 = row.getString(1);
			good = row.getInt(2);
			bad = row.getInt(3);
			level = row.getInt(4);

			viewWord1.setText(word1);
			viewWord2.setText(word2);
			viewGood.setText(Integer.toString(good));
			viewBad.setText(Integer.toString(bad));
			viewAll.setText(Integer.toString(good + bad));
			if (good == 0 && bad == 0)
				viewRate.setText("-");
			else
				viewRate.setText(Integer.toString(good * 100 / (good + bad)) + "%");
			viewLevel.setText(Integer.toString(level));
		}else{
			finish();
		}
	}
	@Override
	public void onClick(View v) {

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.editSth).setVisible(true);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int id, MenuItem item) {
		super.onMenuItemSelected(id, item);
		if(item.getItemId() == R.id.editSth){
			nextActivity(WEditWord.class, 
					     new String[] { DBHelper.WORD1, DBHelper.WORD2 },
					     new String[] { word1, word2 });
		}
		return true;
	}
}
