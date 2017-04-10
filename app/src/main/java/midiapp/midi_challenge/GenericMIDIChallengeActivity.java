package midiapp.midi_challenge;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class GenericMIDIChallengeActivity extends AppCompatActivity {

    FunzioniDatabase funzioniDatabaseComuni = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        funzioniDatabaseComuni = new FunzioniDatabase(this.getBaseContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.button_action_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.show_navigation_drawer :
                DrawerLayout drw = (DrawerLayout)findViewById(R.id.drawer_layout);
                if(drw != null) {   //alcune activiy potrebbero avere il drw layout, altre no.
                    if (!drw.isDrawerOpen(Gravity.LEFT))
                        drw.openDrawer(Gravity.LEFT);
                    else
                        drw.closeDrawer(Gravity.LEFT);
                    break;
                }
        }
        return true;
    }
}
