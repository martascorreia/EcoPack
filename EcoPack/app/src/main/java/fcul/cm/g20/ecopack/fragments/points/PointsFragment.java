package fcul.cm.g20.ecopack.fragments.points;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import fcul.cm.g20.ecopack.mappers.PrizeMapper;
import fcul.cm.g20.ecopack.mappers.UserMapper;
import fcul.cm.g20.ecopack.models.Prize;
import fcul.cm.g20.ecopack.models.User;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.points.recyclerview.GridItemDecorator;
import fcul.cm.g20.ecopack.fragments.points.recyclerview.PrizesAdapter;
import fcul.cm.g20.ecopack.utils.Utils;

import static fcul.cm.g20.ecopack.utils.Utils.showToast;

public class PointsFragment extends Fragment {

    private FloatingActionButton addPointsButton;
    private final int CAMERA_REQUEST_CODE = 98;

    View pointsFragmentView;

    private User user;
    private List<Prize> prizes;

    OnBackButtonPressed backListener;

    PrizesAdapter prizeAdapter;
    RecyclerView gridRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pointsFragmentView = inflater.inflate(R.layout.fragment_points, container, false);
        backListener = user -> onBackActions(user);
        return pointsFragmentView;
    }

    private void setOnClickCameraFragment() {
        addPointsButton = getView().findViewById(R.id.add_points_button);
        addPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    showToast("Não foi possível aceder à câmera. Por favor, dê as permissões necessárias.", getContext());
                    return;
                }

                CameraFragment cameraFragment = new CameraFragment(user, backListener);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_points, cameraFragment)
                        .addToBackStack("points")
                        .commit();
            }
        });
    }

    private void loadRecyclerView() {
        // get the reference of RecyclerView
        gridRecyclerView = (RecyclerView) pointsFragmentView.findViewById(R.id.grid_points_prizes_container);

        // set a GridLayoutManager with 2 number of columns , horizontal gravity and false value for reverseLayout to show the items from start to end
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        gridRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        gridRecyclerView.addItemDecoration(new GridItemDecorator(10, 2));
        gridRecyclerView.setAdapter(new PrizesAdapter(new ArrayList<>())); //dummy empty recycler
    }

    @SuppressLint("NewApi")
    private void loadPrizesToRecyclerView() {
        if (prizes != null && !prizes.isEmpty() && user != null) {

            // filter all prizes already bought
            List<Prize> prizesToShow = prizes.stream()
                    .filter(prize ->
                            user.getRedeemedPrizesIds() == null ||
                                    user.getRedeemedPrizesIds().isEmpty() ||
                                    user.getRedeemedPrizesIds()
                                            .stream()
                                            .allMatch(redeemedPrizeId -> !redeemedPrizeId.equals(prize.getTitle())))
                    .collect(Collectors.toList());

            // Disable prizes that the user can't buy
            prizesToShow.forEach(prize -> prize.setIsDisable(prize.getCost() > user.getPoints()));

            prizeAdapter = new PrizesAdapter(prizesToShow);
            gridRecyclerView.setAdapter(prizeAdapter);

            prizeAdapter.setOnPrizeClickListener(new PrizesAdapter.OnPrizeClickListener() {
                @Override
                public void onPrizeClickListener(int position) {
                    Prize prizeModel = prizeAdapter.getPrize(position);
                    if (prizeModel == null)
                        return;
                    RedeemFragment redeemFragment = new RedeemFragment(user, prizeModel, backListener);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_points, redeemFragment)
                            .addToBackStack("points")
                            .commit();
                }
            });
        }
    }

    private LinkedList<Prize> generateDummyData() {
        LinkedList<Prize> prizeModels = new LinkedList<>();
        Bitmap fitness_Icon = Utils.getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_temp_fitness);
        Bitmap heathcare_Icon = Utils.getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_temp_heathcare);
        Bitmap mcdonalds_Icon = Utils.getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_temp_mcdonalds);
        Bitmap pizza_Icon = Utils.getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_temp_pizza);
        Bitmap starbucks_Icon = Utils.getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_temp_starbucks);
        prizeModels.add(new Prize("Fitness", 8, fitness_Icon));
        prizeModels.add(new Prize("Heathcare", 100, heathcare_Icon));
        prizeModels.add(new Prize("McDonald's", 2, mcdonalds_Icon));
        prizeModels.add(new Prize("Pizza", 4, pizza_Icon));
        prizeModels.add(new Prize("Starbucks", 3, starbucks_Icon));

        return prizeModels;
    }

    // ONLY USED BY US TO MAKE NEW PRIZES!!!!!!!!!!
    private void savePrizesToFireStore() {
        PrizeMapper.savePrizes(generateDummyData());
    }

    private void getPrizes() {
        PrizeMapper.getPrizes(new PrizeMapper.OnCompleteSuccessful() {
            @Override
            public void onSuccess(List<Prize> u) {
                prizes = u;
                // set prizes fragments
                loadPrizesToRecyclerView();
            }
        });
    }

    private void getUserInfo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString("username", "");

        UserMapper.getUserByUserName(username, getContext(), new UserMapper.OnCompleteSuccessful() {
            @Override
            public void onSuccess(User u) {
                if (u == null)
                    Utils.showToast("Não foi possível obter a informação do utilizador. Por favor, tente mais tarde.", getContext());
                else {
                    user = u;
                    // set user wallet info
                    TextView userWalletValue = getView().findViewById(R.id.points_userWalletValue);
                    userWalletValue.setText(user.getPoints() + " Pontos");
                    // get prizes
                    getPrizes();
                    // set camera fragment
                    setOnClickCameraFragment();
                }
            }
        });
    }

    private void onBackActions(User u) {
        // get user again from firebase;
        getUserInfo();
    }

    @Override
    public void onResume() {
        loadRecyclerView();
        getUserInfo();
        super.onResume();
    }
}
