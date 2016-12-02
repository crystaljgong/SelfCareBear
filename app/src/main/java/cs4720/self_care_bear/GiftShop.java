package cs4720.self_care_bear;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GiftShop extends AppCompatActivity implements GiftItemFragment.OnListFragmentInteractionListener{

    public static ArrayList<GiftItem> allGift;
    public static GiftItemFragment frag;
    public static TextView spendPts;


//    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_shop);

        allGift = MainScreen.ALL_GIFTS;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frag = GiftItemFragment.newInstance(allGift);
        Log.i("onCreate", "frag was made");
        getSupportFragmentManager().beginTransaction().add(R.id.giftRV, frag).commit();

        spendPts = (TextView) findViewById(R.id.spendingPoints);
        spendPts.setText("Your PandaPoints: " + MainScreen.P_POINTS);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //TODO: Do things idk
    @Override
    public void onListFragmentInteraction(GiftItem item) {

    }
}
