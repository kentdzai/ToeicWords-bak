package com.groupthree.toeicword;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.groupthree.toeicword.controller.luyentap.LattuActivity;
import com.groupthree.toeicword.controller.luyentap.LuyenNgheActivity;
import com.groupthree.toeicword.controller.luyentap.LuyenNoiActivity;
import com.groupthree.toeicword.controller.luyentap.NgheLapActivity;
import com.groupthree.toeicword.controller.luyentap.TracNghiemActivity;

import java.util.ArrayList;

public class LuyenTapDialog extends Dialog implements AdapterView.OnItemClickListener {
    ListView lvLuyenTap;
    public Activity c;
    ArrayList<LuyenTap> arrL;
    AdapterLuyenTap adapter;

    public LuyenTapDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.luyen_tap_dialog);
        init();
    }

    private void init() {
        lvLuyenTap = (ListView) findViewById(R.id.lvLuyenTap);
        adapter = new AdapterLuyenTap(c.getApplicationContext(), R.layout.adapter_luyentap, setupLuyenTap());
        lvLuyenTap.setAdapter(adapter);
        lvLuyenTap.setOnItemClickListener(this);
    }

    public ArrayList<LuyenTap> setupLuyenTap() {
        arrL = new ArrayList<>();
        arrL.add(new LuyenTap(R.mipmap.ic_nghelap, "Nghe Lặp"));
        arrL.add(new LuyenTap(R.mipmap.ic_lattu, "Lật Từ"));
        arrL.add(new LuyenTap(R.mipmap.ic_tracnghiem, "Trắc Nghiệm"));
        arrL.add(new LuyenTap(R.mipmap.ic_luyennghe, "Luyện Nghe"));
        arrL.add(new LuyenTap(R.mipmap.ic_luyennoi, "Luyện Nói"));
        return arrL;
    }

    public void handleStartActivity(Class<?> cls) {
        c.startActivity(new Intent(c, cls));
        c.overridePendingTransition(R.anim.xin_from, R.anim.xin_to);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                handleStartActivity(NgheLapActivity.class);
                break;
            case 1:
                handleStartActivity(LattuActivity.class);
                break;
            case 2:
                handleStartActivity(TracNghiemActivity.class);
                break;
            case 3:
                handleStartActivity(LuyenNgheActivity.class);
                break;
            case 4:
                handleStartActivity(LuyenNoiActivity.class);
                break;
            default:
                dismiss();
                break;
        }
    }

    public class AdapterLuyenTap extends ArrayAdapter<LuyenTap> {
        int idLayout;
        ArrayList<LuyenTap> arrL;
        ImageView icluyentap;
        TextView tvLuyenTap;

        public AdapterLuyenTap(Context context, int idLayout, ArrayList<LuyenTap> arrL) {
            super(context, idLayout, arrL);
            this.idLayout = idLayout;
            this.arrL = arrL;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
            icluyentap = (ImageView) v.findViewById(R.id.icluyentap);
            tvLuyenTap = (TextView) v.findViewById(R.id.tvLuyenTap);
            LuyenTap lt = arrL.get(position);
            icluyentap.setImageResource(lt.icon);
            tvLuyenTap.setText(lt.title);
            return v;
        }
    }

    class LuyenTap {
        int icon;
        String title;

        public LuyenTap(int icon, String title) {
            this.icon = icon;
            this.title = title;
        }
    }
}
