package fcul.cm.g20.ecopack.fragments.tree.information;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import fcul.cm.g20.ecopack.Models.AppSession;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.tree.EcoPackageFragment;
import fcul.cm.g20.ecopack.fragments.tree.alimentacao.AlimentacaoFragment;
import fcul.cm.g20.ecopack.fragments.tree.granel.GranelFragment;
import fcul.cm.g20.ecopack.fragments.tree.recycling.PoliticaRsFragment;

public class InformationFragment extends Fragment {

    private RelativeLayout cincoR;
    private RelativeLayout granelL;
    private RelativeLayout ecoP;
    private RelativeLayout alm;
    private ImageButton backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_information, container, false);
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //CINCO R
        cincoPage();
        //GRANEL
        granelPage();
        //ALIMENTACAO
        alimentacao();
        //ECOPACKAGE
        ecoPackage();
        //Back button
        backButton();

    }

    private void backButton() {
        backButton = getView().findViewById(R.id.backButtonInfo);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                fm.popBackStack ("tree", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

    //METODO PARA MUDAR PARA O FRAGMENTO DO 5RS
    private void cincoPage(){
        cincoR = getView().findViewById(R.id.cinco_R);
        cincoR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentR = new PoliticaRsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_info, fragmentR)
                        .addToBackStack("info").commit();
            }
        });
    }

    //METODO PARA MUDAR PARA O FRAGMENTO DO GRANEL
    private void granelPage(){
        granelL = getView().findViewById(R.id.granel);
        granelL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentG = new GranelFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_info, fragmentG)
                        .addToBackStack("info").commit();
            }
        });
    }

    //METODO PARA MUDAR PARA O FRAGMENTO DO GRANEL
    private void alimentacao(){
        granelL = getView().findViewById(R.id.alimentacao);
        granelL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentA = new AlimentacaoFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_info, fragmentA)
                        .addToBackStack("info").commit();
            }
        });
    }

    //METODO PARA MUDAR PARA O FRAGMENTO DO EcoPackage
    private void ecoPackage(){
        granelL = getView().findViewById(R.id.produtos_Eco);
        granelL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentE = new EcoPackageFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_info, fragmentE)
                        .addToBackStack("info").commit();
            }
        });
    }

    @Override
    public void onResume() {
        if(AppSession.getInstance().currentFragmentTag.size() < 1)
            AppSession.getInstance().currentFragmentTag.push("tree");
        super.onResume();
    }
}