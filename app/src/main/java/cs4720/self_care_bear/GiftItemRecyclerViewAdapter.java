package cs4720.self_care_bear;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cs4720.self_care_bear.GiftItemFragment.OnListFragmentInteractionListener;
import cs4720.self_care_bear.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GiftItemRecyclerViewAdapter extends RecyclerView.Adapter<GiftItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<GiftItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;


    public GiftItemRecyclerViewAdapter(Context context, ArrayList<GiftItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;
        Log.i("constructor", "adapter made");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_giftitem, parent, false);
        Log.i("onCreateViewHolder", "viewholder made");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getGiftName());
        holder.mContentView.setText("" + mValues.get(position).getGiftPoints() + " PandaPoints");
        holder.mImageView.setImageResource(mValues.get(position).getImg());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        Log.i("onBindViewHolder", "successful");
    }

    @Override
    public int getItemCount() {
        Log.i("getItemCount", "was called");
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public GiftItem mItem;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) mView.findViewById(R.id.giftName);
            mContentView = (TextView) mView.findViewById(R.id.giftPoints);
            mImageView = (ImageView) mView.findViewById(R.id.giftImage);
            Log.i("constructor", "viewholder made");
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItem.isBought() == true) {
                        Toast.makeText(v.getContext(), "You already bought this gift!", Toast.LENGTH_SHORT).show();
                    } else {

                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_buy_gift);
                    dialog.setTitle("Buy Gift");
                    Log.i("onClick", "dialog just set up");
                    TextView text = (TextView) dialog.findViewById(R.id.buyText);
                    text.setText("Would you like to purchase\n" + mIdView.getText() + "\nfor " + mContentView.getText());
                    Log.i("onClick", "buy text set");
                    ImageView img = (ImageView) dialog.findViewById(R.id.buyImg);
                    img.setImageDrawable(mImageView.getDrawable());
                    Log.i("onClick", "gift img set");
                    Button confBut = (Button) dialog.findViewById(R.id.buyConfirm);
                    Log.i("onClick", "confirm button... made?");
                    confBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("onClick", "setting confirm button onClick");

                            //TODO: add buy functionality lol
                            int cost = mItem.getGiftPoints();
                            if (mItem.isBought() == true) {
                                Toast.makeText(v.getContext(), "You already bought this gift!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                if (cost <= MainScreen.P_POINTS) {
                                    Toast.makeText(v.getContext(), "Gift has been bought!", Toast.LENGTH_SHORT).show();
                                    MainScreen.P_POINTS -= cost;
                                    if (mItem.getGiftName().equals("Snack")) {
                                        MainScreen.dialogue.setText(mContext.getResources().getString(R.string.snack));
                                        MainScreen.snack.setVisibility(View.VISIBLE);
                                    } else if (mItem.getGiftName().equals("Flowers")) {
                                        MainScreen.dialogue.setText((mContext.getResources().getString(R.string.flowers)));
                                        MainScreen.flower.setVisibility(View.VISIBLE);
                                    } else if (mItem.getGiftName().equals("Umbrella")) {
                                        MainScreen.dialogue.setText((mContext.getResources().getString(R.string.umbrella)));
                                        MainScreen.umbrella.setVisibility(View.VISIBLE);
                                    } else if (mItem.getGiftName().equals("Power Drill")) {
                                        MainScreen.dialogue.setText((mContext.getResources().getString(R.string.powerDrill)));
                                        MainScreen.drill.setVisibility(View.VISIBLE);
                                    } else if (mItem.getGiftName().equals("Fireworks")) {
                                        MainScreen.dialogue.setText((mContext.getResources().getString(R.string.fireworks)));
                                        MainScreen.fire.setVisibility(View.VISIBLE);
                                    } else {
                                        MainScreen.dialogue.setText((mContext.getResources().getString(R.string.photo)));
                                        MainScreen.camera.setVisibility(View.VISIBLE);
                                        MainScreen.photo.setVisibility(View.VISIBLE);
                                    }
                                    mItem.setBought(true);
                                    mView.setAlpha((float) 0.4);
                                    GiftShop.spendPts.setText("Your PandaPoints: " + MainScreen.P_POINTS);
                                    MainScreen.pointsStatus.setText("Panda Points: " + MainScreen.P_POINTS);
                                    Log.i("onClick", "item successfully bought!");
                                    mView.setEnabled(false);
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(v.getContext(), "You don't have enough points to buy this!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }

                        }
                    });
                    Log.i("onClick", "confirm button set");
                    Button canBut = (Button) dialog.findViewById(R.id.buyDecline);
                    canBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Log.i("onClick", "cancel button set");
                    dialog.show();

                }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
