package fcul.cm.g20.ecopack.fragments.tree;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import fcul.cm.g20.ecopack.Models.User;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.tree.information.InformationFragment;

public class TreeFragment extends Fragment {

    private FloatingActionButton histBt;
    private FloatingActionButton infoBt;
    private User user;
    private TextView pontosArvore;
    private ImageView img;
    private int value = -50;//VARIÁVEL QUE ESTÁ A DAR PONTOS TEMPORARIAMENTE -> PASSAR PARA O VALOR QUE VEM DA FIREBASE DEPOIS
    private String data;
    private String nivel;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // INFLATE THE LAYOUT TO THIS BUTTON
        View view = inflater.inflate(R.layout.fragment_tree, container, false);
        /*pontosArvore = view.findViewById(R.id.pontosarvore);
        nivel = "";
        data = nivel;
        pontosArvore.setText(data);//VAI DISPOR NO ECRÃ OS PONTOS NO ECRÃ*/
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyPage();
        informationPage();
        mudaArvore();

    }



    //METODO PARA MUDAR PARA O FRAGMENTO DO HISTÓRICO
    private void historyPage(){
        histBt = getView().findViewById(R.id.floating_action_history);

        histBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //   getView().findViewById(R.id.floating_action_info).setVisibility(View.INVISIBLE);
            //   getView().findViewById(R.id.floating_action_history).setVisibility(View.INVISIBLE);

            //Fragment fragmentI = new HistoricoSemanal();
            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tree, fragmentI).commit();
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

    private void mudaArvore(){
        //TEXT COM OS PONTOS
        pontosArvore = getView().findViewById(R.id.pontosarvore);
        img = getView().findViewById(R.id.image_tree);
        if (value > 0 && value <=50){
            nivel = "Muito bem! Vais num bom caminho.";

            data = nivel;
            pontosArvore.setText(data);
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_arvorep));

        }else if (value < 0 && value >= -50){
            nivel = "Não tens optado pelas melhores escolhas, mas ainda vais a tempo de mudar!";

            data = nivel;
            pontosArvore.setText(data);
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_arvorene));
        } else if (value < -50){
            nivel = "Esta semana não estás a fazer as escolhas mais corretas. Vamos mudar isso?";

            data = nivel;
            pontosArvore.setText(data);
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_arvoreh));
        }else if (value >50){
            nivel = "Excelente! Continua assim!";
            data = nivel;
            pontosArvore.setText(data);
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_arvorep));//ALTERAR DEPOIS PARA O NOME DA IMAGEM ALTERADA DO NÍVEL EXCELENTE

        }



    }
}