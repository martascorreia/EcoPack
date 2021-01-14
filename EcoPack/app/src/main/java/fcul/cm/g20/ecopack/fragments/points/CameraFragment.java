package fcul.cm.g20.ecopack.fragments.points;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fcul.cm.g20.ecopack.Mappers.StoreMapper;
import fcul.cm.g20.ecopack.Mappers.UserMapper;
import fcul.cm.g20.ecopack.Models.MarkerTypes;
import fcul.cm.g20.ecopack.Models.Store;
import fcul.cm.g20.ecopack.Models.StoreVisit;
import fcul.cm.g20.ecopack.Models.User;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.utils.Utils;

public class CameraFragment extends Fragment {

    private final int CAMERA_REQUESTE_CODE = 101;
    private CodeScanner codeScanner;
    private ImageButton backButton;

    private OnBackButtonPressed backCallback;
    private User userModel;

    public CameraFragment(User user, OnBackButtonPressed callback) {
        backCallback = callback;
        userModel = user;
    }

    public CameraFragment(OnBackButtonPressed callback) {
        backCallback = callback;
     }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                fm.popBackStack ("points", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                backCallback.onBack(userModel);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        codeScanner();
        backButton();
    }

    private void backButton() {
        backButton = getView().findViewById(R.id.backButtonCamera);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                fm.popBackStack("points", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                backCallback.onBack(null);
            }
        });
    }

    public void codeScanner() {
        CodeScannerView scannerView = getView().findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(getContext(), scannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(userModel.canReceivePoints()){
                            decodePointsCode(result.getText());
                        } else{
                            Utils.showToast("Não é possivel ler mais QR-Codes, só pode fazer scan de um codigo por visita.", getContext());
                        }
                        //TODO: PERGUNTAR AO GRUPO SE QUEREM QUE A APP VOLTE PARA TRAS APOS A LEITURA DE QR CODE!
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }

    private void decodePointsCode(String text) {
        String[] splitted = text.split('\u0000' + "");
        if (splitted.length == 2) {
            String type = splitted[0];
            String storePath = splitted[1];
            boolean transactionInitiated = StoreMapper.getStoreByPath(storePath, getContext(), new StoreMapper.OnCompleteSuccessful() {
                @Override
                public void onSuccess(Store store) {
                    if (userModel != null && store != null) {
                        store.incrementCounter(type, 0.35);
                        int points = store.getPackageTypePoints(type);
                        userModel.addPoints(points);
                        // create visit
                        MarkerTypes marker = Store.convertQrTypeToMarkerType(type);
                        Log.d("name123", store.getName());
                        StoreVisit visit = new StoreVisit(storePath, store.getName(), marker, System.currentTimeMillis());
                        userModel.addVisit(visit);
                        //update store and points
                        StoreMapper.updateCounters(store, getContext());
                        //update store and points
                        UserMapper.updateUserPointsAndVisits(userModel, getContext());

                        // TODO: MELHORAR MESSAGENS PARA CADA TIPO DE EMBALAGEM
                        Utils.showToast("Por usar uma embalagem de " + type + " acabou de ganhar " + points + " pontos!!", getContext());
                    } else {
                        Utils.showToast("Ocorreu um erro na atribuição de pontos!", getContext());
                    }
                }
            });
            if(!transactionInitiated)
                Utils.showToast("Não foi possivel atribuir pontos, não é possivel aceder a internet.", getContext());
        } else{
            Utils.showToast("O codigo lido é invalido!!", getContext());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
        if(userModel==null){
            // goBack
            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
            fm.popBackStack("points", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            backCallback.onBack(null);
        }

    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}