package com.anb.coinconverter.feature;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.IOException;
import java.util.concurrent.ExecutionException;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    Boolean  activado=false;
    Button[] elim;
    String[] Coins;
    TextView[] price;
    int x;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9810112105886212~6944773371");

        LinearLayout main = (LinearLayout) findViewById(R.id.LinearLayout2);
        final EditText num = (EditText) findViewById(R.id.editText3);
        final Button convert=(Button) findViewById(R.id.button);
        num.setText("1");
        convert.setText("USD");


        /* GRABO SI ES NUEVO, TRAIGO SI ES VIEJO */

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String gCoins = "DOLLAR,BITCOIN,ETHEREUM";
        String gShortCoins = "USD,BTC,ETH";
        String gCoinURL = "https://es.exchange-rates.org/converter/USD/USD/1/Y,https://coinmarketcap.com/currencies/bitcoin/,https://coinmarketcap.com/currencies/ethereum/";
        String gCoinType = "currency,coin,coin";
      //  editor.clear().apply();
        if (pref.getString("coins", null) == null) {
            editor.putString("coins", "dollar,bitcoin,ethereum");
            editor.putString("shortcoins", "USD,BTC,ETH");
            editor.putString("coinurl", "https://es.exchange-rates.org/converter/USD/USD/1/Y,https://coinmarketcap.com/currencies/bitcoin/,https://coinmarketcap.com/currencies/ethereum/");
            editor.putString("cointype", "currency,coin,coin");
            editor.commit();
        } else {
            gCoins = pref.getString("coins", null);
            gShortCoins = pref.getString("shortcoins", null);
            gCoinURL = pref.getString("coinurl", null);
            gCoinType = pref.getString("cointype", null);
        }

        Coins = gCoins.split(",");
        final String[] SCoins = gShortCoins.split(",");
        String[] CoinURL = gCoinURL.split(",");
        String[] CoinType = gCoinType.split(",");
        Log.d("CoinConverter", "TOTAL:" + Coins.length);
        final String[] scoinsel = new String[1];
        scoinsel[0] = "USD";
        Float scoinselvalUSD = Float.valueOf(0);
        /* GRABO SI ES NUEVO, TRAIGO SI ES VIEJO */



        /*
        ARMO BORDES
        */


        ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class

// get paint
        Paint paint = rectShapeDrawable.getPaint();

