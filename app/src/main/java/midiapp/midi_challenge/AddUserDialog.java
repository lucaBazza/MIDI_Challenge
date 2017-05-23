package midiapp.midi_challenge;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Paolo on 06/04/2017.
 */

public class AddUserDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Aggiungi Utente");
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_user_layout,container,false);
        Button btnAdd = (Button) v.findViewById(R.id.btn_nuovo_utente);
        final EditText nuovoNome = (EditText) v.findViewById(R.id.edtTxt_nomeUtente);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nomeDaInserire = nuovoNome.getText().toString();
                FunzioniDatabase db = new FunzioniDatabase(getActivity().getBaseContext());
                long idNuovoUtente = db.inserisci(new Utente(nomeDaInserire));
                if (idNuovoUtente != -1) {
                    Toast.makeText(getActivity().getBaseContext(), "Inserimento Completato", Toast.LENGTH_SHORT).show();
                    dismiss();
                    Intent i = new Intent(getActivity().getApplicationContext(),activity_MainRestyled.class);
                    i.putExtra("id_utente",idNuovoUtente);
                    startActivity(i);
                }
            }
        });
        return v;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
