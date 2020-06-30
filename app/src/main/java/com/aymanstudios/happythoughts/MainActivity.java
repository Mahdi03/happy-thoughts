package com.aymanstudios.happythoughts;

//All Imported Scripts for Widgets and Notification Functions (A view is an object on the screen)

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

//Main function that runs during runtime
public class MainActivity extends AppCompatActivity {
    //Global variables declared - can be accessed by other files if they are "public static"

    public static final List<String> listOfHappyThoughtsFallback = Arrays.asList("Rise and shine, today will be the day you make it, so go on and make a great day!",
            "Be patient with yourself. Nothing in nature blooms all year.",
            "Time you enjoy wasting is not wasted time.\n- Marthe Troly-Curtin",
            "Stop being afraid of what could go wrong.\nThink of what could go right!",
            "Bad news travels fast, but there is still good news in this world.",
            "Even bad people do good deeds.",
            "Life is full of surprises...\nmake it a good one.",
            "Believe in yourself, you can do anything.",
            "It doesn't matter how many times you fall down, what matters is how many times you get back up.",
            "A pessimist sees the difficulty in every opportunity. An optimist sees the opportunity in every difficulty.",
            "Be fantastically positive and militantly optimistic. If something is not to your liking, change your liking.\n- Rick Steves",
            "Choose to be optimistic. It feels better.\n- Dalai Lama",
            "Pursue excellence. Not perfection.",
            "Just because things aren't good now, doesn't mean they will be that way forever.",
            "Even the darkest night will end and the sun will rise.",
            "You can't live a positive life with a negative mind.",
            "I always like to look on the optimistic side of life, but I am realistic enough to know that life is a complex matter.\n- Walt Disney",
            "OPTIMIST: Someone who figures that taking a step backward after taking a step forward is not a disaster, it's more like a Cha-Cha.",
            "If you light a lamp for someone else, it will also brighten your path.\n- Buddha",
            "Don't cry because it's over, smile because it happened.\n- Dr. Seuss",
            "Today you are you! That is truer than true! There is no one alive who is you-er than you!\n- Dr. Seuss",
            "A person's a person, no matter how small.\n- Dr. Seuss",
            "You're off to Great Places! Today is your day! Your mountain is waiting, So... get on your way!\n- Dr. Seuss",
            "Sometimes you will never know the value of a moment, until it becomes a memory.\n- Dr. Seuss",
            "Yesterday is history, tomorrow is a mystery, and today is a gift...\nThat's why they call it the Present\n- Master Oogway",
            "...let me assert my firm belief that the only thing we have to fear is fear itself...\n- Franklin Roosevelt",
            "My momma always said, \"Life was like a box of chocolates. You never know what you're gonna get.\"\n- Forrest Gump",
            "Nothing is impossible. The word itself says: \"I'm Possible\"\n- Audrey Hepburn",
            "Be strong because things will get better. It may be stormy now, but it never rains forever.",
            "Optimism is a happiness magnet. If you stay positive, good things and good people will be drawn to you.\n- Mary Lou Retton",
            "Enjoy the little things in life, for someday you will realize they were the big things.",
            "The happiest people don't have the best of everything, they just make the best of everything.",
            "Don't trip over what is behind you.",
            "There is always something to be thankful for.",
            "The world needs dreamers and the world needs doers. But above all, the world needs dreamers who do.",
            "Minds are like flowers: They open only when the time is right.\n- Stephen Richards",
            "Every day may not be good...\nBut there is something good in every day.",
            "Everybody is a genius. But if you judge a fish by its ability to climb a tree, it will live its whole life believing that it is stupid.\n- Albert Einstein",
            "Birds sing after a storm, Why shouldn't we?\n- Rose F. Kennedy",
            "Every day is a new beginning. Treat it that way. Stay away from what might have been, and look at what can be.",
            "Always have eyes that see the best, a heart that forgives the worst, a mind that forgets the bad, and a soul that never loses hope.",
            "The hardships you are facing today, will be the fun stories of tomorrow.",
            "Sometimes you have to support yourself in life when no one else will.",
            "You can't live until you die...\nYou can't learn to tell the truth, until you learn to lie...\nThere's nothing like a funeral to make you feel alive...\nJust open your eyes and see that Life is Beautiful\n- Life is Beautiful by Sixx:A.M.",
            "There is a time in every man's education when he arrives at the conviction that envy is ignorance; that imitation is suicide; that he must take himself for better, for worse, as his portion;\n- Ralph Waldo Emerson",
            "Change will not come if we wait for some other person or some other time. We are the ones we've been waiting for. We are the change that we seek.\n- Barack Obama",
            "It is not the strongest of the species that survive, nor the most intelligent, but the one most responsive to change.\n- Charles Darwin",
            "I cannot say whether things will get better if we change;\nWhat I can say is they must change if they are to get better.\n- Georg C. Lichtenburg",
            "Change is the law of life. And those who look only to the past and present are certain to miss the future.\n- John F. Kennedy",
            "The greatest discovery of all time is that a person can change his future by merely changing his attitude.\n- Oprah Winfrey",
            "If you don't like something, change it. If you can't change it, change your attitude.\n- Maya Angelou");
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    //Set Variables for daily notification
    public static int setTimeHour;
    public static int setTimeMinute;

    //Use Android built-in Shared Preferences feature to save variables even after reboot
    private SharedPreferences mPreferences;
    private String sharedPreferencesFile = "com.aymanstudios.happythoughts";

