package id.ranuwp.greetink.rwpbaking.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import id.ranuwp.greetink.rwpbaking.R;
import id.ranuwp.greetink.rwpbaking.databinding.FragmentStepDetailBinding;
import id.ranuwp.greetink.rwpbaking.model.Step;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment implements View.OnClickListener {

    private Step step;
    private boolean lastStep;
    private SimpleExoPlayer player;
    private FragmentStepDetailBinding fragmentStepDetailBinding;
    private OnClickListener onClickListener;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton:
                if (onClickListener == null) return;
                onClickListener.onNextButtonClick(step);
                break;
        }
    }

    public interface OnClickListener {
        void onNextButtonClick(Step step);
    }

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public void setStep(Step step, boolean lastStep) {
        if(step == null) return;
        this.step = step;
        this.lastStep = lastStep;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(savedInstanceState != null){
            step = savedInstanceState.getParcelable(Step.class.getName());
        }
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        fragmentStepDetailBinding = FragmentStepDetailBinding.bind(view);
        if(step != null){
            fragmentStepDetailBinding.descriptionTextView.setText(step.getDescription());
            fragmentStepDetailBinding.simpleExoPlayerView.requestFocus();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            fragmentStepDetailBinding.simpleExoPlayerView.setPlayer(player);
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(),
                            getContext().getPackageName()),
                    defaultBandwidthMeter);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(step.getVideoURL()),
                    dataSourceFactory,
                    extractorsFactory,
                    null,
                    null
            );
            player.prepare(mediaSource);
            if (lastStep) {
                fragmentStepDetailBinding.nextButton.setVisibility(View.GONE);
            } else {
                fragmentStepDetailBinding.nextButton.setOnClickListener(this);
            }
        }
        return fragmentStepDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            step = savedInstanceState.getParcelable(Step.class.getName());
            if(getResources().getBoolean(R.bool.multiPane)){
                setStep(step,true);
            }else{
                setStep(step,false);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Step.class.getName(), step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onClickListener = (OnClickListener) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}
