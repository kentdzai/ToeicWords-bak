package com.groupthree.toeicword.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupthree.toeicword.DetailsWord;
import com.groupthree.toeicword.HocTuActivity;
import com.groupthree.toeicword.ListWordWithSubjectActivity;
import com.groupthree.toeicword.R;
import com.groupthree.toeicword.model.DatabaseWord;
import com.groupthree.toeicword.model.ListWord;

import java.util.ArrayList;

public class AdapterListWordWithListView extends ArrayAdapter<ListWord> {
    ArrayList<ListWord> arrL;
    int idLayout;
    DatabaseWord db;
    Context context;

    public AdapterListWordWithListView(Context context, int idLayout, ArrayList<ListWord> arrL, DatabaseWord db) {
        super(context, idLayout, arrL);
        this.arrL = arrL;
        this.idLayout = idLayout;
        this.db = db;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderListWord vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
            vh = new ViewHolderListWord(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolderListWord) convertView.getTag();
        }

        ListWord lw = arrL.get(position);

        vh.tvMean.setText(lw.Mean);
        vh.tvWord.setText(lw.Word);
        vh.cbListWord.setChecked(lw.getCheck());

        vh.cbListWord.setTag(lw);

        vh.cbListWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ListWord lw = (ListWord) cb.getTag();
                if (!cb.isChecked()) {
                    cb.setChecked(false);
                    lw.setCheck(false);
                } else {
                    cb.setChecked(true);
                    lw.setCheck(true);
                }
                db.updateFavourite(lw);
            }
        });

        vh.lnListWord.setTag(lw);
        vh.lnListWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ln = (LinearLayout) v;
                ListWord l = (ListWord) ln.getTag();
                Intent it = new Intent(v.getContext(), DetailsWord.class);
                Activity activity = (Activity) context;
                if (activity instanceof HocTuActivity) {
                    it.putExtra("from", "HOC_TU");
                }
                if (activity instanceof ListWordWithSubjectActivity) {
                    it.putExtra("from", "LIST_WORD");
                }
                it.putExtra("Id", l.Id);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(it);
                activity.overridePendingTransition(R.anim.xin_from, R.anim.xin_to);
                activity.finish();
            }
        });

        return convertView;
    }

    class ViewHolderListWord {
        CheckBox cbListWord;
        TextView tvWord, tvMean;
        LinearLayout lnListWord;

        public ViewHolderListWord(View v) {
            cbListWord = (CheckBox) v.findViewById(R.id.cbListWord);
            tvWord = (TextView) v.findViewById(R.id.tvWord);
            tvMean = (TextView) v.findViewById(R.id.tvMean);
            lnListWord = (LinearLayout) v.findViewById(R.id.lnListWord);
        }
    }
}