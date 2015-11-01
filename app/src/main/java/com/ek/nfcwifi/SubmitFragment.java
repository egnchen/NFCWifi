package com.ek.nfcwifi;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by eyeKill on 15/10/31.
 */
public class SubmitFragment extends Fragment {

    private NfcAdmin nfcAdmin;

    ListView list;
    Button btnSubmit;
    LinearLayout listUnit;

    ArrayList<NfcAdmin.myNfcRecord> records=new ArrayList<NfcAdmin.myNfcRecord>();


    public void addContent(NfcAdmin.myNfcRecord record) {
        records.add(record);
        //if(list!=null) Toast.makeText(this.getActivity(),list.getCount(),Toast.LENGTH_SHORT).show();
    }


    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                LinearLayout ll=(LinearLayout)inflater.inflate(R.layout.submit_unit,list,false);
                ImageView pic=(ImageView)ll.getChildAt(0);
                TextView tvName=(TextView)(((LinearLayout) ll.getChildAt(1)).getChildAt(0));
                TextView tvSub=(TextView)(((LinearLayout) ll.getChildAt(1)).getChildAt(1));
                switch(records.get(position).msgType){
                    case NfcAdmin.myNfcRecord.EKNFC_TYPE_WIFI:
                        pic.setImageDrawable(getResources().getDrawable(R.mipmap.wifi_enabled));
                        tvName.setText("WIFI");
                        break;
                    case NfcAdmin.myNfcRecord.EKNFC_TYPE_URL:
                        pic.setImageDrawable(getResources().getDrawable(R.mipmap.url));
                        tvName.setText("URL");
                        break;
                    case NfcAdmin.myNfcRecord.EKNFC_TYPE_STARTAPP:
                        pic.setImageDrawable(getResources().getDrawable(R.mipmap.app));
                        tvName.setText("APP");
                        break;
                    default:
                }
                tvSub.setText("    "+records.get(position).toString());
                return ll;
            }
        });
        return view;
    }
}
