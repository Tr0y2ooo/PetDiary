package com.troy2ooo.petdiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.HorizontalScrollView;
import android.view.MotionEvent;
import android.content.pm.PackageManager;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PetFragment extends Fragment {
    private Context context;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout scrollContainer;
    private int informationCount = 0;
    private static final int PICK_IMAGE_REQUEST = 1; // 識別請求代碼
    private FrameLayout imageFrame;
    private TextView hintText;
    private ImageView uploadedImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 初始化布局
        View view = inflater.inflate(R.layout.pet_fragment, container, false);

        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
        scrollContainer = view.findViewById(R.id.scrollContainer);

        // 檢查並請求讀取外部存儲的權限
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

        // 設置觸摸偵測
        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            private float initialX = 0;
            private final float SWIPE_THRESHOLD = 100;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        float finalX = event.getX();
                        float swipeDistance = initialX - finalX;

                        if (Math.abs(swipeDistance) < SWIPE_THRESHOLD) {
                            return false;
                        }

                        if (swipeDistance > 0) {
                            if (isAtRightEnd()) {
                                addInformationBar();
                                scrollToRight();
                            }
                        }
                        break;
                }
                return false;
            }
        });

        // 初始資訊欄
        addInformationBar();

        return view;
    }

    // 判斷是否滾動到最右邊
    private boolean isAtRightEnd() {
        int scrollX = horizontalScrollView.getScrollX();
        int containerWidth = scrollContainer.getWidth();
        int scrollViewWidth = horizontalScrollView.getWidth();
        return scrollX + scrollViewWidth >= containerWidth;
    }

    private void setupImageButtonClickListener(ImageButton button, final String message) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addInformationBar() {
        // 使用 LayoutInflater 加載信息欄的佈局
        View informationBarView = LayoutInflater.from(getContext()).inflate(R.layout.pet_info, null);

        // 獲取內部視圖
        EditText nameEditText = informationBarView.findViewById(R.id.nameEditText);
        EditText phoneEditText = informationBarView.findViewById(R.id.yearEditText);
        EditText describe = informationBarView.findViewById(R.id.describeEditText);
        RadioButton male = informationBarView.findViewById(R.id.radioButton);
        RadioButton female = informationBarView.findViewById(R.id.radioButton2);

        String name = nameEditText.getText().toString();
        String sex = male.isChecked() ? "M" : "F" ;

        ImageButton health = informationBarView.findViewById(R.id.imageButton);
        ImageButton behaver = informationBarView.findViewById(R.id.imageButton2);
        ImageButton remind = informationBarView.findViewById(R.id.imageButton3);

        Button delete = informationBarView.findViewById(R.id.button2);
        Button save = informationBarView.findViewById(R.id.button3);

        imageFrame = informationBarView.findViewById(R.id.imageFrame);
        hintText = informationBarView.findViewById(R.id.hintText);
        uploadedImage = informationBarView.findViewById(R.id.uploadedImage);

        uploadedImage.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        // 設置方框的點擊事件
        imageFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // 在此處插入 healthButton 點擊事件邏輯
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                PetHealth petHealth = new PetHealth();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                petHealth.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, petHealth)
                        .addToBackStack(null)
                        .commit();
            }
        });

        behaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                PetBehaver petBehaverFragment = new PetBehaver();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                petBehaverFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, petBehaverFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                PetRemind petRemind = new PetRemind();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                petRemind.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, petRemind)
                        .addToBackStack(null)
                        .commit();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollContainer.removeView(informationBarView);
                informationCount--;
                Toast.makeText(getActivity(), "已刪除一個寵物檔案" , Toast.LENGTH_SHORT).show();
            }
        });

        // 創建 LayoutParams 並設置寬高
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.card_width),
                (int) getResources().getDimension(R.dimen.card_height)
        );
        layoutParams.setMargins(
                (int) getResources().getDimension(R.dimen.card_margin),
                (int) getResources().getDimension(R.dimen.card_margin),
                (int) getResources().getDimension(R.dimen.card_margin),
                0
        );

        informationBarView.setLayoutParams(layoutParams);
        scrollContainer.addView(informationBarView);
        informationCount++;

        if (informationCount > 1) {
            Toast.makeText(getActivity(), "新增寵物檔案 " + informationCount, Toast.LENGTH_SHORT).show();
        }
    }

    private void scrollToRight() {
        horizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                int scrollX = scrollContainer.getWidth();
                horizontalScrollView.smoothScrollTo(scrollX, 0);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (getActivity() != null && requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                uploadedImage.setImageBitmap(bitmap);
                uploadedImage.setVisibility(View.VISIBLE);
                hintText.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}



