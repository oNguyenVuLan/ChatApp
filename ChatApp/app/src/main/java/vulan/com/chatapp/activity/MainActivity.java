package vulan.com.chatapp.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vulan.com.chatapp.R;
import vulan.com.chatapp.adapter.RecyclerLeftDrawerAdapter;
import vulan.com.chatapp.entity.ChatRoom;
import vulan.com.chatapp.entity.Contact;
import vulan.com.chatapp.entity.DrawerLeftItem;
import vulan.com.chatapp.fragment.BaseFragment;
import vulan.com.chatapp.fragment.ChatRoomFragment;
import vulan.com.chatapp.fragment.ContactFragment;
import vulan.com.chatapp.fragment.HomeFragment;
import vulan.com.chatapp.listener.OnLeftItemClickListener;
import vulan.com.chatapp.util.Constants;
import vulan.com.chatapp.widget.LinearItemDecoration;

/**
 * Created by VULAN on 9/11/2016.
 */
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OnLeftItemClickListener, View.OnClickListener {

    private RecyclerView mLeftRecyclerDrawer;
    private RecyclerLeftDrawerAdapter mRecyclerLeftDrawerAdapter;
    private List<DrawerLeftItem> mDrawerLeftItemList;
    private DrawerLayout mDrawerLayout;
    private ImageView mButtonMenuLeft;
    private SearchView mSearchView;
    private static final int LOGOUT_POSITION = 1, SETTING_POSITION = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        init();
        replaceFragment(new HomeFragment(), Constants.HOME_FRAGMENT);
    }

    private void findView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftRecyclerDrawer = (RecyclerView) findViewById(R.id.left_recycler_navigation_drawer);
        mButtonMenuLeft = (ImageView) findViewById(R.id.button_menu_left);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(this);
        mButtonMenuLeft.setOnClickListener(this);
    }

    private void init() {
        mDrawerLeftItemList = new ArrayList<>();
        mDrawerLeftItemList.add(new DrawerLeftItem(getString(R.string.change_password), R.drawable.ic_setting));
        mDrawerLeftItemList.add(new DrawerLeftItem(getString(R.string.logout), R.drawable.ic_logout));
        mRecyclerLeftDrawerAdapter = new RecyclerLeftDrawerAdapter(this, mDrawerLeftItemList);
        mLeftRecyclerDrawer.setLayoutManager(new LinearLayoutManager(this));
        mLeftRecyclerDrawer.addItemDecoration(new LinearItemDecoration(this));
        mLeftRecyclerDrawer.setAdapter(mRecyclerLeftDrawerAdapter);
        mRecyclerLeftDrawerAdapter.setOnClick(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (HomeFragment.sViewPager != null) {
            switch (HomeFragment.sViewPager.getCurrentItem()) {
                case 0:
                    List<ChatRoom> listChatRoom = filterChatRoom(ChatRoomFragment.sChatRoomList, newText);
                    ChatRoomFragment.sChatRoomAdapter.animateTo(listChatRoom);
                    break;
                case 1:
                    List<Contact> listContact = filterContact(ContactFragment.sContactList, newText);
                    ContactFragment.sContactAdapter.animateTo(listContact);
                    break;
            }
        }

        return true;
    }

    private List<Contact> filterContact(List<Contact> list, String query) {
        query = query.toLowerCase();
        List<Contact> contacts = new ArrayList<>();
        for (Contact item : list) {
            String itemName = item.getName().toLowerCase();
            if (itemName.contains(query)) {
                contacts.add(item);
            }
        }
        Toast.makeText(this,"1:"+contacts.size(),Toast.LENGTH_SHORT).show();
        return contacts;
    }

    private List<ChatRoom> filterChatRoom(List<ChatRoom> list, String query) {
        query = query.toLowerCase();
        List<ChatRoom> contacts = new ArrayList<>();
        for (ChatRoom item : list) {
            String itemName = item.getTitle().toLowerCase();
            if (itemName.contains(query)) {
                contacts.add(item);
            }
        }
        Toast.makeText(this,"0: "+contacts.size(),Toast.LENGTH_SHORT).show();
        return contacts;
    }

    public void replaceFragment(BaseFragment fragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_left_enter, R.animator.fragment_slide_right_exit)
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack("").commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onLeftItemClick(int position) {
        switch (position) {
            case LOGOUT_POSITION:
                finish();
                break;
            case SETTING_POSITION:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_menu_left:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
