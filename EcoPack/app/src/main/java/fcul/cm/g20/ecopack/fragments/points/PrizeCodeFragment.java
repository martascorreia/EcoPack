package fcul.cm.g20.ecopack.fragments.points;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import fcul.cm.g20.ecopack.IOnBackPressed;
import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.Models.AppSession;
import fcul.cm.g20.ecopack.Models.Prize;
import fcul.cm.g20.ecopack.Models.User;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.utils.JavaMailAPI;
import fcul.cm.g20.ecopack.utils.PDF;
import fcul.cm.g20.ecopack.utils.Utils;

import static fcul.cm.g20.ecopack.R.id.points_prizeCode_back_button;
import static fcul.cm.g20.ecopack.R.id.profile_info_email_text;

public class PrizeCodeFragment extends Fragment {
    ImageButton backButton;
    OnBackButtonPressed backCallback;

    Prize prizeModel;
    User userModel;
    
    public PrizeCodeFragment() {
        // Required empty public constructor
    }

    public PrizeCodeFragment(User userModel, Prize prizeModel, OnBackButtonPressed callback) {
        this.prizeModel = prizeModel;
        this.userModel = userModel;
        backCallback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String code = prizeModel.generateCode();
        TextView codeText = getView().findViewById(R.id.points_prizeCode_codeText);
        codeText.setText(code);
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

    //region Create PDF file
    // Request code for creating a PDF document.
    private static final int CREATE_FILE = 1;

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "invoice.pdf");

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
                String subject = getResources().getString(R.string.prize_code_email_subject) + prizeModel.getTitle() + "\n\n";
                String text = getResources().getString(R.string.prize_code_email_text) + "\n" + prizeModel.generateCode();
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
                String subject = getResources().getString(R.string.prize_code_email_subject) + prizeModel.getTitle();
                String text = getResources().getString(R.string.prize_code_email_text) + "\n" + prizeModel.generateCode();
                if(userModel != null){
                    if(userModel.getEmail() != null && userModel.getEmail().isEmpty() && !userModel.getEmail().equals("N/A")){
                        JavaMailAPI javaMail = new JavaMailAPI(ctx, userModel.getEmail(), subject, text);
                        javaMail.execute();
                        Utils.showToast("E-mail enviado com sucesso!", getContext());
                    }else {
                        Utils.showToast("O E-mail que tem o seu perfil não é valido, por favor altere para um email valido", getContext());
                    }
                    Utils.showToast("Ocorreu um erro, E-mail não enviado", getContext());
                }
            }
        });
    }

    private void loadUserInfo() {
        // set user wallet info
        TextView text = getView().findViewById(R.id.points_prizeCode_walletPoints);
        text.setText(userModel.getPoints() + " Pontos");
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
        if(AppSession.getInstance().currentFragmentTag.size() < 1)
            AppSession.getInstance().currentFragmentTag.push("points");

        // TODO: this was done to avoid crash, find better solution
        if(userModel == null || prizeModel == null)
            backButton();
        super.onResume();
    }
}