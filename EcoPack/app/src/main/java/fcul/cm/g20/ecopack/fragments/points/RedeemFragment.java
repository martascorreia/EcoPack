package fcul.cm.g20.ecopack.fragments.points;

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

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.points.model.Prize;

public class RedeemFragment extends Fragment {

    ImageButton backButton;
    Prize prize;
    int userWallet;

    public RedeemFragment(){
        // Mandatory empty constructor
    }

    public RedeemFragment(int userPoints, Prize prize) {
        this.prize = prize;
        this.userWallet = userPoints;
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
                PrizeCodeFragment prizeCodeFragment = new PrizeCodeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_points_redeem, prizeCodeFragment)
                        .addToBackStack("pointsRedeem")
                        .commit();
            }
        });
    }

    private void setupLayout() {
        TextView userWalletValue = getView().findViewById(R.id.points_redeem_walletPoints);
        TextView prizeName = getView().findViewById(R.id.points_redeem_title);
        ImageView prizeImage = getView().findViewById(R.id.points_redeem_image);
        TextView prizeCost = getView().findViewById(R.id.points_redeem_cost);

        userWalletValue.setText(userWallet + " Pontos");
        prizeName.setText(prize.getTitle());
        prizeCost.setText(prize.getCost() + " Pontos");
        prizeImage.setImageBitmap(prize.getImage());
    }

    private void backButton() {
        backButton = getView().findViewById(R.id.points_redeem_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                fm.popBackStack ("points", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }
}
