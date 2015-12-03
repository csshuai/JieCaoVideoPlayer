package fm.jiecao.jcvideoplayer_lib;

import android.app.Application;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * 统一管理MediaPlayer,管理视频的暂停播放进度全屏的功能
 * Created by Nathen
 * On 2015/11/30 15:39
 */
public class JCMediaPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener {

    public MediaPlayer mediaPlayer;
    private static JCMediaPlayer jcMediaPlayer;
    private static Application context;
    public static UUID uuid;//这个是正在播放中的视频控件的uuid，
    public static UUID prev_uuid;

    public static JCMediaPlayer init(Application cont) {
        if (jcMediaPlayer == null || context == null) {
            jcMediaPlayer = new JCMediaPlayer();
            context = cont;
        }
        return jcMediaPlayer;
    }

    public static JCMediaPlayer intance() {
        return jcMediaPlayer;
    }

    public JCMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void prepareToPlay(String url) {
        try {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, Uri.parse(url));
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_PREPARED));
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE));
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_BUFFERUPDATE);
        videoEvents.obj = percent;
        EventBus.getDefault().post(videoEvents);
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_SEEKCOMPLETE));
    }
}
