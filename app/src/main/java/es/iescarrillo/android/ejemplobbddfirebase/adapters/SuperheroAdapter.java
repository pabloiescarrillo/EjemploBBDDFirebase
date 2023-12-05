package es.iescarrillo.android.ejemplobbddfirebase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import es.iescarrillo.android.ejemplobbddfirebase.R;
import es.iescarrillo.android.ejemplobbddfirebase.models.Superhero;

public class SuperheroAdapter extends ArrayAdapter<Superhero> {

    public SuperheroAdapter(Context context, List<Superhero> superheros) {
        super(context, 0, superheros);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtén el objeto Contact en la posición actual
        Superhero superhero = getItem(position);

        // Reutiliza una vista existente o crea una nueva si es necesario
        if (convertView == null) {
            // Indicamos la vista plantilla
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_superhero, parent, false);
        }

        // Accede al TextView en el diseño de cada elemento del ListView
        TextView tvId = convertView.findViewById(R.id.tvId);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPowers = convertView.findViewById(R.id.tvPowers);
        Switch swActive = convertView.findViewById(R.id.swActive);

        // Modificamos el texto a mostrar
        tvId.setText("Id: " + superhero.getId());
        tvName.setText("Name: " + superhero.getName());
        tvPowers.setText("Powers: " + superhero.getPowers().toString());
        swActive.setChecked(superhero.isActive());

        return convertView;
    }
}
