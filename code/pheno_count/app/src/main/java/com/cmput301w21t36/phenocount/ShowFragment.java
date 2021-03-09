package com.cmput301w21t36.phenocount;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/*
get from the lab, need to be changed later, since this ons is still
for adding experiments
 */

public class ShowFragment extends DialogFragment {
    private EditText cityName;
    private EditText provinceName;
    private City cityTarget;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressedAdd(City newCity);
        void onOkPressedEdit(City editedCity);
    }

    public AddCityFragment(City city) {
        this.cityTarget = city;
    }

    public AddCityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_city_fragment_layout, null);
        cityName = view.findViewById(R.id.city_name_editText);
        provinceName = view.findViewById(R.id.province_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add/Edit City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (cityTarget == null) {
                            cityTarget = new City();
                            String city = cityName.getText().toString();
                            String province = provinceName.getText().toString();
                            cityTarget.setCity(city);
                            cityTarget.setProvince(province);
                            listener.onOkPressedAdd(cityTarget);
                        }else{
                            String city = cityName.getText().toString();
                            String province = provinceName.getText().toString();
                            cityTarget.setCity(city);
                            cityTarget.setProvince(province);
                            listener.onOkPressedEdit(cityTarget);
                        }
                    }}).create();
    }
}

