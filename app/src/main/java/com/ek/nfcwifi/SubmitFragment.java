package com.ek.nfcwifi;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by eyeKill on 15/10/31.
 */
public class SubmitFragment extends Fragment {

    private NfcAdmin nfcAdmin;
    MainActivity dad;

    ListView list;
    Button btnSubmit;

    ArrayList<NfcAdmin.myNfcRecord> records=new ArrayList<NfcAdmin.myNfcRecord>();


    public void addContent(NfcAdmin.myNfcRecord record) {
        records.add(record);
        dad.getNfcAdmin().appendRecord(record);
        //if(list!=null) Toast.makeText(this.getActivity(),list.getCount(),Toast.LENGTH_SHORT).show();
    }


    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dad=(MainActivity)getActivity();
        nfcAdmin = ((MainActivity) getActivity()).getNfcAdmin();
        View view = inflater.inflate(R.layout.fragment_submit, container, false);
        list=(ListView)view.findViewById(R.id.submit_list);
        btnSubmit=(Button)view.findViewById(R.id.submit_btnSubmit);
        list.setAdapter(new BaseAdapter() {
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
                LinearLayout ll=(LinearLayout)inflater.inflate(R.layout.submit_unit, list, false);
                TextView tvName=(TextView)ll.getChildAt(0);
                TextView tvSub=(TextView)ll.getChildAt(1);
                switch(records.get(position).msgType){
                    case NfcAdmin.myNfcRecord.EKNFC_TYPE_WIFI:
                        ll.setBackgroundColor(Color.BLUE);
                        tvName.setText("WIFI");
                        break;
                    case NfcAdmin.myNfcRecord.EKNFC_TYPE_URL:
                        ll.setBackgroundColor(Color.RED);
                        tvName.setText("URL");
                        break;
                    case NfcAdmin.myNfcRecord.EKNFC_TYPE_STARTAPP:
                        ll.setBackgroundColor(Color.GREEN);
                        tvName.setText("APP");
                        break;
                    default:
                }
                tvSub.setText("    "+records.get(position).toString());
                return ll;
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dad.setToWrite();
            }
        });
        return view;
    }
}
