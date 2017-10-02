package com.example.asus.turkcell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by ASUS on 13.09.2017.
 */



public class Search extends Fragment{

    private TextView search ;
    private RecyclerView postList;
    private SearchView s;
    private EditText i;
    private String input;
    private ImageView ara;
    DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private  Query query = mFirebaseDatabaseReference.child("Images").orderByChild("lecture").equalTo(input);

    private  static  final String TAG="Search";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.search, container, false);

         search = (TextView)view.findViewById(R.id.item);
        ara = (ImageView) view.findViewById(R.id.ara);



        //arama işlemi
    ara.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            input = search.getText().toString();

            final Query query = mFirebaseDatabaseReference.child("Images").orderByChild("lecture").equalTo(input);


            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                        postList = (RecyclerView) view.findViewById(R.id.post_list2);
                        postList.setHasFixedSize(true);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        mLayoutManager.setReverseLayout(true);
                        mLayoutManager.setStackFromEnd(true);
                        postList.setLayoutManager(mLayoutManager);



                        FirebaseRecyclerAdapter<Post, Profile.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, Profile.PostViewHolder>(
                                Post.class,
                                R.layout.timeline_row,
                                Profile.PostViewHolder.class,
                                query

                        ) {
                            @Override
                            protected void populateViewHolder(Profile.PostViewHolder viewHolder, Post model, int position) {
                                final String key = getRef(position).getKey();

                                viewHolder.setUniversity(model.getUniversity());
                                viewHolder.setDepartment(model.getDepartment());
                                viewHolder.setLecture(model.getLecture());
                                viewHolder.setSubject(model.getSubject());
                                viewHolder.setTerm(model.getTerm());
                                viewHolder.setUserName(model.getUsername());
                                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());

                                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent i = new Intent(getActivity(), ShowPost.class);
                                        i.putExtra("key", key);
                                        startActivity(i);

                                        // Toast.makeText(getActivity(), key, Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        };

                        postList.setAdapter(firebaseRecyclerAdapter);


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    });





        return view;
    }











    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View view;

        public PostViewHolder(View itemView) {
            super(itemView);

            view = itemView;
        }

        public void setUniversity(String university){

            TextView post_university = (TextView) view.findViewById(R.id.txtCardUni);
            post_university.setText(university);

        }

        public void setDepartment(String department){
            TextView post_department = (TextView) view.findViewById(R.id.txtCardDepartment);
            post_department.setText(department);
        }

        public void setLecture(String lecture){
            TextView post_lecture = (TextView) view.findViewById(R.id.txtCardLecture);
            post_lecture.setText(lecture);
        }

        public void setSubject(String subject){
            TextView post_subject = (TextView) view.findViewById(R.id.txtCardSubject);
            post_subject.setText(subject);
        }

        public void setTerm(String term){
            TextView post_term = (TextView) view.findViewById(R.id.txtCardTerm);
            post_term.setText(term);
        }
        public void setUserName(String name)
        {
            TextView usernameText = (TextView) view.findViewById(R.id.user_name);
            usernameText.setText(name);
        }

        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) view.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }


    }

}
