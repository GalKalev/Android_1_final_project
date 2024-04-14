package com.example.finalprojectandroid1.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
//import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.AddOwnedShop;
import com.example.finalprojectandroid1.TagsChipsAdapter;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.SetWeekdayWorkingTimeDialog;
import com.example.finalprojectandroid1.shop.AppointmentsTimeAndPrice;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.example.finalprojectandroid1.shop.shopFragments.Address;
import com.example.finalprojectandroid1.user.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

//import com.google.android.material.chip.Chip;
//import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateShopActivity extends AppCompatActivity {

    // In this activity, the user creates a their own shop.
    // and an owner can update the information of an already
    // existing shop

    String TAG = "uUpdateShopActivity";

    ImageView shopImage;
    Bitmap scaledImageBitmap;

    TableLayout sunRowLayout;
    TextView timePlaceholderSun;
    TableLayout monRowLayout;
    TextView timePlaceholderMon;
    TableLayout tueRowLayout;
    TextView timePlaceholderTue;
    TableLayout wedRowLayout;
    TextView timePlaceholderWed;
    TableLayout thurRowLayout;
    TextView timePlaceholderThur;
    TableLayout friRowLayout;
    TextView timePlaceholderFri;
    TableLayout satRowLayout;
    TextView timePlaceholderSat;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference myRef = database.getReference("shops");
    StorageReference imageRef ;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout linksLayout;
    Button addShopLinkButton;
    LinearLayout pickedTagsLayout;
    LinearLayout allAppointmentNameAndLengthLayout;
    boolean toUpdate = false;
    int maxWidth = 400;
    int maxHeight = 400;
    int appointsNum = 0;

    HashMap<String, List<TimeRange>> defaultWorkTimeEachDay = new HashMap<>();
    private ActivityResultLauncher<Intent> pickImageLauncher;
    String userUid;
    UserInfo user;
    ShopModel shop;

    RecyclerView tagsRes;
    TagsChipsAdapter tagsChipsAdapter;

    boolean imageChanged = false;
    ProgressBar progressBar;
    String addressCity;
    String addressStreet;
    int addressHouseNum;
    int addressFloor;
    ArrayList<String> tagsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shop);

        Bundle getValues = getIntent().getExtras();
        userUid = getValues.getString("userUid");
        user = getValues.getParcelable("user");
        shop = getValues.getParcelable("shop");

        progressBar = findViewById(R.id.progressBarShopActivity);


        // Getting shop image from a device's gallery
        pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri imageUri = data.getData();

                        // Load the image into a Bitmap
                        Bitmap bitmap = loadBitmapFromUri(imageUri);

                        // Resize the bitmap
                        scaledImageBitmap = scaleBitmap(this,bitmap, maxWidth, maxHeight,imageUri);

                        // Set the scaled bitmap to the ImageView
                        shopImage.setImageBitmap(scaledImageBitmap);
                        shopImage.setVisibility(View.VISIBLE);
                        imageChanged = true;

                    }
                } else {
                    Toast.makeText(this, "לא ניתן לגשת לגלריה.", Toast.LENGTH_SHORT).show();
                }
        });


        EditText shopName = findViewById(R.id.addShopName);

        EditText shopAddressHouseNum = findViewById(R.id.addShopAddressHouseNum);
        EditText shopAddressFloor = findViewById(R.id.addShopAddressFloor);
        EditText shopAddressStreet = findViewById(R.id.addShopAddressStreet);

        Spinner citiesSpinner = findViewById(R.id.citiesSpinner);
        String[] citiesList = GlobalMembers.citiesList;
        ArrayAdapter<String> citiesSpinnerAdapter = new ArrayAdapter<>(this,R.layout.spinner_text, citiesList);
        citiesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citiesSpinner.setAdapter(citiesSpinnerAdapter);

        shopImage = findViewById(R.id.shopImage);

        Button addShopImageButton = findViewById(R.id.addShopImageButton);

        EditText shopDes = findViewById(R.id.addShopDes);

        addShopLinkButton = findViewById(R.id.addShopLinkButton);
        linksLayout = findViewById(R.id.linksLayout);

        Spinner addTagsSpinner = findViewById(R.id.addShopTagsSpinner);

        tagsRes = findViewById(R.id.showTagsRes);
        tagsList = new ArrayList<>();
        tagsChipsAdapter = new TagsChipsAdapter(tagsList);
        tagsRes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tagsRes.setAdapter(tagsChipsAdapter);

        String[] spinnerTagsList = {"בחר תגית", "איפור", "מספרה", "הסרת שיער בלייזר", "מספרת כלבים", "שיעורים פרטיים", "אימוני כושר אישיים" ,"עיצוב ציפורניים"};
        ArrayAdapter<String> spinnerTagsAdapter = new ArrayAdapter<>(this, R.layout.spinner_text, spinnerTagsList);
        spinnerTagsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addTagsSpinner.setAdapter(spinnerTagsAdapter);

        allAppointmentNameAndLengthLayout = findViewById(R.id.allAppointmentNameAndLengthLayout);
        Button addAppointmentNameAndLengthButton = findViewById(R.id.addAppointmentNameAndLengthButton);

        Button addWorkingTimeButton = findViewById(R.id.addWorkingTimeRangeButton);

        sunRowLayout = findViewById(R.id.sunLayout);
        timePlaceholderSun = findViewById(R.id.timePlaceholderForSun);
        monRowLayout = findViewById(R.id.monLayout);
        timePlaceholderMon = findViewById(R.id.timePlaceholderForMon);
        tueRowLayout = findViewById(R.id.tueLayout);
        timePlaceholderTue = findViewById(R.id.timePlaceholderForTue);
        wedRowLayout = findViewById(R.id.wedLayout);
        timePlaceholderWed = findViewById(R.id.timePlaceholderForWed);
        thurRowLayout = findViewById(R.id.thurLayout);
        timePlaceholderThur = findViewById(R.id.timePlaceholderForThur);
        friRowLayout = findViewById(R.id.friLayout);
        timePlaceholderFri = findViewById(R.id.timePlaceholderForFri);
        satRowLayout = findViewById(R.id.satLayout);
        timePlaceholderSat = findViewById(R.id.timePlaceholderForSat);

        EditText firstAppointTypeText = findViewById(R.id.firstAppointTypeName);
        EditText firstAppointTypeTime = findViewById(R.id.firstAppointTypeTime);
        EditText firstAppointTypePrice = findViewById(R.id.firstAppointTypePrice);

        Button addShopButton = findViewById(R.id.addTheShopButton);


        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT  // Height, adjust as needed
        );

        // Checking if shop already exists, which means the user wants to
        // update shop's information and setting the text fields accordingly
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


            String imageUrl = shop.getShopImage().toString();
            Glide.with(this).asBitmap().load(imageUrl)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            scaledImageBitmap = scaleBitmapForUpdate(resource, maxWidth,maxHeight);
                            shopImage.setImageBitmap(scaledImageBitmap);
                            shopImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

            if(shop.getShopLinks() != null){
                for(String link : shop.getShopLinks()){
                    updateLinks(link);
                }
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


        }

        // Choosing shop's city
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

        // Choosing shop's image
        addShopImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                pickImageLauncher.launch(Intent.createChooser(i, "Select Picture"));

            }
        });


        // Adding links
        addShopLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLinks(null);
            }
        });

        // Shop's tags
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

            }
        });


        // Default week shop's activity time
        addWorkingTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetWeekdayWorkingTimeDialog(UpdateShopActivity.this);
            }
        });

        // Adding the appointments types
        addAppointmentNameAndLengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAppointTypeAndTime(null,null, null);
            }
        });

        // Adding the shop to the database
        addShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String saveShopName = shopName.getText().toString().trim();

                addressStreet = shopAddressStreet.getText().toString().trim();

                if(!shopAddressFloor.getText().toString().isEmpty()){
                    addressFloor = Integer.parseInt(shopAddressFloor.getText().toString().trim());
                }else{
                    Toast.makeText(v.getContext(), "נא להזין את כל שדות החובה", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!shopAddressHouseNum.getText().toString().isEmpty()){
                    addressHouseNum = Integer.parseInt(shopAddressHouseNum.getText().toString().trim());
                }else{
                    Toast.makeText(v.getContext(), "נא להזין את כל שדות החובה", Toast.LENGTH_SHORT).show();
                    return;
                }

                Address saveShopAddress = new Address(addressStreet,addressHouseNum,addressFloor,addressCity);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try{

                    scaledImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                }catch(Exception e){
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(UpdateShopActivity.this, "נא להזין את כל שדות החובה", Toast.LENGTH_SHORT).show();
                    return;
                }
                byte[] imageData = baos.toByteArray();

                String saveShopDes = shopDes.getText().toString().trim();

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

                                String linkText = linkEditText.getText().toString().trim();
                                if (!linkText.isEmpty()) {

                                    if (isValidURL(linkText)) {
                                        linksArray.add(linkText);
                                    } else {
                                        Toast.makeText(UpdateShopActivity.this, "שגיאה בהזהת לינק", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                            }
                        }
                    }
                }

                HashMap<String,AppointmentsTimeAndPrice> appointmentsType = new HashMap<>();

                for (int i = 0; i < allAppointmentNameAndLengthLayout.getChildCount(); i++) {
                    View child = allAppointmentNameAndLengthLayout.getChildAt(i);
                    if (child instanceof LinearLayout) {
                        LinearLayout linkLayout = (LinearLayout) child;
                        String nameText = null;
                        String timeText = null;
                        String priceText = null;
                        for (int j = 0; j < linkLayout.getChildCount(); j++) {
                            View innerChild = linkLayout.getChildAt(j);
                            if (innerChild instanceof EditText) {
                                EditText editText = (EditText) innerChild;
                                int editTextId = editText.getId();
                                if(editTextId == R.id.firstAppointTypeName){
                                    nameText = editText.getText().toString().trim();
                                }else if(editTextId == R.id.firstAppointTypeTime) {
                                    timeText = editText.getText().toString();
                                }else if(editTextId == R.id.firstAppointTypePrice){
                                    priceText = editText.getText().toString().trim();
                                } else if (editTextId % 3 == 0) {
                                    priceText = editText.getText().toString().trim();
                                }else if (editTextId % 3 == 1) {
                                    timeText = editText.getText().toString().trim();
                                }else if(editTextId % 3 == 2){
                                    nameText = editText.getText().toString().trim();
                                }
                            }
                        }
                        if(!nameText.isEmpty() && !priceText.isEmpty() && !timeText.isEmpty() && Integer.parseInt(priceText) != 0 && Integer.parseInt(timeText) != 0 && Integer.parseInt(timeText) % 5 == 0){
                            appointmentsType.put(nameText,new AppointmentsTimeAndPrice(Integer.parseInt(timeText),Integer.parseInt(priceText)));
//                            appointTypeNameList.add(nameText);
                        }else{
                            Toast.makeText(v.getContext(), "נא להזין את סוגי התורים לפי ההוראות", Toast.LENGTH_SHORT).show();
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
                                // Creating or updating a shop
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
                            Toast.makeText(UpdateShopActivity.this, "הכתובת שהוזנה כבר קיימת במערכת", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void updateTags(String selectedTag) {
        if (!tagsList.contains(selectedTag)) {
            tagsList.add(selectedTag);
            tagsChipsAdapter.notifyDataSetChanged();
           }
    }

    // Creating links
    private void updateLinks( String link){
        LinearLayout eachLinkLayout = new LinearLayout(this);
        EditText newLink = new EditText(new ContextThemeWrapper(this,R.style.editText));
        newLink.setBackgroundResource(R.drawable.update_activity_input_text);
        newLink.setTextColor(Color.BLACK);

        ImageButton deleteLinkButton = new ImageButton(this);
        deleteLinkButton.setBackgroundColor(Color.TRANSPARENT);
        deleteLinkButton.setImageResource(R.drawable.round_cancel_24);

        layoutParams.weight = 1;

        eachLinkLayout.setLayoutParams(layoutParams);

        newLink.setHint("לינק");
        if(toUpdate){
            newLink.setText(link);
        }
        newLink.setLayoutParams(layoutParams);


        deleteLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linksLayout.removeView(eachLinkLayout);
                addShopLinkButton.setEnabled(true);

            }
        });
        eachLinkLayout.addView(deleteLinkButton);
        eachLinkLayout.addView(newLink);
        linksLayout.addView(eachLinkLayout);

    }

    // Creating appointments type with time and price
    public void updateAppointTypeAndTime(String appointNameText, String appointTimeText, String appointPriceText ){
        LinearLayout appointmentNameAndLengthLayout = new LinearLayout(this);
        appointmentNameAndLengthLayout.setLayoutParams(layoutParams);
        appointmentNameAndLengthLayout.setGravity(Gravity.END);
        layoutParams.weight = 1;

        LinearLayout.LayoutParams appointLayoutParams =  new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        TextView ils = new TextView(this);
        ils.setText("ש\"ח");
        ils.setLayoutParams(appointLayoutParams);
        ils.setTextColor(Color.BLACK);

        EditText appointmentPrice = new EditText(new ContextThemeWrapper(this,R.style.editText));
        appointmentPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        appointmentPrice.setLayoutParams(appointLayoutParams);
        appointmentPrice.setId(appointsNum++);
        appointmentPrice.setBackgroundResource(R.drawable.update_activity_input_text);
        appointmentPrice.setHint("מחיר");
        appointmentPrice.setLayoutParams(layoutParams);
        appointmentPrice.setTextColor(Color.BLACK);
        appointmentPrice.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);

        TextView minutes = new TextView(this);
        minutes.setText("דק'");
        minutes.setLayoutParams(appointLayoutParams);
        minutes.setTextColor(Color.BLACK);

        EditText appointmentTime = new EditText(new ContextThemeWrapper(this,R.style.editText));
        appointmentTime.setInputType(InputType.TYPE_CLASS_NUMBER);
        appointmentTime.setLayoutParams(appointLayoutParams);
        appointmentTime.setId(appointsNum++);
        appointmentTime.setBackgroundResource(R.drawable.update_activity_input_text);
        appointmentTime.setHint("זמן");
        appointmentTime.setLayoutParams(layoutParams);
        appointmentTime.setTextColor(Color.BLACK);
        appointmentTime.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);


        TextView hyphen = new TextView(this);
        hyphen.setText("  -  ");
        hyphen.setLayoutParams(appointLayoutParams);
        hyphen.setTextColor(Color.BLACK);

        EditText appointmentName = new EditText(new ContextThemeWrapper(this,R.style.editText));
        appointmentName.setInputType(InputType.TYPE_CLASS_TEXT);
        appointmentName.setLayoutParams(appointLayoutParams);
        appointmentName.setId(appointsNum++);
        appointmentName.setBackgroundResource(R.drawable.update_activity_input_text);
        appointmentName.setHint("שם התור");
        appointmentName.setLayoutParams(layoutParams);
        appointmentName.setTextColor(Color.BLACK);

        if(appointTimeText != null){
            appointmentPrice.setText(appointPriceText);
            appointmentTime.setText(appointTimeText);
            appointmentName.setText(appointNameText);
        }

        ImageButton deleteAppointmentNameAndType = new ImageButton(this);
        deleteAppointmentNameAndType.setLayoutParams(appointLayoutParams);
        deleteAppointmentNameAndType.setImageResource(R.drawable.round_cancel_24);
        deleteAppointmentNameAndType.setBackgroundColor(Color.TRANSPARENT);

        appointmentNameAndLengthLayout.addView(deleteAppointmentNameAndType);
        appointmentNameAndLengthLayout.addView(ils);
        appointmentNameAndLengthLayout.addView(appointmentPrice);
        appointmentNameAndLengthLayout.addView(minutes);
        appointmentNameAndLengthLayout.addView(appointmentTime);
        appointmentNameAndLengthLayout.addView(hyphen);
        appointmentNameAndLengthLayout.addView(appointmentName);

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
    private Bitmap scaleBitmap(Context context, Bitmap bitmap, int maxWidth, int maxHeight, Uri uri) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scale = Math.min((float) maxWidth / width, (float) maxHeight / height);

        int rotation = getRotationFromExif(context,uri);
        Matrix matrix = new Matrix();

        if(rotation != 0 ){
            matrix.postRotate(rotation);
        }

        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
    private Bitmap scaleBitmapForUpdate(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scale = Math.min((float) maxWidth / width, (float) maxHeight / height);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private int getRotationFromExif(Context context, Uri uri){
        int rotationDegree = 0;
        try{
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if(inputStream != null){
                ExifInterface exifInterface = new ExifInterface(inputStream);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation){
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotationDegree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotationDegree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotationDegree = 270;
                        break;
                }
                inputStream.close();

            }
        }catch (Exception e){
            Log.e(TAG, "getRotationFromExif: " + e.getMessage());
        }
        return rotationDegree;
    }

    // Cheking to see if urk is valid
    private boolean isValidURL(String url) {
        // Implement URL validation logic here
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }


    // Update the shop active time in a week table
    public void updateWorkTime(ArrayList<String> days, String startTime, String endTime){
        int newStartTime = Integer.parseInt(startTime);
        int newEndTime = Integer.parseInt(endTime);

        for(String day : days){

            boolean timeInDay = false;
            if(defaultWorkTimeEachDay.containsKey(day)){
                for(TimeRange time : defaultWorkTimeEachDay.get(day)){
                    int startInTime = Integer.parseInt(time.getStartTime());
                    int endInTime = Integer.parseInt(time.getEndTime());
                    if ((startInTime <= newStartTime && newStartTime < endInTime) ||
                            (startInTime < newEndTime && newEndTime <= endInTime) ||
                            (newStartTime <= startInTime && endInTime <= newEndTime)) {
                        timeInDay = true;
                        Toast.makeText(UpdateShopActivity.this, "הזמן שהוזן חופף עם זמן קיים.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (!timeInDay) {
                    TimeRange newTime = new TimeRange(startTime,endTime);
                    defaultWorkTimeEachDay.get(day).add(newTime);
                }
            }else{
                TimeRange timeArray = new TimeRange(startTime, endTime);
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
            timeRanges.sort(Comparator.comparingInt(tr -> Integer.parseInt(tr.getStartTime())));

            updateDaysTable(day, timeRanges);

        }
    }

    public void updateDaysTable(String day, List<TimeRange> timeArray){
        TableLayout updateTable = new TableLayout(UpdateShopActivity.this);
        for (TimeRange time : timeArray) {

            TableRow newWorkTimeRow = new TableRow(UpdateShopActivity.this);
            newWorkTimeRow.setGravity(Gravity.END);
            TextView showTime = new TextView(UpdateShopActivity.this);

            ImageButton deleteNewTime = new ImageButton(UpdateShopActivity.this);

            String startTimeStr = time.getStartTime();
            String endTimeStr = time.getEndTime();

            String formattedStartTimeStr = startTimeStr.substring(0, 2) + ":" + startTimeStr.substring(2);
            String formattedEndTimeStr = endTimeStr.substring(0, 2) + ":" + endTimeStr.substring(2);

            showTime.setText(formattedStartTimeStr + " - " + formattedEndTimeStr);
            showTime.setTextColor(Color.BLACK);


            deleteNewTime.setBackgroundColor(Color.TRANSPARENT);
            deleteNewTime.setImageResource(R.drawable.round_cancel_24);
            deleteNewTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newWorkTimeRow.removeView(showTime);
                    newWorkTimeRow.removeView(deleteNewTime);

                    defaultWorkTimeEachDay.get(day).remove(time);
                    checkDay(day,updateTable);
                    updateDefaultDaysHash();
                }
            });
            newWorkTimeRow.addView(deleteNewTime);
            newWorkTimeRow.addView(showTime);
            updateTable.addView(newWorkTimeRow);
        }

        checkDay(day, updateTable);

    }

    private void checkDay(String day, TableLayout updateTable){
        switch (day) {
            case "א":
                if(updateTable.getChildCount() == 0){
                    timePlaceholderSun.setVisibility(View.VISIBLE);
                }else{
                    timePlaceholderSun.setVisibility(View.GONE);
                    sunRowLayout.removeViews(1,sunRowLayout.getChildCount() - 1);
                    sunRowLayout.addView(updateTable); // Add the new table
                }
                break;
            case "ב":
                if(updateTable.getChildCount() == 0){
                    timePlaceholderMon.setVisibility(View.VISIBLE);
                }else{
                    timePlaceholderMon.setVisibility(View.GONE);
                    monRowLayout.removeViews(1,monRowLayout.getChildCount() - 1);
                    monRowLayout.addView(updateTable); // Add the new table
                }

                break;
            case "ג":
                if(updateTable.getChildCount() == 0){
                    timePlaceholderTue.setVisibility(View.VISIBLE);
                }else{
                    timePlaceholderTue.setVisibility(View.GONE);
                    tueRowLayout.removeViews(1,tueRowLayout.getChildCount() - 1);
                    tueRowLayout.addView(updateTable); // Add the new table
                }

                break;
            case "ד":
                if(updateTable.getChildCount() == 0){
                    timePlaceholderWed.setVisibility(View.VISIBLE);
                }else{
                    timePlaceholderWed.setVisibility(View.GONE);
                    wedRowLayout.removeViews(1,wedRowLayout.getChildCount() - 1);
                    wedRowLayout.addView(updateTable); // Add the new table
                }

                break;
            case "ה":
                if(updateTable.getChildCount() == 0){
                    timePlaceholderThur.setVisibility(View.VISIBLE);
                }else{
                    timePlaceholderThur.setVisibility(View.GONE);
                    thurRowLayout.removeViews(1,thurRowLayout.getChildCount() - 1);
                    thurRowLayout.addView(updateTable); // Add the new table
                }
                break;
            case "ו":
                if(updateTable.getChildCount() == 0){
                    timePlaceholderFri.setVisibility(View.VISIBLE);
                }else{
                    timePlaceholderFri.setVisibility(View.GONE);
                    friRowLayout.removeViews(1,friRowLayout.getChildCount() - 1);
                    friRowLayout.addView(updateTable); // Add the new table
                }

                break;
            case "ש":
                if(updateTable.getChildCount() == 0){
                    timePlaceholderSat.setVisibility(View.VISIBLE);
                }else{
                    timePlaceholderSat.setVisibility(View.GONE);
                    satRowLayout.removeViews(1,satRowLayout.getChildCount() - 1);
                    satRowLayout.addView(updateTable); // Add the new table
                }

                break;
            default:
        }

    }

    // Adding the shop to database
    private void addOwnedShop(String shopName, Address shopAddress, byte[] imageData,
                              String shopDes, ArrayList<String> links, ArrayList<String> tags,
                              HashMap<String,AppointmentsTimeAndPrice> appointmentType, HashMap<String, List<TimeRange>> defaultWorkTimeEachDay, String shopUid){

        final String finalShopUid;
        if(shopUid == null){
            finalShopUid = myRef.push().getKey();
        }else{
            finalShopUid = shopUid;
        }

        DatabaseReference newShopRef = myRef.child(finalShopUid);

        imageRef = storageRef.child("shops/images/" + finalShopUid + ".jpg");
        progressBar.setVisibility(View.VISIBLE);

        UploadTask uploadTask = imageRef.putBytes(imageData);

        uploadTask.addOnFailureListener(exception -> {
            progressBar.setVisibility(View.GONE);
            // Handle unsuccessful uploads
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

                try {
                    newShopRef.child("shopInfo").setValue(newShop).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressBar.setVisibility(View.GONE);
                            Intent i;

                            if(toUpdate){
                                i = new Intent(UpdateShopActivity.this, ShopInfoActivity.class);
                                i.putExtra("shopDefaultAvailableTime", newShop.getShopDefaultAvailableTime());
                                i.putExtra("shopSetAppointment", newShop.getShopSetAppointment());
                                i.putExtra("shop",  newShop);
                                i.putExtra("isOwned",  true);
                            }else{
                                i = new Intent(UpdateShopActivity.this, MainActivity.class);
                                i.putExtra("updateShop",1);
                            }
                            i.putExtra("userUid", userUid);
                            i.putExtra("user", user);
                            startActivity(i);
                            finish();
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
                    Toast.makeText(this, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                }

            });
        });
    }


}