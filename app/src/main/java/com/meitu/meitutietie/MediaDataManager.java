package com.meitu.meitutietie;

import android.database.Cursor;
import android.provider.MediaStore;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MediaDataManager {

    private Map<String, ImageFolder> bucketToFolder = new HashMap<>();
    private ArrayList<ImageFolder> folderList = new ArrayList<>();
    private static MediaDataManager mediaDataManager = new MediaDataManager();

    private MediaDataManager() {
    }

    public static MediaDataManager getInstance() {
        return mediaDataManager;
    }

    public void clear() {
        bucketToFolder.clear();
        folderList.clear();
    }

    public ArrayList<ImageFolder> getFolderList() {
        return folderList;
    }

    /**
     * 获取相册根目录下每个文件夹的信息
     *
     * @param mMediaStoreCursor
     */
    public void decode(Cursor mMediaStoreCursor) {
        while (mMediaStoreCursor.moveToNext()) {
            // 文件路径
            String path = mMediaStoreCursor.getString(mMediaStoreCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            // 文件名
            String fileName = new File(path).getName();
            // 文件Bucket_id
            String bucket_id = mMediaStoreCursor.getString(mMediaStoreCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
            File parentFile = new File(path).getParentFile();
            if (parentFile == null || !isImage(fileName)) {
                continue;
            }
            // 文件夹路径
            String parentPath = parentFile.getAbsolutePath();
            // 文件夹名称
            String parentName = parentFile.getName();

            if (!bucketToFolder.containsKey(bucket_id)) {
                ImageFolder imageFolder = new ImageFolder();
                imageFolder.setFirstImagePath(path);
                imageFolder.setFolderName(parentName);
                imageFolder.setDir(parentPath);
                imageFolder.setImagePaths(new ArrayList<>(Arrays.asList(path)));
                bucketToFolder.put(bucket_id, imageFolder);
                folderList.add(imageFolder);
            } else {
                bucketToFolder.get(bucket_id).getImagePaths().add(path);
            }
        }
    }

    /**
     * 判断文件是否为图像（无后缀情况？）
     */
    private static boolean isImage(String filename) {
        return filename.toLowerCase().endsWith(".jpg")
                || filename.toLowerCase().endsWith(".png")
                || filename.toLowerCase().endsWith(".jpeg")
                || filename.toLowerCase().endsWith(".png")
                || filename.toLowerCase().endsWith(".gif");
    }

}
