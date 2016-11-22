package com.l2minigames.wanderfulworld;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PickupFragment extends DialogFragment {

    ImageButton closeButton;
    TextView itemType;
    ImageView pickImage;


    public PickupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pickup, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        closeButton = (ImageButton)view.findViewById(R.id.closeButton);
        itemType = (TextView)view.findViewById(R.id.itemType);
        pickImage = (ImageView)view.findViewById(R.id.pickImage);
        String markerType = getArguments().getString("itemtype");
        if (markerType.equals("earth")){
            itemType.setText(getResources().getString(R.string.a_plant));
            pickImage.setBackgroundResource(R.drawable.earth_item);

        }
        else if (markerType.equals("fire")){
            itemType.setText(getResources().getString(R.string.a_flame));
            pickImage.setBackgroundResource(R.drawable.fire_item);
        }
        else if (markerType.equals("air")){
            itemType.setText(getResources().getString(R.string.trombulus));
            pickImage.setBackgroundResource(R.drawable.air_item);
        }
        else if (markerType.equals("water")){
            itemType.setText(getResources().getString(R.string.a_waterdrop));
            pickImage.setBackgroundResource(R.drawable.water_item);
        }
        else if (markerType.equals("scroll")){
            itemType.setText(getResources().getString(R.string.an_ancient_scrollifix));
            pickImage.setBackgroundResource(R.drawable.scroll_item);
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
