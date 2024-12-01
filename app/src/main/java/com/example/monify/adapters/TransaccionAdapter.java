package com.example.monify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monify.Entity.TransactionWithCard;
import com.example.monify.R;

import java.util.List;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.ViewHolder> {

    private final List<TransactionWithCard> transacciones;

    // Constructor del adaptador
    public TransaccionAdapter(List<TransactionWithCard> transacciones) {
        this.transacciones = transacciones;
    }

    // Crea nuevas vistas (invocado por el layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño de cada ítem
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaccion, parent, false);
        return new ViewHolder(view);
    }

    // Reemplaza el contenido de una vista (invocado por el layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtiene la transacción en la posición actual
        TransactionWithCard transactionWithCard = transacciones.get(position);

        // Configura los datos en las vistas
        holder.tipo.setText(transactionWithCard.transaccion.getTipo());
        holder.monto.setText(String.format("$ %.2f", transactionWithCard.transaccion.getMonto()));
        holder.tipoTarjeta.setText("Tipo: " + transactionWithCard.tarjeta.getNombre());
        holder.tarjeta.setText( transactionWithCard.tarjeta.getNumero());
    }

    // Devuelve el tamaño del dataset
    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    // Proporciona una referencia a las vistas de cada ítem
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tipo,tipoTarjeta, monto, tarjeta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tipo = itemView.findViewById(R.id.text_tipo);   // ID del TextView en transaccion_item.xml
            monto = itemView.findViewById(R.id.text_monto); // ID del TextView en transaccion_item.xml
            tipoTarjeta = itemView.findViewById(R.id.text_tipo_tarjeta);
            tarjeta = itemView.findViewById(R.id.text_numero_tarjeta); // ID del nuevo TextView para mostrar la tarjeta
        }
    }
}

