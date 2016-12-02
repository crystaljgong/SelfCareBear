package cs4720.self_care_bear;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class GiftShop extends AppCompatActivity implements GiftItemFragment.OnListFragmentInteractionListener {

    public static ArrayList<GiftItem> allGift;
    public static GiftItemFragment frag;
    public static TextView spendPts;
    //for camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    //    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_shop);

        allGift = MainScreen.ALL_GIFTS;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (frag == null) {
            frag = GiftItemFragment.newInstance(allGift);
            Log.i("onCreate", "frag was made");
            getSupportFragmentManager().beginTransaction().add(R.id.giftRV, frag).commit();
        }

        spendPts = (TextView) findViewById(R.id.spendingPoints);
        spendPts.setText("Your PandaPoints: " + MainScreen.P_POINTS);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        saveInstanceState.putParcelableArrayList("gifts", allGift);

    }

    @Override
    public void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);
        allGift = saveInstanceState.getParcelableArrayList("gifts");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView tempSelfieView = (ImageView) findViewById(R.id.tempSelfieView);
            tempSelfieView.setImageBitmap(imageBitmap);
            //TODO:  attempt to put image in makes this crash
//            ImageView photoVIew = (ImageView) findViewById(R.id.photo);
//            photoVIew.setImageBitmap(imageBitmap);

            Intent passBackToMain = new Intent();
            passBackToMain.putExtras(extras);
            setResult(RESULT_OK, passBackToMain);
            finish();
        }
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
