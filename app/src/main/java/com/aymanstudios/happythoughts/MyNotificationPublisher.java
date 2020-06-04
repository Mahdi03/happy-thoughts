package com.aymanstudios.happythoughts;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.aymanstudios.happythoughts.MainActivity.NOTIFICATION_CHANNEL_ID;
import static com.aymanstudios.happythoughts.MainActivity.listOfHappyThoughtsFallback;
/*
The BroadcastReceiver is what is used to communicate between the two java classes

MyNotificationPublisher is called every time the AlarmManager set in MainActivity.java
goes off, so the notification is repeatedly updated under the same id, which allows
for the text to be different each time
 */
public class MyNotificationPublisher extends BroadcastReceiver {
    //Global vars
    private final static String default_notification_channel_id = "default";
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_BUILDER = "notification_builder";

    /*ArrayList<Integer> enumArr = new ArrayList<Integer>();
        for (int i = 0; i < listOfHappyThoughts.size(); i++) {
        enumArr.add(i);
    }*/
    //This class is the format that is used to upload and download the listOfHappyThoughts form the Firebase Firestore database
    class ListOfHappyThoughtsClass {
        //List (complicated Array) of sayings to choose from
        private List<String> listOfHappyThoughts;
        //Main Setter for class
        public ListOfHappyThoughtsClass(List<String> listOfHappyThoughts) {
            this.listOfHappyThoughts = listOfHappyThoughts;
        }
        //Main Getter for class
        public List<String> getListOfHappyThoughts() {
            return listOfHappyThoughts;
        }
    }
    //Actually use class to define variable if used from online
    private ListOfHappyThoughtsClass listOfHappyThoughtsClass;
    //Final List to choose sayings from (could be updated or from fallback list)
    public List<String> listOfHappyThoughts;


