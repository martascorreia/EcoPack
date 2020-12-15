package fcul.cm.g20.ecopack.ui.fragments.points;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.ui.fragments.points.model.Prize;
import fcul.cm.g20.ecopack.ui.fragments.points.recyclerview.GridItemDecorator;
import fcul.cm.g20.ecopack.ui.fragments.points.recyclerview.PrizesAdapter;

public class PointsFragment extends Fragment {

    FloatingActionButton addPointsButton;

    final int CAMERA_REQUEST_CODE = 98;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pointsFragmentView = inflater.inflate(R.layout.fragment_points_v2, container, false);

        // get the reference of RecyclerView
        RecyclerView gridRecyclerView = (RecyclerView) pointsFragmentView.findViewById(R.id.grid_points_prizes_container);

        // set a GridLayoutManager with 3 number of columns , horizontal gravity and false value for reverseLayout to show the items from start to end
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
        gridRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        // TODO: LOAD FROM FIREBASE

        Bitmap example = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.image_temp_1);

        LinkedList<Prize> prizes = new LinkedList<>();
        prizes.add(new Prize("Pizza", 5 , example));
        prizes.add(new Prize("Bananas", 2,example));
        prizes.add(new Prize("Cadeira", 10,example));
        prizes.add(new Prize("Café Starbucks", 4,example));
        prizes.add(new Prize("Bubble tea",3,example));
        prizes.add(new Prize("Jumbo juice",3 ,example));
        prizes.add(new Prize("Liberdade", 400,example));
        prizes.add(new Prize("Um abraço", 80085,example));

        gridRecyclerView.addItemDecoration(new GridItemDecorator(20,2));

        PrizesAdapter prizeAdapter = new PrizesAdapter(prizes);
        gridRecyclerView.setAdapter(prizeAdapter);

//        prizeAdapter.setOnLocationClickListener(new PrizesAdapter.OnLocationClickListener() {
//            @Override
//            public void onLocationClick(int position) {
//                Toast t = Toast.makeText(getContext(), "Location " + position, Toast.LENGTH_SHORT);
//                t.show();
//            }
//        });

        return pointsFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
}