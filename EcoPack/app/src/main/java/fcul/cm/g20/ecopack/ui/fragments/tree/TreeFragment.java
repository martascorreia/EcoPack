package fcul.cm.g20.ecopack.ui.fragments.tree;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.ui.fragments.tree.information.InformationFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TreeFragment extends Fragment {

    private FloatingActionButton histBt;
    private FloatingActionButton infoBt;

    public static TreeFragment newInstance(String param1, String param2) {
        TreeFragment fragment = new TreeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // INFLATE THE LAYOUT TO THID BUTTON
        View view = inflater.inflate(R.layout.fragment_tree, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyPage();
        informationPage();
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
                getView().findViewById(R.id.floating_action_info).setVisibility(View.INVISIBLE);
                getView().findViewById(R.id.floating_action_history).setVisibility(View.INVISIBLE);

                InformationFragment fragmentInfo = new InformationFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_tree, fragmentInfo)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}