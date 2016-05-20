package com.sunnet.trackapp.client.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunnet.trackapp.client.R;
import com.sunnet.trackapp.client.db.entity.CallVoiceEntity;
import com.sunnet.trackapp.client.log.Log;
import com.sunnet.trackapp.client.ui.customview.ProgressWheel;
import com.sunnet.trackapp.client.util.ConfigApi;
import com.sunnet.trackapp.client.util.Utils;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.util.List;

public class VoiceAdapter extends RecyclerView.Adapter<VoiceAdapter.VoiceHolder> {
    private static IOnCLick iOnCLick;
    private List<CallVoiceEntity> voiceList;
    private static ThinDownloadManager downloadManager;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 2;
    private static String itemIdPlayAudioCurr;

    public VoiceAdapter(List<CallVoiceEntity> voiceList, IOnCLick iOnCLick) {
        this.voiceList = voiceList;
        this.iOnCLick = iOnCLick;
        downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
    }

    @Override
    public VoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_voice_item, parent, false);
        return new VoiceHolder(v);
    }

    @Override
    public void onBindViewHolder(VoiceHolder holder, int position) {
        holder.setUi(voiceList.get(position));
    }

    @Override
    public int getItemCount() {
        return voiceList != null ? voiceList.size() : 0;
    }

    static class VoiceHolder extends RecyclerView.ViewHolder {
        View itemView;
        ProgressWheel progressWheel;
        ImageView iconPlay;
        TextView tvNameOrPhone, tvDuration, tvTime;
        View.OnClickListener onClickListener;

        public VoiceHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvNameOrPhone = (TextView) itemView.findViewById(R.id.tv1);
            tvDuration = (TextView) itemView.findViewById(R.id.tv2);
            tvTime = (TextView) itemView.findViewById(R.id.tv3);
            iconPlay = (ImageView) itemView.findViewById(R.id.icon_play);
            progressWheel = (ProgressWheel) itemView.findViewById(R.id.progress_wheel);
        }

        public void setUi(final CallVoiceEntity entity) {
            tvNameOrPhone.setText(entity.getPhoneName());
            tvDuration.setText("00:00");
            tvTime.setText(entity.getDate());

            progressWheel.setProgress(0);
            final String fileName = entity.getAudio();
            if (Utils.isExistFile(Utils.getPathVoice(fileName))) {
                if (Utils.isFullTextSearch(itemIdPlayAudioCurr, fileName))
                    iconPlay.setImageResource(R.mipmap.btn_play_selected);
                else
                    iconPlay.setImageResource(R.mipmap.btn_play);
            } else {
                iconPlay.setImageResource(R.mipmap.icon_download_audio);
            }

            progressWheel.setVisibility(View.GONE);
            if (onClickListener == null) {
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pathAudio = Utils.getPathVoice(fileName);
                        if (Utils.isExistFile(pathAudio)) {
                            itemIdPlayAudioCurr = fileName;
                            iOnCLick.onClick(entity);
                        } else {
                            //-- Download audio from server
                            progressWheel.setVisibility(View.VISIBLE);
                            iconPlay.setVisibility(View.INVISIBLE);
                            DownloadRequest downloadRequest =
                                    new DownloadRequest(Uri.parse(ConfigApi.URL_VOICE + entity.getAudio()))
                                    .setDestinationURI(Uri.parse(pathAudio))
                                    .setDownloadListener(new DownloadStatusListener() {
                                        @Override
                                        public void onDownloadComplete(int id) {
                                            progressWheel.setVisibility(View.GONE);
                                            iconPlay.setImageResource(R.mipmap.btn_play);
                                            iconPlay.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                                            Log.e(VoiceAdapter.class.getName() + " download audio error: " + errorMessage);
                                            progressWheel.setVisibility(View.GONE);
                                            iconPlay.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onProgress(int id, long totalBytes, int progress) {
                                            progressWheel.setProgress(progress);
                                        }
                                    });
                            downloadManager.add(downloadRequest);
                        }
                    }
                };
                iconPlay.setOnClickListener(onClickListener);
            }
        }
    }
}
