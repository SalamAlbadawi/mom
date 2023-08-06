package algonquin.cst2335.mom;


import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // Fetch the ImageView
        ImageView loadedImageView = findViewById(R.id.loadedImageView);

        // Get the URL from the Intent extras
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        // Use Glide to load the image
        Glide.with(this)
                .load(imageUrl)
                .into(loadedImageView);
    }
}
