package fcul.cm.g20.ecopack.fragments.points;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import fcul.cm.g20.ecopack.Mappers.UserMapper;
import fcul.cm.g20.ecopack.Models.User;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.utils.Utils;

public class CameraFragment extends Fragment {

    // TODO: Very silly solution to avoid simple QR codes
    private final static String PLASTIC_CODE = "MEsjlz8/icY3IkBimNebBI3UPhuD8XKfFXRCmKfkAus=";
    private final static String PAPER_CODE = "AKXmbDmQMvAd55PbfzYQwIflLQw1M/bbTEahSb24CPw=";
    private final static String REUSABLE_CODE = "zkI4Yz/RbxUvlXMz5BAAE04GCdPAIrynFT2ktn79o2M=";
    private final static String BIO_CODE = "UZBxg+j7ZlI8n+6VUyugr1o0bxZPiELUHHKKmImXHPc=";

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
                        int points = decodePointsCode(result.getText());
                        // UPDATE User
                        if(userModel != null){
                            userModel.addPoints(points);
                            //save user
                            UserMapper.updateUser(userModel, getContext());
                        }
                        Utils.showToast("Por usar este tipo de embalagem acabou de ganhar " + points + " pontos!!", getContext());
                        Utils.showToast(result.getText(), getContext());

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

    private int decodePointsCode(String text) {
        int result = 0;
        if (text.equals(PLASTIC_CODE))
            result = 1;
        else if (text.equals(PAPER_CODE))
            result = 2;
        else if (text.equals(REUSABLE_CODE))
            result = 4;
        else if (text.equals(BIO_CODE))
            result = 3;
        return result;
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