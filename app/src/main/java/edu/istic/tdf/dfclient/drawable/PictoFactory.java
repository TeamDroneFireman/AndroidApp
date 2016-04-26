package edu.istic.tdf.dfclient.drawable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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
 * Factory for icon likes mean, water point, etc...
 * Created by guerin on 22/04/16.
 */
public class PictoFactory {

    private enum ElementColor{

        PEOPLE(0xFFFF00FF),
        FIRE(0xFFFF00FF),
        WATER(0xFFFF00FF),
        SPECIFIC(0xFFFF00FF),
        DEFAULT(0xFFFF00FF),
        COMMAND();

        private int color;

        private ElementColor(int color){
            this.color = color;
        }

        public int getColorByRole(Role role){
            return 0;
        }

        public int getColor(){
            return color;
        }

        @Override
        public String toString() {
            return Integer.toString(color);
        }
    }

    private enum ElementDrawable{
        MEAN(R.drawable.mean),
        TMP_MEAN(R.drawable.tmp_mean),
        ISOLATED(R.drawable.isolated),
        TMP_ISOLATED(R.drawable.tmp_isolated),
        WATERPOINT(R.drawable.waterpoint),
        SUPPLY_WATERPOINT(R.drawable.supply_waterpoint),
        SUSTAINABLE_WATERPOINT(R.drawable.sustainable_waterpoint),
        GROUP(R.drawable.group),
        COLUMN(R.drawable.column);

        private int drawable;
        private ElementDrawable(int drawable){
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
    private int color = defaultColor;
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

    public static PictoFactory createPicto(Context context, IElement element){
        PictoFactory pictoFactory = new PictoFactory(context).setColor();
        return new PictoFactory(context);
    }

    public PictoFactory setColor(int color){
        this.color = color;
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

    }

    public Drawable toDrawable(){

    }

    public Bitmap toBitmap(){

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
        switch (role){
            case NONE:return Color.BLACK;
            default:return Color.WHITE;
        }

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
        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.sensible);
        return drawable;
    }

    /**
     * Trieangle /\
     * @return
     */
    private Drawable getDefaultRiskForm() {
        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.risk);
        return drawable;
    }

    /**
     * Circle
     * @return
     */
    private Drawable getDefaulWaterPointForm() {

        Drawable drawable=ContextCompat.getDrawable(context, R.drawable.water_point_drawable);
        return drawable;
    }

    /**
     * Square
     * @return
     */
    private Drawable getDefaultInterventionMeanForm() {

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.mean);
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
