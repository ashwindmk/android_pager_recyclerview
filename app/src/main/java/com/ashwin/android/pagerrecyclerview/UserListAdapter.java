package com.ashwin.android.pagerrecyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserListAdapter extends PagerRecyclerView.PagerAdapter<UserListAdapter.UserViewHolder, User> implements View.OnClickListener {
    private static final String TAG = UserListAdapter.class.getSimpleName();

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final View root;
        private final TextView userIdTextView;
        private final TextView userNameTextView;
        private final TextView userEmailTextView;

        UserViewHolder(View itemView) {
            super(itemView);
            root = itemView.getRootView();
            userIdTextView = itemView.findViewById(R.id.user_id_text_view);
            userNameTextView = itemView.findViewById(R.id.user_name_text_view);
            userEmailTextView = itemView.findViewById(R.id.user_email_text_view);
        }

        private void bind(User user, int position, View.OnClickListener clickListener) {
            userIdTextView.setText(String.valueOf(user.getId()));
            userNameTextView.setText(user.getName());
            userEmailTextView.setText(user.getEmail());
            root.setOnClickListener(clickListener);
            root.setTag(position);
        }
    }

    @NonNull
    @Override
    public UserListAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserViewHolder holder, int position) {
        if (mDataSet.size() > position) {
            User user = mDataSet.get(position);
            holder.bind(user, position, this);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : getVirtualItemCount(mDataSet.size());
    }

    @Override
    public int getItemViewType(int position) {
        return (position % onePageSize) + 1;
    }

    @Override
    public void updateData(List<User> userList) {
        mDataSet = userList;
        notifyDataSetChanged();
    }

    @Override
    public int getTrueItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (itemClickListener != null) {
            itemClickListener.onItemClick(v, mDataSet.get(position));
        }
    }
}
