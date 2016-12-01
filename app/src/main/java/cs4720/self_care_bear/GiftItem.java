package cs4720.self_care_bear;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by annie_000 on 11/29/2016.
 */

public class GiftItem implements Parcelable {

    public int imgID;
    public String giftName;
    public int giftPoints;
    public boolean bought;

    public GiftItem() {
    }

    public GiftItem(int img, String name, int point, boolean buy) {
        this.imgID = img;
        this.giftName = name;
        this.giftPoints = point;
        this.bought = buy;
    }

    protected GiftItem(Parcel in) {
        giftName = in.readString();
        giftPoints = in.readInt();
        bought = in.readByte() != 0;
    }

    public static final Creator<GiftItem> CREATOR = new Creator<GiftItem>() {
        @Override
        public GiftItem createFromParcel(Parcel in) {
            return new GiftItem(in);
        }

        @Override
        public GiftItem[] newArray(int size) {
            return new GiftItem[size];
        }
    };

    public int getImg() {
        return imgID;
    }

    public void setImg(int img) {
        this.imgID = img;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getGiftPoints() {
        return giftPoints;
    }

    public void setGiftPoints(int giftPoints) {
        this.giftPoints = giftPoints;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(giftName);
        dest.writeInt(giftPoints);
        dest.writeByte((byte) (bought ? 1 : 0));
    }
}
