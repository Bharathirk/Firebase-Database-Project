package com.org.firebase.helper.filecompress;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.org.firebase.R;
import com.org.firebase.app.AppConstants;
import com.org.firebase.utils.CommonUtils;
import com.org.firebase.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import static com.org.firebase.app.AppConstants.REQUEST_GALLERY_PICK;


public class FileAttachmentSelector {
    private AppCompatActivity activity;
    private Fragment fragment;
    private AttachmentSelectionListener listener;
    private File mAudioFile;
    private File mCameraFile;
    private String uniqueIdPerWorkOrder;



    public interface AttachmentSelectionListener {
        void onAttachmentSelected(String filePath, int code);
    }

    private FileAttachmentSelector(String uniqueIdPerWorkOrder, AttachmentSelectionListener listener) {
        this.uniqueIdPerWorkOrder = uniqueIdPerWorkOrder;
        this.listener = listener;
    }

    public FileAttachmentSelector(String uniqueIdPerWorkOrder, AppCompatActivity activity, AttachmentSelectionListener listener) {
        this.uniqueIdPerWorkOrder = uniqueIdPerWorkOrder;
        this.activity = (AppCompatActivity) activity;
        this.listener = (AttachmentSelectionListener) listener;
    }

    public FileAttachmentSelector(String uniqueIdPerWorkOrder, Fragment fragment, AttachmentSelectionListener listener) {
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
        Uri imgUri = FileProvider.getUriForFile(getContext(), "com.org.firebase", mCameraFile);

        return imgUri;
    }

    public void selectGalleryAttachment() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (activity != null) {
            activity.startActivityForResult(intent,REQUEST_GALLERY_PICK);
        } else {
            fragment.startActivityForResult(intent, REQUEST_GALLERY_PICK);
        }
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


    public void selectDocumentAttachment() {
        Intent target = FileUtils.createGetContentIntent();
        boolean isDocAvailable = CommonUtils.isAvailable(getContext(), target);
        if (isDocAvailable)
            if (activity != null) {
                activity.startActivityForResult(target, AppConstants.REQUEST_DOCUMENT_PICK);
            } else {
                fragment.startActivityForResult(target, AppConstants.REQUEST_DOCUMENT_PICK);
            }
        else
            Toast.makeText(getContext(), getContext().getString(R.string.no_app_to_perform), Toast.LENGTH_SHORT).show();
    }

//    @Subscribe
//    public void onSelectedImages(ImageSelectionEvent selectedImage) {

//        Toast.makeText(activity, ""+selectedImage, Toast.LENGTH_SHORT).show();
//    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return false;

        if (requestCode == AppConstants.REQUEST_CAMERA_PICK
                || requestCode == REQUEST_GALLERY_PICK
                || requestCode == AppConstants.REQUEST_DOCUMENT_PICK
                || requestCode == AppConstants.REQUEST_AUDIO_PICK) {

            Uri attachmentUri;
            try {
                if (requestCode == AppConstants.REQUEST_CAMERA_PICK) {
                    if (listener != null) {
                        if (mCameraFile != null) {
                            listener.onAttachmentSelected(mCameraFile.getAbsolutePath(), AppConstants.REQUEST_CAMERA_PICK);
                        }
                    }
                } else {
                    attachmentUri = data.getData();
                    if (listener != null) {
                        listener.onAttachmentSelected(attachmentUri.toString(), AppConstants.REQUEST_DOCUMENT_PICK);
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

