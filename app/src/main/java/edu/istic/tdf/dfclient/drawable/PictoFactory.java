package edu.istic.tdf.dfclient.drawable;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.UI.AutoScaleTextView;
import edu.istic.tdf.dfclient.domain.element.ElementType;
import edu.istic.tdf.dfclient.domain.element.IElement;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.domain.element.pointOfInterest.PointOfInterest;
import edu.istic.tdf.dfclient.drawable.element.DomainType;

/**
 * Factory for icon likes mean_other, water point, etc...
 * Created by guerin on 22/04/16.
 */
public class PictoFactory {

    private boolean isExternal = false;

    public enum ElementForm {

        // Mean
        MEAN("Véhicule", R.drawable.mean),
        MEAN_PLANNED("Véhicule prévu", R.drawable.mean_planned),
        MEAN_GROUP("Groupe de véhicules", R.drawable.mean_group),
        MEAN_GROUP_PLANNED("Groupe de véhicules prévu", R.drawable.mean_group_planned),
        MEAN_COLUMN("Colonne", R.drawable.mean_column),
        MEAN_COLUMN_PLANNED("Colonne prévue", R.drawable.mean_column_planned),

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
    private Role role = Role.DEFAULT;
    private ElementForm form = ElementForm.MEAN;
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
        this.setForm(element.getForm());
        // Check if SIG or not
        if(element.getType() == ElementType.POINT_OF_INTEREST){
            this.isExternal = ((PointOfInterest)element).isExternal();
        }
        this.setRole(element.getRole());
        this.setLabel(element.getName());
        return this;
    }

    public PictoFactory setRole(Role role){
        this.role = role;
        return this;
    }

    public PictoFactory setLabel(String label){
        this.label = label;
        return this;
    }

    public PictoFactory setForm(ElementForm form){
        this.form = form;
        return this;
    }

    public PictoFactory setSize(int size){
        this.size = size;
        return this;
    }

    public Bitmap toBitmap(){

        // Icon
        Bitmap bitmap = getOptimizedBitmap();

        // Text
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fitTextToBitmap(paint, bitmap);
        Rect bounds = new Rect();
        paint.getTextBounds(this.label, 0, this.label.length(), bounds);

        Canvas canvas = new Canvas(bitmap);

        // Color of icon + text
        if(isExternal){
            canvas.drawColor(this.role.getDarkColor(), PorterDuff.Mode.SRC_IN);
            paint.setColor(this.role.getDarkColor());
        } else {
            canvas.drawColor(this.role.getColor(), PorterDuff.Mode.SRC_IN);
            paint.setColor(this.role.getColor());
        }

        if(this.form == ElementForm.AIRMEAN
                || this.form == ElementForm.AIRMEAN_PLANNED
                || this.form == ElementForm.SOURCE
                || this.form == ElementForm.TARGET
                || this.form == ElementForm.WATERPOINT
                || this.form == ElementForm.WATERPOINT_SUPPLY
                || this.form == ElementForm.WATERPOINT_SUSTAINABLE){
            paint.setColor(this.role.getDarkColor());
            canvas.drawText(this.label, 4, bounds.height()/2 + bitmap.getScaledHeight(canvas)/2, paint);
        } else {
            canvas.drawText(this.label, 4, bounds.height()/2 + bitmap.getScaledHeight(canvas)/2, paint);
        }

        return bitmap;
    }

    private void fitTextToBitmap(Paint paint, Bitmap bitmap) {

        int textSize = 40;
        paint.setTextSize(textSize);
        if(paint.measureText(this.label) > (bitmap.getWidth() - 8)){
            while(paint.measureText(this.label) > (bitmap.getWidth() - 8)){
                textSize--;
                paint.setTextSize(textSize);
            }
        }

    }

    private Bitmap getOptimizedBitmap() {

        Resources resources = context.getResources();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(resources, this.form.getDrawable(), options);
        options.inSampleSize = calculateInSampleSize(options, this.size, this.size);
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeResource(resources, this.form.getDrawable(), options);

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }

       return bitmap.copy(bitmapConfig, true);
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

    public static ElementForm getFormPlanned(ElementForm elementForm){
        switch (elementForm){
            case MEAN:
            case MEAN_PLANNED:
                return ElementForm.MEAN_PLANNED;
            case MEAN_GROUP:
            case MEAN_GROUP_PLANNED:
                return ElementForm.MEAN_GROUP_PLANNED;
            case MEAN_COLUMN:
            case MEAN_COLUMN_PLANNED:
                return ElementForm.MEAN_COLUMN_PLANNED;
            // Mean other
            case MEAN_OTHER:
            case MEAN_OTHER_PLANNED:
                return ElementForm.MEAN_OTHER_PLANNED;
            // Airmean
            case AIRMEAN:
            case AIRMEAN_PLANNED:
                return ElementForm.AIRMEAN_PLANNED;
            default:
                return elementForm;
        }
    }

    public static ElementForm getFormNotPlanned(ElementForm elementForm){
        switch (elementForm){
            case MEAN:
            case MEAN_PLANNED:
                return ElementForm.MEAN_PLANNED;
            case MEAN_GROUP:
            case MEAN_GROUP_PLANNED:
                return ElementForm.MEAN_GROUP_PLANNED;
            case MEAN_COLUMN:
            case MEAN_COLUMN_PLANNED:
                return ElementForm.MEAN_COLUMN_PLANNED;
            // Mean other
            case MEAN_OTHER:
            case MEAN_OTHER_PLANNED:
                return ElementForm.MEAN_OTHER_PLANNED;
            // Airmean
            case AIRMEAN:
            case AIRMEAN_PLANNED:
                return ElementForm.AIRMEAN_PLANNED;
            default:
                return elementForm;
        }
    }


}
