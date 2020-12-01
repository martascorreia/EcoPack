package fcul.cm.g20.ecopack.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import fcul.cm.g20.ecopack.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RelativeLayout cincoR;
    private RelativeLayout granelL;
    private RelativeLayout ecoP;
    private RelativeLayout alm;

    public InformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformationFragment newInstance(String param1, String param2) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        //CINCO R
       cincoPage(view);
        //Granel R
        granelPage(view);

       return view;
    }

    //METODO PARA MUDAR PARA O FRAGMENTO DO 5RS
    private void cincoPage(final View view){
        cincoR = view.findViewById(R.id.cinco_R);
        cincoR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentR = new politicaRs();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.info_pegada, fragmentR).commit();
            }
        });
    }

    //METODO PARA MUDAR PARA O FRAGMENTO DO GRANEL
    private void granelPage(final View view){
        granelL = view.findViewById(R.id.granel);
        granelL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentG = new GranelFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.info_pegada, fragmentG).commit();
            }
        });
    }
}