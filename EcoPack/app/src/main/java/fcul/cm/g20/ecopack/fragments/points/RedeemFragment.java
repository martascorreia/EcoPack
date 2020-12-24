package fcul.cm.g20.ecopack.fragments.points;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import fcul.cm.g20.ecopack.Models.User;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.Models.Prize;
import fcul.cm.g20.ecopack.utils.Utils;

public class RedeemFragment extends Fragment {

    OnBackButtonPressed backCallback;
    ImageButton backButton;
    Prize prizeModel;
    User userModel;

    public RedeemFragment(){
        // Mandatory empty constructor
    }

    public RedeemFragment(User user, Prize prizeModel, OnBackButtonPressed callback) {
        this.prizeModel = prizeModel;
        this.userModel = user;
        backCallback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_points_redeem, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupLayout();
        setupRedeemButton();
        backButton();
    }

    private void setupRedeemButton() {
        Button redeemButton = getView().findViewById(R.id.points_redeem_redeemButton);
        redeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog();
            }
        });
    }

    private void confirmationDialog() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Está prestes a comprar este cupão, quer prosseguir?")
                .setPositiveButton("Comprar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean success = userModel.buyPrize(prizeModel);
                        if(success)
                            changeToPrizeCodeFragment();
                        else
                            Utils.showToast("Não tem pontos suficientes para comprar este cupão!", getContext());
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utils.showToast("Compra cancelada!", getContext());
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    private void changeToPrizeCodeFragment() {
        // go to redeem view
        PrizeCodeFragment prizeCodeFragment = new PrizeCodeFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_points_redeem, prizeCodeFragment)
                .addToBackStack("pointsRedeem")
                .commit();
    }

    private void setupLayout() {
        TextView userWalletValue = getView().findViewById(R.id.points_redeem_walletPoints);
        TextView prizeName = getView().findViewById(R.id.points_redeem_title);
        ImageView prizeImage = getView().findViewById(R.id.points_redeem_image);
        TextView prizeCost = getView().findViewById(R.id.points_redeem_cost);

        userWalletValue.setText(userModel.getPoints() + " Pontos");
        prizeName.setText(prizeModel.getTitle());
        prizeCost.setText(prizeModel.getCost() + " Pontos");
        prizeImage.setImageBitmap(prizeModel.getImage());
    }

    private void backButton() {
        backButton = getView().findViewById(R.id.points_redeem_back_button);
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


}
