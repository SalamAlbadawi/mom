package algonquin.cst2335.mom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageViewActivity extends AppCompatActivity {

    ImageView loadedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // Fetch the ImageView
        loadedImageView = findViewById(R.id.loadedImageView);

        // Get the URL from the Intent extras
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        // Use Glide to load the image
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        loadedImageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Toast.makeText(ImageViewActivity.this, "Failed to load the image", Toast.LENGTH_SHORT).show();
                    }
                });

        // Fetch the Save Button
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ImageViewActivity.this)
                        .setTitle("Save Image")
                        .setMessage("Do you want to save this picture?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                saveImageToDatabase();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void saveImageToDatabase() {
        // Convert the image into byte array
        Bitmap bitmap = ((BitmapDrawable) loadedImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save to database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        boolean success = databaseHelper.addImage(byteArray);
        databaseHelper.close();

        if (success) {
            Toast.makeText(this, "Image saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error saving image.", Toast.LENGTH_SHORT).show();
        }
    }
}
