package com.qunar.liwei.graduation.mafengwo_crawler;

import java.io.Serializable;
import java.sql.Timestamp;

public class Travel implements Serializable {
        private static final long serialVersionUID = -8118419734532576517L;
        private int cityId;
        private String title;
        private String contentText;
        private String author;
        private Timestamp pubTime;
        private Timestamp travelTime;
        private String url;
        public Travel() {
                super();
        }
        public Travel(int cityId, String title, String contentText, String author,
                        Timestamp pubTime, Timestamp travelTime, String url) {
                super();
                this.cityId = cityId;
                this.title = title;
                this.contentText = contentText;
                this.author = author;
                this.pubTime = pubTime;
                this.travelTime = travelTime;
                this.url = url;
        }
        public int getCityId() {
                return cityId;
        }
        public void setCityId(int cityId) {
                this.cityId = cityId;
        }
        public String getTitle() {
                return title;
        }
        public void setTitle(String title) {
                this.title = title;
        }
        public String getContentText() {
                return contentText;
        }
        public void setContentText(String contentText) {
                this.contentText = contentText;
        }
        public String getAuthor() {
                return author;
        }
        public void setAuthor(String author) {
                this.author = author;
        }
        public Timestamp getPubTime() {
                return pubTime;
        }
        public void setPubTime(Timestamp pubTime) {
                this.pubTime = pubTime;
        }
        public Timestamp getTravelTime() {
                return travelTime;
        }
        public void setTravelTime(Timestamp travelTime) {
                this.travelTime = travelTime;
        }
        public String getUrl() {
                return url;
        }
        public void setUrl(String url) {
                this.url = url;
        }
        public static long getSerialversionuid() {
                return serialVersionUID;
        }
        @Override
        public String toString() {
                return "Travel [cityId=" + cityId + ", title=" + title
                                + ", contentText=" + contentText + ", author=" + author
                                + ", pubTime=" + pubTime + ", travelTime=" + travelTime
                                + ", url=" + url + "]";
        }


}