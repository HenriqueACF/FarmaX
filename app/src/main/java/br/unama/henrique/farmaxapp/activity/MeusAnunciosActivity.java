package br.unama.henrique.farmaxapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.unama.henrique.farmaxapp.R;
import br.unama.henrique.farmaxapp.adapter.AdapterAnuncios;
import br.unama.henrique.farmaxapp.helper.ConfiguracaoFirebase;
import br.unama.henrique.farmaxapp.model.Anuncio;

public class MeusAnunciosActivity extends AppCompatActivity {

    private RecyclerView recyclerAnuncios;
    private List<Anuncio> anuncios = new ArrayList<>();
    private AdapterAnuncios adapterAnuncios;
    private DatabaseReference anuncioUsuarioRef;

    @Override
    public boolean showAssist(Bundle args) {
        return super.showAssist(args);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);
        //Configurações Iniciais
        anuncioUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_anuncios").child(ConfiguracaoFirebase.getIdUsuario());

        inicializarComponentes();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CadastrarAnuncioActivity.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurar RecyclerView
        recyclerAnuncios.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnuncios.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(anuncios, this);
        recyclerAnuncios.setAdapter( adapterAnuncios );

        //Recupera anúncios para o usuário
        recuperarAnuncios();
    }

    private void recuperarAnuncios(){

    anuncioUsuarioRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

             anuncios.clear();
             for ( DataSnapshot ds : dataSnapshot.getChildren() ){
                anuncios.add( ds.getValue(Anuncio.class) );
             }

             Collections.reverse( anuncios );
             adapterAnuncios.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }

    public void inicializarComponentes(){

        recyclerAnuncios =  findViewById(R.id.recyclerAnuncios);

    }

}
