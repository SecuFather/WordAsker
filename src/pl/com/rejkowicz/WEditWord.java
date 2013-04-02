package pl.com.rejkowicz;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WEditWord extends WActivity implements OnClickListener {
	EditText editWord1, editWord2, editGood, editBad, editLevel;
	Button okClicked;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.weditword);

		(okClicked = (Button) findViewById(R.id.okClicked)).setOnClickListener(this);
		editWord1 = (EditText) findViewById(R.id.word1);
		editWord2 = (EditText) findViewById(R.id.word2);
		editGood = (EditText) findViewById(R.id.good);
		editBad = (EditText) findViewById(R.id.bad);
		editLevel = (EditText) findViewById(R.id.level);

		if (word1 != null) {
			editWord1.setText(word1);			
		}
		if(word2 != null) {
			editWord2.setText(word2);
		}
	}

	@Override
	public void onClick(View v) {
		if(word1 != null && word2 != null){
			if(dbHelp.editWord(editWord1.getText().toString(), editWord2.getText().toString()) == false){
				toast(getString(R.string.emptyStringNotAllowed));
				return;
			}
		}else{
			if(dbHelp.insert(editWord1.getText().toString(), editWord2.getText().toString(),
					          editGood.getText().toString(), editBad.getText().toString(), 
					          editLevel.getText().toString()) == false){
				toast(getString(R.string.emptyStringNotAllowed));
				return;
			}		
		}
		dbHelp.setWords(editWord1.getText().toString(), null);
		finish();						
	}
}
