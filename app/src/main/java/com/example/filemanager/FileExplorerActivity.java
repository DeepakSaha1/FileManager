package com.example.filemanager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileExplorerActivity extends AppCompatActivity implements FileDirectoryAdapter.OnItemClickListener {

    private List<File> mFiles;
    private RecyclerView mRecyclerView;
    FileDirectoryAdapter mFileDirAdapter;
    private String mCurrentDir;
    private File mRootDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);

        mRootDir = Environment.getExternalStorageDirectory();

        mCurrentDir = mRootDir.getAbsolutePath();
        mRecyclerView = findViewById(R.id.recycler_view_ist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFiles = Arrays.asList(mRootDir.listFiles());
        mFileDirAdapter = new FileDirectoryAdapter(mFiles, this);
        mRecyclerView.setAdapter(mFileDirAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setOnItemClick(String filePath, File file) {
        if (file.isDirectory()) {
            mCurrentDir = filePath;
            mFiles = Arrays.asList(file.listFiles());
            if (mFiles != null) {
                if (mFiles.size() != 0) {
                    mFileDirAdapter = new FileDirectoryAdapter(mFiles, FileExplorerActivity.this);
                    mRecyclerView.setAdapter(mFileDirAdapter);
                } else {
                    Toast.makeText(this, file.getName() + " is Empty!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            String mimeType = getMimeType(mCurrentDir);
            Uri apkURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            newIntent.setDataAndType(apkURI, mimeType);
            newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(newIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getMimeType(String iUrl) {
        String mimeType = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(iUrl);
        if (extension != null) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!mCurrentDir.equals(mRootDir.getAbsolutePath())) {
                    mCurrentDir = new File(mCurrentDir).getParent();
                    File parentFile = new File(mCurrentDir);
                    mFiles = Arrays.asList(parentFile.listFiles());
                    mFileDirAdapter.setFiledList(mFiles);
                    return true;
                } else {
                    super.onBackPressed();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}