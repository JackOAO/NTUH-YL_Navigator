package eos.waypointbasedindoornavigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.viro.core.ARAnchor;
import com.viro.core.ARHitTestListener;
import com.viro.core.ARHitTestResult;
import com.viro.core.ARNode;
import com.viro.core.ARScene;
import com.viro.core.AmbientLight;
import com.viro.core.AsyncObject3DListener;
import com.viro.core.Box;
import com.viro.core.CameraListener;
import com.viro.core.GestureRotateListener;
import com.viro.core.Object3D;
import com.viro.core.Quaternion;
import com.viro.core.RotateState;
import com.viro.core.Text;
import com.viro.core.Vector;
import com.viro.core.ViroView;
import com.viro.core.ViroViewARCore;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import eos.waypointbasedindoornavigation.Find_loc.DeviceParameter;
import eos.waypointbasedindoornavigation.Find_loc.Find_Loc;
import eos.waypointbasedindoornavigation.Find_loc.ReadWrite_File;

import static eos.waypointbasedindoornavigation.GeoCalulation.getDirectionFromBearing;
import static eos.waypointbasedindoornavigation.Setting.getPreferenceValue;


public class ARNavigationActivity extends AppCompatActivity implements BeaconConsumer {
    //---------------------UI of Layout---------------------
    private TextView facingDirectionText;
    private TextView destinationText;
    private TextView currentLocationText;
    ProgressBar progressBar;
    TextView progressNumber;
    int progressStatus = 0;
    //// Indicator for desicing which instruction been shown
    String turnNotificationForPopup = null;

    //---------------------ViroCore uses parameter---------------------
    private ViroView mViroView;
    // Constants used to determine if plane or point is within bounds. Units in meters.
    private static final float MIN_DISTANCE = 0.2f;
    private static final float MAX_DISTANCE = 10f;
    //The ARScene we will be creating within this activity.
    private com.viro.core.ARScene mScene;
    //Trace Camera Position, Rotation and Forward
    private Vector lastCameraPosition;
    private Vector lastCameraRotation;
    private Vector lastCameraForward;
    //The list of placed nodes
    private List<ARNode> placedNode;
    private List<String> placedID;

    //Boolean to show whether  or not cross the floor
    private boolean isOverFloor;

    //---------------------Sensor---------------------
    private SensorManager sensorManager;
    //accelerometer
    private Sensor accelerometerSensor;
    float[] accelerometerValue = new float[3];
    //magnetic
    private Sensor magneticSensor;
    float[] magneticValue = new float[3];
    //toShowCurrentDirection
    private Integer currentDirection;
    private Integer predictDirection;
    private DirectionSensorUtils directionSensorUtils;


    //---------------------Connect Waypoint Category---------------------
    private static final int NORMAL_WAYPOINT = 0;
    private static final int ELEVATOR_WAYPOINT = 1;
    private static final int STAIRWELL_WAYPOINT = 2;
    private static final int CONNECTPOINT = 3;
    //---------------------Intruction Category to decise which UI to Show---------------------
    private static final int ARRIVED_NOTIFIER = 0;
    private static final int WRONGWAY_NOTIFIER = 1;
    private static final int MAKETURN_NOTIFIER = 2;
    //---------------------Virtual Node---------------------
    private static final int VIRTUAL_UP = 1;
    private static final int VIRTUAL_DOWN = 2;
    String language_option;
    //---------------------English String---------------------
    private static final String FRONT = "front";
    private static final String FRONT_RIGHTSIDE = "frontRightSide";
    private static final String FRONT_LEFTSIDE = "frontLeftSide";
    private static final String LEFT = "left";
    private static final String FRONT_LEFT = "frontLeft";
    private static final String REAR_LEFT = "rearLeft";
    private static final String RIGHT = "right";
    private static final String FRONT_RIGHT = "frontRight";
    private static final String REAR_RIGHT = "rearRight";
    private static final String ELEVATOR = "elevator";
    private static final String STAIR = "stair";
    private static final String ARRIVED = "arrived";
    private static final String WRONG = "wrong";
    //---------------------Chinese String---------------------
    private String NOW_GO_STRAIGHT_RIGHTSIDE = "請靠右";
    private String NOW_GO_STRAIGHT_LEFTSIDE = "請靠左";
    private String NOW_GO_STRAIGHT = "請繼續直走";
    private String NOW_TURN_LEFT = "請向左轉後";
    private String NOW_TURN_RIGHT = "請向右轉後";
    private String NOW_TURN_FRONT_LEFT = "請向左前方轉後";
    private String NOW_TURN__FRONT_RIGHT = "請向右前方轉後";
    private String NOW_TURN_REAR_LEFT = "請向左後方轉後";
    private String NOW_TURN__REAR_RIGHT = "請向右後方轉後";
    private String NOW_TAKE_ELEVATOR = "離開電梯後";
    private String NOW_WALK_UP_STAIR = "上樓梯後";
    private String NOW_WALK_DOWN_STAIR = "下樓梯後";

    private String GO_STRAIGHT_ABOUT = "直走約";
    private String THEN_GO_STRAIGHT = "然後直走";
    private String THEN_GO_STRAIGHT_RIGHTSIDE = "然後靠右直走";
    private String THEN_GO_STRAIGHT_LEFTSIDE = "然後靠左直走";
    private String THEN_TURN_LEFT = "然後向左轉";
    private String THEN_TURN_RIGHT = "然後向右轉";
    private String THEN_TURN_FRONT_LEFT = "然後向左前方轉";
    private String THEN_TURN__FRONT_RIGHT = "然後向右前方轉";
    private String THEN_TURN_REAR_LEFT = "然後向左後方轉";
    private String THEN_TURN__REAR_RIGHT = "然後向右後方轉";
    private String THEN_TAKE_ELEVATOR = "然後搭電梯";
    private String THEN_WALK_UP_STAIR = "然後爬樓梯";
    private String THEN_WALK_DOWN_STAIR = "然後下樓梯";
    private String WAIT_FOR_ELEVATOR = "電梯中請稍候";
    private String WALKING_UP_STAIR = "爬樓梯";
    private String WALKING_DOWN_STAIR = "下樓梯";

    private String YOU_HAVE_ARRIVE = "抵達目的地";
    private String GET_LOST = "糟糕，你走錯路了";
    private String METERS = "公尺";
    private String PLEASE_GO_STRAIGHT = "請直走";
    private String PLEASE_GO_STRAIGHT_RIGHTSIDE = "請靠右直走";
    private String PLEASE_GO_STRAIGHT_LEFTSIDE = "請靠左直走";
    private String PLEASE_TURN_LEFT = "請左轉";
    private String PLEASE_TURN_RIGHT = "請右轉";
    private String PLEASE_TURN_FRONT_LEFT = "請向左前方轉";
    private String PLEASE_TURN__FRONT_RIGHT = "請向右前方轉";
    private String PLEASE_TURN_REAR_LEFT = "請向左後方轉";
    private String PLEASE_TURN__REAR_RIGHT = "請向右後方轉";
    private String PLEASE_TAKE_ELEVATOR = "請搭電梯";
    private String PLEASE_WALK_UP_STAIR = "請走樓梯";

    private String To = "至";
    private String toBasement = "至地下一樓";
    private String toFirstFloor = "至一樓";
    private String toSecondFloor = "至二樓";
    private String toThirdFloor = "至三樓";
    private String toFourthFloor = "至四樓";
    private String toFivthFloor = "至五樓";
    private String floor = "樓";

    private String TITLE = "臺大雲林分院室內導航系統";
    private String PRESENT_POSITION = "目前位置：";
    private String DESTINATION = "目的地：";
    private String initailDirectionImageTitle;

    //Direction list
    private String NORTH;
    private String NORTHEAST;
    private String EAST;
    private String SOUTHEAST;
    private String SOUTH;
    private String SOUTHWEST;
    private String WEST;
    private String NORTHWEST;
    private List<String > directionList;
    private String PLEASEFACETO;
    private String DIRECTION;
    private String RIGHTDIRETION;
    private String FACINGDIRETION;


    //---------------------Bluetooth---------------------
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    //---------------------Route Information---------------------
    // IDs and Regions of source and destination input by user on home screen
    String sourceID, destinationID, sourceRegion, destinationRegion, destinationName, wrongdestinationID, wrongdestinationRegion, tmpdestinationID, tmpdestinationRegion;
    String currentLocationName;
    Node startNode;
    Node endNode;
    Node lastNode;
    Node wrongWaypoint;
    Node chosestartNode;
    // Integer to record how many waypoints have been traveled
    int walkedWaypoint = 0;
    int pathLength = 0;
    int regionIndex = 0;
    int passedGroupID = -1;
    String passedRegionID;
    List<String> tmpDestinationID = new ArrayList<>();
    //recieve 2 times for same LBeacon
    int error_count = 0;
    int turnback_count = 0;
    Node tmpNode;
    Node recordbeacon;
    int nextFloor;
    String firstMovement;
    String nextTurnMovement;
    String howFarToMove;

    //---------------------Map Information---------------------
    // List of NavigationSubgraph object representing a Navigation Graph
    List<NavigationSubgraph> navigationGraph = new ArrayList<>();
    List<NavigationSubgraph> navigationGraphForAllWaypoint = new ArrayList<>();
    // List of Region object storing the information of regions that will be traveled through
    List<eos.waypointbasedindoornavigation.Region> regionPath = new ArrayList<>();
    // Hashmap for storing region data
    RegionGraph regionGraph = new RegionGraph();
    // List of Node object representing a navigation path
    List<Node> navigationPath = new ArrayList<Node>();
    List<Node> virtualNodeUp = new ArrayList<Node>();
    List<Node> virtualNodeDown = new ArrayList<Node>();
    HashMap<String, String> navigationPath_ID_to_Name_Mapping = new HashMap<>();
    HashMap<String, String> mappingOfRegionNameAndID = new HashMap<>();
    HashMap<String, Node> allWaypointData = new HashMap<>();

    // ---------------------Vocice Engine---------------------
    private TextToSpeech voiceEngine;

    //---------------------Beacon Signal---------------------
    //BeaconManager
    private BeaconManager beaconManager;
    private org.altbeacon.beacon.Region regionForBeacon;
    // Thread for handling Lbeacon ID while in a navigation tour
    Thread threadForHandleLbeaconID;
    // Handlers for "threadToHandleLbeaconID", receive message from the thread
    static Handler instructionHandler, currentPositiontHandler, walkedPointHandler, progressHandler;
    // Synchronization between Lbeacon receiver and handler thread
    final Object sync = new Object();
    // String for storing currently received Lbeacon ID
    String currentLBeaconID = "";
    String lastRoundBeaconID = "";

    // Find_loc part
    private Find_Loc findLoc = new Find_Loc();
    private DateFormat dataFormat = new SimpleDateFormat("yy_MM_DD_hh_mm");
    private ReadWrite_File readWrite_file = new ReadWrite_File();
    private DeviceParameter deviceParameter;
    String receivebeacon;
    double offset;
    String Find_Loc_pass = "";


    //---------------------Boolean for judge---------------------
    boolean search_in_image = false;
    boolean isFirstBeacon = true;
    boolean FirstTurn = true;
    boolean isInVirtualNode = false;
    boolean StairGoUp = false;
    boolean LastisSlash = false;
    boolean DirectCompute = false;
    boolean JumpNode = false;
    boolean arriveinwrong = false;
    boolean isLongerPath = false;
    boolean CallDirectionInWrong = false;
    boolean Calibration = false;

    //Initial Time
    private long startT = System.currentTimeMillis();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get offset value
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        offset = (double) pref.getFloat("offset",(float) 0);
        Log.i("offset", "offset : " + offset);
        Log.i("ARModule_Create_Mem", "usedMemory: Heap/Allocated Heap "+ Debug.getNativeHeapSize() + "/" + Debug.getNativeHeapAllocatedSize());

