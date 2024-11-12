package com.example.recipe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.OutputStream;
import java.util.Objects;

//extra: glide have the feature to make the picture circular
public class bnv_fragment3_profile extends Fragment {

    private ImageView camera_icon;
    private ImageView profile_image;
    private Uri photoUri;
    private AlertDialog alertDialog;
    private static final int REQUEST_CAMERA_PERMISSION_CODE = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK) {
                    if (photoUri != null) {
                        Glide.with(this).load(photoUri).into(profile_image);
                        storeImageUri(photoUri);
                    } else if (result.getData() != null) {
                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        profile_image.setImageBitmap(photo);
                        saveImageToGallery(photo);
                    }
                }
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
    );

    private final ActivityResultLauncher<Intent> imageResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK) {
                    Uri imageUri = result.getData().getData();
                    Glide.with(this).load(imageUri).into(profile_image);
                    storeImageUri(imageUri);
                } else {
                    Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
                }
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bnv_fragment3_profile, container, false);

        camera_icon = view.findViewById(R.id.camera_icon);
        profile_image = view.findViewById(R.id.profile_image);

        loadProfileImage();

        camera_icon.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
            View dialogView = inflater.inflate(R.layout.alert_dialog_camera_or_gallery, null);
            dialogBuilder.setView(dialogView);

            AlertDialog alertDialog = dialogBuilder.create();

            Button alert_dialog_cancel_button = dialogView.findViewById(R.id.alert_dialog_cancel_button);
            TextView alert_dialog_camera = dialogView.findViewById(R.id.alert_dialog_camera);
            TextView alert_dialog_gallery = dialogView.findViewById(R.id.alert_dialog_gallery);

            alert_dialog_cancel_button.setOnClickListener(view1 -> alertDialog.dismiss());
            alert_dialog_camera.setOnClickListener(view1 -> {
                checkPermissionAndProceed(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION_CODE, this::captureImage);
                alertDialog.dismiss();
            });
            alert_dialog_gallery.setOnClickListener(view1 -> {
                checkPermissionAndProceed(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE, this::pickImageFromGallery);
                alertDialog.dismiss();
            });

            alertDialog.show();
        });

        return view;
    }

    private void checkPermissionAndProceed(String permission, int requestCode, Runnable onGranted) {
        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, requestCode);
        } else {
            onGranted.run();
        }
    }

    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        imageResultLauncher.launch(galleryIntent);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        cameraLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == STORAGE_PERMISSION_CODE) {
                pickImageFromGallery();
            } else if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
                captureImage();
            }
        } else {
            String message = requestCode == STORAGE_PERMISSION_CODE ? "Storage permission denied" : "Camera permission denied";
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageToGallery(Bitmap imageBitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri uri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try (OutputStream outStream = requireContext().getContentResolver().openOutputStream(Objects.requireNonNull(uri))) {
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
            Toast.makeText(requireContext(), "Image Saved Successfully", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    //store the path/uri in database
    private void storeImageUri(Uri imageUri) {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PROFILE_IMAGE_URI, imageUri.toString());

        db.insert(DatabaseHelper.TABLE_PROFILE_IMAGE, null, values);
        db.close();
    }

    private void loadProfileImage() {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PROFILE_IMAGE,
                new String[]{DatabaseHelper.COLUMN_PROFILE_IMAGE_URI},
                null, null, null, null,
                DatabaseHelper.COLUMN_PROFILE_ID + " DESC",
                "1"
        );

        if (cursor != null && cursor.moveToFirst()) {
            String uriString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PROFILE_IMAGE_URI));
            Uri imageUri = Uri.parse(uriString);
            requireActivity().runOnUiThread(() -> Glide.with(this)
                    .load(imageUri)
                    .into(profile_image));
            cursor.close();
        }
        db.close();
    }


}
