package com.ck.zusha.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.zusha.R;
import com.github.barteksc.pdfviewer.PDFView;



/**
 * A simple {@link Fragment} subclass.
 */
public class RoadSigns extends Fragment {


    public RoadSigns() {
        // Required empty public constructor
    }

    PDFView pdfView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


       View v=inflater.inflate(R.layout.fragment_road_signs, container, false);

       pdfView=v.findViewById(R.id.readsigns);
       pdfView.fromAsset("the-highway-code-traffic-signs.pdf")
                .load();

       return v;
    }

}
