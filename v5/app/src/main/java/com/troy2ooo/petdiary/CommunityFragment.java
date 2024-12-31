package com.troy2ooo.petdiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    private EditText etPostContent;
    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private Uri selectedImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.community_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("post_images");

        etPostContent = rootView.findViewById(R.id.et_post_content);
        Button btnSelectImage = rootView.findViewById(R.id.btn_select_image);
        Button btnPost = rootView.findViewById(R.id.btn_post);
        rvPosts = rootView.findViewById(R.id.rv_posts);

        // 初始化 RecyclerView
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(postAdapter);

        // 選擇圖片按鈕
        btnSelectImage.setOnClickListener(v -> openImagePicker());

        // 發佈貼文按鈕
        btnPost.setOnClickListener(v -> postToFirebase());

        // 加載貼文資料
        loadPostsFromFirebase();

        return rootView;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Toast.makeText(getContext(), "圖片已選擇", Toast.LENGTH_SHORT).show();
        }
    }

    private void postToFirebase() {
        String content = etPostContent.getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "請輸入貼文內容", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri != null) {
            StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpg");
            fileRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                uploadPost(content, uri.toString());
            })).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "圖片上傳失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            uploadPost(content, null);
        }
    }

    private void uploadPost(String content, String imageUrl) {
        String userID = mAuth.getCurrentUser().getUid();
        Map<String, Object> post = new HashMap<>();
        post.put("userID", userID);
        post.put("content", content);
        post.put("timestamp", System.currentTimeMillis());
        post.put("imageUrl", imageUrl);

        db.collection("posts").add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "貼文已發佈", Toast.LENGTH_SHORT).show();
                    etPostContent.setText("");
                    selectedImageUri = null;
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
                                document.getLong("timestamp"),
                                document.getString("imageUrl")
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
