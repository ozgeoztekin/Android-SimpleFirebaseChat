package com.oztekino.simplefirebasechat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserListFragment extends Fragment {

    private FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter;
    private RecyclerView recyclerViewUserList;
    private LinearLayoutManager linearLayoutManager;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_user_list, container, false);

        updateUI(rootView);
        return rootView;
    }

    private void updateUI(View rootView) {

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerViewUserList = (RecyclerView) rootView.findViewById(R.id.layout_user_list_recycler_view);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.item_user_list,
                UserViewHolder.class,
                databaseReference.child("users")
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
                viewHolder.textViewUsername.setText(model.getUsername());
                viewHolder.viewStatus.setImageResource(model.getStatus() == UserStatus.ONLINE.ordinal()
                        ? R.drawable.circle_green
                        : R.drawable.circle_red);
            }
        };

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewUserList.setLayoutManager(linearLayoutManager);
        recyclerViewUserList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername;
        ImageView viewStatus;

        public UserViewHolder(View v) {
            super(v);
            textViewUsername = (TextView) itemView.findViewById(R.id.item_user_list_username);
            viewStatus = (ImageView) itemView.findViewById(R.id.item_user_list_status);
        }
    }
}