        // Receive value passed from NavigationActivity,
        //including IDs and Regions of source and destination
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            destinationName = bundle.getString("destinationName");
            destinationID = bundle.getString("destinationID");
            destinationRegion = bundle.getString("destinationRegion");
        }
        Log.i("AR_destinationName","destinationName : " + destinationName);
        Log.i("AR_destinationID","destinationID : " + destinationID);
        Log.i("AR_destinationRegion","destinationRegion : " + destinationRegion);

        //LogFile setup
        readWrite_file.setFile_name("Log" + dataFormat.format(Calendar.getInstance().getTime()));
        deviceParameter = new DeviceParameter();

        // voice engine setup
        voiceEngine = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status != TextToSpeech.ERROR) {
                    voiceEngine.setLanguage(Locale.CHINESE);
                }
            }
        });

        //Language setup
        Language_SetUp();

        // Load region data from region graph
        regionGraph = DataParser.getRegionDataFromRegionGraph(this);
        mappingOfRegionNameAndID = DataParser.waypointNameAndIDMappings(this,
                regionGraph.getAllRegionNames());

        navigationPath.add(new Node("empty", "empty", "empty", "empty"));
        //Load all waypoint data for precise positioning
        loadAllWaypointData();
        virtualNodeUp = DataParser.getVirtualNode(this,VIRTUAL_UP);
        virtualNodeDown = DataParser.getVirtualNode(this,VIRTUAL_DOWN);

        //ViroCore startup
        mViroView = new ViroViewARCore(this, new ViroViewARCore.StartupListener() {
            @Override
            public void onSuccess() {
                displayScene();
            }

            @Override
            public void onFailure(ViroViewARCore.StartupError startupError, String s) {
                Log.e("AR Failure", "Error initializing AR [" + s + "]");
            }
        });
        //setCameraListener to trace Camera Rotation
        mViroView.setCameraListener(new myCameraListener());
        setContentView(mViroView);

    }

    //Menu setup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation,menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_home){
            beaconManager.removeAllMonitorNotifiers();
            beaconManager.removeAllRangeNotifiers();
            beaconManager.unbind(ARNavigationActivity.this);
            Intent intent = new Intent();
            intent = new Intent(ARNavigationActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected  void onStart()
    {
        super.onStart();
        mViroView.onActivityStarted(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mViroView.onActivityResumed(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mViroView.onActivityPaused(this);
    }

    @Override
    protected  void onStop()
    {
        super.onStop();
        mViroView.onActivityStopped(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        beaconManager.removeAllMonitorNotifiers();
        beaconManager.removeAllRangeNotifiers();
        beaconManager.unbind(this);
        mViroView.onActivityDestroyed(this);
        System.gc();
    }

    //Back Key
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            beaconManager.removeAllMonitorNotifiers();
            beaconManager.removeAllRangeNotifiers();
            beaconManager.unbind(ARNavigationActivity.this);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }

    @Override
    public void onBeaconServiceConnect() {
        //Start scanning for Lbeacon signal
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, org.altbeacon.beacon.Region region) {
                Log.i("beacon", "Beacono Size:" + beacons.size());
                Log.i("beaconManager", "beaconRanging");
                if(FirstTurn == true && Calibration == false){
                    long endT = System.currentTimeMillis();
                    //If do not have any beacon in then down the threshold by 5
                    if(endT - startT > 8000){
                        offset = offset - 5;
                        Calibration = true;
                    }
                }
                if (beacons.size() > 0) {
                    Iterator<Beacon> beaconIterator = beacons.iterator();
                    while (beaconIterator.hasNext()) {
                        Beacon beacon = beaconIterator.next();
                        Log.i("offset","offset = " + offset);
                        logBeaconData(findLoc.Find_Loc(beacon, 3, offset,Find_Loc_pass));
                    }
                }
            }

        });
        try {
            beaconManager.startRangingBeaconsInRegion(new org.altbeacon.beacon.Region("myRangingUniqueId",
                    null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // load beacon ID
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void logBeaconData(List<String> beacon) {
        Log.i("beaconSize", "size : " + beacon.size());
        if (beacon.size() > 2) {

            Log.i("ScanedBeacon", "beacon 0: " + beacon.get(0));

            Log.i("ScanedBeacon", "beacon 1: " + beacon.get(1));

            Log.i("ScanedBeacon", "beacon 2: " + beacon.get(2));
            Node receiveNode;
            Boolean pass = false;

            readWrite_file.writeFile("NAP1:" + beacon.toString());
            Log.i("NAP1", beacon.toString());
            receivebeacon = null;


            if(beacon.get(2).equals("close"))
                Log.i("now","Now Receive " + beacon.get(3));
            if(Find_Loc_pass.equals(beacon.get(3)) && beacon.get(2).equals("close")){
                Log.i("pass","Pass" + Find_Loc_pass);
                turnback_count++;
            }else if(beacon.get(2).equals("close")){
                turnback_count = 0;
            }

            if(turnback_count == 0 || turnback_count > 5) {
                //連續收兩次判斷，close才進去
                if (beacon.get(2).equals("close") && navigationPath.size() > 0) {
                    tmpNode = allWaypointData.get(beacon.get(3));
                    if (navigationPath.get(0)._waypointID.equals(beacon.get(3))) {
                        //inpath Next equal receive close
                        receivebeacon = beacon.get(3);
                        error_count = 0;
                    } else if (navigationPath.get(0)._groupID != 0 && navigationPath.get(0)._groupID == tmpNode._groupID) {
                        //inpath Next equal receive group close
                        receivebeacon = beacon.get(3);
                        error_count = 0;
                    } else if (!navigationPath.get(0)._waypointID.equals(beacon.get(3)) && error_count == 0) {
                        //Not inpath First times
                        recordbeacon = allWaypointData.get(beacon.get(3));
                        error_count++;
                    } else if (!navigationPath.get(0)._waypointID.equals(beacon.get(3)) && error_count == 1) {
                        //Not inpath Second times
                        if (recordbeacon._waypointID.equals(beacon.get(3))) {
                            receivebeacon = beacon.get(3);
                            error_count = 0;
                        } else if (!recordbeacon._waypointID.equals(beacon.get(3)) && recordbeacon._groupID != 0) {

                            if (tmpNode._groupID == recordbeacon._groupID) {
                                receivebeacon = beacon.get(3);
                                error_count = 0;
                            } else {
                                error_count = 0;
                            }
                        } else {
                            error_count = 0;
                        }
                    } else {
                        receivebeacon = beacon.get(3);
                        error_count = 0;
                    }
                }
            }
            Log.i("NAP1", beacon.toString() + receivebeacon);

            receiveNode = allWaypointData.get(receivebeacon);

            if (receiveNode != null)
                currentLocationText.setText(PRESENT_POSITION + receiveNode._waypointName);


            Log.i("beaconManager", "receiveID: " + receivebeacon);

            if (isFirstBeacon && receiveNode != null) {
                chosestartNode = receiveNode;
                sourceID = receiveNode._waypointID;
                sourceRegion = receiveNode._regionID;
                passedRegionID = sourceRegion;
                loadNavigationGraph();
                navigationPath = startNavigation();
                progressBar.setMax(navigationPath.size());

                //sourceID = destination
                for (int i = 0;i < chosestartNode._attachIDs.size();i++) {
                    Log.i("arrive","choseNodeattachID = " + chosestartNode._attachIDs.get(i) + "mainID = " + endNode._mainID);
                    if ((endNode._mainID != 0 && endNode._mainID == chosestartNode._attachIDs.get(i))) {
                        showHintAtWaypoint(ARRIVED_NOTIFIER);
                        isFirstBeacon = false;
                    }
                }

                if(chosestartNode._waypointID.equals(endNode._waypointID)) {
                    showHintAtWaypoint(ARRIVED_NOTIFIER);
                    isFirstBeacon = false;
                }

                //appendLog("StartNavigation");

                //初始設定
                if(navigationPath.size() >= 1 && isFirstBeacon == true) {
                    beaconManager.removeAllMonitorNotifiers();
                    beaconManager.removeAllRangeNotifiers();
                    beaconManager.unbind(this);

                    ARInitialImage arInitialImage = new ARInitialImage();
                    int imageNumber = arInitialImage.deciseImageToShow(navigationPath.get(0)._waypointID, navigationPath.get(1)._waypointID);
                    predictDirection = arInitialImage.getPredict(navigationPath.get(0)._waypointID, navigationPath.get(1)._waypointID);
                    Log.i("predictDirection","predict : " + predictDirection);

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.ar_initial_image_layout,null);
                    ImageView image = (ImageView) layout.findViewById(R.id.initialImage);
                    image.setImageResource(imageNumber);
                    if(imageNumber == R.drawable.elevator)
                    {
                        if(language_option == "繁體中文")
                            initailDirectionImageTitle = "請搭電梯";
                        else if(language_option == "English")
                            initailDirectionImageTitle = "Please take the elevator.";
                    }
                    TextView title = layout.findViewById(R.id.faceto);
                    title.setText(initailDirectionImageTitle);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ARNavigationActivity.this);
                    builder.setView(layout);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            beaconManagerSetup();
                        }
                    });
                    builder.show();

                    //Show initialDirectionImage
                    //Intent intent = new Intent(ARNavigationActivity.this, InitDirectionImage.class);
                    //intent.putExtra("nowID", navigationPath.get(0)._waypointID);
                    //intent.putExtra("nextID", navigationPath.get(0)._waypointID);
                    //startActivity(intent);
                    // TODO: 2020/2/24 maybe need to been fixed
                    //imageTurnIndicator.setImageResource(R.drawable.up_now);
                    //add3DObject("file:///android_asset/ARModel/Front.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()));
                }
                isFirstBeacon = false;
            }

            if (navigationPath.size() > 0) {
                if (receivebeacon != null && !currentLBeaconID.equals(receivebeacon) && receiveNode != null) {
                    Log.i("receivebeacon", "receivebeacon =" + receivebeacon);
                    Log.i("receiveNode", "receiveNode = " + receiveNode._waypointName);
                    //Log.i("Path", "navigationPath.get(0)ID = " + navigationPath.get(0)._waypointName);
                    for(int i = 0;i < navigationPath.size();i ++)
                    {
                        Log.i("Path" ,"navigationPath.get(" + i +") =" + navigationPath.get(i)._waypointName);
                    }
                    if (receiveNode._groupID == navigationPath.get(0)._groupID &&
                            receiveNode._groupID != 0) {
                        Log.i("NAP2-1", receiveNode.getName());
                        Log.i("enter", "1");
                        currentLBeaconID = navigationPath.get(0)._waypointID;
                        pass = true;
                    } else if (receiveNode._groupID == passedGroupID && receiveNode._groupID != 0) {
                        Log.i("enter", "2");
                        pass = false;
                    } else {
                        Log.i("NAP2-2", receiveNode.getName());
                        Log.i("enter", "3");
                        currentLBeaconID = receivebeacon;
                        pass = true;
                    }
                } else {
                    Log.i("enter", "4");
                    pass = false;
                }
            }
            Log.i("renavigate", "CurrentID: " + currentLBeaconID);

            if(allWaypointData.containsKey(currentLBeaconID))
            {
                if(!placedID.contains(currentLBeaconID))
                    pass = true;
            }

            // block the Lbeacon ID the navigator just received
            Log.i("pass","pass = " + pass);
            if (pass) {
                synchronized (sync) {
                    sync.notify();
                }
            }
        }
    }
    
    //Load Waypoint Data
    private void loadNavigationGraph()
    {
        regionGraph = DataParser.getRegionDataFromRegionGraph(this);
        mappingOfRegionNameAndID = DataParser.waypointNameAndIDMappings(this,
                regionGraph.getAllRegionNames());
        //RegionPath for storing Region objects represent the regions
        //that the user passes by from source to destination
        regionPath = regionGraph.getRegionPath(sourceRegion, destinationRegion);

        //A list of String of region name in regionPath
        List<String> regionPathID = new ArrayList<>();

        for (int i = 0; i < regionPath.size(); i++)
            regionPathID.add(regionPath.get(i)._regionName);

        //Load waypoint data from the navigation subgraphs according to the regionPathID
        navigationGraph = DataParser.getWaypointDataFromNavigationGraph(this, regionPathID);

        //Get the two Node objects that represent starting point and destination
        startNode = navigationGraph.get(0).nodesInSubgraph.get(sourceID);
        endNode = navigationGraph.get(navigationGraph.size() - 1).nodesInSubgraph.get(destinationID);
    }

    //Generate the path
    private List<Node> startNavigation()
    {
        List<Node> path = new ArrayList<>();
        int startNodeType = startNode._nodeType;

        // Temporary variable to record connectPointID
        int connectPointID;

        // Navigation in the same region
        if (navigationGraph.size() == 1) {

            path = computeDijkstraShortestPath(startNode, endNode);

            // preform typical dijkstra's algorithm with two given Node objects
            //navigationPath = computeDijkstraShortestPath(startNode, endNode);


        }
        // navigation between several regions
        else {

            // compute N-1 navigation paths for each region,
            // where N is the number of region to travel

            Log.i("graph", "Navigation Graph Size " + navigationGraph.size());
            for (int i = 0; i < navigationGraph.size() - 1; i++) {

                // a destination vertex for each region
                Node destinationOfARegion = null;

                tmpDestinationID.clear();

                // the source vertex becomes a normal waypoint
                navigationGraph.get(i).nodesInSubgraph.get(sourceID)._nodeType = NORMAL_WAYPOINT;

                //If the elevation of the next region to travel is same as the current region
                if (regionPath.get(i)._elevation == regionPath.get(i + 1)._elevation) {

                    // compute a path to a transfer point of current region
                    // return the transfer point
                    destinationOfARegion = computePathToTraversePoint(
                            navigationGraph.get(i).nodesInSubgraph.get(sourceID), true, i + 1);

                    // sourceID is updated with the ID of transfer node for the next computation
                    // since the transfer node has the same ID in the same elevation
                    sourceID = destinationOfARegion.getID();

                }
                //If the elevation of the next region to travel is different from the current region
                else if (regionPath.get(i)._elevation != regionPath.get(i + 1)._elevation) {

                    Log.i("path", "region name " + regionPath.get(i)._regionID);
                    // compute a path to a transfer point(elevator or stairwell) of current region
                    // return the transfer point

                    //start point is a transfer point
                    if (startNodeType == Setting.getPreferenceValue() &&
                            find_SourceID_In_Next_Region(startNode._connectPointID, i + 1) != null) {
                        destinationOfARegion = startNode;

                        // get the connectPointID of the transfer node
                        connectPointID = destinationOfARegion._connectPointID;

                        sourceID = find_SourceID_In_Next_Region(connectPointID, i + 1);
                    } else {

                        String tmpSourceID = null;

                        // loop until find the correct source id for the next region
                        while (tmpSourceID == null) {

                            // if tmpDestinationID is not null, re-load navigation graph
                            // and change the waypoint into normal waypoint
                            if (tmpDestinationID.size() >= 1) {

                                loadNavigationGraph();

                                for (int count = 0; count < tmpDestinationID.size(); count++) {
                                    navigationGraph.get(i).nodesInSubgraph.get(tmpDestinationID.get(count))._nodeType = NORMAL_WAYPOINT;
                                }

                            }

                            destinationOfARegion = computePathToTraversePoint(
                                    navigationGraph.get(i).nodesInSubgraph.get(sourceID), false, i + 1);

                            // get the connectPointID of the transfer node
                            connectPointID = destinationOfARegion._connectPointID;

                            // find if the tmpDestination can connect to the next region
                            // if so, tmpSourceID is not null
                            // if not, tmpSourceID is null, then continue looping
                            tmpSourceID = find_SourceID_In_Next_Region(connectPointID, i + 1);

                            Log.i("path", "destination Of region " + destinationOfARegion._waypointName);

                        }

                        sourceID = tmpSourceID;

                        Log.i("path", "source ID in next region " + sourceID);

                    }

                }

                // add up all the navigation paths into one
                //navigationPath.addAll(getShortestPathToDestination(destinationOfARegion));
                path.addAll(getShortestPathToDestination(destinationOfARegion));

            }

            //Compute navigation path in the last region
            List<Node> pathInLastRegion = computeDijkstraShortestPath(
                    navigationGraph.get(navigationGraph.size() - 1).nodesInSubgraph.get(sourceID),
                    endNode);

            Log.i("path", "Path in last region " + pathInLastRegion.size());

            // complete the navigation path
            //navigationPath.addAll(pathInLastRegion);
            path.addAll(pathInLastRegion);

            // remove duplicated waypoints which are used as connecting points in the same elevation
            for (int i = 1; i < path.size(); i++) {
                if (path.get(i)._waypointID.equals(path.get(i - 1)._waypointID))
                    path.remove(i);
            }
        }

        //Log.i("bbb", "path size"+navigationPath.size());

        for (int i = 0; i < path.size(); i++)
            navigationPath_ID_to_Name_Mapping.put(path.get(i)._waypointID,
                    path.get(i)._waypointName);

        findLoc.setpath(path);

        pathLength = GeoCalulation.getPathLength(path);

        Log.i("pathLength", "pathLength: " + pathLength);

        for (int i = 0; i < path.size(); i++)
            Log.i("path", path.get(i)._waypointName);

        //防止連續接收
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 1000);

        return path;
    }

    //Compute the shortest pathe by Dijkstra
    //Step 1 decise which node is the destination
    public List<Node> computeDijkstraShortestPath(Node source, Node destination)
    {
        source.minDistance = 0.;
        PriorityQueue<Node> nodeQueue = new PriorityQueue<Node>();
        nodeQueue.add(source);
        int destinationGroup = destination._mainID;

        while (!nodeQueue.isEmpty()) {
            Node v = nodeQueue.poll();

            Log.i("Dijkstra", "In dijsk node name " + v._waypointName);

            if (destinationGroup != 0 && DirectCompute == false) {
                for(int i = 0 ; i < v._attachIDs.size(); i++) {
                    if (v._attachIDs.get(i) == destinationGroup) {
                        destination = navigationGraph.get(navigationGraph.size() - 1).nodesInSubgraph.get(v._waypointID);
                        Log.i("Dijkstra", "destination is: " + destination._waypointName);
                        break;
                    }
                }
            }

            //Stop searching when reach the destination node
            if (v._waypointID.equals(destination._waypointID))
                break;
            // Visit each edge that is adjacent to v
            for (Edge e : v._edges) {
                Node a = e.target;
                Log.i("Dijkstra", "node a " + a._waypointName);
                double weight = e.weight;
                double distanceThroughU = v.minDistance + weight;
                if (distanceThroughU < a.minDistance) {
                    nodeQueue.remove(a);
                    a.minDistance = distanceThroughU;
                    a.previous = v;
                    Log.i("Dijkstra", "set previous");
                    nodeQueue.add(a);
                }
            }
        }
        Log.i("Dijkstra", "destination is: " + destination._waypointName);

        return getShortestPathToDestination(destination);
    }
    //Step 2 If the destination and current waypoint int the same region, get the shortest pathe to destination
    public List<Node> getShortestPathToDestination(Node destination) {
        List<Node> path = new ArrayList<Node>();


        for (Node node = destination; node != null; node = node.previous) {
            Log.i("Path", "get path " + node._waypointName);
            path.add(node);
        }


        // reverse path to get correct order 顛倒
        Collections.reverse(path);
        return path;
    }

    //If the destination and current waypoint isn't in the same region, get the traverse point as the destination of path
    public Node computePathToTraversePoint(Node source, Boolean sameElevation, int indexOfNextRegion)
    {
        Node backupTransferNode = null;
        boolean entered = false;
        source.minDistance = 0.;
        PriorityQueue<Node> nodeQueue = new PriorityQueue<Node>();
        nodeQueue.add(source);


        while (!nodeQueue.isEmpty()) {

            Node u = nodeQueue.poll();

            // Visit each edge exiting u
            for (Edge e : u._edges) {
                Node v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    nodeQueue.remove(v);

                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    nodeQueue.add(v);
                }

                // if the elevation of the next region to travel is same as current region
                // find the nearest connect point and check if it is legal
                if (sameElevation == true && v._nodeType == CONNECTPOINT) {

                    // return v, only if the connect point is in the next region
                    if (navigationGraph.get(indexOfNextRegion).nodesInSubgraph.get(v._waypointID) != null)
                        return v;

                }

                // if the elevation of the next region to travel is different from current region
                // find the nearest elevator or stairwell based on user's preference
                else if (sameElevation == false && v._nodeType == getPreferenceValue()) {
                    tmpDestinationID.add(v._waypointID);
                    return v;
                } else if (sameElevation == false && v._nodeType != getPreferenceValue() && v._nodeType != NORMAL_WAYPOINT && entered == false) {
                    backupTransferNode = v;
                    entered = true;
                }
            }
        }

        if (backupTransferNode != null)
            return backupTransferNode;
        else
            return source;
    }

    //If the current waypoint and destination in the different region then need to find the source of the next region
    public String find_SourceID_In_Next_Region(int currentConnectID, int nextRegionIndex)
    {
        for (Map.Entry<String, Node> entry : navigationGraph.get(nextRegionIndex).nodesInSubgraph.entrySet()) {

            Node v = entry.getValue();

            if (v._connectPointID == currentConnectID) {

                String id = v.getID();
                return id;
            }
        }

        return null;
    }

    //ViroCore Camera Listener to trace camera position, rotation and forward
    private class myCameraListener implements CameraListener
    {
        @Override
        public void onTransformUpdate(Vector vector, Vector vector1, Vector vector2) {
            lastCameraPosition = vector;
            lastCameraRotation = vector1;
            lastCameraForward = vector2;

        }
    }

    //ARSceneListener Setup to initial AR
    private  class ARSceneListener implements ARScene.Listener{
        private WeakReference<Activity> mCurrentActivityWeak;
        private boolean mInitialized;
        public ARSceneListener(Activity activity, View rootView) {
            mCurrentActivityWeak = new WeakReference<Activity>(activity);
            mInitialized = false;
        }
        @Override
        public void onTrackingUpdated(ARScene.TrackingState trackingState,
                                      ARScene.TrackingStateReason trackingStateReason) {
            if (!mInitialized && trackingState == ARScene.TrackingState.NORMAL) {
                Activity activity = mCurrentActivityWeak.get();
                if (activity == null) {
                    return;
                }
                Log.i("AR Initial","AR is initialized");
                mInitialized = true;
            }
        }

        @Override
        public void onTrackingInitialized() {
            // This method is deprecated.
        }

        @Override
        public void onAmbientLightUpdate(float lightIntensity, Vector lightColor) {
            // No-op
        }

        @Override
        public void onAnchorFound(ARAnchor arAnchor, ARNode arNode) {

        }

        @Override
        public void onAnchorRemoved(ARAnchor arAnchor, ARNode arNode) {

        }

        @Override
        public void onAnchorUpdated(ARAnchor arAnchor, ARNode arNode) {

        }
    }


    //Sensor Listener
    final SensorEventListener SensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValue = event.values;
            if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticValue = event.values;
            calculateOrientation();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private void calculateOrientation()
    {
        //tempValue[0]: azimuth, rotation around the Z axis.
        //tempValues[1]: pitch, rotation around  the X axis.
        //tempValues[2]: roll, rotation around the Y axis.
        float[] tempValues = new float[3];
        float[] tempRotation = new float[9];
        SensorManager.getRotationMatrix(tempRotation,null,accelerometerValue,magneticValue);
        SensorManager.getOrientation(tempRotation,tempValues);

        //toDegrees
        tempValues[0] = (float)Math.toDegrees(tempValues[0]);
        tempValues[1] = (float)Math.toDegrees(tempValues[1]);
        tempValues[2] = (float)Math.toDegrees(tempValues[2]);
        Log.i("tempValues[0]","tempValues[0] : " + tempValues[0]);

        //pitch < -80 代表手機於直立狀態, 改以z軸的旋轉作為航向角,但與原本相差180度
        if(tempValues[1] < -80)
            tempValues[0] = (float) Math.toDegrees(Math.atan2(tempRotation[2],tempRotation[5])) - 180;

        Log.i("Azimuth" ,"" + tempValues[0]);

        if(tempValues[0] >= -22 && tempValues[0] <22) {
        currentDirection = 0;
        Log.i("facingDirection", "北");
        facingDirectionText.setText(FACINGDIRETION + NORTH);

    }
    else if(tempValues[0] >= 22 && tempValues[0] <67)
    {
        currentDirection = 1;
        Log.i("facingDirection", "東北");
        facingDirectionText.setText(FACINGDIRETION + NORTHEAST);
    }
    else if(tempValues[0] >= 67 && tempValues[0] < 112){
        currentDirection = 2;
        Log.i("facingDirection", "東");
        facingDirectionText.setText(FACINGDIRETION + EAST);
    }
    else if(tempValues[0] >= 112 && tempValues[0] <157)
    {
        currentDirection = 3;
        Log.i("facingDirection", "東南");
        facingDirectionText.setText(FACINGDIRETION + SOUTHEAST);
    }
    else if((tempValues[0] >= 157 && tempValues[0] <= 180) || (tempValues[0]) >= -180 && tempValues[0] < -157){
        currentDirection = 4;
        Log.i("facingDirection", "南");
        facingDirectionText.setText(FACINGDIRETION + SOUTH);
    }
    else if(tempValues[0] >= -157 && tempValues[0] < -112)
    {
        currentDirection = 5;
        Log.i("facingDirection", "西南");
        facingDirectionText.setText(FACINGDIRETION + SOUTHWEST);
    }
    else if(tempValues[0] >= -112 && tempValues[0] < -67){
        currentDirection = 6;
        Log.i("facingDirection", "西");
        facingDirectionText.setText(FACINGDIRETION + WEST);
    }
    else if(tempValues[0] >= -67 && tempValues[0] < -22)
    {
        currentDirection = 7;
        Log.i("facingDirection", "西北");
        facingDirectionText.setText(FACINGDIRETION + NORTHWEST);
    }
        Log.i("currentDirection","currentDirection : " + currentDirection);

    }

    private void Language_SetUp()
    {
        Context appContext = GetApplicationContext.getAppContext();
        SharedPreferences languagePref = PreferenceManager.getDefaultSharedPreferences(appContext);
        language_option = languagePref.getString("language","繁體中文");
        directionList = new ArrayList<String>();
        if(language_option.equals("繁體中文"))
        {
            TITLE = "臺大雲林分院室內導航系統";
            initailDirectionImageTitle = "請面向圖中方向開始導航";
            //指令
            NOW_GO_STRAIGHT_RIGHTSIDE = "請靠右";
            NOW_GO_STRAIGHT_LEFTSIDE = "請靠左";
            NOW_GO_STRAIGHT = "請繼續直走";
            NOW_TURN_LEFT = "請向左轉後";
            NOW_TURN_RIGHT = "請向右轉後";
            NOW_TURN_FRONT_LEFT = "請向左前方轉後";
            NOW_TURN__FRONT_RIGHT = "請向右前方轉後";
            NOW_TURN_REAR_LEFT = "請向左後方轉後";
            NOW_TURN__REAR_RIGHT = "請向右後方轉後";
            NOW_TAKE_ELEVATOR = "離開電梯後";
            NOW_WALK_UP_STAIR = "上樓梯後";
            NOW_WALK_DOWN_STAIR = "下樓梯後";

            GO_STRAIGHT_ABOUT = "直走約";
            THEN_GO_STRAIGHT = "然後直走";
            THEN_GO_STRAIGHT_RIGHTSIDE = "然後靠右直走";
            THEN_GO_STRAIGHT_LEFTSIDE = "然後靠左直走";
            THEN_TURN_LEFT = "然後向左轉";
            THEN_TURN_RIGHT = "然後向右轉";
            THEN_TURN_FRONT_LEFT = "然後向左前方轉";
            THEN_TURN__FRONT_RIGHT = "然後向右前方轉";
            THEN_TURN_REAR_LEFT = "然後向左後方轉";
            THEN_TURN__REAR_RIGHT = "然後向右後方轉";
            THEN_TAKE_ELEVATOR = "然後搭電梯";
            THEN_WALK_UP_STAIR = "然後爬樓梯";
            THEN_WALK_DOWN_STAIR = "然後下樓梯";
            WAIT_FOR_ELEVATOR = "電梯中請稍候";
            WALKING_UP_STAIR = "爬樓梯";
            WALKING_DOWN_STAIR = "下樓梯";

            YOU_HAVE_ARRIVE = "抵達目的地";
            GET_LOST = "糟糕，你走錯路了";
            METERS = "公尺";
            PLEASE_GO_STRAIGHT = "請直走";
            PLEASE_GO_STRAIGHT_RIGHTSIDE = "請靠右直走";
            PLEASE_GO_STRAIGHT_LEFTSIDE = "請靠左直走";
            PLEASE_TURN_LEFT = "請左轉";
            PLEASE_TURN_RIGHT = "請右轉";
            PLEASE_TURN_FRONT_LEFT = "請向左前方轉";
            PLEASE_TURN__FRONT_RIGHT = "請向右前方轉";
            PLEASE_TURN_REAR_LEFT = "請向左後方轉";
            PLEASE_TURN__REAR_RIGHT = "請向右後方轉";
            PLEASE_TAKE_ELEVATOR = "請搭電梯";
            PLEASE_WALK_UP_STAIR = "請走樓梯";

            To = "至";
            toBasement = "至地下一樓";
            toFirstFloor = "至一樓";
            toSecondFloor = "至二樓";
            toThirdFloor = "至三樓";
            floor = "樓";


            PRESENT_POSITION = "目前位置：";
            DESTINATION = "目的地：";

            directionList.clear();
            NORTH = "北";
            directionList.add(NORTH);
            NORTHEAST = "東北";
            directionList.add(NORTHEAST);
            EAST = "東";
            directionList.add(EAST);
            SOUTHEAST = "東南";
            directionList.add(SOUTHEAST);
            SOUTH = "南";
            directionList.add(SOUTH);
            SOUTHWEST = "西南";
            directionList.add(SOUTHWEST);
            WEST = "西";
            directionList.add(WEST);
            NORTHWEST = "西北";
            directionList.add(NORTHWEST);
            PLEASEFACETO = "請面對";
            DIRECTION = "方";
            RIGHTDIRETION = "方向正確開始導航";
            FACINGDIRETION = "目前面對方向 : ";
        }
        else  if(language_option.equals("English"))
        {
            TITLE = "NTUH - Yunlin";
            initailDirectionImageTitle = "Please face to the direction in picture and start navigation.";
            //英文指令
            NOW_GO_STRAIGHT_RIGHTSIDE = "After keep right.";
            NOW_GO_STRAIGHT_LEFTSIDE = "After keep left.";
            NOW_GO_STRAIGHT = "Keep going straight.";
            NOW_TURN_LEFT = "After turn left.";
            NOW_TURN_RIGHT = "After turn right.";
            NOW_TURN_FRONT_LEFT = "After turn front left.";
            NOW_TURN__FRONT_RIGHT = "After turn front right.";
            NOW_TURN_REAR_LEFT = "After turn rear left.";
            NOW_TURN__REAR_RIGHT = "After turn rear right.";
            NOW_TAKE_ELEVATOR = "After take the elevator.";
            NOW_WALK_UP_STAIR = "After walk up stairs.";
            NOW_WALK_DOWN_STAIR = "After walk down stairs.";

            GO_STRAIGHT_ABOUT = "Go straight about";
            THEN_GO_STRAIGHT = "and go straight";
            THEN_GO_STRAIGHT_RIGHTSIDE = "and keep right";
            THEN_GO_STRAIGHT_LEFTSIDE = "and keep left";
            THEN_TURN_LEFT = "and turn left";
            THEN_TURN_RIGHT = "and turn right";
            THEN_TURN_FRONT_LEFT = "and turn front left";
            THEN_TURN__FRONT_RIGHT = "and turn front right";
            THEN_TURN_REAR_LEFT = "and turn rear left";
            THEN_TURN__REAR_RIGHT = "and turn rear right";
            THEN_TAKE_ELEVATOR = "and take the elevator";
            THEN_WALK_UP_STAIR = "and walk up stairs";
            THEN_WALK_DOWN_STAIR = "and walk down stairs";
            WAIT_FOR_ELEVATOR = "taking elevator";
            WALKING_UP_STAIR = "walk up stairs ";
            WALKING_DOWN_STAIR = "walk down stairs ";

            YOU_HAVE_ARRIVE = "You have arrived";
            GET_LOST = "Sorry, you get lost";
            METERS = "meters";
            PLEASE_GO_STRAIGHT = "Please go straight";
            PLEASE_GO_STRAIGHT_RIGHTSIDE = "Please keep right";
            PLEASE_GO_STRAIGHT_LEFTSIDE = "Please keep left";
            PLEASE_TURN_LEFT = "Please turn left";
            PLEASE_TURN_RIGHT = "Please turn right";
            PLEASE_TURN_FRONT_LEFT = "Please turn front left";
            PLEASE_TURN__FRONT_RIGHT = "Please turn front right";
            PLEASE_TURN_REAR_LEFT = "Please turn rear left";
            PLEASE_TURN__REAR_RIGHT = "Please turn rear right";
            PLEASE_TAKE_ELEVATOR = "Please take the elevator";
            PLEASE_WALK_UP_STAIR = "Please walk stairs";

            To = " to ";
            toBasement = "to B1";
            toFirstFloor = "to 1F";
            toSecondFloor = "to 2F";
            toThirdFloor = "to 3F";
            floor = "F";

            //
            PRESENT_POSITION = "Current location：";
            DESTINATION = "Destination：";

            directionList.clear();
            NORTH = "north";
            directionList.add(NORTH);
            NORTHEAST = "northeast";
            directionList.add(NORTHEAST);
            EAST = "east";
            directionList.add(EAST);
            SOUTHEAST = "eastsouth";
            directionList.add(SOUTHEAST);
            SOUTH = "south";
            directionList.add(SOUTH);
            SOUTHWEST = "southwest";
            directionList.add(SOUTHWEST);
            WEST = "west";
            directionList.add(WEST);
            NORTHWEST = "northwest";
            directionList.add(NORTHWEST);
            PLEASEFACETO = "Please face the ";
            DIRECTION = "direction";
            RIGHTDIRETION = "Start navigation!";
            FACINGDIRETION = "The current facing direction : ";
        }
    }

    private void loadAllWaypointData()
    {
        //Get all regions from graph
        navigationGraphForAllWaypoint = DataParser.getWaypointDataFromNavigationGraph(this, regionGraph.getAllRegionNames());

        //allWaypointData 是HashMap(所有Node資料)     navigationGraphForAllWaypoint(regionID)
        for (int i = 0; i < navigationGraphForAllWaypoint.size(); i++)
            allWaypointData.putAll(navigationGraphForAllWaypoint.get(i).nodesInSubgraph);

        //findLoc Setup
        findLoc.set_allWaypointData(allWaypointData);
        //Load all waypoint Parameter Data
        new DeviceParameter().setupDeviceParameter(this);
    }

    private void beaconManagerSetup()
    {
        Log.i("beaconManager", "beaconManagerSetup");

        //Beacon manager setup
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.unbind(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-15,i:16-19,i:20-23,p:24-24"));

        //setBeaconLayout("m:2-3=0215,i:4-19,i:20-23,i:24-27,p:28-28"));
        // Detect the Eddystone main identifier (UID) frame:
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));

        // Detect the Eddystone telemetry (TLM) frame:
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15"));

        // Detect the Eddystone URL frame:
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20"));

        //beaconManager.setForegroundScanPeriod(ONE_SECOND);
        //beaconManager.setForegroundBetweenScanPeriod(2*ONE_SECOND);


        beaconManager.setForegroundScanPeriod(50);
        beaconManager.setForegroundBetweenScanPeriod(0);

        beaconManager.removeAllMonitorNotifiers();
        beaconManager.removeAllRangeNotifiers();

        // Get the details for all the beacons we encounter.
        regionForBeacon = new org.altbeacon.beacon.Region("justGiveMeEverything", null, null, null);
        bluetoothManager = (BluetoothManager)
                getSystemService(Context.BLUETOOTH_SERVICE);
        //ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1001);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scanning for Beacons");
        Intent intent = new Intent(this, ARNavigationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification Channel ID",
                    "My Notification Name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("My Notification Channel Description");
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channel.getId());
        }
        beaconManager.enableForegroundServiceScanning(builder.build(), 456);
        beaconManager.setEnableScheduledScanJobs(false);
        beaconManager.setBackgroundBetweenScanPeriod(0);
        beaconManager.setBackgroundScanPeriod(1100);
        beaconManager.bind(ARNavigationActivity.this);
    }

    //-Initial AR, set up layout and other UI
    public void displayScene()
    {
        Log.i("display", "displayScene: ");
        mScene = new ARScene();
        // Add a listener to the scene so we can update the 'AR Initialized' text.
        mScene.setListener(new ARSceneListener(this, mViroView));
        // Add a light to the scene so our models show up
        mScene.getRootNode().addLight(new AmbientLight(Color.WHITE, 1000f));
        mViroView.setScene(mScene);
        //The list contains the Nodes which have placed the model
        placedNode = new ArrayList<>();
        placedID = new ArrayList<>();
        isOverFloor = false;
        //UI Setup
        View.inflate(this, R.layout.activity_ar_navigation, ((ViewGroup) mViroView));
        setTitle(TITLE);
        facingDirectionText = findViewById(R.id.facingDirection);
        destinationText = findViewById(R.id.destnation);
        destinationText.setText(DESTINATION + destinationName);
        currentLocationText = findViewById(R.id.curentLocation);
        currentLocationText.setText(PRESENT_POSITION);
        progressBar = findViewById(R.id.progressBar);
        progressNumber = findViewById(R.id.progressNumber);

        //SensorSetup
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(SensorListener,accelerometerSensor,Sensor.TYPE_ACCELEROMETER,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(SensorListener,magneticSensor,Sensor.TYPE_MAGNETIC_FIELD,SensorManager.SENSOR_DELAY_GAME);

        //LBeaconManager Setup
        beaconManagerSetup();

        // create a thread to handle the Lbeacon signal
        threadForHandleLbeaconID = new Thread(new ARNavigationActivity.NavigationThread());
        threadForHandleLbeaconID.start();


        //Handler for instruction display
        instructionHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                // receive a turn direction message from threadForHandleLbeaconID
                String turnDirection = (String) msg.obj;

                // distance to the next waypoint
                int distance = 0;

                // if there are two or more waypoints to go
                if (navigationPath.size() >= 2)
                    distance = (int) GeoCalulation.getDistance(navigationPath.get(0), navigationPath.get(1));
               
                navigationInstructionDisplay(turnDirection, distance);

            }
        };

        //Handler for setting current location on UI
        currentPositiontHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                currentLocationName = (String) msg.obj;
                currentLocationText.setText(PRESENT_POSITION + currentLocationName);
            }
        };

        //Handler for number of waypoint traveled in this navigation route
        walkedPointHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                int numberOfWaypointTraveled = (int) msg.obj;

                //If it is the first waypoint of travel of a region, meaning that
                //heading correction is needed
                if (numberOfWaypointTraveled == 1 && (navigationPath.size() >= 2))
                    turnNotificationForPopup = null;
            }
        };

        // the max value of prgress bar is set to the size of navigation path
        progressBar.setMax(navigationPath.size());

        progressHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                Boolean isMakingProgress = (Boolean) msg.obj;

                if (isMakingProgress == true) {

                    //progressStatus += 1;
                    progressStatus = walkedWaypoint;
                    progressBar.setProgress(progressStatus);
                    progressNumber.setText(progressStatus + "/" + progressBar.getMax());
                }
            }

        };
    }

    class NavigationThread implements Runnable
    {
        @Override
        public void run() {
            while (!navigationPath.isEmpty())
            {
                // the thread waits for beacon manager to notify it when a new Lbeacon ID is received
                synchronized (sync) {
                    try {
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // if the received ID matches the ID of the next waypoint in the navigation path
                if (navigationPath.get(0)._waypointID.equals(currentLBeaconID)) {
                    // three message objects send messages to corresponding handlers
                    Message messageFromInstructionHandler = instructionHandler.obtainMessage();
                    Message messageFromCurrentPositionHandler = currentPositiontHandler.obtainMessage();
                    Message messageFromWalkedPointHandler = walkedPointHandler.obtainMessage();
                    Message messageFromProgressHandler = progressHandler.obtainMessage();

                    // CurrentPositionHandler get the message of currently matched waypoint name
                    messageFromCurrentPositionHandler.obj = navigationPath.get(0)._waypointName;

                    // if the navigation path has more than three waypoints to travel
                    if (navigationPath.size() >= 3) {

                        // if the next two waypoints are in the same region as the current waypoint
                        // get the turn direction at the next waypoint
                        if (navigationPath.get(0)._regionID.equals(navigationPath.get(1)._regionID) &&
                                navigationPath.get(1)._regionID.equals(navigationPath.get(2)._regionID)) {
                            messageFromInstructionHandler.obj =
                                    getDirectionFromBearing(navigationPath.get(0),
                                            navigationPath.get(1), navigationPath.get(2));
                        }

                        // if the next two waypoints are not in the same region
                        // means that the next waypoint is the last waypoint of the region to travel
                        else if (!(navigationPath.get(1)._regionID.equals(navigationPath.get(2)._regionID))) {
                            messageFromInstructionHandler.obj = FRONT;
                        }

                        // if the current waypoint and the next waypoint are not in the same region
                        // transfer through elevator or stairwell
                        else if (!(navigationPath.get(0)._regionID.equals(navigationPath.get(1)._regionID))) {

                            if (navigationPath.get(0)._nodeType == ELEVATOR_WAYPOINT)
                                messageFromInstructionHandler.obj = ELEVATOR;
                            else if (navigationPath.get(0)._nodeType == STAIRWELL_WAYPOINT)
                                messageFromInstructionHandler.obj = STAIR;
                            else if ((navigationPath.get(0)._nodeType == CONNECTPOINT))
                                messageFromInstructionHandler.obj =
                                        getDirectionFromBearing(navigationPath.get(0),
                                                navigationPath.get(1), navigationPath.get(2));
                            else if (navigationPath.get(0)._nodeType == NORMAL_WAYPOINT) {

                                if (Setting.getPreferenceValue() == ELEVATOR_WAYPOINT)
                                    messageFromInstructionHandler.obj = ELEVATOR;
                                else if (Setting.getPreferenceValue() == STAIRWELL_WAYPOINT)
                                    messageFromInstructionHandler.obj = STAIR;
                            }

                        }
                    }
                    // if there are two waypoints left in the navigation path
                    else if (navigationPath.size() == 2) {

                        // if the current waypoint and the next waypoint are not in the same region
                        if (!(navigationPath.get(0)._regionID.equals(navigationPath.get(1)._regionID))) {

                            if (navigationPath.get(0)._nodeType == ELEVATOR_WAYPOINT)
                                messageFromInstructionHandler.obj = ELEVATOR;
                            else if (navigationPath.get(0)._nodeType == STAIRWELL_WAYPOINT)
                                messageFromInstructionHandler.obj = STAIR;
                        }
                        // else go straight to the final waypoint
                        else
                            messageFromInstructionHandler.obj = FRONT;

                    }
                    // if there is only one waypoint left, the user has arrived
                    else if (navigationPath.size() == 1)
                        messageFromInstructionHandler.obj = ARRIVED;


                    // every time the received ID is matched,
                    // the user is considered to travel one more waypoint

                    if(currentLBeaconID != lastRoundBeaconID)
                    {
                        walkedWaypoint++;
                        lastRoundBeaconID = currentLBeaconID;
                    }

                    // WalkedPointHandler get the message of number
                    //of waypoint has been traveled in a region
                    messageFromWalkedPointHandler.obj = walkedWaypoint;

                    messageFromProgressHandler.obj = true;

                    // send the newly updated message to three handlers
                    walkedPointHandler.sendMessage(messageFromWalkedPointHandler);
                    instructionHandler.sendMessage(messageFromInstructionHandler);
                    currentPositiontHandler.sendMessage(messageFromCurrentPositionHandler);
                    progressHandler.sendMessage(messageFromProgressHandler);
                }
                // if the received ID does not match the ID of waypoint in the navigation path
                else if (!(navigationPath.get(0)._waypointID.equals(currentLBeaconID))) {

                    // send a "wrong" message to the handler
                    Message messageFromInstructionHandler = instructionHandler.obtainMessage();
                    messageFromInstructionHandler.obj = WRONG;
                    instructionHandler.sendMessage(messageFromInstructionHandler);

                }
            }
        }
    }

    synchronized public void navigationInstructionDisplay(String turnDirection, int distance) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout));
        ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
        Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        final Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 25);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        Log.i("showPath","path(0) =  " + navigationPath.get(0)._waypointID);
        if (FirstTurn == true) {
            Log.i("InFirstTurn","in the first loop");
            Log.i("showPredictDireciton","PredictDirection = " + predictDirection);
            lastNode = navigationPath.get(0);
            if (navigationPath.size() == 2 && navigationPath.get(0)._elevation != navigationPath.get(1)._elevation)
            {
                Log.i("showPredictDireciton","PredictDirection = " + predictDirection);
                if(currentDirection != predictDirection)
                    Toast.makeText(ARNavigationActivity.this, PLEASEFACETO + toDirection(predictDirection), Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(ARNavigationActivity.this, RIGHTDIRETION, Toast.LENGTH_LONG).show();
                    turnDirection = STAIR;
                }
            }
            else if(turnDirection != STAIR && turnDirection != ELEVATOR && turnDirection != ARRIVED)
            {
                if(currentDirection != predictDirection)
                    Toast.makeText(ARNavigationActivity.this, PLEASEFACETO  + toDirection(predictDirection), Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(ARNavigationActivity.this, RIGHTDIRETION , Toast.LENGTH_LONG).show();
                    add3DObject("file:///android_asset/ARModel/Front.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                    FirstTurn = false;
                }
                //turnNotificationForPopup = null;
            }
        }
        //樓梯或電梯方向顯示
        if (navigationPath.size() >= 2 && !turnDirection.equals(WRONG))
            ShowDirectionFromConnectPoint();
        //Toast.makeText(ARNavigationActivity.this, "turnDirection = " + turnDirection , Toast.LENGTH_LONG).show();
        Log.i("showInstruction","turnNotification = " + turnNotificationForPopup
                                        + " turnDirection = " + turnDirection
                                        + " FirstTurn = " + FirstTurn
                                        + " PredictDirection = " + predictDirection
                                        + "Path (0) = " + navigationPath.get(0)._waypointName
                                        + "Path (1) = " + navigationPath.get(1)._waypointName);
            switch (turnDirection) {
                case LEFT:
                    firstMovement = GO_STRAIGHT_ABOUT;
                    if(navigationPath.size() > 1)
                        howFarToMove = distance + " " +METERS + To + navigationPath.get(1)._waypointName;
                    switch (navigationPath.get(1)._nodeType)
                    {
                        case ELEVATOR_WAYPOINT:
                        case STAIRWELL_WAYPOINT:
                            if(!navigationPath.get(1)._regionID.equals(navigationPath.get(2)._regionID))
                                nextFloor = navigationPath.get(2)._elevation;
                            break;
                        case NORMAL_WAYPOINT:
                            break;
                    }
                    if(!placedID.contains(currentLBeaconID)) {
                        if (currentDirection == predictDirection) {
                            if (turnNotificationForPopup != null)
                            {
                                Toast.makeText(ARNavigationActivity.this, RIGHTDIRETION, Toast.LENGTH_LONG).show();
                                showHintAtWaypoint(MAKETURN_NOTIFIER);
                                makeNextPreditDirection(turnNotificationForPopup);
                            }
                        } else {
                            Toast.makeText(ARNavigationActivity.this, PLEASEFACETO + toDirection(predictDirection), Toast.LENGTH_LONG).show();
                        }
                    }

                    turnNotificationForPopup = LEFT;
                    break;

                case FRONT_LEFT:
                    firstMovement = GO_STRAIGHT_ABOUT;
                    if(navigationPath.size() > 1)
                        howFarToMove = distance + " " +METERS + To + navigationPath.get(1)._waypointName;
                    switch (navigationPath.get(1)._nodeType)
                    {
                        case ELEVATOR_WAYPOINT:
                        case STAIRWELL_WAYPOINT:
                            if(!navigationPath.get(1)._regionID.equals(navigationPath.get(2)._regionID))
                                nextFloor = navigationPath.get(2)._elevation;
                            break;
                        case NORMAL_WAYPOINT:
                            break;
                    }
                    if(!placedID.contains(currentLBeaconID))
                    {
                        if(currentDirection == predictDirection)
                        {
                            if (turnNotificationForPopup != null)
                            {
                                Toast.makeText(ARNavigationActivity.this, RIGHTDIRETION , Toast.LENGTH_LONG).show();
                                showHintAtWaypoint(MAKETURN_NOTIFIER);
                                makeNextPreditDirection(turnNotificationForPopup);
                            }
                        } else {
                            Toast.makeText(ARNavigationActivity.this, PLEASEFACETO + toDirection(predictDirection), Toast.LENGTH_LONG).show();
                        }
                    }

                    turnNotificationForPopup = FRONT_LEFT;
                    break;

                case REAR_LEFT:
                    firstMovement = GO_STRAIGHT_ABOUT;
                    if(navigationPath.size() > 1)
                        howFarToMove = distance + " " +METERS + To + navigationPath.get(1)._waypointName;
                    switch (navigationPath.get(1)._nodeType)
                    {
                        case ELEVATOR_WAYPOINT:
                        case STAIRWELL_WAYPOINT:
                            if(!navigationPath.get(1)._regionID.equals(navigationPath.get(2)._regionID))
                                nextFloor = navigationPath.get(2)._elevation;
                            break;
                        case NORMAL_WAYPOINT:
                            break;
                    }
                    if(!placedID.contains(currentLBeaconID))
                    {
                        if(currentDirection == predictDirection)
                        {
                            if (turnNotificationForPopup != null)
                            {
                                Toast.makeText(ARNavigationActivity.this, RIGHTDIRETION, Toast.LENGTH_LONG).show();
                                showHintAtWaypoint(MAKETURN_NOTIFIER);
                                makeNextPreditDirection(turnNotificationForPopup);
                            }
                        } else {
                            Toast.makeText(ARNavigationActivity.this, PLEASEFACETO + toDirection(predictDirection), Toast.LENGTH_LONG).show();
                        }
                    }

                    turnNotificationForPopup = REAR_LEFT;
                    break;

                case RIGHT:
                    firstMovement = GO_STRAIGHT_ABOUT;
                    if(navigationPath.size() > 1)
                        howFarToMove = distance + " " + METERS + To + navigationPath.get(1)._waypointName;
                    switch (navigationPath.get(1)._nodeType)
                    {
                        case ELEVATOR_WAYPOINT:
                        case STAIRWELL_WAYPOINT:
                            if(!navigationPath.get(1)._regionID.equals(navigationPath.get(2)._regionID))
                                nextFloor = navigationPath.get(2)._elevation;
                            break;
                        case NORMAL_WAYPOINT:
                            break;
                    }
                    if(!placedID.contains(currentLBeaconID))
                    {
                        if(currentDirection == predictDirection)
                        {
                            if (turnNotificationForPopup != null)
                            {
                                Toast.makeText(ARNavigationActivity.this, RIGHTDIRETION, Toast.LENGTH_LONG).show();
                                showHintAtWaypoint(MAKETURN_NOTIFIER);
                                makeNextPreditDirection(turnNotificationForPopup);
                            }
                        } else {
                            Toast.makeText(ARNavigationActivity.this, PLEASEFACETO + toDirection(predictDirection), Toast.LENGTH_LONG).show();
                        }
                    }

                    turnNotificationForPopup = RIGHT;
                    break;

                case FRONT_RIGHT:
                    firstMovement = GO_STRAIGHT_ABOUT;
                    if(navigationPath.size() > 1)
                        howFarToMove = distance + " " +METERS + To + navigationPath.get(1)._waypointName;
                    switch (navigationPath.get(1)._nodeType)
                    {
                        case ELEVATOR_WAYPOINT:
                        case STAIRWELL_WAYPOINT:
                            if(!navigationPath.get(1)._regionID.equals(navigationPath.get(2)._regionID))
                                nextFloor = navigationPath.get(2)._elevation;
                            break;
                        case NORMAL_WAYPOINT:
                            break;
                    }
                    if(!placedID.contains(currentLBeaconID))
                    {
                        if(currentDirection == predictDirection)
                        {
                            if (turnNotificationForPopup != null)
                            {
                                Toast.makeText(ARNavigationActivity.this, RIGHTDIRETION , Toast.LENGTH_LONG).show();
                                showHintAtWaypoint(MAKETURN_NOTIFIER);
                                makeNextPreditDirection(turnNotificationForPopup);
                            }
                        }else {
                            Toast.makeText(ARNavigationActivity.this, PLEASEFACETO + toDirection(predictDirection), Toast.LENGTH_LONG).show();
                        }
                    }

                    turnNotificationForPopup = FRONT_RIGHT;
                    break;

                case REAR_RIGHT:
                    firstMovement = GO_STRAIGHT_ABOUT;
                    if(navigationPath.size() > 1)
                        howFarToMove = distance + " " +METERS + To + navigationPath.get(1)._waypointName;
                    switch (navigationPath.get(1)._nodeType)
                    {
                        case ELEVATOR_WAYPOINT:
                        case STAIRWELL_WAYPOINT:
                            if(!navigationPath.get(1)._regionID.equals(navigationPath.get(2)._regionID))
                                nextFloor = navigationPath.get(2)._elevation;
                            break;
                        case NORMAL_WAYPOINT:
                            break;
                    }
                    if(!placedID.contains(currentLBeaconID))
                    {
                        if(currentDirection == predictDirection)
                        {
                            if (turnNotificationForPopup != null)
                            {
                                Toast.makeText(ARNavigationActivity.this, RIGHTDIRETION , Toast.LENGTH_LONG).show();
                                showHintAtWaypoint(MAKETURN_NOTIFIER);
                                makeNextPreditDirection(turnNotificationForPopup);
                            }
                        } else {
                            Toast.makeText(ARNavigationActivity.this, PLEASEFACETO + toDirection(predictDirection), Toast.LENGTH_LONG).show();
                        }
                    }

                    turnNotificationForPopup = REAR_RIGHT;
                    break;

                case FRONT:
                    firstMovement = GO_STRAIGHT_ABOUT;
                    if(navigationPath.size() > 1)
                        howFarToMove = distance + " " +METERS + To + navigationPath.get(1)._waypointName;
                    switch (navigationPath.get(1)._nodeType)
                    {
                        case ELEVATOR_WAYPOINT:
                        case STAIRWELL_WAYPOINT:
                            if(!navigationPath.get(1)._regionID.equals(navigationPath.get(2)._regionID))
                                nextFloor = navigationPath.get(2)._elevation;
                            break;
                        case NORMAL_WAYPOINT:
                            break;
                    }
                    if(!placedID.contains(currentLBeaconID))
                    {
                        if(currentDirection == predictDirection)
                        {
                            if (turnNotificationForPopup != null) {
                                Toast.makeText(ARNavigationActivity.this, RIGHTDIRETION, Toast.LENGTH_LONG).show();
                                showHintAtWaypoint(MAKETURN_NOTIFIER);
                                makeNextPreditDirection(turnNotificationForPopup);
                            }
                            else
                                Toast.makeText(ARNavigationActivity.this, "turnNotification = null", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(ARNavigationActivity.this, PLEASEFACETO + toDirection(predictDirection), Toast.LENGTH_LONG).show();
                        }
                    }

                    turnNotificationForPopup = FRONT;
                    break;

                case FRONT_RIGHTSIDE:
                    firstMovement = GO_STRAIGHT_ABOUT;
                    if(navigationPath.size() > 1)
                        howFarToMove = distance + " " +METERS + To + navigationPath.get(1)._waypointName;
                    switch (navigationPath.get(1)._nodeType)
                    {
                        case ELEVATOR_WAYPOINT:
                        case STAIRWELL_WAYPOINT:
                            if(!navigationPath.get(1)._regionID.equals(navigationPath.get(2)._regionID))
                                nextFloor = navigationPath.get(2)._elevation;
                            break;
                        case NORMAL_WAYPOINT:
                            break;
                    }
                    if(!placedID.contains(currentLBeaconID))
                    {
                        if(currentDirection == predictDirection)
                        {
                            if (turnNotificationForPopup != null) {
                                Toast.makeText(ARNavigationActivity.this, RIGHTDIRETION , Toast.LENGTH_LONG).show();
                                showHintAtWaypoint(MAKETURN_NOTIFIER);
                                makeNextPreditDirection(turnNotificationForPopup);
                            }
                        }else {
                            Toast.makeText(ARNavigationActivity.this, PLEASEFACETO + toDirection(predictDirection), Toast.LENGTH_LONG).show();
                        }
                    }

                    turnNotificationForPopup = FRONT_RIGHTSIDE;
                    break;

                case FRONT_LEFTSIDE:
                    firstMovement = GO_STRAIGHT_ABOUT;
                    if(navigationPath.size() > 1)
                        howFarToMove = distance + " " +METERS + To + navigationPath.get(1)._waypointName;
                    switch (navigationPath.get(1)._nodeType)
                    {
                        case ELEVATOR_WAYPOINT:
                        case STAIRWELL_WAYPOINT:
                            if(!navigationPath.get(1)._regionID.equals(navigationPath.get(2)._regionID))
                                nextFloor = navigationPath.get(2)._elevation;
                            break;
                        case NORMAL_WAYPOINT:
                            break;
                    }
                    if(!placedID.contains(currentLBeaconID))
                    {
                        if(currentDirection == predictDirection)
                        {
                            if (turnNotificationForPopup != null) {
                                Toast.makeText(ARNavigationActivity.this, RIGHTDIRETION , Toast.LENGTH_LONG).show();
                                showHintAtWaypoint(MAKETURN_NOTIFIER);
                                makeNextPreditDirection(turnNotificationForPopup);
                            }
                        }else {
                            Toast.makeText(ARNavigationActivity.this, PLEASEFACETO + toDirection(predictDirection), Toast.LENGTH_LONG).show();
                        }
                    }

                    turnNotificationForPopup = FRONT_LEFTSIDE;
                    break;

                case STAIR:
                    turnNotificationForPopup = STAIR;
                    if(navigationPath.size() > 2)
                    {
                        nextFloor = navigationPath.get(2)._elevation;
                        //Up Stair
                        if(navigationPath.get(2)._elevation > navigationPath.get(0)._elevation)
                        {
                            if(!FirstTurn)
                            {
                                image.setImageResource(R.drawable.stairs_up);
                                toast.show();
                                myVibrator.vibrate(800);
                            }
                            switch (navigationPath.get(2)._elevation)
                            {
                                case 0:
                                    firstMovement = WALKING_UP_STAIR + toBasement;
                                    break;
                                case 1:
                                    firstMovement = WALKING_UP_STAIR + toFirstFloor;
                                    break;
                                case 2:
                                    firstMovement = WALKING_UP_STAIR + toSecondFloor;
                                    break;
                                case 3:
                                    firstMovement = WALKING_UP_STAIR + toThirdFloor;
                                    break;
                                case 4:
                                    firstMovement = WALKING_UP_STAIR + toFourthFloor;
                                    break;
                                case 5:
                                    firstMovement = WALKING_UP_STAIR + toFivthFloor;
                                    break;
                            }
                        }
                        //Down Stair
                        else if(navigationPath.get(2)._elevation < navigationPath.get(0)._elevation)
                        {
                            if(!FirstTurn)
                            {
                                image.setImageResource(R.drawable.stairs_down);
                                toast.show();
                                myVibrator.vibrate(800);
                            }
                            switch (navigationPath.get(2)._elevation)
                            {
                                case 0:
                                    firstMovement = WALKING_DOWN_STAIR + toBasement;
                                    break;
                                case 1:
                                    firstMovement = WALKING_DOWN_STAIR + toFirstFloor;
                                    break;
                                case 2:
                                    firstMovement = WALKING_DOWN_STAIR + toSecondFloor;
                                    break;
                                case 3:
                                    firstMovement = WALKING_DOWN_STAIR + toThirdFloor;
                                    break;
                                case 4:
                                    firstMovement = WALKING_DOWN_STAIR + toFourthFloor;
                                    break;
                                case 5:
                                    firstMovement = WALKING_DOWN_STAIR + toFivthFloor;
                                    break;
                            }
                        }
                    }
                    else if(navigationPath.size() == 2)
                    {
                        nextFloor = navigationPath.get(1)._elevation;
                        //Up Stair
                        if(navigationPath.get(1)._elevation > navigationPath.get(0)._elevation)
                        {
                            if(!FirstTurn)
                            {
                                image.setImageResource(R.drawable.stairs_up);
                                toast.show();
                                myVibrator.vibrate(800);
                            }
                            switch (navigationPath.get(1)._elevation)
                            {
                                case 0:
                                    firstMovement = WALKING_UP_STAIR + toBasement;
                                    break;
                                case 1:
                                    firstMovement = WALKING_UP_STAIR + toFirstFloor;
                                    break;
                                case 2:
                                    firstMovement = WALKING_UP_STAIR + toSecondFloor;
                                    break;
                                case 3:
                                    firstMovement = WALKING_UP_STAIR + toThirdFloor;
                                    break;
                                case 4:
                                    firstMovement = WALKING_UP_STAIR + toFourthFloor;
                                    break;
                                case 5:
                                    firstMovement = WALKING_UP_STAIR + toFivthFloor;
                                    break;
                            }
                        }
                        //Down Stair
                        else if(navigationPath.get(1)._elevation < navigationPath.get(0)._elevation)
                        {
                            if(!FirstTurn)
                            {
                                image.setImageResource(R.drawable.stairs_down);
                                toast.show();
                                myVibrator.vibrate(800);
                            }
                            switch (navigationPath.get(1)._elevation)
                            {
                                case 0:
                                    firstMovement = WALKING_DOWN_STAIR + toBasement;
                                    break;
                                case 1:
                                    firstMovement = WALKING_DOWN_STAIR + toFirstFloor;
                                    break;
                                case 2:
                                    firstMovement = WALKING_DOWN_STAIR + toSecondFloor;
                                    break;
                                case 3:
                                    firstMovement = WALKING_DOWN_STAIR + toThirdFloor;
                                    break;
                                case 4:
                                    firstMovement = WALKING_DOWN_STAIR + toFourthFloor;
                                    break;
                                case 5:
                                    firstMovement = WALKING_DOWN_STAIR + toFivthFloor;
                                    break;
                            }
                        }
                    }
                    howFarToMove= "";
                    //起點為樓梯
                    if(FirstTurn == true)
                    {
                        predictDirection = new ARInitialImage().getPredict(navigationPath.get(0)._waypointID, navigationPath.get(1)._waypointID);
                        if(currentDirection == predictDirection)
                        {
                            add3DObject("file:///android_asset/ARModel/Front.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                            FirstTurn = false;
                            //predictDirection =new ARInitialImage().getPredict(navigationPath.get(1)._waypointID, navigationPath.get(2)._waypointID);
                        }
                        else
                        {
                            Toast.makeText(ARNavigationActivity.this, PLEASE_WALK_UP_STAIR + "(" + toDirection(predictDirection) + ")", Toast.LENGTH_LONG).show();
                        }
                    }
                    if(!placedID.contains(currentLBeaconID))
                    {
                        Log.i("portal case","stair case,  currentLBeaconID" + currentLBeaconID);
                        if (turnNotificationForPopup != null && FirstTurn == false)
                        {
                            showHintAtWaypoint(MAKETURN_NOTIFIER);
                            walkedWaypoint = 0;
                            sourceID = navigationPath.get(1)._waypointID;
                            //predictDirection = new ARInitialImage().getPredict(navigationPath.get(1)._waypointID, navigationPath.get(2)._waypointID);
                        }
                    }
                    break;

                case ELEVATOR:
                    turnNotificationForPopup = ELEVATOR;
                    firstMovement = WAIT_FOR_ELEVATOR + To + navigationPath.get(1)._elevation + floor;
                    howFarToMove = "";
                    if(!FirstTurn)
                    {
                        image.setImageResource(R.drawable.elevator);
                        toast.show();
                        myVibrator.vibrate(800);
                    }
                    //predictDirection =new ARInitialImage().getPredict(navigationPath.get(1)._waypointID, navigationPath.get(2)._waypointID);
                    if(navigationPath.size() > 2)
                        nextFloor = navigationPath.get(2)._elevation;
                    else
                        nextFloor = navigationPath.get(1)._elevation;

                    //起點為電梯
                    if(FirstTurn == true)
                    {
                        predictDirection = new ARInitialImage().getPredict(navigationPath.get(0)._waypointID, navigationPath.get(1)._waypointID);
                        if(currentDirection == predictDirection)
                        {
                            add3DObject("file:///android_asset/ARModel/Front.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                            FirstTurn = false;
                            //predictDirection =new ARInitialImage().getPredict(navigationPath.get(1)._waypointID, navigationPath.get(2)._waypointID);
                        }
                        else
                        {
                            Toast.makeText(ARNavigationActivity.this, PLEASE_TAKE_ELEVATOR + "(" + toDirection(predictDirection) + ")", Toast.LENGTH_LONG).show();
                        }
                    }
                    if(!placedID.contains(currentLBeaconID))
                    {
                        Log.i("portal case","elevator case,  currentLBeaconID" + currentLBeaconID);
                        if (turnNotificationForPopup != null && FirstTurn == false)
                        {
                            showHintAtWaypoint(MAKETURN_NOTIFIER);
                            walkedWaypoint = 0;
                            predictDirection = new ARInitialImage().getPredict(navigationPath.get(1)._waypointID, navigationPath.get(2)._waypointID);
                        }
                    }
                    break;

                case ARRIVED:
                    firstMovement = " ";
                    howFarToMove = " ";
                    if(!placedID.contains(currentLBeaconID))
                    {
                        if (turnNotificationForPopup != null)
                            showHintAtWaypoint(ARRIVED_NOTIFIER);
                        walkedWaypoint = 0;
                    }
                    cleanPlacedNode(placedNode);
                    cleanPlacedID(placedID);
                    break;

                case WRONG:
                    List<Node> newPath = new ArrayList<>();
                    List<Node> wrongPath = new ArrayList<>();
                    cleanPlacedNode(placedNode);
                    cleanPlacedID(placedID);
                    wrongWaypoint = allWaypointData.get(currentLBeaconID);
                    currentLocationText.setText(PRESENT_POSITION + currentLocationName);

                    //WrongID = destination 直接改成ARRIVE
                    for (int i = 0; i < wrongWaypoint._attachIDs.size(); i++) {
                        if ((endNode._mainID != 0 && endNode._mainID == wrongWaypoint._attachIDs.get(i))) {
                            showHintAtWaypoint(ARRIVED_NOTIFIER);
                            arriveinwrong = true;
                            break;
                        }
                    }
                    if (wrongWaypoint._waypointID.equals(endNode._waypointID)) {
                        showHintAtWaypoint(ARRIVED_NOTIFIER);
                        arriveinwrong = true;
                    }
                    if (arriveinwrong == false) {
                        isLongerPath = false;
                        //If the last waypoint's neighbor contain current waypoint then JumpNode = false
                        JumpNode = true;
                        for (int i = 0; i < lastNode._adjacentWaypoints.size(); i++) {
                            if (lastNode._adjacentWaypoints.get(i).equals(wrongWaypoint._waypointID)) {
                                JumpNode = false;
                            }
                        }
                        //Find the lastNode in the wrong way
                        if (JumpNode == true) {
                            DirectCompute = true;
                            tmpdestinationID = destinationID;
                            tmpdestinationRegion = destinationRegion;

                            sourceID = startNode._waypointID;
                            sourceRegion = startNode._regionID;
                            destinationID = wrongWaypoint._waypointID;
                            destinationRegion = wrongWaypoint._regionID;
                            loadNavigationGraph();
                            wrongPath = startNavigation();

                            if (wrongPath.size() > 2)
                                lastNode = wrongPath.get(wrongPath.size() - 2);

                            Log.i("LastNode in wrong way", "wrongWay LastNode = " + lastNode._waypointName);
                            DirectCompute = false;
                            destinationID = tmpdestinationID;
                            destinationRegion = tmpdestinationRegion;
                        }

                        //Reroute
                        sourceID = wrongWaypoint._waypointID;
                        sourceRegion = wrongWaypoint._regionID;
                        loadNavigationGraph();
                        newPath = startNavigation();
                        for (int i = 0; i < newPath.size(); i++)
                            Log.i("NewPath", "path node " + newPath.get(i)._waypointName);
                        //Check if the path is longer than before
                        for (int i = 0; i < newPath.size(); i++) {
                            if (newPath.get(i)._waypointName.equals(lastNode._waypointName)) {
                                isLongerPath = true;
                                break;
                            }
                            isLongerPath = false;
                        }
                        if (isLongerPath) {
                            currentLBeaconID = "EmptyString";
                            navigationPath = newPath;
                            progressBar.setMax(navigationPath.size());
                            walkedWaypoint = 0;
                            progressStatus = 0;
                            firstMovement = "迴轉然後等待指示";
                            howFarToMove = "";
                            turnNotificationForPopup = "goback";
                            showHintAtWaypoint(MAKETURN_NOTIFIER);
                            predictDirection = new ARInitialImage().getPredict(navigationPath.get(0)._waypointID, navigationPath.get(1)._waypointID);
                            FirstTurn = true;
                            //turnNotificationForPopup = null;
                            Log.i("WrongPredict","" + predictDirection);
                            // TODO: 2020/3/22 maybe need to fix the predict direction
                        } else {
                            navigationPath = newPath;
                            progressBar.setMax(navigationPath.size());
                            walkedWaypoint = 1;
                            progressStatus = 1;
                            progressNumber.setText(progressStatus + "/" + progressBar.getMax());
                            firstMovement = GO_STRAIGHT_ABOUT;
                            turnNotificationForPopup = null;
                            predictDirection = new ARInitialImage().getPredict(lastNode._waypointID, navigationPath.get(0)._waypointID);
                            if (navigationPath.size() >= 2) {
                                howFarToMove = GeoCalulation.getDistance(navigationPath.get(0), navigationPath.get(1)) + " " + METERS + To + navigationPath.get(1)._waypointName;
                                turnNotificationForPopup = getDirectionFromBearing(lastNode, navigationPath.get(0), navigationPath.get(1));
                            }
                            currentLocationText.setText(PRESENT_POSITION + currentLocationName);
                            showHintAtWaypoint(MAKETURN_NOTIFIER);

                            if (navigationPath.size() >= 3) {
                                if (navigationPath.get(2)._elevation == navigationPath.get(1)._elevation)  //新路線在同一層樓
                                {
                                    //turnNotificationForPopup = getDirectionFromBearing(lastNode, navigationPath.get(0), navigationPath.get(1));
                                    turnNotificationForPopup = getDirectionFromBearing(navigationPath.get(0), navigationPath.get(1), navigationPath.get(2));//預先算下一點
                                    predictDirection = new ARInitialImage().getPredict(navigationPath.get(1)._waypointID, navigationPath.get(2)._waypointID);//預先算下一點預測方向
                                }
                                else
                                {
                                    nextFloor = navigationPath.get(2)._elevation;
                                    ShowDirectionFromConnectPoint();
                                }
                                //The current waypoint and next one is the same stair or elevator
                                if (navigationPath.get(0)._connectPointID != 0 && navigationPath.get(0)._connectPointID == navigationPath.get(1)._connectPointID)
                                {
                                    nextFloor = navigationPath.get(1)._elevation;
                                    switch (navigationPath.get(1)._nodeType)
                                    {
                                        case ELEVATOR_WAYPOINT:
                                            turnNotificationForPopup = ELEVATOR;
                                            firstMovement = WAIT_FOR_ELEVATOR + To + navigationPath.get(1)._elevation + floor;
                                            break;
                                        case STAIRWELL_WAYPOINT:
                                            turnNotificationForPopup = STAIR;
                                            if(navigationPath.size() > 2)
                                            {
                                                //Up Stair
                                                if (navigationPath.get(2)._elevation > navigationPath.get(0)._elevation)
                                                {
                                                    switch (navigationPath.get(2)._elevation)
                                                    {
                                                        case 0:
                                                            firstMovement = WALKING_UP_STAIR + toBasement;
                                                            break;
                                                        case 1:
                                                            firstMovement = WALKING_UP_STAIR + toFirstFloor;
                                                            break;
                                                        case 2:
                                                            firstMovement = WALKING_UP_STAIR + toSecondFloor;
                                                            break;
                                                        case 3:
                                                            firstMovement = WALKING_UP_STAIR + toThirdFloor;
                                                            break;
                                                        case 4:
                                                            firstMovement = WALKING_UP_STAIR + toFourthFloor;
                                                            break;
                                                        case 5:
                                                            firstMovement = WALKING_UP_STAIR + toFivthFloor;
                                                            break;

                                                    }
                                                }
                                                //Down Stair
                                                else if (navigationPath.get(2)._elevation < navigationPath.get(0)._elevation)
                                                {
                                                    switch (navigationPath.get(2)._elevation)
                                                    {
                                                        case 0:
                                                            firstMovement = WALKING_DOWN_STAIR + toBasement;
                                                            break;
                                                        case 1:
                                                            firstMovement = WALKING_DOWN_STAIR + toFirstFloor;
                                                            break;
                                                        case 2:
                                                            firstMovement = WALKING_DOWN_STAIR + toSecondFloor;
                                                            break;
                                                        case 3:
                                                            firstMovement = WALKING_DOWN_STAIR + toThirdFloor;
                                                            break;
                                                        case 4:
                                                            firstMovement = WALKING_DOWN_STAIR + toFourthFloor;
                                                            break;
                                                        case 5:
                                                            firstMovement = WALKING_DOWN_STAIR + toFivthFloor;
                                                    }
                                                }
                                            }
                                            else if(navigationPath.size() == 2)
                                            {
                                                //Up Stair
                                                if (navigationPath.get(1)._elevation > navigationPath.get(0)._elevation)
                                                {
                                                    switch (navigationPath.get(1)._elevation)
                                                    {
                                                        case 0:
                                                            firstMovement = WALKING_UP_STAIR + toBasement;
                                                            break;
                                                        case 1:
                                                            firstMovement = WALKING_UP_STAIR + toFirstFloor;
                                                            break;
                                                        case 2:
                                                            firstMovement = WALKING_UP_STAIR + toSecondFloor;
                                                            break;
                                                        case 3:
                                                            firstMovement = WALKING_UP_STAIR + toThirdFloor;
                                                            break;
                                                        case 4:
                                                            firstMovement = WALKING_UP_STAIR + toFourthFloor;
                                                            break;
                                                        case 5:
                                                            firstMovement = WALKING_UP_STAIR + toFivthFloor;
                                                            break;

                                                    }
                                                }
                                                //Down Stair
                                                else if (navigationPath.get(1)._elevation < navigationPath.get(0)._elevation)
                                                {
                                                    switch (navigationPath.get(1)._elevation)
                                                    {
                                                        case 0:
                                                            firstMovement = WALKING_DOWN_STAIR + toBasement;
                                                            break;
                                                        case 1:
                                                            firstMovement = WALKING_DOWN_STAIR + toFirstFloor;
                                                            break;
                                                        case 2:
                                                            firstMovement = WALKING_DOWN_STAIR + toSecondFloor;
                                                            break;
                                                        case 3:
                                                            firstMovement = WALKING_DOWN_STAIR + toThirdFloor;
                                                            break;
                                                        case 4:
                                                            firstMovement = WALKING_DOWN_STAIR + toFourthFloor;
                                                            break;
                                                        case 5:
                                                            firstMovement = WALKING_DOWN_STAIR + toFivthFloor;
                                                    }
                                                }
                                            }
                                            break;
                                        case NORMAL_WAYPOINT:
                                            break;
                                    }
                                    howFarToMove = "";
                                    if (turnNotificationForPopup != null)
                                        showHintAtWaypoint(MAKETURN_NOTIFIER);
                                    walkedWaypoint = 0;
                                    sourceID = navigationPath.get(1)._waypointID;
                                }
                            }
                            for (int i = 0; i < navigationPath.size(); i++) {
                                Log.i("recomputePath", "navigationPath(" + i + ") = " + navigationPath.get(i)._waypointName);
                            }
                            if(JumpNode == false)
                            {
                                CallDirectionInWrong = true;
                                ShowDirectionFromConnectPoint();
                            }
                            else
                            {
                                turnNotificationForPopup = getDirectionFromBearing(lastNode,navigationPath.get(0),navigationPath.get(1));
                                showHintAtWaypoint(MAKETURN_NOTIFIER);
                                //強制轉為直走
                                if(navigationPath.size() > 2) {
                                    turnNotificationForPopup = getDirectionFromBearing(navigationPath.get(0),navigationPath.get(1),navigationPath.get(2));
                                    if ((navigationPath.get(0)._adjacentWaypoints.size() <= 2 && navigationPath.get(1)._adjacentWaypoints.size() <= 2 && navigationPath.get(0)._elevation == lastNode._elevation) //只有兩個鄰居，且自己不在樓梯
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0606-000000000606") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("00000015-0000-0001-8500-000000019000")) //病歷室大廳->核子->樓梯
                                            || (navigationPath.get(0)._waypointID.equals("0x7b9812120x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("00000015-0000-0001-8500-000000019000")) //病歷室大廳2->核子->樓梯
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(1)._waypointID.equals("00000015-0000-0001-8500-000000019000") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0101-000000000101")) //核子->樓梯->新舊大樓連接走廊
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0909-000000000909") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("00000015-0000-0001-8500-000000019000")) //電腦斷層大廳->抽血->樓梯
                                            || (navigationPath.get(0)._waypointID.equals("0x7b9551660x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("00000015-0000-0001-8500-000000019000")) //電腦斷層大廳2->抽血->樓梯
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(1)._waypointID.equals("00000015-0000-0001-8500-000000019000") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0101-000000000101")) //抽血->樓梯->連接走廊
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0606-000000000606") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("00160015-0000-0000-0001-000000000001")) //病歷室大廳->核子->樓梯
                                            || (navigationPath.get(0)._waypointID.equals("0x7b9812120x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("00160015-0000-0000-0001-000000000001")) //病歷室大廳2->核子->樓梯
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(1)._waypointID.equals("00160015-0000-0000-0001-000000000001") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0101-000000000101")) //核子->樓梯->新舊大樓連接走廊
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0909-000000000909") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("00160015-0000-0000-0001-000000000001")) //電腦斷層大廳->抽血->樓梯
                                            || (navigationPath.get(0)._waypointID.equals("0x7b9551660x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("00160015-0000-0000-0001-000000000001")) //電腦斷層大廳2->抽血->樓梯
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(1)._waypointID.equals("00160015-0000-0000-0001-000000000001") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0101-000000000101")) //抽血->樓梯->連接走廊
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0606-000000000606") && navigationPath.get(2)._waypointID.equals("00170015-0000-0000-0001-000000000002")) //核子->病歷室大廳->後門出口
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(1)._waypointID.equals("0x7b9812120x00020000") && navigationPath.get(2)._waypointID.equals("00170015-0000-0000-0001-000000000002")) //核子->病歷室大廳2->後門出口
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0909-000000000909") && navigationPath.get(2)._waypointID.equals("00170015-0000-0000-0001-000000000002")) //抽血->電腦斷層大廳->後門出口
                                            || (navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(1)._waypointID.equals("0x7b9551660x00020000") && navigationPath.get(2)._waypointID.equals("00170015-0000-0000-0001-000000000002")) //抽血->電腦斷層大廳2->後門出口
                                    ) {
                                        Log.i("xxx_Slash", "強制轉為直走in wrong");
                                        turnNotificationForPopup = FRONT;
                                        LastisSlash = true;
                                    }
                                }
                            }
                            if(placedID.contains(navigationPath.get(0)._waypointID))
                            {
                                lastNode = navigationPath.get(0);
                                navigationPath.remove(0);
                                passedGroupID = navigationPath.get(0)._groupID;
                            }
                        }
                    }
                    break;
            }
        if(navigationPath.size() > 1)
        {
            if (!passedRegionID.equals(navigationPath.get(0)._regionID))
                regionIndex++;
            passedRegionID = navigationPath.get(0)._regionID;
            passedGroupID = navigationPath.get(0)._groupID;

            if(!turnDirection.equals(WRONG) && navigationPath.size() > 2 && lastNode._elevation == navigationPath.get(0)._elevation){
                if((navigationPath.get(0)._adjacentWaypoints.size() <= 2 && navigationPath.get(1)._adjacentWaypoints.size() <= 2 && navigationPath.get(0)._elevation == navigationPath.get(1)._elevation) //只有兩個鄰居，且自己不在樓梯
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0606-000000000606") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("00000015-0000-0001-8500-000000019000")) //病歷室大廳->核子->樓梯(靠新大樓)
                        ||(navigationPath.get(0)._waypointID.equals("0x7b9812120x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("00000015-0000-0001-8500-000000019000")) //病歷室大廳->核子->樓梯(靠新大樓)
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(1)._waypointID.equals("00000015-0000-0001-8500-000000019000") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0101-000000000101")) //核子->樓梯(靠新大樓)->新舊大樓連接走廊
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0909-000000000909") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("00000015-0000-0001-8500-000000019000")) //電腦斷層大廳->抽血->樓梯(靠新大樓)
                        ||(navigationPath.get(0)._waypointID.equals("0x7b9551660x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("00000015-0000-0001-8500-000000019000")) //電腦斷層大廳->抽血->樓梯(靠新大樓)
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(1)._waypointID.equals("00000015-0000-0001-8500-000000019000") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0101-000000000101")) //抽血->樓梯(靠新大樓)->連接走廊
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0606-000000000606") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("00160015-0000-0000-0001-000000000001")) //病歷室大廳->核子->樓梯(靠新大樓)
                        ||(navigationPath.get(0)._waypointID.equals("0x7b9812120x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("00160015-0000-0000-0001-000000000001")) //病歷室大廳->核子->樓梯(靠新大樓)
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(1)._waypointID.equals("00160015-0000-0000-0001-000000000001") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0101-000000000101")) //核子->樓梯->新舊大樓連接走廊
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0909-000000000909") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("00160015-0000-0000-0001-000000000001")) //電腦斷層大廳->抽血->樓梯
                        ||(navigationPath.get(0)._waypointID.equals("0x7b9551660x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("00160015-0000-0000-0001-000000000001")) //電腦斷層大廳->抽血->樓梯
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(1)._waypointID.equals("00160015-0000-0000-0001-000000000001") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0101-000000000101")) //抽血->樓梯->連接走廊
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0606-000000000606") && navigationPath.get(2)._waypointID.equals("00170015-0000-0000-0001-000000000002")) //核子->病歷室大廳->後門出口
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(1)._waypointID.equals("0x7b9812120x00020000") && navigationPath.get(2)._waypointID.equals("00170015-0000-0000-0001-000000000002")) //核子->病歷室大廳->後門出口
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0909-000000000909") && navigationPath.get(2)._waypointID.equals("00170015-0000-0000-0001-000000000002")) //抽血->電腦斷層大廳->後門出口
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0909-000000000909") && navigationPath.get(2)._waypointID.equals("00170015-0000-0000-0001-000000000002")) //抽血->電腦斷層大廳->後門出口
                        ||(navigationPath.get(0)._waypointID.equals("00170015-0000-0000-0001-000000000002") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0909-000000000909") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0505-000000000505")) //後門出口->電腦斷層大廳->抽血
                        ||(navigationPath.get(0)._waypointID.equals("00170015-0000-0000-0001-000000000002") && navigationPath.get(1)._waypointID.equals("0x7b9551660x00020000") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0505-000000000505")) //後門出口->電腦斷層大廳2->抽血
                        ||(navigationPath.get(0)._waypointID.equals("00170015-0000-0000-0001-000000000002") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0606-000000000606") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0404-000000000404")) //後門出口->病歷室大廳->核子醫學部
                        ||(navigationPath.get(0)._waypointID.equals("00170015-0000-0000-0001-000000000002") && navigationPath.get(1)._waypointID.equals("0x7b9812120x00020000") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0404-000000000404")) //後門出口->病歷室大廳2->核子醫學部
                        ||(navigationPath.get(0)._waypointID.equals("0x7b6913130x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0909-000000000909") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0505-000000000505")) //後門出口2->電腦斷層大廳->抽血
                        ||(navigationPath.get(0)._waypointID.equals("0x7b6913130x00020000") && navigationPath.get(1)._waypointID.equals("0x7b9551660x00020000") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0505-000000000505")) //後門出口2->電腦斷層大廳2->抽血
                        ||(navigationPath.get(0)._waypointID.equals("0x7b6913130x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0606-000000000606") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0404-000000000404")) //後門出口2->病歷室大廳->核子醫學部
                        ||(navigationPath.get(0)._waypointID.equals("0x7b6913130x00020000") && navigationPath.get(1)._waypointID.equals("0x7b9812120x00020000") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0404-000000000404")) //後門出口2->病歷室大廳2->核子醫學部
                        ||(navigationPath.get(0)._waypointID.equals("00170015-0000-0000-0001-000000000002") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0909-000000000909") && navigationPath.get(2)._waypointID.equals("00210016-0000-0000-0002-000000000003")) //後門出口->電腦斷層大廳->心臟血管樓梯
                        ||(navigationPath.get(0)._waypointID.equals("00170015-0000-0000-0001-000000000002") && navigationPath.get(1)._waypointID.equals("0x7b9551660x00020000") && navigationPath.get(2)._waypointID.equals("00210016-0000-0000-0002-000000000003")) //後門出口->電腦斷層大廳2->心臟血管樓梯
                        ||(navigationPath.get(0)._waypointID.equals("00170015-0000-0000-0001-000000000002") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0606-000000000606") && navigationPath.get(2)._waypointID.equals("00200016-0000-0000-0002-000000000002")) //後門出口->病歷室大廳->神經部樓梯
                        ||(navigationPath.get(0)._waypointID.equals("00170015-0000-0000-0001-000000000002") && navigationPath.get(1)._waypointID.equals("0x7b9812120x00020000") && navigationPath.get(2)._waypointID.equals("00200016-0000-0000-0002-000000000002")) //後門出口->病歷室大廳2->神經部樓梯
                        ||(navigationPath.get(0)._waypointID.equals("0x7b6913130x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0909-000000000909") && navigationPath.get(2)._waypointID.equals("00210016-0000-0000-0002-000000000003")) //後門出口2->電腦斷層大廳->心臟血管樓梯
                        ||(navigationPath.get(0)._waypointID.equals("0x7b6913130x00020000") && navigationPath.get(1)._waypointID.equals("0x7b9551660x00020000") && navigationPath.get(2)._waypointID.equals("00210016-0000-0000-0002-000000000003")) //後門出口2->電腦斷層大廳2->心臟血管樓梯
                        ||(navigationPath.get(0)._waypointID.equals("0x7b6913130x00020000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0606-000000000606") && navigationPath.get(2)._waypointID.equals("00200016-0000-0000-0002-000000000002")) //後門出口2->病歷室大廳->神經部樓梯
                        ||(navigationPath.get(0)._waypointID.equals("0x7b6913130x00020000") && navigationPath.get(1)._waypointID.equals("0x7b9812120x00020000") && navigationPath.get(2)._waypointID.equals("00200016-0000-0000-0002-000000000002")) //後門出口2->病歷室大廳2->神經部樓梯
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0001-8500-000000019000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0606-000000000606") && FirstTurn == true) //樓梯(靠新大樓)->核子->病歷室大廳
                        ||(navigationPath.get(0)._waypointID.equals("00160015-0000-0000-0001-000000000001") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0606-000000000606") && FirstTurn == true) //樓梯->核子->病歷室大廳
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0001-8500-000000019000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("0x7b9812120x00020000") && FirstTurn == true) //樓梯(靠新大樓)->核子->病歷室大廳
                        ||(navigationPath.get(0)._waypointID.equals("00160015-0000-0000-0001-000000000001") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0404-000000000404") && navigationPath.get(2)._waypointID.equals("0x7b9812120x00020000") && FirstTurn == true) //樓梯->核子->病歷室大廳
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0001-8500-000000019000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0909-000000000909") && FirstTurn == true) //樓梯(靠新大樓)->抽血->電腦斷層大廳
                        ||(navigationPath.get(0)._waypointID.equals("00160015-0000-0000-0001-000000000001") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("00000015-0000-0000-0909-000000000909") && FirstTurn == true) //樓梯->抽血->電腦斷層大廳
                        ||(navigationPath.get(0)._waypointID.equals("00000015-0000-0001-8500-000000019000") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("0x7b9551660x00020000") && FirstTurn == true) //樓梯(靠新大樓)->抽血->電腦斷層大廳
                        ||(navigationPath.get(0)._waypointID.equals("00160015-0000-0000-0001-000000000001") && navigationPath.get(1)._waypointID.equals("00000015-0000-0000-0505-000000000505") && navigationPath.get(2)._waypointID.equals("0x7b9551660x00020000") && FirstTurn == true) //樓梯->抽血->電腦斷層大廳
                ){
                    Log.i("xxx_Slash","強轉為直走");
                    turnNotificationForPopup = FRONT;
                    turnDirection = FRONT;
                    //showHintAtWaypoint(MAKETURN_NOTIFIER);
                    LastisSlash = true;
                }
            }
        }
        if(arriveinwrong == false && FirstTurn == false)
            readNavigationInstruction();

        if (!turnDirection.equals(WRONG)) {
            if(placedID.contains(navigationPath.get(0)._waypointID))
            {
                lastNode = navigationPath.get(0);
                navigationPath.remove(0);
            }
        }

        //FirstTurn = false;
        Find_Loc_pass = lastNode._waypointID;

    }

    public void readNavigationInstruction()
    {
        voiceEngine.speak(firstMovement+ howFarToMove, TextToSpeech.QUEUE_ADD, null);
    }

    public void ShowDirectionFromConnectPoint()
    {
        Log.i("Step","connect");
        Log.i("ConnectionStep","path[0].connecPointID:" + navigationPath.get(0)._connectPointID);
        if(navigationPath.get(0)._connectPointID == 0)
            isInVirtualNode = false;
        Log.i("ConnectionStep","isInVirtualNode:" + isInVirtualNode);
        //選擇的起始點不是目前位置
        if(chosestartNode._waypointID != navigationPath.get(0)._waypointID) {
            //收到的ConnectID != 0 目前與下個點的conectID相同，進入樓梯階段
            if (navigationPath.get(0)._connectPointID != 0 && navigationPath.get(1)._connectPointID == navigationPath.get(0)._connectPointID && isInVirtualNode == false) {
                Log.i("ConnectionStep","enter stair");
                Log.i("ConnectionStep","isInVirtualNodeInIF :" + isInVirtualNode);
                //判斷上下樓
                if(navigationPath.get(1)._elevation > navigationPath.get(0)._elevation) {
                    StairGoUp = true;
                }else{
                    StairGoUp = false;
                }//找到對應VirtualNode
                for (int i = 0; i < virtualNodeUp.size(); i++) {
                    if((virtualNodeDown.get(i)._connectPointID == navigationPath.get(0)._connectPointID)){
                        if(StairGoUp == true){
                            turnNotificationForPopup = getDirectionFromBearing(lastNode, navigationPath.get(0), virtualNodeDown.get(i));
                        }else {
                            turnNotificationForPopup = getDirectionFromBearing(lastNode, navigationPath.get(0), virtualNodeUp.get(i));
                        }
                        if(!placedID.contains(currentLBeaconID))
                        {
                            if(FirstTurn == false && CallDirectionInWrong == false)
                                showHintAtWaypoint(MAKETURN_NOTIFIER);
                            /*if(navigationPath.get(0)._nodeType == 1)
                                placedID.add(navigationPath.get(0)._waypointID);//雖然不放置模型，但仍需要紀錄為已放置過的點*/
                            isInVirtualNode = true;
                            Log.i("ConnectionStep","show on enter stair");
                        }

                    }
                }
            }//離開樓梯階段
            else if (navigationPath.get(0)._connectPointID != 0 && lastNode._connectPointID == navigationPath.get(0)._connectPointID && isInVirtualNode == true) {
                Log.i("ConnectionStep","out stair");
                Log.i("lastNodeOnVirtual","lastNode = " + lastNode._waypointID);
                //判斷上下樓
                if(navigationPath.get(0)._elevation > lastNode._elevation) {
                    StairGoUp = true;
                }else{
                    StairGoUp = false;
                }
                for (int i = 0; i < virtualNodeUp.size(); i++) {
                    if((virtualNodeDown.get(i)._connectPointID == navigationPath.get(0)._connectPointID)) {
                        cleanPlacedID(placedID);
                        cleanPlacedNode(placedNode);
                        if(StairGoUp == true){
                            turnNotificationForPopup = getDirectionFromBearing(virtualNodeUp.get(i), navigationPath.get(0), navigationPath.get(1));
                        }else {
                            turnNotificationForPopup = getDirectionFromBearing(virtualNodeDown.get(i), navigationPath.get(0), navigationPath.get(1));
                        }
                        predictDirection = new ARInitialImage().getPredict(virtualNodeDown.get(i)._waypointID,navigationPath.get(0)._waypointID);
                    }
                }
            }
        }//選擇起始點是目前位置，且進入樓梯
        else if(chosestartNode._connectPointID != 0 && chosestartNode._waypointID == navigationPath.get(0)._waypointID && navigationPath.get(0)._connectPointID == navigationPath.get(1)._connectPointID) {
            Log.i("ConnectionStep","enter stair II");
            //判斷是往上或往下走
            if(navigationPath.get(1)._elevation > navigationPath.get(0)._elevation) {
                StairGoUp = true;
            }else{
                StairGoUp = false;
            }
            for (int i = 0; i < virtualNodeUp.size(); i++) {
                //找到與目前位置相同ConnectID
                if (virtualNodeDown.get(i)._connectPointID == navigationPath.get(0)._connectPointID && isInVirtualNode == false) {
                    if(StairGoUp == true){
                        turnNotificationForPopup = getDirectionFromBearing(lastNode, navigationPath.get(0), virtualNodeDown.get(i));
                    }else{
                        turnNotificationForPopup = getDirectionFromBearing(lastNode, navigationPath.get(0), virtualNodeUp.get(i));
                    }
                    if(!placedID.contains(currentLBeaconID))
                    {
                        if(FirstTurn == false && CallDirectionInWrong == false)//電梯因有方向問題不顯示進樓梯之虛擬點
                        {
                            showHintAtWaypoint(MAKETURN_NOTIFIER);
                            Log.i("ConnectionStep","show on enter stair II");
                        }
                        /*if(navigationPath.get(0)._nodeType == 1)
                            placedID.add(navigationPath.get(0)._waypointID);//雖然不放置模型，但仍需要紀錄為已放置過的點*/
                        isInVirtualNode = true;
                    }
                }
            }
        }
        CallDirectionInWrong = false;
    }

    public void showHintAtWaypoint(final int instruction)
    {
        Log.i("Step","show");
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout));
        ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
        final Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 25);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        // distance determine showhint time
        int DistanceForShowHint = 0;
        // if there are two or more waypoints to go
        //依距離顯示時間測試->到達的點會先被Remove掉
        if(navigationPath.size()>=2)
            DistanceForShowHint = (int) GeoCalulation.getDistance(navigationPath.get(0), navigationPath.get(1));
        String turnDirection = null;

        if(instruction == ARRIVED_NOTIFIER)
        {
            appendLog("抵達" + destinationName);
            turnDirection = YOU_HAVE_ARRIVE;
            image.setImageResource(R.drawable.arrived_1);
            voiceEngine.speak(turnDirection, TextToSpeech.QUEUE_ADD, null);
            toast.show();
            myVibrator.vibrate(800);
            beaconManager.removeAllMonitorNotifiers();
            beaconManager.removeAllRangeNotifiers();
            beaconManager.unbind(ARNavigationActivity.this);
            //-----------------------
            Intent i = new Intent(ARNavigationActivity.this, MainActivity.class);
            i.putExtra("Arrived_flag", 1);
            startActivity(i);
            finish();
        }
        else if (instruction == WRONGWAY_NOTIFIER)
        {
            appendLog("抵達" + navigationPath.get(0)._waypointName);
            turnDirection = "正在幫您重新計算路線";
            //tts.speak(turnDirection, TextToSpeech.QUEUE_ADD, null);
            Log.i("xxx_wrong", "wrongway");
            image.setImageResource(R.drawable.refresh);
            toast.show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 1000);
            myVibrator.vibrate(1000);
        }
        else if (instruction == MAKETURN_NOTIFIER)
        {
            appendLog("抵達" + navigationPath.get(0)._waypointName);
            switch (turnNotificationForPopup)
            {
                case RIGHT:
                    turnDirection = PLEASE_TURN_RIGHT;
                    add3DObject("file:///android_asset/ARModel/Right.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                    LastisSlash = false;
                    Log.i("Display Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    break;
                case LEFT:
                    turnDirection = PLEASE_TURN_LEFT;
                    add3DObject("file:///android_asset/ARModel/Left.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                    LastisSlash = false;
                    Log.i("Display Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    break;
                case FRONT_RIGHT:
                    turnDirection = PLEASE_TURN__FRONT_RIGHT;
                    if (LastisSlash == false) {
                        add3DObject("file:///android_asset/ARModel/RightFront.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                        LastisSlash = true;
                    } else {
                        add3DObject("file:///android_asset/ARModel/Front.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                        LastisSlash = false;
                    }
                    Log.i("Display Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    break;
                case FRONT_LEFT:
                    turnDirection = PLEASE_TURN_FRONT_LEFT;
                    if (LastisSlash == false) {
                        add3DObject("file:///android_asset/ARModel/LeftFront.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                        LastisSlash = true;
                    } else {
                        add3DObject("file:///android_asset/ARModel/Front.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                        LastisSlash = false;
                    }
                    Log.i("Display Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    break;

                case REAR_RIGHT:
                    turnDirection = PLEASE_TURN__REAR_RIGHT;
                    add3DObject("file:///android_asset/ARModel/R-back.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                    LastisSlash = false;
                    Log.i("Display Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    break;
                case REAR_LEFT:
                    turnDirection = PLEASE_TURN_REAR_LEFT;
                    add3DObject("file:///android_asset/ARModel/L-back.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                    LastisSlash = false;
                    Log.i("Display Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    break;
                case FRONT:
                    turnDirection = PLEASE_GO_STRAIGHT;
                    add3DObject("file:///android_asset/ARModel/Front.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                    LastisSlash = false;
                    Log.i("Display Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    break;
                case FRONT_RIGHTSIDE:
                    turnDirection = PLEASE_GO_STRAIGHT_RIGHTSIDE;
                    if (LastisSlash == false) {
                        add3DObject("file:///android_asset/ARModel/RightSite.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                        LastisSlash = true;
                    } else {
                        add3DObject("file:///android_asset/ARModel/Front.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                        LastisSlash = false;
                    }
                    Log.i("Display Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    break;
                case FRONT_LEFTSIDE:
                    turnDirection = PLEASE_GO_STRAIGHT_LEFTSIDE;
                    if (LastisSlash == false) {
                        add3DObject("file:///android_asset/ARModel/LeftSite.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                        LastisSlash = true;
                    } else {
                        add3DObject("file:///android_asset/ARModel/Front.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                        LastisSlash = false;
                    }
                    Log.i("Display Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    break;
                case STAIR:
                    turnDirection = PLEASE_WALK_UP_STAIR;
                    // TODO: 2020/4/10 cancel the 3D model
                    //add3DObject(turnDirection,mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                    placedID.add(navigationPath.get(0)._waypointID);
                    LastisSlash = false;
                    Log.i("Display Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    break;
                case ELEVATOR:
                    turnDirection = PLEASE_TAKE_ELEVATOR;
                    // TODO: 2020/4/10 cancel the 3D model 
                    //add3DObject(turnDirection,mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                    placedID.add(navigationPath.get(0)._waypointID);
                    LastisSlash = false;
                    Log.i("xxx_Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    break;
                case "goback":
                    turnDirection = " ";
                    image.setImageResource(R.drawable.turn_back);
                    toast.show();
                    myVibrator.vibrate(800);
                    //add3DObject("file:///android_asset/ARModel/R-back.obj",mViroView.getLastCameraPositionRealtime().add(mViroView.getLastCameraForwardRealtime()),navigationPath.get(0)._waypointID);
                    Log.i("xxx_Direction", "跳出指令方向 = " + turnNotificationForPopup);
                    // setNowPostition();
                    break;
            }
        }
    }

    private void add3DObject(String fileName, Vector position, String waypointID)
    {
        if(isOverFloor == true)
        {
            cleanPlacedNode(placedNode);
            cleanPlacedID(placedID);
        }

        Log.i("Step","add3Dmodel");
        ARNode node = mScene.createAnchoredNode(position.add(new Vector(0,-0.3,0)));
        if(fileName == PLEASE_WALK_UP_STAIR)
        {
            isOverFloor = true;
            //UpStair
            if(nextFloor > navigationPath.get(0)._elevation)
            {
                fileName = "file:///android_asset/ARModel/upStair.obj";
            }
            //DownStair
            else if(nextFloor < navigationPath.get(0)._elevation)
            {
                fileName = "file:///android_asset/ARModel/downStair.obj";
            }
        }
        else if(fileName == PLEASE_TAKE_ELEVATOR)
        {
            isOverFloor = true;
            if(nextFloor > navigationPath.get(0)._elevation)
            {
                fileName = "file:///android_asset/ARModel/upElevator.obj";
            }
            //DownStair
            else if(nextFloor < navigationPath.get(0)._elevation)
            {
                fileName = "file:///android_asset/ARModel/downElevator.obj";
            }

        }

        final Object3D object3D =new Object3D();
        object3D.setScale(new Vector(1f,1f,1f));
        object3D.setRotation(mViroView.getLastCameraRotationEulerRealtime());
        object3D.loadModel(mViroView.getViroContext(), Uri.parse(fileName), Object3D.Type.OBJ, new AsyncObject3DListener() {
            @Override
            public void onObject3DLoaded(Object3D object3D, Object3D.Type type) {
                Log.i("3Dload","Successfully loaded the model");
            }

            @Override
            public void onObject3DFailed(String s) {
                Log.i("3Dload","Error loaded the model");
                Toast.makeText(ARNavigationActivity.this,"An error occured when loading the 3D Object",Toast.LENGTH_LONG).show();
            }
        });

        if(object3D != null)

        {
            node.addChildNode(object3D);
            //showTheDistance(position.add(new Vector(0,-0.3,0)));
            placedNode.add(node);
            placedID.add(waypointID);
        }
    }

    //清除所有放置的模型
    public void cleanPlacedNode(List<ARNode> nodes)
    {
        for(int i = 0; i < nodes.size(); i++)
        {
            nodes.get(i).detach();
        }
        nodes.clear();
        isOverFloor = false;
    }

    //清除所有已放置模型的waypoint紀錄
    public void cleanPlacedID(List<String> list)
    {
        list.clear();
    }

    //顯示模型到相機距離
    public void showTheDistance(Vector position)
    {
        Vector cameraPostition = mViroView.getLastCameraPositionRealtime();
        float distance = cameraPostition.distance(position);
        distance = (float)(Math.round(distance * 1000.0) / 1000.0);
        Toast.makeText(ARNavigationActivity.this,"Distance = " + distance,Toast.LENGTH_LONG).show();
    }

    public void appendLog(String text)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss - ");
        Date date = new Date(System.currentTimeMillis());
        simpleDateFormat.format(date);
        File logFile = new File("sdcard/ARArrivelogfile.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            Writer buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile,true),"UTF-8"));
            buf.append( simpleDateFormat.format(date).toString());
            buf.append(text + "\n");
            buf.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //數字與方位對照
    public String toDirection(int num)
    {
        String returnString = "";
        switch (num)
        {
            case 0:
                returnString = NORTH;
                break;
            case 1:
                returnString = NORTHEAST;
                break;
            case 2:
                returnString = EAST;
                break;
            case 3:
                returnString = SOUTHEAST;
                break;
            case 4:
                returnString = SOUTH;
                break;
            case 5:
                returnString = SOUTHWEST;
                break;
            case 6:
                returnString = WEST;
                break;
            case 7:
                returnString = NORTHWEST;
                break;
        }
        return returnString + DIRECTION;
    }

    public void makeNextPreditDirection(String turnNotificationForPopup)
    {
        switch (turnNotificationForPopup)
        {
            case RIGHT:
                predictDirection = (predictDirection + 2) % 8;
                break;
            case LEFT:
                predictDirection = (predictDirection + 6) % 8;
                break;
            case FRONT_RIGHT:
                predictDirection = (predictDirection + 1) % 8;
                break;
            case FRONT_LEFT:
                predictDirection = (predictDirection + 7) % 8;
                break;
            case REAR_RIGHT:
                predictDirection = (predictDirection + 3) % 8;
                break;
            case REAR_LEFT:
                predictDirection = (predictDirection + 5) % 8;
                break;
            case FRONT:
                predictDirection = (predictDirection + 0) % 8;
                break;
            case FRONT_RIGHTSIDE:
                predictDirection = (predictDirection + 0) % 8;
                break;
            case FRONT_LEFTSIDE:
                predictDirection = (predictDirection + 0) % 8;
                break;
            /*case STAIR:
                // TODO: 2020/4/11 matbe need to fix the predic direction
                predictDirection = (predictDirection + 0) % 8;
                break;
            case ELEVATOR:
                // TODO: 2020/4/11 matbe need to fix the predic direction
                predictDirection = (predictDirection + 0) % 8;
                break;*/
            case "goback":
                predictDirection = (predictDirection + 4) % 8;
                break;
        }
    }
}
