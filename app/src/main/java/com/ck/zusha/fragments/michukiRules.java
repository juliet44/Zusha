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
public class michukiRules extends Fragment {


    public michukiRules() {
        // Required empty public constructor
    }
    PDFView pdfViewer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_michuki_rules, container, false);
        pdfViewer =v.findViewById(R.id.michukirules);
        pdfViewer.fromAsset("michukirules.pdf")
                .load();
        return v;
    }

}
