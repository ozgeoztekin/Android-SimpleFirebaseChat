package com.oztekino.simplefirebasechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    private static final int TAB_COUNT = 2;
    private static final String USER_OFFLINE_EVENT = "user_offline";
    private static final String APP_BACKGROUND = "app_background";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseAnalytics firebaseAnalytics;
    private ChildEventListener userListListener;
    private ValueEventListener currentUserListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        currentUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userFromFirebase = dataSnapshot.getValue(User.class);
                UserUtil.syncCurrentUser(userFromFirebase);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        userListListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                getSupportActionBar().setSubtitle(UserUtil.handleUserTypingList(getApplicationContext(), user));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        databaseReference.child("users").addChildEventListener(userListListener);
        if (UserUtil.getUser() == null) {
            databaseReference.child("users").child(firebaseUser.getUid()).child("status").setValue(UserStatus.ONLINE.ordinal());
            databaseReference.child("users").child(firebaseUser.getUid()).addValueEventListener(currentUserListener);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_chat_tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_chat_view_pager);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.child("users").child(firebaseUser.getUid()).child("status").setValue(UserStatus.ONLINE.ordinal());
    }

    @Override
    protected void onPause() {
        databaseReference.child("users").child(firebaseUser.getUid()).child("status").setValue(UserStatus.INACTIVE.ordinal());
        firebaseAnalytics.logEvent(APP_BACKGROUND, null);
        super.onPause();
    }

    @Override
    public void onStop() {
        if (userListListener != null) {
            databaseReference.child("users").removeEventListener(userListListener);
        }
        databaseReference.child("users").child(firebaseUser.getUid()).child("status").setValue(UserStatus.INACTIVE.ordinal());
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                Bundle offlineUser = new Bundle();
                offlineUser.putString(FirebaseAnalytics.Param.VALUE, firebaseUser.getDisplayName());
                firebaseAnalytics.logEvent(USER_OFFLINE_EVENT, offlineUser);
                databaseReference.child("users").child(firebaseUser.getUid()).child("status").setValue(UserStatus.OFFLINE.ordinal());
                firebaseAuth.signOut();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new ChatRoomFragment();
            }

            return new UserListFragment();
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.chat_room_tab_title);
            }

            return getString(R.string.user_list_tab_title);
        }
    }


}
