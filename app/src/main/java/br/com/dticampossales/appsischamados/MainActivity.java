package br.com.dticampossales.appsischamados;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import Utils.JsonRequest;
import Utils.Security;
import br.com.dticampossales.appsischamados.workers.NotificationWorker;

public class MainActivity extends AppCompatActivity {
    private TextView alertText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alertText = findViewById(R.id.alert_txt_main);

        verifyUserAuthentication();
        setUpNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void verifyUserAuthentication() {

        Context context = getApplicationContext();
        String hashLogin = Security.getHashLogin(context);
        String urlJSON = String.format(getResources().getString(R.string.api_login), hashLogin);

        if (!hashLogin.equals("")) {
            try {
                JSONObject jsonObject = JsonRequest.request(urlJSON);
                boolean isAuth = jsonObject.getInt("id") != 0;

                Intent intent = isAuth
                        ? new Intent(context, ChamadosActivity.class)
                        : new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();

            } catch (ExecutionException | InterruptedException | JSONException e) {
                alertText.setText(getString(R.string.app_fail));
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setUpNotifications() {
        String workName = "pushNotification";
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                NotificationWorker.class, 15, TimeUnit.MINUTES)
                .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork(workName, ExistingPeriodicWorkPolicy.KEEP, workRequest);
    }
}