package br.untins.webservice;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadJsonInternet jsonInternet = new DownloadJsonInternet();
        jsonInternet.execute("http://www.livroiphone.com.br/carros/carros_esportivos.json");
    }
}


class DownloadJsonInternet extends AsyncTask<String, Void, ModeloCarro>{

    //Codigo executado em uma nova thread(paralelo)
    @Override
    protected ModeloCarro doInBackground(String... strings) {
        OkHttpClient vrClienthttp = new OkHttpClient();
        Gson vrGson = new Gson();

        //Monta a requisição
        Request request = new Request.Builder().url(strings[0]).build();

        try{
            //Baixa o conteudo do webService
            Response response = vrClienthttp.newCall(request).execute();

            //Converte os bytes para String
            String sJson = response.body().string();

            //Converte a String JSON em Objetos do tipo carro
            ModeloCarro carros = vrGson.fromJson(sJson, ModeloCarro.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    //Thread finalizaa (código executado na Thread principal) (Iterface)

    protected void onPostExecute(ArrayList<ModeloCarro> carros) {

    }

    //Prepara para excutar a Thread
    @Override
    protected void onPreExecute() {



    }
}