// set border color, stroke and stroke width
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5); // you can change the value of 5

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0xFFFFFFFF, 0xB5B5B5});
        gd.setCornerRadius(0f);

        /* TERMINA BORDES */

        RelativeLayout[] f = new RelativeLayout[Coins.length];
        final TextView[] coin = new TextView[Coins.length];
        price = new TextView[Coins.length];
        final TextView[] scoin = new TextView[Coins.length];
        final Float[] coinval = new Float[Coins.length];
        elim=new Button[Coins.length];

        RelativeLayout.LayoutParams params1;
        RelativeLayout.LayoutParams params;
        for (x = 0; x < Coins.length; x++) {
            Log.d("CoinConverter", "VOY POR EL:" + x);
            Log.d("CoinConverter", "COIN:" + Coins[x]);


            f[x] = new RelativeLayout(this);
            coin[x] = new TextView(this);
            price[x] = new TextView(this);
            scoin[x] = new TextView(this);

            f[x].setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            f[x].setBackgroundDrawable(rectShapeDrawable);
            f[x].setBackgroundDrawable(gd);
            f[x].setId(x + 2000);
            if (x != 0) {
                params1 = (RelativeLayout.LayoutParams) f[x].getLayoutParams();
                params1.addRule(RelativeLayout.BELOW, f[x - 1].getId());

            }

            coin[x].setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            coin[x].setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
            coin[x].setGravity(Gravity.LEFT);
            coin[x].setText(Coins[x].toUpperCase());
            coin[x].setId(x + 3000);
            params = (RelativeLayout.LayoutParams) coin[x].getLayoutParams();
            params.setMargins(20, 20, 0, 0);

            scoin[x].setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            scoin[x].setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
            scoin[x].setGravity(Gravity.LEFT);
            scoin[x].setText(SCoins[x].toUpperCase());
            scoin[x].setId(x + 4000);
            params = (RelativeLayout.LayoutParams) scoin[x].getLayoutParams();
            params.addRule(RelativeLayout.BELOW, coin[x].getId());
            params.setMargins(20, 0, 0, 20);

            elim[x] = new Button(this);
            elim[x].setVisibility(View.INVISIBLE);
            elim[x].setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            params = (RelativeLayout.LayoutParams) elim[x].getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            params.setMargins(0, 20, 20, 0);
            elim[x].setText("-");
            elim[x].setId(x+5000);

            elim[x].setOnClickListener(new View.OnClickListener() {

                                    public void onClick(View view) {

                                               SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                               SharedPreferences.Editor editor = pref.edit();
                                               String gCoins = pref.getString("coins", null);
                                               String gShortCoins = pref.getString("shortcoins", null);
                                               String gCoinURL = pref.getString("coinurl", null);
                                               String gCoinType = pref.getString("cointype", null);
                                               Log.d("CoinConverter",String.valueOf(view.getId()));


                                               int id=view.getId();
                                               id=id-5000;
                                               String[] coinse=gCoins.split(",");
                                                String[] scoinse=gShortCoins.split(",");
                                        String[] coinurle=gCoinURL.split(",");
                                        String[] cointypee=gCoinType.split(",");
                                        String ncoins="";
                                        String nscoins="";
                                        String ncoinurl="";
                                        String ncointype="";
                                        Log.d("CoinConverter",String.valueOf(id));
                                        for (int y=0;y<coinse.length;y++) {
                                        if (id!=0) {
                                            if (y != id) {
                                                if (y == 0) {
                                                    ncoins = coinse[y];
                                                    nscoins = scoinse[y];
                                                    ncoinurl = coinurle[y];
                                                    ncointype = cointypee[y];
                                                } else {
                                                    ncoins += "," + coinse[y];
                                                    nscoins += "," + scoinse[y];
                                                    ncoinurl += "," + coinurle[y];
                                                    ncointype += "," + cointypee[y];
                                                }

                                            }
                                        }else {
                                            if (y==1) {
                                                ncoins = coinse[y];
                                                nscoins = scoinse[y];
                                                ncoinurl = coinurle[y];
                                                ncointype = cointypee[y];
                                            }else{
                                                ncoins += "," + coinse[y];
                                                nscoins += "," + scoinse[y];
                                                ncoinurl += "," + coinurle[y];
                                                ncointype += "," + cointypee[y];
                                        }
                                        }

                                        }


                                        editor.putString("coins", ncoins);
                                        editor.putString("shortcoins", nscoins);
                                        editor.putString("coinurl", ncoinurl);
                                        editor.putString("cointype", ncointype);
                                        editor.commit();

                                        finish();
                                        startActivity(getIntent());

                                           }
                                       });


            price[x] = new TextView(this);
            price[x].setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            params = (RelativeLayout.LayoutParams) price[x].getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.RIGHT_OF, coin[x].getId());
            params.setMargins(0, 20, 20, 0);



            price[x].setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
            price[x].setGravity(Gravity.RIGHT | Gravity.END);
            String siteUrl = CoinURL[x];
            String pri = "";
            try {
                // converterresult-toAmount
                pri = new ParseURL().execute(new String[]{siteUrl}).get().toString().toString();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            coinval[x] = Float.valueOf(pri.trim());

            if (scoinsel[0].equals(SCoins[x].toUpperCase())) {
                scoinselvalUSD = coinval[x];
            }
            f[x].addView(coin[x]);
            f[x].addView(price[x]);
            f[x].addView(scoin[x]);
            f[x].addView(elim[x]);
            main.addView(f[x]);
        }

        for (int x = 0; x < Coins.length; x++) {
            if (scoinsel[0].equals(SCoins[x].toUpperCase())) {
                Log.d("CoinConverter", "PRUEBA" + coinval[x]);
                price[x].setText(coinval[x] + " " + SCoins[x].toUpperCase());
            } else {

                float value = (Float.valueOf(scoinselvalUSD) / coinval[x]);
                Log.d("CoinConverter", "PRUEBAdouble" + scoinselvalUSD);
                price[x].setText(value + " " + SCoins[x].toUpperCase());
            }
        }

        num.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(s.toString().equals(""))) {
                    Log.d("CoinConverter", "CAMBIO A: " + s);
                float number = Float.valueOf(String.valueOf(s));


                  Log.d("CoinConverter", "CAMBIO A: " + number);


                    float numberw = 0;
                    for (int x = 0; x < coin.length; x++) {
                        if (scoinsel[0].equals(SCoins[x].toUpperCase())) {
                            numberw=coinval[x];
                        }
                    }



                  for (int x = 0; x < coin.length; x++) {
                      if (scoinsel[0].equals(SCoins[x].toUpperCase())) {
                          price[x].setText(number + " " + SCoins[x].toUpperCase());
                      } else {
                          float value = (Float.valueOf(number) / coinval[x]) * numberw;
                          price[x].setText(value + " " + SCoins[x].toUpperCase());
                      }
                  }


              }

            }
        });

