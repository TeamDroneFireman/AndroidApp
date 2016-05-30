package edu.istic.tdf.dfclient.UI.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.istic.tdf.dfclient.R;
import edu.istic.tdf.dfclient.domain.element.Role;
import edu.istic.tdf.dfclient.drawable.PictoFactory;

/**
 * Created by Alexandre on 28/04/2016.
 */
public class RoleArrayAdapter extends ArrayAdapter<Role> {

    private List<Role> roles = new ArrayList<>();
    private LayoutInflater inflater;

    public RoleArrayAdapter(Context context, int resource, Role[] roles) {
        super(context, resource, roles);
    }

    public RoleArrayAdapter(Context context, Role[] roles){
        super(context, R.layout.role_spinner_item, roles);

        for(Role role : roles){
            this.roles.add(role);
        }
        inflater = LayoutInflater.from(context);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent){

        View row = convertView;
        ViewHolder holder = null;

        //inflate row layout
        if(row==null){
            row =inflater.inflate(R.layout.role_spinner_item, parent,false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }else{
            holder =(ViewHolder) row.getTag();
        }

        Role role = roles.get(position);

        //show data
        holder.textViewTitle.setText(role.getTitle());

        holder.imageViewIcon.setImageBitmap(PictoFactory.createPicto(getContext()).setLabel("").setForm(PictoFactory.ElementForm.WATERPOINT_SUSTAINABLE).toBitmap());
        holder.imageViewIcon.setColorFilter(role.getColor());

        return row;

    }


    class ViewHolder {
        ImageView imageViewIcon;
        TextView textViewTitle ;

        public ViewHolder(View v){
            imageViewIcon =(ImageView) v.findViewById(R.id.spinner_icon);
            textViewTitle =(TextView) v.findViewById(R.id.spinner_name);
        }
    }

}
