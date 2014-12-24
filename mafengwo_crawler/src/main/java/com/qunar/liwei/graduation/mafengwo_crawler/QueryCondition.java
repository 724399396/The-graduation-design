package com.qunar.liwei.graduation.mafengwo_crawler;

public class QueryCondition {
        private int cityId;
        private int queryNums;
        public QueryCondition(int cityId, int queryNums) {
                super();
                this.cityId = cityId;
                this.queryNums = queryNums;
        }


        public int getCityId() {
                return cityId;
        }

        public void setCityId(int cityId) {
                this.cityId = cityId;
        }

        public int getQueryNums() {
                return queryNums;
        }

        public void setQueryNums(int queryNums) {
                this.queryNums = queryNums;
        }

        @Override
        public String toString() {
                return "QueryCondition [cityId=" + cityId + ", queryNums=" + queryNums
                                + "]";
        }

}