    public void onReceive(Context context, Intent intent) {
        listOfHappyThoughts = listOfHappyThoughtsFallback;
        final Context mainContext = context;
        //Firebase stuff for testing
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Toast.makeText(mainContext, "Good job 1", Toast.LENGTH_SHORT).show();
        db.collection("happyThoughts").document("happyThoughts").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //Toast.makeText(mainContext, "Good job 2", Toast.LENGTH_SHORT).show();

                                //listOfHappyThoughtsClass = new ListOfHappyThoughtsClass((List<String>) document.getData().get("listOfHappyThoughts"));
                                //Toast.makeText(context, "Good job", Toast.LENGTH_LONG).show();
                                //List<String> getData = (List<String>) document.getData().get("listOfHappyThoughts");
                                //listOfHappyThoughts = listOfHappyThoughtsClass.getListOfHappyThoughts();

                                listOfHappyThoughts = (List<String>) document.getData().get("listOfHappyThoughts");

                                /*listOfHappyThoughts = new ArrayList<String>();
                                listOfHappyThoughts.add(0, "1");
                                listOfHappyThoughts.add(1, "2");*/

                                //Toast.makeText(mainContext, "Good job 3", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            //listOfHappyThoughts = listOfHappyThoughtsFallback;
                            Toast.makeText(mainContext, task.getException().toString() + " Using a fallback list instead", Toast.LENGTH_SHORT).show();
                        }
                        NotificationManager notificationManager = (NotificationManager) mainContext.getSystemService(Context.NOTIFICATION_SERVICE);
                        //Builds new Notification object
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(mainContext, default_notification_channel_id);
                        //Give it a title
                        builder.setContentTitle("Happy Thought Of The Day:");
                        //Set its short text that is immediately available
                        builder.setContentText("...");
                        builder.setSmallIcon(R.mipmap.app_logo_foreground);
                        //Set its long text (lines wrap and all text is visible)
                        //Toast.makeText(context, "before set main notification text", Toast.LENGTH_SHORT).show(); //testing
                        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(listOfHappyThoughts.get(getRandNum())));

                        //builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Hello\nBye\nMaybe Not"));

                        //Toast.makeText(context, "after set main notification text", Toast.LENGTH_SHORT).show(); //testing
                        //When clicked the notification does not go away
                        builder.setAutoCancel(false);
                        //Allow the notification to be seen on the lock screen
                        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                        //Direct the notification into a specific channel that will shortly be created
                        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
                        //Store all of the aforementioned info into a Notification object that can be passed into the NotificationManager
                        Notification notification = builder.build();


                        //Notification notification = intent.getParcelableExtra(NOTIFICATION);
                        //notification.setStyle

                        //If SDK > 26
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            //Use Notification Channel because that's how you do it
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            //Sets/declares a new NotificationChannel
                            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Daily Happy Thoughts", importance);
                            assert notificationManager != null;
                            //Creates a new NotificationChannel
                            notificationManager.createNotificationChannel(notificationChannel);
                        }
        /*else {
            Toast.makeText(context, "Notifications aren't supported on your device, sorry!", Toast.LENGTH_LONG);
        }*/
                        //int id = 0;
                        assert notificationManager != null;
                        //Finally push the notification
                        notificationManager.notify(1, notification);
                    }
                });
        //Toast.makeText(context, Integer.toString(listOfHappyThoughts.size()), Toast.LENGTH_LONG);

        /*//Get listOfHappyThoughts from firestore
        //String[] listOfHappyThoughts;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("happyThoughts").document("happyThoughts").set(listOfHappyThoughts);
        /*
DocumentReference docRef = db.collection("happyThoughts").document("happyThoughts");
docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                listOfHappyThoughts = document.getData();
                //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
            } else {
                //Log.d(TAG, "No such document");
                Toast errInfo1 = new Toast(context);
                errInfo1.makeText(context, "You must be connected to the internet to access all of the Happy Thoughts", Toast.LENGTH_LONG);
                errInfo1.show();
                Thread.sleep(Toast.LENGTH_LONG);
                //Toast errInfo2 = new Toast(context);
                errInfo2.makeText(context, "Using a fallback list", Toast.LENGTH_SHORT);
                errInfo2.show();
            }
        } //else {
            //Log.d(TAG, "get failed with ", task.getException());

        //}
    }
});
        */

        /*Toast.makeText(context, "BeforeTry", Toast.LENGTH_SHORT).show(); //testing
        try {
            //Declare and Initialize FirebaseFirestore object
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Toast.makeText(context, "db declared", Toast.LENGTH_SHORT).show(); //testing
            //Try getting Data from database
            db.collection("happyThoughts").document("happyThoughts").get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //Toast.makeText(context, "firebase firestore get = success", Toast.LENGTH_SHORT).show(); //testing
                    //Set variable value to result from online
                    listOfHappyThoughtsClass = documentSnapshot.toObject(ListOfHappyThoughtsClass.class);
                    //Toast.makeText(context, "after toObject", Toast.LENGTH_SHORT).show(); //testing
                    //Actually extract the List object from result class with Getter
                    listOfHappyThoughts = listOfHappyThoughtsClass.getListOfHappyThoughts();
                    //Toast.makeText(context, "afterStores variable from firestore", Toast.LENGTH_SHORT).show(); //testing
                }
            });
            Toast.makeText(context, "after db get", Toast.LENGTH_SHORT).show(); //testing
        } catch (Exception e) {
            //If it doesn't work, issue a Toast telling the user
            Toast.makeText(context, "Failed to get newest notifications, using a fallback list", Toast.LENGTH_SHORT).show();
            //Set final listOfHappyThoughts to fallback list that is built-in
            listOfHappyThoughts = listOfHappyThoughtsFallback;
        }
        Toast.makeText(context, "BeforeSetNotification", Toast.LENGTH_SHORT).show(); //testing*/
        //listOfHappyThoughts = listOfHappyThoughtsFallback;
        //Overall Notification Manager - ultimately used to fire the notification directly

    }
    int getRandNum() {
        int randNum = (int) Math.floor(Math.random() * listOfHappyThoughts.size());
        return randNum;
    }
}