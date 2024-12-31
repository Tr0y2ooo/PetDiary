package com.troy2ooo.petdiary;

import android.net.Uri;
import androidx.lifecycle.ViewModel;

public class PetViewModel extends ViewModel {
    private String petName = "";
    private String petYear = "";
    private String petDescribe = "";
    private String petGender = "";
    private Uri petPhotoUri = null;  // 用於保存照片的 Uri

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetYear() {
        return petYear;
    }

    public void setPetYear(String petYear) {
        this.petYear = petYear;
    }

    public String getPetDescribe() {
        return petDescribe;
    }

    public void setPetDescribe(String petDescribe) {
        this.petDescribe = petDescribe;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

    public Uri getPetPhotoUri() {
        return petPhotoUri;
    }

    public void setPetPhotoUri(Uri uri) {
        this.petPhotoUri = uri;
    }
} 