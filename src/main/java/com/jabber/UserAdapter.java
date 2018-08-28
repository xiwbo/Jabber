package com.jabber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>
{
	private Context ctx;
	private List<Users> usersList;

	public UserAdapter(Context ctx, List<Users> usersList) {
		this.ctx = ctx;
		this.usersList = usersList;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(ctx).inflate(R.layout.user_item, parent, false);
		return(new UserAdapter.ViewHolder(view));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Users users = usersList.get(position);
		holder.username.setText(users.getUsername());
	}

	@Override
	public int getItemCount() {
		return(usersList.size());
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		public TextView username;

		public ViewHolder(View view) {
			super(view);
			username = view.findViewById(R.id.textUsername);
		}

	}
}
