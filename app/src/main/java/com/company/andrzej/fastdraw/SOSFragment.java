package com.company.andrzej.fastdraw;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SOSFragment extends Fragment {

    private Context context;
    private SOSModule sosModule;

    public SOSFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sos, container, false);
        Button btn_exit = (Button) view.findViewById(R.id.btn_exit);
        Button btn_sos = (Button) view.findViewById(R.id.btn_sos);
        sosModule = new SOSModule();
        ((MainActivity) context).checkCameraPermission();
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).closeSOSFragment();
                ((MainActivity) context).showToolbarFragment();
            }
        });
        btn_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sosModule.blink(100, 50);


            }
        });
        return view;

    }
}