    //Final List to choose sayings from (could be updated or from fallback list)
    public List<String> listOfHappyThoughts;
    //This represents the paragraph text
    TextView textView2;
    //This represents the button that you click for the time picker dialog to open
    Button pickTimeButton;
    //This is the required "this" scope for the time picker dialog to open
    Context context = this;
    //Actually use class to define variable if used from online
    private ListOfHappyThoughtsClass listOfHappyThoughtsClass;
    //final Calendar myCalendar = Calendar.getInstance();
    private InterstitialAd mInterstitialAd;

    //I'm assuming that "onCreate" runs when the screen loads and all the widgets are loaded
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show(); //testing

        ///*
        //Get and Show AdMob Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        //Testing AdMob Interstitial Ad ID: ca-app-pub-3940256099942544/1033173712
        //Replace AdMob Interstitial Ad ID with ca-app-pub-8495483038077603/2156743218
        mInterstitialAd.setAdUnitId("ca-app-pub-8495483038077603/2156743218");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //Define the widgets
        textView2 = findViewById(R.id.textView2);
        pickTimeButton = findViewById(R.id.pickTimeButton);
        //Get current time to open time dialog with current time already selected as placeholder
        Calendar calendar = Calendar.getInstance();
        //"final" is required because these variable will be used within an inner class - it is analogous to const
        final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = calendar.get(Calendar.MINUTE);

        //Actually Define Shared Preferences File
        mPreferences = getSharedPreferences(sharedPreferencesFile, MODE_PRIVATE);

        //Equal to JS button.addEventListener("click", callback)
        pickTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Declares and instantiates a new time picker popup dialog with an event listener for when the time is selected
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        //This Function is called when "ok" is pressed on the popup dialog
                        //Setup daily notifications here
                        //hour and minute represent the time they picked
                        setTimeHour = hour;
                        setTimeMinute = minute;
                        //Create a calendar object to convert the time into milliseconds
                        Calendar getTime = Calendar.getInstance();
                        Calendar setTime = (Calendar) getTime.clone();
                        //setTime.setTimeInMillis(System.currentTimeMillis());
                        setTime.set(Calendar.HOUR_OF_DAY, setTimeHour);
                        setTime.set(Calendar.MINUTE, setTimeMinute);
                        setTime.set(Calendar.SECOND, 0);
                        setTime.set(Calendar.MILLISECOND, 0);

                        if (setTime.compareTo(getTime) <= 0) {
                            setTime.add(Calendar.DATE, 1);
                        }

                        //scheduleNotification(getNotification("Happy Thought Of The Day:"), setTime.getTimeInMillis());
                        //Actual call to schedule notification
                        scheduleNotification(setTime.getTimeInMillis());
                        //Now Save The Chosen Time to Shared Preferences file for reboots
                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                        //Reset Previously Existing Times
                        preferencesEditor.clear();
                        preferencesEditor.commit();
                        ///Set new times
                        preferencesEditor.putInt("setTimeHourSharedPreferences", setTimeHour);
                        preferencesEditor.putInt("setTimeMinuteSharedPreferences", setTimeMinute);
                        preferencesEditor.apply();

                        //After daily notifications are setup, call boot listener that will reset the alarm if the user shuts down the device
                        ComponentName bootReceiver = new ComponentName(context, BootReceiver.class);
                        PackageManager pm = context.getPackageManager();
                        pm.setComponentEnabledSetting(bootReceiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                        //Actually display the ad
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                        //Change informational text to text that tells user that their notifications have been set
                        textView2.setText("Your optimistic notifications have been scheduled for " + hour + ":" + minute + " every day.\n\nNow that you have setup your daily notifications, you no longer need to open this app. If you want to change the time of notifications, simply open this app again and put in your new time. Thank you for installing Happy Thoughts.");
                        //Hide time picker button
                        pickTimeButton.setVisibility(View.GONE);
                    }
                    //The third and fourth parameters to new TimePickerDialog supply the placeholder time, and we will use the current time as its placeholder
                    //The last parameter is a boolean that decides whether we use 12 or 24 hour format
                }, currentHour, currentMinute, android.text.format.DateFormat.is24HourFormat(context));
                //Finally actually show the time dialog when the button is clicked
                timePickerDialog.show();
            }
        });

    }

    //Function to schedule the notifications
    private void scheduleNotification(long delay) {
        /*
        Create a new intent that is the MyNotificationPublisher.class
        What this does is it queues up to call the MyNotificationPublisher.java file
        to run the code inside that class
        */
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        //Set an ID to transfer over to the MyNotificationPublisher.java
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1);
        /*
        This pending intent is an intent that is waiting to happen, meaning that it will
        run when scheduled. To this pending action we attach the new intent we just created
        for MyNotificationPublisher.java.
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        /*
        Everything in MyNotificationPublisher.java will be repeated every single time the
        AlarmManager goes off. The AlarmManager is a simple function that runs a timer in
        the background and lets you call a predestined class (function) at a given time. In
        this case, we are repeating the call once a day at a user-selected time
        */
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        /*
        Change to AlarmManager.INTERVAL_FIFTEEN_MINUTES for testing purposes

        This function call wakes up the device every time the alarm sets off, and it calls our
        PendingIntent which is ultimately binded to our Intent that is binded to
        MyNotificationPublisher.java class
        The setInexactRepeating instead of exact repeating is for the device because
        the notifications aren't so urgent so although they will be triggered at the
        same time, they will show up when readily-available for the device
        */
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, delay, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    class ListOfHappyThoughtsClass {
        //List (complicated Array) of sayings to choose from
        private List<String> listOfHappyThoughts;

        public ListOfHappyThoughtsClass() {
            //Required for DataSnapshot.getValue(ListOfHappyThoughts.class)
        }

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
