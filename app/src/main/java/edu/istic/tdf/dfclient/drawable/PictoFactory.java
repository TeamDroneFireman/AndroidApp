package edu.istic.tdf.dfclient.drawable;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.AutoScaleTextView;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.drawable.element.DomainType;

/**
 * Factory for icon likes mean_other, water point, etc...
 * Created by guerin on 22/04/16.
 */
public class PictoFactory {

    public enum ElementForm {

        // Mean
        MEAN("Véhicule", R.drawable.mean_other),
        MEAN_PLANNED("Véhicule prévu", R.drawable.mean_planned),
        MEAN_GROUP("Groupe de véhicules", R.drawable.mean_group),
        MEAN_COLUMN("Colonne", R.drawable.mean_column),

        // Mean other
        MEAN_OTHER("Moyen d'intervention", R.drawable.mean_other),
        MEAN_OTHER_PLANNED("Moyen d'intervention prévu", R.drawable.mean_other_planned),

        // Waterpoint
        WATERPOINT("Prise d'eau non pérenne", R.drawable.waterpoint),
        WATERPOINT_SUPPLY("Point de ravitaillement", R.drawable.waterpoint_supply),
        WATERPOINT_SUSTAINABLE("Prise d'eau pérenne", R.drawable.waterpoint_sustainable),

        // Airmean
        AIRMEAN("Moyen aérien", R.drawable.airmean),
        AIRMEAN_PLANNED("Moyen aérien prévu", R.drawable.airmean_planned),

        // Sources / target
        SOURCE("Source", R.drawable.source),
        TARGET("Cible", R.drawable.target);

        private String label;
        private int drawable;
        ElementForm(String label, int drawable){
            this.label = label;
            this.drawable = drawable;
        }

        public int getDrawable(){
            return drawable;
        }
        public String getLabel(){
            return label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    /**
     * The fragment context
     */
    private Context context;

    /**
     * Picto attributes
     */
    private int color = Role.DEFAULT.getColor();
    private int drawable = ElementForm.MEAN.getDrawable();
    private int size = 64;
    private String label = "Test";
    private DomainType domainType = DomainType.INTERVENTIONMEAN;

    /**
     * Constructor with parameter the fragment context
     * @param context
     */
    public PictoFactory(Context context){
        this.context=context;
    }

    public static PictoFactory createPicto(Context context){
        return new PictoFactory(context);
    }

    public PictoFactory setElement(IElement element){
        this.setDrawable(element.getForm().getDrawable());
        this.setColor(element.getRole().getColor());
        this.setLabel(element.getName());
        return this;
    }

    public PictoFactory setColor(int color){
        this.color = color;
        return this;
    }

    public PictoFactory setLabel(String label){
        this.label = label;
        return this;
    }

    public PictoFactory setDrawable(int drawable){
        this.drawable = drawable;
        return this;
    }

    public PictoFactory setSize(int size){
        this.size = size;
        return this;
    }

    public Drawable toDrawable(){
        Drawable drawable = ContextCompat.getDrawable(context, this.drawable);

        drawable.setColorFilter(this.color, PorterDuff.Mode.MULTIPLY);
        drawable.setBounds(0, 0, size, size);
        return drawable;
    }

    public Bitmap toBitmap(){
/*
        Bitmap bitmap = getOptimizedBitmap();
        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.WHITE);
        // text size in pixels
        int textSize = 30;
        paint.setTextSize((int) (textSize));

        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);
        if(paint.measureText(this.label) > bitmap.getWidth()){
            while(paint.measureText(this.label) > bitmap.getWidth()){
                textSize--;
                paint.setTextSize((int) (textSize));
            }
        }
        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(this.label, 0, this.label.length(), bounds);
        int x = (bitmap.getWidth()) / 2;
        int y = (bitmap.getHeight()) / 2;

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(this.color);
        paint.setColorFilter(new PorterDuffColorFilter(this.color, PorterDuff.Mode.MULTIPLY));
        //canvas.drawColor(this.color);
        canvas.drawText(this.label, x, y, paint);
        return bitmap;
*/
        View view = getView();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);

        ImageView imageView = (ImageView) view.findViewById(R.id.icon_image_view);
        imageView.setImageResource(this.drawable);
        imageView.setColorFilter(this.color);

        AutoScaleTextView textView = (AutoScaleTextView) view.findViewById(R.id.icon_text);
        //textView.setText(this.label);
        textView.setTextColor(this.color);

        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(this.color);

        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        int x = (bitmap.getWidth())/2;
        int y = (bitmap.getHeight())/2;

        canvas.drawText(label, x, y, paint);
        view.draw(canvas);

        return bitmap;

    }

    private Bitmap getOptimizedBitmap() {

        Resources resources = context.getResources();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(resources, this.drawable, options);
        options.inSampleSize = calculateInSampleSize(options, this.size, this.size);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, this.drawable, options);

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }

       return bitmap.copy(bitmapConfig, true);
    }

    /**
     *
     * @return
     */
    private View getView(){
        return  ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.icon_layout, null);
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
