package filtermusic.net.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.R;
import filtermusic.net.common.data.DataProvider;
import filtermusic.net.common.model.Radio;
import filtermusic.net.player.PlayerController;

/**
 * View holding the details for a radio and that gives the user the possibility of playing that
 * radio
 * and saving the radio as favorite
 */
public class RadioDetailView extends LinearLayout implements PlayerController.PlayerListener {

    private static final String LOG_TAG = RadioDetailView.class.getSimpleName();
    private ImageView mRadioImage;
    private TextView mRadioTitle;
    private TextView mRadioDescription;

    private ImageView mPlayButton;
    private ImageView mStarButton;
    private ProgressBar mLoadingProgress;

    private Radio mRadio;

    private Context mContext;

    private PlayerController mPlayerController;

    @Inject
    DataProvider mDataProvider;

    public RadioDetailView(Context context) {
        super(context);
        initUI(context);
    }

    public RadioDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(context);
    }

    public RadioDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI(context);
    }

    public boolean isRadioPlaying(@NonNull final Radio radio) {
        return mRadio == radio;
    }

    private void initUI(final Context context) {

        FiltermusicApplication.getInstance().inject(this);

        setOrientation(VERTICAL);

        mContext = context;

        mPlayerController = PlayerController.getInstance();
        mPlayerController.addListener(this);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.radio_detail_view, this, true);

        mRadioImage = (ImageView) rootView.findViewById(R.id.radio_image);
        mRadioTitle = (TextView) rootView.findViewById(R.id.radio_title);
        mRadioDescription = (TextView) rootView.findViewById(R.id.radio_description);

        mLoadingProgress = (ProgressBar) rootView.findViewById(R.id.loading_progress);

        mPlayButton = (ImageView) rootView.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isPlaying = mPlayerController.isRadioPlaying(mRadio);
                        if (!isPlaying) {
                            mPlayButton.setVisibility(View.GONE);
                            mLoadingProgress.setVisibility(View.VISIBLE);
                            mPlayerController.play(mRadio);
                        } else {
                            mPlayerController.stop();
                        }
                    }
                });


        mStarButton = (ImageView) rootView.findViewById(R.id.star_button);
        mStarButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRadio.setFavorite(!mRadio.isFavorite());

                        int star = mRadio.isFavorite() ? R.drawable.star : R.drawable.star_outline;
                        mStarButton.setImageResource(star);
                        mDataProvider.updateRadio(mRadio);

                    }
                });
    }

    public void setRadio(@NonNull Radio radio) {
        mRadio = radio;
        updateUI();
    }

    private void updateUI() {

        Picasso.with(mContext).load(mRadio.getImageUrl()).placeholder(R.drawable.station_image)
                .into(mRadioImage);

        mRadioTitle.setText(mRadio.getTitle());
        mRadioDescription.setText(Html.fromHtml(mRadio.getDescription()));

        int star = mRadio.isFavorite() ? R.drawable.star : R.drawable.star_outline;
        mStarButton.setImageResource(star);

        mPlayButton.setVisibility(VISIBLE);
        mLoadingProgress.setVisibility(GONE);
        if (mPlayerController.isRadioPlaying(mRadio)) {
            mPlayButton.setImageResource(R.drawable.pause_circle_fill);
        } else {
            mPlayButton.setImageResource(R.drawable.play_circle);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPlayerController.removeListener(this);
    }


    @Override
    public void onPlayerStartedPlaying(Radio radio) {
        if(mRadio != null && mRadio.equals(radio)) {
            Log.d(LOG_TAG, "onPlaying " + radio.getTitle());
            mLoadingProgress.setVisibility(View.GONE);
            mPlayButton.setVisibility(VISIBLE);
            mPlayButton.setImageResource(R.drawable.pause_circle_fill);
        }
    }

    @Override
    public void onError() {
        mPlayButton.setImageResource(R.drawable.play_circle);

    }

    @Override
    public void onPlayerStopped() {
        mPlayButton.setImageResource(R.drawable.play_circle);
    }

    @Override
    public void onTrackChanged(String track) {

    }
}
