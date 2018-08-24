package com.jabber;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter
{
	private final List<Fragment> fragmentList = new ArrayList<>();
	private final List<String> fragmentListTitles = new ArrayList<>();

	public PageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return(fragmentList.get(position));
	}

	@Override
	public int getCount() {
		return(fragmentListTitles.size());
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return(fragmentListTitles.get(position));
	}

	public void AddFragment(Fragment fragment, String title) {
		fragmentList.add(fragment);
		fragmentListTitles.add(title);
	}
}
