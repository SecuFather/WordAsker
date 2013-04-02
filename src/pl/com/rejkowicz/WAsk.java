package pl.com.rejkowicz;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class WAsk extends WActivity implements OnClickListener{
	TextView question;
	Button checkButton;
	
	@Override
	public void onCreate(Bundle bundle) {		
		super.onCreate(bundle);
		setContentView(R.layout.wask);		
				
		if((word1 = dbHelp.getQuestion()) == null){
			finish();
			return;
		}else{
			(question = (TextView) findViewById(R.id.questionText)).setText(word1);
			(checkButton = (Button) findViewById(R.id.checkButton)).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		nextActivity(WAnswer.class, new String[] { DBHelper.WORD1 }, new String[] { word1 });
		finish();
	}
}
