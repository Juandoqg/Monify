package com.example.monify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monify.Entity.Tarjeta;
import com.example.monify.R;

import java.util.List;

public class TarjetasAdapter extends RecyclerView.Adapter<TarjetasAdapter.TarjetaViewHolder> {

    private List<Tarjeta> listaTarjetas;

    public TarjetasAdapter(List<Tarjeta> listaTarjetas) {
        this.listaTarjetas = listaTarjetas;
    }

    @NonNull
    @Override
    public TarjetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarjeta, parent, false);
        return new TarjetaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarjetaViewHolder holder, int position) {
        Tarjeta tarjeta = listaTarjetas.get(position);

        // Seteamos los textos
        holder.tvNombreTarjeta.setText(tarjeta.getNombre());
        holder.tvNumeroTarjeta.setText(tarjeta.getNumero());
        holder.tvSaldoTarjeta.setText("Saldo: $" + tarjeta.getSaldo());
        holder.tvFechaExpiracion.setText("Vence: " + tarjeta.getFechaExpiracion());

        // Cambiamos el fondo dinámicamente según el tipo de tarjeta
        if (tarjeta.getNombre().equalsIgnoreCase("VISA")) {
            holder.imageViewTarjeta.setImageResource(R.drawable.card_visa_background); // Fondo para Visa
        } else if (tarjeta.getNombre().equalsIgnoreCase("MASTERCARD")) {
            holder.imageViewTarjeta.setImageResource(R.drawable.card_mastercard_background); // Fondo para MasterCard
        } else {
            holder.imageViewTarjeta.setImageResource(R.drawable.card_background); // Fondo genérico
        }
    }

    @Override
    public int getItemCount() {
        return listaTarjetas.size();
    }

    static class TarjetaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreTarjeta, tvNumeroTarjeta, tvSaldoTarjeta, tvFechaExpiracion;
        ImageView imageViewTarjeta;

        public TarjetaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreTarjeta = itemView.findViewById(R.id.tvNombreTarjeta);
            tvNumeroTarjeta = itemView.findViewById(R.id.tvNumeroTarjeta);
            tvSaldoTarjeta = itemView.findViewById(R.id.tvSaldoTarjeta);
            tvFechaExpiracion = itemView.findViewById(R.id.tvFechaExpiracion);
            imageViewTarjeta = itemView.findViewById(R.id.imageViewTarjeta);
        }
    }
}

