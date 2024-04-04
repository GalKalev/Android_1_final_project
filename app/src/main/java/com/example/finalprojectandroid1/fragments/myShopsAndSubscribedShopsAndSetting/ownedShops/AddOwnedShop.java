//package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.Navigation;
//
//import android.text.InputType;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.finalprojectandroid1.R;
//import com.example.finalprojectandroid1.activities.MainActivity;
//import com.example.finalprojectandroid1.shop.WeekdayWorkTime;
//import com.google.android.material.chip.Chip;
//import com.google.android.material.chip.ChipGroup;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link AddOwnedShop#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class AddOwnedShop extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public AddOwnedShop() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment AddOwnedShop.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static AddOwnedShop newInstance(String param1, String param2) {
//        AddOwnedShop fragment = new AddOwnedShop();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//        pickImageLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == getActivity().RESULT_OK) {
//                        Intent data = result.getData();
//                        if (data != null && data.getData() != null) {
//                            Uri imageUri = data.getData();
//
//                            // Load the image into a Bitmap
//                            Bitmap bitmap = loadBitmapFromUri(imageUri);
//
//                            // Set your maximum allowed dimensions
//                            int maxWidth = 400;
//                            int maxHeight = 400;
//
//                            // Resize the bitmap
//                            scaledImageBitmap = scaleBitmap(bitmap, maxWidth, maxHeight);
//
//                            // Set the scaled bitmap to the ImageView
//                            shopImage.setImageBitmap(scaledImageBitmap);
//                            shopImage.setVisibility(View.VISIBLE);
//
//                        }
//                    } else {
//                        Toast.makeText(getContext(), "לא ניתן לגשת לגלריה.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    String TAG = "AddOwnedShop";
//
//    ImageView shopImage;
//    Bitmap scaledImageBitmap;
//
//    TableLayout sunRowLayout;
//    TableLayout monRowLayout;
//    TableLayout tueRowLayout;
//    TableLayout wedRowLayout;
//    TableLayout thurRowLayout;
//    TableLayout friRowLayout;
//    TableLayout satRowLayout;
//
//    static int linkEditTextCount = 0;
//    //i have a ShopData class put the added info there u dingus
//
////    HashMap<String, List<Integer[]>> defaultWorkTimeEachDay = new HashMap<>();
//    HashMap<String, List<WeekdayWorkTime>> defaultWorkTimeEachDay = new HashMap<>();
//    private ActivityResultLauncher<Intent> pickImageLauncher;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_add_owned_shop, container, false);
//
//        Log.d(TAG, "inside");
//
//        MainActivity mainActivity = (MainActivity) getActivity();
//
//        EditText shopName = view.findViewById(R.id.addShopName);
//
//        EditText shopAddress = view.findViewById(R.id.addShopAddress);
//
//        shopImage = view.findViewById(R.id.shopImage);
//
//        Button addShopImageButton = view.findViewById(R.id.addShopImageButton);
//
//        EditText shopDes = view.findViewById(R.id.addShopDes);
//
//        Button addShopLinkButton = view.findViewById(R.id.addShopLinkButton);
//        LinearLayout linksLayout = view.findViewById(R.id.linksLayout);
//
//        Spinner addTagsSpinner = view.findViewById(R.id.addShopTagsSpinner);
//        ChipGroup pickedTagsChipGroup = view.findViewById(R.id.pickedTagsChipGroup);
//        String[] spinnerTagsList = {"בחר תגית", "איפור", "מספרה", "הסרת שיער בלייזר", "חייט", "שיעורים פרטיים", "עיצוב ציפרניים"};
//        ArrayAdapter<String> spinnerTagsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerTagsList);
//
////        EditText appointmentTime = view.findViewById(R.id.appointmentTime);
//////        EditText appointmentType = view.findViewById()
//        LinearLayout allAppointmentNameAndLengthLayout = view.findViewById(R.id.allAppointmentNameAndLengthLayout);
//        Button addAppointmentNameAndLengthButton = view.findViewById(R.id.addAppointmentNameAndLengthButton);
//
//        Button addWorkingTimeButton = view.findViewById(R.id.addWorkingTimeRangeButton);
//
//        sunRowLayout = view.findViewById(R.id.sunLayout);
//        monRowLayout = view.findViewById(R.id.monLayout);
//        tueRowLayout = view.findViewById(R.id.tueLayout);
//        monRowLayout = view.findViewById(R.id.monLayout);
//        wedRowLayout = view.findViewById(R.id.wedLayout);
//        thurRowLayout = view.findViewById(R.id.thurLayout);
//        friRowLayout = view.findViewById(R.id.friLayout);
//        satRowLayout = view.findViewById(R.id.satLayout);
//
//        Button addShopButton = view.findViewById(R.id.addTheShopButton);
//
//
//
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, // Width
//                LinearLayout.LayoutParams.WRAP_CONTENT  // Height, adjust as needed
//        );
//        addShopImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent();
//                i.setType("image/*");
//                i.setAction(Intent.ACTION_GET_CONTENT);
//
//                pickImageLauncher.launch(Intent.createChooser(i, "Select Picture"));
//
//            }
//        });
//
//
//        addShopLinkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LinearLayout eachLinkLayout = new LinearLayout(getContext());
//                EditText newLink = new EditText(getContext());
//                Button deleteLinkButton = new Button(getContext());
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
//
//                // Validate and check domain of the link
//
//
//
//            }
//        });
//
//        spinnerTagsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        addTagsSpinner.setAdapter(spinnerTagsAdapter);
//
//        addTagsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedTag = (String) parent.getItemAtPosition(position);
//                if (!selectedTag.equals("בחר תגית")) {
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
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Handle case where nothing is selected
//            }
//        });
//
//
//        addWorkingTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SetWeekdayWorkingTimeDialog(v.getContext(), AddOwnedShop.this);
////                Log.d(TAG,daysSelected + " , " + timeSelected );
//            }
//        });
//
//        addAppointmentNameAndLengthButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LinearLayout appointmentNameAndLengthLayout = new LinearLayout(getContext());
//                appointmentNameAndLengthLayout.setLayoutParams(layoutParams);
//                layoutParams.weight = 1;
//
//                TextView minutes = new TextView(getContext());
//                minutes.setText("דק");
//
//                EditText appointmentTime = new EditText(getContext());
//                appointmentTime.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//                TextView hyphen = new TextView(getContext());
//                hyphen.setText("  -  ");
//
//                EditText appointmentName = new EditText(getContext());
//                appointmentName.setInputType(InputType.TYPE_CLASS_TEXT);
//
//                Button deleteAppointmentNameAndType = new Button(getContext());
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
//            }
//        });
//
//        addShopButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String saveShopName = shopName.getText().toString();
//                Log.d(TAG, "shop name: " + saveShopName);
//
//                String saveShopAddress = shopAddress.getText().toString();
//                Log.d(TAG, "shop address: " + saveShopAddress);
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                scaledImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                byte[] imageData = baos.toByteArray();
//
//                String saveShopDes = shopDes.getText().toString();
//                Log.d(TAG, "shop des: " + saveShopDes);
//
//                ArrayList<String> linksArray = new ArrayList<>();
//
//                for (int i = 0; i < linksLayout.getChildCount(); i++) {
//                    View child = linksLayout.getChildAt(i);
//                    if (child instanceof LinearLayout) {
//                        LinearLayout linkLayout = (LinearLayout) child;
//                        // Iterate through the child views of each linkLayout
//                        for (int j = 0; j < linkLayout.getChildCount(); j++) {
//                            View innerChild = linkLayout.getChildAt(j);
//                            if (innerChild instanceof EditText) {
//                                EditText linkEditText = (EditText) innerChild;
//                                // Get the text from the EditText and do whatever you need with it
//                                String linkText = linkEditText.getText().toString();
//                                if (!linkText.isEmpty()) {
//
//                                    if (isValidURL(linkText)) {
////
//                                        isLinkReachable(linkText);
//                                        linksArray.add(linkText);
//                                    } else {
//                                        // Invalid URL
//                                        Log.d(TAG, "Invalid URL");
//                                    }
//                                }
//
//                            }
//                        }
//                    }
//                }
//
//                for(String link : linksArray){
//                    Log.d(TAG,"link text: " + link);
//                }
//
//                ArrayList<String> tagsList = new ArrayList<>();
//
//                for (int i = 0; i < pickedTagsChipGroup.getChildCount(); i++) {
//                    View child = pickedTagsChipGroup.getChildAt(i);
//                    if (child instanceof Chip) {
//                        Chip tagChild = (Chip) child;
//                        String text = tagChild.getText().toString();
//                        tagsList.add(text);
//
//                    }
//                }
//
//                //add the work time
//
//                Log.d(TAG, "tags: " + tagsList);
//
//                HashMap<String,Integer> appointmentsType = new HashMap<>();
//
//                for (int i = 0; i < allAppointmentNameAndLengthLayout.getChildCount(); i++) {
//                    View child = allAppointmentNameAndLengthLayout.getChildAt(i);
//                    if (child instanceof LinearLayout) {
//                        LinearLayout linkLayout = (LinearLayout) child;
//                        // Iterate through the child views of each linkLayout
//                        String nameText = null;
//                        String timeText = null;
//                        for (int j = 0; j < linkLayout.getChildCount(); j++) {
//                            View innerChild = linkLayout.getChildAt(j);
//                            if (innerChild instanceof EditText) {
//                                if (((EditText) innerChild).getInputType() == InputType.TYPE_CLASS_TEXT) {
//                                    EditText nameEditText = (EditText) innerChild;
//                                    nameText = nameEditText.getText().toString();
////                                    Log.d(TAG, "appo name " + (i + 1) + ": " + nameText);
//                                }
//                                if (((EditText) innerChild).getInputType() == InputType.TYPE_CLASS_NUMBER) {
//                                    EditText nameEditText = (EditText) innerChild;
//                                    timeText = nameEditText.getText().toString();
////                                    Log.d(TAG, "appo time " + (i + 1) + ": " + timeText);
//                                }
//                            }
//                        }
//                        if(!timeText.isEmpty()){
//                            appointmentsType.put(nameText,Integer.valueOf(timeText));
//                        }
//
//                    }
//                }
//
//                for(String name : appointmentsType.keySet()){
//                    Log.d(TAG, "app name: " + name + ", app time: " + appointmentsType.get(name));
//                }
//
//                Drawable emptyShopImage = ContextCompat.getDrawable(requireContext(), R.drawable.shop_empty_photo);
//
//
//                if (saveShopName.isEmpty() || saveShopDes.isEmpty() || saveShopAddress.isEmpty()||
//                        shopImage.getDrawable().getConstantState().equals(emptyShopImage.getConstantState()) ||
//                        tagsList.isEmpty() || appointmentsType.isEmpty() || defaultWorkTimeEachDay.isEmpty()) {
//                    Toast.makeText(getContext(), "נא להזין את כל שדות החובה", Toast.LENGTH_SHORT).show();
//                }else{
//                    mainActivity.addOwnedShop(saveShopName,saveShopAddress, imageData,
//                            saveShopDes, linksArray,tagsList,appointmentsType,defaultWorkTimeEachDay);
//                }
//                try{
//                    Navigation.findNavController(view).navigate(R.id.action_addOwnedShop_to_myOwnedShops2);
//
//                }catch(Exception e){
//                    Log.d(TAG, "Error navigate: " + e.getMessage());
//                }
//
//            }
//        });
//
//
//
//
//        return view;
//    }
//
//    private Bitmap loadBitmapFromUri(Uri uri) {
//        try {
//            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
//            if (inputStream != null) {
//                return BitmapFactory.decodeStream(inputStream);
//            } else {
//                Log.e(TAG, "Input stream is null");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(TAG, "Error loading bitmap from URI: " + e.getMessage());
//        }
//        return null;
//    }
//    private Bitmap scaleBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//
//        float scale = Math.min((float) maxWidth / width, (float) maxHeight / height);
//
//        Matrix matrix = new Matrix();
//        matrix.postRotate(90);
//        matrix.postScale(scale, scale);
//
//        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
//    }
//
//    private void isLinkReachable(String urlString) {
//        AsyncTask.execute(() -> {
//            try {
//                URL url = new URL(urlString);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("HEAD");
//                int responseCode = connection.getResponseCode();
//                boolean isReachable = (responseCode == HttpURLConnection.HTTP_OK);
//                // You can do something with the result here, such as displaying a toast
//                // or updating UI elements.
//                Log.d(TAG, "Link " + urlString + " is reachable: " + isReachable);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                // Handle invalid URL error
//                Log.e(TAG, "Invalid URL: " + urlString);
//            } catch (IOException e) {
//                e.printStackTrace();
//                // Handle other IO-related errors (e.g., network issues)
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(), "Error connecting to URL: " + urlString, Toast.LENGTH_SHORT).show();
//                    }
//                });
//                Log.e(TAG, "Error connecting to URL: " + urlString);
//            } catch (Exception e) {
//                e.printStackTrace();
//                // Handle other unexpected errors
//                Log.e(TAG, "Unexpected error: " + e.getMessage());
//            }
//        });
//    }
//
//    private String extractDomain(String url) {
//        try {
//            URI uri = new URI(url);
//
//            String domain = uri.getHost();
//            if (domain != null) {
//                return domain.startsWith("www.") ? domain.substring(4) : domain;
//            }
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//    private boolean isValidURL(String url) {
//        // Implement URL validation logic here
//        try {
//            new URL(url).toURI();
//            return true;
//        } catch (MalformedURLException | URISyntaxException e) {
//            return false;
//        }
//    }
//
//    /// ____________________________move to main activity???______________________________________//
//    // Method to detect social media platform from a domain
//    private String detectSocialMedia(String domain) {
//        // Implement domain comparison with known social media domains
//        // You can use the approach described in the previous response
//        return domain;
//    }
//
//    // _____________________________________________________________________//
//
//
//    public void updateWorkTime(ArrayList<String> days,int startHour, int startMinutes, int endHour, int endMinutes){
//        int newStartTime;
//        int newEndTime;
//        if(startMinutes < 10){
//            newStartTime = Integer.valueOf(startHour + "0" + startMinutes);
//        }else{
//            newStartTime = Integer.valueOf(startHour + "" + startMinutes);
//        }
//        if(endMinutes < 10){
//            newEndTime = Integer.valueOf(endHour + "0" + endMinutes);
//        }else{
//            newEndTime = Integer.valueOf(endHour + "" + endMinutes);
//        }
//        Log.d(TAG, "newStartTime: " + newStartTime + ", newEndTime: " + newEndTime);
//        for(String day : days){
//
//            boolean timeInDay = false;
//            if(defaultWorkTimeEachDay.containsKey(day)){
//                for(WeekdayWorkTime time : defaultWorkTimeEachDay.get(day)){
//                    int startInTime = time.getStartTime();
//                    int endInTime = time.getEndTime();
//                    if ((startInTime <= newStartTime && newStartTime < endInTime) ||
//                            (startInTime < newEndTime && newEndTime <= endInTime) ||
//                            (newStartTime <= startInTime && endInTime <= newEndTime)) {
//                        timeInDay = true;
//                        Toast.makeText(getContext(), "הזמן שהוזן חופף עם זמן קיים.", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                }
//                if (!timeInDay) {
//                    WeekdayWorkTime newTime = new WeekdayWorkTime(newStartTime,newEndTime);
//                    defaultWorkTimeEachDay.get(day).add(newTime);
//                }
//            }else{
//                WeekdayWorkTime timeArray = new WeekdayWorkTime(newStartTime, newEndTime);
//                ArrayList<WeekdayWorkTime> allTimeArray = new ArrayList<>();
//                allTimeArray.add(timeArray);
//
//                defaultWorkTimeEachDay.put(day,allTimeArray);
//            }
//        }
//        updateDefaultDaysHash();
//    }
//
//
//
//
//    private void updateDefaultDaysHash() {
//        for (Map.Entry<String, List<WeekdayWorkTime>> entry : defaultWorkTimeEachDay.entrySet()) {
//            String day = entry.getKey();
//            List<WeekdayWorkTime> timeRanges = entry.getValue();
//
//            // Sort timeRanges based on start time
//            timeRanges.sort(Comparator.comparingInt(WeekdayWorkTime::getStartTime));
//
//            updateDaysTable(day, timeRanges);
//
//            // Print sorted time ranges
//            for (WeekdayWorkTime time : timeRanges) {
//                int startTime = time.getStartTime();
//                int endTime = time.getEndTime();
//                System.out.println("Day: " + day + ", Start Time: " + startTime + ", End Time: " + endTime);
//            }
//        }
//    }
//
//    private void updateDaysTable(String day, List<WeekdayWorkTime> timeArray){
//        TableLayout updateTable = new TableLayout(getContext());
//        for (WeekdayWorkTime time : timeArray) {
//            TableRow newWorkTimeRow = new TableRow(getContext());
//            TextView showTime = new TextView(getContext());
//
//            Button deleteNewTime = new Button(getContext());
//
//            String startTimeStr = String.valueOf(time.getStartTime());
//            String endTimeStr = String.valueOf(time.getEndTime());
//
//            if (startTimeStr.length() < 4) {
//                startTimeStr = "0" + startTimeStr;
//            }
//            if (endTimeStr.length() < 4) {
//                endTimeStr = "0" + endTimeStr;
//            }
//            String formattedStartTimeStr = startTimeStr.substring(0, 2) + ":" + startTimeStr.substring(2);
//            String formattedEndTimeStr = endTimeStr.substring(0, 2) + ":" + endTimeStr.substring(2);
//
//            showTime.setText(formattedStartTimeStr + " - " + formattedEndTimeStr);
//
//            deleteNewTime.setText("מחק שעה");
//            deleteNewTime.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    newWorkTimeRow.removeView(showTime);
//                    newWorkTimeRow.removeView(deleteNewTime);
//                    defaultWorkTimeEachDay.get(day).remove(time);
//                    updateDefaultDaysHash();
//                }
//            });
//            newWorkTimeRow.addView(deleteNewTime);
//            newWorkTimeRow.addView(showTime);
//            updateTable.addView(newWorkTimeRow);
//        }
//
//        switch (day) {
//            case "א":
//                sunRowLayout.removeAllViews(); // Clear existing views
//                sunRowLayout.addView(updateTable); // Add the new table
//                break;
//            case "ב":
//                monRowLayout.removeAllViews(); // Clear existing views
//                monRowLayout.addView(updateTable); // Add the new table
//                break;
//            case "ג":
//                tueRowLayout.removeAllViews(); // Clear existing views
//                tueRowLayout.addView(updateTable); // Add the new table
//                break;
//            case "ד":
//                wedRowLayout.removeAllViews(); // Clear existing views
//                wedRowLayout.addView(updateTable); // Add the new table
//                break;
//            case "ה":
//                thurRowLayout.removeAllViews(); // Clear existing views
//                thurRowLayout.addView(updateTable); // Add the new table
//                break;
//            case "ו":
//                friRowLayout.removeAllViews(); // Clear existing views
//                friRowLayout.addView(updateTable); // Add the new table
//                break;
//            case "ש":
//                satRowLayout.removeAllViews(); // Clear existing views
//                satRowLayout.addView(updateTable); // Add the new table
//                break;
//            default:
//        }
//
//    }
//
//
//}