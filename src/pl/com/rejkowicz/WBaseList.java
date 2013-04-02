package pl.com.rejkowicz;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WBaseList extends WActivity implements OnItemClickListener, OnItemLongClickListener {
	ListView list;
	String[] items;
	String action;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		action = getIntent().getExtras().getString("action");
		
		setContentView(list = new ListView(this));
		
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);

		registerForContextMenu(list);
	}
	
	@Override
	protected void onResume() {
		super.onResume();		

		items = dbHelp.getTablesNames().toArray(new String[] {});
		
		removeUnderscores(items);
		
		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items));		
	}
	
	
	private void removeUnderscores(String[] array) {
		for(int i=0; i<array.length; ++i){
			array[i] = array[i].replace('_', ' ');			
		}				
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {				
		menu.add(R.string.editSth);
		menu.add(R.string.deleteSth);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle().equals(getString(R.string.editSth))) {

			String[] values = dbHelp.getCurrentTable().split("_to_");

			nextActivity(WEditBase.class, new String[] { DBHelper.WORD1,
					DBHelper.WORD2 }, values);
		} else {
			dbHelp.deleteTable(-1);		
			onResume();
		}

		return super.onContextItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> list, View view, int position, long id) {
		dbHelp.setTable(position);		
		if (action.equals(getString(R.string.base))) {			
			nextActivity(WBase.class, null, null);			
		} else {
			dbHelp.prepareBase();
			nextActivity(WAsk.class, null, null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.findItem(R.id.addSth).setVisible(true);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int id, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addSth:
			nextActivity(WEditBase.class, null, null);
			break;
		}
		return super.onMenuItemSelected(id, item);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> list, View view, int position,
			long id) {
		dbHelp.setTable(position);
		return false;
	}
}
