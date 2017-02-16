package cn.utsoft.update.simple.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.utsoft.cd.utupdater.UTUpdaterCallback;
import cn.utsoft.cd.utupdater.UTUpdaterListener;
import cn.utsoft.cd.utupdater.UTUpdaterManager;
import cn.utsoft.cd.utupdater.config.ErrorCode;
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
    private Map<String, Viewload> viewloadMap = new HashMap<>();
    private boolean isNetDisconnect = false;
    private UTUpdaterListener listener = new UTUpdaterListener() {

        @Override
        public void onPrepare(String tag) {
            UpdaterEntity entity = getEntityByTag(tag);
            Viewload holder = getViewHolderByTag(tag);
            if (entity == null || holder == null) {
                return;
            }
            entity.status = 1;
            changeStatus(entity, holder);
        }

        @Override
        public void onStart(String tag) {
            UpdaterEntity entity = getEntityByTag(tag);
            Viewload holder = getViewHolderByTag(tag);
            if (entity == null || holder == null) {
                return;
            }
            entity.status = 2;
            changeStatus(entity, holder);
        }

        @Override
        public void onPause(String tag) {
            UpdaterEntity entity = getEntityByTag(tag);
            Viewload holder = getViewHolderByTag(tag);
            if (entity == null || holder == null) {
                return;
            }
            entity.status = 3;
            changeStatus(entity, holder);
        }

        @Override
        public void onProgress(String tag, long current, long total, String velocity) {
            UpdaterEntity entity = getEntityByTag(tag);
            Viewload holder = getViewHolderByTag(tag);
            if (entity == null || holder == null) {
                return;
            }
            int progress = (int) (current * 100 / total);
            holder.pro.setProgress(progress);
            holder.tvProgress.setText(velocity);
            entity.progress = current;
            entity.total = total;

            Log.d("DownloadAdapter", tag + " -- " + current + "/" + total);
        }

        @Override
        public void onComplete(String tag, String path) {
            UpdaterEntity entity = getEntityByTag(tag);
            Viewload holder = getViewHolderByTag(tag);
            if (entity == null || holder == null) {
                return;
            }
            entity.path = path;
            entity.status = 4;

            changeStatus(entity, holder);
        }

        @Override
        public void onRemove(String tag) {

        }

        @Override
        public void onError(String tag, int code, String msg) {
            UpdaterEntity entity = getEntityByTag(tag);
            Viewload holder = getViewHolderByTag(tag);
            if (entity == null || holder == null) {
                return;
            }
            if (code == ErrorCode.ERROR_DISCONNECT_CODE) {
                changeStatus(entity, holder);
            } else {
                entity.status = -1;

                changeStatus(entity, holder);
            }
        }
    };

    private Viewload getViewHolderByTag(String tag) {
        return viewloadMap.get(tag);
    }

    private UpdaterEntity getEntityByTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            Iterator<UpdaterEntity> iterator = mUpdaterList.iterator();
            while (iterator.hasNext()) {
                UpdaterEntity entity = iterator.next();
                if (tag.equals(entity.tag)) {
                    return entity;
                }
            }
        }
        return null;
    }

    public DownloadAdapter(Context context, List<UpdaterEntity> list) {
        this.mContext = context;

        updateList(list);

        UTUpdaterManager.observer(new UTUpdaterCallback() {
            @Override
            public void onNetworkChange(boolean enable) {
                if (isNetDisconnect != !enable) {
                    Toast.makeText(mContext, "网络状态 = " + enable, Toast.LENGTH_SHORT).show();
                    isNetDisconnect = !enable;

//                    for (int i = 0; i < mUpdaterList.size(); i++) {
//                        UpdaterEntity entity = mUpdaterList.get(i);
//                        Viewload viewload = viewloadMap.get(entity.tag);
//                        if (entity == null || viewload == null) {
//                            continue;
//                        }
//                        changeStatus(entity, viewload);
//                    }

                    notifyDataSetChanged();
                }
            }

            @Override
            public void onPauseAllDownload() {
                Toast.makeText(mContext, "全部暂停", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResumeAllDownload() {
                Toast.makeText(mContext, "全部重新下载", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRemoveAllDownload() {

            }

            @Override
            public void onClearDownloadHistory() {
                Toast.makeText(mContext, "清空下载记录", Toast.LENGTH_SHORT).show();
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
    public void onBindViewHolder(final Viewload holder, final int position) {
        final UpdaterEntity entity = mUpdaterList.get(position);

        viewloadMap.put(entity.tag, holder);

        holder.ivDownload.setSelected(entity.status == 2);

        if (entity.isAPK) {
            Glide.with(mContext).load(R.mipmap.ic_launcher).into(holder.ivView);
        } else {
            Glide.with(mContext).load(entity.url).into(holder.ivView);
        }

        if (entity.status == 4) {
            holder.tvShow.setVisibility(View.VISIBLE);
            holder.ivDownload.setVisibility(View.GONE);
        } else {
            holder.tvShow.setVisibility(View.GONE);
            holder.ivDownload.setVisibility(View.VISIBLE);
        }

        holder.tvStatus.setSelected(isNetDisconnect);

        if (isNetDisconnect) {
            holder.tvStatus.setText("网络连接断开");
        }

        holder.tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.isAPK) {
                    Toast.makeText(mContext, entity.path, Toast.LENGTH_SHORT).show();
                    UTUpdaterManager.installApk(mContext, entity.path);
                } else {
                    ImageActivity.start((Activity) mContext, entity.path);
                }
            }
        });

        changeStatus(entity, holder);

        holder.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                int status = entity.status;
                switch (status) {
                    case 0:
                    case -1:
                        v.setEnabled(false);
                        UTUpdaterManager.load(mContext, entity.tag, entity.url, listener);
                        break;
                    case 1:
                        UTUpdaterManager.pause(mContext, entity.tag);
                        break;
                    case 2:
                        UTUpdaterManager.pause(mContext, entity.tag);
                        break;
                    case 3:
                        UTUpdaterManager.resume(mContext, entity.tag);
                        break;
                }
            }
        });
    }

    private void changeStatus(UpdaterEntity entity, Viewload holder) {
        holder.tvShow.setVisibility(View.GONE);
        holder.ivDownload.setVisibility(View.VISIBLE);
        holder.tvStatus.setSelected(isNetDisconnect || entity.status == -1);

        if (entity.total > 0 && entity.progress > 0) {
            int progress = (int) (entity.progress * 100 / entity.total);
            holder.pro.setProgress(progress);
        } else {
            holder.pro.setProgress(0);
        }

        if (isNetDisconnect) {
            holder.tvProgress.setText("");
            holder.tvStatus.setText("网络连接断开");
            if (entity.status == 4) {
                holder.tvStatus.setText("下载完成");
                holder.tvShow.setVisibility(View.VISIBLE);
                holder.ivDownload.setVisibility(View.GONE);
            } else if (entity.status == 2) {
                holder.ivDownload.setSelected(true);
            } else if (entity.status == 1) {
                holder.ivDownload.setSelected(true);
            } else if (entity.status == 3) {
                holder.ivDownload.setSelected(false);
            }
        } else {
            switch (entity.status) {
                case 0:
                    holder.tvStatus.setText("...");
                    holder.tvProgress.setText("");
                    break;
                case 1:
                    holder.tvStatus.setText("等待下载...");
                    holder.tvProgress.setText("");

                    holder.ivDownload.setSelected(true);
                    holder.ivDownload.setEnabled(true);
                    break;
                case 2:
                    holder.tvStatus.setText("正在下载...");
                    holder.tvProgress.setText("");
                    break;
                case 3:
                    holder.tvStatus.setText("暂停下载");
                    holder.tvProgress.setText("");
                    break;
                case 4:
                    holder.tvStatus.setText("下载完成");
                    holder.tvProgress.setText("");
                    holder.pro.setProgress(100);
                    holder.tvShow.setVisibility(View.VISIBLE);
                    holder.ivDownload.setVisibility(View.GONE);
                    break;
                case -1:
                    holder.tvStatus.setText("下载错误");
                    break;
            }
        }
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
        private TextView tvStatus;
        private TextView tvProgress;

        public Viewload(View itemView) {
            super(itemView);
            pro = (ProgressBar) itemView.findViewById(R.id.pro);
            ivView = (ImageView) itemView.findViewById(R.id.iv_view);
            ivDownload = (ImageView) itemView.findViewById(R.id.iv_download);
            tvShow = (TextView) itemView.findViewById(R.id.tv_show);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvProgress = (TextView) itemView.findViewById(R.id.tv_progress);
        }
    }
}
