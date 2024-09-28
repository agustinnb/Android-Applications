package com.anb.coinconverter.feature;

public class Coins {


        String coin = null;
        String scoin = null;
        String typecoin = null;
        String urlcoin = null;

        public Coins(String coin, String scoin, String typecoin, String urlcoin) {
            super();
            this.coin = coin;
            this.scoin = scoin;
            this.typecoin = typecoin;
            this.urlcoin = urlcoin;
        }

        public String getCoin() {
            return coin;
        }
        public void setCoin(String coin) {
            this.coin = coin;
        }
        public String getScoin() {
            return scoin;
        }
        public void setScoin(String scoin) {
            this.scoin = scoin;
        }
        public String getTypeCoin() {
            return typecoin;
        }
        public void setTypeCoin(String typecoin) {
            this.typecoin = typecoin;
        }
        public String getURLCoin() {
            return urlcoin;
        }
        public void setURLCoin(String urlcoin) {
            this.urlcoin = urlcoin;
        }

        @Override
        public String toString() {
            return  coin + " " + scoin + " "
                    + typecoin + " " + urlcoin;
        }


}

