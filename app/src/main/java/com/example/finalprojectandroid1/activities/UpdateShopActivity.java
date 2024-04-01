package com.example.finalprojectandroid1.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import android.app.DownloadManager;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.AddOwnedShop;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.MyOwnedShops;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.SetWeekdayWorkingTimeDialog;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.WeekdayWorkTime;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.app.FragmentManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UpdateShopActivity extends AppCompatActivity {

    String TAG = "uUpdateShopActivity";

    ImageView shopImage;
    Bitmap scaledImageBitmap;

    TableLayout sunRowLayout;
    TableLayout monRowLayout;
    TableLayout tueRowLayout;
    TableLayout wedRowLayout;
    TableLayout thurRowLayout;
    TableLayout friRowLayout;
    TableLayout satRowLayout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference myRef = database.getReference("shops");
    StorageReference imageRef ;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout linksLayout;
    Button addShopLinkButton;
    ChipGroup pickedTagsChipGroup;
    LinearLayout allAppointmentNameAndLengthLayout;
    boolean toUpdate = false;
    int maxWidth = 400;
    int maxHeight = 400;



    static int linkEditTextCount = 0;
    //i have a ShopData class put the added info there u dingus

    //    HashMap<String, List<Integer[]>> defaultWorkTimeEachDay = new HashMap<>();
    HashMap<String, List<WeekdayWorkTime>> defaultWorkTimeEachDay = new HashMap<>();
    private ActivityResultLauncher<Intent> pickImageLauncher;
    String userUid;
    ShopModel shop;

    int shopPosition;
    boolean imageChanged = false;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shop);
//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
//                if(fragmentManager.getBackStackEntryCount() > 0){
//
//                    Log.d(TAG, "back pressed");
//                }else{
//                    Log.d(TAG, "back not pressed");
//                    fragmentManager.popBackStack();
//
//                }
//            }
//        });

        Bundle getValues = getIntent().getExtras();
        userUid = getValues.getString("userUid");
        Log.d(TAG, "getting user uid: " + userUid);
        shop = getValues.getParcelable("shop");

        progressBar = findViewById(R.id.progressBarShopActivity);



        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri imageUri = data.getData();

                            // Load the image into a Bitmap
                            Bitmap bitmap = loadBitmapFromUri(imageUri);

                            // Set your maximum allowed dimensions


                            // Resize the bitmap
                            scaledImageBitmap = scaleBitmap(bitmap, maxWidth, maxHeight);

                            // Set the scaled bitmap to the ImageView
                            shopImage.setImageBitmap(scaledImageBitmap);
                            shopImage.setVisibility(View.VISIBLE);
                            imageChanged = true;

                        }
                    } else {
                        Toast.makeText(this, "לא ניתן לגשת לגלריה.", Toast.LENGTH_SHORT).show();
                    }
                });

        Log.d(TAG, "inside");

//        MainActivity mainActivity = MainActivity

        EditText shopName = findViewById(R.id.addShopName);

        EditText shopAddress = findViewById(R.id.addShopAddress);

        shopImage = findViewById(R.id.shopImage);

        Button addShopImageButton = findViewById(R.id.addShopImageButton);

        EditText shopDes = findViewById(R.id.addShopDes);

        addShopLinkButton = findViewById(R.id.addShopLinkButton);
        linksLayout = findViewById(R.id.linksLayout);

        Spinner addTagsSpinner = findViewById(R.id.addShopTagsSpinner);
        pickedTagsChipGroup = findViewById(R.id.pickedTagsChipGroup);
        String[] spinnerTagsList = {"בחר תגית", "איפור", "מספרה", "הסרת שיער בלייזר", "חייט", "שיעורים פרטיים", "עיצוב ציפרניים"};
        ArrayAdapter<String> spinnerTagsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerTagsList);

