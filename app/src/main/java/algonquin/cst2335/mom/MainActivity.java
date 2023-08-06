package algonquin.cst2335.mom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String PREFERENCES_FILE = "my_preferences";
    private static final String KEY_WIDTH = "width";
    private static final String KEY_HEIGHT = "height";
    private EditText editTextWidth;
    private EditText editTextHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextWidth = findViewById(R.id.editTextWidth);
        editTextHeight = findViewById(R.id.editTextHeight);

        // Create and set individual TextWatchers for width and height
        editTextWidth.addTextChangedListener(createValidationTextWatcher(editTextWidth));
        editTextHeight.addTextChangedListener(createValidationTextWatcher(editTextHeight));

        // Fetch the saved values from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_FILE, MODE_PRIVATE);
        int savedWidth = prefs.getInt(KEY_WIDTH, 0);
        int savedHeight = prefs.getInt(KEY_HEIGHT, 0);

        editTextWidth.setText(savedWidth != 0 ? String.valueOf(savedWidth) : "");
        editTextHeight.setText(savedHeight != 0 ? String.valueOf(savedHeight) : "");

        Button generateButton = findViewById(R.id.generateButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String width = editTextWidth.getText().toString();
                String height = editTextHeight.getText().toString();

                if(!TextUtils.isEmpty(width) && !TextUtils.isEmpty(height)) {
                    String url = "https://placebear.com/" + width + "/" + height;

                    Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
                    intent.putExtra("IMAGE_URL", url);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter valid dimensions!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        try {
            int currentWidth = Integer.parseInt(editTextWidth.getText().toString());
            int currentHeight = Integer.parseInt(editTextHeight.getText().toString());

            editor.putInt(KEY_WIDTH, currentWidth);
            editor.putInt(KEY_HEIGHT, currentHeight);
        } catch (NumberFormatException e) {
            // handle or log the error if required
        }

        editor.apply();  // This saves the values
    }

    /**
     * Create a validation TextWatcher for an EditText.
     * This ensures the input starts with a non-zero number.
     *
     * @param editText The EditText to validate.
     * @return A validation TextWatcher.
     */
    private TextWatcher createValidationTextWatcher(EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().startsWith("0")) {
                    editText.setError("Invalid number");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Further validations can be added here if required
            }
        };
    }
}
