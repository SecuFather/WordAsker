package pl.com.rejkowicz;

import java.util.List;

import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WMenu extends WActivity implements OnClickListener{
	Button startButton, baseButton, optionsButton, exitButton;
	@Override
	public void onCreate(Bundle bundle) {
		dbHelp = new DBHelper(this);
		super.onCreate(bundle);
		
		setContentView(R.layout.wmenu);
		(startButton = (Button) findViewById(R.id.startButton)).setOnClickListener(this);
		(baseButton = (Button) findViewById(R.id.baseButton)).setOnClickListener(this);
		(optionsButton = (Button) findViewById(R.id.optionsButton)).setOnClickListener(this);
		(exitButton = (Button) findViewById(R.id.exitButton)).setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.startButton:
		case R.id.baseButton:
			nextActivity(WBaseList.class, 
					     new String[]{ "action" },
					     new String[]{ (String) ((Button)v).getText() });
			break;
			
		case R.id.optionsButton:
			nextActivity(WOptions.class, null, null);
			break;
		case R.id.exitButton:
			finish();
			break;			
		}
	}
	
}
