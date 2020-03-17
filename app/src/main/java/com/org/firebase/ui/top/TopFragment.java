package com.org.firebase.ui.top;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.org.firebase.R;
import com.org.firebase.app.AppController;
import com.org.firebase.app.AppPreference;
import com.org.firebase.base.BaseFragment;
import com.org.firebase.ui.post.PostModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopFragment extends BaseFragment implements PostListAdapter.OnItemClickListeners,PostListAdapter.OnItemClickListenersForDelete {


    List<PostModel> postModelList = new ArrayList<>();
    private DatabaseReference database;
    private AppPreference appPreference;
    private PostListAdapter postListAdapter;
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rv_post_recycle)
    RecyclerView rvPostRecycle;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);
        ButterKnife.bind(this, view);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        database = firebaseDatabase.getReference();
        appPreference = AppController.getInstance().getAppPreference();
        currentUserId = appPreference.getUserId();
        postListAdapter = new PostListAdapter(getActivity(), this,this, currentUserId);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPostRecycle.setLayoutManager(linearLayoutManager);
        rvPostRecycle.setAdapter(postListAdapter);
        getallInboxMessage();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    private void getallInboxMessage() {
        postModelList.clear();
        try {
            FirebaseDatabase.getInstance().getReference().child("commonposts")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                PostModel postModel = dataSnapshot1.getValue(PostModel.class);
                                postModelList.add(postModel);
                            }
                            if (postModelList.size() > 0) {

                                Collections.sort(postModelList, new Comparator<PostModel>() {
                                    public int compare(PostModel o1, PostModel o2) {
                                        if (o1.getDateWithTime() == null || o2.getDateWithTime() == null)
                                            return 0;
                                        return o1.getDateWithTime().compareTo(o2.getDateWithTime());
                                    }
                                });

                                Collections.reverse(postModelList);

                                postListAdapter.setList(postModelList);
                            } else {
                                Toast.makeText(mActivity, " no more list" + postModelList.size(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(mActivity, "try agin", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {

        }


    }

    @Override
    public void OnLikeClickListener(PostModel postModel) {
        setLikeButton(postModel);

    }

    @Override
    public void OnLikeClickListenerDelete(PostModel postModel) {
        DatabaseReference databaserc = FirebaseDatabase.getInstance().getReference()
                .child("commonposts").child(postModel.getPostId());
        databaserc.removeValue();
        Toast.makeText(mActivity, "Post deleted successfully", Toast.LENGTH_SHORT).show();

        getallInboxMessage();
    }

    private void setLikeButton(PostModel postModel) {


        FirebaseDatabase.getInstance().getReference().child("commonposts").child(postModel.getPostId()).child("postLikedUser")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> postLikedUser = new ArrayList<>();
                        boolean isCurrentUserLike = false;

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Object sd = dataSnapshot1.getValue();
                            String sdd = String.valueOf(sd);

                            if (!appPreference.getUserId().equals(sdd)) {
                                postLikedUser.add(sdd);
                            } else {
                                isCurrentUserLike = true;
                            }
                        }
                        if (!isCurrentUserLike) {
                            postLikedUser.add(appPreference.getUserId());
                        }
                        database.child("commonposts").child(postModel.getPostId()).child("postLikedUser").setValue(postLikedUser);
                        getallInboxMessage();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }



}
