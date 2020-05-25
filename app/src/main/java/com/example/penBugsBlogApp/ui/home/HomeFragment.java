package com.example.penBugsBlogApp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penBugsBlogApp.Adaptors.PostAdaptor;
import com.example.penBugsBlogApp.Models.Post;
import com.example.penBugsBlogApp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView postRecyclerView;
    PostAdaptor postAdaptor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Post> postList; // getting the list from the Post getter and Setter

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        // From Post Adaptor and Recyclerview v set the layout and get the reference from data base EX : Posts
        View fragmentViewNewFeeds = inflater.inflate(R.layout.new_feeds_fragment, container, false);
        postRecyclerView = fragmentViewNewFeeds.findViewById(R.id.newFeedsRecyclerView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");

        return fragmentViewNewFeeds;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get the List Post from the Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postsnap : dataSnapshot.getChildren()){
                    Post post = postsnap.getValue(Post.class);
                    postList.add(post);
                }
                postAdaptor = new PostAdaptor(getActivity(), postList);

                postRecyclerView.setAdapter(postAdaptor);
                Collections.reverse(postList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }
}
