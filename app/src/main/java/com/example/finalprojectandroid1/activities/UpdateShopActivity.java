package com.example.finalprojectandroid1.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.bumptech.glide.request.transition.Transition;
import com.example.finalprojectandroid1.CitiesListForSpinners;
import com.example.finalprojectandroid1.R;
//import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.AddOwnedShop;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.SetWeekdayWorkingTimeDialog;
import com.example.finalprojectandroid1.shop.AppointmentsTimeAndPrice;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.example.finalprojectandroid1.shop.shopFragments.Address;
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
    int appointsNum = 0;



    static int linkEditTextCount = 0;
    //i have a ShopData class put the added info there u dingus

    //    HashMap<String, List<Integer[]>> defaultWorkTimeEachDay = new HashMap<>();
    HashMap<String, List<TimeRange>> defaultWorkTimeEachDay = new HashMap<>();
    private ActivityResultLauncher<Intent> pickImageLauncher;
    String userUid;
    ShopModel shop;

    int shopPosition;
    boolean imageChanged = false;
    ProgressBar progressBar;
    String addressCity;
    String addressStreet;
    int addressHouseNum;
    int addressFloor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shop);

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


//        MainActivity mainActivity = MainActivity

        EditText shopName = findViewById(R.id.addShopName);

        EditText shopAddressHouseNum = findViewById(R.id.addShopAddressHouseNum);
        EditText shopAddressFloor = findViewById(R.id.addShopAddressFloor);
        EditText shopAddressStreet = findViewById(R.id.addShopAddressStreet);

        Spinner citiesSpinner = findViewById(R.id.citiesSpinner);
        String[] citiesList = CitiesListForSpinners.citiesList;
        ArrayAdapter<String> citiesSpinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, citiesList);
        citiesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citiesSpinner.setAdapter(citiesSpinnerAdapter);
