package com.example.monify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monify.Entity.Transaccion;
import com.example.monify.R;

import java.util.List;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.ViewHolder> {

    private final List<Transaccion> transacciones;

    public TransaccionAdapter(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaccion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaccion transaccion = transacciones.get(position);
        holder.tipo.setText(transaccion.getTipo());
        holder.monto.setText(String.format("$ %.2f", transaccion.getMonto()));
        holder.tarjeta.setText("Tarjeta: " + transaccion.getTarjetaId());

    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tipo, monto, tarjeta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tipo = itemView.findViewById(R.id.text_tipo);
            monto = itemView.findViewById(R.id.text_monto);
            tarjeta = itemView.findViewById(R.id.text_tarjeta);
        }
    }
}
