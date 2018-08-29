package com.jabber;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NavHomeScreen extends Fragment
{
	private View view;
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private PageAdapter adapter;

	public NavHomeScreen() {
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		getActivity().getMenuInflater().inflate(R.menu.main, menu);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle("Home");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_nav_home, container, false);
		tabLayout = view.findViewById(R.id.tabs);
		viewPager = view.findViewById(R.id.chatTabViewPager);
		adapter = new PageAdapter(getChildFragmentManager());
		adapter.AddFragment(new FragmentMessagesTab(), "Messages");
		adapter.AddFragment(new FragmentGroupTab(), "Groups");
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
		return(view);
	}
}
