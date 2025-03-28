//package com.example.rtoexamdrivingtheory.hazardTest;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Point;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.util.Log;
//import android.view.Display;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//import android.widget.Switch;
//import android.widget.TextView;
//
//import com.example.rtoexamdrivingtheory.hazardTest.adapter.FlagAdapter;
//import com.example.rtoexamdrivingtheory.hazardTest.dialog.DialogHazardTestResult;
//import com.example.rtoexamdrivingtheory.model.FlagModel;
//import com.example.rtoexamdrivingtheory.model.HazardTestModel;
//import com.example.rtoexamdrivingtheory.R;
//import com.example.rtoexamdrivingtheory.utils.AssetsUtil;
//import com.example.rtoexamdrivingtheory.utils.WindowUtils;
////import com.google.android.exoplayer2.ExoPlayer;
////import com.google.android.exoplayer2.ExoPlayerFactory;
////import com.google.android.exoplayer2.Player;
////import com.google.android.exoplayer2.SimpleExoPlayer;
////import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
////import com.google.android.exoplayer2.source.ExtractorMediaSource;
////import com.google.android.exoplayer2.ui.PlayerView;
////import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
////import com.google.android.exoplayer2.util.Util;
//import com.google.android.exoplayer2.ExoPlayer;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
//import com.google.android.exoplayer2.ui.PlayerView;
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
//import com.google.android.exoplayer2.util.Util;
//import com.rey.material.widget.RelativeLayout;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//public class HazardTestClipActivity extends AppCompatActivity implements View.OnClickListener {
//
//    PlayerView videoView;
//    RecyclerView rcvFlag;
//    FlagAdapter flagAdapter;
//    private Activity activity;
//    private ImageView ivBack;
//    LinearLayout lnGuideView;
//    TextView tvCount;
//    RelativeLayout rltClick;
//    private SimpleExoPlayer exoPlayer;
//    ArrayList<HazardTestModel> listOfHazardTest = new ArrayList<>();
//    private String TAG = "HazardTestClipActivity";
//    private PlaybackStateListener playbackStateListener;
//    private boolean isCountDownFinish = false;
//    TextView tvGuide;
//    int initVideoPosition = 0;
//    private boolean failedInTest = false;
//    private TextView tvDescription, tvScoreTop;
//    RelativeLayout rltSeekBarResult, rltSeekBarFlag, rltControl, rltTakeTestAgain;
//    android.widget.RelativeLayout rltSeekBar;
//    SeekBar videoSeekBar;
//    Switch scoreSwitch;
//    ImageView ivPlay;
//    TextView tvPlay;
//    TextView tvScoredBottom;
//    private boolean stateEnded = false;
//    private Handler videoSeekHandler = new Handler();
//    private boolean isResultShown = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_hazard_test_clip);
//
//        activity = HazardTestClipActivity.this;
//
//        WindowUtils.setStatusBarLight(activity, R.color.grey10);
//
//        initVideoPosition = 0;
//
//        initView();
//
//        setFlagRcv();
//
//        getJsonData();
//
//        showGuideView();
//
//        initVideoPlayer();
//
//    }
//
//    private void setFlagRcv() {
//        flagAdapter = new FlagAdapter(activity);
//        rcvFlag.setAdapter(flagAdapter);
//    }
//
//    private void getJsonData() {
//        int index = getIntent().getIntExtra("index", 1);
//        int rawResource = R.raw.test1;
//        if (index == 1) {
//            rawResource = R.raw.test1;
//        } else if (index == 2) {
//            rawResource = R.raw.test2;
//        } else if (index == 3) {
//            rawResource = R.raw.test3;
//        } else if (index == 4) {
//            rawResource = R.raw.test4;
//        } else if (index == 5) {
//            rawResource = R.raw.test5;
//        } else if (index == 6) {
//            rawResource = R.raw.test6;
//        } else if (index == 7) {
//            rawResource = R.raw.test7;
//        } else if (index == 8) {
//            rawResource = R.raw.test8;
//        } else if (index == 9) {
//            rawResource = R.raw.test9;
//        } else if (index == 10) {
//            rawResource = R.raw.test10;
//        }
//        String object = AssetsUtil.openRawResource(activity, rawResource);
//        try {
//            listOfHazardTest.clear();
//            JSONArray jsonArray = new JSONArray(object);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                HazardTestModel hazardTestModel = new HazardTestModel();
//
//                JSONObject dataObject = jsonArray.getJSONObject(i);
//
//                ArrayList<HazardTestModel.Hazards> listOfHazards = new ArrayList<>();
//                JSONArray hazardArray = dataObject.getJSONArray("hazards");
//                for (int j = 0; j < hazardArray.length(); j++) {
//                    HazardTestModel.Hazards hazards = new HazardTestModel.Hazards();
//                    hazards.setStart(hazardArray.getJSONObject(j).getString("start"));
//                    hazards.setEnd(hazardArray.getJSONObject(j).getString("end"));
//
//                    listOfHazards.add(hazards);
//                }
//
//                hazardTestModel.setId(dataObject.getString("id"));
//                hazardTestModel.setVideoUrl(dataObject.getString("mp4"));
//                hazardTestModel.setDescription(dataObject.getString("title"));
//                hazardTestModel.setDescription(dataObject.getString("description"));
//                hazardTestModel.setListOfHazards(listOfHazards);
//
//                listOfHazardTest.add(hazardTestModel);
//            }
//        } catch (JSONException e) {
//            Log.e(TAG, "getJsonData: ", e);
//        }
//    }
//
//    private void showGuideView() {
//        findViewById(R.id.loadignView).setVisibility(View.GONE);
//
//        lnGuideView.setVisibility(View.VISIBLE);
//        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
//        aniFade.setDuration(1000);
//        lnGuideView.setAnimation(aniFade);
//
//        tvCount.setVisibility(View.GONE);
//        rltClick.setVisibility(View.GONE);
//
//        tvGuide.setVisibility(View.GONE);
//
//        tvDescription.setVisibility(View.GONE);
//
//        tvScoreTop.setVisibility(View.GONE);
//
//        rltSeekBar.setVisibility(View.GONE);
//
//        rcvFlag.setVisibility(View.VISIBLE);
//        if (flagAdapter != null) flagAdapter.clearFlags();
//
//        rltSeekBar.setVisibility(View.GONE);
//
//        rltControl.setVisibility(View.GONE);
//
//        rltTakeTestAgain.setVisibility(View.GONE);
//    }
//
//    private void startTvCount() {
//        lnGuideView.setVisibility(View.GONE);
//        tvCount.setVisibility(View.VISIBLE);
//
//        CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
//                aniFade.setDuration(500);
//                tvCount.setAnimation(aniFade);
//                if ((millisUntilFinished / 1000) == 0) {
//                    tvCount.setText("Go!");
//                    return;
//                }
//                tvCount.setText("" + millisUntilFinished / 1000);
//            }
//
//            public void onFinish() {
//                rltClick.setVisibility(View.VISIBLE);
//                tvCount.setVisibility(View.GONE);
//                isCountDownFinish = true;
//                if (exoPlayer != null) exoPlayer.seekTo(0);
//                ivPlay.setImageResource(R.drawable.ic_play);
//                tvPlay.setText(getResources().getString(R.string.play));
//                tvGuide.setVisibility(View.VISIBLE);
//                findViewById(R.id.loadignView).setVisibility(View.VISIBLE);
//                rltSeekBarFlag.removeAllViews();
//                scoreSwitch.setChecked(false);
//                playVideo();
//            }
//        };
//        countDownTimer.start();
//
//    }
//
//    private void initVideoPlayer() {
//        exoPlayer = ExoPlayerFactory.newSimpleInstance(activity);
//        playbackStateListener = new PlaybackStateListener();
//        exoPlayer.addListener(playbackStateListener);
//
//        videoView.setPlayer(exoPlayer);
//        setVideoInfo(initVideoPosition);
//    }
//
//    private void setVideoInfo(int position) {
//        String userAgent = Util.getUserAgent(activity, "Offline Player");
//        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(activity, userAgent))
//                .setExtractorsFactory(new DefaultExtractorsFactory())
//                .createMediaSource(Uri.parse(listOfHazardTest.get(position).getVideoUrl()));
//        exoPlayer.prepare(mediaSource);
//        videoSeekBar.setMax((int) (exoPlayer.getDuration() / 1000));
//        playVideo();
//    }
//
//    private void playVideo() {
//        if (isCountDownFinish && exoPlayer != null) {
//            exoPlayer.setPlayWhenReady(true);
//        } else {
//            if (exoPlayer != null) exoPlayer.setPlayWhenReady(false);
//        }
//    }
//
//    private void videoEnd() {
//        String result = getResources().getString(R.string.scoreStringFront) + " " + countResult() + "" + getResources().getString(R.string.scoreStringEnd);
//
//        if (!isResultShown) {
//            showResultDialog(result, getResources().getString(R.string.result), getResources().getString(R.string.text_cancel), new DialogHazardTestResult.OnDialogDismissListener() {
//                @Override
//                public void onResultListener() {
//                    showResult();
//                }
//            });
//        }
//
//    }
//
//    private void showResult() {
//        isResultShown = true;
//        tvGuide.setVisibility(View.GONE);
//        tvDescription.setVisibility(View.VISIBLE);
//        tvDescription.setText(listOfHazardTest.get(initVideoPosition).getDescription());
//
//        tvScoreTop.setVisibility(View.VISIBLE);
//        tvScoreTop.setText(activity.getResources().getString(R.string.you_scored) + " " + countResult());
//
//        rltSeekBar.setVisibility(View.VISIBLE);
//
//        rcvFlag.setVisibility(View.GONE);
//
//        rltSeekBar.setVisibility(View.VISIBLE);
//
//        rltControl.setVisibility(View.VISIBLE);
//
//        rltTakeTestAgain.setVisibility(View.VISIBLE);
//
//        rltClick.setVisibility(View.GONE);
//
//        videoSeekBar.setMax((int) (exoPlayer.getDuration()));
//
//        updateProgressBar();
//
//        setResultSeekBar();
//    }
//
//    private void setResultSeekBar() {
//        int paddingLeft = videoSeekBar.getPaddingLeft();
//        int F0 = displayWidth();
//
//        long difference = listOfHazardTest.get(initVideoPosition).getListOfHazards().get(0).getEndLong() - listOfHazardTest.get(initVideoPosition).getListOfHazards().get(0).getStartLong();
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rltSeekBarResult.getLayoutParams();
//        long j = (long) (F0 - (paddingLeft * 2));
//        layoutParams.width = (int) ((difference * j) / exoPlayer.getDuration());
//        layoutParams.leftMargin = paddingLeft + ((int) ((listOfHazardTest.get(initVideoPosition).getListOfHazards().get(0).getStartLong() * j) / exoPlayer.getDuration()));
//        rltSeekBarResult.requestLayout();
//
//    }
//
//    public void drawFlagSeekBar() {
//        int paddingLeft = videoSeekBar.getPaddingLeft();
//        int F0 = displayWidth();
//        long n = exoPlayer.getCurrentPosition();
//        int duration = (int) ((((long) (F0 - (paddingLeft * 2))) * n) / (exoPlayer.getDuration()));
//
//        View inflate = LayoutInflater.from(HazardTestClipActivity.this).inflate(R.layout.item_flag, (ViewGroup) null);
//        int K0 = getDisplayMetrix(activity, 20.0f);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(K0, getDisplayMetrix(activity, 20.0f));
//        layoutParams.addRule(15, -1);
//        int i2 = duration + paddingLeft;
//        layoutParams.leftMargin = i2 - getDisplayMetrix(activity, 10.0f);
//
//        rltSeekBarFlag.addView(inflate, layoutParams);
//
//    }
//
//
//    public int getDisplayMetrix(Context context, float f2) {
//        return (int) (f2 * context.getResources().getDisplayMetrics().density);
//    }
//
//    public int displayWidth() {
//        Display defaultDisplay = getWindowManager().getDefaultDisplay();
//        Point point = new Point();
//        defaultDisplay.getSize(point);
//        return point.x;
//    }
//
//    private void updateProgressBar() {
//        long position = exoPlayer == null ? 0 : exoPlayer.getCurrentPosition();
//        videoSeekBar.setProgress((int) position);
//        int pos = getScoreAtPosition(position);
//        tvScoredBottom.setText("" + pos);
//
//        if (scoreSwitch.isChecked()) {
//            if (pos != 0 && exoPlayer.isPlaying()) {
//                exoPlayer.pause();
//                ivPlay.setImageResource(R.drawable.ic_play);
//                tvPlay.setText(getResources().getString(R.string.play));
//                tvCount.setVisibility(View.VISIBLE);
//                tvCount.setText(String.valueOf(pos) + "");
//
//                Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
//                aniFade.setDuration(500);
//                aniFade.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        tvCount.setVisibility(View.GONE);
//                        exoPlayer.seekTo(exoPlayer.getCurrentPosition() + (listOfHazardTest.get(initVideoPosition).getListOfHazards().get(0).getEndLong() - listOfHazardTest.get(initVideoPosition).getListOfHazards().get(0).getStartLong()) / 5);
//                        updateProgressBar();
////                        videoSeekHandler.removeCallbacks(updateProgressAction);
////                        videoSeekHandler.postDelayed(updateProgressAction, 200);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//                tvCount.setAnimation(aniFade);
//                return;
//            }
//        }
//        videoSeekHandler.removeCallbacks(updateProgressAction);
//        videoSeekHandler.postDelayed(updateProgressAction, 200);
//
//    }
//
//    private int getScoreAtPosition(long positionOfVideo) {
//        HazardTestModel hazardTestModel = listOfHazardTest.get(initVideoPosition);
//        ArrayList<HazardTestModel.Hazards> hazardList = hazardTestModel.getListOfHazards();
//
//        HazardTestModel.Hazards hazards = hazardList.get(0);
//
//        long timeDiff = (hazards.getEndLong() - hazards.getStartLong()) / 5;
//        long[] timeParts = new long[]{
//                hazards.getStartLong() + timeDiff,
//                hazards.getStartLong() + (timeDiff * 2),
//                hazards.getStartLong() + (timeDiff * 3),
//                hazards.getStartLong() + (timeDiff * 4),
//                hazards.getStartLong() + (timeDiff * 5)
//        };
//
//        long clickLong = positionOfVideo;
//        if (clickLong >= hazards.getStartLong() && clickLong <= timeParts[0]) {
//            return 5;
//        } else if (clickLong >= timeParts[0] && clickLong <= timeParts[1]) {
//            return 4;
//        } else if (clickLong >= timeParts[1] && clickLong <= timeParts[2]) {
//            return 3;
//        } else if (clickLong >= timeParts[2] && clickLong <= timeParts[3]) {
//            return 2;
//        } else if (clickLong >= timeParts[3] && clickLong <= timeParts[4]) {
//            return 1;
//        }
//
//        return 0;
//    }
//
//    private final Runnable updateProgressAction = new Runnable() {
//        @Override
//        public void run() {
//            updateProgressBar();
//        }
//    };
//
//
//    private int countResult() {
//        if (failedInTest) {
//            return 0;
//        }
//        HazardTestModel hazardTestModel = listOfHazardTest.get(initVideoPosition);
//        ArrayList<HazardTestModel.Hazards> hazardList = hazardTestModel.getListOfHazards();
//
//        HazardTestModel.Hazards hazards = hazardList.get(0);
//
//        long timeDiff = (hazards.getEndLong() - hazards.getStartLong()) / 5;
//        long[] timeParts = new long[]{
//                hazards.getStartLong() + timeDiff,
//                hazards.getStartLong() + (timeDiff * 2),
//                hazards.getStartLong() + (timeDiff * 3),
//                hazards.getStartLong() + (timeDiff * 4),
//                hazards.getStartLong() + (timeDiff * 5)
//        };
//
//        for (int i = 0; i < flagAdapter.getAllFlags().size(); i++) {
//
//            FlagModel flagModel = flagAdapter.getAllFlags().get(i);
//
//            long clickLong = Long.parseLong(flagModel.getTime());
//
//            if (clickLong >= hazards.getStartLong() && clickLong <= timeParts[0]) {
//                return 5;
//            } else if (clickLong >= timeParts[0] && clickLong <= timeParts[1]) {
//                return 4;
//            } else if (clickLong >= timeParts[1] && clickLong <= timeParts[2]) {
//                return 3;
//            } else if (clickLong >= timeParts[2] && clickLong <= timeParts[3]) {
//                return 2;
//            } else if (clickLong >= timeParts[3] && clickLong <= timeParts[4]) {
//                return 1;
//            }
//        }
//
//        return 0;
//    }
//
//    private void initView() {
//        ivBack = (ImageView) findViewById(R.id.ivBack);
//        ivBack.setOnClickListener(this);
//
//        videoView = findViewById(R.id.videoView);
//        rcvFlag = findViewById(R.id.recyclerFlag);
//
//        lnGuideView = findViewById(R.id.lnGuideClick);
//        lnGuideView.setOnClickListener(this);
//
//        tvCount = findViewById(R.id.tvCount);
//
//        rltClick = findViewById(R.id.rltClick);
//        rltClick.setVisibility(View.GONE);
//        rltClick.setOnClickListener(this);
//
//        tvGuide = findViewById(R.id.tvGuide);
//
//        tvDescription = findViewById(R.id.tvDes);
//        tvScoreTop = findViewById(R.id.tvScored);
//
//        rltSeekBar = findViewById(R.id.rltSeekBar);
//        rltSeekBar.setVisibility(View.GONE);
//
//        rltSeekBarResult = findViewById(R.id.rltSeekBarResult);
//        rltSeekBarFlag = findViewById(R.id.rltSeekBarFlag);
//        rltControl = findViewById(R.id.rltControl);
//
//        rltTakeTestAgain = findViewById(R.id.takeTestAgain);
//        rltTakeTestAgain.setOnClickListener(this);
//
//        videoSeekBar = findViewById(R.id.seekBar);
//        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                exoPlayer.seekTo(seekBar.getProgress());
//            }
//        });
//
//        scoreSwitch = findViewById(R.id.swShowScore);
//
//        ivPlay = findViewById(R.id.imvPlay);
//        ivPlay.setOnClickListener(this);
//
//        tvPlay = findViewById(R.id.tvPlay);
//
//        tvScoredBottom = findViewById(R.id.scored);
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.ivBack) {
//            onBackPressed();
//        } else if (id == R.id.lnGuideClick) {
//            startTvCount();
//        } else if (id == R.id.rltClick) {
//            if (flagAdapter.getAllFlagCount() > 10) {
//                failedInTest = true;
//                showResultDialog(activity.getResources().getString(R.string.clipRespondError), getResources().getString(R.string.text_ok), getResources().getString(R.string.text_cancel), null);
//                return;
//            }
//            if (flagAdapter != null) {
//                drawFlagSeekBar();
//                flagAdapter.addFlag(new FlagModel(String.valueOf(getPlayerCurrentPosition())));
//            }
//        } else if (id == R.id.takeTestAgain) {
//            exoPlayer.pause();
//            isResultShown = false;
//            showGuideView();
//        } else if (id == R.id.imvPlay) {
//            if (stateEnded) {
//                stateEnded = false;
//                if (exoPlayer != null) exoPlayer.seekTo(0);
//            }
//
//            if (exoPlayer.isPlaying()) {
//                exoPlayer.pause();
//                ivPlay.setImageResource(R.drawable.ic_play);
//                tvPlay.setText(getResources().getString(R.string.play));
//            } else {
//                exoPlayer.play();
//                ivPlay.setImageResource(R.drawable.ic_pause);
//                tvPlay.setText(getResources().getString(R.string.pause));
//            }
//            updateProgressBar();
//        }
//    }
//
//    private class PlaybackStateListener implements Player.EventListener {
//        @Override
//        public void onPlaybackStateChanged(int playbackState) {
//            String stateString;
//            switch (playbackState) {
//                case ExoPlayer.STATE_IDLE:
//                    stateString = "ExoPlayer.STATE_IDLE      -";
//                    break;
//                case ExoPlayer.STATE_BUFFERING:
//                    stateString = "ExoPlayer.STATE_BUFFERING -";
//                    if (isCountDownFinish)
//                        findViewById(R.id.loadignView).setVisibility(View.VISIBLE);
//                    break;
//                case ExoPlayer.STATE_READY:
//                    stateString = "ExoPlayer.STATE_READY     -";
//                    findViewById(R.id.loadignView).setVisibility(View.GONE);
//                    ivPlay.setImageResource(R.drawable.ic_pause);
//                    tvPlay.setText(getResources().getString(R.string.pause));
//                    playVideo();
//                    break;
//                case ExoPlayer.STATE_ENDED:
//                    stateEnded = true;
//                    stateString = "ExoPlayer.STATE_ENDED     -";
//                    videoEnd();
//                    ivPlay.setImageResource(R.drawable.ic_play);
//                    tvPlay.setText(getResources().getString(R.string.play));
//                    break;
//                default:
//                    stateString = "UNKNOWN_STATE             -";
//                    break;
//            }
//            Log.e(TAG, "changed state to " + stateString);
//        }
//    }
//
//    public long getPlayerCurrentPosition() {
//        long time = exoPlayer.getCurrentPosition();
//        Log.e(TAG, "getPlayerCurrentPosition : " + time);
//        return time;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        playVideo();
//    }
//
//    private void showResultDialog(String content, String positiveString, String negativeString, DialogHazardTestResult.OnDialogDismissListener onDialogDismissListener) {
//        DialogHazardTestResult dialogHazardTestResult = new DialogHazardTestResult(activity, content, positiveString, negativeString, onDialogDismissListener);
//        dialogHazardTestResult.show();
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        exoPlayer.release();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        exoPlayer.release();
//    }
//}

package com.example.rtoexamdrivingtheory.hazardTest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.rtoexamdrivingtheory.hazardTest.adapter.FlagAdapter;
import com.example.rtoexamdrivingtheory.hazardTest.dialog.DialogHazardTestResult;
import com.example.rtoexamdrivingtheory.model.FlagModel;
import com.example.rtoexamdrivingtheory.model.HazardTestModel;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.utils.AssetsUtil;
import com.example.rtoexamdrivingtheory.utils.WindowUtils;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.rey.material.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HazardTestClipActivity extends AppCompatActivity implements View.OnClickListener {

    PlayerView videoView;
    RecyclerView rcvFlag;
    FlagAdapter flagAdapter;
    private Activity activity;
    private ImageView ivBack;
    LinearLayout lnGuideView;
    TextView tvCount;
    RelativeLayout rltClick;
    private ExoPlayer exoPlayer;
    ArrayList<HazardTestModel> listOfHazardTest = new ArrayList<>();
    private String TAG = "HazardTestClipActivity";
    private PlaybackStateListener playbackStateListener;
    private boolean isCountDownFinish = false;
    TextView tvGuide;
    int initVideoPosition = 0;
    private boolean failedInTest = false;
    private TextView tvDescription, tvScoreTop;
    RelativeLayout rltSeekBarResult, rltSeekBarFlag, rltTakeTestAgain;
    View rltControl;
    android.widget.RelativeLayout rltSeekBar;
    SeekBar videoSeekBar;
    Switch scoreSwitch;
    ImageView ivPlay;
    TextView tvPlay;
    TextView tvScoredBottom;
    private boolean stateEnded = false;
    private Handler videoSeekHandler = new Handler();
    private boolean isResultShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_test_clip);

        activity = HazardTestClipActivity.this;

        WindowUtils.setStatusBarLight(activity, R.color.grey10);

        initVideoPosition = 0;

        initView();

        setFlagRcv();

        getJsonData();

        showGuideView();

        initVideoPlayer();

    }

    private void setFlagRcv() {
        flagAdapter = new FlagAdapter(activity);
        rcvFlag.setAdapter(flagAdapter);
    }

    private void getJsonData() {
        int index = getIntent().getIntExtra("index", 1);
        int rawResource = R.raw.test1;
        if (index == 1) {
            rawResource = R.raw.test1;
        } else if (index == 2) {
            rawResource = R.raw.test2;
        } else if (index == 3) {
            rawResource = R.raw.test3;
        } else if (index == 4) {
            rawResource = R.raw.test4;
        } else if (index == 5) {
            rawResource = R.raw.test5;
        } else if (index == 6) {
            rawResource = R.raw.test6;
        } else if (index == 7) {
            rawResource = R.raw.test7;
        } else if (index == 8) {
            rawResource = R.raw.test8;
        } else if (index == 9) {
            rawResource = R.raw.test9;
        } else if (index == 10) {
            rawResource = R.raw.test10;
        }
        String object = AssetsUtil.openRawResource(activity, rawResource);
        try {
            listOfHazardTest.clear();
            JSONArray jsonArray = new JSONArray(object);
            for (int i = 0; i < jsonArray.length(); i++) {
                HazardTestModel hazardTestModel = new HazardTestModel();

                JSONObject dataObject = jsonArray.getJSONObject(i);

                ArrayList<HazardTestModel.Hazards> listOfHazards = new ArrayList<>();
                JSONArray hazardArray = dataObject.getJSONArray("hazards");
                for (int j = 0; j < hazardArray.length(); j++) {
                    HazardTestModel.Hazards hazards = new HazardTestModel.Hazards();
                    hazards.setStart(hazardArray.getJSONObject(j).getString("start"));
                    hazards.setEnd(hazardArray.getJSONObject(j).getString("end"));

                    listOfHazards.add(hazards);
                }

                hazardTestModel.setId(dataObject.getString("id"));
                hazardTestModel.setVideoUrl(dataObject.getString("mp4"));
                hazardTestModel.setDescription(dataObject.getString("title"));
                hazardTestModel.setDescription(dataObject.getString("description"));
                hazardTestModel.setListOfHazards(listOfHazards);

                listOfHazardTest.add(hazardTestModel);
            }
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData: ", e);
        }
    }

    private void showGuideView() {
        findViewById(R.id.loadignView).setVisibility(View.GONE);

        lnGuideView.setVisibility(View.VISIBLE);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        aniFade.setDuration(1000);
        lnGuideView.setAnimation(aniFade);

        tvCount.setVisibility(View.GONE);
        rltClick.setVisibility(View.GONE);

        tvGuide.setVisibility(View.GONE);

        tvDescription.setVisibility(View.GONE);

        tvScoreTop.setVisibility(View.GONE);

        rltSeekBar.setVisibility(View.GONE);

        rcvFlag.setVisibility(View.VISIBLE);
        if (flagAdapter != null) flagAdapter.clearFlags();

        rltSeekBar.setVisibility(View.GONE);

        rltControl.setVisibility(View.GONE);

        rltTakeTestAgain.setVisibility(View.GONE);
    }

    private void startTvCount() {
        lnGuideView.setVisibility(View.GONE);
        tvCount.setVisibility(View.VISIBLE);

        CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
                Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                aniFade.setDuration(500);
                tvCount.setAnimation(aniFade);
                if ((millisUntilFinished / 1000) == 0) {
                    tvCount.setText("Go!");
                    return;
                }
                tvCount.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                rltClick.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.GONE);
                isCountDownFinish = true;
                if (exoPlayer != null) exoPlayer.seekTo(0);
                ivPlay.setImageResource(R.drawable.ic_play);
                tvPlay.setText(getResources().getString(R.string.play));
                tvGuide.setVisibility(View.VISIBLE);
                findViewById(R.id.loadignView).setVisibility(View.VISIBLE);
                rltSeekBarFlag.removeAllViews();
                scoreSwitch.setChecked(false);
                playVideo();
            }
        };
        countDownTimer.start();

    }

    private void initVideoPlayer() {
        exoPlayer = new ExoPlayer.Builder(activity).build();
        playbackStateListener = new PlaybackStateListener();
        exoPlayer.addListener(playbackStateListener);

        videoView.setPlayer(exoPlayer);
        setVideoInfo(initVideoPosition);
    }

    private void setVideoInfo(int position) {
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(Uri.parse(listOfHazardTest.get(position).getVideoUrl()))
                .setMimeType(MimeTypes.APPLICATION_MP4)
                .build();

        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource
                .Factory(new DefaultDataSource.Factory(this))
                .createMediaSource(mediaItem);
        exoPlayer.setMediaSource(mediaSource);
        exoPlayer.prepare();
        videoSeekBar.setMax((int) (exoPlayer.getDuration() / 1000));
        playVideo();
    }

    private void playVideo() {
        if (isCountDownFinish && exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
        } else {
            if (exoPlayer != null) exoPlayer.setPlayWhenReady(false);
        }
    }

    private void videoEnd() {
        String result = getResources().getString(R.string.scoreStringFront) + " " + countResult() + "" + getResources().getString(R.string.scoreStringEnd);

        if (!isResultShown) {
            showResultDialog(result, getResources().getString(R.string.result), getResources().getString(R.string.text_cancel), new DialogHazardTestResult.OnDialogDismissListener() {
                @Override
                public void onResultListener() {
                    showResult();
                }
            });
        }

    }

    private void showResult() {
        isResultShown = true;
        tvGuide.setVisibility(View.GONE);
        tvDescription.setVisibility(View.VISIBLE);
        tvDescription.setText(listOfHazardTest.get(initVideoPosition).getDescription());

        tvScoreTop.setVisibility(View.VISIBLE);
        tvScoreTop.setText(activity.getResources().getString(R.string.you_scored) + " " + countResult());

        rltSeekBar.setVisibility(View.VISIBLE);

        rcvFlag.setVisibility(View.GONE);

        rltSeekBar.setVisibility(View.VISIBLE);

        rltControl.setVisibility(View.VISIBLE);

        rltTakeTestAgain.setVisibility(View.VISIBLE);

        rltClick.setVisibility(View.GONE);

        videoSeekBar.setMax((int) (exoPlayer.getDuration()));

        updateProgressBar();

        setResultSeekBar();
    }

    private void setResultSeekBar() {
        int paddingLeft = videoSeekBar.getPaddingLeft();
        int F0 = displayWidth();

        long difference = listOfHazardTest.get(initVideoPosition).getListOfHazards().get(0).getEndLong() - listOfHazardTest.get(initVideoPosition).getListOfHazards().get(0).getStartLong();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rltSeekBarResult.getLayoutParams();
        long j = (long) (F0 - (paddingLeft * 2));
        layoutParams.width = (int) ((difference * j) / exoPlayer.getDuration());
        layoutParams.leftMargin = paddingLeft + ((int) ((listOfHazardTest.get(initVideoPosition).getListOfHazards().get(0).getStartLong() * j) / exoPlayer.getDuration()));
        rltSeekBarResult.requestLayout();

    }

    public void drawFlagSeekBar() {
        int paddingLeft = videoSeekBar.getPaddingLeft();
        int F0 = displayWidth();
        long n = exoPlayer.getCurrentPosition();
        int duration = (int) ((((long) (F0 - (paddingLeft * 2))) * n) / (exoPlayer.getDuration()));

        View inflate = LayoutInflater.from(HazardTestClipActivity.this).inflate(R.layout.item_flag, (ViewGroup) null);
        int K0 = getDisplayMetrix(activity, 20.0f);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(K0, getDisplayMetrix(activity, 20.0f));
        layoutParams.addRule(15, -1);
        int i2 = duration + paddingLeft;
        layoutParams.leftMargin = i2 - getDisplayMetrix(activity, 10.0f);

        rltSeekBarFlag.addView(inflate, layoutParams);

    }


    public int getDisplayMetrix(Context context, float f2) {
        return (int) (f2 * context.getResources().getDisplayMetrics().density);
    }

    public int displayWidth() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point.x;
    }

    private void updateProgressBar() {
        long position = exoPlayer == null ? 0 : exoPlayer.getCurrentPosition();
        videoSeekBar.setProgress((int) position);
        int pos = getScoreAtPosition(position);
        tvScoredBottom.setText("" + pos);

        if (scoreSwitch.isChecked()) {
            if (pos != 0 && exoPlayer.isPlaying()) {
                exoPlayer.pause();
                ivPlay.setImageResource(R.drawable.ic_play);
                tvPlay.setText(getResources().getString(R.string.play));
                tvCount.setVisibility(View.VISIBLE);
                tvCount.setText(String.valueOf(pos) + "");

                Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                aniFade.setDuration(500);
                aniFade.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tvCount.setVisibility(View.GONE);
                        exoPlayer.seekTo(exoPlayer.getCurrentPosition() + (listOfHazardTest.get(initVideoPosition).getListOfHazards().get(0).getEndLong() - listOfHazardTest.get(initVideoPosition).getListOfHazards().get(0).getStartLong()) / 5);
                        updateProgressBar();
//                        videoSeekHandler.removeCallbacks(updateProgressAction);
//                        videoSeekHandler.postDelayed(updateProgressAction, 200);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                tvCount.setAnimation(aniFade);
                return;
            }
        }
        videoSeekHandler.removeCallbacks(updateProgressAction);
        videoSeekHandler.postDelayed(updateProgressAction, 200);

    }

    private int getScoreAtPosition(long positionOfVideo) {
        HazardTestModel hazardTestModel = listOfHazardTest.get(initVideoPosition);
        ArrayList<HazardTestModel.Hazards> hazardList = hazardTestModel.getListOfHazards();

        HazardTestModel.Hazards hazards = hazardList.get(0);

        long timeDiff = (hazards.getEndLong() - hazards.getStartLong()) / 5;
        long[] timeParts = new long[]{
                hazards.getStartLong() + timeDiff,
                hazards.getStartLong() + (timeDiff * 2),
                hazards.getStartLong() + (timeDiff * 3),
                hazards.getStartLong() + (timeDiff * 4),
                hazards.getStartLong() + (timeDiff * 5)
        };

        long clickLong = positionOfVideo;
        if (clickLong >= hazards.getStartLong() && clickLong <= timeParts[0]) {
            return 5;
        } else if (clickLong >= timeParts[0] && clickLong <= timeParts[1]) {
            return 4;
        } else if (clickLong >= timeParts[1] && clickLong <= timeParts[2]) {
            return 3;
        } else if (clickLong >= timeParts[2] && clickLong <= timeParts[3]) {
            return 2;
        } else if (clickLong >= timeParts[3] && clickLong <= timeParts[4]) {
            return 1;
        }

        return 0;
    }

    private final Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            updateProgressBar();
        }
    };


    private int countResult() {
        if (failedInTest) {
            return 0;
        }
        HazardTestModel hazardTestModel = listOfHazardTest.get(initVideoPosition);
        ArrayList<HazardTestModel.Hazards> hazardList = hazardTestModel.getListOfHazards();

        HazardTestModel.Hazards hazards = hazardList.get(0);

        long timeDiff = (hazards.getEndLong() - hazards.getStartLong()) / 5;
        long[] timeParts = new long[]{
                hazards.getStartLong() + timeDiff,
                hazards.getStartLong() + (timeDiff * 2),
                hazards.getStartLong() + (timeDiff * 3),
                hazards.getStartLong() + (timeDiff * 4),
                hazards.getStartLong() + (timeDiff * 5)
        };

        for (int i = 0; i < flagAdapter.getAllFlags().size(); i++) {

            FlagModel flagModel = flagAdapter.getAllFlags().get(i);

            long clickLong = Long.parseLong(flagModel.getTime());

            if (clickLong >= hazards.getStartLong() && clickLong <= timeParts[0]) {
                return 5;
            } else if (clickLong >= timeParts[0] && clickLong <= timeParts[1]) {
                return 4;
            } else if (clickLong >= timeParts[1] && clickLong <= timeParts[2]) {
                return 3;
            } else if (clickLong >= timeParts[2] && clickLong <= timeParts[3]) {
                return 2;
            } else if (clickLong >= timeParts[3] && clickLong <= timeParts[4]) {
                return 1;
            }
        }

        return 0;
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        videoView = findViewById(R.id.videoView);
        rcvFlag = findViewById(R.id.recyclerFlag);

        lnGuideView = findViewById(R.id.lnGuideClick);
        lnGuideView.setOnClickListener(this);

        tvCount = findViewById(R.id.tvCount);

        rltClick = findViewById(R.id.rltClick);
        rltClick.setVisibility(View.GONE);
        rltClick.setOnClickListener(this);

        tvGuide = findViewById(R.id.tvGuide);

        tvDescription = findViewById(R.id.tvDes);
        tvScoreTop = findViewById(R.id.tvScored);

        rltSeekBar = findViewById(R.id.rltSeekBar);
        rltSeekBar.setVisibility(View.GONE);

        rltSeekBarResult = findViewById(R.id.rltSeekBarResult);
        rltSeekBarFlag = findViewById(R.id.rltSeekBarFlag);
        rltControl = findViewById(R.id.rltControl);

        rltTakeTestAgain = findViewById(R.id.takeTestAgain);
        rltTakeTestAgain.setOnClickListener(this);

        videoSeekBar = findViewById(R.id.seekBar);
        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                exoPlayer.seekTo(seekBar.getProgress());
            }
        });

        scoreSwitch = findViewById(R.id.swShowScore);

        ivPlay = findViewById(R.id.imvPlay);
        ivPlay.setOnClickListener(this);

        tvPlay = findViewById(R.id.tvPlay);

        tvScoredBottom = findViewById(R.id.scored);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ivBack) {
            onBackPressed();
        } else if (id == R.id.lnGuideClick) {
            startTvCount();
        } else if (id == R.id.rltClick) {
            if (flagAdapter.getAllFlagCount() > 10) {
                failedInTest = true;
                showResultDialog(activity.getResources().getString(R.string.clipRespondError), getResources().getString(R.string.text_ok), getResources().getString(R.string.text_cancel), null);
                return;
            }
            if (flagAdapter != null) {
                drawFlagSeekBar();
                flagAdapter.addFlag(new FlagModel(String.valueOf(getPlayerCurrentPosition())));
            }
        } else if (id == R.id.takeTestAgain) {
            exoPlayer.pause();
            isResultShown = false;
            showGuideView();
        } else if (id == R.id.imvPlay) {
            if (stateEnded) {
                stateEnded = false;
                if (exoPlayer != null) exoPlayer.seekTo(0);
            }

            if (exoPlayer.isPlaying()) {
                exoPlayer.pause();
                ivPlay.setImageResource(R.drawable.ic_play);
                tvPlay.setText(getResources().getString(R.string.play));
            } else {
                exoPlayer.play();
                ivPlay.setImageResource(R.drawable.ic_pause);
                tvPlay.setText(getResources().getString(R.string.pause));
            }
            updateProgressBar();
        }
    }

    private class PlaybackStateListener implements Player.Listener {
        @Override
        public void onPlaybackStateChanged(int playbackState) {
            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    if (isCountDownFinish)
                        findViewById(R.id.loadignView).setVisibility(View.VISIBLE);
                    break;
                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    findViewById(R.id.loadignView).setVisibility(View.GONE);
                    ivPlay.setImageResource(R.drawable.ic_pause);
                    tvPlay.setText(getResources().getString(R.string.pause));
                    playVideo();
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateEnded = true;
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    videoEnd();
                    ivPlay.setImageResource(R.drawable.ic_play);
                    tvPlay.setText(getResources().getString(R.string.play));
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.e(TAG, "changed state to " + stateString);
        }
    }

    public long getPlayerCurrentPosition() {
        long time = exoPlayer.getCurrentPosition();
        Log.e(TAG, "getPlayerCurrentPosition : " + time);
        return time;
    }

    @Override
    protected void onResume() {
        super.onResume();
        playVideo();
    }

    private void showResultDialog(String content, String positiveString, String negativeString, DialogHazardTestResult.OnDialogDismissListener onDialogDismissListener) {
        DialogHazardTestResult dialogHazardTestResult = new DialogHazardTestResult(activity, content, positiveString, negativeString, onDialogDismissListener);
        dialogHazardTestResult.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }
}