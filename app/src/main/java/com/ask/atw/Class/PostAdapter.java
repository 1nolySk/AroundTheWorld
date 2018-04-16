package com.ask.atw.Class;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ask.atw.R;
import com.squareup.picasso.Picasso;



import java.util.List;

/**
 * Created by DELL on 29-Mar-18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<ImageData> mDataset;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView img;
        TextView title,author,desc,date;
        RelativeLayout layout;
        public ViewHolder(View v)
        {
            super(v);
            img= v.findViewById(R.id.post_img);
            title=v.findViewById(R.id.post_head);
            author =v.findViewById(R.id.post_user);
            desc=v.findViewById(R.id.post_desc);
            date=v.findViewById(R.id.post_date);
            layout=v.findViewById(R.id.post_rel);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PostAdapter(Context myContext, List<ImageData> myDataset) {
        mContext=myContext;
        mDataset = myDataset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card, parent, false);

        return new ViewHolder(v);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        //new LoadImg(mContext,holder.img,mDataset.get(position).getUrl()).setImage();
        Picasso.get()
                .load(mDataset.get(position).getUrl())
                .placeholder(R.drawable.loadtext)
                .error(R.drawable.errortext)
                .into(holder.img);



        holder.title.setText(mDataset.get(position).getPlace());
        holder.desc.setText(mDataset.get(position).getDesc());
        holder.desc.setMaxLines(2);
        holder.author.setText(mDataset.get(position).getAuthor());
        if(mDataset.get(position).getGender().equalsIgnoreCase("male")){
        holder.author.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_male, 0, 0, 0);
        }
        else if(mDataset.get(position).getGender().equalsIgnoreCase("female")){
            holder.author.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_female, 0, 0, 0);
    }
        holder.date.setText(mDataset.get(position).getDate());

        Log.d("-----adapter",holder.title.getText().toString());
        /*
                        holder.Details.addView(textView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}