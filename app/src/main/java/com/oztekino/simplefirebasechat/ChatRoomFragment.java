package com.oztekino.simplefirebasechat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatRoomFragment extends Fragment {

    private static final String MESSAGE_SENT_EVENT = "message_sent";

    private FirebaseRecyclerAdapter<Message, MessageViewHolder> firebaseRecyclerAdapter;
    private RecyclerView recyclerViewConversation;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference databaseReference;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_conversation, container, false);

        updateUI(rootView);
        return rootView;
    }

    private void updateUI(View rootView) {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        recyclerViewConversation = (RecyclerView) rootView.findViewById(R.id.activity_chat_recycler_view_conversation);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.item_message,
                MessageViewHolder.class,
                databaseReference.child("conversation").child("messages")
        ) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                viewHolder.textViewMessage.setText(model.getText());
                viewHolder.textViewMessageSender.setText(
                        UserUtil.getUser().getUserId().equals(model.getSenderId())
                                ? getString(R.string.chat_sender_name_you)
                                : model.getSenderName());
                viewHolder.textViewMessage.setTextColor(model.getColorCode());
                viewHolder.textViewMessageSender.setTextColor(model.getColorCode());
            }
        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    recyclerViewConversation.scrollToPosition(positionStart);
                }
            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewConversation.setLayoutManager(linearLayoutManager);
        recyclerViewConversation.setAdapter(firebaseRecyclerAdapter);

        final Button buttonSend = (Button) rootView.findViewById(R.id.activity_chat_button_send);
        final EditText editTextMessage = (EditText) rootView.findViewById(R.id.activity_chat_edittext_message);
        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonSend.setEnabled(charSequence.toString().trim().length() > 0);
                databaseReference.child("users").child(UserUtil.getUser().getUserId()).child("typing").setValue(
                        charSequence.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = UserUtil.getUser();
                Message message = new Message(editTextMessage.getText().toString(),
                        user.getUsername(),
                        user.getUserId(),
                        user.getColorCode());
                databaseReference.child("conversation").child("messages").push().setValue(message);
                editTextMessage.setText("");
                firebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
            }
        });
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView textViewMessageSender;

        public MessageViewHolder(View v) {
            super(v);
            textViewMessage = (TextView) itemView.findViewById(R.id.item_message_textview_message);
            textViewMessageSender = (TextView) itemView.findViewById(R.id.item_message_textview_owner);
        }
    }
}