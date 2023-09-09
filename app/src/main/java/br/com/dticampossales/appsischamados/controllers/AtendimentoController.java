package br.com.dticampossales.appsischamados.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Utils.JsonRequest;
import Utils.JsonUtil;
import Utils.Security;
import br.com.dticampossales.appsischamados.R;

public class AtendimentoController extends BaseController {
    private final ArrayList<JSONObject> historico;
    private final Integer chamadoId;

    public AtendimentoController(Context context, Integer chamadoId) {
        super(context, setDataSet(context, chamadoId));

        this.historico = buildObjectList(getContext().getString(R.string.api_historico_key));
        this.chamadoId = chamadoId;
    }

    public Integer getChamadoId() {
        return this.chamadoId;
    }

    public ArrayList<JSONObject> getHistorico() {
        return this.historico;
    }

    private static JSONObject setDataSet(Context context, Integer chamadoId) {
        JSONObject fullDataSet = new JSONObject();

        String hashLogin = Security.getHashLogin(context);

        if (!hashLogin.equals("")) {
            try {
                String chamadosUrl = String.format(context.getString(R.string.api_atendimento), hashLogin, chamadoId);
                fullDataSet = JsonRequest.request(chamadosUrl);
                Log.i("msg", fullDataSet.toString());

            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(context, R.string.app_fail, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        return fullDataSet;
    }
}
