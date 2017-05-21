package com.example.rajat.medics;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajat on 18/4/17.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyFolderView>{

    List<String> name;
    Context ctx;
    public FolderAdapter(Context ct, List<String> a){
        ctx = ct;
        name = a;
    }

    @Override
    public FolderAdapter.MyFolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View v = layoutInflater.inflate(R.layout.folder_row, parent, false);
        return new MyFolderView(v);
    }

    @Override
    public void onBindViewHolder(FolderAdapter.MyFolderView holder, int position) {
        holder.t.setText(name.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }


    public class MyFolderView extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView t;
        public MyFolderView(View itemView) {
            super(itemView);
            t = (TextView) itemView.findViewById(R.id.foldername);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
//            Toast.makeText(ctx, "Position=" + position + " item = " + t.getText() + " Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ctx, TwolistAct.class);
            intent.putExtra("name", t.getText());
            intent.putExtra("uId", MainActivity.uId);
            ctx.startActivity(intent);
        }
    }

}
