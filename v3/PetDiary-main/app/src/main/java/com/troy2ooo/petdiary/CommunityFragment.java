package com.troy2ooo.petdiary;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText etPostContent;
    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.community_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etPostContent = rootView.findViewById(R.id.et_post_content);
        Button btnPost = rootView.findViewById(R.id.btn_post);
        rvPosts = rootView.findViewById(R.id.rv_posts);

        // 初始化 RecyclerView
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(postAdapter);

        // 發佈貼文按鈕
        btnPost.setOnClickListener(v -> postToFirebase());

        // 加載貼文資料
        loadPostsFromFirebase();

        return rootView;
    }

    private void postToFirebase() {
        String content = etPostContent.getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "請輸入貼文內容", Toast.LENGTH_SHORT).show();
            return;
        }

        String userID = mAuth.getCurrentUser().getUid();
        Map<String, Object> post = new HashMap<>();
        post.put("userID", userID);
        post.put("content", content);
        post.put("timestamp", System.currentTimeMillis());

        db.collection("posts").add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "貼文已發佈", Toast.LENGTH_SHORT).show();
                    etPostContent.setText("");
                    loadPostsFromFirebase(); // 更新貼文列表
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "貼文發佈失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadPostsFromFirebase() {
        db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = new Post(
                                document.getString("userID"),
                                document.getString("content"),
                                document.getLong("timestamp")
                        );
                        postList.add(post);
                    }
                    postAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "貼文加載失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
