package com.aymanstudios.happythoughts;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    //Final List to choose sayings from (could be updated or from fallback list)
    public List<String> listOfHappyThoughts;
    //Actually use class to define variable if used from online
    private ListOfHappyThoughtsClass listOfHappyThoughtsClass;

    public void onReceive(Context context, Intent intent) {
        listOfHappyThoughts = listOfHappyThoughtsFallback;
        final Context mainContext = context;
        //Firebase stuff for getting info from database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("happyThoughts").document("happyThoughts").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                listOfHappyThoughts = (List<String>) document.getData().get("listOfHappyThoughts");
                            }
                        } else {
                            Toast.makeText(mainContext, task.getException().toString() + "Failed to get document, using a fallback list instead", Toast.LENGTH_SHORT).show();
                        }
                        NotificationManager notificationManager = (NotificationManager) mainContext.getSystemService(Context.NOTIFICATION_SERVICE);
                        //Builds new Notification object
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(mainContext, default_notification_channel_id);
                        //Give it a title
                        builder.setContentTitle("Happy Thought Of The Day:");
                        //Set its short text that is immediately available
                        builder.setContentText("...");
                        builder.setSmallIcon(R.drawable.appLogo);
                        //Set its long text (lines wrap and all text is visible)
                        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(listOfHappyThoughts.get(getRandNum())));
                        //When clicked the notification does not go away
                        builder.setAutoCancel(false);
                        //Allow the notification to be seen on the lock screen
                        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                        //Direct the notification into a specific channel that will shortly be created
                        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
                        //Store all of the aforementioned info into a Notification object that can be passed into the NotificationManager
                        Notification notification = builder.build();
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
                        //int id = 0;
                        assert notificationManager != null;
                        //Finally push the notification
                        notificationManager.notify(1, notification);
                    }
                });
    }

    int getRandNum() {
        int randNum = (int) Math.floor(Math.random() * listOfHappyThoughts.size());
        return randNum;
    }

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
}