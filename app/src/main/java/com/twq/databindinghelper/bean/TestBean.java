package com.twq.databindinghelper.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/2 0002.
 */

public class TestBean {

    @Override
    public String toString() {
        return "TestBean{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }

    /**
     * code : 200
     * data : {"entries":[{"CREATETIMESTAMP":"2018-02-02 10:20:21.925","PATH":"1eab7db9ec374e3d8a3565ebdaa7b8b9","DESCRIPTION":"文件上传","LENGTH":311584,"USERNAME":"root:管理员","ID":"1eab7db9ec374e3d8a3565ebdaa7b8b9","FILETYPE":"jpg","serviceName":null,"FILENAME":"7e5aa839d090eb71f2944a23d7882f6c.jpg"}]}
     * message : 成功！
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        private List<EntriesBean> entries;

        @Override
        public String toString() {
            return "DataBean{" +
                    "entries=" + entries +
                    '}';
        }

        public List<EntriesBean> getEntries() {
            return entries;
        }

        public void setEntries(List<EntriesBean> entries) {
            this.entries = entries;
        }

        public static class EntriesBean {
            @Override
            public String toString() {
                return "EntriesBean{" +
                        "CREATETIMESTAMP='" + CREATETIMESTAMP + '\'' +
                        ", PATH='" + PATH + '\'' +
                        ", DESCRIPTION='" + DESCRIPTION + '\'' +
                        ", LENGTH=" + LENGTH +
                        ", USERNAME='" + USERNAME + '\'' +
                        ", ID='" + ID + '\'' +
                        ", FILETYPE='" + FILETYPE + '\'' +
                        ", serviceName=" + serviceName +
                        ", FILENAME='" + FILENAME + '\'' +
                        '}';
            }

            /**
             * CREATETIMESTAMP : 2018-02-02 10:20:21.925
             * PATH : 1eab7db9ec374e3d8a3565ebdaa7b8b9
             * DESCRIPTION : 文件上传
             * LENGTH : 311584
             * USERNAME : root:管理员
             * ID : 1eab7db9ec374e3d8a3565ebdaa7b8b9
             * FILETYPE : jpg
             * serviceName : null
             * FILENAME : 7e5aa839d090eb71f2944a23d7882f6c.jpg
             */

            private String CREATETIMESTAMP;
            private String PATH;
            private String DESCRIPTION;
            private int LENGTH;
            private String USERNAME;
            private String ID;
            private String FILETYPE;
            private Object serviceName;
            private String FILENAME;

            public String getCREATETIMESTAMP() {
                return CREATETIMESTAMP;
            }

            public void setCREATETIMESTAMP(String CREATETIMESTAMP) {
                this.CREATETIMESTAMP = CREATETIMESTAMP;
            }

            public String getPATH() {
                return PATH;
            }

            public void setPATH(String PATH) {
                this.PATH = PATH;
            }

            public String getDESCRIPTION() {
                return DESCRIPTION;
            }

            public void setDESCRIPTION(String DESCRIPTION) {
                this.DESCRIPTION = DESCRIPTION;
            }

            public int getLENGTH() {
                return LENGTH;
            }

            public void setLENGTH(int LENGTH) {
                this.LENGTH = LENGTH;
            }

            public String getUSERNAME() {
                return USERNAME;
            }

            public void setUSERNAME(String USERNAME) {
                this.USERNAME = USERNAME;
            }

            public String getID() {
                return ID;
            }

            public void setID(String ID) {
                this.ID = ID;
            }

            public String getFILETYPE() {
                return FILETYPE;
            }

            public void setFILETYPE(String FILETYPE) {
                this.FILETYPE = FILETYPE;
            }

            public Object getServiceName() {
                return serviceName;
            }

            public void setServiceName(Object serviceName) {
                this.serviceName = serviceName;
            }

            public String getFILENAME() {
                return FILENAME;
            }

            public void setFILENAME(String FILENAME) {
                this.FILENAME = FILENAME;
            }
        }
    }
}
