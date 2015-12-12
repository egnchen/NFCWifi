package com.ek.nfcwifi;


import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by eyeKill on 15/10/31.
 */
public class UrlFragment extends WriteContentFragment {
    ImageView url;
    Button btnWww, btnCom, btnCn;
    Button btnSubmit;
    EditText etUrl;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_url, container, false);
        url = (ImageView) view.findViewById(R.id.writeUrl_image);
        btnWww = (Button) view.findViewById(R.id.writeUrl_btnwww);
        btnCom = (Button) view.findViewById(R.id.wirteUrl_com);
        btnCn = (Button) view.findViewById(R.id.writeUrl_cn);
        btnSubmit = (Button) view.findViewById(R.id.writeUrl_btnSubmit);
        etUrl = (EditText) view.findViewById(R.id.writeUrl_etUrl);


        btnWww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUrl.setText("www." + etUrl.getText());etUrl.setSelection(4);
            }
        });
        btnCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUrl.append(".com");
            }
        });
        btnCn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUrl.append(".cn");
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String u=etUrl.getText().toString();
                    if(!(u.startsWith("http://")||u.startsWith("https://")))
                        u="http://"+u;
                    URL url = new URL(u);
                    //Toast.makeText(dad,"prot="+url.getProtocol()+" host="+url.getHost(),Toast.LENGTH_SHORT).show();
                    submit(nfcAdmin.getRecord_url(u));
                } catch (MalformedURLException e) {
                    Toast.makeText(dad.getApplicationContext(), "Invalid url string.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}