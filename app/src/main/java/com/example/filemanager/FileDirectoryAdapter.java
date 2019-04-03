package com.example.filemanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileDirectoryAdapter extends RecyclerView.Adapter<FileDirectoryAdapter.MyViewHolder> {
    private List<File> mFilesList;
    private OnItemClickListener onItemClickListener;

    public FileDirectoryAdapter(List<File> mFilesList, OnItemClickListener onItemClickListener) {
        this.mFilesList = mFilesList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_file_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final File currentFile = mFilesList.get(i);
        myViewHolder.mFileName.setText(currentFile.getName());

        if (currentFile.isDirectory()) {
            myViewHolder.mTotalItemCounts.setText(String.valueOf(Arrays.asList(currentFile.listFiles()).size()) + " items");
            myViewHolder.mFileOrFolderImage.setImageResource(R.drawable.image_folder);
        } else if (currentFile.isFile()) {
            myViewHolder.mFileOrFolderImage.setImageResource(R.drawable.image_file);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.setOnItemClick(currentFile.getAbsolutePath(), currentFile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mFileName;
        TextView mTotalItemCounts;
        TextView mFilePath;
        ImageView mFileOrFolderImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mFileName = itemView.findViewById(R.id.tv_file_name);
            mTotalItemCounts = itemView.findViewById(R.id.tv_total_items_count);
            mFilePath = itemView.findViewById(R.id.tv_file_path);
            mFileOrFolderImage = itemView.findViewById(R.id.iv_file_n_folder);
        }
    }

    public void setFiledList(List<File> iFileList) {
        this.mFilesList = iFileList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void setOnItemClick(String filePath, File file);
    }
}
