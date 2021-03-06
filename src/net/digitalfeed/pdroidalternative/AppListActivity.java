/**
 * Copyright (C) 2012 Simeon J. Morgan (smorgan@digitalfeed.net)
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 * The software has the following requirements (GNU GPL version 3 section 7):
 * You must retain in pdroid-manager, any modifications or derivatives of
 * pdroid-manager, or any code or components taken from pdroid-manager the author
 * attribution included in the files.
 * In pdroid-manager, any modifications or derivatives of pdroid-manager, or any
 * application utilizing code or components taken from pdroid-manager must include
 * in any display or listing of its creators, authors, contributors or developers
 * the names or pseudonyms included in the author attributions of pdroid-manager
 * or pdroid-manager derived code.
 * Modified or derivative versions of the pdroid-manager application must use an
 * alternative name, rather than the name pdroid-manager.
 */

/**
 * @author Simeon J. Morgan <smorgan@digitalfeed.net>
 */
package net.digitalfeed.pdroidalternative;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The main activity for the application - loads and presents a list of applications to
 * the user which can be selected and have the settings changed.
 * 
 * @author smorgan
 *
 */
public class AppListActivity extends Activity implements AppListFragment.OnApplicationSelectedListener, AppDetailFragment.OnDetailActionListener {

	AppDetailFragment detailFragment = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageHelper.updateLanguageIfRequired(this);
        
        setContentView(R.layout.application_list_frame_layout);
		//detailFragment = (AppDetailFragment)
		//		getFragmentManager().findFragmentById(R.id.application_detail_fragment);
		
		if (detailFragment != null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.hide(detailFragment);
			ft.commit();
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.app_list_activity, menu);
		return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.app_list_menu_preferences:
			openPreferenceInterface();
			return true;
		}

		return false;
	}
	
	@Override
	public void onDetailUp() {
		hideDetailInterface();
	}
	
	@Override
	public void onDetailSave() {
		hideDetailInterface();
	}
	
	@Override
	public void onDetailClose() {
		hideDetailInterface();
	}
	
	@Override
	public void onDetailDelete() {
		hideDetailInterface();
	}
		
	/**
	 * Callback for when an app is selected in the Application List fragment.
	 * If there is a 'detail fragment' in the layout, then the application
	 * details will be loaded in that interface. Otherwise,
	 * an intent is used to open the detail interface for the application
	 * 
	 * @param application  The application which was selected
	 */
	@Override
	public void onApplicationSelected(Application application) {

		if (detailFragment != null) {
            // If the detailFragment is available, running in dual-pane
        	Log.d("PDroidAlternative","Telling the detail fragment to load package " + application.getPackageName());
            // Call a method in the ArticleFragment to update its content
            detailFragment.loadApplicationDetail(application.getPackageName());
        	
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.show(detailFragment);
			ft.commit();
        } else {
        	//This is definitely not what Google recommend in https://developer.android.com/guide/components/fragments.html#CommunicatingWithActivity
        	openDetailInterface(application);
        }
	}
	
    /**
     * Starts the 'detail' activity to view the details of the provided application object
     * @param application  Application for which the detail interface should be opened
     */
    private void openDetailInterface(Application application) {
    	Intent intent = new Intent(this, AppDetailActivity.class);
		intent.putExtra(AppDetailActivity.BUNDLE_PACKAGE_NAME, application.getPackageName());
		intent.putExtra(AppDetailActivity.BUNDLE_IN_APP, true);
		startActivity(intent);
    }
    
    private void hideDetailInterface() {
		if (detailFragment != null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.hide(detailFragment);
			ft.commit();
		}
    }

    
    /**
     * Starts the 'preference' interface
     */
    private void openPreferenceInterface() {
    	Intent intent = new Intent(this, PreferencesListActivity.class);
		startActivity(intent);
    }

}
