package midiapp.midi_challenge;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by lucabazzanella on 01/02/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ImageView imageView = null;
    private TextView textView = null;
    private List<Utente> listaUtenti = null;
    private int padding = 8*2;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public ImageAdapter(Context c, List<Utente> listaUtenti){
        mContext =c;
        this.listaUtenti = listaUtenti;
    }

    public int getCount() {
        //return mThumbIds.length;
        if(listaUtenti!=null) return listaUtenti.size();
        else return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout = new LinearLayout(mContext);

        if (convertView == null) { // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150)); // 85 85
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setImageResource(R.drawable.icona_default_utente);  //setImmagineUtente(R.drawable.icona_default_utente);

            textView = new TextView(mContext);
            textView.setText(listaUtenti.get(position).getNickName());
            textView.setTextSize(20);

            linearLayout.addView(imageView);
            linearLayout.addView(textView);
        }
        else { linearLayout = (LinearLayout) convertView; }


        return linearLayout;
    }

    public void setImmagineUtente(int risorsaImmagine){
        if (imageView != null){
            try { imageView.setImageResource(risorsaImmagine); }
            catch (Exception e){ Toast.makeText(mContext,e.toString(),Toast.LENGTH_SHORT).show(); }
        }
    }
}
