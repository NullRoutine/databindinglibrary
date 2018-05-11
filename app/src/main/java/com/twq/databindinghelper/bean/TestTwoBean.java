package com.twq.databindinghelper.bean;

import java.util.List;

/**
 * Created by tang.wangqiang on 2018/5/9.
 */

public class TestTwoBean {

    /**
     * data : {"goodsUrl":"http://www.geenk.cn","banners":[{"isContract":true,"advertisement":{"picPath":"http://www.geenk.cn/upload/backstage/20171201/201712011502016267381.jpg","picHref":"#","platform":1,"picIsused":1,"goodsid":82,"picType":1,"picOrder":1,"isAct":0,"picId":434,"picName":"APP测试轮播图"}}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * goodsUrl : http://www.geenk.cn
         * banners : [{"isContract":true,"advertisement":{"picPath":"http://www.geenk.cn/upload/backstage/20171201/201712011502016267381.jpg","picHref":"#","platform":1,"picIsused":1,"goodsid":82,"picType":1,"picOrder":1,"isAct":0,"picId":434,"picName":"APP测试轮播图"}}]
         */

        private String goodsUrl;
        private List<BannersBean> banners;

        public String getGoodsUrl() {
            return goodsUrl;
        }

        public void setGoodsUrl(String goodsUrl) {
            this.goodsUrl = goodsUrl;
        }

        public List<BannersBean> getBanners() {
            return banners;
        }

        public void setBanners(List<BannersBean> banners) {
            this.banners = banners;
        }

        public static class BannersBean {
            /**
             * isContract : true
             * advertisement : {"picPath":"http://www.geenk.cn/upload/backstage/20171201/201712011502016267381.jpg","picHref":"#","platform":1,"picIsused":1,"goodsid":82,"picType":1,"picOrder":1,"isAct":0,"picId":434,"picName":"APP测试轮播图"}
             */

            private boolean isContract;
            private AdvertisementBean advertisement;

            public boolean isIsContract() {
                return isContract;
            }

            public void setIsContract(boolean isContract) {
                this.isContract = isContract;
            }

            public AdvertisementBean getAdvertisement() {
                return advertisement;
            }

            public void setAdvertisement(AdvertisementBean advertisement) {
                this.advertisement = advertisement;
            }

            public static class AdvertisementBean {
                /**
                 * picPath : http://www.geenk.cn/upload/backstage/20171201/201712011502016267381.jpg
                 * picHref : #
                 * platform : 1
                 * picIsused : 1
                 * goodsid : 82
                 * picType : 1
                 * picOrder : 1
                 * isAct : 0
                 * picId : 434
                 * picName : APP测试轮播图
                 */

                private String picPath;
                private String picHref;
                private int platform;
                private int picIsused;
                private int goodsid;
                private int picType;
                private int picOrder;
                private int isAct;
                private int picId;
                private String picName;

                public String getPicPath() {
                    return picPath;
                }

                public void setPicPath(String picPath) {
                    this.picPath = picPath;
                }

                public String getPicHref() {
                    return picHref;
                }

                public void setPicHref(String picHref) {
                    this.picHref = picHref;
                }

                public int getPlatform() {
                    return platform;
                }

                public void setPlatform(int platform) {
                    this.platform = platform;
                }

                public int getPicIsused() {
                    return picIsused;
                }

                public void setPicIsused(int picIsused) {
                    this.picIsused = picIsused;
                }

                public int getGoodsid() {
                    return goodsid;
                }

                public void setGoodsid(int goodsid) {
                    this.goodsid = goodsid;
                }

                public int getPicType() {
                    return picType;
                }

                public void setPicType(int picType) {
                    this.picType = picType;
                }

                public int getPicOrder() {
                    return picOrder;
                }

                public void setPicOrder(int picOrder) {
                    this.picOrder = picOrder;
                }

                public int getIsAct() {
                    return isAct;
                }

                public void setIsAct(int isAct) {
                    this.isAct = isAct;
                }

                public int getPicId() {
                    return picId;
                }

                public void setPicId(int picId) {
                    this.picId = picId;
                }

                public String getPicName() {
                    return picName;
                }

                public void setPicName(String picName) {
                    this.picName = picName;
                }
            }
        }
    }
}
