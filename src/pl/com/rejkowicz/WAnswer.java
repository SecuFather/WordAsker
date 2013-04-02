package pl.com.rejkowicz;

import java.util.Arrays;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WAnswer extends WActivity implements OnClickListener{
	Button sureWordButton, unknownWordButton, easyWordButton, hardWordButton;
	TextView question, answer;
	
	String[] answers;
	int leftWords;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(R.layout.wanswer);
		
		
		answers = dbHelp.selectWords(DBHelper.WORD2, word1, word2, true);
		leftWords = answers.length - 1;
		
		(question = (TextView) findViewById(R.id.questionText)).setText(word1);		
		(answer = (TextView) findViewById(R.id.answerText)).setText(word2 = answers[leftWords]);
		try{
			Cursor stats = dbHelp.getReadableDatabase().rawQuery(
					"SELECT " + DBHelper.GOOD + "," + DBHelper.BAD + "," +
							    DBHelper.LEVEL + "," + DBHelper.LEFT + 
					" FROM " + dbHelp.getCurrentTable() + 
					" WHERE " + DBHelper.WORD1 + "='" + word1 + "' AND " +
								DBHelper.WORD2 + "='" + word2 + "';", null);
			stats.moveToFirst();
			((TextView)findViewById(R.id.stats)).setText(
					"Rate: " + Integer.toString(stats.getInt(0)) + "/" +
							   Integer.toString(stats.getInt(0) + stats.getInt(1)) +
					" Left: " + Integer.toString(stats.getInt(3)) + "/" +
							    Integer.toString(stats.getInt(2)));
			
		}catch (SQLiteException e) {
			Log.i(WActivity.LOG_TAG, e.getMessage());
		}catch (Exception e) {
			toast("wtf");
		}
		
		(sureWordButton = (Button)findViewById(R.id.sureWordButton)).setOnClickListener(this);
		(unknownWordButton = (Button)findViewById(R.id.unknownWordButton)).setOnClickListener(this);
		(easyWordButton = (Button)findViewById(R.id.easyWordButton)).setOnClickListener(this);
		(hardWordButton = (Button)findViewById(R.id.hardWordButton)).setOnClickListener(this);		
	}
	@Override
	public void onClick(View v) {
		dbHelp.setWords(word1, word2);
		
		switch(v.getId()){
		case R.id.sureWordButton:
			dbHelp.sureAnswer();
			break;
		case R.id.unknownWordButton:
			dbHelp.unknownAnswer();
			break;
		case R.id.easyWordButton:
			dbHelp.easyAnswer();
			break;
		case R.id.hardWordButton:
			dbHelp.hardAnswer();
			break;
		}
				
		if(--leftWords >= 0){
			answer.setText(word2 = answers[leftWords]);
		}else{
			nextActivity(WAsk.class, null, null);
			finish();
		}
		
	}
	
}
