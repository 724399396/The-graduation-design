package com.qunar.liwei.graduation.weibo_crawler;

import java.io.Serializable;
import java.sql.Timestamp;

public class Weibo implements Serializable {

        private static final long serialVersionUID = 5570192519696895557L;
        private String userName;
        private String commontText;
        private String type;
        private String from;
        private String forwardReason;
        private Timestamp time;
        private String imageUrl;

        public Weibo() {
                super();
        }

        public Weibo(String userName, String commontText, String type, String from,
                        String forwardReason, Timestamp time, String imageUrl) {
                super();
                this.userName = userName;
                this.commontText = commontText;
                this.type = type;
                this.from = from;
                this.forwardReason = forwardReason;
                this.time = time;
                this.imageUrl = imageUrl;
        }

        public String getImageUrl() {
                return imageUrl;
        }
        public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
        }
        public String getForwardReason() {
                return forwardReason;
        }

        public void setForwardReason(String forwardReason) {
                this.forwardReason = forwardReason;
        }

        public String getUserName() {
                return userName;
        }
        public void setUserName(String userName) {
                this.userName = userName;
        }

        public String getCommontText() {
                return commontText;
        }
        public void setCommontText(String commontText) {
                this.commontText = commontText;
        }
        public String getType() {
                return type;
        }
        public void setType(String type) {
                this.type = type;
        }
        public String getFrom() {
                return from;
        }
        public void setFrom(String from) {
                this.from = from;
        }

        public Timestamp getTime() {
                return time;
        }

        public void setTime(Timestamp time) {
                this.time = time;
        }

        @Override
        public String toString() {
                return "Weibo [userName=" + userName + ", commontText=" + commontText
                                + ", type=" + type + ", from=" + from + ", forwardReason="
                                + forwardReason + ", time=" + time + ", imageUrl=" + imageUrl
                                + "]";
        }
}