package br.com.dticampossales.appsischamados.adapters.Atendimento;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.MaterialColors;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Colors;
import Utils.Dates;
import Utils.JsonUtil;
import Utils.Security;
import br.com.dticampossales.appsischamados.R;
import br.com.dticampossales.appsischamados.controllers.AtendimentoController;

public class AtendimentoRecyclerViewAdapter extends RecyclerView.Adapter<AtendimentoRecyclerViewAdapter.ViewHolder> {
    private ArrayList<JSONObject> historico;
    private JSONObject tecnicos;
    private Context context;
    private Integer chamadoId;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tecnico;
        private final TextView reportDate;
        private final TextView reportMsg;
        private final ConstraintLayout card;

        public ViewHolder(View view) {
            super(view);

            tecnico = view.findViewById(R.id.report_tec);
            reportDate = view.findViewById(R.id.report_date);
            reportMsg = view.findViewById(R.id.report_msg);
            card = view.findViewById(R.id.report_card);
        }
    }

    public AtendimentoRecyclerViewAdapter(AtendimentoController atendimentoController) {
        setAdapterData(atendimentoController);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh() {
        setAdapterData(new AtendimentoController(context, chamadoId));
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAdapterData(AtendimentoController atendimentoController) {
        this.context = atendimentoController.getContext();
        this.chamadoId = atendimentoController.getChamadoId();
        this.historico = atendimentoController.getHistorico();
        this.tecnicos = atendimentoController.getTecnicos();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.report_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Context context = viewHolder.itemView.getContext();

        viewHolder.tecnico.setText(getTextById(tecnicos, position, context.getString(R.string.report_tec)));
        viewHolder.reportDate.setText(makeDate(position, context.getString(R.string.report_date)));
        viewHolder.reportMsg.setText(makeText(position, context.getString(R.string.report_msg)));

        String sessionUser = String.valueOf(Security.getSessionUserId(context));
        String reportUser = makeText(position, context.getString(R.string.report_tec));
        
        if (sessionUser.equals(reportUser)) {
            viewHolder.card.setBackgroundColor(
                    Colors.getAttrColor(context, com.google.android.material.R.attr.colorTertiaryContainer));
        }
    }

    @Override
    public int getItemCount() {
        return historico.size();
    }

    private String makeText(Integer position, String key) {
        return StringEscapeUtils.unescapeHtml4(JsonUtil.getJsonVal(historico.get(position), key));
    }

    private String getTextById(JSONObject jsonObject, Integer position, String key) {
        return StringEscapeUtils.unescapeHtml4(JsonUtil.getJsonVal(jsonObject, makeText(position, key)));
    }

    private String makeDate(Integer position, String key) {
        return Dates.fmtLocalTime(JsonUtil.getJsonVal(historico.get(position), key));
    }
}