//        String addressCity;



        shopImage = findViewById(R.id.shopImage);

        Button addShopImageButton = findViewById(R.id.addShopImageButton);

        EditText shopDes = findViewById(R.id.addShopDes);

        addShopLinkButton = findViewById(R.id.addShopLinkButton);
        linksLayout = findViewById(R.id.linksLayout);

        Spinner addTagsSpinner = findViewById(R.id.addShopTagsSpinner);
        pickedTagsChipGroup = findViewById(R.id.pickedTagsChipGroup);
        String[] spinnerTagsList = {"בחר תגית", "איפור", "מספרה", "הסרת שיער בלייזר", "חייט", "שיעורים פרטיים", "עיצוב ציפרניים"};
        ArrayAdapter<String> spinnerTagsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerTagsList);
        spinnerTagsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addTagsSpinner.setAdapter(spinnerTagsAdapter);

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

        EditText firstAppointTypeText = findViewById(R.id.firstAppointTypeName);
        EditText firstAppointTypeTime = findViewById(R.id.firstAppointTypeTime);
        EditText firstAppointTypePrice = findViewById(R.id.firstAppointTypePrice);

        Button addShopButton = findViewById(R.id.addTheShopButton);


        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT  // Height, adjust as needed
        );

        if(shop != null){
            toUpdate = true;
            addShopButton.setText("עדכן חנות");

            shopName.setText(shop.getShopName());

            shopDes.setText(shop.getShopDes());

            shopAddressFloor.setText(String.valueOf(shop.getShopAddress().getFloor()));
            shopAddressHouseNum.setText(String.valueOf(shop.getShopAddress().getHouseNum()));
            shopAddressStreet.setText(shop.getShopAddress().getStreet());

            for(int i = 0; i < citiesList.length; i++){
                if(citiesList[i].equals(shop.getShopAddress().getCity())){
                    citiesSpinner.setSelection(i);
                    break;
                }
            }

//            shopAddress.setText(shop.getShopAddress());

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

            defaultWorkTimeEachDay = (HashMap<String, List<TimeRange>>) getValues.getSerializable("shopDefaultAvailableTime");
            for(String day : defaultWorkTimeEachDay.keySet()){
                updateDaysTable(day, defaultWorkTimeEachDay.get(day));
            }

            for(String tag : shop.getShopTags()){
                updateTags(tag);
            }

            HashMap<String,AppointmentsTimeAndPrice> shopAppointsTypes = (HashMap<String, AppointmentsTimeAndPrice>) getValues.getSerializable("shopAppointsTypes");


            String[] shopAppointsTypesKeys =  shopAppointsTypes.keySet().toArray(new String[0]);


            firstAppointTypeText.setText(shopAppointsTypesKeys[shopAppointsTypesKeys.length - 1]);
            firstAppointTypeTime.setText(String.valueOf(shopAppointsTypes.get(shopAppointsTypesKeys[shopAppointsTypesKeys.length - 1]).getTime()));
            firstAppointTypePrice.setText(String.valueOf(shopAppointsTypes.get(shopAppointsTypesKeys[shopAppointsTypesKeys.length - 1]).getPrice()));

            for(int i = shopAppointsTypesKeys.length - 2 ; i >= 0 ; i--){
                String appointTypeName = shopAppointsTypesKeys[i];
                String appointTypeTime = String.valueOf(shopAppointsTypes.get(appointTypeName).getTime());
                String appointTypePrice = String.valueOf(shopAppointsTypes.get(appointTypeName).getPrice());

                updateAppointTypeAndTime(appointTypeName, appointTypeTime, appointTypePrice) ;
            }


        }else{
            Log.d(TAG,"shop name null");
        }



        citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = (String) parent.getItemAtPosition(position);
                if(!selectedCity.equals("בחר עיר") ){
                    addressCity = selectedCity;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                updateLinks(null);
            }
        });





        addTagsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTag = (String) parent.getItemAtPosition(position);
                if (!selectedTag.equals("בחר תגית")) {
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
            }
        });

        addAppointmentNameAndLengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAppointTypeAndTime(null,null, null);
            }
        });


        addShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saveShopName = shopName.getText().toString();

                addressStreet = shopAddressStreet.getText().toString();

                if(!shopAddressFloor.getText().toString().isEmpty()){
                    addressFloor = Integer.parseInt(shopAddressFloor.getText().toString());
                }else{
                    Toast.makeText(v.getContext(), "נא להזין את כל שדות החובה", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!shopAddressHouseNum.getText().toString().isEmpty()){
                    addressHouseNum = Integer.parseInt(shopAddressHouseNum.getText().toString());
                }else{
                    Toast.makeText(v.getContext(), "נא להזין את כל שדות החובה", Toast.LENGTH_SHORT).show();
                    return;
                }

                Address saveShopAddress = new Address(addressStreet,addressHouseNum,addressFloor,addressCity);
                Log.d(TAG, "shop address: " + saveShopAddress);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                scaledImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                String saveShopDes = shopDes.getText().toString();

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

                ArrayList<String> tagsList = new ArrayList<>();

                for (int i = 0; i < pickedTagsChipGroup.getChildCount(); i++) {
                    View child = pickedTagsChipGroup.getChildAt(i);
                    if (child instanceof Chip) {
                        Chip tagChild = (Chip) child;
                        String text = tagChild.getText().toString();
                        tagsList.add(text);

                    }
                }


                HashMap<String,AppointmentsTimeAndPrice> appointmentsType = new HashMap<>();

                for (int i = 0; i < allAppointmentNameAndLengthLayout.getChildCount(); i++) {
                    View child = allAppointmentNameAndLengthLayout.getChildAt(i);
                    if (child instanceof LinearLayout) {
                        LinearLayout linkLayout = (LinearLayout) child;
                        // Iterate through the child views of each linkLayout
                        String nameText = null;
                        String timeText = null;
                        String priceText = null;
                        for (int j = 0; j < linkLayout.getChildCount(); j++) {
                            View innerChild = linkLayout.getChildAt(j);
                            if (innerChild instanceof EditText) {
                                EditText editText = (EditText) innerChild;
                                int editTextId = editText.getId();
                                if(editTextId == R.id.firstAppointTypeName){
                                    nameText = editText.getText().toString();
                                }else if(editTextId == R.id.firstAppointTypeTime) {
                                    timeText = editText.getText().toString();
                                }else if(editTextId == R.id.firstAppointTypePrice){
                                    priceText = editText.getText().toString();
                                } else if (editTextId % 3 == 0) {
                                    priceText = editText.getText().toString();
                                }else if (editTextId % 3 == 1) {
                                    timeText = editText.getText().toString();
                                }else if(editTextId % 3 == 2){
                                    nameText = editText.getText().toString();
                                }
                                Log.d(TAG, "appoint name: " + nameText + " time: " + timeText + " price: " + priceText);
                            }
                        }
                        if(nameText != null && priceText != null && timeText != null){
                            appointmentsType.put(nameText,new AppointmentsTimeAndPrice(Integer.parseInt(timeText),Integer.parseInt(priceText)));
                        }else{
                            Toast.makeText(v.getContext(), "נא להזין את כל שדות החובה", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                }

                Drawable emptyShopImage = ContextCompat.getDrawable(v.getContext(), R.drawable.shop_empty_photo);

                Query query = FirebaseDatabase.getInstance().getReference("shops").orderByChild("shopAddress").equalTo(saveShopAddress.toString());

                if (saveShopName.isEmpty() || saveShopDes.isEmpty() || saveShopAddress.getCity().isEmpty()|| saveShopAddress.getStreet().isEmpty()||
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
    }
    public void updateAppointTypeAndTime(String appointNameText, String appointTimeText, String appointPriceText ){
        LinearLayout appointmentNameAndLengthLayout = new LinearLayout(this);
        appointmentNameAndLengthLayout.setLayoutParams(layoutParams);
        layoutParams.weight = 1;

        LinearLayout.LayoutParams appointLayoutParams =  new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT  // Height, adjust as needed
        );
        TextView ils = new TextView(this);
        ils.setText("ש\"ח");
        ils.setLayoutParams(appointLayoutParams);

        EditText appointmentPrice = new EditText(this);
        appointmentPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        appointmentPrice.setLayoutParams(appointLayoutParams);
        appointmentPrice.setId(appointsNum++);
        Log.d(TAG, "appointsNum price : " + appointsNum);


        TextView minutes = new TextView(this);
        minutes.setText("דק");
        minutes.setLayoutParams(appointLayoutParams);

        EditText appointmentTime = new EditText(this);
        appointmentTime.setInputType(InputType.TYPE_CLASS_NUMBER);
        appointmentTime.setLayoutParams(appointLayoutParams);
        appointmentTime.setId(appointsNum++);
        Log.d(TAG, "appointsNum time : " + appointsNum);


        TextView hyphen = new TextView(this);
        hyphen.setText("  -  ");
        hyphen.setLayoutParams(appointLayoutParams);

        EditText appointmentName = new EditText(this);
        appointmentName.setInputType(InputType.TYPE_CLASS_TEXT);
        appointmentName.setLayoutParams(appointLayoutParams);
        appointmentName.setId(appointsNum++);
        Log.d(TAG, "appointsNum name : " + appointsNum);

        if(appointTimeText != null){
            appointmentPrice.setText(appointPriceText);
            appointmentTime.setText(appointTimeText);
            appointmentName.setText(appointNameText);
        }

        Button deleteAppointmentNameAndType = new Button(this);
        deleteAppointmentNameAndType.setText("הסר תור");
        deleteAppointmentNameAndType.setLayoutParams(appointLayoutParams);

        appointmentNameAndLengthLayout.addView(ils);
        appointmentNameAndLengthLayout.addView(appointmentPrice);
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
        for(String day : days){

            boolean timeInDay = false;
            if(defaultWorkTimeEachDay.containsKey(day)){
                for(TimeRange time : defaultWorkTimeEachDay.get(day)){
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
                    TimeRange newTime = new TimeRange(newStartTime,newEndTime);
                    defaultWorkTimeEachDay.get(day).add(newTime);
                }
            }else{
                TimeRange timeArray = new TimeRange(newStartTime, newEndTime);
                ArrayList<TimeRange> allTimeArray = new ArrayList<>();
                allTimeArray.add(timeArray);

                defaultWorkTimeEachDay.put(day,allTimeArray);
            }
        }
        updateDefaultDaysHash();
    }




    private void updateDefaultDaysHash() {
        for (Map.Entry<String, List<TimeRange>> entry : defaultWorkTimeEachDay.entrySet()) {
            String day = entry.getKey();
            List<TimeRange> timeRanges = entry.getValue();

            // Sort timeRanges based on start time
            timeRanges.sort(Comparator.comparingInt(TimeRange::getStartTime));

            updateDaysTable(day, timeRanges);

            // Print sorted time ranges
            for (TimeRange time : timeRanges) {
                int startTime = time.getStartTime();
                int endTime = time.getEndTime();
                System.out.println("Day: " + day + ", Start Time: " + startTime + ", End Time: " + endTime);
            }
        }
    }

    public void updateDaysTable(String day, List<TimeRange> timeArray){
        TableLayout updateTable = new TableLayout(UpdateShopActivity.this);
        for (TimeRange time : timeArray) {
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
    private void addOwnedShop(String shopName, Address shopAddress, byte[] imageData,
                              String shopDes, ArrayList<String> links, ArrayList<String> tags,
                              HashMap<String,AppointmentsTimeAndPrice> appointmentType, HashMap<String, List<TimeRange>> defaultWorkTimeEachDay, String shopUid){

        final String finalShopUid;
        // Generate a unique key for the new shop
        if(shopUid == null){
            finalShopUid = myRef.push().getKey();
        }else{
            finalShopUid = shopUid;
        }



        DatabaseReference newShopRef = myRef.child(finalShopUid);

        // Upload shop image
        imageRef = storageRef.child("shops/images/" + finalShopUid + ".jpg");
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
                            i.putExtra("userUid", userUid);
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
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "Error setValue: " + e.getMessage());
                }

            });
        });
    }


}