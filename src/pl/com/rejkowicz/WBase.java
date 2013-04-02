package pl.com.rejkowicz;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WBase extends WActivity implements OnItemClickListener, OnItemLongClickListener{
	ListView list;
	String[] items;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(list = new ListView(this));
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);
		registerForContextMenu(list);
	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		if(word1 == null){
			setTitle(dbHelp.getCurrentTable().replace('_', ' '));
			items = dbHelp.selectWords(DBHelper.WORD1, null, null, false);
		}else{			
			setTitle(getString(R.string.resultFor) + " " + word1);
			items = dbHelp.selectWords(DBHelper.WORD2, word1, null, false);
			
			if(items == null){
				finish();
				return;
			}
		}							
		
		if(items != null){
			toast(Integer.toString(items.length));
			list.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, items));
		}else{			
			list.setAdapter(null);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.addSth).setVisible(true);
		menu.findItem(R.id.deleteSth).setVisible(true);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int id, MenuItem item) {
		super.onMenuItemSelected(id, item);
		
		switch(item.getItemId()){
		case R.id.addSth:
			nextActivity(WEditWord.class, new String[] { DBHelper.WORD1 }, new String[] { word1 });			
			break;
		case R.id.deleteSth:
			dbHelp.deleteWords();
			onResume();
			break;			
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> list, View view, int position, long id){
		if(word1 == null){
			nextActivity(WBase.class, new String[] { DBHelper.WORD1 }, new String[] { items[position] });
		}else{			
			nextActivity(WWordView.class, 
						 new String[] { DBHelper.WORD1, DBHelper.WORD2 },
						 new String[] { word1, items[position] });
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> list, View view, int position, long id) {
		if(word1 != null){
			word2 = items[position];
		}else{
			word1 = items[position];
		}
		dbHelp.setWords(word1, word2);
		return false;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(word2 != null){
			menu.add(R.string.editSth);
		}
		menu.add(R.string.deleteSth);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle().equals(getString(R.string.editSth))) {		

			nextActivity(WEditWord.class, 
						 new String[] { DBHelper.WORD1, DBHelper.WORD2 },
						 new String[] { word1, word2} );
		} else {
			dbHelp.deleteWords();
			if(word2 != null){
				word2 = null;
			}else{
				word1 = null;
			}
			onResume();
		}

		return super.onContextItemSelected(item);
	}
	
	
	
}
