package fcul.cm.g20.ecopack.ui.fragments.tree.information;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.ui.fragments.tree.granel.GranelFragment;
import fcul.cm.g20.ecopack.ui.fragments.tree.reciclagem.PoliticaRsFragment;

public class InformationFragment extends Fragment {

    private RelativeLayout cincoR;
    private RelativeLayout granelL;
    private RelativeLayout ecoP;
    private RelativeLayout alm;

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
        //Granel R
        granelPage();
    }

    //METODO PARA MUDAR PARA O FRAGMENTO DO 5RS
    private void cincoPage(){
        cincoR = getView().findViewById(R.id.cinco_R);
        cincoR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentR = new PoliticaRsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_info, fragmentR).addToBackStack(null).commit();
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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_info, fragmentG).addToBackStack(null).commit();
            }
        });
    }
}