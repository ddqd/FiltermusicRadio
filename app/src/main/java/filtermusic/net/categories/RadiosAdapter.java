package filtermusic.net.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import filtermusic.net.R;
import filtermusic.net.common.model.Category;
import filtermusic.net.common.model.Radio;

/**
 * Adapter for the list of radios
 */
public class RadiosAdapter extends ArrayAdapter<Radio> {

    private LayoutInflater mLayoutInflater;

    public RadiosAdapter(Context context, int resource) {
        super(context, resource);
        init(context);
    }

    public RadiosAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        init(context);
    }

    public RadiosAdapter(Context context, int resource, Radio[] objects) {
        super(context, resource, objects);
        init(context);
    }

    public RadiosAdapter(Context context, int resource, int textViewResourceId, Radio[] objects) {
        super(context, resource, textViewResourceId, objects);
        init(context);
    }

    public RadiosAdapter(Context context, int resource, List<Radio> objects) {
        super(context, resource, objects);
        init(context);
    }

    public RadiosAdapter(Context context, int resource, int textViewResourceId, List<Radio> objects) {
        super(context, resource, textViewResourceId, objects);
        init(context);
    }

    private void init(Context context){
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mLayoutInflater.inflate(R.layout.radio_list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        Radio radio = getItem(position);
        holder.title.setText(radio.getTitle());

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.radio_title) TextView title;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
