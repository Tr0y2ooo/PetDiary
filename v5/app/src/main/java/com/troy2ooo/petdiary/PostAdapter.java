package com.troy2ooo.petdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(post.getUserID()) // 使用 post 中的 uid 查詢
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userID = documentSnapshot.getString("userID");
                        holder.tvUserId.setText(userID != null ? userID : "未知用戶");
                    } else {
                        holder.tvUserId.setText("未知用戶");
                    }
                })
                .addOnFailureListener(e -> holder.tvUserId.setText("加載失敗"));

        holder.tvUserId.setText(post.getUserID());
        holder.tvContent.setText(post.getContent());
        holder.tvTimestamp.setText(formatTimestamp(post.getTimestamp()));

        if (post.getImageUrl() != null) {
            holder.ivPostImage.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(post.getImageUrl())
                    .into(holder.ivPostImage);
        } else {
            holder.ivPostImage.setVisibility(View.GONE);
        }
    }
    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserId, tvContent, tvTimestamp;
        ImageView ivPostImage; // 確認這裡有定義 ivPostImage

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserId = itemView.findViewById(R.id.tv_user_id);
            tvContent = itemView.findViewById(R.id.tv_post_content);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            ivPostImage = itemView.findViewById(R.id.iv_post_image); // 正確綁定 ImageView
        }
    }
}
