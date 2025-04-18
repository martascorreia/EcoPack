package fcul.cm.g20.ecopack.fragments.points;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Map;

import fcul.cm.g20.ecopack.models.Prize;
import fcul.cm.g20.ecopack.models.User;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.utils.JavaMailAPI;
import fcul.cm.g20.ecopack.utils.PDF;
import fcul.cm.g20.ecopack.utils.Utils;

import static fcul.cm.g20.ecopack.R.id.points_prizeCode_back_button;

public class PrizeCodeFragment extends Fragment {
    ImageButton backButton;
    OnBackButtonPressed backCallback;

    Prize prizeModel;
    User userModel;
    Map<String, String> code;

    public PrizeCodeFragment() {
        // Required empty public constructcor
    }

    public PrizeCodeFragment(User userModel, Prize prizeModel, OnBackButtonPressed callback) {
        this.prizeModel = prizeModel;
        this.userModel = userModel;
        backCallback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_points_prize_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUserInfo();
        generateCode();
        setEmailButton();
        setDownloadButton();
        backButton();
    }

    private void generateCode() {
        code = prizeModel.generateCode();

        TextView codeText = getView().findViewById(R.id.points_prizeCode_codeText);
        codeText.setText(code.get("code"));

        TextView codeInfo = getView().findViewById(R.id.points_prizeCode_codeInfo);
        codeInfo.setText(code.get("company") + "\n" + code.get("date"));
    }

    private void setDownloadButton() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        Button downloadButton = getView().findViewById(R.id.points_prizeCode_printButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile();
            }
        });
    }

    // Region create PDF file
    // Request code for creating a PDF document
    private static final int CREATE_FILE = 1;

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "code.pdf");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, new Uri.Builder().build());
        startActivityForResult(intent, CREATE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == CREATE_FILE
                && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                String subject = getResources().getString(R.string.email_subject) + prizeModel.getTitle() + "\n\n";

                String text = "Código: " + code.get("code") + "\n" + code.get("company") + "\n" + code.get("date");
                PDF.create(uri,subject + text, getContext());
            }
        }
    }
    //endRegion

    private void setEmailButton() {
        Button emailButton = getView().findViewById(R.id.points_prizeCode_emailButton);
        final Context ctx = getContext();

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = getResources().getString(R.string.email_subject) + ": " + prizeModel.getTitle();
                String text = "Olá " + userModel.getUserName() + "! \n" + getResources().getString(R.string.email_text) + "\n" + "Código: "
                        + code.get("code") + "\n" + code.get("company") + "\n" + code.get("date");
                if(userModel != null){
                    if(userModel.getEmail() != null && !userModel.getEmail().isEmpty() && !userModel.getEmail().equals("N/A")){
                        JavaMailAPI javaMail = new JavaMailAPI(ctx, userModel.getEmail(), subject, text);
                        javaMail.execute();
                        Utils.showToast("Email enviado com sucesso!", getContext());
                    }else {
                        Utils.showToast("Não foi possível enviar para o endereço de email fornecido. Por favor, escolha um endereço de email válido.", getContext());
                    }
                } else{
                    Utils.showToast("Ocorreu um erro. Email não enviado.", getContext());
                }
            }
        });
    }

    private void loadUserInfo() {
        // set user wallet info
        //TextView text = getView().findViewById(R.id.points_prizeCode_walletPoints);
        TextView prizeName = getView().findViewById(R.id.points_prizeCode_title);

        //text.setText(userModel.getPoints() + " Pontos");
        prizeName.setText(prizeModel.getTitle());
    }

    private void backButton() {
        backButton = getView().findViewById(points_prizeCode_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                fm.popBackStack ("points", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                backCallback.onBack(userModel);
            }
        });
    }

    @Override
    public void onResume() {
        // TODO: this was done to avoid crash, find better solution
        if(userModel == null || prizeModel == null)
            backButton();
        super.onResume();
    }
}
