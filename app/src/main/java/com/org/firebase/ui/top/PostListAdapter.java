package com.org.firebase.ui.top;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Color;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.org.firebase.R;
import com.org.firebase.ui.post.PostModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.MyViewHolder> {
    private Context context;
    private List<PostModel> postList = new ArrayList<>();
    public OnItemClickListeners onItemClickListeners;
    public OnItemClickListenersForDelete onItemClickListenersForDelete;
    private String currentUserId = "";

    public PostListAdapter(FragmentActivity activity, OnItemClickListeners onItemClick, OnItemClickListenersForDelete onItemClickDelete,String userId) {
        this.context = activity;
        this.onItemClickListeners = onItemClick;
        this.onItemClickListenersForDelete = onItemClickDelete;
        this.currentUserId = userId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        ButterKnife.bind(this, view);
        return new MyViewHolder(view);
    }

    public interface OnItemClickListeners {
        void OnLikeClickListener(PostModel postModel);
    }
    public interface OnItemClickListenersForDelete {
        void OnLikeClickListenerDelete(PostModel postModel);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setBindData(postList.get(position));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setList(List<PostModel> postModelList) {
        if (postModelList == null) {
            return;
        }
        postList.clear();
        postList.addAll(postModelList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_cutomer_name)
        TextView txtCustomerName;
        @BindView(R.id.txt_date)
        TextView txtLastSeen;
        @BindView(R.id.img_added)
        ImageView imgAdded;
        @BindView(R.id.txt_commit)
        TextView txtComment;
        @BindView(R.id.img_like)
        ImageView imgLike;
        @BindView(R.id.txt_like)
        TextView txtTotalLikes;
        @BindView(R.id.bt_delete_post)
        Button btdelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setBindData(PostModel postModel) {
            txtCustomerName.setText(postModel.getUserName());
            txtLastSeen.setText(postModel.getDate());
            txtComment.setText(postModel.getPostContent());

            if (currentUserId.equals(postModel.getUserId())){
                btdelete.setEnabled(true);
                btdelete.setBackgroundColor(Color.RED);
            }else{ btdelete.setEnabled(false);
                btdelete.setBackgroundColor(Color.GRAY);
            }


            Glide.with(context).load(R.drawable.ic_heart_empty).into(imgLike);
            if (postModel.getPostLikedUser() != null && postModel.getPostLikedUser().size() > 0) {
                for (int i = 0; i < postModel.getPostLikedUser().size(); i++) {
                    if (postModel.getPostLikedUser().get(i).equals(currentUserId)) {
                        Glide.with(context).load(R.drawable.ic_heart_filled).into(imgLike);
                    }
                }

                txtTotalLikes.setText(postModel.getPostLikedUser().size() + " likes");
            } else {
                txtTotalLikes.setText("0 likes");
            }
            if (TextUtils.isEmpty(postModel.getImagepath())) {
                imgAdded.setVisibility(View.GONE);
            } else {
                imgAdded.setVisibility(View.VISIBLE);
                Uri uri = Uri.parse(postModel.getImagepath());
                Glide.with(context).load(uri).into(imgAdded);
            }
        }

        @OnClick({R.id.img_like,R.id.bt_delete_post})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_like:
                    int pos = getAdapterPosition();
                    if (onItemClickListeners != null) {
                        onItemClickListeners.OnLikeClickListener(postList.get(pos));
                    }
                    break;
                    case R.id.bt_delete_post:
                    int pos1 = getAdapterPosition();
                    if (onItemClickListeners != null) {
                        onItemClickListenersForDelete.OnLikeClickListenerDelete(postList.get(pos1));
                    }
                        break;
            }
        }
    }
}
