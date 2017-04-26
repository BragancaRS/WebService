package br.untins.webservice;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        DownloadJsonInternet jsonInternet = new DownloadJsonInternet(this);
        jsonInternet.execute("http://www.livroiphone.com.br/carros/carros_esportivos.json");
    }
}


class DownloadJsonInternet extends AsyncTask<String, Void, List<Carro>> {

    MainActivity copy = null;

    DownloadJsonInternet(MainActivity context) {
        copy = context;
    }

    //Codigo executado em uma nova thread(paralelo)
    @Override
    protected List<Carro> doInBackground(String... strings) {
        OkHttpClient vrClienthttp = new OkHttpClient();
        Gson vrGson = new Gson();

        //Monta a requisição
        Request request = new Request.Builder().url(strings[0]).build();

        try {
            //Baixa o conteudo do webService
            Response response = vrClienthttp.newCall(request).execute();

            //Converte os bytes para String
            String sJson = response.body().string();

            //Converte a String JSON em Objetos do tipo carro
            ModeloCarro carros = vrGson.fromJson(sJson, ModeloCarro.class);

            return carros.getCarros().getCarro();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //Thread finalizaa (código executado na Thread principal) (Iterface)

    protected void onPostExecute(List<Carro> carros) {

        Adapter adapter = new Adapter(copy, carros);
        copy.listView.setAdapter(adapter);
    }

    //Prepara para excutar a Thread
    @Override
    protected void onPreExecute() {

    }
}


class Adapter extends ArrayAdapter<Carro> {
    MainActivity copy = null;

    public Adapter(MainActivity context, List<Carro> vetCarros) {
        super(context, 0, vetCarros);
    }


    public View getView(int position, View recycledCell, ViewGroup father) {

        //Criar uma celula para cada elemento do vetor de carros
        //Preencher essa celula com os dados do vetor de carros na Posicao position
        Carro c = this.getItem(position);

        if (recycledCell == null) {
            recycledCell = LayoutInflater.from(getContext()).inflate(R.layout.celula, father, false);
        }

        //Pegar os dados do objeto e setar os elementos visuais da celula
        TextView carModel = (TextView) recycledCell.findViewById(R.id.carModel);
        carModel.setText(c.getNome());
        //Setar imagem

        return recycledCell;
    }
}