package com.ek.nfcwifi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.StackView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by eyeKill on 15/10/31.
 */
public class SubmitFragment extends Fragment {

    private NfcAdmin nfcAdmin;
    MainActivity dad;

    StackView stacklist;
    Button btnSubmit,btnErase;
    TextView tvIndex,tvContent,tvType;
    int idOffset=106;

    ArrayList<NfcAdmin.myNfcRecord> records=new ArrayList<NfcAdmin.myNfcRecord>();


    public void addContent(NfcAdmin.myNfcRecord record) {
        records.add(record);
        dad.getNfcAdmin().appendRecord(record);
        stacklist.postInvalidate();
        //if(stacklist!=null) Toast.makeText(this.getActivity(),stacklist.getCount(),Toast.LENGTH_SHORT).show();
    }


    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dad=(MainActivity)getActivity();
        nfcAdmin = ((MainActivity) getActivity()).getNfcAdmin();
        View view = inflater.inflate(R.layout.fragment_submit, container, false);
        stacklist=(StackView)view.findViewById(R.id.submit_stackList);
        btnSubmit=(Button)view.findViewById(R.id.submit_btnSubmit);
        btnErase=(Button)view.findViewById(R.id.submit_btnErase);
        tvContent=(TextView)view.findViewById(R.id.submit_tvContent);
        tvIndex=(TextView)view.findViewById(R.id.submit_tvIndex);
        tvType=(TextView)view.findViewById(R.id.submit_tvType);
        TextView tv=new TextView(dad);
        tv.setText("NOTHING TO SHOW");
        tv.setTextSize(40);
        stacklist.setEmptyView(tv);
        stacklist.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return records.size();
            }

            @Override
            public Object getItem(int position) {
                return records.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                //main part
                ImageView img = new ImageView(dad);
                img.setMinimumWidth(300);
                img.setMinimumHeight(300);
                switch (records.get(position).getmsgType()) {
                    case NfcAdmin.myNfcRecord.EKNFC_TYPE_WIFI:
                        img.setBackground(getResources().getDrawable(R.mipmap.wifi_unit));
                        break;
                    case NfcAdmin.myNfcRecord.EKNFC_TYPE_URL:
                        img.setBackground(getResources().getDrawable(R.mipmap.url_unit));
                        break;
                    case NfcAdmin.myNfcRecord.EKNFC_TYPE_STARTAPP:
                        //TODO get an image of app
                        break;
                    default:
                }
                return img;
            }
        });
        stacklist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvContent.setText(records.get(position).toString());
                tvType.setText(records.get(position).getmsgTypeString());
                tvIndex.setText((position + 1) + "/" + parent.getCount());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        stacklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvContent.setText(records.get(position).toString());
                tvType.setText(records.get(position).getmsgTypeString());
                tvIndex.setText((position + 1) + "/" + parent.getCount());
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dad.setToWrite();
            }
        });
        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stacklist.removeViewAt(stacklist.getSelectedItemPosition());
            }
        });
        return view;
    }
}
