package br.com.dticampossales.appsischamados.controllers;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.SortedMap;

import Utils.JsonUtil;
import br.com.dticampossales.appsischamados.R;

public abstract class BaseController {
    private final Context context;
    private final JSONObject dataSet;
    private final JSONObject tecnicos;
    private final JSONObject setores;
    private final JSONObject tipos;
    private final JSONObject status;
    private final JSONObject equipments;

    public BaseController(Context context, JSONObject dataSet) {
        this.context = context;
        this.dataSet = dataSet;

        this.tecnicos = buildPropObject(PropType.TECNICOS);
        this.setores = buildPropObject(PropType.SETORES);
        this.tipos = buildPropObject(PropType.TIPOS);
        this.status = buildPropObject(PropType.STATUS);
        this.equipments = buildPropObject(PropType.EQUIPMENTS);
    }

    public Context getContext() {
        return this.context;
    }

    public JSONObject getDataSet() {
        return this.dataSet;
    }

    public JSONObject getTecnicos() {
        return this.tecnicos;
    }

    public JSONObject getSetores() {
        return this.setores;
    }

    public JSONObject getStatus() {
        return this.status;
    }

    public JSONObject getTipos() {
        return this.tipos;
    }

    public JSONObject getEquipments() {
        return this.equipments;
    }

    public SortedMap<Integer, ArrayList<String>> getMappedPropObject(JSONObject propObject) {
        return JsonUtil.mapJsonPropObject(propObject);
    }

    public ArrayList<JSONObject> buildObjectList(String dataSetKey) {
        ArrayList<JSONObject> objects = new ArrayList<>();

        try {
            objects.addAll(JsonUtil.jsonList(getDataSet().getJSONArray(dataSetKey)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objects;
    }

    public JSONObject buildPropObject(PropType type) {
        JSONObject propObject = new JSONObject();

        String propKey = getPropKey(context, type);

        try {
            propObject = dataSet.getJSONObject(propKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return propObject;
    }

    private String getPropKey(Context context, PropType type) {
        String propKey = "";
        switch (type) {
            case SETORES: propKey = context.getString(R.string.api_setores_key); break;
            case TECNICOS: propKey = context.getString(R.string.api_tecnicos_key); break;
            case TIPOS: propKey = context.getString(R.string.api_tipos_key); break;
            case STATUS: propKey = context.getString(R.string.api_status_key); break;
            case DETALHES: propKey = context.getString(R.string.api_detalhes_key); break;
            case EQUIPMENTS: propKey = context.getString(R.string.api_equipments_key); break;
        }
        return propKey;
    }

    public enum PropType {
        SETORES(1), TECNICOS(2), TIPOS(3), STATUS(4), DETALHES(5), EQUIPMENTS(6);

        private final int type;

        PropType(int id) {
            type = id;
        }

        public int getPropType() {
            return type;
        }
    }
}
