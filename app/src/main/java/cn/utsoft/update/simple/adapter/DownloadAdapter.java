package cn.utsoft.update.simple.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.utsoft.cd.utupdater.UTLoadManager;
import cn.utsoft.cd.utupdater.event.UTUpdateCallback;
import cn.utsoft.update.simple.ImageActivity;
import cn.utsoft.update.simple.R;
import cn.utsoft.update.simple.entity.UpdaterEntity;

/**
 * Created by 李波 on 2017/2/10.
 * Function:
 * Desc:
 */
public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.Viewload> {

    private final Context mContext;
    private List<UpdaterEntity> mUpdaterList;
    private HashMap<String, WeakReference<Viewload>> mViewHolderMap;

    public DownloadAdapter(Context context, List<UpdaterEntity> list) {
        this.mContext = context;
        this.mViewHolderMap = new HashMap<>();

        updateList(list);

        UTLoadManager.observer(new UTUpdateCallback() {

            @Override
            public void onStart(String tag) {
                WeakReference<Viewload> reference = mViewHolderMap.get(tag);
                Viewload viewload = reference.get();
                if (viewload != null) {
                    viewload.ivDownload.setSelected(true);
                    viewload.ivDownload.setEnabled(true);
                }
                Iterator<UpdaterEntity> iterator = mUpdaterList.iterator();
                while (iterator.hasNext()) {
                    UpdaterEntity entity = iterator.next();
                    if (tag.equals(entity.tag)) {
                        entity.isStart = true;
                        return;
                    }
                }
            }

            @Override
            public void onProgress(String tag, long current, long total) {
                WeakReference<Viewload> reference = mViewHolderMap.get(tag);
                Viewload viewload = reference.get();
                if (viewload != null) {
                    int progress = (int) (current * 100 / total);
                    viewload.pro.setProgress(progress);
                }
            }

            @Override
            public void onFinish(String tag, String path) {
                if (TextUtils.isEmpty(tag)) {
                    return;
                }
                Iterator<UpdaterEntity> iterator = mUpdaterList.iterator();
                while (iterator.hasNext()) {
                    UpdaterEntity entity = iterator.next();
                    if (tag.equals(entity.tag)) {
                        entity.path = path;
                        entity.finish = true;
                        WeakReference<Viewload> reference = mViewHolderMap.get(tag);
                        Viewload viewload = reference.get();
                        if (viewload != null) {
                            viewload.tvShow.setVisibility(View.VISIBLE);
                            viewload.ivDownload.setVisibility(View.GONE);
                        }
                        return;
                    }
                }
            }

            @Override
            public void onError(String tag, int code, String msg) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateList(List<UpdaterEntity> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        mUpdaterList = list;
        notifyDataSetChanged();
    }

    @Override
    public Viewload onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.layout_download_item, parent, false);
        return new Viewload(view);
    }

    @Override
    public void onBindViewHolder(Viewload holder, final int position) {
        final UpdaterEntity entity = mUpdaterList.get(position);
        WeakReference<Viewload> holdReference = new WeakReference<>(holder);
        mViewHolderMap.put(entity.tag, holdReference);

        holder.ivDownload.setSelected(entity.isStart);
        if (entity.isAPK) {
            Glide.with(mContext).load(R.mipmap.ic_launcher).into(holder.ivView);
        } else {
            Glide.with(mContext).load(entity.url).into(holder.ivView);
        }

        if (entity.finish) {
            holder.tvShow.setVisibility(View.VISIBLE);
            holder.ivDownload.setVisibility(View.GONE);
        } else {
            holder.tvShow.setVisibility(View.GONE);
            holder.ivDownload.setVisibility(View.VISIBLE);
        }

        holder.tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.isAPK) {
                    Toast.makeText(mContext, entity.path, Toast.LENGTH_SHORT).show();
                    UTLoadManager.installApk(mContext, entity.path);
                } else {
                    ImageActivity.start((Activity) mContext, entity.path);
                }
            }
        });

        holder.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.finish) {
                    Toast.makeText(mContext, "已下载完成", Toast.LENGTH_SHORT).show();
                } else {
                    if (!entity.isStart) {
                        UTLoadManager.load(mContext,
                                entity.url,
                                entity.url,
                                entity.name,
                                entity.versionName,
                                entity.version);
                        v.setEnabled(false);
                    } else {
                        if (v.isSelected()) {
                            UTLoadManager.pause(mContext, entity.url);
                        } else {
                            UTLoadManager.resume(mContext, entity.url);
                        }
                        v.setSelected(!v.isSelected());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUpdaterList.size();
    }

    public static class Viewload extends RecyclerView.ViewHolder {
        private ImageView ivView;
        private TextView tvShow;
        private ProgressBar pro;
        private ImageView ivDownload;

        public Viewload(View itemView) {
            super(itemView);
            pro = (ProgressBar) itemView.findViewById(R.id.pro);
            ivView = (ImageView) itemView.findViewById(R.id.iv_view);
            ivDownload = (ImageView) itemView.findViewById(R.id.iv_download);
            tvShow = (TextView) itemView.findViewById(R.id.tv_show);
        }
    }
}
