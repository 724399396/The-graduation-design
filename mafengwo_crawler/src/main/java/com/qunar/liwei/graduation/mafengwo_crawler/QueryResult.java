package com.qunar.liwei.graduation.mafengwo_crawler;

public class QueryResult {
        private int cityId;
        private String url;
        public QueryResult(int cityId, String url) {
                super();
                this.cityId = cityId;
                this.url = url;
        }

        public QueryResult() {
                super();
        }

        public int getCityId() {
                return cityId;
        }

        public void setCityId(int cityId) {
                this.cityId = cityId;
        }

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        @Override
        public String toString() {
                return "QueryResult [cityId=" + cityId + ", url=" + url + "]";
        }
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + cityId;
                result = prime * result + ((url == null) ? 0 : url.hashCode());
                return result;
        }
        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                QueryResult other = (QueryResult) obj;
                if (cityId != other.cityId)
                        return false;
                if (url == null) {
                        if (other.url != null)
                                return false;
                } else if (!url.equals(other.url))
                        return false;
                return true;
        }

}