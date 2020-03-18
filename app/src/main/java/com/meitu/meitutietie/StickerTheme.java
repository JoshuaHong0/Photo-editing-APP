package com.meitu.meitutietie;

public class StickerTheme {

    private String thumbnailUrl;
    private String topThumbnailUrl;
    private String name;
    private String previewUrl;
    private String zipUrl;
    private boolean isFree;
    private boolean downloaded;
    private boolean turnOn;
    private int zipSize;
    private int count;
    private int id;

    public StickerTheme() {
    }

    public boolean isTurnOn() {
        return turnOn;
    }

    public void setTurnOn(boolean turnOn) {
        this.turnOn = turnOn;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTopThumbnailUrl() {
        return topThumbnailUrl;
    }

    public void setTopThumbnailUrl(String topThumbnailUrl) {
        this.topThumbnailUrl = topThumbnailUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getZipUrl() {
        return zipUrl;
    }

    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public int getZipSize() {
        return zipSize;
    }

    public void setZipSize(int zipSize) {
        this.zipSize = zipSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