//        EditText appointmentTime = view.findViewById(R.id.appointmentTime);
////        EditText appointmentType = view.findViewById()
        allAppointmentNameAndLengthLayout = findViewById(R.id.allAppointmentNameAndLengthLayout);
        Button addAppointmentNameAndLengthButton = findViewById(R.id.addAppointmentNameAndLengthButton);

        Button addWorkingTimeButton = findViewById(R.id.addWorkingTimeRangeButton);

        sunRowLayout = findViewById(R.id.sunLayout);
        monRowLayout = findViewById(R.id.monLayout);
        tueRowLayout = findViewById(R.id.tueLayout);
        monRowLayout = findViewById(R.id.monLayout);
        wedRowLayout = findViewById(R.id.wedLayout);
        thurRowLayout = findViewById(R.id.thurLayout);
        friRowLayout = findViewById(R.id.friLayout);
        satRowLayout = findViewById(R.id.satLayout);

        Button addShopButton = findViewById(R.id.addTheShopButton);


        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT  // Height, adjust as needed
        );

        if(shop != null){
            toUpdate = true;
            addShopButton.setText("עדכן חנות");
            Log.d(TAG,"shop name not null");

            shopName.setText(shop.getShopName());

            shopDes.setText(shop.getShopDes());

            shopAddress.setText(shop.getShopAddress());

            String imageUrl = shop.getShopImage().toString();
            Glide.with(this).asBitmap().load(imageUrl)
                    .into(new CustomTarget<Bitmap>() {

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            scaledImageBitmap = scaleBitmap(resource, maxWidth,maxHeight);
                            shopImage.setImageBitmap(scaledImageBitmap);
                            shopImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });


            for(String link : shop.getShopLinks()){
                updateLinks(link);
            }

            defaultWorkTimeEachDay = (HashMap<String, List<WeekdayWorkTime>>) getValues.getSerializable("shopDefaultAvailableTime");
            for(String day : defaultWorkTimeEachDay.keySet()){
                updateDaysTable(day, defaultWorkTimeEachDay.get(day));
            }

            for(String tag : shop.getShopTags()){
                updateTags(tag);
            }

            HashMap<String,Integer> shopAppointsTypes = (HashMap<String, Integer>) getValues.getSerializable("shopAppointsTypes");

            String[] shopAppointsTypesKeys =  shopAppointsTypes.keySet().toArray(new String[0]);

            EditText firstAppointTypeText = findViewById(R.id.firstAppointTypeName);
            firstAppointTypeText.setText(shopAppointsTypesKeys[0]);

            EditText firstAppointTypeTime = findViewById(R.id.firstAppointTypeTime);
            firstAppointTypeTime.setText((shopAppointsTypes.get(shopAppointsTypesKeys[0]).toString()));

            for(int i = 1 ; i < shopAppointsTypesKeys.length ; i++){
                String typeName = shopAppointsTypesKeys[i];
                String typeTime = shopAppointsTypes.get(typeName).toString();
                Log.d(TAG,"appointName: " + typeName);
                Log.d(TAG,"appointTime: " + typeTime);
                updateAppointTypeAndTime(typeName, typeTime);
            }


        }else{
            Log.d(TAG,"shop name null");
        }
        addShopImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                pickImageLauncher.launch(Intent.createChooser(i, "Select Picture"));

            }
        });



        addShopLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLinks( null);

//                LinearLayout eachLinkLayout = new LinearLayout(v.getContext());
//                EditText newLink = new EditText(v.getContext());
//                Button deleteLinkButton = new Button(v.getContext());
//
//
//                layoutParams.weight = 1;
//
//                eachLinkLayout.setLayoutParams(layoutParams);
//
//                newLink.setHint("לינק");
//                newLink.setLayoutParams(layoutParams);
//
//                deleteLinkButton.setText("מחק לינק");
//                deleteLinkButton.setLayoutParams(layoutParams);
//                deleteLinkButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        linksLayout.removeView(eachLinkLayout);
//                        linkEditTextCount--;
//                        addShopLinkButton.setEnabled(true);
//
//                    }
//                });
//                eachLinkLayout.addView(deleteLinkButton);
//                eachLinkLayout.addView(newLink);
//                linksLayout.addView(eachLinkLayout);
//                linkEditTextCount++;
//                if (linkEditTextCount == 3) {
//                    addShopLinkButton.setEnabled(false);
//                }
//                Log.d(TAG, "links count: " + linkEditTextCount);

                // Validate and check domain of the link

            }
        });

        spinnerTagsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addTagsSpinner.setAdapter(spinnerTagsAdapter);

        addTagsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTag = (String) parent.getItemAtPosition(position);
                if (!selectedTag.equals("בחר תגית")) {
//                    Chip selectedTagChip = new Chip(pickedTagsChipGroup.getContext());
//                    selectedTagChip.setText(selectedTag);
//                    selectedTagChip.setCloseIconVisible(true);
//                    selectedTagChip.setOnCloseIconClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            pickedTagsChipGroup.removeView(v);
//                        }
//                    });
//
//                    pickedTagsChipGroup.addView(selectedTagChip);
                    updateTags(selectedTag);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });


        addWorkingTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetWeekdayWorkingTimeDialog(UpdateShopActivity.this);
