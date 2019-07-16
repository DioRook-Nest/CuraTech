package com.example.ishmaamin.curatech;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

public class EhrAdapter extends RecyclerView.Adapter<EhrAdapter.ViewHolder> {

public List<post>list;
public Context context;




    public EhrAdapter(List<post> list){ this.list=list;}


    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        String image_url=list.get(position).getImage_url();
        holder.setimage(image_url);

        long millisecond=list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();

        holder.setTime(dateString);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

private ImageView imageView;
private View myview;
private TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            myview=itemView;
        }

        public void setimage(String downloaduri){
            imageView=myview.findViewById(R.id.post_image);
            Glide.with(context).load(downloaduri).into(imageView);
        }

        public void setTime(String date){
                time=myview.findViewById(R.id.post_date);
                time.setText(date);
        }
    }
}
