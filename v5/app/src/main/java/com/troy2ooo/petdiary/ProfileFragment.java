package com.troy2ooo.petdiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import android.text.InputType;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.inputmethod.EditorInfo;

import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;  // 用於 Firebase Authentication
    private FirebaseFirestore db;  // 用於 Firestore
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 確保在這裡初始化 context
        this.context = context;
        // 初始化 Firebase 認證和 Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 只調用一次 inflater.inflate() 並返回根視圖
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        // 取得 Firebase 用戶
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // 如果用戶已經登入
        if (currentUser != null) {
            // 取得 Google 帳號 (電子郵件)
            String googleAccount = currentUser.getEmail();  // 取得電子郵件

            // 找到 TextView 並設置顯示內容
            TextView GAccount = rootView.findViewById(R.id.tv_googleAccount);
            GAccount.setText(googleAccount);  // 設置 Google 帳號
        } else {
            // 用戶未登入，可以顯示提示訊息或其他處理
            Toast.makeText(getContext(), "未登入", Toast.LENGTH_SHORT).show();
        }

        // 使用 rootView.findViewById() 來實體化按鈕
        ImageButton EditUser = rootView.findViewById(R.id.btn_user);
        ImageButton EditUserID = rootView.findViewById(R.id.btn_userID);
        ImageButton EditBirth = rootView.findViewById(R.id.btn_birth);
        ImageButton EditGAccount = rootView.findViewById(R.id.btn_googleAccount);
        ImageButton EditUserIntro = rootView.findViewById(R.id.btn_userIntro);

        Button logout = rootView.findViewById(R.id.button);

        TextView User = rootView.findViewById(R.id.tv_user);
        TextView UserID = rootView.findViewById(R.id.tv_userID);
        TextView Birth = rootView.findViewById(R.id.tv_birth);
        TextView UserIntro = rootView.findViewById(R.id.tv_userIntro);

        // 設置按鈕的點擊事件
        EditUser.setOnClickListener(v -> showEditDialog(User, "userName"));
        EditUserID.setOnClickListener(v -> showEditDialog(UserID, "userID"));
        EditBirth.setOnClickListener(v -> showEditDialog(Birth, "birthDate"));
        //EditGAccount.setOnClickListener(v -> showEditDialog(GAccount, "googleAccount"));
        EditUserIntro.setOnClickListener(v -> showEditDialog(UserIntro, "userIntro"));
        EditGAccount.setOnClickListener(view -> Toast.makeText(getContext(), "現在還不能改", Toast.LENGTH_SHORT).show());


        // 設置登出按鈕點擊事件
        logout.setOnClickListener(view -> logoutUser());

        // 加載當前用戶的資料
        loadUserData(User, UserID, Birth, UserIntro);

        // 返回根視圖
        return rootView;
    }

    private void loadUserData(TextView userTextView, TextView userIDTextView, TextView birthTextView,  TextView introTextView) {
        // 獲取當前登入用戶的 UID
        String currentUserID = mAuth.getCurrentUser().getUid();

        // 從 Firestore 中獲取該用戶的資料
        CollectionReference usersCollection = db.collection("users");
        usersCollection.document(currentUserID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // 資料存在，將資料顯示在對應的 TextView 上
                        userTextView.setText(documentSnapshot.getString("userName"));
                        userIDTextView.setText(documentSnapshot.getString("userID"));
                        birthTextView.setText(documentSnapshot.getString("birthDate"));
                        introTextView.setText(documentSnapshot.getString("userIntro"));
                    } else {
                        // 如果沒有資料，設置預設值並寫入 Firestore
                        setDefaultData(currentUserID);
                        userTextView.setText("尚未設定");
                        userIDTextView.setText("尚未設定");
                        birthTextView.setText("尚未設定");
                        introTextView.setText("尚未設定");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "加載資料失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // 設置預設資料
    private void setDefaultData(String userID) {
        Map<String, Object> user = new HashMap<>();
        user.put("userName", "尚未設定");
        user.put("userID", "尚未設定");
        user.put("birthDate", "尚未設定");
        user.put("googleAccount", "尚未設定");
        user.put("userIntro", "尚未設定");

        // 儲存到 Firestore
        db.collection("users").document(userID)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "資料已初始化", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "初始化資料失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // 顯示修改資料對話框
    private void showEditDialog(final TextView textView, final String fieldName) {
        final EditText editText = new EditText(context);
        editText.setText(textView.getText().toString()); // 設置初始值為當前 TextView 的文本

        new AlertDialog.Builder(context)
                .setTitle("修改文字")
                .setMessage("請輸入新的文字：")
                .setView(editText)
                .setPositiveButton("確認", (dialog, which) -> {
                    // 更新 Firestore 中的資料
                    String newText = editText.getText().toString();
                    textView.setText(newText);  // 更新 TextView 顯示
                    updateUserProfile(fieldName, newText);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    // 更新用戶資料到 Firestore
    private void updateUserProfile(String fieldName, String newText) {
        String currentUserID = mAuth.getCurrentUser().getUid();
        // 根據字段名稱更新相應的資料
        db.collection("users").document(currentUserID)
                .update(fieldName, newText)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "資料已更新", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "更新失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // 登出用戶
    private void logoutUser() {
        mAuth.signOut();  // 登出
        Toast.makeText(context, "已登出", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}








