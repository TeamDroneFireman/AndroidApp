package edu.istic.tdf.dfclient.drawable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.istic.tdf.dfclient.R;
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
        MEAN(R.drawable.mean_other),
        MEAN_PLANNED(R.drawable.mean_planned),
        MEAN_GROUP(R.drawable.mean_group),
        MEAN_COLUMN(R.drawable.mean_column),

        // Mean other
        MEAN_OTHER(R.drawable.mean_other),
        MEAN_OTHER_PLANNED(R.drawable.mean_other_planned),

        // Waterpoint
        WATERPOINT(R.drawable.waterpoint),
        WATERPOINT_SUPPLY(R.drawable.waterpoint_supply),
        WATERPOINT_SUSTAINABLE(R.drawable.waterpoint_sustainable),

        // Airmean
        AIRMEAN(R.drawable.airmean),
        AIRMEAN_PLANNED(R.drawable.airmean_planned),

        // Sources / target
        SOURCE(R.drawable.source),
        TARGET(R.drawable.target);

        private int drawable;
        private ElementForm(int drawable){
            this.drawable = drawable;
        }

        public int getDrawable(){
            return drawable;
        }

    }

    /**
     * The color of the default icon
     */
    private int defaultColor=Color.WHITE;

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
        return this;
    }

    public PictoFactory setColor(int color){
        this.color = color;
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

    public PictoFactory setDomainType(DomainType domainType){
        this.domainType = domainType;
        return this;
    }

    public View toView(){
        return getView();
    }

    public Drawable toDrawable(){
        return ContextCompat.getDrawable(context, this.drawable);
    }

    public ImageView toImageView(){

        ImageView imageView = new ImageView(this.context);
        imageView.setImageResource(this.drawable);
        imageView.setColorFilter(this.color);

        return imageView;
    }

    public Bitmap toBitmap(){

        View view = getView();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);

        ImageView imageView = (ImageView) view.findViewById(R.id.icon_image_view);
        imageView.setImageResource(this.drawable);
        imageView.setColorFilter(this.color);

        TextView textView = (TextView) view.findViewById(R.id.num_txt);
        textView.setText("");
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
    /**
     * get the icon to the type "View" for adroid manipulation
     * @param element: element
     * @param domainType: the type of the element
     * @return
     */
    public View getIconView(IElement element,DomainType domainType){
        Drawable defaultForm=getDefaultPicto(domainType);
        View view=getView();
        ImageView imageView=getImageView(defaultForm,getView());

        //transform the color etc...
        imageView.setColorFilter(getColor(element.getRole()));

        return view;

    }

    /**
     * get the icon for google map
     * @param element
     * @param domainType
     * @return
     */
    public Bitmap getBitMap(IElement element,DomainType domainType){
        return getBitmap(getIconView(element,domainType));

    }

    /**For default form**/

    /**
     * get default icon to type View for android manipulation
     * @param domainType
     * @return
     */
    public View getDefaultIconView(DomainType domainType){

        Drawable drawable=getDefaultPicto(domainType);
        View marker = getView();
        ImageView imageView=getImageView(drawable,marker);

        imageView.setColorFilter(defaultColor);

        /*TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
        numTxt.setText("CDC 2");
*/
        return marker;
    }

    public static Drawable getDefaultPicto(Context context, DomainType domainType){

        PictoFactory pictoFactory = new PictoFactory(context);
        return pictoFactory.getDefaultPicto(domainType);

    }

    /**
     * get default icon for google map
     * @param domainType
     * @return
     */
    public Bitmap getDefaultBitMap(DomainType domainType){

        View view=getDefaultIconView(domainType);
        return getBitmap(view);

    }

    /*** Methods to modify dynamically icon **/

    /**
     *
     * @param bitmap
     * @param string
     */
    public void setText(Bitmap bitmap, String string){

    }

    /**
     *
     * @param view
     * @param str
     */
    public void setText(View view,String str){
        ImageView imageView=getImageView(view);
        TextView numTxt = (TextView) view.findViewById(R.id.num_txt);
        numTxt.setText(str);
    }

    /**
     *
     * @param view
     * @param role
     */
    public void setColor(View view,Role role){
        ImageView imageView=getImageView(view);
        imageView.setColorFilter(getColor(role));
    }


    /** Method for create View or bitMap**/

    /**
     *
     * @param drawable
     * @param marker
     * @return
     */
    private ImageView getImageView(Drawable drawable, View marker) {
        ImageView imageView = (ImageView) marker.findViewById(R.id.icon_image_view);
        imageView.setImageDrawable(drawable);
        return imageView;
    }

    /**
     *
     * @param marker
     * @return
     */
    private ImageView getImageView(View marker){
        return (ImageView) marker.findViewById(R.id.icon_image_view);
    }

    /**
     *
     * @return
     */
    private View getView(){
        return  ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.icon_layout, null);
    }

    /**
     *
     * @param view
     * @return
     */
    private Bitmap getBitmap(View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    /**
     *
     * @param role
     * @return
     */
    private int getColor(Role role) {
        return role.getColor();
    }

    /**Get the icon form ***/

    /**
     *
     * @param domainType
     * @return
     */
    private Drawable getDefaultPicto(DomainType domainType) {
        switch (domainType){
            case DRONE: return getDefaultDroneForm();
            case INTERVENTIONMEAN:return getDefaultInterventionMeanForm();
            case WATERPOINT:return getDefaulWaterPointForm();
            case RISK:return getDefaultRiskForm();
            case SENSIBLEPOINT:return getDefaultSensibleForm();
        }

        return null;
    }


    /***
     * Triangle \/
     * @return
     */
    private  Drawable getDefaultSensibleForm() {
        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.target);
        return drawable;
    }

    /**
     * Trieangle /\
     * @return
     */
    private Drawable getDefaultRiskForm() {
        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.source);
        return drawable;
    }

    /**
     * Circle
     * @return
     */
    private Drawable getDefaulWaterPointForm() {

        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.waterpoint_sustainable);
        return drawable;
    }

    /**
     * Square
     * @return
     */
    private Drawable getDefaultInterventionMeanForm() {

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.mean_other);
        return drawable;
    }

    /**
     * AirMean = square with two triangles
     * @return
     */
    private Drawable getDefaultDroneForm() {
        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.airmean);
        return drawable;

    }
}