//                Log.d(TAG,daysSelected + " , " + timeSelected );
            }
        });

        addAppointmentNameAndLengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LinearLayout appointmentNameAndLengthLayout = new LinearLayout(v.getContext());
//                appointmentNameAndLengthLayout.setLayoutParams(layoutParams);
//                layoutParams.weight = 1;
//
//                TextView minutes = new TextView(v.getContext());
//                minutes.setText("דק");
//
//                EditText appointmentTime = new EditText(v.getContext());
//                appointmentTime.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//                TextView hyphen = new TextView(v.getContext());
//                hyphen.setText("  -  ");
//
//                EditText appointmentName = new EditText(v.getContext());
//                appointmentName.setInputType(InputType.TYPE_CLASS_TEXT);
//
//                Button deleteAppointmentNameAndType = new Button(v.getContext());
//                deleteAppointmentNameAndType.setText("הסר תור");
//
//                appointmentNameAndLengthLayout.addView(minutes);
//                appointmentNameAndLengthLayout.addView(appointmentTime);
//                appointmentNameAndLengthLayout.addView(hyphen);
//                appointmentNameAndLengthLayout.addView(appointmentName);
//                appointmentNameAndLengthLayout.addView(deleteAppointmentNameAndType);
//
//                deleteAppointmentNameAndType.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        allAppointmentNameAndLengthLayout.removeView(appointmentNameAndLengthLayout);
//                    }
//                });
//
//                allAppointmentNameAndLengthLayout.addView(appointmentNameAndLengthLayout);
                updateAppointTypeAndTime(null,null);
            }
        });


        addShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saveShopName = shopName.getText().toString();
                Log.d(TAG, "shop name: " + saveShopName);

                String saveShopAddress = shopAddress.getText().toString();
                Log.d(TAG, "shop address: " + saveShopAddress);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                scaledImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                String saveShopDes = shopDes.getText().toString();
                Log.d(TAG, "shop des: " + saveShopDes);

                ArrayList<String> linksArray = new ArrayList<>();

                for (int i = 0; i < linksLayout.getChildCount(); i++) {
                    View child = linksLayout.getChildAt(i);
                    if (child instanceof LinearLayout) {
                        LinearLayout linkLayout = (LinearLayout) child;
                        // Iterate through the child views of each linkLayout
                        for (int j = 0; j < linkLayout.getChildCount(); j++) {
                            View innerChild = linkLayout.getChildAt(j);
                            if (innerChild instanceof EditText) {
                                EditText linkEditText = (EditText) innerChild;
                                // Get the text from the EditText and do whatever you need with it
                                String linkText = linkEditText.getText().toString();
                                if (!linkText.isEmpty()) {

                                    if (isValidURL(linkText)) {
//
                                        isLinkReachable(linkText);
                                        linksArray.add(linkText);
                                    } else {
                                        // Invalid URL
                                        Log.d(TAG, "Invalid URL");
                                    }
                                }

                            }
                        }
                    }
                }

                for(String link : linksArray){
                    Log.d(TAG,"link text: " + link);
                }

                ArrayList<String> tagsList = new ArrayList<>();

                for (int i = 0; i < pickedTagsChipGroup.getChildCount(); i++) {
                    View child = pickedTagsChipGroup.getChildAt(i);
                    if (child instanceof Chip) {
                        Chip tagChild = (Chip) child;
                        String text = tagChild.getText().toString();
                        tagsList.add(text);

                    }
                }

                //add the work time

                Log.d(TAG, "tags: " + tagsList);

                HashMap<String,Integer> appointmentsType = new HashMap<>();

                for (int i = 0; i < allAppointmentNameAndLengthLayout.getChildCount(); i++) {
                    View child = allAppointmentNameAndLengthLayout.getChildAt(i);
                    if (child instanceof LinearLayout) {
                        LinearLayout linkLayout = (LinearLayout) child;
                        // Iterate through the child views of each linkLayout
                        String nameText = null;
                        String timeText = null;
                        for (int j = 0; j < linkLayout.getChildCount(); j++) {
                            View innerChild = linkLayout.getChildAt(j);
                            if (innerChild instanceof EditText) {
                                if (((EditText) innerChild).getInputType() == InputType.TYPE_CLASS_TEXT) {
                                    EditText nameEditText = (EditText) innerChild;
                                    nameText = nameEditText.getText().toString();
//                                    Log.d(TAG, "appo name " + (i + 1) + ": " + nameText);
                                }
                                if (((EditText) innerChild).getInputType() == InputType.TYPE_CLASS_NUMBER) {
                                    EditText nameEditText = (EditText) innerChild;
                                    timeText = nameEditText.getText().toString();
//                                    Log.d(TAG, "appo time " + (i + 1) + ": " + timeText);
                                }
                            }
                        }
                        if(!timeText.isEmpty()){
                            appointmentsType.put(nameText,Integer.valueOf(timeText));
                        }

                    }
                }

                for(String name : appointmentsType.keySet()){
                    Log.d(TAG, "app name: " + name + ", app time: " + appointmentsType.get(name));
                }

                Drawable emptyShopImage = ContextCompat.getDrawable(v.getContext(), R.drawable.shop_empty_photo);

                Query query = FirebaseDatabase.getInstance().getReference("shops").orderByChild("shopAddress").equalTo(saveShopAddress);

                if (saveShopName.isEmpty() || saveShopDes.isEmpty() || saveShopAddress.isEmpty()||
                        shopImage.getDrawable().getConstantState().equals(emptyShopImage.getConstantState()) ||
                        tagsList.isEmpty() || appointmentsType.isEmpty() || defaultWorkTimeEachDay.isEmpty()) {
                    Toast.makeText(v.getContext(), "נא להזין את כל שדות החובה", Toast.LENGTH_SHORT).show();
                }else{
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists() && !snapshot.child(shop.getShopUid()).exists()){
                                Toast.makeText(UpdateShopActivity.this, "הכתובת שהוזנה כבר קיימת במערכת", Toast.LENGTH_SHORT).show();
                            }else{
                                if(!toUpdate){
                                    addOwnedShop(saveShopName,saveShopAddress, imageData,
                                            saveShopDes, linksArray,tagsList,appointmentsType,
                                            defaultWorkTimeEachDay,null);
                                }else{

                                    addOwnedShop(saveShopName,saveShopAddress, imageData,
                                            saveShopDes, linksArray,tagsList,appointmentsType,
                                            defaultWorkTimeEachDay,shop.getShopUid());
                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
    }

    private void updateTags(String selectedTag){
        Chip selectedTagChip = new Chip(pickedTagsChipGroup.getContext());
        selectedTagChip.setText(selectedTag);
        selectedTagChip.setCloseIconVisible(true);
        selectedTagChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickedTagsChipGroup.removeView(v);
            }
        });

        pickedTagsChipGroup.addView(selectedTagChip);
    }

    private void updateLinks( String link){
        LinearLayout eachLinkLayout = new LinearLayout(this);
        EditText newLink = new EditText(this);
        Button deleteLinkButton = new Button(this);


        layoutParams.weight = 1;

        eachLinkLayout.setLayoutParams(layoutParams);

        newLink.setHint("לינק");
        if(toUpdate){
            newLink.setText(link);
        }
        newLink.setLayoutParams(layoutParams);

        deleteLinkButton.setText("מחק לינק");
        deleteLinkButton.setLayoutParams(layoutParams);
        deleteLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linksLayout.removeView(eachLinkLayout);
                linkEditTextCount--;
                addShopLinkButton.setEnabled(true);

            }
        });
        eachLinkLayout.addView(deleteLinkButton);
        eachLinkLayout.addView(newLink);
        linksLayout.addView(eachLinkLayout);
        linkEditTextCount++;
        if (linkEditTextCount == 3) {
            addShopLinkButton.setEnabled(false);
        }
        Log.d(TAG, "links count: " + linkEditTextCount);
    }
    public void updateAppointTypeAndTime(String appointNameText, String appointTimeText){
        LinearLayout appointmentNameAndLengthLayout = new LinearLayout(this);
        appointmentNameAndLengthLayout.setLayoutParams(layoutParams);
        layoutParams.weight = 1;

        LinearLayout.LayoutParams appointLayoutParams =  new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT  // Height, adjust as needed
        );

        TextView minutes = new TextView(this);
        minutes.setText("דק");
        minutes.setLayoutParams(appointLayoutParams);

        EditText appointmentTime = new EditText(this);
        appointmentTime.setInputType(InputType.TYPE_CLASS_NUMBER);
        appointmentTime.setLayoutParams(appointLayoutParams);


        TextView hyphen = new TextView(this);
        hyphen.setText("  -  ");
        hyphen.setLayoutParams(appointLayoutParams);

        EditText appointmentName = new EditText(this);
        appointmentName.setInputType(InputType.TYPE_CLASS_TEXT);
        appointmentName.setLayoutParams(appointLayoutParams);

        if(appointTimeText != null){
            appointmentTime.setText(appointTimeText);
            appointmentName.setText(appointNameText);
        }

        Button deleteAppointmentNameAndType = new Button(this);
        deleteAppointmentNameAndType.setText("הסר תור");
        deleteAppointmentNameAndType.setLayoutParams(appointLayoutParams);


        appointmentNameAndLengthLayout.addView(minutes);
        appointmentNameAndLengthLayout.addView(appointmentTime);
        appointmentNameAndLengthLayout.addView(hyphen);
        appointmentNameAndLengthLayout.addView(appointmentName);
        appointmentNameAndLengthLayout.addView(deleteAppointmentNameAndType);

        deleteAppointmentNameAndType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allAppointmentNameAndLengthLayout.removeView(appointmentNameAndLengthLayout);
            }
        });

        allAppointmentNameAndLengthLayout.addView(appointmentNameAndLengthLayout);
    }

    private Bitmap loadBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = this.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream);
            } else {
                Log.e(TAG, "Input stream is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error loading bitmap from URI: " + e.getMessage());
        }
        return null;
    }
    private Bitmap scaleBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scale = Math.min((float) maxWidth / width, (float) maxHeight / height);

        Matrix matrix = new Matrix();
        if(!toUpdate){
            matrix.postRotate(90);
        }
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private void isLinkReachable(String urlString) {
        AsyncTask.execute(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                int responseCode = connection.getResponseCode();
                boolean isReachable = (responseCode == HttpURLConnection.HTTP_OK);
                // You can do something with the result here, such as displaying a toast
                // or updating UI elements.
                Log.d(TAG, "Link " + urlString + " is reachable: " + isReachable);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                // Handle invalid URL error
                Log.e(TAG, "Invalid URL: " + urlString);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle other IO-related errors (e.g., network issues)
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpdateShopActivity.this, "Error connecting to URL: " + urlString, Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG, "Error connecting to URL: " + urlString);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle other unexpected errors
                Log.e(TAG, "Unexpected error: " + e.getMessage());
            }
        });
    }

    private String extractDomain(String url) {
        try {
            URI uri = new URI(url);

            String domain = uri.getHost();
            if (domain != null) {
                return domain.startsWith("www.") ? domain.substring(4) : domain;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }
    private boolean isValidURL(String url) {
        // Implement URL validation logic here
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    /// ____________________________move to main activity???______________________________________//
    // Method to detect social media platform from a domain
    private String detectSocialMedia(String domain) {
        // Implement domain comparison with known social media domains
        // You can use the approach described in the previous response
        return domain;
    }

    // _____________________________________________________________________//


    public void updateWorkTime(ArrayList<String> days,int startHour, int startMinutes, int endHour, int endMinutes){
        int newStartTime;
        int newEndTime;
        if(startMinutes < 10){
            newStartTime = Integer.valueOf(startHour + "0" + startMinutes);
        }else{
            newStartTime = Integer.valueOf(startHour + "" + startMinutes);
        }
        if(endMinutes < 10){
            newEndTime = Integer.valueOf(endHour + "0" + endMinutes);
        }else{
            newEndTime = Integer.valueOf(endHour + "" + endMinutes);
        }
        Log.d(TAG, "newStartTime: " + newStartTime + ", newEndTime: " + newEndTime);
        for(String day : days){

            boolean timeInDay = false;
            if(defaultWorkTimeEachDay.containsKey(day)){
                for(WeekdayWorkTime time : defaultWorkTimeEachDay.get(day)){
                    int startInTime = time.getStartTime();
                    int endInTime = time.getEndTime();
                    if ((startInTime <= newStartTime && newStartTime < endInTime) ||
                            (startInTime < newEndTime && newEndTime <= endInTime) ||
                            (newStartTime <= startInTime && endInTime <= newEndTime)) {
                        timeInDay = true;
                        Toast.makeText(UpdateShopActivity.this, "הזמן שהוזן חופף עם זמן קיים.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (!timeInDay) {
                    WeekdayWorkTime newTime = new WeekdayWorkTime(newStartTime,newEndTime);
                    defaultWorkTimeEachDay.get(day).add(newTime);
                }
            }else{
                WeekdayWorkTime timeArray = new WeekdayWorkTime(newStartTime, newEndTime);
                ArrayList<WeekdayWorkTime> allTimeArray = new ArrayList<>();
                allTimeArray.add(timeArray);

                defaultWorkTimeEachDay.put(day,allTimeArray);
            }
        }
        updateDefaultDaysHash();
    }




    private void updateDefaultDaysHash() {
        for (Map.Entry<String, List<WeekdayWorkTime>> entry : defaultWorkTimeEachDay.entrySet()) {
            String day = entry.getKey();
            List<WeekdayWorkTime> timeRanges = entry.getValue();

            // Sort timeRanges based on start time
            timeRanges.sort(Comparator.comparingInt(WeekdayWorkTime::getStartTime));

            updateDaysTable(day, timeRanges);

            // Print sorted time ranges
            for (WeekdayWorkTime time : timeRanges) {
                int startTime = time.getStartTime();
                int endTime = time.getEndTime();
                System.out.println("Day: " + day + ", Start Time: " + startTime + ", End Time: " + endTime);
            }
        }
    }

    public void updateDaysTable(String day, List<WeekdayWorkTime> timeArray){
        TableLayout updateTable = new TableLayout(UpdateShopActivity.this);
        for (WeekdayWorkTime time : timeArray) {
            TableRow newWorkTimeRow = new TableRow(UpdateShopActivity.this);
            TextView showTime = new TextView(UpdateShopActivity.this);

            Button deleteNewTime = new Button(UpdateShopActivity.this);

            String startTimeStr = String.valueOf(time.getStartTime());
            String endTimeStr = String.valueOf(time.getEndTime());

            if (startTimeStr.length() < 4) {
                startTimeStr = "0" + startTimeStr;
            }
            if (endTimeStr.length() < 4) {
                endTimeStr = "0" + endTimeStr;
            }
            String formattedStartTimeStr = startTimeStr.substring(0, 2) + ":" + startTimeStr.substring(2);
            String formattedEndTimeStr = endTimeStr.substring(0, 2) + ":" + endTimeStr.substring(2);

            showTime.setText(formattedStartTimeStr + " - " + formattedEndTimeStr);

            deleteNewTime.setText("מחק שעה");
            deleteNewTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newWorkTimeRow.removeView(showTime);
                    newWorkTimeRow.removeView(deleteNewTime);
                    defaultWorkTimeEachDay.get(day).remove(time);
                    updateDefaultDaysHash();
                }
            });
            newWorkTimeRow.addView(deleteNewTime);
            newWorkTimeRow.addView(showTime);
            updateTable.addView(newWorkTimeRow);
        }

        switch (day) {
            case "א":
                sunRowLayout.removeAllViews(); // Clear existing views
                sunRowLayout.addView(updateTable); // Add the new table
                break;
            case "ב":
                monRowLayout.removeAllViews(); // Clear existing views
                monRowLayout.addView(updateTable); // Add the new table
                break;
            case "ג":
                tueRowLayout.removeAllViews(); // Clear existing views
                tueRowLayout.addView(updateTable); // Add the new table
                break;
            case "ד":
                wedRowLayout.removeAllViews(); // Clear existing views
                wedRowLayout.addView(updateTable); // Add the new table
                break;
            case "ה":
                thurRowLayout.removeAllViews(); // Clear existing views
                thurRowLayout.addView(updateTable); // Add the new table
                break;
            case "ו":
                friRowLayout.removeAllViews(); // Clear existing views
                friRowLayout.addView(updateTable); // Add the new table
                break;
            case "ש":
                satRowLayout.removeAllViews(); // Clear existing views
                satRowLayout.addView(updateTable); // Add the new table
                break;
            default:
        }

    }
    private void addOwnedShop(String shopName, String shopAddress, byte[] imageData,
                             String shopDes, ArrayList<String> links, ArrayList<String> tags,
                             HashMap<String,Integer> appointmentType, HashMap<String, List<WeekdayWorkTime>> defaultWorkTimeEachDay, String shopUid){

        Log.d(TAG,"add shop");

        final String finalShopUid;
        // Generate a unique key for the new shop
        if(shopUid == null){
            finalShopUid = myRef.push().getKey();
        }else{
            finalShopUid = shopUid;
        }



        DatabaseReference newShopRef = myRef.child(shopUid);

        // Upload shop image
        imageRef = storageRef.child("shops/images/" + shopUid + ".jpg");
        progressBar.setVisibility(View.VISIBLE);

        UploadTask uploadTask = imageRef.putBytes(imageData);

        uploadTask.addOnFailureListener(exception -> {
            progressBar.setVisibility(View.GONE);
            // Handle unsuccessful uploads
            Log.d(TAG, "Image upload failed: " + exception.getMessage());
            Toast.makeText(UpdateShopActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {

            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl;
                if(imageChanged){
                     imageUrl = uri.toString();
                }else{
                     imageUrl = shop.getShopImage().toString();
                }


                ShopModel newShop = new ShopModel(finalShopUid,shopName, shopAddress, imageUrl, shopDes,
                        userUid, appointmentType, tags, links,defaultWorkTimeEachDay);

                // Save the new shop data to the database
                try {
                    newShopRef.setValue(newShop).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "shop updated successfully");
//                            ownedShopList.add(newShop);
                            progressBar.setVisibility(View.GONE);
                            Intent i;
                            if(toUpdate){
                                i = new Intent(UpdateShopActivity.this, ShopInfoActivity.class);
                                i.putExtra("shopDefaultAvailableTime", newShop.getShopDefaultAvailableTime());
                                i.putExtra("shopSetAppointment", newShop.getShopSetAppointment());
                                i.putExtra("shop",  newShop);
                            }else{
                                i = new Intent(UpdateShopActivity.this, MainActivity.class);
                                i.putExtra("updateShop",1);
                            }

                            Log.d(TAG, "user :" + userUid);
                            i.putExtra("curUserUid", userUid);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(UpdateShopActivity.this, "העלאת החנות נכשלה. נסה שוב", Toast.LENGTH_SHORT).show();
                        }
                    });
//

                   Log.d(TAG,newShop.toString()) ;

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "Error setValue: " + e.getMessage());
                }

            });
        });
    }


}