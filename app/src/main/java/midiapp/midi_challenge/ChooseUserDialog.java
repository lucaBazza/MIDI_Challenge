package midiapp.midi_challenge;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.List;

/**
 * Created by Paolo on 05/04/2017.
 */

public class ChooseUserDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Seleziona Utente");
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.activity_login_activity,container,false);
        GridView grigliaUtenti = (GridView) v.findViewById(R.id.layout_griglia_utenti);

        FunzioniDatabase db = new FunzioniDatabase(this.getActivity().getBaseContext());
        final List<Utente> listaUtenti = db.prendiTuttiUtenti();

        final ArrayAdapter ad = new ArrayAdapter(this.getActivity().getBaseContext(),R.layout.users_grid_element,R.id.txt_NomeUtenteLogin, listaUtenti);
        grigliaUtenti.setAdapter(new ImageAdapter(this.getActivity().getBaseContext(),listaUtenti));

        grigliaUtenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FunzioniDatabase db = new FunzioniDatabase(getActivity().getBaseContext());
                Utente u = db.trovaUtente(listaUtenti.get(i).getIdUtente());
                Intent activityUtente = new Intent(getActivity().getBaseContext(),MainActivity.class);
                activityUtente.putExtra("id_utente",u.getIdUtente());
                //activityUtente.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(activityUtente);
            }
        });

        return v;
    }


}
