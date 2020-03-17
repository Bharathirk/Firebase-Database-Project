package com.org.firebase.helper.filecompress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.org.firebase.R;
import com.org.firebase.app.AppConstants;

import java.io.File;
import java.io.IOException;

public class AttachmentSelector {

    private AppCompatActivity activity;
    private Fragment fragment;
    private AttachmentSelectionListener listener;
    private File mCameraFile;
    private String uniqueIdPerWorkOrder;
    private int attachmentType;

//    public void selectGalleryAttachment() {
//        FolderActivity.getCallingInstance(getContext());
//    }

    public interface AttachmentSelectionListener {
        void onAttachmentSelected(String filePath, int code, int attachmentType);
    }

    private AttachmentSelector(String uniqueIdPerWorkOrder, AttachmentSelectionListener listener) {
        this.uniqueIdPerWorkOrder = uniqueIdPerWorkOrder;
        this.listener = listener;
    }

    public AttachmentSelector(String uniqueIdPerWorkOrder, AppCompatActivity activity, AttachmentSelectionListener listener, int attachmentType) {
        this.uniqueIdPerWorkOrder = uniqueIdPerWorkOrder;
        this.activity = activity;
        this.listener = listener;
        this.attachmentType = attachmentType;
    }

    public AttachmentSelector(String uniqueIdPerWorkOrder, Fragment fragment, AttachmentSelectionListener listener) {
        this(uniqueIdPerWorkOrder, listener);
        this.fragment = fragment;
    }


    public Context getContext() {
        if (activity != null)
            return activity;
        else
            return fragment.getContext();
    }

    private Uri createUriForCameraIntent(Context context) throws IOException {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String fileName = String.format("%s", uniqueIdPerWorkOrder);

        mCameraFile = File.createTempFile(
                fileName,  /* prefix */
                ".jpg",        /* suffix */
                storageDir      /* directory */
        );
        Uri imgUri = FileProvider.getUriForFile(getContext(),
                "com.smartpoint.smartcarrierscustomer", mCameraFile);

        return imgUri;
    }

    public void selectCameraAttachment() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            Uri mCameraUri = createUriForCameraIntent(getContext());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri);
            if (activity != null) {
                activity.startActivityForResult(takePictureIntent, AppConstants.REQUEST_CAMERA_PICK);
            } else {
                fragment.startActivityForResult(takePictureIntent, AppConstants.REQUEST_CAMERA_PICK);
            }
        } else
            Toast.makeText(getContext(), getContext().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
    }



    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return false;

        if (requestCode == AppConstants.REQUEST_CAMERA_PICK
                || requestCode == AppConstants.REQUEST_GALLERY_PICK
                || requestCode == AppConstants.REQUEST_DOCUMENT_PICK
                || requestCode == AppConstants.REQUEST_AUDIO_PICK) {

            Uri attachmentUri;
            try {
                if (requestCode == AppConstants.REQUEST_CAMERA_PICK) {
                    if (listener != null) {
                        if (mCameraFile != null) {
                            listener.onAttachmentSelected(mCameraFile.getAbsolutePath(), AppConstants.REQUEST_CAMERA_PICK, attachmentType);
                        }
                    }
                } else {
                    attachmentUri = data.getData();
                    if (listener != null) {
                        listener.onAttachmentSelected(attachmentUri.toString(), AppConstants.REQUEST_DOCUMENT_PICK, attachmentType);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), getContext().getString(R.string.failed_to_attach), Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return false;
        }

    }


}

