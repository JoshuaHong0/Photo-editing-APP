package com.meitu.meitutietie;

import java.util.ArrayList;

/**
 * 储存相册文件夹的相关信息
 */
public class ImageFolder {
    private String firstImagePath; // 文件夹第一张图片路径
    private String dir;  // 文件夹路径
    private String folderName;  // 文件夹名字
    private ArrayList<String> imagePaths; //文件夹下图片路径集合

    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    /**
     * 返回该文件夹下图片个数
     * @return
     */
    public int getNumOfImages() {
        return imagePaths.size();
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
