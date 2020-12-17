package fcul.cm.g20.ecopack.ui.fragments.points;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.ui.fragments.points.model.Prize;
import fcul.cm.g20.ecopack.ui.fragments.points.recyclerview.GridItemDecorator;
import fcul.cm.g20.ecopack.ui.fragments.points.recyclerview.PrizesAdapter;
import fcul.cm.g20.ecopack.utils.Utils;

public class PointsFragment extends Fragment {

    FloatingActionButton addPointsButton;
    int userPoints = 0;

    final int CAMERA_REQUEST_CODE = 98;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pointsFragmentView = inflater.inflate(R.layout.fragment_points, container, false);

        // get the reference of RecyclerView
        RecyclerView gridRecyclerView = (RecyclerView) pointsFragmentView.findViewById(R.id.grid_points_prizes_container);

        // set a GridLayoutManager with 3 number of columns , horizontal gravity and false value for reverseLayout to show the items from start to end
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
        gridRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        LinkedList<Prize> prizes = generateDummyData();
        // LOAD FROM FIREBASE

        // get User points info
            // what he as bought
            // points left

        // get prizes


        gridRecyclerView.addItemDecoration(new GridItemDecorator(20,2));

        final PrizesAdapter prizeAdapter = new PrizesAdapter(prizes);
        gridRecyclerView.setAdapter(prizeAdapter);

        prizeAdapter.setOnPrizeClickListener(new PrizesAdapter.OnPrizeClickListener() {
            @Override
            public void onPrizeClickListener(int position) {
                Prize prize = prizeAdapter.getPrize(position);
                if(prize == null)
                    return;
                RedeemFragment redeemFragment = new RedeemFragment(userPoints, prize);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_points, redeemFragment)
                        .addToBackStack("points")
                        .commit();
            }
        });

        return pointsFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set user wallet info
        TextView userWalletValue = getView().findViewById(R.id.points_userWalletValue);
        userWalletValue.setText(userPoints + " Pontos");

        addPointsButton = getView().findViewById(R.id.add_points_button);
        addPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Permissions
                if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                }

                CameraFragment cameraFragment = new CameraFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_points, cameraFragment)
                        .addToBackStack("points")
                        .commit();
            }
        });

    }

    private LinkedList<Prize> generateDummyData() {
        LinkedList<Prize> prizes = new LinkedList<>();
        Bitmap fitness_Icon = Utils.getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_temp_fitness);
        Bitmap heathcare_Icon = Utils.getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_temp_heathcare);
        Bitmap mcdonalds_Icon = Utils.getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_temp_mcdonalds);
        Bitmap pizza_Icon = Utils.getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_temp_pizza);
        Bitmap starbucks_Icon = Utils.getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_temp_starbucks);
        prizes.add(new Prize("Fitness", 8, fitness_Icon));
        prizes.add(new Prize("Heathcare", 100, heathcare_Icon));
        prizes.add(new Prize("Mc'Donalds", 2, mcdonalds_Icon));
        prizes.add(new Prize("Pizza", 4, pizza_Icon));
        prizes.add(new Prize("Starbucks", 3, starbucks_Icon));

        userPoints = 16;
        return prizes;
    }
}