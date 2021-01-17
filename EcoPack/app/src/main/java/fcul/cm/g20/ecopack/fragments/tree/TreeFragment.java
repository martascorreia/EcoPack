package fcul.cm.g20.ecopack.fragments.tree;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.Models.StoreVisit;
import fcul.cm.g20.ecopack.Models.User;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.profile.ProfileFragment;
import fcul.cm.g20.ecopack.fragments.tree.information.InformationFragment;

public class TreeFragment extends Fragment {

    private FloatingActionButton histBt;
    private FloatingActionButton infoBt;
    private User user;
    private TextView pontosArvore;
    private ImageView img;
    private String data;
    private String nivel;
    private String strpoints;
    private String dateS;
    private String outroDia;
    private String novaData;
    private HashMap hashMap1;
    private String nomeDiaSemana;
    private String ontem;
    private String anteOntemSemana;
    private String aanteontemSemana;
    private String aaanteontemSemana;
    private String aaaanteontemSemana;
    private String aaaaanteontemSemana;
    private String hd;
    private String od;
    private String aod;
    private String aaod;
    private String aaaod;
    private String aaaaod;
    private String aaaaaod;
    private SimpleDateFormat formatdate;
    private MainActivity mainActivity;
    private FirebaseFirestore db;
    private Locale locale = new Locale("pr", "PT");//PARA SABER A DATA EM PORTUGAL
    private String hojeSemana;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // INFLATE THE LAYOUT
        View view = inflater.inflate(R.layout.fragment_tree, container, false);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyPage();
        informationPage();
        writeDayWeek();
        writeDatesWeek();
        mudaArvore();

    }

    //METODO PARA MUDAR PARA O FRAGMENTO DO HISTÓRICO
    private void historyPage(){
        histBt = getView().findViewById(R.id.floating_action_history);
        histBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_tree, profileFragment)
                        .addToBackStack("tree")
                        .commit();
            }
        });
    }

    //METODO PARA MUDAR PARA O FRAGMENTO DO HISTÓRICO
    private void informationPage(){
        infoBt = getView().findViewById(R.id.floating_action_info);
        infoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationFragment fragmentInfo = new InformationFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_tree, fragmentInfo)
                        .addToBackStack("tree")
                        .commit();
            }
        });
    }


        private void writeDayWeek(){

        /*-------------------------------DATAS-------------------------------------------------*/

        /*----------------DIAS DA SEMANA-------------------------------*/
        //OBTENÇÃO DO DIA DA SEMANA DE HOJE

        Calendar calSEMANA0 = Calendar.getInstance();
        calSEMANA0.add(Calendar.DAY_OF_WEEK,0);
        nomeDiaSemana  = calSEMANA0.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,locale);

        //OBTENÇÃO DO DIA DA SEMANA DE ONTEM
        Calendar calSEMANA1 = Calendar.getInstance();
        calSEMANA1.add(Calendar.DAY_OF_WEEK,-1);
        ontem = calSEMANA1.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,locale);

        //OBTENÇÃO DO DIA DA SEMANA DE ANTEONTEM
        Calendar calSEMANA2 = Calendar.getInstance();
        calSEMANA2.add(Calendar.DAY_OF_WEEK,-2);
        anteOntemSemana = calSEMANA2.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,locale);

        //OBTENÇÃO DO DIA DA SEMANA DE ANTE-ANTEONTEM
        Calendar calSEMANA3 = Calendar.getInstance();
        calSEMANA3.add(Calendar.DAY_OF_WEEK,-3);
        aanteontemSemana = calSEMANA3.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,locale);

        //OBTENÇÃO DO DIA DA SEMANA DE ANTE-ANTE-ANTEONTEM
        Calendar calSEMANA4 = Calendar.getInstance();
        calSEMANA4.add(Calendar.DAY_OF_WEEK,-4);
        aaanteontemSemana = calSEMANA4.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,locale);

        //OBTENÇÃO DO DIA DA SEMANA DE ANTE-ANTE-ANTE-ANTEONTEM
        Calendar calSEMANA5 = Calendar.getInstance();
        calSEMANA5.add(Calendar.DAY_OF_WEEK,-5);
        aaaanteontemSemana = calSEMANA5.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,locale);

        //OBTENÇÃO DO DIA DA SEMANA DE ANTE-ANTE-ANTE-ANTE-ANTEONTEM
        Calendar calSEMANA6 = Calendar.getInstance();
        calSEMANA5.add(Calendar.DAY_OF_WEEK,-6);
        aaaaanteontemSemana = calSEMANA6.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,locale);


    }

    private void writeDatesWeek(){
        /*---------DATA OFICIAL - FORMATO PARA VERIFICAÇÃO DA SUA EXISTÊNCIA NA LISTA COM AS CHAVES DO HASHMAP-----------------------------------------*/
        //DATA DO DIA DE HOJE ESPECIAL
        Calendar calHoje = Calendar.getInstance();
        calHoje.add(Calendar.DATE, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        hd = sdf.format(calHoje.getTime());//hoje

        //DATA DO DIA DE ONTEM ESPECIAL
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        od = sdf.format(cal.getTime());//-1

        //DATA DO DIA DE ANTEONTEM
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, -2);
        aod = sdf.format(cal2.getTime());//-2

        //DATA DO DIA DE ANTE-ANTEONTEM
        Calendar cal3 = Calendar.getInstance();
        cal3.add(Calendar.DATE, -3);
        aaod = sdf.format(cal3.getTime());//-3

        //DATA DO DIA DE ANTE-ANTE-ANTEONTEM
        Calendar cal4 = Calendar.getInstance();
        cal4.add(Calendar.DATE, -4);
        aaaod = sdf.format(cal4.getTime());//-4

        //DATA DO DIA DE ANTE-ANTE-ANTE-ANTEONTEM
        Calendar cal5 = Calendar.getInstance();
        cal5.add(Calendar.DATE, -5);
        aaaaod = sdf.format(cal5.getTime());//-5

        //DATA DO DIA DE ANTE-ANTE-ANTE-ANTE-ANTEONTEM
        Calendar cal6 = Calendar.getInstance();
        cal6.add(Calendar.DATE, -6);
        aaaaaod = sdf.format(cal5.getTime());//-6
    }


    private void mudaArvore(){
        //TEXT COM OS PONTOS
        pontosArvore = getView().findViewById(R.id.pontosarvore);
        img = getView().findViewById(R.id.image_tree);
        //VAMOS USAR O SHAREDpREFENCES PARA ACEDER AO VALOR AOS PONTOS DO UTILIZADOR
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
        //VAMOS ACEDER À COLEÇÃO DE DADOS
        db.collection("users").whereEqualTo("username", sharedPreferences.getString("username", "")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                    //VAMOS BUSCAR AS VISITAS
                    mainActivity.userVisits = StoreVisit.toVisitsList((ArrayList<HashMap<String, Object>>) documentSnapshot.get("visits"));

                    //VAMOS BUSCAR A DATA DAS VISITA DADA
                    /*------------------------------HASHMAP COM AS VISITAS--------------------------------------*/
                    //SABER O DIA DA SEMANA ATUAL
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", locale);
                    //FORMATO DE DATA PARA O HASHMAP
                    Date d = new Date();
                    formatdate = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                    //Nº DE VISITAS EXISTENTES NO FIREBASE
                    int len = mainActivity.userVisits.size();

                    //CRIAÇÃO DE UM HASHMAP1 COM AS VISITAS DO UTILIZADOR
                    hashMap1 = new HashMap();

                    for (int x = 0; x < len; x++){
                        //SABER QUAL ERA O MARKER DA VISITA INDICADA
                        dateS = String.valueOf(mainActivity.userVisits.get(x).getMarkerTag());
                        //SABER O DIA DA SEMANA DA DATA INDICADA
                        outroDia = dayFormat.format(mainActivity.userVisits.get(x).getDate());
                        //FORMATAR A DATA DO FIREBASE
                        novaData = formatdate.format(mainActivity.userVisits.get(x).getDate());
                        //diaVisit = dayFormat.format(mainActivity.userVisits.get(x).getDate());
                        hashMap1.put(outroDia+"_"+novaData,dateS);

                    }

                    //FORMAÇÃO DE UMA ARRAYLIST COM TODAS AS KEYS DO HASHMAP PARA FRILTRAR PELA SUBSTRING
                    ArrayList nova = new ArrayList();
                    nova.addAll(hashMap1.keySet());

                    //LISTAS COM AS CHAVES PARA CAD UM DOS DIAS DA SEMANA
                    ArrayList ontemFiltrada = new ArrayList();//ontem
                    ArrayList hojeFiltrada = new ArrayList();//hoje
                    ArrayList anteOntemFiltrada = new ArrayList();//anteontem
                    ArrayList aanteOntemFiltrada = new ArrayList();//aanteontem
                    ArrayList aaanteOntemFiltrada = new ArrayList();//aaanteontem
                    ArrayList aaaanteOntemFiltrada = new ArrayList();//aaaanteontem
                    ArrayList aaaaanteOntemFiltrada = new ArrayList();//aaaaanteontem


                    /*--------FILTRO DA ARRAYLIST DAS CHAVES CONSOANTE DOS DIAS DA SEMANA E A SUA DATA-------------*/
                    //VAMOS AVALIAR SE EXISTEM MARKER COM O DIA DE HOJE
                    for (int ho = 0; ho < nova.size(); ho++){
                        //VAMOS PASSAR AS CHAVES PARA STRING
                        String strIndexHo = nova.get(ho).toString();
                        //SE HOUVEREM MARKERS COM O DIA DE HOJE
                        if (strIndexHo.contains(nomeDiaSemana) && strIndexHo.contains(hd)){
                            hojeFiltrada.add(hashMap1.get(strIndexHo).toString());
                        }
                    }

                    //VAMOS VERIFICAR SE EXISTEM MARKERS COM A DATA DE ONTEM
                    for (int n = 0; n< nova.size(); n++){
                        //VAMOS PASSAR AS CHAVES PARA STR
                        String strIndex = nova.get(n).toString();
                        //SE ESSA CHAVE TIVER O NOME DO DIA DA SEMANA E O DIA DE HOJE NO HASHMAP -1dia
                        if ( strIndex.contains(ontem) && strIndex.contains(od)){
                            //VAMOS ADICIONAR OS MARCADORES DESSE DIA DA SEMANA A UMA LISTA SÓ COM OS MARCADORES
                            //novaFiltrada.add(hashMap1.get(strIndex).toString());
                            ontemFiltrada.add(hashMap1.get(strIndex).toString());
                            //diasFiltrados.add(strIndex);
                            //pontosArvore.setText("tem um deles");
                        }
                    }

                    //VAMOS VERIFICAR SE EXISTEM MARKER COM A DATA DE ANTEONTEM -2 dias
                    for (int ao = 0; ao <  nova.size(); ao++){
                        String strIndexAo = nova.get(ao).toString();
                        if (strIndexAo.contains(anteOntemSemana) && strIndexAo.contains(aod)){
                            anteOntemFiltrada.add(hashMap1.get(strIndexAo).toString());
                        }
                    }

                    //SABER SE EXISTEM MARKERS COM A DATA DO DIA DE ANTE-ANTEONTEM -3dias
                    for (int aao = 0; aao < nova.size(); aao++){
                        String strIndexAAo = nova.get(aao).toString();
                        if (strIndexAAo.contains(aanteontemSemana) && strIndexAAo.contains(aaod)){
                            aanteOntemFiltrada.add(hashMap1.get(strIndexAAo).toString());
                        }
                    }

                    //SABER SE EXISTEM PARA O DIA DE ANTE-ANTE-ANTEONTEM -4 dias
                    for (int aaao = 0; aaao < nova.size(); aaao++){
                        String strIndexAAAo = nova.get(aaao).toString();
                        if (strIndexAAAo.contains(aaanteontemSemana) && strIndexAAAo.contains(aaaod)){
                            aaanteOntemFiltrada.add(hashMap1.get(strIndexAAAo).toString());
                        }
                    }

                    //SABER SE EXISTEM MARKERS PARA O DIA DE ANTE-ANTE-ANTE-ANTEONTEM -5 dias
                    for (int aaaao = 0; aaaao < nova.size(); aaaao++){
                        String strIndexAAAAo = nova.get(aaaao).toString();
                        if (strIndexAAAAo.contains(aaaanteontemSemana) && strIndexAAAAo.contains(aaaaod)){
                            aaaanteOntemFiltrada.add(hashMap1.get(strIndexAAAAo).toString());
                        }
                    }

                    //SABER SE EXISTEM MARKERS PARA O DIA DE ANTE-ANTE-ANTE-ANTE-ANTEONTEM -6 dias
                    for (int aaaaao = 0; aaaaao < nova.size(); aaaaao++){
                        String strIndexAAAAAo = nova.get(aaaaao).toString();
                        if (strIndexAAAAAo.contains(aaaaanteontemSemana) && strIndexAAAAAo.contains(aaaaaod)){
                            aaaaanteOntemFiltrada.add(hashMap1.get(strIndexAAAAAo).toString());
                        }
                    }


                    /*---------------------------------MUDANÇA DA ÁRVORE -------------------------------------------------*/
                    //FORMAÇÃO DE CADA UM DOS CONTADORES PARA MUDANÇA DA ÁRVORE
                    int n = 0;
                    int countMarkerPlastic = 0;
                    int coutMarkerPaper = 0;
                    int coutMarkerHome = 0;
                    int coutMarkerReusable = 0;
                    int coutMarkerBio = 0;
                    int plastic = 0;

                    //VAMOS VERIFICAR CASO A CASO CONSANTE O DIA DA SEMANA
                    ArrayList semanal = new ArrayList();

                    //SE HOJE FOR SÁBADO - TESTE PARA OS CASOS DA SEGUNDA FEIRA
                    if (nomeDiaSemana.contains("segunda")){
                        semanal.addAll(hojeFiltrada);
                    }else if (nomeDiaSemana.contains("terça")){
                        semanal.addAll(hojeFiltrada);
                        semanal.addAll(ontemFiltrada);
                    }else if (nomeDiaSemana.contains("quarta")){
                        semanal.addAll(hojeFiltrada);
                        semanal.addAll(ontemFiltrada);
                        semanal.addAll(anteOntemFiltrada);

                    }else if (nomeDiaSemana.contains("quinta")){
                        semanal.addAll(hojeFiltrada);
                        semanal.addAll(ontemFiltrada);
                        semanal.addAll(anteOntemFiltrada);
                        semanal.addAll(aanteOntemFiltrada);
                    }else if (nomeDiaSemana.contains("sexta")){
                        semanal.addAll(hojeFiltrada);
                        semanal.addAll(ontemFiltrada);
                        semanal.addAll(anteOntemFiltrada);
                        semanal.addAll(aanteOntemFiltrada);
                        semanal.addAll(aaanteOntemFiltrada);
                    }else if (nomeDiaSemana.contains("sábado")){
                        semanal.addAll(hojeFiltrada);
                        semanal.addAll(ontemFiltrada);
                        semanal.addAll(anteOntemFiltrada);
                        semanal.addAll(aanteOntemFiltrada);
                        semanal.addAll(aaanteOntemFiltrada);
                        semanal.addAll(aaaanteOntemFiltrada);
                    }else if (nomeDiaSemana.contains("domingo")){
                        semanal.addAll(hojeFiltrada);
                        semanal.addAll(ontemFiltrada);
                        semanal.addAll(anteOntemFiltrada);
                        semanal.addAll(aanteOntemFiltrada);
                        semanal.addAll(aaanteOntemFiltrada);
                        semanal.addAll(aaaanteOntemFiltrada);
                        semanal.addAll( aaaaanteOntemFiltrada);
                    }

                    //AGORA VAMOS MEXER NA ÁRVORE
                    ArrayList contadorMarcadores = new ArrayList();

                    if (semanal.isEmpty() == true){
                        pontosArvore.setText(nomeDiaSemana+" "+hd);
                        img.setImageDrawable(getResources().getDrawable(R.drawable.ic_arvoren));

                    }else if (semanal.isEmpty() == false){
                        //CONTADORES DOS MARKERS
                        countMarkerPlastic = Collections.frequency(semanal,"marker_plastic");
                        coutMarkerPaper =+ Collections.frequency(semanal,"marker_paper");
                        coutMarkerHome =+ Collections.frequency(semanal,"marker_home");
                        coutMarkerReusable =+ Collections.frequency(semanal,"marker_reusable");
                        coutMarkerBio =+ Collections.frequency(semanal,"marker_bio");

                        //CRIAÇÃO DE UMA ARRAYLIST COM OS CONTADORES DE TODOS OS MARKERS
                        contadorMarcadores.add(countMarkerPlastic);
                        contadorMarcadores.add(coutMarkerPaper);
                        contadorMarcadores.add(coutMarkerHome);
                        contadorMarcadores.add(coutMarkerReusable);
                        contadorMarcadores.add(coutMarkerBio);
                        n = (int) Collections.max(contadorMarcadores);

                        if (countMarkerPlastic == n ){
                            nivel = "Esta semana não estás a fazer as escolhas mais corretas. Vamos mudar isso?";
                            data = nivel;
                            //String max = String.valueOf(n);
                            pontosArvore.setText(nivel);
                            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_arvoreh));
                        }else if (coutMarkerPaper == n || countMarkerPlastic == coutMarkerPaper) {
                            nivel = "Não tens optado pelas melhores escolhas, mas ainda vais a tempo de mudar!";
                            data = nivel;
                            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_arvorene));
                            pontosArvore.setText(nivel);
                        }else if (coutMarkerBio == n || coutMarkerBio == coutMarkerPaper || countMarkerPlastic == coutMarkerBio){
                            nivel = "Muito bem! Vais num bom caminho.";
                            data = nivel;
                            pontosArvore.setText(nivel);
                            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_arvorep));
                        }else if (coutMarkerHome == n || coutMarkerReusable == n || coutMarkerHome == coutMarkerReusable){
                            nivel = "Excelente! Continua assim!";
                            data = nivel;
                            strpoints = String.valueOf(mainActivity.userVisits.get(0).getMarkerTag());
                            pontosArvore.setText(nivel);
                            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_arvorep));//ALTERAR DEPOIS PARA O NOME DA IMAGEM ALTERADA DO NÍVEL EXCELENTE
                        }
                    }
               }
            }
        });






    }
}