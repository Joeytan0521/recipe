package com.example.recipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

public class bnv_fragment3_profile extends Fragment {

    ImageView camera_icon;
    ImageView profile_image;
    private Uri photoUri;

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    if (photoUri != null) {
                        profile_image.setImageURI(photoUri); //use glide
                    } else if (result.getData() != null) {
                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        profile_image.setImageBitmap(photo);
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bnv_fragment3_profile, container, false);

        camera_icon = view.findViewById(R.id.camera_icon);
        profile_image = view.findViewById(R.id.profile_image);

        camera_icon.setOnClickListener(v -> {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File photoFile = createImageFile();
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(getContext(), "com.example.recipe.fileprovider", photoFile);
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }

            cameraLauncher.launch(camera_intent);
        });

        return view;
    }

    private File createImageFile() {
        try {
            File storageDir = getActivity().getExternalFilesDir(null);
            File image = new File(storageDir, "flashCropped.png");
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
