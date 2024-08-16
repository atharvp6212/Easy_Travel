package com.atharv.image_capt_tran;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.soundcloud.android.crop.Crop;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private ImageView imageView;
    private Button additionalButton, playmusic, new_activity; // The additional button you added
    private boolean imageLoaded = false; // Flag to track if an image has been loaded
    private Bitmap imageBitmap;
    private TextView textView, source_lang, end_language;

    String identifiedLanguageName;


    //lafda zala tar delete
    boolean[] selectedLanguage;
    ArrayList<Integer> langList = new ArrayList<>();
    String[] langArray = {"English", "Marathi", "Spanish", "Chinese", "German", "French"};
    private String sourceLanguageCode = ""; // Initialize with empty string
    private String endLanguageCode = ""; // Initialize with empty string
    private TextToSpeech textToSpeech;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    // Set language to the default locale
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

        // Initialize UI components
        Button cameraButton = findViewById(R.id.camera_button);
        Button galleryButton = findViewById(R.id.gallery_button);
        imageView = findViewById(R.id.image_view);
        additionalButton = findViewById(R.id.translate_button); // Initialize your additional button
        textView = findViewById(R.id.showtextView);
        source_lang = findViewById(R.id.select_source_lang);
        end_language = findViewById(R.id.select_end_lang);
        playmusic = findViewById(R.id.audio);
        new_activity = findViewById(R.id.next_button);

        selectedLanguage = new boolean[langArray.length];

        source_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // set title
                builder.setTitle("Select Language");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(langArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            langList.add(i);
                            // Sort array list
                            Collections.sort(langList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(langArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        source_lang.setText(stringBuilder.toString());
                        sourceLanguageCode = langArray[langList.get(0)];
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                // show dialog
                builder.show();
            }
        });

        new_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                startActivity(intent);
            }
        });
        end_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // set title
                builder.setTitle("Select Language");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(langArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            langList.add(i);
                            // Sort array list
                            Collections.sort(langList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(langArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        end_language.setText(stringBuilder.toString());
                        endLanguageCode = langArray[langList.get(0)];
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                // show dialog
                builder.show();
            }
        });

        // Set click listener for the camera button
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    openCamera();
                }
            }
        });

        // Set click listener for the gallery button
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkGalleryPermission()) {
                    openGallery();
                }
            }
        });
        additionalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageLoaded) {
                    detectTextFromImage(imageBitmap);
                } else {
                    textView.setText("Please load an image first!");
                }
            }
        });

        playmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text from showtextView
                String text = textView.getText().toString();
                // Speak the tex
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    // Check if camera permission is granted
    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            return false;
        }
        return true;
    }

    // Check if gallery permission is granted
    private boolean checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_GALLERY);
            return false;
        }
        return true;
    }

    // Open the camera
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Open the gallery
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
    }

    // Handle the captured/selected image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    imageBitmap = (Bitmap) extras.get("data");
                    // Start the crop activity with the captured image
                    startCropActivity(imageBitmap);
                }
            } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                if (data != null && data.getData() != null) {
                    // Get the selected image URI
                    Uri selectedImageUri = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        // Start the crop activity with the selected image
                        startCropActivity(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data);
            }
        }
    }

    private void startCropActivity(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            // Create a temporary file to save the cropped image
            Uri tempUri = Uri.fromFile(new File(getExternalCacheDir(), "cropped_image"));
            // Start the crop activity
            Crop.of(getImageUri(this, imageBitmap), tempUri).start(this);
        }
    }

    // Method to handle the cropped image
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri imageUri = Crop.getOutput(result);
            try {
                // Get the cropped image bitmap
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                // Display the cropped image
                imageView.setImageBitmap(imageBitmap);
                imageLoaded = true; // Set the flag to true
                additionalButton.setVisibility(View.VISIBLE); // Show the additional button
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Exception error = (Exception) Crop.getError(result);
            error.printStackTrace();
        }
    }
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    private void detectTextFromImage(Bitmap imageBitmap) {
        // Create an InputImage object from the Bitmap
        InputImage image = InputImage.fromBitmap(imageBitmap, 0);

        // Get a TextRecognizer instance
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // Process the image for text
        recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        // Text recognition successful, extract text
                        String recognizedText = extractText(visionText);
                        textView.setText(recognizedText); // Display the extracted text
                        translateText(recognizedText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Text recognition failed, handle error
                        textView.setText("Text recognition failed!");
                    }
                });
    }
    private String extractText(Text visionText) {
        StringBuilder extractedText = new StringBuilder();
        for (Text.TextBlock block : visionText.getTextBlocks()) {
            extractedText.append(block.getText() + "\n");
        }
        return extractedText.toString();
    }

    private void translateText(String text) {
        // Get the detected source language name from the global variable
        String sourceLanguageName = source_lang.getText().toString(); // Retrieve text content of source_lang TextView

        // Get the detected end language name from the global variable
        String endLanguageName = end_language.getText().toString(); // Retrieve text content of end_language TextView

        // Map language names to language codes
        String sourceLangCode = getLanguageCode(sourceLanguageName);
        String endLangCode = getLanguageCode(endLanguageName);

        if (sourceLangCode == null || endLangCode == null) {
            textView.setText("Translation failed: Invalid source or end language name");
            return;
        }

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLangCode)
                .setTargetLanguage(endLangCode)
                .build();
        Translator translator = Translation.getClient(options);

        // Download the translation model if needed
        translator.downloadModelIfNeeded()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Model downloaded successfully, perform translation
                        translator.translate(text)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translatedText) {
                                        // Display the translated text
                                        textView.setText("Translated Text:\n" + translatedText);
                                        playmusic.setVisibility(View.VISIBLE);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Translation failed, handle error
                                        textView.setText("Translation failed: " + e.getMessage());
                                        Log.e("TranslationError", "Translation failed", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model download failed, handle error
                        textView.setText("Model download failed: " + e.getMessage());
                        Log.e("ModelDownloadError", "Model download failed", e);
                    }
                });
    }

    // Method to get language code from language name
    private String getLanguageCode(String languageName) {
        switch (languageName) {
            case "English":
                return "en";
            case "Marathi":
                return "mr";
            case "Spanish":
                return "es";
            case "Chinese":
                return "zh";
            case "German":
                return "de";
            case "French":
                return "fre";
            default:
                return null;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // Shutdown TextToSpeech
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }




}