package com.org.firebase.ui.post;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.org.firebase.R;
import com.org.firebase.app.AppConstants;
import com.org.firebase.app.AppController;
import com.org.firebase.app.AppPreference;
import com.org.firebase.base.BaseFragment;
import com.org.firebase.helper.filecompress.AttachmentSelector;
import com.org.firebase.helper.filecompress.FileAttachmentSelector;
import com.org.firebase.helper.runtimepermission.PermissionHelper;
import com.org.firebase.ui.top.PostListAdapter;
import com.org.firebase.ui.top.TopFragment;
import com.org.firebase.utils.DateUtils;
import com.org.firebase.utils.FileUtils;
import com.org.firebase.utils.RxJavaUtils;
import com.org.firebase.utils.UiUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.org.firebase.app.AppConstants.REQUEST_CAMERA_PICK;

public class PostFragment extends BaseFragment implements AttachmentSelector.AttachmentSelectionListener,
        View.OnClickListener {
    private PermissionHelper permissionHelper;
    private FileAttachmentSelector attachmentSelector;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
    @BindView(R.id.btn_add_image)
    ImageView btnAddImage;
    private static final String[] M_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int TYPE_PHOTO = 761;
    private Uri currentPickImaeUri = null;
    private String imageUploadPath;
    @BindView(R.id.edt_name)
    EditText edtContent;
    @BindView(R.id.img_added)
    ImageView img_added;
    private AppPreference preference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_post, container, false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        preference = AppController.getInstance().getAppPreference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isDenied = false;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                isDenied = true;
                break;
            }
        }

        if (!isDenied) {
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1212);

    }

    private void createpost() {
        String postId = UUID.randomUUID().toString();
        PostModel postModel = new PostModel();
        postModel.setDate(DateUtils.getCurrentDate());
        postModel.setDateWithTime(Calendar.getInstance().getTime().toString());
        postModel.setUserId(preference.getUserId());
        postModel.setUserName(preference.getUserName());
        postModel.setImagepath(imageUploadPath);
        postModel.setLiked(false);
        postModel.setPostContent(edtContent.getText().toString());
        postModel.setPostTitle("General Post");
        postModel.setPostId(postId);
        //Databaseで追加
        mDatabase.child("userposts").child(preference.getUserId()).child(postId).setValue(postModel);
        mDatabase.child("commonposts").child(postId).setValue(postModel);

        Toast.makeText(mActivity, "post created successfully", Toast.LENGTH_SHORT).show();
        edtContent.setText("");
        currentPickImaeUri = null;
        img_added.setVisibility(View.GONE);
    }
    private void uploadImage(Uri filepath) {


        if (filepath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            storageReference = storageReference.child("images/" + UUID.randomUUID().toString());
            storageReference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    imageUploadPath = uri.toString();
                                    createpost();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }


    @OnClick({R.id.btn_add_image, R.id.btn_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_image:
                askRuntimePermissionForCameraAndStorage();
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(edtContent.getText().toString().trim())) {
                    UiUtils.showToast(getActivity(), "Kindly Enter Something");
                    return;
                } else {
                    if (currentPickImaeUri == null) {
                        createpost();
                    } else {
                        uploadImage(currentPickImaeUri);
                    }
                }
                break;
        }
    }

    private void askRuntimePermissionForCameraAndStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean allPermissionsGranted = true;
            for (int i = 0, mPermissionLength = M_PERMISSIONS.length; i < mPermissionLength; i++) {
                String permission = M_PERMISSIONS[i];
                if (ActivityCompat.checkSelfPermission(getActivity(), permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (!allPermissionsGranted) {
                ActivityCompat.requestPermissions(getActivity(), M_PERMISSIONS, M_PERMISSIONS.length);
                return;
            }
            pickImageFromGallery();
//            selectImage(761);
        } else {
            pickImageFromGallery();
//            selectImage(761);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212) {
            currentPickImaeUri = data.getData();
            img_added.setVisibility(View.VISIBLE);
            img_added.setImageURI(currentPickImaeUri);
        } else if (attachmentSelector != null) {
            attachmentSelector.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onAttachmentSelected(String filePath, int code, int attachmentType) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        if (attachmentType == TYPE_PHOTO) {
//            imgTick.setImageResource(R.drawable.ic_tick);
            String path = filePath;
            String filename = path.substring(path.lastIndexOf("/") + 1);
//            txtAttachment.setText(filename);
//            Toast.makeText(this, "attachmenttype" + filePath, Toast.LENGTH_SHORT).show();
        }

    }
}