//ALERT DIALOG CLICKEANDO BOTON

        convert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);

                // setup the alert builder
                int temp = (Math.random() <= 0.5) ? 1 : 2;

                if (temp==1) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            // Load the next interstitial.
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        }

                    });
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose a Coin or Currency");
// add a radio button list
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String gCoins = pref.getString("coins", null);
                String gShortCoins = pref.getString("shortcoins", null);
                int selected=0;
                final String[] coins = gCoins.split(",");

                final String[] scoins= gShortCoins.split(",");
                final String[] coinss= new String[coins.length];
                for (int x=0;x<coins.length;x++) {
                 coinss[x]=coins[x].toUpperCase() + " - " + scoins[x].toUpperCase();
                }

                final int[] checkedItem = {0}; // cow
                builder.setSingleChoiceItems(coinss, checkedItem[0], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                    checkedItem[0] =which;
                    }
                });
// add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK



                        convert.setText(scoins[checkedItem[0]].toUpperCase());
                        scoinsel[0] =scoins[checkedItem[0]];

                       float number = 0;
                        for (int x = 0; x < coins.length; x++) {
                            if (scoinsel[0].equals(SCoins[x].toUpperCase())) {
                            number=coinval[x];
                            }
                        }
                        
                        for (int x = 0; x < coins.length; x++) {
                         if (scoinsel[0].equals(SCoins[x].toUpperCase())) {

                             price[x].setText(num.getText() + " " + SCoins[x].toUpperCase());

                            } else {
                             Log.d("CoinConverter","COIN VAL ES: " + coinval[x]);
                                Float value = (Float.valueOf(number) / coinval[x])* Float.valueOf(num.getText().toString());
                                price[x].setText(value + " " + SCoins[x].toUpperCase());
                            }
                        }



                    }
                });
                builder.setNegativeButton("Cancel", null);
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });



        // TERMINA ALERT DIALOG CLICKEANDO BOTON


   }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addnewcoin) {

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            });
            Intent intent = new Intent(this, ActivityAddCoins.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_deletecoin) {
         for (int x=2;x<Coins.length;x++){
        elim[x].setVisibility(View.VISIBLE);
         price[x].setVisibility(View.INVISIBLE);
         }
        activado=true;

            return true;
        }

        if (id == R.id.action_refresh) {
            finish();
            startActivity(getIntent());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class ParseURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            try {
                Document doc = Jsoup.connect(strings[0]).userAgent("Mozilla").get();
                // Get document (HTML page) title
                Elements elements = doc.select("span.cmc-details-panel-price__price");
                for (Element e : elements) {
                   buffer.append(e.text().substring(1).replace(",",""));
                }

                elements = doc.select("span#ctl00_M_lblToAmount");
                for (Element e : elements) {
                    buffer.append(e.text().replace(".","").replace(",","."));}



            } catch (Throwable t) {
                t.printStackTrace();
            }
            return buffer.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    @Override
    public void onBackPressed() {
        // your code.
        if (activado==true){
            for (int x=2;x<Coins.length;x++){
                elim[x].setVisibility(View.INVISIBLE);
                price[x].setVisibility(View.VISIBLE);
            }
        activado=false;
        }
    }



}



