package cn.utsoft.update.simple.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_download_item, parent, false);
        return new Viewload(view);
    }

    @Override
    public void onBindViewHolder(Viewload holder, int position) {
        UpdaterEntity entity = mUpdaterList.get(position);
        WeakReference<Viewload> holdReference = new WeakReference<>(holder);
        mViewHolderMap.put(entity.tag, holdReference);

        holder.ivDownload.setSelected(entity.isStart);
        Glide.with(mContext).load(entity.url).into(holder.ivView);
    }

    @Override
    public int getItemCount() {
        return mUpdaterList.size();
    }

    public static class Viewload extends RecyclerView.ViewHolder {
        private ImageView ivView;
        private ProgressBar pro;
        private ImageView ivDownload;

        public Viewload(View itemView) {
            super(itemView);
            pro = (ProgressBar) itemView.findViewById(R.id.pro);
            ivView = (ImageView) itemView.findViewById(R.id.iv_view);
            ivDownload = (ImageView) itemView.findViewById(R.id.iv_download);
        }
    }
}
