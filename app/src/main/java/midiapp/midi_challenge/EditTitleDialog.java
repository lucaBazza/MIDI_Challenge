package midiapp.midi_challenge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import android.view.*;

/**
 * Created by Paolo on 27/05/2017.
 */

public class EditTitleDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setMessage("Imposta Titolo");

        LayoutInflater i = getActivity().getLayoutInflater();

        final View v = i.inflate(R.layout.edit_text_layout,null);

        b.setView(v);

        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FunzioniDatabase fb = new FunzioniDatabase(getActivity().getBaseContext());
                Bundle b = getArguments();
                long id_brano = b.getLong("id_brano_modificare");
                Brano brano = fb.trovaBrano(id_brano);

                EditText ed = (EditText) v.findViewById(R.id.txt_edit_modifica_titolo_brano);

                brano.setTitolo(ed.getText().toString());
                fb.aggiornaBrano(brano);
            }
        });

        b.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return b.create();
    }
}
