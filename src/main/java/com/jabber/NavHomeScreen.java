package com.jabber;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class NavHomeScreen extends Fragment
{
	View view;
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
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//title(header) bar
		getActivity().setTitle("Home");
		//screen orientation of fragments
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_nav_home, container, false);
		tabLayout = view.findViewById(R.id.tabs);
		viewPager = view.findViewById(R.id.chatTabViewPager);
		PageAdapter adapter;
		adapter = new PageAdapter(getChildFragmentManager());
		adapter.AddFragment(new FragmentMessagesTab(), "Messages");
		adapter.AddFragment(new FragmentGroupTab(), "Groups");
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
		return(view);
	}
}
