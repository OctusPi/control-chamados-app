package br.com.dticampossales.appsischamados;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.SortedMap;

import br.com.dticampossales.appsischamados.adapters.Chamados.ChamadosListAdapter;
import br.com.dticampossales.appsischamados.controllers.ChamadosController;

public class ChamadosActivity extends AppCompatActivity {
    private ChamadosListAdapter chamadosListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        ChamadosController chamadosController = new ChamadosController(this, getString(R.string.api_search_default));

        chamadosListAdapter = new ChamadosListAdapter(chamadosController);

        populateRecyclerView();

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.bringToFront();

        FloatingActionButton floatingActionButton = findViewById(R.id.chamados_floating);
        floatingActionButton.setOnClickListener(view -> toggleFilterLayout());

        ChamadosSpinner sectorSpinner = new ChamadosSpinner(
                findViewById(R.id.filter_sector),
                chamadosController.getMappedPropObject(ChamadosController.TypeList.SETORES));


        ChamadosSpinner statusSpinner = new ChamadosSpinner(
                findViewById(R.id.filter_status),
                chamadosController.getMappedPropObject(ChamadosController.TypeList.STATUS));

        sectorSpinner.build();
        statusSpinner.build();

        Button filterChamadosBtn = findViewById(R.id.filter_button);
        Button clearChamadoBtn = findViewById(R.id.clear_button);

        filterChamadosBtn.setOnClickListener(view -> {
            toggleFilterLayout();
            ChamadosController filteredController = new ChamadosController(this, chamadosListAdapter.makeFilter(
                    sectorSpinner.getSelected(), statusSpinner.getSelected()));
            chamadosListAdapter.applyFilter(filteredController);
        });

        clearChamadoBtn.setOnClickListener(view -> {
            toggleFilterLayout();
            ChamadosController filteredController = new ChamadosController(this, getString(R.string.api_search_default));
            chamadosListAdapter.applyFilter(filteredController);
            sectorSpinner.build(); statusSpinner.build();
        });
    }

    private void populateRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.chamados_list);
        ChamadosListAdapter adapter = chamadosListAdapter;
        TextView emptyMessage = findViewById(R.id.chamados_list_empty);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }
            void checkEmpty() {
                emptyMessage.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(15);
        
        recyclerView.setAdapter(adapter);
    }

    private void toggleFilterLayout() {
        ConstraintLayout filterLayout = findViewById(R.id.filter_layout);
        if (filterLayout.getVisibility() != View.VISIBLE) {
            filterLayout.setVisibility(View.VISIBLE);
        } else {
            filterLayout.setVisibility(View.GONE);
        }
    }

    private class ChamadosSpinner {
        private final Spinner spinner;
        private final SortedMap<Integer, ArrayList<String>> optionsMap;
        private final ArrayList<String> options;

        public ChamadosSpinner(Spinner spinner, SortedMap<Integer, ArrayList<String>> optionsMap) {
            this.spinner = spinner;
            this.optionsMap = optionsMap;

            this.optionsMap.put(Integer.parseInt(getString(R.string.filter_id_default)),
                    new ArrayList<>(Arrays.asList(getString(R.string.filter_id_default), getString(R.string.filter_name_default))));

            this.options = new ArrayList<>();

            for (Integer option : optionsMap.keySet()) {
                options.add(StringEscapeUtils.unescapeHtml4(Objects.requireNonNull(optionsMap.get(option)).get(1)));
            }
        }

        private void build() {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, options);

            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

            spinner.setAdapter(arrayAdapter);
        }

        public String getSelected() {
            Integer key = spinner.getSelectedItemPosition();
            return Objects.requireNonNull(optionsMap.get(key)).get(0);
        }
    }
}