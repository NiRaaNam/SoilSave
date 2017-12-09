package com.gistda.niraanam.soilsave;

/**
 * Created by NiRaaNam on 07-Dec-17.
 */
import android.content.Context;
import java.util.List;
import android.app.Activity;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListAdapterClass extends BaseAdapter{

    Context context;
    List<Soil> valueList;

    public ListAdapterClass(List<Soil> listValue, Context context)
    {
        this.context = context;
        this.valueList = listValue;
    }

    @Override
    public int getCount()
    {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.listview_item_soil, null);

            viewItem.TextViewNumMark = (TextView)convertView.findViewById(R.id.txtSoil);

            viewItem.TextViewModified = (TextView)convertView.findViewById(R.id.txtModified);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem) convertView.getTag();
        }

        viewItem.TextViewNumMark.setText(valueList.get(position).num_mark);

        viewItem.TextViewModified.setText(valueList.get(position).last_modified);

        return convertView;
    }
}

class ViewItem
{
    TextView TextViewNumMark;

    TextView TextViewModified;

}
