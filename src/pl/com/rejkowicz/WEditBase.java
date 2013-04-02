package pl.com.rejkowicz;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WEditBase extends WActivity implements OnClickListener{
	EditText lang1, lang2;
	Button okButton;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(R.layout.weditbase);
		lang1 = (EditText) findViewById(R.id.language1);
		lang2 = (EditText) findViewById(R.id.language2);
		(okButton = (Button) findViewById(R.id.okClicked)).setOnClickListener(this);
		
		if(word1 != null && word2 != null){
			lang1.setText(word1);
			lang2.setText(word2);
		}
	}
	
	@Override
	public void onClick(View v) {
		if(lang1.getText().toString().length() == 0 || lang2.getText().toString().length() == 0){
			toast(getString(R.string.emptyStringNotAllowed));
			return;
		}
		
		if(word1 != null && word2 != null){
			if(!dbHelp.editTable(-1, lang1.getText().toString(), lang2.getText().toString()))
				return;
		}else{
			if(!dbHelp.createTable(lang1.getText().toString(), lang2.getText().toString()))
				return;
		}		
		finish();
	}
}
